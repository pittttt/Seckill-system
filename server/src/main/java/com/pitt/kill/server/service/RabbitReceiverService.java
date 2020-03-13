package com.pitt.kill.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.pitt.kill.model.dto.KillSuccessUserInfo;
import com.pitt.kill.model.entity.ItemKillSuccess;
import com.pitt.kill.model.mapper.ItemKillSuccessMapper;
import com.pitt.kill.server.dto.MailDto;

/**
 * RabbitMQ接收消息服务
 * 
 * @author pitt
 *
 */
@Service
public class RabbitReceiverService {
	private static final Logger log = LoggerFactory.getLogger(RabbitReceiverService.class);

	@Autowired
	private MailService mailService;
	@Autowired
	private Environment env;
	@Autowired
	private ItemKillSuccessMapper itemKillSuccessMapper;

	/**
	 * 异步邮件通知-接收消息
	 */
	@RabbitListener(queues = { "${mq.kill.item.success.email.queue}" }, containerFactory = "singleListenerContainer")
	public void consumeEmailMsg(KillSuccessUserInfo info) {
		try {
			log.info("异步邮件通知-接收消息:{}", info);
			// 进行邮件发送
			final String content = String.format(env.getProperty("mail.kill.item.success.content"), info.getItemName(),
					info.getCode());
			MailDto dto = new MailDto(env.getProperty("mail.kill.item.success.subject"), content,
					new String[] { info.getEmail() });
			// mailService.sendSimpleEmail(dto);
			mailService.sendHTMLMail(dto);
		} catch (Exception e) {
			log.error("异步邮件通知-接收消息发生异常:{}", e.fillInStackTrace());
		}
	}

	/**
	 * 用户秒杀成功后超时未支付-监听者
	 * 
	 */
	@RabbitListener(queues = {
			"${mq.kill.item.success.kill.dead.real.queue}" }, containerFactory = "multiListenerContainer")
	public void consumeExpireOrder(KillSuccessUserInfo info) {
		try {
			log.info("用户秒杀成功后超时未支付-监听者-接收消息:{}", info);

			if (info != null) {
				ItemKillSuccess entity = itemKillSuccessMapper.selectByPrimaryKey(info.getCode());
				if (entity != null && entity.getStatus().intValue() == 0) {
					itemKillSuccessMapper.expireOrder(info.getCode());
				}
			}
		} catch (Exception e) {
			log.error("用户秒杀成功后超时未支付-监听者-发生异常：", e.fillInStackTrace());
		}
	}
}
