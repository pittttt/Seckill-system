package com.pitt.kill.model.dto;

import java.io.Serializable;

import com.pitt.kill.model.entity.ItemKillSuccess;

public class KillSuccessUserInfo extends ItemKillSuccess implements Serializable {

	private String userName;

	private String phone;

	private String email;

	private String itemName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Override
	public String toString() {
		return super.toString() + "\nKillSuccessUserInfo [userName=" + userName + ", phone=" + phone + ", email="
				+ email + ", itemName=" + itemName + "]";
	}
}