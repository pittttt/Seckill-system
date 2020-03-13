package com.pitt.kill.model.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @Data 用来获取get、set等方法（使用后获取不到属性，猜测是lombok版本问题）
 * @author pitt
 *
 */
// @Data
public class ItemKill {
	private Integer id;

	private Integer itemId;

	private Integer total;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date startTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date endTime;

	private Byte isActive;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	// 和数据库中查询属性对应
	private String itemName;

	// 判断是否可以秒杀
	private Integer canKill;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Byte getIsActive() {
		return isActive;
	}

	public void setIsActive(Byte isActive) {
		this.isActive = isActive;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getCanKill() {
		return canKill;
	}

	public void setCanKill(Integer canKill) {
		this.canKill = canKill;
	}

	@Override
	public String toString() {
		return "\nItemKill [id=" + id + ", itemId=" + itemId + ", total=" + total + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", isActive=" + isActive + ", createTime=" + createTime + ", itemName="
				+ itemName + ", canKill=" + canKill + "]";
	}

}