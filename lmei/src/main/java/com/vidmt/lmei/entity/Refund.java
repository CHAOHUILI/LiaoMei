package com.vidmt.lmei.entity;

import java.util.Date;



public class Refund {
	private Integer id;
	private Integer buy_id;  //购买方
	private Integer seller_id;  //服务方
	private Date create_date;   //创建时间
	private Integer token;   //退款爱意
	private String seller_name;//服务方名字
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBuy_id() {
		return buy_id;
	}
	public void setBuy_id(Integer buyId) {
		buy_id = buyId;
	}
	public Integer getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(Integer sellerId) {
		seller_id = sellerId;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date createDate) {
		create_date = createDate;
	}
	public Integer getToken() {
		return token;
	}
	public void setToken(Integer token) {
		this.token = token;
	}
	
	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String sellerName) {
		seller_name = sellerName;
	}

	

}