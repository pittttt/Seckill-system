package com.pitt.kill.server.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class KillDto implements Serializable {
	@NotNull
	private Integer killId;

	private Integer userId;

	public Integer getKillId() {
		return killId;
	}

	public void setKillId(Integer killId) {
		this.killId = killId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "KillDto [killId=" + killId + ", userId=" + userId + "]";
	}

}
