package com.pitt.kill.server.service.impl;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.joda.time.DateTime;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.pitt.kill.model.entity.ItemKill;
import com.pitt.kill.model.entity.ItemKillSuccess;
import com.pitt.kill.model.mapper.ItemKillMapper;
import com.pitt.kill.model.mapper.ItemKillSuccessMapper;
import com.pitt.kill.server.enums.SysConstant;
import com.pitt.kill.server.service.IKillService;
import com.pitt.kill.server.service.RabbitSenderService;
import com.pitt.kill.server.utils.RandomUtil;
import com.pitt.kill.server.utils.SnowFlake;

@Service
public class KillService implements IKillService {

	@Autowired
	ItemKillSuccessMapper itemKillSuccessMapper;

	@Autowired
	ItemKillMapper itemKillMapper;

	@Autowired
	RabbitSenderService rabbitSenderService;

	/**
	 * 商品秒杀核心业务逻辑的处理-无分布式锁
	 */
	@Override
	public Boolean killItem(Integer killId, Integer userId) throws Exception {
		Boolean result = false;
		// 判断当前用户是否已经抢购过此商品
		if (itemKillSuccessMapper.countByKillUserId(killId, userId) <= 0) {
			// 查询商品详情
			ItemKill itemKill = itemKillMapper.selectById(killId);
			// 判断是否可以秒杀
			if (itemKill != null && itemKill.getCanKill() == 1 && itemKill.getTotal() > 0) {
				// 扣库存
				int res = itemKillMapper.updateKillItem(killId);

				// 如果扣库存成功，生成秒杀成功订单，同时通知用户秒杀成功消息
				if (res > 0) {
					commonRecordKillSuccessInfo(itemKill, userId);
					result = true;
				}
			}

		} else {
			throw new Exception("您已经抢购过该商品了！");
		}
		return result;
	}

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 商品秒杀核心业务逻辑的处理-redis的分布式锁
	 * 
	 * @param killId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public Boolean killItemV3(Integer killId, Integer userId) throws Exception {
		Boolean result = false;

		if (itemKillSuccessMapper.countByKillUserId(killId, userId) <= 0) {

			// 借助Redis的原子操作实现分布式锁-对共享操作-资源进行控制
			ValueOperations valueOperations = stringRedisTemplate.opsForValue();
			final String key = new StringBuffer().append(killId).append(userId).append("-RedisLock").toString();
			final String value = RandomUtil.generateOrderCode();
			Boolean cacheRes = valueOperations.setIfAbsent(key, value); // luna脚本提供“分布式锁服务”，就可以写在一起
			// redis部署节点宕机了
			if (cacheRes) {
				stringRedisTemplate.expire(key, 30, TimeUnit.SECONDS);

				try {
					ItemKill itemKill = itemKillMapper.selectById(killId);
					if (itemKill != null && 1 == itemKill.getCanKill() && itemKill.getTotal() > 0) {
						int res = itemKillMapper.updateKillItem(killId);
						if (res > 0) {
							commonRecordKillSuccessInfo(itemKill, userId);

							result = true;
						}
					}
				} catch (Exception e) {
					throw new Exception("还没到抢购日期、已过了抢购时间或已被抢购完毕！");
				} finally {
					if (value.equals(valueOperations.get(key).toString())) {
						stringRedisTemplate.delete(key);
					}
				}
			}
		} else {
			throw new Exception("Redis-您已经抢购过该商品了!");
		}
		return result;
	}

	@Autowired
	private RedissonClient redissonClient;

	/**
	 * 商品秒杀核心业务逻辑的处理-redisson的分布式锁
	 *
	 * @param killId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public Boolean killItemV4(Integer killId, Integer userId) throws Exception {
		Boolean result = false;

		final String lockKey = new StringBuffer().append(killId).append(userId).append("-RedissonLock").toString();
		RLock lock = redissonClient.getLock(lockKey);

		try {
			Boolean cacheRes = lock.tryLock(30, 10, TimeUnit.SECONDS);
			if (cacheRes) {
				// 核心业务逻辑的处理
				if (itemKillSuccessMapper.countByKillUserId(killId, userId) <= 0) {
					ItemKill itemKill = itemKillMapper.selectById(killId);
					if (itemKill != null && 1 == itemKill.getCanKill() && itemKill.getTotal() > 0) {
						int res = itemKillMapper.updateKillItem(killId);
						if (res > 0) {
							commonRecordKillSuccessInfo(itemKill, userId);
							result = true;
						}
					}
				} else {
					throw new Exception("redisson-您已经抢购过该商品了!");
				}
			}
		} finally {
			lock.unlock();
			// lock.forceUnlock();//强制释放
		}
		return result;
	}

	@Autowired
	private CuratorFramework curatorFramework;

	// 节点路径
	private static final String pathPrefix = "/kill/zkLock/";

	/**
	 * 商品秒杀核心业务逻辑的处理-基于ZooKeeper的分布式锁
	 *
	 * @param killId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public Boolean killItemV5(Integer killId, Integer userId) throws Exception {
		Boolean result = false;

		InterProcessMutex mutex = new InterProcessMutex(curatorFramework, pathPrefix + killId + userId + "-lock");
		try {
			if (mutex.acquire(10L, TimeUnit.SECONDS)) {

				// 核心业务逻辑
				if (itemKillSuccessMapper.countByKillUserId(killId, userId) <= 0) {
					ItemKill itemKill = itemKillMapper.selectById(killId);
					if (itemKill != null && 1 == itemKill.getCanKill() && itemKill.getTotal() > 0) {
						int res = itemKillMapper.updateKillItem(killId);
						if (res > 0) {
							commonRecordKillSuccessInfo(itemKill, userId);
							result = true;
						}
					}
				} else {
					throw new Exception("zookeeper-您已经抢购过该商品了!");
				}
			}
		} catch (Exception e) {
			throw new Exception("还没到抢购日期、已过了抢购时间或已被抢购完毕！");
		} finally {
			if (mutex != null) {
				mutex.release();
			}
		}
		return result;
	}

	private SnowFlake snowFlake = new SnowFlake(2, 3);

	/**
	 * 通用方法
	 * 
	 * 记录用户秒杀成功后生成订单，并进行异步邮件消息通知
	 * 
	 * @param itemKill
	 * @param userId
	 */
	private void commonRecordKillSuccessInfo(ItemKill itemKill, Integer userId) {
		// 记录秒杀成功后生成的订单记录
		ItemKillSuccess itemKillSuccess = new ItemKillSuccess();
		String orderNo = String.valueOf(snowFlake.nextId());

		// itemKillSuccess.setCode(RandomUtil.generateOrderCode());//时间戳+随机数
		itemKillSuccess.setCode(orderNo);// 雪花算法
		itemKillSuccess.setItemId(itemKill.getItemId());
		itemKillSuccess.setKillId(itemKill.getId());
		itemKillSuccess.setUserId(userId.toString());
		itemKillSuccess.setStatus(SysConstant.OrderStatus.SuccessNotPayed.getCode().byteValue());
		itemKillSuccess.setCreateTime(DateTime.now().toDate());
		// 仿照单例模式的双重检验锁写法
		if (itemKillSuccessMapper.countByKillUserId(itemKill.getId(), userId) <= 0) {
			int res = itemKillSuccessMapper.insertSelective(itemKillSuccess);
			if (res > 0) {
				// 进行异步邮件消息的通知
				// rabbitmq+mail
				rabbitSenderService.sendKillSuccessEmailMsg(orderNo);

				// 入死信队列，用于失效超过指定TTL时间仍未支付的订单
				rabbitSenderService.sendKillSuccessOrderExpireMsg(orderNo);
			}
		}
	}
}
