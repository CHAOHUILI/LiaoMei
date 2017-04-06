package com.vidmt.lmei.entity;

public class City {
	private Integer id;
	
	private Integer p_id;
	
	private String name;
	
	private String province;
	
	private String sortLetters;   //字母
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name.trim();
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province.trim();
	}
	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	
}