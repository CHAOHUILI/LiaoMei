package com.vidmt.lmei.entity;


//魅力展示列表表
public class Charm {
	private String present;//礼物名
	private String persion_name;//赠送用户名

	private String create_date;
	private Integer charm;//获得魅力点
	private String chat;//聊天
	public String getPresent() {
		return present;
	}
	public void setPresent(String present) {
		this.present = present;
	}
	public String getPersion_name() {
		return persion_name;
	}
	public void setPersion_name(String persionName) {
		persion_name = persionName;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String createDate) {
		create_date = createDate;
	}
	public Integer getCharm() {
		return charm;
	}
	public void setCharm(Integer charm) {
		this.charm = charm;
	}
	public String getChat() {
		return chat;
	}
	public void setChat(String chat) {
		this.chat = chat;
	}
	
}
