package com.vidmt.lmei.entity;

public class ChatRound  {
	private Integer id;
	private Integer p_id;
	private Integer other_id;
	private Integer round;
	private Integer state;

	
	public ChatRound() {
		super();
	}

	public ChatRound(Integer id, Integer round) {
		super();
		this.id = id;
		this.round = round;
	}

	public ChatRound(Integer pId, Integer otherId, Integer round) {
		super();
		p_id = pId;
		other_id = otherId;
		this.round = round;
	}

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

	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "ChatRound [id=" + id + ", other_id=" + other_id + ", p_id="
				+ p_id + ", round=" + round + "]";
	}

}