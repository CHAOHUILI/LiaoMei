package com.vidmt.lmei.entity;

import java.util.Date;



//谁看过我
public class ReadMeRecord {
	private Integer id;
	private Integer persion_id;//登录用户id
	private Integer other_id;//看过我的用户id

	private String update_date;//看我的时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPersion_id() {
		return persion_id;
	}

	public void setPersion_id(Integer persionId) {
		persion_id = persionId;
	}

	public Integer getOther_id() {
		return other_id;
	}

	public void setOther_id(Integer otherId) {
		other_id = otherId;
	}

	public String getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(String updateDate) {
		update_date = updateDate;
	}

	
	
}