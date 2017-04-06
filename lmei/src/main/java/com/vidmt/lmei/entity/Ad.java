package com.vidmt.lmei.entity;

import java.util.Date;


public class Ad {
    private Integer id;

    private String ad_photo;//广告图片

    private String ad_address;//广告指向地址

    private Integer ad_place;//广告出现位置

    private Integer ad_state;//广告状态

    private String ad_content;//广告内容

    private String create_date;//创建时间


    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAd_address() {
		return ad_address;
	}

	public void setAd_address(String adAddress) {
		ad_address = adAddress;
	}

	public Integer getAd_place() {
		return ad_place;
	}

	public void setAd_place(Integer adPlace) {
		ad_place = adPlace;
	}

	public Integer getAd_state() {
		return ad_state;
	}

	public void setAd_state(Integer adState) {
		ad_state = adState;
	}

	public String getAd_content() {
		return ad_content;
	}

	public void setAd_content(String adContent) {
		ad_content = adContent;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String createDate) {
		create_date = createDate;
	}

	public String getAd_photo() {
        return ad_photo;
    }

   

}