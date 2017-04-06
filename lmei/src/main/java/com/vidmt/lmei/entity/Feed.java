package com.vidmt.lmei.entity;

public class Feed {
	private int id;//id自增
	private int persion_id;//用户编号
	private String feedback;//反馈意见
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPersion_id() {
		return persion_id;
	}
	public void setPersion_id(int persionId) {
		persion_id = persionId;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public Feed() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Feed(int id, int persionId, String feedback) {
		super();
		this.id = id;
		persion_id = persionId;
		this.feedback = feedback;
	}
	@Override
	public String toString() {
		return "Feed [feedback=" + feedback + ", id=" + id + ", persion_id="
				+ persion_id + "]";
	}
	
	

	
}