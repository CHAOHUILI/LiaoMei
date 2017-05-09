package com.vidmt.lmei.controller;


import com.ta.util.http.RequestParams;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.util.think.HttpUtil;


public class Chat_Service {


	/**
	 * 判断是否为第一次聊天
	 * 
	 * firstChat?";//sell_id=&buy_id=
	 * sell_id 服务方
	 * buy_id 购买方
	 * 
	 * 成功返回chat对象 失败fail第一次聊天返回first,订单存在，但是卖方还未回复，订单还未开始返回notBegin,countdown 还有多少时间订单时间到，单位：秒
	 */
	public static String firstchat (int buy_id, int sell_id) {
		RequestParams params = new RequestParams();
		params.put("buy_id", buy_id+"");
		params.put("sell_id", sell_id+"");
		String json = HttpUtil.doPost(Constant.FIRSTCHAT, params);
		return json;
	}



	public static String buytime (int buy_id, int sell_id,int token,int buy_time ,int type) {
		RequestParams params = new RequestParams();
		params.put("buy_id", buy_id+"");
		params.put("sell_id", sell_id+"");
		params.put("token", token+"");
		params.put("buy_time", buy_time+"");
		params.put("type",type +"");
		String json = HttpUtil.doPost(Constant.BUYTIME, params);
		return json;
	}


	public static String sendchat (String  tel) {
		RequestParams params = new RequestParams();
		params.put("tel", tel);
		String json = HttpUtil.doPost(Constant.SENDCHAT, params);
		return json;
	}
	/**
	 * 
	 * 开始聊天，插入聊天记录表
	 * id =  订单id
       finish_time = 结束时间（通过接通时间计算这个结束的时间）
	 * 
	 */
	public static String baginchat(int id ,int buy_time ,int type,int sell_id,int buy_id) {
		RequestParams params = new RequestParams();
		params.put("id", id+"");
		params.put("type", type+"");
		params.put("buy_time", buy_time+"");
		params.put("sell_id", sell_id+"");
		params.put("buy_id", buy_id+"");
		String json = HttpUtil.doPost(Constant.BAGINCHAT, params);
		return json;
	}

	/**
	 * 
	 * 返回结束时间
	 *id = 聊天订单id
	 * 
	 */
	public static String  endchat(int id ) {
		RequestParams params = new RequestParams();
		params.put("id", id+"");
		String json = HttpUtil.doPost(Constant.ENDCHATTIME, params);
		return json;
	}
	/**
	 * 
	    聊天失败，返回金币，修改成功率
	 * 
	 * id = 订单id
       buy_id =  购买方id
       sell_id = 服务方id
       token = 花费金币数
	 */
	public static String  endchat(int id ,int buy_id , int sell_id, int token) {
		RequestParams params = new RequestParams();
		params.put("id", id+"");
		params.put("buy_id", buy_id+"");
		params.put("sell_id", sell_id+"");
		params.put("token", token+"");
		String json = HttpUtil.doPost(Constant.ERRORCHAT, params);
		return json;
	}

	/**
	 * 
	    获取礼物列表
	 * 
	 */
	public static String  getpresent() {

		String json = HttpUtil.doPost(Constant.GETPRESENT, null);
		return json;
	}



	/**
	 *     聊天结束
	    id = 聊天的id（订单的id）
	 */
	public static String  chatover(String buy_account,String sell_account) {
		RequestParams params = new RequestParams();
		params.put("sell_account", sell_account);
		params.put("buy_account", buy_account);
		String json = HttpUtil.doPost(Constant.CHATOVER, params);
		return json;
	}
	
	/**
	 *   获取聊天状态
	    id = 聊天的id（订单的id）
	 */
	public static String  getflag(String buy_account,String sell_account) {
		RequestParams params = new RequestParams();
		params.put("sell_account", sell_account);
		params.put("buy_account", buy_account);
		String json = HttpUtil.doPost(Constant.FLAGJUDGE, params);
		return json;
	}

	/**
	 *   根据融账号返回persion
	 */
	public static String selectrong(String r_account) {
		RequestParams params = new RequestParams();
		params.put("r_account", r_account);
		String json = HttpUtil.doPost(Constant.SELECTRONG, params);
		return json;
	}
	

	/**
	 *  视频截图上传
	 */
	public static String upcutphoto(int video_order,String photo) {
		RequestParams params = new RequestParams();
		params.put("video_order", video_order+"");
		params.put("screenshot", photo);
		String json = HttpUtil.doPost(Constant.UPCUTPHOTO, params);
		return json;
	}

	/**
	 *   发送消息
	 *   
	 *        异常 = error
                对方被禁用 = sell_error
               自己被禁用 = buy_error
                自己金币不足 = buy_nomoney
                 对方聊天不收费 = sell_free （当返回该值时，此次聊天中不再请求接口）
                  成功 = 返回自己的剩余金币   int  类型
	 *   
	 */
	public static String sendmes(String  buy_id ,String  sell_id,int sms_money) {
		RequestParams params = new RequestParams();
		params.put("buy_id", buy_id);
		params.put("sell_id", sell_id);
		params.put("sms_money", sms_money+"");
		String json = HttpUtil.doPost(Constant.IM, params);
		return json;
	}
	/**
	 * 
	    送礼物
	 * 
	 *buy_id     int      发送者
    sell_id     int       接收者
    id         int      礼物的id
          异常 = error
           对方被禁用 = sell_error
          自己被禁用 = buy_error
         自己金币不足 = buy_nomoney
         成功 = 返回自己的剩余金币   int  类型
	 */
	public static String  sendpresent(String buy_id ,String sell_id,int id) {
		RequestParams params = new RequestParams();
		params.put("id", id+"");
		params.put("buy_id", buy_id);
		params.put("sell_id", sell_id);
		String json = HttpUtil.doPost(Constant.GIVEPRESENT, params);
		return json;
	}
	/**
	 * 
	    语音视频 - 创建订单(暂定)
	 * 
buy_id     int      发起人
sell_id     int       接听人
type       String     标识符 语音 voice 视频 video
异常 = error
对方被禁用 = sell_error
自己被禁用 = buy_error
对方不开启语音 = sell_novoice
对方不开启视频 = sell_novideo
自己与对方有未处理的语音消费 = voice_noend正常情况不会出现此反馈
自己与对方有未处理的视频消费 = video_noend正常情况不会出现此反馈
成功 = 语音视频的订单号id   int  类型
	 */
	public static String  vstart(String buy_id ,String sell_id,String type) {
		RequestParams params = new RequestParams();

		params.put("buy_id", buy_id);
		params.put("sell_id", sell_id);
		params.put("type", type);
		String json = HttpUtil.doPost(Constant.VSTART, params);
		return json;
	}
	/**
	 * 
	 语音视频 - 根据订单id进行扣费
orderid    int      4接口返回值  
异常 = error
结算时间异常 = time_error  正常情况不会出现此反馈
对方收费标准有误 = sell_geterror   对方没有设置语音视频收费金额
订单不存在或者不是语音视频的订单 = order_error
自己金币不足 = buy_nomoney
成功 = 返回自己的剩余金币   int  类型
	 */
	public static String  vcutmoney(int orderid) {
		RequestParams params = new RequestParams();
		params.put("orderid", orderid+"");
		String json = HttpUtil.doPost(Constant.VCUTMONEY, params);
		return json;
	}
	/**
	 *  聊天评价、
	 * orderid=4&star= 5
	 */
	public static String grade(int orderid,int star) {
		RequestParams params = new RequestParams();
		params.put("orderid", orderid+"");
		params.put("star", star+"");
		String json = HttpUtil.doPost(Constant.GRADE, params);
		return json;
	}


}
