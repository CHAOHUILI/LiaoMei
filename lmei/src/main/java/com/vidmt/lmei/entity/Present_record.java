package com.vidmt.lmei.entity;
/**
 * 用户表实体
 * @author Administrator
 *
 */

public class Present_record {
	private int id;//编号

	private String create_date;//购买时间
	private int buy_id;//购买人id
	private int present_id;//礼物id
	private int update_date;//送人时间
	private int sell_id;//接收人
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String createDate) {
		create_date = createDate;
	}
	public int getBuy_id() {
		return buy_id;
	}
	public void setBuy_id(int buyId) {
		buy_id = buyId;
	}
	public int getPresent_id() {
		return present_id;
	}
	public void setPresent_id(int presentId) {
		present_id = presentId;
	}
	public int getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(int updateDate) {
		update_date = updateDate;
	}
	public int getSell_id() {
		return sell_id;
	}
	public void setSell_id(int sellId) {
		sell_id = sellId;
	}
	public Present_record(int id, String createDate, int buyId, int presentId,
			int updateDate, int sellId) {
		super();
		this.id = id;
		create_date = createDate;
		buy_id = buyId;
		present_id = presentId;
		update_date = updateDate;
		sell_id = sellId;
	}
	public Present_record() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Present_record [buy_id=" + buy_id + ", create_date="
				+ create_date + ", id=" + id + ", present_id=" + present_id
				+ ", sell_id=" + sell_id + ", update_date=" + update_date + "]";
	}
	
	 
}