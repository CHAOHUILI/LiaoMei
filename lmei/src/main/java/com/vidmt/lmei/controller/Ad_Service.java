package com.vidmt.lmei.controller;

import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.ta.util.http.RequestParams;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.entity.Ad;
import com.vidmt.lmei.util.think.HttpUtil;
import com.vidmt.lmei.util.think.JsonUtil;

public class Ad_Service {
	/**
	 * json转换list
	* @Title getAll
	* @Description TODO(这里用一句话描述这个方法的作用)
	* @param @return    参数
	* @return List<Ad>    返回类型
	 *//*
	public static List<Ad> getAll()
	{
		String json = HttpUtil.doPost(Constant.AD_LIST, null);
		return JsonUtil.JsonToObj(json, new TypeToken<List<Ad>>(){}.getType());
	}
	
	*//** 实体转换json
	 * 回复主题
	* @Title replyTheme
	* @Description TODO(这里用一句话描述这个方法的作用)
	* @param @param p_id
	* @param @param other_id
	* @param @return    参数
	* @return String    返回类型
	 *//*
	public static String replyTheme(String json)
	{
		RequestParams params = new RequestParams();
		params.put("json", json);
		return HttpUtil.doPost(Constant.REPLY_THEME, params);
	}*/
}
