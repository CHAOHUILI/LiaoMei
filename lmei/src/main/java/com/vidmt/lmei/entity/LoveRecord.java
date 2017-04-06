package com.vidmt.lmei.entity;


//爱意获得记录表
public class LoveRecord {
	private Integer id;
	private Integer other_id;//对方id

	private String create_date;
	private Integer persion_id;//登录用户
	private Integer love_num;//获得爱意数量
	private String nick_name; //对方昵称，页面展示用
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getOther_id() {
		return other_id;
	}
	public void setOther_id(Integer otherId) {
		other_id = otherId;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String createDate) {
		create_date = createDate;
	}
	public Integer getPersion_id() {
		return persion_id;
	}
	public void setPersion_id(Integer persionId) {
		persion_id = persionId;
	}
	public Integer getLove_num() {
		return love_num;
	}
	public void setLove_num(Integer loveNum) {
		love_num = loveNum;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nickName) {
		nick_name = nickName;
	}
	@Override
	public String toString() {
		return "LoveRecord [create_date=" + create_date + ", id=" + id
				+ ", love_num=" + love_num + ", nick_name=" + nick_name
				+ ", other_id=" + other_id + ", persion_id=" + persion_id + "]";
	}
	
}
