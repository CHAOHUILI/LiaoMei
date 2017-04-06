package com.vidmt.lmei;

import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;

import com.baidu.location.LocationClientOption.LocationMode;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ta.TAApplication;
import com.vidmt.lmei.constant.Constant.Config;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.rule.ManageDataBase;
import com.vidmt.lmei.util.rule.SharedPreferencesUtil;
import com.vidmt.lmei.util.think.DbUtil;
import com.xsj.crasheye.Crasheye;
import com.vidmt.lmei.activity.FooterPageActivity;
import com.vidmt.lmei.activity.MainActivity;
import com.vidmt.lmei.activity.MyConversationListBehaviorListener;
import com.vidmt.lmei.activity.SealAppContext;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.smssdk.SMSSDK;
import io.rong.calllib.RongCallCommon;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.ConversationBehaviorListener;
import io.rong.imkit.RongIM.ConversationListBehaviorListener;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectionStatusListener;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import io.rong.imlib.model.UserInfo;
import io.rong.imlib.model.Conversation.ConversationType;

public class Application extends TAApplication  {
	public LocationClient mLocationClient;
	public MyLocationListener mMyLocationListener;
	//创建公共handler 方便activity间共享数据
	public Handler handler = null; 
	private static Application mInstance = null;
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		if (Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}
		mInstance = this;
		/*mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        initLocation();
        mLocationClient.start();*/
		super.onCreate();
		initImageLoader(getApplicationContext());	
		Crasheye.init(this, "030ac220");
		//		//jpush
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
	
		/**
		 * 注意：
		 *
		 * IMKit SDK调用第一步 初始化
		 *
		 * context上下文
		 *
		 * 只有两个进程需要初始化，主进程和 push 进程
		 */
		//RongIM.setServerInfo("nav.cn.ronghub.com", "img.cn.ronghub.com");
		// RongPushClient.registerHWPush(this);

		//	    if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
		//	    	RongIM.init(this);
		//	    
		//		}
		if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {

			RongIM.init(this);

		}
		//		RongIM.init(this);
		//RongIM.init(this,"lmxuhwagxhf4d");

		SealAppContext.init(this);	
		RongIM.setConnectionStatusListener(new ConnectionStatusListener() {

			@Override
			public void onChanged(ConnectionStatus arg0) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub

				switch (arg0){

				case CONNECTED://连接成功。



					break;
				case DISCONNECTED://断开连接。
					DbUtil  dbutil = new DbUtil((TAApplication)getApplication());
					List<Persion> list = ManageDataBase.SelectList(dbutil, Persion.class, null);
					if (list.size() > 0) {
						Persion	b_person = list.get(0);
						connect(b_person.getRongyuntoken(), b_person);
					}




					break;
				case CONNECTING://连接中。



					break;
				case NETWORK_UNAVAILABLE://网络不可用。


					break;
				case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线

					MainActivity.mainactivity.Tankuang(2);
					// Toast.makeText(getApplicationContext(), "您的账号在其他地方登录", Toast.LENGTH_SHORT).show();
					break;
				}





			}


		});
		//友盟配置appid，appkey
		//SMSSDK.initSDK(this,"da1e2b341f7e","48b01ac168d7a2ad7280ecc93515059f");
	}




	public static Application getInstance()
	{
		return mInstance;
	}
	/**
	 * 发起定位
	 */
	public void requestLocationInfo()
	{
		initLocation();

		if (mLocationClient != null && !mLocationClient.isStarted())
		{
			mLocationClient.start();
		}

		if (mLocationClient != null && mLocationClient.isStarted())
		{
			mLocationClient.requestLocation();
		} 
	}

	/**
	 * 实现实时位置回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			//Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());// 单位：公里每小时
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\nheight : ");
				sb.append(location.getAltitude());// 单位：米
				sb.append("\ndirection : ");
				sb.append(location.getDirection());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\ndescribe : ");
				sb.append("gps定位成功");

			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				//运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
				sb.append("\ndescribe : ");
				sb.append("网络定位成功");
			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
				sb.append("\ndescribe : ");
				sb.append("离线定位成功，离线定位结果也是有效的");
			} else if (location.getLocType() == BDLocation.TypeServerError) {
				sb.append("\ndescribe : ");
				sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
				sb.append("\ndescribe : ");
				sb.append("网络不同导致定位失败，请检查网络是否通畅");
			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
				sb.append("\ndescribe : ");
				sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
			}
			sb.append("\nlocationdescribe : ");// 位置语义化信息
			sb.append(location.getLocationDescribe());
			List<Poi> list = location.getPoiList();// POI信息
			if (list != null) {
				sb.append("\npoilist size = : ");
				sb.append(list.size());
				for (Poi p : list) {
					sb.append("\npoi= : ");
					sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
				}
			}
			String city = location.getCity();
			SharedPreferencesUtil.putString(getApplicationContext(), "x", String.valueOf(location.getLatitude()));
			SharedPreferencesUtil.putString(getApplicationContext(), "y", String.valueOf(location.getLongitude()));
			SharedPreferencesUtil.putString(getApplicationContext(), "city", city);
			//Log.e("Baidu", location.getCity()+"=======================================================");
			//sendBroadCast(city);
			mLocationClient.stop();
		}


	}

	/**
	 * 得到发送广播
	 * @param address
	 */
	public void sendBroadCast(String city)
	{
		LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
		//mLocationClient.stop();
		Intent intent = new Intent("mainactivity");
		intent.putExtra("city", city);
		localBroadcastManager.sendBroadcast(intent);
	}

	/**
	 * 设置地图相关参数
	 * @Title initLocation
	 * @Description TODO(这里用一句话描述这个方法的作用)
	 * @param     参数
	 * @return void    返回类型
	 */
	private void initLocation()
	{

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(0);
		option.setProdName("二手车"); //设置产品线名称 
		option.setIsNeedAddress(true);  //为true时能获取到详细地址
		option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		mLocationClient.setLocOption(option);

	}

	/**
	 * 建立与融云服务器的连接
	 *
	 * @param token
	 */
	private void connect(final String token,final Persion persion) {
		//ToastShow(token);
		if (RongIM.getInstance().getRongIMClient()!=null) {



		}else {

		}

		if (getApplicationInfo().packageName.equals(Application.getApplication().getPackageName())) {

			/**
			 * IMKit SDK调用第二步,建立与服务器的连接
			 */
			RongIM.connect(token, new RongIMClient.ConnectCallback() {

				/**
				 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
				 */
				@Override
				public void onTokenIncorrect() {
					//ToastShow("--onTokenIncorrect");
					Log.d("LoginActivity", "--onTokenIncorrect");
					//	                StartActivity(HomePageActivity.class);
					//					finish();
				}

				/**
				 * 连接融云成功
				 * @param userid 当前 token
				 */
				@Override
				public void onSuccess(String userid) {
					//ToastShow("Success");
					Log.d("LoginActivity", "--onSuccess" + userid);
					RongIM.getInstance().refreshUserInfoCache(new UserInfo(persion.getId()+"", persion.getNick_name(), Uri.parse(persion.getPhoto())));
					RongIM.getInstance().setMessageAttachedUserInfo(true);


				}

				/**
				 * 连接融云失败
				 * @param errorCode 错误码，可到官网 查看错误码对应的注释
				 */
				@Override
				public void onError(RongIMClient.ErrorCode errorCode) {


					//					StartActivity(HomePageActivity.class);
					//					finish();
				}
			});
		}else {


		}
	}	

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them, 
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.enableLogging() // Not necessary in common
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public static String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
				.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}


}
