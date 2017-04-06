package com.vidmt.lmei.entity;


import java.util.Date;



public class Version{
	private int id;
	private String version_info;//版本描述信息
	private String address;//下载地址
	private String create_date;
	private String title;//标识，前端不展示
	private Integer must;//该版本必须升级的标识符   1必须升级  NULL为默认值
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVersion_info() {
		return version_info;
	}
	public void setVersion_info(String versionInfo) {
		version_info = versionInfo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String createDate) {
		create_date = createDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return "Version [address=" + address + ", create_date=" + create_date
				+ ", id=" + id + ", title=" + title + ", version_info="
				+ version_info + "]";
	}
	public Integer getMust() {
		return must;
	}
	public void setMust(Integer must) {
		this.must = must;
	}
	
}
