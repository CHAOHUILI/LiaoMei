package com.vidmt.lmei.constant;


public class Constant {
	public static class Config {
		public static final boolean DEVELOPER_MODE = false;//debug模式
	}

	public static final String TEL = "0351-5632599";
	public static final String ERROR_MES = "网络忙";

	public static final String ERROR = "error";

	public static final String FAIL = "fail";

	public static final String SUCCESS = "success";

	public static final String WARNING = "warning";

	public static final String EMAIL = "kefu@rendezhixin.com";

	public static int c_id = 3;

	public static int p_id = 1;
	public static final String HOST = "http://192.168.0.119:8080/lmeiApi/";
//	public static final String HOST = "http://192.168.0.111:8080/lmeiApi/";
//	public static final String HOST = "http://lmei.vidmt.com/lmeiApi/";
	/**
	 * 用户注册
	 */
	public static final String USER_REGISTER = HOST + "reg_and_third?"; // tel=?&pwd=?
	/**
	 * 用户登录
	 */
	public static final String USER_LOGIN = HOST + "login?"; // tel=?&pwd=?
	/**
	 * 修改密码
	 */
	public static final String UPDATEPOWD = HOST + "find_pwd?"; // tel=?&pwd=?
	/**
	 * 第三方登录
	 */
	public static final String THIRD_LOGIN = HOST + "reg_and_third";//
	/**
	 *修改个人数据 传什么改什么
	 */ 
    public static final String EDITDATE = HOST + "/editDate";
	/**
	 * 版本
	 */
	public static final String VERSION = HOST + "version";
	/**
	 * 设置语音、视频
	 */
	public static final String MYINCOME = HOST + "my_income";
	/**
	 * 验证手机号
	 */
	public static final String ISPHONE = HOST + "reg_tel_exist?";
	// 上传视频、音频、高清头像
	public static final String FILEUPLOAD = HOST + "fileUpload";

	public static final String EDITDATED = HOST + "editDated";
	/**
	 * 验证手机号
	 */
	public static final String SELECTUSERINFO = HOST + "persion_query";
	/**
	 * 好友列表
	 */
	public static final String SELECTFRIENDSINFO = HOST + "friend";
	/**
	 * 查找好友
	 */
	public static final String SELECTSTRANGERINFO = HOST + "queryfriend";
	/**
	 * 关注
	 */
	public static final String ADDPASTE = HOST + "add_paste";
	/**
	 * 取消关注
	 */
	public static final String UNSUBSCRIBE= HOST + "cancel_paste";
	/**
	 * 黑名单
	 */
	public static final String MYBLACKLIST = HOST + "myblacklist";
	/**
	 * 黑名单添加
	 */
	public static final String ADDBLACK = HOST + "add_black";
	/**
	 * 黑名单移除
	 */
	public static final String DELBLACK = HOST + "del_black";
	/**
	 * 允许视频音频
	 */
	public static final String ALOWDEANSWER = HOST + "alowed_to_answer";
	/**
	 * 查看详情
	 */
	public static final String SELECTUSERINFODETILE = HOST + "otherPersionInfo?";
	/**
	 * 对方是否拉黑
	 * 用例名	查看对方详情 
http://192.168.1.127:8080/lmeiApi/querymyisblack?self_id=1&other_id=2
self_id      自己的id     int
other_id     别人的id     int
	 */
	public static final String SELECTISBLACK = HOST + "querymyisblack?";
	/**
	 * 充值套餐
	 */
	public static final String SELECTTOKENINFO = HOST + "token_package?";
	/**
	 * 更新信息
	 */
	public static final String PERSIONINFOUPDATE = HOST + "persionOne?";
	/**
	 * zfb绑定
	 */
	public static final String ADDALICOUNT = HOST + "AddAliCount?";
	/**
	 * 充值
	 */
	public static final String RECHARGE = HOST + "ALiPay/getaliorder?";
	/**
	 * 充值提现记录
	 */
	public static final String RECHARGERECODE = HOST + "recharge?";
	/**
	 * 体现申请
	 */
	public static final String WITHDRAW = HOST + "applicationWithdraw?";
	/**
	 * 收费申请
	 */
	public static final String SELCETCHARGE = HOST + "getcharge?";
	/**
	 * 收费修改
	 */
	public static final String UPDATECHARGE = HOST + "charging_set?";
	/**
	 * 一键转换
	 */
	public static final String TRANSITION = HOST + "captotoken?";
	// 查看支付宝信息
	public static final String SELECTBYPERSIONID = HOST + "selectByPersionId";
	// 更新个人数据
	public static final String PERSIONONE = HOST + "persionOne";
	// 消息
	public static final String GETNOTICE = HOST + "notice_query?";
	/*
	 *  消息
	 *  http://192.168.1.127:8080/lmeiApi/del_notice?id=&flag=3
id   =        整型(int)信息id    系统消息id
flag  =  3     现阶段默认传3    代表删除
成功success
失败fail
	 */
	public static final String DELNOTICE = HOST + "del_notice?";
	// 收支记录
	public static final String BALANCE = HOST + "/balance";
	// 退款记录
	public static final String REFUNDLIST = HOST + "/refundList";
	//查看提现信息
		public static final String SELECTWITHDRAWBYPID = HOST +"/selectWithdrawByPid";
 	
 	
		//删除相册照片
		public static final String DELALBUM  = HOST +"/delAlbum";
 	
 	
 	
 	
 	//聊天
 	
 	/*
	 * 判断是否为第一次聊天
	 * 
	 */
	public static final String  FIRSTCHAT = HOST+"firstChat?";//sell_id=&buy_id=&type=
	/* 
	 * 创建订单，只要是开始聊天就走这个订单，
	 * 如果通过接口12
	 * 返回的chat对象的
	 * state=2：正在聊
	 * ，state=3：聊天结束，
	 * state=1：聊天失败，
	 * 当state=2的时候不走这个接口
	 */
	public static final String  BUYTIME = HOST+"createChat?";
	/**
	 * 聊天接口
	 */
	//发起聊天要求，发送jpush信息
	//persion_id = 需要发送信息的用户id
	public static final String  SENDCHAT = HOST +"sms?";  //persion_id=
	//开始聊天，插入聊天记录表
	/**
	 * buy_id =  购买方id
       sell_id = 服务方id
       finish_time = 结束时间
       buy_time = 购买时间（接通、服务方开始回复的时间）
       token = 花费金币数
        type = 1-普通，2-语音，3-视频，4-短信
	 **/
	public static final String BAGINCHAT = HOST +"beginChat?";//json=";

	//返回结束时间
	/*
	 * id = 聊天id
	 * 成功，返回finish_time
                   失败，返回空或null
	 */
	public static final String ENDCHATTIME = HOST +"endChatReturnTime?";//id=";
	/*
	 * 聊天失败
	 * buy_id =  购买方id
       sell_id = 服务方id
       token = 花费金币数
                  成功success
                  失败fail
	 */
	public static final String ERRORCHAT = HOST +"errorChat?";// buy_id=&token=&sell_id=;

	//礼物列表
	public static final String GETPRESENT = HOST +"present_query"; 
	//送礼物
	/**
	 * buy_id =  购买方id
       sell_id = 服务方id
       token = 花费金币数
       price = 礼物的金币数
	 */
	public static final String SENDPRESENT = HOST +"give_present?";//buy_id=&sell_id=&token=&price=";

	//聊天结束结算爱意
	/*
	 * chat = 聊天id
      type = 1-普通，2-语音，3-视频，4-短信
	 * 成功success
                  失败fail
	 */
	public static final String CHATOVER = HOST +"chatEnd?";//chat=&type=";
	/***
	 * 
	 * 
	 */

	/**
	 * 
	 * 获取聊天的flag
	 */
	public static final String  FLAGJUDGE=  HOST+"flagJudge?";
	/**
	 * 根据融联云账号查询人
	 */
	public static final String SELECTRONG = HOST +"selectByRAccount?";//r_account=54346843"
 	
 	
 	
	/**
	 * 
	 * 视频聊天截图上传
  http://192.168.1.127:8080/loveApi/upcutphoto? video_order=1&screenshot=base64   video_order    int     视频聊天的订单号
screenshot     String   base64的截图  
	 */

	public  static final String UPCUTPHOTO = HOST+"upcutphoto?";
 	
	/**
	 * 允许接听 
	 * Id = 整型(int) 用户编号
       voice_state  = 整型(int)语音状态(1.开启 2.关闭)
       video_state  = 整型(int)视频状态(1.开启 2.关闭)
               根据按钮的开关情况修改persion 对象里的 voice_state和
        video_state 值 ，开启时赋为1 关闭时赋为2 
        text_state开启时赋为1 关闭时赋为2

	 */

	public  static final String CHANGEJIE = HOST+"alowed_to_answer?";
	/**
	 IM聊天 每发一条走一次
      http://192.168.1.127:8080/loveApi/im/chat?buy_id=114&sell_id=128
      buy_id     int      聊天发送者
      sell_id     int       聊天接收者
         异常 = error
    对方被禁用 = sell_error
自己被禁用 = buy_error
自己金币不足 = buy_nomoney
对方聊天不收费 = sell_free （当返回该值时，此次聊天中不再请求接口）
成功 = 返回自己的剩余金币   int  类型
	 */

	public  static final String IM = HOST+"im/chat?";
	/**
	 * 
	 * 送礼物
	 http://192.168.1.127:8080/loveApi/im/give_present?buy_id=114&sell_id=128&id=20
buy_id     int      发送者
sell_id     int       接收者
id         int      礼物的id
异常 = error
对方被禁用 = sell_error
自己被禁用 = buy_error
自己金币不足 = buy_nomoney
成功 = 返回自己的剩余金币   int  类型

	 */

	public  static final String GIVEPRESENT = HOST+"im/give_present?";
	/**
	 * 
	 *语音视频 - 创建订单
	http://192.168.1.127:8080/loveApi/im/v_start?buy_id=114&sell_id=128&type=voice
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

	public  static final String VSTART = HOST+"im/v_start?";
	/**
	 * 
	 * 语音视频 - 根据订单id进行扣费
	http://192.168.1.127:8080/loveApi/im/v_cutmoney? orderid=  4接口返回值
orderid    int      4接口返回值  
异常 = error
结算时间异常 = time_error  正常情况不会出现此反馈
对方收费标准有误 = sell_geterror   对方没有设置语音视频收费金额
订单不存在或者不是语音视频的订单 = order_error
自己金币不足 = buy_nomoney
成功 = 返回自己的剩余金币   int  类型

	 */

	public  static final String VCUTMONEY = HOST+"im/v_cutmoney?";
 	
 /**
  * 评价	
  * http://192.168.1.127:8080/lmeiApi/im/order_grade?orderid=4&star= 5
  * 
  */
 	
	public  static final String GRADE = HOST+"im/order_grade?";
 	
	//举报
		public static final String COMPLAINT = HOST +"/complaint";
	/**
	 * 
	 * 更新用户的登录时间、经纬度
	http://192.168.1.127:8080/loveApi/login_again?id=1&s_position_x=&s_position_y=

      id    int类型(整型)登录用户
    s_position_x   double   用户经度
    s_position_y   double   用户经度
	 */

	public  static final String FRESHLOGIN = HOST+"/login_again?";
	
	
	
}
