package com.vidmt.lmei.entity;

import java.util.Date;



/**
 * 评论表
 * @author Administrator
 *
 */
public class Score {
	private int id;//编号
	private int buy_id;//评价人编号
	private int sell_id;//接受人编号
	private int state;// 评价状态;
	private String create_date;//评价时间;
	private int attitude;//陪聊态度
	private int speed;//回复速度
	private int rate;//回复成功率
	private int comments_type;//评价类型
	private String comments_content;//评价内容
	
	private int comments;//评论
	private int high_praise;//好评
	private double high_praise_rate;//好评率
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBuy_id() {
		return buy_id;
	}
	public void setBuy_id(int buyId) {
		buy_id = buyId;
	}
	public int getSell_id() {
		return sell_id;
	}
	public void setSell_id(int sellId) {
		sell_id = sellId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String createDate) {
		create_date = createDate;
	}
	public int getAttitude() {
		return attitude;
	}
	public void setAttitude(int attitude) {
		this.attitude = attitude;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public int getComments_type() {
		return comments_type;
	}
	public void setComments_type(int commentsType) {
		comments_type = commentsType;
	}
	public String getComments_content() {
		return comments_content;
	}
	public void setComments_content(String commentsContent) {
		comments_content = commentsContent;
	}
	
	
	
	public Score() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public int getComments() {
		return comments;
	}
	public void setComments(int comments) {
		this.comments = comments;
	}
	public int getHigh_praise() {
		return high_praise;
	}
	public void setHigh_praise(int highPraise) {
		high_praise = highPraise;
	}
	
	
	public double getHigh_praise_rate() {
		return high_praise_rate;
	}
	public void setHigh_praise_rate(double highPraiseRate) {
		high_praise_rate = highPraiseRate;
	}
	
	
	
}