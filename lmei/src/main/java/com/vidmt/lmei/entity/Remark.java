package com.vidmt.lmei.entity;



/**
 * 用户备注表
 * @author style
 *
 */

public class Remark {
	
	private Integer id;
	private Integer p_id;//用户
	private Integer other_id;//备注用户
	private String remark;//备注

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getP_id() {
		return p_id;
	}
	public void setP_id(Integer pId) {
		p_id = pId;
	}
	public Integer getOther_id() {
		return other_id;
	}
	public void setOther_id(Integer otherId) {
		other_id = otherId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}



}
