package com.vidmt.lmei.entity;
//聊天表
public class Chat {
    private Integer id;

    private Integer state;//1-失败，2-正在聊，3-聊天结束

    private Integer buy_id;//购买方，即登录用户

    private Integer sell_id;//服务方

    private String create_date;

    private String finish_time;//聊天结束时间

    private Integer buy_time;//购买总时长，天计算
    
    private Integer token;//花费爱意
    
    private Integer type;//1-普通，2-语音，3-视频，4-短信
    
    private String countdown;//倒计时
    
    private Integer pay_time;//语音视频的时间长度
    
    private Integer flag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

	public Integer getBuy_id() {
		return buy_id;
	}

	public void setBuy_id(Integer buyId) {
		buy_id = buyId;
	}

	public Integer getSell_id() {
		return sell_id;
	}

	public void setSell_id(Integer sellId) {
		sell_id = sellId;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String createDate) {
		create_date = createDate;
	}

	public String getFinish_time() {
		return finish_time;
	}

	public void setFinish_time(String finishTime) {
		finish_time = finishTime;
	}

	public Integer getBuy_time() {
		return buy_time;
	}

	public void setBuy_time(Integer buyTime) {
		buy_time = buyTime;
	}

	public Integer getToken() {
		return token;
	}

	public void setToken(Integer token) {
		this.token = token;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCountdown() {
		return countdown;
	}

	public void setCountdown(String countdown) {
		this.countdown = countdown;
	}

	public Integer getPay_time() {
		return pay_time;
	}

	public void setPay_time(Integer payTime) {
		pay_time = payTime;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "Chat [buy_id=" + buy_id + ", buy_time=" + buy_time
				+ ", countdown=" + countdown + ", create_date=" + create_date
				+ ", finish_time=" + finish_time + ", id=" + id + ", pay_time="
				+ pay_time + ", sell_id=" + sell_id + ", state=" + state
				+ ", token=" + token + ", type=" + type + "]";
	}

    
}