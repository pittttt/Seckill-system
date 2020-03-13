package com.pitt.kill.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pitt.kill.model.dto.KillSuccessUserInfo;
import com.pitt.kill.model.entity.ItemKillSuccess;
import com.pitt.kill.model.entity.ItemKillSuccessExample;

public interface ItemKillSuccessMapper {

	// 根据秒杀活动跟用户Id查询用户的抢购数量
	int countByKillUserId(@Param("killId") Integer killId, @Param("userId") Integer userId);

	// 根据雪花算法生成的code来查询信息
	KillSuccessUserInfo selectByCode(@Param("code") String code);

	// 失效超时的订单记录
	int expireOrder(@Param("code") String code);

	// 批量获取待处理的已保存订单记录
	List<ItemKillSuccess> selectExpireOrders();

	long countByExample(ItemKillSuccessExample example);

	int deleteByExample(ItemKillSuccessExample example);

	int deleteByPrimaryKey(String code);

	int insert(ItemKillSuccess record);

	int insertSelective(ItemKillSuccess record);

	List<ItemKillSuccess> selectByExample(ItemKillSuccessExample example);

	ItemKillSuccess selectByPrimaryKey(String code);

	int updateByExampleSelective(@Param("record") ItemKillSuccess record,
			@Param("example") ItemKillSuccessExample example);

	int updateByExample(@Param("record") ItemKillSuccess record, @Param("example") ItemKillSuccessExample example);

	int updateByPrimaryKeySelective(ItemKillSuccess record);

	int updateByPrimaryKey(ItemKillSuccess record);
}