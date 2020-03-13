package com.pitt.kill.server.dto;

import java.io.Serializable;
import java.util.Arrays;

public class MailDto implements Serializable {
	// 邮件主题
	private String subject;
	// 邮件内容
	private String content;
	// 接收人
	private String[] tos;

	public MailDto() {
		super();
	}

	public MailDto(String subject, String content, String[] tos) {
		super();
		this.subject = subject;
		this.content = content;
		this.tos = tos;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String[] getTos() {
		return tos;
	}

	public void setTos(String[] tos) {
		this.tos = tos;
	}

	@Override
	public String toString() {
		return "MailDto [subject=" + subject + ", content=" + content + ", tos=" + Arrays.toString(tos) + "]";
	}

}
