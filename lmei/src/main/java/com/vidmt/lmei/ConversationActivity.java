package com.vidmt.lmei;




import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.ta.TAApplication;
import com.vidmt.lmei.activity.BaseActivity;
import com.vidmt.lmei.activity.HomeDetailActivity;
import com.vidmt.lmei.activity.MainActivity;
import com.vidmt.lmei.activity.OnDataListener;
import com.vidmt.lmei.activity.RongCallKit;
import com.vidmt.lmei.activity.SealAppContext;
import com.vidmt.lmei.activity.SingleCallActivity;
import com.vidmt.lmei.activity.UserRechargeActivity;
import com.vidmt.lmei.activity.HomeDetailActivity.ShowContentView;
import com.vidmt.lmei.adapter.GridViewAdapter;
import com.vidmt.lmei.adapter.PersentAdapter;
import com.vidmt.lmei.adapter.ViewPagerAdapter;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Chat_Service;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.dialog.LoadingDialog;
import com.vidmt.lmei.entity.Model;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.entity.Present;
import com.vidmt.lmei.entity.SmsCode;
import com.vidmt.lmei.util.rule.ManageDataBase;
import com.vidmt.lmei.util.rule.ScreenUtils;
import com.vidmt.lmei.util.rule.SharedPreferencesUtil;
import com.vidmt.lmei.util.think.DbUtil;
import com.vidmt.lmei.util.think.JsonUtil;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import io.rong.eventbus.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.OnSendMessageListener;
import io.rong.imkit.RongIM.SentMessageErrorCode;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.widget.AlterDialogFragment;
import io.rong.imkit.widget.InputView;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectionStatusListener;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.location.RealTimeLocationConstant;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.PublicServiceProfile;
import io.rong.imlib.model.UserInfo;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import u.aly.bp;

public class ConversationActivity extends FragmentActivity implements OnDataListener,RongIMClient.RealTimeLocationListener, View.OnClickListener,ConnectionStatusListener {

	private static final int GET_USER_INFO = 111;
	private static final int GET_GROUP_MEMBER = 100;
	private String TAG = ConversationActivity.class.getSimpleName();
	/**
	 * 对方id
	 */
	private String mTargetId;
	/**
	 * 自己id
	 * 
	 */
	private String targetId;
	/**
	 * 会话类型
	 */
	private Conversation.ConversationType mConversationType;
	/**
	 * title
	 */
	private String title;
	/**
	 * 是否在讨论组内，如果不在讨论组内，则进入不到讨论组设置页面
	 */
	private boolean isFromPush = false;

	private RelativeLayout mRealTimeBar;//real-time bar
	private RealTimeLocationConstant.RealTimeLocationStatus currentLocationStatus;
	private LoadingDialog mDialog;

	private SharedPreferences sp;

	private final String TextTypingTitle = "对方正在输入...";
	private final String VoiceTypingTitle = "对方正在讲话...";

	private Handler mHandler;
	private RongIM.IGroupMemberCallback mMentionMemberCallback;
	public static final int SET_TEXT_TYPING_TITLE = 1;
	public static final int SET_VOICE_TYPING_TITLE = 2;
	public static final int SET_TARGET_ID_TITLE = 0;
	private Button mRinghtButton;
	private ImageView liwubtn;//礼物按钮
	private ArrayList<Present> presents; 
	//送礼物
	private PersentAdapter  persentAdapter;
	private View parent;
	private Present cates;
	private TextView headtitle;
	private ImageView left1;
	private ImageView right1;
	private Persion persion;

	private RelativeLayout headerthemeleft;
	private LinearLayout headerright;

	View kk;
	private  int chattype;//1为对方聊天不收费2 为收费3为不能聊天
	private int token;
	private int catetoken;//礼物的金币数
	File photofile;
	protected DbUtil dbutil;
	protected  Persion persion_main; 
	int liwutype;//1为赠送2为没赠送
	private String[] titles = {"棒棒糖", "去污皂", "喜欢你", "萌猫耳", "金话筒", "皇冠", "跑车", "掌声", "信用卡", "桃心",
			"玫瑰", "跑车", "玩偶熊", "蛋糕", "发夹", "化妆品", "别墅", "马卡龙", "金卡", "冰激凌", "爱心", "包包"};
	private ViewPager mPager;
	private List<View> mPagerList;
	private List<Model> mDatas;
	private LinearLayout mLlDot;
	private LayoutInflater inflater;
	/**
	 * 总的页数
	 */
	private int pageCount;
	/**
	 * 每一页显示的个数
	 */
	private int pageSize = 8;
	/**
	 * 当前显示的是第几页
	 */
	private int curIndex = 0;

	private int chatpay;

	private int isblack=0;
	String str ;
	@Override
	@TargetApi(23)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
		setContentView(R.layout.activity_conversation);
		if (isFromPush) {

		}
		sp = getSharedPreferences("config", MODE_PRIVATE);
		mDialog = new LoadingDialog(this);

		//        setRightVisibility(View.INVISIBLE);

		Intent intent = getIntent();

		if (intent == null || intent.getData() == null){
			return;
		}else{
			mTargetId = intent.getData().getQueryParameter("targetId");
		}	
		//	targetId = intent.getData().getQueryParameter("mytargetId");
		//10000 为 Demo Server 加好友的 id，若 targetId 为 10000，则为加好友消息，默认跳转到 NewFriendListActivity
		// Demo 逻辑
		if (mTargetId != null && mTargetId.equals("10000")) { 

			return;
		}
		//intent.getData().getLastPathSegment();//获得当前会话类型

		mConversationType = Conversation.ConversationType.valueOf(intent.getData()
				.getLastPathSegment().toUpperCase(Locale.getDefault()));

		str= intent.getData().getQueryParameter("title");
		//截取#之前的字符串
		//String str = "sdfs#d";

		if (str.contains("//")) {

			title =str.substring(0, str.indexOf("//"));
			targetId = str.substring(str.indexOf("//")+2,str.length());
			dbutil = new DbUtil((TAApplication) this.getApplication());
			List<Persion>  	users= dbutil.selectData(Persion.class,null);
			if(users !=null)
			{
				if(users.size()>0)
				{
					try {
						persion_main = users.get(0);
						targetId=persion_main.getId().toString();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}

		}else {
			if (str.contains("/")) {
				title =str.substring(0, str.indexOf("/"));
				targetId = str.substring(str.indexOf("/")+1,str.length());
				dbutil = new DbUtil((TAApplication) this.getApplication());
				List<Persion>  	users= dbutil.selectData(Persion.class,null);
				if(users !=null)
				{
					if(users.size()>0)
					{
						try {
							persion_main = users.get(0);
							targetId=persion_main.getId().toString();
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			}else {
				title =str;

				dbutil = new DbUtil((TAApplication) this.getApplication());
				List<Persion>  	users= dbutil.selectData(Persion.class,null);
				if(users !=null)
				{
					if(users.size()>0)
					{
						try {
							persion_main = users.get(0);
							targetId=persion_main.getId().toString();
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			}
		}

		isPushMessage(intent);
		headtitle = (TextView) findViewById(R.id.headercontent);
		headerthemeleft = (RelativeLayout) findViewById(R.id.headerthemeleft);
		headerthemeleft.setVisibility(View.VISIBLE);
		headerright = (LinearLayout) findViewById(R.id.headerright);
		setActionBarTitle(mConversationType, mTargetId); 
		if (mConversationType == Conversation.ConversationType.CUSTOMER_SERVICE) {
			headerright.setVisibility(View.GONE);
		}else {
			headerright.setVisibility(View.VISIBLE);
			getpersion();

			gettpersion();
			//请求礼物\

			genpresent();
			loaduserinfo();

		}



		right1=(ImageView) findViewById(R.id.typelog);
		left1 =(ImageView) findViewById(R.id.user);
		kk = findViewById(R.id.kk);

		left1.setImageResource(R.drawable.goback);
		right1.setImageResource(R.drawable.persons);
		headerthemeleft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!closeRealTimeLocation()) {
					if (fragment != null && !fragment.onBackPressed()) {
						if (isFromPush) {
							isFromPush = false;
							//						startActivity(new Intent(this, MainActivity.class));
						}
						finish();
						//				      try {
						//				    	  SealAppContext.getInstance().popAllActivity();
						//					} catch (Exception e) {
						//						// TODO: handle exception
						//						e.printStackTrace();
						//					}

					}
				}
			}
		});

		headerright.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (str.contains("//")) {
					if(mTargetId!=null&&!mTargetId.equals("")&&!mTargetId.equals("0")){
						Intent intent = new Intent(ConversationActivity.this, HomeDetailActivity.class);				
						intent.putExtra("userid",Integer.valueOf(mTargetId).intValue());
						intent.putExtra("chattype", 1);
						ConversationActivity.this.startActivity(intent);
					}

				}else {
					finish();
				}




			}
		});
		//themes();
		//地理位置共享，若不是用地理位置共享，可忽略
		setRealTime();


		//		if ("ConversationActivity".equals(this.getClass().getSimpleName())){
		//			EventBus.getDefault().register(this);
		//		}



		// android 6.0 以上版本，监听SDK权限请求，弹出对应请求框。




		liwubtn = (ImageView) findViewById(R.id.liwubtn);
		if (mConversationType == Conversation.ConversationType.CUSTOMER_SERVICE) {
			liwubtn.setVisibility(View.GONE);
		}
		liwubtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new PopupWindows(ConversationActivity.this,kk,1);
			}
		});

		mHandler = new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(android.os.Message msg) {
				// TODO Auto-generated method stub

				switch (msg.what) {
				case SET_TEXT_TYPING_TITLE:
					setTitle(TextTypingTitle);
					break;
				case SET_VOICE_TYPING_TITLE:
					setTitle(VoiceTypingTitle);
					break;
				case SET_TARGET_ID_TITLE:
					setActionBarTitle(mConversationType, mTargetId);
					break;
				default:
					break;
				}
				return true;

			}
		});


		RongIMClient.setTypingStatusListener(new RongIMClient.TypingStatusListener() {
			@Override
			public void onTypingStatusChanged(Conversation.ConversationType type, String targetId, Collection<TypingStatus> typingStatusSet) {
				//当输入状态的会话类型和targetID与当前会话一致时，才需要显示
				if (type.equals(mConversationType) && targetId.equals(mTargetId)) {
					int count = typingStatusSet.size();
					//count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
					if (count > 0) {
						Iterator iterator = typingStatusSet.iterator();
						TypingStatus status = (TypingStatus) iterator.next();
						String objectName = status.getTypingContentType();

						MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
						MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
						//匹配对方正在输入的是文本消息还是语音消息
						if (objectName.equals(textTag.value())) {
							mHandler.sendEmptyMessage(SET_TEXT_TYPING_TITLE);
						} else if (objectName.equals(voiceTag.value())) {
							mHandler.sendEmptyMessage(SET_VOICE_TYPING_TITLE);
						}
					} else {//当前会话没有用户正在输入，标题栏仍显示原来标题
						mHandler.sendEmptyMessage(SET_TARGET_ID_TITLE);
					}
				}
			}
		});
		//发送前判断 如果可以发送return false如果不可以return ture;
		RongIM.getInstance().setSendMessageListener(new OnSendMessageListener() {

			@Override
			public boolean onSent(io.rong.imlib.model.Message arg0, SentMessageErrorCode arg1) {
				// TODO Auto-generated method stub
				if (mConversationType == Conversation.ConversationType.CUSTOMER_SERVICE) {
					return true;
				}else {
					if (chattype==1) {
						if (liwutype==1) {
							liwutype=2;
						}else {
							//sendTextBlackMessage("消费了0金币", 2);
						}
						return false;
					}else {
						if (token>10) {

							if (liwutype==1) {
								liwutype=2;
							}else {

							}

							return false;
						}else {


							return true;
						}
					}

				}

			}

			@Override
			public io.rong.imlib.model.Message onSend(io.rong.imlib.model.Message arg0) {
				// TODO Auto-generated method stub




				if (mConversationType == Conversation.ConversationType.CUSTOMER_SERVICE) {
					return arg0;
				}else {

					if (chattype==1) {
						if (liwutype==1) {
							liwutype=2;
						}else {

						}
						return arg0;
					}else if (chattype==3) {


						return null;
					}else {
						if (token>10) {
							if (liwutype==1) {
								liwutype=2;
							}else {

								if (arg0.getContent() instanceof ImageMessage) {

									if (arg0.getMessageId()==0) {



									}else {
										sendmes();	
									}
								}else {
									sendmes();
								}







							}
							return arg0;
						}else {

							if (liwutype==1) {
								liwutype=2;
							}else {


								if (persion_main!=null) {

									if (persion.getSms_state()==2) {

										sendTextBlackMessage("对方未开启聊天", 2);
										return null;	

									}else {
										if (persion_main.getToken()==null) {

											sendTextBlackMessage("您的金币不足，请充值后再聊天。", 2);
											LoadDataUpdate();
											//	MainActivity.mainactivity.Tankuang(4);
											Intent in = new Intent(ConversationActivity.this,CloseAccountActivity.class);
											in.putExtra("type", 4);
											startActivity(in);
											return null; 
										}else {

											if (persion_main.getToken()==0) {


												if (chatpay==0) {
													sendmes();

													return arg0;
												}else {
													sendTextBlackMessage("您的金币不足，请充值后再聊天。", 2);
													LoadDataUpdate();
													Intent in = new Intent(ConversationActivity.this,CloseAccountActivity.class);
													in.putExtra("type", 4);
													startActivity(in);

													//													MainActivity.mainactivity.Tankuang(4);
												}


												return null;
											}else {

												if (persion_main.getToken()<persion.getSms_money()) {
													sendTextBlackMessage("您的金币不足，请充值后再聊天。", 2);
													LoadDataUpdate();
													//MainActivity.mainactivity.Tankuang(4);
													Intent in = new Intent(ConversationActivity.this,CloseAccountActivity.class);
													in.putExtra("type", 4);
													startActivity(in);

													return null;
												}else {
													if (arg0.getContent() instanceof ImageMessage) {

														if (arg0.getMessageId()==0) {

														}else {
															sendmes();
														}
													}else {


														sendmes();
													}
												}

											}
										}


									}

								}else {

								}


							}
							return arg0;


						}
					}

					//	arg0.getExtra();

				}

			}
		});

		//		SealAppContext.getInstance().pushActivity(this);






	}


	public void themes() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			Window win = getWindow();
			// win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = ScreenUtils.getStatusHeight(this);
			View layoutAll = findViewById(R.id.linheader);
			// 设置系统栏需要的内偏移
			layoutAll.setPadding(0, ScreenUtils.getStatusHeight(this), 0, 0);
			LinearLayout linheader = (LinearLayout) findViewById(R.id.linheader);
			if (linheader != null) {
				int height = linheader.getLayoutParams().height;
				linheader.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height + bits));
			}
		}
	}
	// 发送小灰条消息。
	@SuppressWarnings("deprecation")
	public void sendTextBlackMessage(String text,int type) {
		InformationNotificationMessage  informationNotificationMessage;
		if (type==1) {
			informationNotificationMessage = InformationNotificationMessage.obtain("聊天时请保持社交礼仪，如果出现谩骂、\n色情及骚扰信息，请及时举报，一旦核实\n立即封号。");

		}else {
			informationNotificationMessage = InformationNotificationMessage.obtain(text);

		}
		//	RongIM.getInstance().insertMessage(ConversationType.PRIVATE, targetId, targetId, informationNotificationMessage );
		RongIM.getInstance().insertMessage(ConversationType.PRIVATE, mTargetId, targetId, informationNotificationMessage, new ResultCallback<Message>() {

			@Override
			public void onError(ErrorCode arg0) {
				// TODO Auto-generated method stub
				arg0.getValue();
			}

			@Override
			public void onSuccess(Message arg0) {
				// TODO Auto-generated method stub
				arg0.getContent();
			}
		});

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	/**
	 *请求用户资料
	 * 
	 */

	private void getpersion() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String Rankingmap =  Person_Service.persionOne(targetId);
				android.os.Message msg = mUIHandler.obtainMessage(1);
				msg.obj = Rankingmap;
				msg.sendToTarget();				
			}
		}).start();

	}
	public void loaduserinfo() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mTargetId.contains("KEFU")) {

				}else {

					int mTargetIds = Integer.valueOf(mTargetId).intValue();
					int targetIds = Integer.valueOf(targetId).intValue();
					String jhuser = Person_Service.loaduserinfo(targetIds,mTargetIds );
					android.os.Message msg = mUIHandler.obtainMessage(6);
					msg.obj = jhuser;
					msg.sendToTarget();
				}

			}
		}).start();
	}
	public void loaduserinfos() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				if (mTargetId.contains("KEFU")) {

				}else {
					int mTargetIds = Integer.valueOf(mTargetId).intValue();
					int targetIds = Integer.valueOf(targetId).intValue();
					String jhuser = Person_Service.loaduserinfo(targetIds,mTargetIds );
					android.os.Message msg = mUIHandler.obtainMessage(7);
					msg.obj = jhuser;
					msg.sendToTarget();
				}



			}
		}).start();
	}
	/**
	 *请求用户资料
	 * 
	 */

	private void gettpersion() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String Rankingmap =  Person_Service.persionOne(mTargetId);
				android.os.Message msg = mUIHandler.obtainMessage(5);
				msg.obj = Rankingmap;
				msg.sendToTarget();				
			}
		}).start();

	}
	/**
	 *发送消息扣钱
	 * 
	 */

	private void sendmes() {
		// TODO Auto-generated method stub

		if (mConversationType == Conversation.ConversationType.CUSTOMER_SERVICE) {

		}else {


			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String Rankingmap =  Chat_Service.sendmes(targetId,mTargetId,chatpay);
					android.os.Message msg = mUIHandler.obtainMessage(2);
					msg.obj = Rankingmap;
					msg.sendToTarget();				
				}
			}).start();
		}
	}
	/**
	 *请求礼物
	 *
	 * 
	 */
	private void genpresent() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String json =Chat_Service.getpresent();
				android.os.Message  msg = mUIHandler.obtainMessage(3);
				msg.obj = json;
				msg.sendToTarget();		
			}
		}).start();

	}
	/**
	 * 发送礼物
	 * @param buy_id
	 * @param sell_id
	 * @param token
	 * @param id
	 */
	private void sendpresent(final String buy_id,final String sell_id,final int id) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				String json =Chat_Service.sendpresent(buy_id, sell_id, id);
				android.os.Message msg = mUIHandler.obtainMessage(4);
				msg.obj = JsonUtil.JsonToObj(json, String.class);
				msg.sendToTarget();
			}
		}).start();
	}
	private Handler mUIHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if(msg.obj!=null)
				{
					String mes = (String) msg.obj;
					if("".equals(mes)){

						Toast.makeText(ConversationActivity.this, "网络异常，请稍后再试", Toast.LENGTH_SHORT).show();
					}else {
						Persion p = JsonUtil.JsonToObj(mes, Persion.class);
						RongIM.getInstance().refreshUserInfoCache(new UserInfo(p.getId() + "",
								p.getNick_name(), Uri.parse(p.getPhoto())));


						//						persion_main =p;
						//						token = persion.getToken();	
					}


				}

				break;
			case 2:

				if (msg.obj != null) {
					String mes = (String) msg.obj;
					if ("".equals(mes)) {

					}else {


						/**
						 * 返回编码   
						 * 0--正常，在这种情况下可直接取token
						 * 1--自己在对方的黑名单中，这个时候不会扣掉自己的钱
						 * 2--对方修改了文字的收费标准，这个情况下去取sms_moeny，token，返回对方的聊天标准，自己剩余的金币
						 * 3--对方聊天免费
						 * 1001--状态异常  直接取message里的提示文字即可
						 * 500--未知错误，此情况下不取其他值。提示稍后再试等。
						 */
						SmsCode  smsCode  = JsonUtil.JsonToObj(mes, SmsCode.class);

						switch (smsCode.getCode()) {
						case 0:
							token=smsCode.getToken();
							sendTextBlackMessage("消费了"+chatpay+"金币", 2);
							LoadDataUpdate();
							break;
						case 1:
							chattype=3;
							sendTextBlackMessage("您已被对方拉黑，不能继续聊天！", 2);
							SharedPreferencesUtil.putInt(ConversationActivity.this, "lblack", 1);
							break;
						case 2:
							token=smsCode.getToken();
							sendTextBlackMessage("消费了"+chatpay+"金币", 2);
							sendTextBlackMessage(smsCode.getMessage(), 2);
							chatpay = smsCode.getSms_money();
							LoadDataUpdate();
							break;
						case 3:
							chattype=1;
							sendTextBlackMessage("消费了0金币", 2);
							LoadDataUpdate();
							break;
						case 1001:
							chattype=3;
							sendTextBlackMessage(smsCode.getMessage(), 2);
							if (smsCode.getMessage().contains("金币不足")) {
								LoadDataUpdate();
								Intent in = new Intent(ConversationActivity.this,CloseAccountActivity.class);
								in.putExtra("type", 4);
								startActivity(in);
							}else if (smsCode.getMessage().contains("拉黑")) {
								SharedPreferencesUtil.putInt(ConversationActivity.this, "lblack", 1);
							}else {

							}
							break;
						case 500:
							chattype=3;
							sendTextBlackMessage(smsCode.getMessage(), 2);
							LoadDataUpdate();
							break;

						default:
							break;
						}




					}

				}else {

				}

				break;
			case 3:
				if (msg.obj != null) {
					String str = (String)msg.obj;
					if (str.equals("error")||str.equals("")) {

						Toast .makeText(ConversationActivity.this, "网络连接失败，请检查您的网络连接", Toast.LENGTH_SHORT).show();

					}else {
						try {
							presents = new ArrayList<Present>();
							presents = JsonUtil.JsonToObj(str, new TypeToken<List<Present>>(){}.getType());
							persentAdapter = new PersentAdapter(ConversationActivity.this, presents, 1);
						} catch (Exception e) {
							// TODO: handle exception
							genpresent();
						}


						//forumAllCateAdapter = new ForumAllCateAdapter(ForumActivity.this, fcategorys,1);

					}
					//loadingDialog.dismiss();
				}
				break;
			case 4:
				//   异常 = error
				//		           对方被禁用 = sell_error
				//		           自己被禁用 = buy_error
				//		          自己金币不足 = buy_nomoney
				//		          成功 = 返回自己的剩余金币   int  类型
				if (msg.obj != null) {
					String mes = (String) msg.obj;
					if("".equals(mes)){
						//	ToastShow("网络异常，请检查网络连接");
					}else if (mes.contains("sell_error")) {
						chattype=3;
						sendTextBlackMessage("对方被禁用，您不能和他聊天", 2);
					}else if (mes.contains("buy_error")) {
						chattype=3;
						sendTextBlackMessage("您已被禁用，您不能和他聊天", 2);
					}else if (mes.contains("buy_nomoney")) {
						chattype=3;
						sendTextBlackMessage("您的金币不足，请充值后再聊天。", 2);
						//MainActivity.mainactivity.Tankuang(4);
						LoadDataUpdate();
						Intent in = new Intent(ConversationActivity.this,CloseAccountActivity.class);
						in.putExtra("type", 4);
						startActivity(in);
					}else{

						token=Integer.valueOf(mes).intValue();
						sendTextBlackMessage("消费了"+catetoken+"金币", 2);
						Toast.makeText(ConversationActivity.this,"发送成功", Toast.LENGTH_SHORT).show();
						LoadDataUpdate();
						//ToastShow(mes+ip);
					}
				}
				break;
			case 5:
				if(msg.obj!=null)
				{
					String mes = (String) msg.obj;
					if("".equals(mes)){
						Toast.makeText(ConversationActivity.this, "网络异常，请稍后再试", Toast.LENGTH_SHORT).show();
					}else {
						try {
							Persion p = JsonUtil.JsonToObj(mes, Persion.class);
							persion =p;
							int ltype = SharedPreferencesUtil.getInt(ConversationActivity.this, "ltype", 0);


							if (p.getVideo_state()!=1) {
								SharedPreferencesUtil.putInt(ConversationActivity.this, "vstype", 1);

							}else {
								SharedPreferencesUtil.putInt(ConversationActivity.this, "vstype", 0);
							}
							if (p.getVoice_state()!=1) {

								SharedPreferencesUtil.putInt(ConversationActivity.this, "astype", 1);
							}else {
								SharedPreferencesUtil.putInt(ConversationActivity.this, "astype", 0);
							}
							if (p.getIsLike()!=null) {

								if (ltype==1) {

								}else {

									SharedPreferencesUtil.putInt(ConversationActivity.this, "ltype", 1);
								}
							}else {

								if (ltype==1) {
									SharedPreferencesUtil.putInt(ConversationActivity.this, "ltype", 0);
								}else {

								}

							}
							RongIM.getInstance().refreshUserInfoCache(new UserInfo(p.getId() + "",
									p.getNick_name(), Uri.parse(p.getPhoto())));
						} catch (Exception e) {
							gettpersion();
						}
						

					}
				}
				break;
			case 6:
				if(msg.obj!=null)
				{
					String mes = (String) msg.obj;
					if("".equals(mes)){
						Toast.makeText(ConversationActivity.this, "网络异常，请稍后再试", Toast.LENGTH_SHORT).show();
					}else {
						Persion p = JsonUtil.JsonToObj(mes, Persion.class);
						int ltype = SharedPreferencesUtil.getInt(ConversationActivity.this, "ltype", 0);
						chatpay = p.getSms_money();
						headtitle.setText(p.getNick_name());
						sendTextBlackMessage("",1);
						sendTextBlackMessage("对方普通聊天收费标准："+chatpay+"金币/条", 2);
						if (p.getVideo_state()!=1) {
							SharedPreferencesUtil.putInt(ConversationActivity.this, "astype", 1);

						}else {
							SharedPreferencesUtil.putInt(ConversationActivity.this, "astype", 0);
						}
						if (p.getVoice_state()!=1) {

							SharedPreferencesUtil.putInt(ConversationActivity.this, "vstype", 1);
						}else {
							SharedPreferencesUtil.putInt(ConversationActivity.this, "vstype", 0);
						}

						if(p.getIsBlack()!=null){
							isblack=p.getIsBlack();

							chattype=3;
							SharedPreferencesUtil.putInt(ConversationActivity.this, "lblack", 1);

						}else{

							isblack=0;

							SharedPreferencesUtil.putInt(ConversationActivity.this, "lblack", 0);

						}
						if (p.getIsLike()!=null) {

							if (ltype==1) {

							}else {
								SharedPreferencesUtil.putInt(ConversationActivity.this, "ltype", 1);
							}
						}else {

							if (ltype==1) {
								SharedPreferencesUtil.putInt(ConversationActivity.this, "ltype", 0);
							}else {

							}

						}
						RongIM.getInstance().refreshUserInfoCache(new UserInfo(p.getId() + "",
								p.getNick_name(), Uri.parse(p.getPhoto())));

					}


				}

				break;
			case 7:
				if(msg.obj!=null)
				{
					String mes = (String) msg.obj;
					if("".equals(mes)){
						Toast.makeText(ConversationActivity.this, "网络异常，请稍后再试", Toast.LENGTH_SHORT).show();
					}else {
						Persion p = JsonUtil.JsonToObj(mes, Persion.class);
						int ltype = SharedPreferencesUtil.getInt(ConversationActivity.this, "ltype", 0);
						chatpay = p.getSms_money();
						headtitle.setText(p.getNick_name());

						if (p.getVideo_state()!=1) {
							SharedPreferencesUtil.putInt(ConversationActivity.this, "astype", 1);

						}else {
							SharedPreferencesUtil.putInt(ConversationActivity.this, "astype", 0);
							SharedPreferencesUtil.putInt(ConversationActivity.this, "ameney", p.getVoice_money());

						}
						if (p.getVoice_state()!=1) {

							SharedPreferencesUtil.putInt(ConversationActivity.this, "vstype", 1);
						}else {
							SharedPreferencesUtil.putInt(ConversationActivity.this, "vstype", 0);
							SharedPreferencesUtil.putInt(ConversationActivity.this, "vmeney", p.getVideo_money());
						}

						if(p.getIsBlack()!=null){
							isblack=p.getIsBlack();

							chattype=3;
							SharedPreferencesUtil.putInt(ConversationActivity.this, "lblack", 1);

						}else{

							isblack=0;

							SharedPreferencesUtil.putInt(ConversationActivity.this, "lblack", 0);

						}
						if (p.getIsLike()!=null) {

							if (ltype==1) {

							}else {
								SharedPreferencesUtil.putInt(ConversationActivity.this, "ltype", 1);
							}
						}else {

							if (ltype==1) {
								SharedPreferencesUtil.putInt(ConversationActivity.this, "ltype", 0);
							}else {

							}

						}
						RongIM.getInstance().refreshUserInfoCache(new UserInfo(p.getId() + "",
								p.getNick_name(), Uri.parse(p.getPhoto())));

					}


				}

				break;
			default:
				break;
			}
		};
	};



	/**
	 * 判断是否是 Push 消息，判断是否需要做 connect 操作
	 */
	private void isPushMessage(Intent intent) {

		if (intent == null || intent.getData() == null)
			return;

		//push
		if (intent.getData( ).getScheme().equals("rong") && intent.getData().getQueryParameter("isFromPush") != null) {

			//通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
			if (intent.getData().getQueryParameter("isFromPush").equals("true")) {
				//只有收到系统消息和不落地 push 消息的时候，pushId 不为 null。而且这两种消息只能通过 server 来发送，客户端发送不了。
				//                RongIM.getInstance().getRongIMClient().recordNotificationEvent(id);
				if (mDialog != null && !mDialog.isShowing()) {
					mDialog.show();
				}
				isFromPush = true;
				RongIM.init(this);
				enterActivity();
			} else if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
				if (mDialog != null && !mDialog.isShowing()) {
					mDialog.show();
					//connect(persion_main.getRongyuntoken(), persion_main);
				}
				if (intent.getData().getPath().contains("conversation/system")) {
					//					Intent intent1 = new Intent(mContext, MainActivity.class);
					//					intent1.putExtra("systemconversation", true);
					//					startActivity(intent1);
					//					finish();
					return;
				}
				RongIM.init(this);
				enterActivity();
			} else {

				//程序切到后台，收到消息后点击进入,会执行这里
				if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {

					///connect(persion_main.getRongyuntoken(), persion_main);
				} else {
					RongIM.init(this);
					enterFragment(mConversationType, mTargetId);
					//Toast.makeText(ConversationActivity.this, "home", 0).show(); 
				}

				if (intent.getData().getPath().contains("conversation/system")) {
					//					Intent intent1 = new Intent(mContext, MainActivity.class);
					//					intent1.putExtra("systemconversation", true);
					//					startActivity(intent1);
					//					finish();
					return;
				}
				enterActivity();
				enterFragment(mConversationType, mTargetId);
			}

		} else {
			if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
				if (mDialog != null && !mDialog.isShowing()) {
					mDialog.show();
				}

				RongIM.init(this);
				//Toast.makeText(ConversationListActivity.this, "rong", Toast.LENGTH_SHORT).show();
				
			
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						enterActivity();
					}
				}, 300);
			} else {
				enterFragment(mConversationType, mTargetId);
			}
		}
	}


	/**
	 * 收到 push 消息后，选择进入哪个 Activity
	 * 如果程序缓存未被清理，进入 MainActivity
	 * 程序缓存被清理，进入 LoginActivity，重新获取token
	 * <p/>
	 * 作用：由于在 manifest 中 intent-filter 是配置在 ConversationActivity 下面，所以收到消息后点击notifacition 会跳转到 DemoActivity。
	 * 以跳到 MainActivity 为例：
	 * 在 ConversationActivity 收到消息后，选择进入 MainActivity，这样就把 MainActivity 激活了，当你读完收到的消息点击 返回键 时，程序会退到
	 * MainActivity 页面，而不是直接退回到 桌面。
	 */
	private void enterActivity() {

		String tokens = null;
		if (persion_main==null) {
			dbutil = new DbUtil((TAApplication) this.getApplication());
			List<Persion>  	users= dbutil.selectData(Persion.class,null);
			if(users !=null)
			{
				if(users.size()>0)
				{
					try {
						persion_main = users.get(0);
						targetId=persion_main.getId().toString();
						tokens = persion_main.getRongyuntoken();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}else {
			tokens = persion_main.getRongyuntoken();
		}


		if ("default".equals(tokens)) {
			Log.e("ConversationActivity push", "push2");
			//			startActivity(new Intent(ConversationActivity.this, LoginActivity.class));
			//			finish();
			reconnect(tokens);
		} else {
			Log.e("ConversationActivity push", "push3");
			reconnect(tokens);
		}
	}

	private void reconnect(String token) {

		RongIM.connect(token, new RongIMClient.ConnectCallback() {
			@Override
			public void onTokenIncorrect() {

				Log.e(TAG, "---onTokenIncorrect--");
			}

			@Override
			public void onSuccess(String s) {
				Log.i(TAG, "---onSuccess--" + s);
				Log.e("ConversationActivity push", "push4");

				if (mDialog != null)
					mDialog.dismiss();           

				//
				//                Intent intent = new Intent();
				//                intent.setClass(ConversationActivity.this, MainActivity.class);
				//                intent.putExtra("PUSH_CONVERSATIONTYPE", mConversationType.toString());
				//                intent.putExtra("PUSH_TARGETID", mTargetId);
				//                startActivity(intent);
				//                finish();

				enterFragment(mConversationType, mTargetId);

			}

			@Override
			public void onError(RongIMClient.ErrorCode e) {
				Log.e(TAG, "---onError--" + e);
				if (mDialog != null)
					mDialog.dismiss();

				enterFragment(mConversationType, mTargetId);
			}
		});

	}

	private ConversationFragment fragment;

	/**
	 * 加载会话页面 ConversationFragment
	 *
	 * @param mConversationType 会话类型
	 * @param mTargetId         会话 Id
	 */
	private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

		fragment = new ConversationFragment();

		Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
				.appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
				.appendQueryParameter("targetId", mTargetId).build();

		fragment.setUri(uri);
		fragment.setInputBoardListener(new InputView.IInputBoardListener() {
			@Override
			public void onBoardExpanded(int height) {
				Log.e(TAG, "onBoardExpanded h : " + height);
			}

			@Override
			public void onBoardCollapsed() {
				Log.e(TAG, "onBoardCollapsed");
			}
		});

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		//xxx 为你要加载的 id
		transaction.add(R.id.rong_content, fragment);
		transaction.commitAllowingStateLoss();
	}


	/**
	 * 设置会话页面 Title
	 *
	 * @param conversationType 会话类型
	 * @param targetId         目标 Id
	 */
	private void setActionBarTitle(Conversation.ConversationType conversationType, String targetId) {

		if (conversationType == null)
			return;

		if (conversationType.equals(Conversation.ConversationType.PRIVATE)) {
			setPrivateActionBar(targetId);
		} else if (conversationType.equals(Conversation.ConversationType.GROUP)) {
			setGroupActionBar(targetId);
		} else if (conversationType.equals(Conversation.ConversationType.DISCUSSION)) {
			setDiscussionActionBar(targetId);
		} else if (conversationType.equals(Conversation.ConversationType.CHATROOM)) {
			setTitle(title);
		} else if (conversationType.equals(Conversation.ConversationType.SYSTEM)) {
			setTitle(R.string.de_actionbar_system);
		} else if (conversationType.equals(Conversation.ConversationType.APP_PUBLIC_SERVICE)) {
			setAppPublicServiceActionBar(targetId);
		} else if (conversationType.equals(Conversation.ConversationType.PUBLIC_SERVICE)) {
			setPublicServiceActionBar(targetId);
		} else if (conversationType.equals(Conversation.ConversationType.CUSTOMER_SERVICE)) {
			setTitle(R.string.main_customer);
		} else {
			setTitle(R.string.de_actionbar_sub_defult);
		}

	}

	/**
	 * 设置群聊界面 ActionBar
	 *
	 * @param targetId 会话 Id
	 */
	private void setGroupActionBar(String targetId) {
		if (!TextUtils.isEmpty(title)) {
			setTitle(title);
		} else {
			setTitle(targetId);
		}
	}

	/**
	 * 设置应用公众服务界面 ActionBar
	 */
	private void setAppPublicServiceActionBar(String targetId) {
		if (targetId == null)
			return;

		RongIM.getInstance().getPublicServiceProfile(Conversation.PublicServiceType.APP_PUBLIC_SERVICE
				, targetId, new RongIMClient.ResultCallback<PublicServiceProfile>() {
			@Override
			public void onSuccess(PublicServiceProfile publicServiceProfile) {
				setTitle(publicServiceProfile.getName());
			}

			@Override
			public void onError(RongIMClient.ErrorCode errorCode) {

			}
		});
	}

	/**
	 * 设置公共服务号 ActionBar
	 */
	private void setPublicServiceActionBar(String targetId) {

		if (targetId == null)
			return;


		RongIM.getInstance().getPublicServiceProfile(Conversation.PublicServiceType.PUBLIC_SERVICE
				, targetId, new RongIMClient.ResultCallback<PublicServiceProfile>() {
			@Override
			public void onSuccess(PublicServiceProfile publicServiceProfile) {
				setTitle(publicServiceProfile.getName());
			}

			@Override
			public void onError(RongIMClient.ErrorCode errorCode) {

			}
		});
	}

	/**
	 * 设置讨论组界面 ActionBar
	 */
	private void setDiscussionActionBar(String targetId) {

		if (targetId != null) {

			RongIM.getInstance().getDiscussion(targetId
					, new RongIMClient.ResultCallback<Discussion>() {
				@Override
				public void onSuccess(Discussion discussion) {
					setTitle(discussion.getName());
				}

				@Override
				public void onError(RongIMClient.ErrorCode e) {
					if (e.equals(RongIMClient.ErrorCode.NOT_IN_DISCUSSION)) {
						setTitle("不在讨论组中");
						supportInvalidateOptionsMenu();
					}
				}
			});
		} else {
			setTitle("讨论组");
		}
	}


	/**
	 * 设置私聊界面 ActionBar
	 */
	private void setPrivateActionBar(String targetId) {
		if (!TextUtils.isEmpty(title)) {
			setTitle(title);
		} else {
			setTitle(targetId);
		}
	}

	/**
	 * 根据 targetid 和 ConversationType 进入到设置页面
	 */
	private void enterSettingActivity() {

		if (mConversationType == Conversation.ConversationType.PUBLIC_SERVICE
				|| mConversationType == Conversation.ConversationType.APP_PUBLIC_SERVICE) {

			RongIM.getInstance().startPublicServiceProfile(this, mConversationType, mTargetId);
		} else {
			//			UriFragment fragment = (UriFragment) getSupportFragmentManager().getFragments().get(0);
			//			//得到讨论组的 targetId
			//			mTargetId = fragment.getUri().getQueryParameter("targetId");

			if (TextUtils.isEmpty(mTargetId)) {
				//Toast.shortToast(mContext, "讨论组尚未创建成功");
			}


			Intent intent = null;
			if (mConversationType == Conversation.ConversationType.GROUP) {
				//intent = new Intent(this, GroupDetailActivity.class);
			} else if (mConversationType == Conversation.ConversationType.PRIVATE) {
				//intent = new Intent(this, FriendDetailActivity.class);
			} else if (mConversationType == Conversation.ConversationType.DISCUSSION) {
				//				intent = new Intent(this, DiscussionDetailActivity.class);
				//				intent.putExtra("TargetId", mTargetId);
				//				startActivityForResult(intent, 166);
				return;
			}else if (mConversationType == Conversation.ConversationType.CUSTOMER_SERVICE) {

			}
			intent.putExtra("TargetId", mTargetId);
			if (intent != null) {
				startActivityForResult(intent, 500);
			}

		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		//        if (data!=null && data.getStringExtra("disFinish").equals("disFinish")) {
		//            finish();
		//        }
		if (resultCode == 501) {
			finish();
		}
	}


	@Override
	protected void onResume() {
		super.onResume();
		showRealTimeLocationBar(null);
		if (mConversationType == Conversation.ConversationType.CUSTOMER_SERVICE) {

		}else{
			loaduserinfos();
		}
	}


	/*－－－－－－－－－－－－－地理位置共享 start－－－－－－－－－*/

	private void setRealTime() {

		mRealTimeBar = (RelativeLayout) this.findViewById(R.id.layout);

		mRealTimeBar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (currentLocationStatus == null)
					currentLocationStatus = RongIMClient.getInstance().getRealTimeLocationCurrentState(mConversationType, mTargetId);

				if (currentLocationStatus == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_INCOMING) {

					final AlterDialogFragment alterDialogFragment = AlterDialogFragment.newInstance("", "加入位置共享", "取消", "加入");
					alterDialogFragment.setOnAlterDialogBtnListener(new AlterDialogFragment.AlterDialogBtnListener() {

						@Override
						public void onDialogPositiveClick(AlterDialogFragment dialog) {
							RealTimeLocationConstant.RealTimeLocationStatus status = RongIMClient.getInstance().getRealTimeLocationCurrentState(mConversationType, mTargetId);

							if (status == null || status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_IDLE) {
								startRealTimeLocation();
							} else {
								joinRealTimeLocation();
							}

						}

						@Override
						public void onDialogNegativeClick(AlterDialogFragment dialog) {
							alterDialogFragment.dismiss();
						}
					});
					alterDialogFragment.show(getSupportFragmentManager());

				} else {
					//					Intent intent = new Intent(ConversationActivity.this, RealTimeLocationActivity.class);
					//					intent.putExtra("conversationType", mConversationType.getValue());
					//					intent.putExtra("targetId", mTargetId);
					//					startActivity(intent);
				}
			}
		});

		if (!TextUtils.isEmpty(mTargetId) && mConversationType != null) {

			RealTimeLocationConstant.RealTimeLocationErrorCode errorCode = RongIMClient.getInstance().getRealTimeLocation(mConversationType, mTargetId);
			if (errorCode == RealTimeLocationConstant.RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_SUCCESS || errorCode == RealTimeLocationConstant.RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_IS_ON_GOING) {
				RongIMClient.getInstance().addRealTimeLocationListener(mConversationType, mTargetId, this);//设置监听
				currentLocationStatus = RongIMClient.getInstance().getRealTimeLocationCurrentState(mConversationType, mTargetId);

				if (currentLocationStatus == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_INCOMING) {
					showRealTimeLocationBar(currentLocationStatus);
				}
			}
		}


	}

	//real-time location method beign

	private void startRealTimeLocation() {
		RongIMClient.getInstance().startRealTimeLocation(mConversationType, mTargetId);
		//		Intent intent = new Intent(ConversationActivity.this, RealTimeLocationActivity.class);
		//		intent.putExtra("conversationType", mConversationType.getValue());
		//		intent.putExtra("targetId", mTargetId);
		//		startActivity(intent);
	}

	private void joinRealTimeLocation() {
		RongIMClient.getInstance().joinRealTimeLocation(mConversationType, mTargetId);
		//		Intent intent = new Intent(ConversationActivity.this, RealTimeLocationActivity.class);
		//		intent.putExtra("conversationType", mConversationType.getValue());
		//		intent.putExtra("targetId", mTargetId);
		//		startActivity(intent);
	}

	@Override
	public void onBackPressed() {


		if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
			closeRealTimeLocation();
		}
		if (isFromPush) {
			isFromPush = false;
			//			startActivity(new Intent(this, MainActivity.class));
			//			finish();
		}
		if(!fragment.onBackPressed()) {
			finish();
		}

	}


	private boolean closeRealTimeLocation() {

		if (mConversationType == null || TextUtils.isEmpty(mTargetId))
			return false;

		if (mConversationType != null && !TextUtils.isEmpty(mTargetId)) {

			RealTimeLocationConstant.RealTimeLocationStatus status = RongIMClient.getInstance().getRealTimeLocationCurrentState(mConversationType, mTargetId);

			if (status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_IDLE || status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_INCOMING) {
				return false;
			}
		}

		final AlterDialogFragment alterDialogFragment =
				AlterDialogFragment.newInstance(getApplicationContext().getString(R.string.prompt),
						getApplicationContext().getString(R.string.location_sharing_exit_promt),
						getApplicationContext().getString(R.string.cancel),
						getApplicationContext().getString(R.string.confirm));
		alterDialogFragment.setOnAlterDialogBtnListener(new AlterDialogFragment.AlterDialogBtnListener() {
			@Override
			public void onDialogPositiveClick(AlterDialogFragment dialog) {
				RongIMClient.getInstance().quitRealTimeLocation(mConversationType, mTargetId);
				SealAppContext.getInstance().popAllActivity();
			}

			@Override
			public void onDialogNegativeClick(AlterDialogFragment dialog) {
				alterDialogFragment.dismiss();
			}
		});
		alterDialogFragment.show(getSupportFragmentManager());

		return true;
	}


	private String locationid;

	private void showRealTimeLocationBar(RealTimeLocationConstant.RealTimeLocationStatus status) {

		//		if (status == null)
		//			status = RongIMClient.getInstance().getRealTimeLocationCurrentState(mConversationType, mTargetId);
		//
		//		final List<String> userIds = RongIMClient.getInstance().getRealTimeLocationParticipants(mConversationType, mTargetId);
		//
		//		if (status != null && status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_INCOMING) {
		//
		//			if (userIds != null && userIds.get(0) != null && userIds.size() == 1) {
		//				locationid = userIds.get(0);
		//				request(GET_USER_INFO);
		//			} else {
		//				if (userIds != null && userIds.size() > 0) {
		//					if (mRealTimeBar != null) {
		//						TextView textView = (TextView) mRealTimeBar.findViewById(android.R.id.text1);
		//						if (userIds.size() <= 1) {
		//							textView.setText(userIds.size() + " " + getApplicationContext().getString(R.string.person_is_sharing_loaction));
		//						} else {
		//							textView.setText(userIds.size() + " " + getApplicationContext().getString(R.string.persons_are_sharing_loaction));
		//						}
		//					}
		//				} else {
		//					if (mRealTimeBar != null && mRealTimeBar.getVisibility() == View.VISIBLE) {
		//						mRealTimeBar.setVisibility(View.GONE);
		//					}
		//				}
		//			}
		//
		//		} else if (status != null && status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_OUTGOING) {
		//			TextView textView = (TextView) mRealTimeBar.findViewById(android.R.id.text1);
		//			textView.setText(getApplicationContext().getString(R.string.you_are_sharing_location));
		//		} else {
		//
		//			if (mRealTimeBar != null && userIds != null) {
		//				TextView textView = (TextView) mRealTimeBar.findViewById(android.R.id.text1);
		//				if (userIds.size() <= 1) {
		//					textView.setText(userIds.size() + " " + getApplicationContext().getString(R.string.person_is_sharing_loaction));
		//				} else {
		//					textView.setText(userIds.size() + " " + getApplicationContext().getString(R.string.persons_are_sharing_loaction));
		//				}
		//			}
		//		}
		//
		//		if (userIds != null && userIds.size() > 0) {
		//
		//			if (mRealTimeBar != null && mRealTimeBar.getVisibility() == View.GONE) {
		//				mRealTimeBar.setVisibility(View.VISIBLE);
		//			}
		//		} else {
		//
		//			if (mRealTimeBar != null && mRealTimeBar.getVisibility() == View.VISIBLE) {
		//				mRealTimeBar.setVisibility(View.GONE);
		//			}
		//		}

	}

	//	public void onEventMainThread(RongEvent.RealTimeLocationMySelfJoinEvent event) {
	//
	//		onParticipantsJoin(RongIM.getInstance().getCurrentUserId());
	//	}

	private void hideRealTimeBar() {
		if (mRealTimeBar != null) {
			mRealTimeBar.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onDestroy() {
		if ("ConversationActivity".equals(this.getClass().getSimpleName()))
			EventBus.getDefault().unregister(this);

		//CallKit start 3
		RongCallKit.setGroupMemberProvider(null);
		//CallKit end 3

		RongIM.getInstance().setGroupMembersProvider(null);
		RongIM.getInstance().setRequestPermissionListener(null);
		RongIMClient.setTypingStatusListener(null);
		super.onDestroy();
	}


	@Override
	public void onStatusChange(final RealTimeLocationConstant.RealTimeLocationStatus status) {
		currentLocationStatus = status;

		EventBus.getDefault().post(status);

		if (status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_IDLE) {
			hideRealTimeBar();

			RealTimeLocationConstant.RealTimeLocationErrorCode errorCode = RongIMClient.getInstance().getRealTimeLocation(mConversationType, mTargetId);

			if (errorCode == RealTimeLocationConstant.RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_SUCCESS) {
				RongIM.getInstance().insertMessage(mConversationType, mTargetId, RongIM.getInstance().getCurrentUserId(),
						InformationNotificationMessage.obtain(getApplicationContext().getString(R.string.location_sharing_ended)));
			}
		} else if (status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_OUTGOING) {//发自定义消息
			showRealTimeLocationBar(status);
		} else if (status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_INCOMING) {
			showRealTimeLocationBar(status);
		} else if (status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_CONNECTED) {
			showRealTimeLocationBar(status);
		}

	}


	@Override
	public void onReceiveLocation(double latitude, double longitude, String userId) {
		Log.e(TAG, "userid = " + userId + ", lat: " + latitude + " long :" + longitude);
		//EventBus.getDefault().post(RongEvent.RealTimeLocationReceiveEvent.obtain(userId, latitude, longitude));
	}

	@Override
	public void onParticipantsJoin(String userId) {
		//EventBus.getDefault().post(RongEvent.RealTimeLocationJoinEvent.obtain(userId));

		if (RongIMClient.getInstance().getCurrentUserId().equals(userId)) {
			showRealTimeLocationBar(null);
		}
	}

	//	@Override
	//	public void onParticipantsQuit(String userId) {
	//		EventBus.getDefault().post(RongEvent.RealTimeLocationQuitEvent.obtain(userId));
	//	}

	@Override
	public void onError(RealTimeLocationConstant.RealTimeLocationErrorCode errorCode) {
		Log.e(TAG, "onError:---" + errorCode);
	}

	/*－－－－－－－－－－－－－地理位置共享 end－－－－－－－－－*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
			if (!closeRealTimeLocation()) {
				if (fragment != null && !fragment.onBackPressed()) {
					if (isFromPush) {
						isFromPush = false;
						//						startActivity(new Intent(this, MainActivity.class));
					}
					finish();
					//				      try {
					//				    	  SealAppContext.getInstance().popAllActivity();
					//					} catch (Exception e) {
					//						// TODO: handle exception
					//						e.printStackTrace();
					//					}

				}
			}
		}
		return false;
	}


	private void hintKbTwo() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive() && getCurrentFocus() != null) {
			if (getCurrentFocus().getWindowToken() != null) {
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}



	@Override
	public void onSuccess(int requestCode, Object result) {
		if (result != null) {
			switch (requestCode) {
			case GET_USER_INFO:
				//				GetUserInfoByIdResponse res = (GetUserInfoByIdResponse) result;
				//				if (res.getCode() == 200) {
				//					TextView textView = (TextView) mRealTimeBar.findViewById(android.R.id.text1);
				//					textView.setText(res.getResult().getNickname() + " 正在共享位置");
				//				}
				break;
			case GET_GROUP_MEMBER:

			}
		}

	}


	@Override
	public void onClick(View v) {
		enterSettingActivity();
	}





	/**
	 * 获取图片
	 * @param url
	 */
	private File getphpto(String url) {
		// TODO Auto-generated method stub
		File file = null;
		byte[] responseBody=getImageFromURL(url);

		//	String str=url.substring(url.lastIndexOf("schoolphoto/"));
		try {
			String path=Environment.getExternalStorageDirectory()+"/aa.png";				
			file = new File(path);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(responseBody);                              
			fos.flush();
			fos.close();

		} catch (Exception e) {

		}
		return file;
	}	
	//	Java代码 
	public static byte[] getImageFromURL(String urlPath) { 
		byte[] data = null; 
		InputStream is = null; 
		HttpURLConnection conn = null; 
		try { 
			URL url = new URL(urlPath); 
			conn = (HttpURLConnection) url.openConnection(); 
			conn.setDoInput(true); 
			// conn.setDoOutput(true); 
			conn.setRequestMethod("GET"); 
			conn.setConnectTimeout(6000); 
			is = conn.getInputStream(); 
			if (conn.getResponseCode() == 200) { 
				data = readInputStream(is); 
			} else{ 
				data=null; 
			} 
		} catch (MalformedURLException e) { 
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} finally { 
			try { 
				if(is != null){ 
					is.close(); 
				}                
			} catch (IOException e) { 
				e.printStackTrace(); 
			} 
			conn.disconnect();           
		} 
		return data; 
	} 

	/**
	 * 读取InputStream数据，转为byte[]数据类型
	 * @param is
	 *            InputStream数据
	 * @return 返回byte[]数据
	 */

	//	Java代码 
	public static byte[] readInputStream(InputStream is) { 
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		byte[] buffer = new byte[1024]; 
		int length = -1; 
		try { 
			while ((length = is.read(buffer)) != -1) { 
				baos.write(buffer, 0, length); 
			} 
			baos.flush(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
		byte[] data = baos.toByteArray(); 
		try { 
			is.close(); 
			baos.close(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
		return data; 
	} 



	private void sendimg(File imageFileThumbs) {
		// TODO Auto-generated method stub
		//发送图片消息
		liwutype=1;
		File imageFileSource = new File(getCacheDir(), "source.png");
		File imageFileThumb = new File(getCacheDir(), "thumb.png");

		//File imageFileSource = 	getphpto("http://192.168.1.127:8080/loveInterest/upload/present/1f43b4c0-e910-4997-9bb2-666909a39501.png");
		try {
			// 读取图片。
			// InputStream is = getAssets().open(imageFileThumbs.getPath());

			// Bitmap bmpSource = BitmapFactory.decodeStream(is);
			Bitmap bmpSource = BitmapFactory.decodeFile(imageFileThumbs.getPath());
			imageFileSource.createNewFile();
			FileOutputStream fosSource = new FileOutputStream(imageFileSource);

			// 保存原图。
			bmpSource.compress(Bitmap.CompressFormat.PNG, 100, fosSource);

			// 创建缩略图变换矩阵。
			Matrix m = new Matrix();
			m.setRectToRect(new RectF(0, 0, bmpSource.getWidth(), bmpSource.getHeight()), new RectF(0, 0, 160, 160), Matrix.ScaleToFit.CENTER);

			// 生成缩略图。
			Bitmap bmpThumb = Bitmap.createBitmap(bmpSource, 0, 0, bmpSource.getWidth(), bmpSource.getHeight(), m, true);

			imageFileThumb.createNewFile();

			FileOutputStream fosThumb = new FileOutputStream(imageFileThumb);

			// 保存缩略图。
			bmpThumb.compress(Bitmap.CompressFormat.PNG, 60, fosThumb);

		} catch (IOException e) {
			e.printStackTrace();
		}

		ImageMessage imgMsg = ImageMessage.obtain(Uri.fromFile(imageFileThumb), Uri.fromFile(imageFileSource));

		/**
		 * 发送图片消息。
		 *       
		 * @param conversationType         会话类型。
		 * @param targetId                 会话目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
		 * @param imgMsg                   消息内容。
		 * @param pushContent              接收方离线时需要显示的push消息内容。
		 * @param pushData                 接收方离线时需要在push消息中携带的非显示内容。
		 * @param SendImageMessageCallback 发送消息的回调。
		 */
		RongIM.getInstance().getRongIMClient().sendImageMessage(ConversationType.PRIVATE, mTargetId, imgMsg, null, null, new RongIMClient.SendImageMessageCallback() {


			@Override
			public void onSuccess(io.rong.imlib.model.Message arg0) {
				// TODO Auto-generated method stub
				arg0.describeContents();

				sendpresent(targetId, mTargetId, cates.getId());

			}

			@Override
			public void onProgress(io.rong.imlib.model.Message arg0, int arg1) {
				// TODO Auto-generated method stub
				arg0.describeContents();
			}

			@Override
			public void onError(io.rong.imlib.model.Message arg0, ErrorCode arg1) {
				// TODO Auto-generated method stub
				arg0.describeContents();
			}

			@Override
			public void onAttached(io.rong.imlib.model.Message arg0) {
				// TODO Auto-generated method stub

			}
		} );

	}

	@Override
	public void onFailure(int requestCode, int state, Object result) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object doInBackground(int requestCode, String parameter) throws HttpException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onParticipantsQuit(String arg0) {
		// TODO Auto-generated method stub

	}




	/**
	 * 
	 *礼物选取界面
	 * 
	 **/
	public class PopupWindows extends PopupWindow {


		public PopupWindows() {
			// TODO Auto-generated method stub

			dismiss();

		}

		public PopupWindows(Context mContext, View parent,int type) {


			if (type==1) {

				View view=null;
				view = View
						.inflate(mContext, R.layout.activity_demo, null);
				view.startAnimation(AnimationUtils.loadAnimation(mContext,
						R.anim.filter));
				setWidth(LayoutParams.FILL_PARENT);
				setHeight(LayoutParams.FILL_PARENT);
				setFocusable(true);
				setBackgroundDrawable(new BitmapDrawable());
				setOutsideTouchable(true);	
				setContentView(view);
				showAtLocation(parent, Gravity.BOTTOM, 0, 0);
				showAsDropDown(parent);  //显示到那个视图之下
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
						WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); 
				ViewPager	mPager = (ViewPager) view.findViewById(R.id.viewpager);
				LinearLayout  mLlDot = (LinearLayout)view. findViewById(R.id.ll_dot);
				RelativeLayout giftout =(RelativeLayout)view. findViewById(R.id.giftout);
				TextView  chunzhi = (TextView)view. findViewById(R.id.chunzhi);
				ImageView  giftguan = (ImageView)view. findViewById(R.id.giftguan);
				initDatas();
				inflater = LayoutInflater.from(ConversationActivity.this);
				//总的页数=总数/每页数量，并取整
				List<Persion>  	users= dbutil.selectData(Persion.class,null);
				if(users !=null)
				{
					if(users.size()>0)
					{
						try {
							persion_main = users.get(0);
							targetId=persion_main.getId().toString();
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
				pageCount = (int) Math.ceil(presents.size() * 1.0 / pageSize);
				mPagerList = new ArrayList<View>();
				for (int i = 0; i < pageCount; i++) {
					// 每个页面都是inflate出一个新实例
					GridView gridView = (GridView) inflater.inflate(R.layout.gridview, mPager, false);
					gridView.setAdapter(new GridViewAdapter(ConversationActivity.this, i, pageSize,presents));
					mPagerList.add(gridView);

					gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							int pos = position + curIndex * pageSize;

							// TODO Auto-generated method stub
							if (checkNetworkState()==false) {

								Toast.makeText(ConversationActivity.this, "当前网络已断开，请网络连接后再送礼物吧。", Toast.LENGTH_SHORT).show();

							}else {

								//							if (psentext.getText().length()==0) {
								//								Toast.makeText(ECChattingActivity.this, "您还没有输入想说的话", Toast.LENGTH_SHORT).show();
								//
								//							}else {
								if (persion.getRongyuntoken()==null) {
									Toast.makeText(ConversationActivity.this, "对方还没有融云账号，不能送礼给她", Toast.LENGTH_SHORT).show();
									//ToastShow("对方还没有融云账号，不能送礼给她");
								}else {
									cates = presents.get(pos);
									catetoken=cates.getPrice();

									token = persion_main.getToken();
									if (token!=0) {
											
										if (token<catetoken) {
											
											
											
											
											
											Toast.makeText(ConversationActivity.this, "您的金币值不够，请充值或者选其他的礼物吧。", Toast.LENGTH_SHORT).show();
											//ToastShow("您的金币值不够，请充值或者选其他的礼物吧。");
											
											
										}else {
											//							if (psentext.getText().length()==0) {
											//								Toast.makeText(ECChattingActivity.this, "您还没有输入想说的话", Toast.LENGTH_SHORT).show();
											//
											//							}else {
											//								if (persion_main.getToken()<catetoken) {
											//
											//									Toast.makeText(UserActivity.this, "您的金币值不够，请充值或者选其他的礼物吧。", Toast.LENGTH_SHORT).show();
											//
											//								}else {


											new Thread(new Runnable() {
												@Override
												public void run() {
													//
													//
													//												if (cates.getResult_img()==null) {
													photofile=	getphpto(cates.getResult_img());
													//												}else {
													//													
													//													photofile=	getphpto(cates.getResult_img());

													if (photofile==null) {


													}else {
														sendimg(photofile);
													}


												}
											}).start();


											//Toast.makeText(ECChattingActivity.this, "选择礼物"+cates.getPresent_name(),Toast.LENGTH_SHORT).show();
										}
									}else {
										if (persion_main.getToken()<catetoken) {
											
											
											//Toast.makeText(ConversationActivity.this, "您的金币值不够，请充值或者选其他的礼物吧。", Toast.LENGTH_SHORT).show();
											//ToastShow("您的金币值不够，请充值或者选其他的礼物吧。");
											Intent in = new Intent(ConversationActivity.this,CloseAccountActivity.class);
											in.putExtra("type", 4);
											startActivity(in);
										}else {


											//							if (psentext.getText().length()==0) {
											//								Toast.makeText(ECChattingActivity.this, "您还没有输入想说的话", Toast.LENGTH_SHORT).show();
											//
											//							}else {
											//								if (persion_main.getToken()<catetoken) {
											//
											//									Toast.makeText(UserActivity.this, "您的金币值不够，请充值或者选其他的礼物吧。", Toast.LENGTH_SHORT).show();
											//
											//								}else {


											new Thread(new Runnable() {
												@Override
												public void run() {
													//
													//
													//												if (cates.getResult_img()==null) {
													photofile=	getphpto(cates.getResult_img());
													//												}else {
													//													
													//													photofile=	getphpto(cates.getResult_img());

													if (photofile==null) {


													}else {
														sendimg(photofile);
													}


												}
											}).start();


											//Toast.makeText(ECChattingActivity.this, "选择礼物"+cates.getPresent_name(),Toast.LENGTH_SHORT).show();
										}
									}
									curIndex=0;
									dismiss();

								}	

							}
							//						}
						//	Toast.makeText(ConversationActivity.this, presents.get(pos).getPresent_name(), Toast.LENGTH_SHORT).show();
							//Toast.makeText(ConversationActivity.this, mDatas.get(pos).getName(), Toast.LENGTH_SHORT).show();
						}
					});
				}
				//设置适配器
				mPager.setAdapter(new ViewPagerAdapter(mPagerList));
				//设置圆点
				setOvalLayout(mLlDot,mPager);
				giftguan.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dismiss();
					}
				});
				giftout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dismiss();
					}
				});
				//充值
				chunzhi.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Intent intent = new Intent(ConversationActivity.this, UserRechargeActivity.class);
						startActivity(intent);
						dismiss();

					}
				});


				//				presents.add(new Present("猪头", 0));
				//				presents.add(new Present("吻", 10));
				//				presents.add(new Present("吻", 10));
				//				presents.add(new Present("跑车", 20));
				//				presents.add(new Present("飞机", 30));
				//				presents.add(new Present("蛋糕", 6));
				//				presents.add(new Present("抱抱", 10));
				//				presents.add(new Present("亲亲", 10));
				//				presents.add(new Present("游艇", 20));

				//				psentext.setVisibility(View.GONE);
				//				psentext.addTextChangedListener( new TextWatcher() {
				//					private CharSequence temp;
				//					private int selectionStart;
				//					private int selectionEnd;
				//					@Override
				//					public void onTextChanged(CharSequence s, int start, int before, int count) {
				//						// TODO Auto-generated method stub
				//
				//					}
				//
				//					@Override
				//					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				//						// TODO Auto-generated method stub
				//						temp = s;
				//					}
				//
				//					@Override
				//					public void afterTextChanged(Editable s) {
				//						// TODO Auto-generated method stub
				//
				//						int number =s.length();
				//						sentnums.setText( number+"");
				//						selectionStart = psentext.getSelectionStart();
				//						selectionEnd = psentext.getSelectionEnd();
				//						if (temp.length() > 20) {
				//							Toast.makeText(ECChattingActivity.this, "您输入的超过字数限制了", Toast.LENGTH_SHORT).show();
				//							s.delete(selectionStart - 1, selectionEnd);
				//							int tempSelection = selectionEnd;
				//							psentext.setSelection(tempSelection);//设置光标在最后
				//						}
				//					}
				//				});


				//			sendpersents.setOnClickListener(new OnClickListener() {
				//
				//					@Override
				//					public void onClick(View v) {
				//						// TODO Auto-generated method stub
				//						if (checkNetworkState()==false) {
				//
				//							Toast.makeText(ConversationActivity.this, "当前网络已断开，请网络连接后再送礼物吧。", Toast.LENGTH_SHORT).show();
				//
				//						}else {
				//							if (cates==null) {
				//								Toast.makeText(ConversationActivity.this, "您还没有选择礼物", Toast.LENGTH_SHORT).show();
				//
				//
				//							}else {
				//								//							if (psentext.getText().length()==0) {
				//								//								Toast.makeText(ECChattingActivity.this, "您还没有输入想说的话", Toast.LENGTH_SHORT).show();
				//								//
				//								//							}else {
				//								if (persion.getRongyuntoken()==null) {
				//									Toast.makeText(ConversationActivity.this, "对方还没有融云账号，不能送礼给她", Toast.LENGTH_SHORT).show();
				//									//ToastShow("对方还没有融云账号，不能送礼给她");
				//								}else {
				//
				//									catetoken=cates.getPrice();
				//
				//									if (persion.getToken()<catetoken) {
				//										Toast.makeText(ConversationActivity.this, "您的金币值不够，请充值或者选其他的礼物吧。", Toast.LENGTH_SHORT).show();
				//										//ToastShow("您的金币值不够，请充值或者选其他的礼物吧。");
				//
				//									}else {
				//
				//
				//										//							if (psentext.getText().length()==0) {
				//										//								Toast.makeText(ECChattingActivity.this, "您还没有输入想说的话", Toast.LENGTH_SHORT).show();
				//										//
				//										//							}else {
				//										//								if (persion_main.getToken()<catetoken) {
				//										//
				//										//									Toast.makeText(UserActivity.this, "您的金币值不够，请充值或者选其他的礼物吧。", Toast.LENGTH_SHORT).show();
				//										//
				//										//								}else {
				//
				//
				//										new Thread(new Runnable() {
				//											@Override
				//											public void run() {
				//												//
				//												//
				//												//												if (cates.getResult_img()==null) {
				//												photofile=	getphpto(cates.getResult_img());
				//												//												}else {
				//												//													
				//												//													photofile=	getphpto(cates.getResult_img());
				//
				//												if (photofile==null) {
				//
				//
				//												}else {
				//													sendimg(photofile);
				//												}
				//
				//
				//											}
				//										}).start();
				//
				//
				//										//Toast.makeText(ECChattingActivity.this, "选择礼物"+cates.getPresent_name(),Toast.LENGTH_SHORT).show();
				//									}
				//									dismiss();
				//
				//								}	
				//							}
				//						}
				//						//						}
				//					}
				//				});
			}

		}

	}


	/** 
	 * 检测网络是否连接 
	 * @return 
	 */  
	private boolean checkNetworkState() {  
		boolean flag = false;  
		//得到网络连接信息  
		ConnectivityManager    manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);  
		//去进行判断网络是否连接  
		if (manager.getActiveNetworkInfo() != null) {  
			flag = manager.getActiveNetworkInfo().isAvailable();  
		}  
		//        if (!flag) {  
		//            setNetwork();  
		//        } else {  
		//            isNetworkAvailable();  
		//        }  

		return flag;  
	}

	/**
	 * 初始化数据源
	 */
	private void initDatas() {
		mDatas = new ArrayList<Model>();
		for (int i = 0; i < titles.length; i++) {
			//动态获取资源ID，第一个参数是资源名，第二个参数是资源类型例如drawable，string等，第三个参数包名
			int imageId = getResources().getIdentifier("ic_category_" + i, "mipmap", getPackageName());
			mDatas.add(new Model(titles[i], imageId));
		}
	}

	/**
	 * 设置圆点
	 */
	public void setOvalLayout(final LinearLayout  mLlDot,ViewPager	mPager) {
		for (int i = 0; i < pageCount; i++) {
			mLlDot.addView(inflater.inflate(R.layout.dot, null));
		}
		// 默认显示第一页
		mLlDot.getChildAt(0).findViewById(R.id.v_dot)
		.setBackgroundResource(R.drawable.dot_selected);
		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			public void onPageSelected(int position) {
				// 取消圆点选中
				mLlDot.getChildAt(curIndex)
				.findViewById(R.id.v_dot)
				.setBackgroundResource(R.drawable.dot_normal);
				// 圆点选中
				mLlDot.getChildAt(position)
				.findViewById(R.id.v_dot)
				.setBackgroundResource(R.drawable.dot_selected);
				curIndex = position;
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void onChanged(ConnectionStatus arg0) {
		// TODO Auto-generated method stub


		// TODO Auto-generated method stub
		switch (arg0){

		case CONNECTED://连接成功。
			Toast.makeText(ConversationActivity.this,"连接成功", Toast.LENGTH_LONG).show();
			break;
		case DISCONNECTED://断开连接。
			//connect(persion_main.getRongyuntoken(), persion_main);
			Toast.makeText(ConversationActivity.this,"连接断开", Toast.LENGTH_LONG).show();
			break;
		case CONNECTING://连接中。
			Toast.makeText(ConversationActivity.this,"连接断开", Toast.LENGTH_LONG).show();
			break;
		case NETWORK_UNAVAILABLE://网络不可用。


			//connect(persion_main.getRongyuntoken(), persion_main);
			Toast.makeText(ConversationActivity.this,"连接断开", Toast.LENGTH_LONG).show();
			break;
		case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线


			//connect(persion_main.getRongyuntoken(), persion_main);
			Toast.makeText(ConversationActivity.this,"连接断开", Toast.LENGTH_LONG).show();
			break;
		}


	}
	public void LoadDataUpdate() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<Persion> list = ManageDataBase.SelectList(dbutil, Persion.class, null);
				if (list.size() > 0) {
					Persion bn = list.get(0);
					String str = Person_Service.loginupdage(bn.getId());
					android.os.Message msg = mUIHandlerres.obtainMessage(1);
					msg.obj = str;
					msg.sendToTarget();
				}
			}
		}).start();
	}

	private Handler mUIHandlerres = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {

			case 1:
				if (msg.obj != null && !msg.obj.equals("")) {
					String str = (String) msg.obj;
					if (!str.equals("") && !str.equals(JsonUtil.ObjToJson(Constant.ERROR))
							&& !str.equals(JsonUtil.ObjToJson("param_error"))) {
						try {
							// new
							// ShowContentView(HomeDetailActivity.this,ll,str,3);
							Persion p1 = JsonUtil.JsonToObj(str, Persion.class);

							if (persion_main.getId().intValue()== p1.getId().intValue()) {

								ManageDataBase.Delete(dbutil, Persion.class, null);
								ManageDataBase.Insert(dbutil, Persion.class, p1);
								persion_main = p1;
							}
						} catch (Exception e) {
							LoadDataUpdate();
							//new ShowContentView(HomeDetailActivity.this, ll, str, 3);
							// TODO: handle exception
							// SharedPreferencesUtil.putInt(HomeDetailActivity.this,
							// "sxdate", 0);
						}

					}
				}
				break;
			default:
				break;
			}
		}
	};

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
					enterActivity();
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
}
