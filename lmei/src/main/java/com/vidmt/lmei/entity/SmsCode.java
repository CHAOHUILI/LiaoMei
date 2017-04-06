package com.vidmt.lmei.entity;

import java.io.Serializable;

import android.R.integer;

public class SmsCode implements Serializable{
	
	/**
	 * 返回编码   
	 * 0--正常，在这种情况下可直接取token
	 * 1--自己在对方的黑名单中，这个时候不会扣掉自己的钱
	 * 2--对方修改了文字的收费标准，这个情况下去取sms_moeny，token，返回对方的聊天标准，自己剩余的金币
	 * 3--对方聊天免费
	 * 1001--状态异常  直接取message里的提示文字即可
	 * 500--未知错误，此情况下不取其他值。提示稍后再试等。
	 */
	private Integer code;
	
	private String message;//提示信息
	 
	private Integer token;//业务所需  自己的金币数
	
	private Integer sms_money;//业务所需  对方聊天收费标准

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getToken() {
		return token;
	}

	public void setToken(Integer token) {
		this.token = token;
	}

	public Integer getSms_money() {
		return sms_money;
	}

	public void setSms_money(Integer sms_money) {
		this.sms_money = sms_money;
	}


}
