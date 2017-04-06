package com.vidmt.lmei.entity;

import java.util.Date;

/*
 * 广告点击率表
 */

public class ClickRate  {
    private Integer id;//广告点击率编号
    private int p_id;//用户编号
    private int ad_id;//用户编号
    private int clicknum;//点击次数

    private String createdate;//创建时间
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getP_id() {
		return p_id;
	}
	public void setP_id(int pId) {
		p_id = pId;
	}
	public int getAd_id() {
		return ad_id;
	}
	public void setAd_id(int adId) {
		ad_id = adId;
	}
	public int getClicknum() {
		return clicknum;
	}
	public void setClicknum(int clicknum) {
		this.clicknum = clicknum;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createDate) {
		createdate = createDate;
	} 
    
}