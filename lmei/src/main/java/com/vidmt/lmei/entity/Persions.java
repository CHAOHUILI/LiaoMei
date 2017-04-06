package com.vidmt.lmei.entity;

import java.io.Serializable;

public class Persions implements Serializable{
	
	
	private int id ;
	private String name;
	private String imhurl;
    private Integer video_money;//视频多少钱一分钟
    private Integer voice_money;//声音多少钱一分钟
    private Integer isLike;//是否点赞
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImhurl() {
		return imhurl;
	}
	public void setImhurl(String imhurl) {
		this.imhurl = imhurl;
	}
	public Integer getVideo_money() {
		return video_money;
	}
	public void setVideo_money(Integer video_money) {
		this.video_money = video_money;
	}
	public Integer getVoice_money() {
		return voice_money;
	}
	public void setVoice_money(Integer voice_money) {
		this.voice_money = voice_money;
	}
	public Integer getIsLike() {
		return isLike;
	}
	public void setIsLike(Integer isLike) {
		this.isLike = isLike;
	}
    
    
    
}
