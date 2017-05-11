package com.vidmt.lmei.activity;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

import com.ta.annotation.TAInjectView;
import com.ta.util.TALogger;
import com.vidmt.lmei.Application;
import com.vidmt.lmei.R;
import com.vidmt.lmei.entity.Persion;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.LocalActivityManager;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

/**
 * 应用主界面页，包含四个activity
 */
@SuppressLint("NewApi")
public class MainActivity extends BaseActivity implements OnTabChangeListener {
	public static TabHost tabHost;
//	@TAInjectView(id = R.id.radiogroup)
//	public static RadioGroup radioGroup;
	private long exitTime = 0;
	private String[] tabStrs = new String[]{"广场","消息","好友","我的"};
	
	@TAInjectView(id = R.id.main_footbar_home)
	RadioButton home;
	@TAInjectView(id = R.id.main_footbar_bulk)
	RadioButton ms;
	@TAInjectView(id = R.id.main_footbar_community)
	RadioButton find;
	@TAInjectView(id = R.id.main_footbar_personal)
	RadioButton main_footbar_personal;
	@TAInjectView(id=R.id.tipcnt_tvs)
	TextView tipcnt_tvs;
	@TAInjectView(id=R.id.tipcnt_tvbg)
	ImageView tipcnt_tvbg;
	public static Handler handler;
	@TAInjectView(id=R.id.lookme)
	ImageView lookme;
	public static boolean isshowHd;//true 显示
	private BroadcastReceiver broadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_footer_page);
		InitView(savedInstanceState);
		
		initData();
		
		registerBoradcastReceiver();
		registerBoradcastReceiver2();
		closeapp();
		//		initJpush();
		handler = mUIHandler;
		if(RongIM.getInstance()!=null && RongIM.getInstance().getRongIMClient()!=null){
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				RongIM.getInstance().setOnReceiveUnreadCountChangedListener(mCountListener, Conversation.ConversationType.PRIVATE);;
			}
		}, 1500);

	}else {

		connect(b_person.getRongyuntoken(), b_person);


	}
		//	SealAppContext.init(MainActivity.this);
	}
	private Handler mUIHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if(msg.obj!=null)
				{
					String mes = (String)msg.obj;
					if(!mes.isEmpty())
					{
						//						Drawable drawable=getResources().getDrawable(R.drawable.f_personal_style2); 
						//						main_footbar_personal.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null);
					}
				}
				break;

			default:
				break;
			}
		}

	};

	private void closeapp() {
		BroadcastReceiver broadcastReceiver = new BroadcastReceiver()

		{
			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
				StartActivity(LoginActivity.class);
			}
		};
		IntentFilter intentToReceiveFilter = new IntentFilter();
		intentToReceiveFilter.addAction("closeapp");
		registerReceiver(broadcastReceiver, intentToReceiveFilter);
	}
	//设置别名用来发消息
	private  void initJpush()
	{
		//设置消息栏样式
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(getApplicationContext());
		builder.statusBarDrawable = R.drawable.ic_launcher;
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
		builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）  
		JPushInterface.setPushNotificationBuilder(1, builder);
		//设置接受信息的数量
		JPushInterface.setLatestNotificationNumber(getApplicationContext(),3);
		//设置别名，用户id为别名
		Set<String> set = new HashSet<String>();
		set.add("all"); // 这指定all 是给所有安装爱狗之家发信息             set是TAG
		//String a = String.valueOf(1);    //别名  指定给某个用户发
		JPushInterface.setAliasAndTags(getApplicationContext(), null, set, mAliasCallback);
	}

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs ;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				TALogger.e("jpush success", logs);
				break;     
			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				TALogger.e("jpush Failed", logs);
				break;
			default:
				logs = "Failed with errorCode = " + code;
				TALogger.e("jpush Failed", logs);
			}
		}

	};

	public void InitView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		tabHost = (TabHost)findViewById(R.id.thost);
		LocalActivityManager  localActivityManager= new LocalActivityManager(this,true);
		localActivityManager.dispatchCreate(savedInstanceState);
		tabHost.setup(localActivityManager);
		TabSpec tabSpec = tabHost.newTabSpec(tabStrs[0]).
				setIndicator(tabStrs[0]).setContent(getTabItemIntent(HomeActivity.class));
		TabSpec tabSpec1 = tabHost.newTabSpec(tabStrs[1]).
				setIndicator(tabStrs[1]).setContent(getTabItemIntent(ConversationListActivity.class));

		TabSpec tabSpec2 = tabHost.newTabSpec(tabStrs[2]).
				setIndicator(tabStrs[2]).setContent(getTabItemIntent(MyFriendsActivity.class));
		TabSpec tabSpec3 = tabHost.newTabSpec(tabStrs[3]).
				setIndicator(tabStrs[3]).setContent(getTabItemIntent(PersonalCenterActivity.class));
		tabHost.addTab(tabSpec);
		tabHost.addTab(tabSpec1);
		tabHost.addTab(tabSpec2);
		tabHost.addTab(tabSpec3);
	}

	/**
	 * 初始化组件
	 */
	private void initData() {
		// 给radioGroup设置监听事件
		

		home.setChecked(true);
		tabHost.setCurrentTabByTag(tabStrs[0]);
		OnClickListener onclick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(v.getId()){
				case R.id.main_footbar_home:
					getcount(false);
					tabHost.setCurrentTabByTag(tabStrs[0]);
					ms.setChecked(false);
					find.setChecked(false);
					main_footbar_personal.setChecked(false);
					break;
				case R.id.main_footbar_bulk:
					getcount(false);
					tabHost.setCurrentTabByTag(tabStrs[1]);
					home.setChecked(false);
					find.setChecked(false);
					main_footbar_personal.setChecked(false);
					break;
				case R.id.main_footbar_community:
					try {
						tipcnt_tvs.setVisibility(View.GONE);
						tipcnt_tvbg.setVisibility(View.GONE);
						tabHost.setCurrentTabByTag(tabStrs[2]);
						home.setChecked(false);
						ms.setChecked(false);
						main_footbar_personal.setChecked(false);
						
						//RongIM.getInstance().startConversationList(HomePageActivity.this);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

					break;
				case R.id.main_footbar_personal:
					getcount(false);
					tabHost.setCurrentTabByTag(tabStrs[3]);
					home.setChecked(false);
					ms.setChecked(false);
					find.setChecked(false);

					break;
				}

			}
		};
		home.setOnClickListener(onclick);
		ms.setOnClickListener(onclick);
		find.setOnClickListener(onclick);
		main_footbar_personal.setOnClickListener(onclick);

		// 给radioGroup设置监听事件
		//		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//			Drawable drawable=null;
		//			@SuppressLint("NewApi")
		//			@Override
		//			public void onCheckedChanged(RadioGroup group, int checkedId) {
		//				switch (checkedId) {
		//				case R.id.main_footbar_service:
		//					tabHost.setCurrentTabByTag(tabStrs[0]);
		//					//					ServiceMainActivity.s_num=0;
		//					break;
		//				case R.id.main_footbar_forum:
		//					//					BBSMainActivity.b_num=0;
		//					tabHost.setCurrentTabByTag(tabStrs[1]);
		//					//					RongIM.getInstance().startConversationList(HomePageActivity.this);
		//					//					RongIM.getInstance().startSubConversationList(HomePageActivity.this, Conversation.ConversationType.PRIVATE);
		//					break;
		//				case R.id.main_footbar_MS:
		//					drawable=getResources().getDrawable(R.drawable.widget_bar_m); 
		//					//					ms.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null);	
		//					tabHost.setCurrentTabByTag(tabStrs[2]);
		//					break;
		//				case R.id.main_footbar_find:
		//					//					PersonalCenterActivity.per_code = 0;
		//					//SharedPreferencesUtil.putString(HomePageActivity.this, "h_mes", "");
		//					drawable=getResources().getDrawable(R.drawable.widget_bar_me); 
		//					find.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null);						
		//					tabHost.setCurrentTabByTag(tabStrs[3]);
		//					break;
		//				}
		//			}
		//		});
		//		((RadioButton) radioGroup.getChildAt(0)).toggle();
	
//
//		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				switch (checkedId) {
//				case R.id.main_footbar_home:
//					tabHost.setCurrentTabByTag(tabStrs[0]);
//					break;
//				case R.id.main_footbar_bulk:
//					tabHost.setCurrentTabByTag(tabStrs[1]);
//					break;
//				case R.id.main_footbar_community:
//
//					tabHost.setCurrentTabByTag(tabStrs[2]);
//					break;
//				case R.id.main_footbar_personal:
//					tabHost.setCurrentTabByTag(tabStrs[3]);
//					break;
//				}
//			}
//		});
//		((RadioButton) radioGroup.getChildAt(0)).toggle();
	}


	public void getcount(boolean get){}


	/**
	 * 给Tab选项卡设置内容（每个内容都是一个Activity）
	 */
	private Intent getTabItemIntent(Class classZ){
		Intent intent = new Intent(MainActivity.this, classZ);
		return intent;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 建立与融云服务器的连接
	 *
	 * @param token
	 */
	private void connect(final String token,final Persion persion) {
		if (RongIM.getInstance().getRongIMClient()!=null) {
			String	as;
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

					RongIM.getInstance().refreshUserInfoCache(new UserInfo(persion.getId()+"", persion.getNick_name(), Uri.parse(persion.getPhoto())));



					initData();
				
					//		alist=getIntent().getParcelableArrayListExtra("alist");
					///initJpush();
					//		initrong();
					//		LoadData();




					//OnUpdateMsgUnreadCounts();
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							RongIM.getInstance().setOnReceiveUnreadCountChangedListener(mCountListener, Conversation.ConversationType.PRIVATE);;
						}
					}, 1500);



				}

				/**
				 * 连接融云失败
				 * @param errorCode 错误码，可到官网 查看错误码对应的注释
				 */
				@Override
				public void onError(RongIMClient.ErrorCode errorCode) {

					Log.d("LoginActivity", "--onError" + errorCode);

					//					StartActivity(HomePageActivity.class);
					//					finish();
				}
			});
		}
	}
	public RongIM.OnReceiveUnreadCountChangedListener mCountListener = new RongIM.OnReceiveUnreadCountChangedListener() {
		@Override
		public void onMessageIncreased(int count) {
			int unreadCount= count;
			// TODO Auto-generated method stub
			//		int unreadCount = IMessageSqlManager.qureyAllSessionUnreadCount();


			if (unreadCount==0) {
				tipcnt_tvs.setVisibility(View.GONE);
				tipcnt_tvbg.setVisibility(View.GONE);

			}else {
				tipcnt_tvbg.setVisibility(View.VISIBLE);
				tipcnt_tvs.setVisibility(View.VISIBLE);
				if (unreadCount>99) {
					tipcnt_tvs.setText("...");	

				}else {
					tipcnt_tvs.setText(unreadCount+"");
				}
			}
		}
	};
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){  

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();  
			if(action.equals("发送广播")){  
				lookme.setVisibility(View.VISIBLE);
				isshowHd = true;
			}else if(action.equals("vanish")){
				lookme.setVisibility(View.GONE);
				isshowHd = false;
			}

		}  

	};  
	public void registerBoradcastReceiver(){  
		//有人看过我
		IntentFilter myIntentFilter = new IntentFilter();  
		myIntentFilter.addAction("发送广播");  
		//注册广播        
		registerReceiver(mBroadcastReceiver, myIntentFilter);  

	}  
	public void registerBoradcastReceiver2(){  
		//查看谁看过我
		IntentFilter myIntent = new IntentFilter();  
		myIntent.addAction("vanish");  
		//注册广播        
		registerReceiver(mBroadcastReceiver, myIntent);  
	}  
	
	public void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(),
					this.getResources().getString(R.string.exit),
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			exitApp();
		}
	}
	@Override
	public void onTabChanged(String arg0) {
		// TODO Auto-generated method stub

	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

}
