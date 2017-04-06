package com.vidmt.lmei.entity;
import java.util.Date;


/**
 * 推荐表
 * @author Administrator
 *
 */

public class Recommended {
    
	private int id;//编号
	private String ip;//本地IP
	private int share_id;//分享人id
	private int registrant_id;//注册人id
	private String create_date;//注册时间
	
	private Persion persion;//用户表信息
	
	private String nick_name;//昵称
	private String photo;//头像
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getShare_id() {
		return share_id;
	}
	public void setShare_id(int shareId) {
		share_id = shareId;
	}
	public int getRegistrant_id() {
		return registrant_id;
	}
	public void setRegistrant_id(int registrantId) {
		registrant_id = registrantId;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String createDate) {
		create_date = createDate;
	}


	public Persion getPersion() {
		return persion;
	}
	public void setPersion(Persion persion) {
		this.persion = persion;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nickName) {
		nick_name = nickName;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
}