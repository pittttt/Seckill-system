package com.pitt.kill.server.service;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.pitt.kill.server.dto.MailDto;

/**
 * 发送邮件服务
 * 
 * @author pitt
 *
 */
@Service
@EnableAsync
public class MailService {

	private static final Logger log = LoggerFactory.getLogger(RabbitReceiverService.class);
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private Environment env;

	/**
	 * 发送简单文本
	 */
	@Async
	public void sendSimpleEmail(final MailDto dto) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(env.getProperty("mail.send.from"));
			message.setTo(dto.getTos());
			message.setSubject(dto.getSubject());
			message.setText(dto.getContent());
			mailSender.send(message);
			log.info("发送简单文本成功！");
		} catch (Exception e) {
			log.error("发送简单文本-发生异常:", e.fillInStackTrace());
		}
	}

	/**
	 * 发送复杂邮件信息
	 */
	@Async
	public void sendHTMLMail(final MailDto dto) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "utf-8");
			mimeMessageHelper.setFrom(env.getProperty("mail.send.from"));
			mimeMessageHelper.setTo(dto.getTos());
			mimeMessageHelper.setSubject(dto.getSubject());
			mimeMessageHelper.setText(dto.getContent(), true);
			mailSender.send(message);
			log.info("发送复杂邮件信息成功！");
		} catch (Exception e) {
			log.error("发送复杂邮件信息-发生异常:", e.fillInStackTrace());
		}
	}
}
