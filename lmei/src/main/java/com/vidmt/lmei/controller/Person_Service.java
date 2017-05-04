package com.vidmt.lmei.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.ta.util.http.RequestParams;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.util.think.HttpUtil;
import com.vidmt.lmei.util.think.JsonUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Person_Service {
	/**
	 * 用户注册
	* @Title Insert
	* @Description TODO(这里用一句话描述这个方法的作用)
	* @param @param u_tel
	* @param @param password
	* @param @return    参数
	* @return String    返回类型
	 */
	public static String insert(String u_tel,String password,String birthday,String photo,int sex,String nick_name,String a,String ver,String ch,String dev)
	{
		//password = MD5.md5(password);
		RequestParams params = new RequestParams();
		params.put("tel", u_tel);
		params.put("pwd", password);
		params.put("birthday", birthday);
		params.put("photo", photo);
		params.put("sex", sex+"");
		params.put("nick_name", nick_name);
		params.put("os", a);
		params.put("ver", ver);
		params.put("ch", ch);
		params.put("dev", dev);
		String str = HttpUtil.doPost(Constant.USER_REGISTER, params);
		return str;
	}
	
	/**
	刷新登录时间
	
 http://192.168.1.119:8080/loveApi/my_income? id=1&s_position_x=&s_position_y=
	 * @return
	 */
	public static String login_again(int id){
		RequestParams params = new RequestParams();
		params.put("id", id+"");
		
		String json = HttpUtil.doPost(Constant.FRESHLOGIN, params);
		return json;
	}
	
	/**
	 * 用户登陆
	* @Title Login
	* @Description TODO(这里用一句话描述这个方法的作用)
	* @param @param utel
	* @param @param password
	* @param @return    参数
	* @return String    返回类型
	 */
	public static String login(String utel,String password,String third_login,String a,String ver,String ch,String dev)
	{
		//password = MD5.md5(password);
		RequestParams params = new RequestParams();
		params.put("tel", utel);
		params.put("pwd", password);
		params.put("third_login", third_login);
		params.put("os",a);
		params.put("ver",ver);
		params.put("ch",ch);
		params.put("dev",dev);
		return HttpUtil.doPost(Constant.USER_LOGIN, params);
	}
	public static String updatePwd(String tel,String password)
	{
		RequestParams params = new RequestParams();
		params.put("tel", tel);
		params.put("pwd", password);
		return HttpUtil.doPost(Constant.UPDATEPOWD, params);
	}
	public static String third_login(String third_login,String nick_name,String birth_day,int sex,String photo,String a,String ver,String ch,String dev){
		RequestParams params = new RequestParams();
		params.put("third_login", third_login);
		params.put("nick_name", nick_name);
		params.put("birth_day", birth_day);
		params.put("sex", sex+"");
		params.put("photo", photo);
		params.put("os", a);
		params.put("ver", ver);
		params.put("ch", ch);
		params.put("dev", dev);
		String json = HttpUtil.doPost(Constant.THIRD_LOGIN, params);
		return json;
	}

	/**
	 * 退出登陆
	 * @param login_out 用户id
	 */
	public static String sendGet(int login_out) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = Constant.LOGINOUT+login_out;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	public static String myIncome(String utel,String password)
	{
		//password = MD5.md5(password);
		RequestParams params = new RequestParams();
		params.put("tel", utel);
		params.put("pwd", password);
		return HttpUtil.doPost(Constant.MYINCOME , params);
	}

	/**
	 * 更新用户状态
	 * @param id 用户id
	 * @param status 状态 1，空闲，2，正在通话
	 * @return
	 */
	public static String update_presence(String id,String status)
	{
		//password = MD5.md5(password);
		RequestParams params = new RequestParams();
		params.put("id", id);
		params.put("presence", status);
		return HttpUtil.doPost(Constant.UPDATE_PRESENCE , params);
	}

	/**
	 * 查询用户是否被禁用
	 * @param id
	 * @return
	 */
	public static String loginupdage(int id)
	{
		RequestParams params = new RequestParams();
		params.put("id", id+"");
		return HttpUtil.doPost(Constant.PERSIONINFOUPDATE, params);
	}

	/**
	 * 充值接口
	 * @param tokenid 套餐id
	 * @param id 用户id
	 * @return
	 */
	public static String PayIns(int tokenid,int id)
	{
		RequestParams params = new RequestParams();
		params.put("token_package_id", tokenid+"");
		params.put("persion_id", id+"");
		return HttpUtil.doPost(Constant.RECHARGE, params);
	}

	/**
	 * 验证手机号
	 * @param tel 号码
	 * @return
	 */
	public static String isPhone(String tel)
	{
		RequestParams params = new RequestParams();
		params.put("tel", tel);
		return HttpUtil.doPost(Constant.ISPHONE, params);
	}
	
	/**
	 * 修改个人信息，头像和相册都在内
	 * @param p
	 * @return
	 */
	public static String editDate(String p){
		RequestParams params = new RequestParams();
		params.put("json", p);
		String json = HttpUtil.doPost(Constant.EDITDATE, params);
		return json;
	}
	
	/**
	 * 删除照片
	 * @param album 照片
	 * @param id  用户id
	 * @return
	 */
	public static String delAlbum(String album,int id){
		RequestParams params = new RequestParams();
		params.put("album",album);
		params.put("id", id+"");
		String json = HttpUtil.doPost(Constant.DELALBUM, params);
		return json;
	}
	/**
	 * 图片或视频认证
	 * @param p
	 * @return
	 */
	public static String editDateD(String p){
		RequestParams params = new RequestParams();
		params.put("json", p);
		String json = HttpUtil.doPost(Constant.EDITDATED, params);
		return json;
	}

	/**
	 * 举报
	 * @param self_id
	 * @param other_id
	 * @param reason
	 * @return
	 */
	public static String complaint(int self_id,int other_id,String reason){
		RequestParams params = new RequestParams();
		params.put("self_id", self_id+"");
		params.put("other_id", other_id+"");
		params.put("reason", reason);
		String json = HttpUtil.doPost(Constant.COMPLAINT, params);
		return json;
	}
	/**
	 * 查看绑定的支付宝信息
	 * @param pid 用户id
	 * @return list<Aliacount>
	 */
	public static String selectByPersionId(int pid){
		RequestParams params = new RequestParams();
		params.put("pid", pid+"");
		String json = HttpUtil.doPost(Constant.SELECTBYPERSIONID, params);
		return json;
	}

	/**
	 * 加载主页用户信息
	 * 	用户id，页面类型，性别，认证，显示数据页
	 */
	public static String getUserAll(int id,int orderCondition,int sex,int ident_state,int pageIndex){
		RequestParams params = new RequestParams();
		params.put("id", id+"");
		params.put("orderCondition", orderCondition+"");
		params.put("sex", sex+"");
		params.put("ident_state", ident_state+"");
		params.put("pageIndex", pageIndex+"");
		String json = HttpUtil.doPost(Constant.SELECTUSERINFO, params);
		return json;
	}

	/**
	 * 获取好友，粉丝，关注列表
	 * 参数 用户id  类型包含好友，粉丝，关注    key是关键字，搜索好友用到
	 */
	public static String selectfriends(int id,int flag,String key){
		RequestParams params = new RequestParams();
		params.put("id", id+"");
		params.put("flag", flag+"");
		params.put("key", key);
		String json = HttpUtil.doPost(Constant.SELECTFRIENDSINFO, params);
		return json;
	}

	/**
	 * 	查找某人，添加好友
	 * @param id 自己id
	 * @param key 关键字
	 * @return
	 */
	public static String selectstranger(int id,String key){
		RequestParams params = new RequestParams();
		params.put("id", id+"");
		params.put("key", key);
		String json = HttpUtil.doPost(Constant.SELECTSTRANGERINFO, params);
		return json;
	}

	/**
	 * 黑名单
	 * @param id
	 * @return
	 */
	public static String myblacklist(int id){
		RequestParams params = new RequestParams();
		params.put("id", id+"");
		String json = HttpUtil.doPost(Constant.MYBLACKLIST, params);
		return json;
	}

	/**
	 * 添加关注
	 */
	public static String addpaste(int persion_id,int accept_id){
		RequestParams params = new RequestParams();
		params.put("persion_id", persion_id+"");
		params.put("accept_id", accept_id+"");
		String json = HttpUtil.doPost(Constant.ADDPASTE, params);
		return json;
	}

	/**
	 * 取消关注
	 */
	public static String unsubscribe(int id){
		RequestParams params = new RequestParams();
		params.put("id", id+"");
		String json = HttpUtil.doPost(Constant.UNSUBSCRIBE, params);
		return json;
	}

	/**
	 * 拉黑n
	 */
	public static String featuresblack(int persion_id,int other_id){
		RequestParams params = new RequestParams();
		params.put("persion_id", persion_id+"");
		params.put("other_id", other_id+"");
		String json = HttpUtil.doPost(Constant.ADDBLACK, params);
		return json;
	}

	/**
	 *取消拉黑
	 */
	public static String cancelPullBlack(int id){
		RequestParams params = new RequestParams();
		params.put("id", id+"");
		String json = HttpUtil.doPost(Constant.DELBLACK, params);
		return json;
	}

	/**
	 * 允许视频或者音频
	 */
	public static String alowed_to_answer(int id,int voice_state,int video_state){
		RequestParams params = new RequestParams();
		params.put("id", id+"");
		params.put("voice_state", voice_state+"");
		params.put("video_state", video_state+"");
		String json = HttpUtil.doPost(Constant.ALOWDEANSWER, params);
		return json;
	}

	/**
	 * 查看别人信息
	 */
	public static String loaduserinfo(int self_id,int other_id){
		RequestParams params = new RequestParams();
		params.put("self_id", self_id+"");
		params.put("other_id", other_id+"");
		String json = HttpUtil.doPost(Constant.SELECTUSERINFODETILE, params);
		return json;
	}


//	public static String selectisblack(int self_id,int other_id){
//		RequestParams params = new RequestParams();
//		params.put("self_id", self_id+"");
//		params.put("other_id", other_id+"");
//		String json = HttpUtil.doPost(Constant.SELECTISBLACK, params);
//		return json;
//	}
	/**
	 *查看对方是否拉黑自己
	 */
	public static String selectisblack(String self_id,String other_id){
		RequestParams params = new RequestParams();
		params.put("self_id", self_id);
		params.put("other_id", other_id);
		String json = HttpUtil.doPost(Constant.SELECTISBLACK, params);
		return json;
	}

	/**
	 * 充值套餐列表加载
	 */
	public static String selecttokeninfo(){
		RequestParams params = new RequestParams();
		String json = HttpUtil.doPost(Constant.SELECTTOKENINFO, params);
		return json;
	}

	/**
	 *绑定支付宝
	 * id 支付宝账号 真实姓名
	 */
	public static String AddAliCount(int persion_id,String account,String realname){
		RequestParams params = new RequestParams();
		params.put("persion_id", persion_id+"");
		params.put("account", account);
		params.put("realname", realname);
		String json = HttpUtil.doPost(Constant.ADDALICOUNT, params);
		return json;
	}


	/**
	 * 申请提现
	 * id 金额
	 */
	public static String applicationWithdraw(int pid,String money){
		RequestParams params = new RequestParams();
		params.put("pid", pid+"");
		params.put("money", money+"");
		String json = HttpUtil.doPost(Constant.WITHDRAW, params);
		return json;
	}

	/**
	 *  收费价格信息加载
	 *  level   用户级别
	 */
	public static String getcharge(int level){
		RequestParams params = new RequestParams();
		params.put("level", level+"");
		String json = HttpUtil.doPost(Constant.SELCETCHARGE, params);
		return json;
	}

	/**
	 * 语言价格更新
	 */
	public static String updatevoicecharge(int id,int voice_money){
		RequestParams params = new RequestParams();
		params.put("id", id+"");
		params.put("voice_money", voice_money+"");
		String json = HttpUtil.doPost(Constant.UPDATECHARGE, params);
		return json;
	}

	/**
	 *视频价格更新
	 */
	public static String updatevideocharge(int id,int video_money){
		RequestParams params = new RequestParams();
		params.put("id", id+"");
		params.put("video_money", video_money+"");
		String json = HttpUtil.doPost(Constant.UPDATECHARGE, params);
		return json;
	}

	/**
	 *conversation页消息价格更新
	 */
	public static String updatesmscharge(int id,int sms_money){
		RequestParams params = new RequestParams();
		params.put("id", id+"");
		params.put("sms_money", sms_money+"");
		String json = HttpUtil.doPost(Constant.UPDATECHARGE, params);
		return json;
	}
	/**
	 * 收支记录
	 * @param persion_id 用户id
	 * @param state    1收入，2支出
	 * @param pageIndex  分页
	 *         flag貌似没用

	 * flag  1级分页
	 * @return
	 */
	public static String balance(int persion_id,int state,int pageIndex,String flag){
		RequestParams params = new RequestParams();
		params.put("persion_id", persion_id+"");
		params.put("state", state+"");
		params.put("pageIndex", pageIndex+"");
		params.put("flag", flag);
		String json = HttpUtil.doPost(Constant.BALANCE, params);
		return json;
	}
	/**
	 * 收支记录2 详情
	 * @param persion_id 用户id
	 * oid 对方id
	 * @param state    1收入，2支出
	 * @param pageIndex  分页
	 *               flag貌似没用
	 * @return
	 */
	public static String balance2(int persion_id,int oid,int state,int pageIndex,String flag){
		RequestParams params = new RequestParams();
		params.put("persion_id", persion_id+"");
		params.put("other_id", oid+"");
		params.put("state", state+"");
		params.put("pageIndex", pageIndex+"");
		params.put("flag", flag);
		String json = HttpUtil.doPost(Constant.BALANCE, params);
		return json;
	}
	/**
	 * 退款记录
	 * @param buy_id  登录的id 
	 * @param pageIndex 分页
	 * @return
	 */
		public static String refundList (int buy_id,int pageIndex) {
			RequestParams params = new RequestParams();
			params.put("buy_id", buy_id+"");
			params.put("pageIndex", pageIndex+"");
			String js = HttpUtil.doPost(Constant.REFUNDLIST, params);
			return js;
		}
		/**
		 * 充值记录
		 * @param persion_id
		 * @param type 没啥用
		 * @return
		 */
		public static String Recharge(int persion_id,int type){
			RequestParams params = new RequestParams();
			params.put("persion_id", persion_id+"");
			params.put("type", type+"");
			String js = HttpUtil.doPost(Constant.RECHARGERECODE, params);
			return js;
		}
		
		/**
		 * 查看提现详情
		 * @param pid
		 * @return
		 */
		public static String selectWithdrawByPid(int pid){
			RequestParams params = new RequestParams();
			params.put("pid", pid+"");
			String json = HttpUtil.doPost(Constant.SELECTWITHDRAWBYPID, params);
			return json;
		}

//	public static String persionOne(int id){
//		RequestParams params = new RequestParams();
//		params.put("id", id+"");
//		String json = HttpUtil.doPost(Constant.PERSIONONE, params);
//		return json;
//	}
	/**
	 *加载个人数据
	 * @param id
	 * @return
	 */
	public static String persionOne(String id){
		RequestParams params = new RequestParams();
		params.put("id", id);
		String json = HttpUtil.doPost(Constant.PERSIONONE, params);
		return json;
	}
	/**
	 * 查看系统消息
	 * @param id
	 * 
	 * @return
	 */
	public static String getnotice(int id){
		RequestParams params = new RequestParams();
		params.put("persion_id", id+"");
		String json = HttpUtil.doPost(Constant.GETNOTICE, params);
		return json;
	}
	/**
	 * 删除系统消息
	 * @param id 消息id
	 * id=&flag=3
	 * @return
	 */
	public static String delnotice(int id,int flag){
		RequestParams params = new RequestParams();
		params.put("id", id+"");
		params.put("flag", flag+"");
		String json = HttpUtil.doPost(Constant.DELNOTICE, params);
		return json;
	}

	/**
	 * 钱转为金币
	 * @param id 用户id
	 * @return
	 */
	public static String transition(int id){
		RequestParams params = new RequestParams();
		params.put("id", id+"");
		String json = HttpUtil.doPost(Constant.TRANSITION, params);
		return json;
	}

	/**
	 * 检查版本更新
	 * @return
	 * @throws Exception
	 */
	public static String VersionUpdate() throws Exception{
		RequestParams params = new RequestParams();
		params.put("type", 1+"");
		String jsonStr = HttpUtil.doPost(Constant.VERSION,params);	
		return jsonStr;
	}
}
