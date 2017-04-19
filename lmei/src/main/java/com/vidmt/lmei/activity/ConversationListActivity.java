package com.vidmt.lmei.activity;



import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.ta.TAApplication;
import com.vidmt.lmei.Application;
import com.vidmt.lmei.R;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.dialog.LoadingDialog;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.rule.ScreenUtils;
import com.vidmt.lmei.util.think.DbUtil;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.ConversationListBehaviorListener;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectionStatusListener;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import io.rong.imlib.model.Conversation.ConversationType;

/**
 * 消息activity
 */
public class ConversationListActivity  extends FragmentActivity implements ConnectionStatusListener{
	/**
	 * 目标 Id
	 */
	private String mTargetId;

	/**
	 * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
	 */
	private String mTargetIds;

	/**
	 * 会话类型
	 */
	private Conversation.ConversationType mConversationType;
	private LinearLayout head_line;
	private ImageView head_left_img;
	private TextView head_right_txt;
	private TextView head_txt;
	protected DbUtil dbutil;
	private String phone;
	private List<Conversation> cList;
	public static  ConversationListActivity conversationListActivity;
	int type=0;
	private long exitTime = 0;
	protected  Persion persion_main; 
	private UIConversation uiConversation;
	private String TargetId;
	private LoadingDialog mDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversation_list);
		//启动会话列表界面

		conversationListActivity=this;
		Intent intent = getIntent();


		dbutil = new DbUtil((TAApplication) this.getApplication());
		List<Persion>  	users= dbutil.selectData(Persion.class,null);
		if(users !=null)
		{
			if(users.size()>0)
			{
				try {
					persion_main = users.get(0);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}



		if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
			RongIM.init(this);
			//Toast.makeText(ConversationListActivity.this, "rong", Toast.LENGTH_SHORT).show();
			if (mDialog != null && !mDialog.isShowing()) {
				mDialog.show();
				
			}
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {

					enterActivity();//连接融云
				}
			}, 300);
		} else {
			if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {

				enterActivity();
			}else {

			}



			try {
				//		ConversationListFragment fragment = (ConversationListFragment) getSupportFragmentManager().findFragmentById(R.id.conversationlist);
				ConversationListFragment fragment = new ConversationListFragment();
				Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
						.appendPath("conversationlist")
						.appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
						.appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
						.build();
				fragment.setUri(uri); 
				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
				//rong_content 为你要加载的 id
				transaction.add(R.id.conversationlist, fragment);
				transaction.commitAllowingStateLoss();                                                                                                                       
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();

			}

		}
		//		if (RongIM.getInstance() != null)  {
		//			Log.e("rongyun", "------------------------------------------------");
		//			try {
		//				//		ConversationListFragment fragment = (ConversationListFragment) getSupportFragmentManager().findFragmentById(R.id.conversationlist);
		//				ConversationListFragment fragment = new ConversationListFragment();
		//				Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
		//						.appendPath("conversationlist")
		//						.appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
		//						.appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
		//						.build();
		//				fragment.setUri(uri); 
		//				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		//				//rong_content 为你要加载的 id
		//				transaction.add(R.id.conversationlist, fragment);
		//				transaction.commitAllowingStateLoss();                                                                                                                       
		//			} catch (Exception e) {
		//				// TODO: handle exception
		//				e.printStackTrace();
		//				Log.e("rongyun", e.getMessage()+"------------------------------------------------");
		//			}
		//		}else {
		//
		//		}





		head_txt =(TextView) findViewById(R.id.headercontent);
		head_txt.setText("消息");
		themes();
	}

	private void initview() {
		// TODO Auto-generated method stub


		ConversationListFragment fragment = (ConversationListFragment) getSupportFragmentManager().findFragmentById(R.id.conversationlist);

		Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
				.appendPath("conversationlist")
				.appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
				.appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
				.build();
		fragment.setUri(uri); 
		RongIM.setConversationListBehaviorListener(new ConversationListBehaviorListener() {

			@Override
			public boolean onConversationPortraitLongClick(Context arg0, ConversationType arg1, String arg2) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onConversationPortraitClick(Context arg0, ConversationType arg1, String arg2) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onConversationLongClick(Context arg0, View arg1, UIConversation arg2) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onConversationClick(Context arg0, View arg1, UIConversation arg2) {
				// TODO Auto-generated method stub
				return false;
			}
		});

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
				linheader.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height + bits));//代碼里重新設置佈局參數
			}
		}
	}
	public void selectisblack(final UIConversation uiConversation) {
		this.uiConversation= uiConversation ;
		new Thread(new Runnable() {
			@Override
			public void run() {

				// TODO Auto-generated method stub
				String jhuser = Person_Service.selectisblack(uiConversation.getConversationSenderId(), uiConversation.getConversationTargetId());
				Message msg = mUIHandler.obtainMessage(1);
				msg.obj = jhuser;
				msg.sendToTarget();
			}
		}).start();
	}
	public void selectisblack(final String TargetId) {
		this.TargetId= 	TargetId;
		new Thread(new Runnable() {
			@Override
			public void run() {

				// TODO Auto-generated method stub
				String jhuser = Person_Service.selectisblack(persion_main.getId()+"", TargetId);
				Message msg = mUIHandler.obtainMessage(1);
				msg.obj = jhuser;
				msg.sendToTarget();
			}
		}).start();
	}
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {

			case 1:

				if (msg.obj != null) {
					String mes = (String) msg.obj;
					if ("".equals(mes)) {
						// ToastShow("网络异常，请检查网络连接");
					} else {

						JSONObject jsStr;
						String province =null;
						int carrier = 0;

						try {
							jsStr = new  JSONObject(mes);

							int myisblack= jsStr.getInt("myisblack");

							if (myisblack==1) {
								Toast.makeText(ConversationListActivity.this, "您被对方拉黑，不能进行此操作", Toast.LENGTH_SHORT).show();
							}else {


								if (uiConversation==null) {

									UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(TargetId);
									if (userInfo==null) {
										RongIM.getInstance().startPrivateChat(ConversationListActivity.this,TargetId,
												TargetId + "//" + persion_main.getId()+"");
									}else {
										RongIM.getInstance().startPrivateChat(ConversationListActivity.this,TargetId,
												userInfo.getName() + "//" + persion_main.getId()+"");
									}

								}else {
									RongIM.getInstance().startPrivateChat(ConversationListActivity.this, uiConversation.getConversationTargetId(),
											uiConversation.getUIConversationTitle() + "//" + uiConversation.getConversationSenderId());
								}

							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}}
				}
				break;
			default:
				break;
			}
		}
	};
	@Override
	public void onChanged(ConnectionStatus connectionStatus) {
		// TODO Auto-generated method stub
		switch (connectionStatus){

		case CONNECTED://连接成功。
			Toast.makeText(ConversationListActivity.this,"连接成功", Toast.LENGTH_LONG).show();
			break;
		case DISCONNECTED://断开连接。
			connect(persion_main.getRongyuntoken(), persion_main);
			Toast.makeText(ConversationListActivity.this,"连接断开", Toast.LENGTH_LONG).show();
			break;
		case CONNECTING://连接中。
			Toast.makeText(ConversationListActivity.this,"连接断开", Toast.LENGTH_LONG).show();
			break;
		case NETWORK_UNAVAILABLE://网络不可用。


			connect(persion_main.getRongyuntoken(), persion_main);
			Toast.makeText(ConversationListActivity.this,"连接断开", Toast.LENGTH_LONG).show();
			break;
		case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线


			connect(persion_main.getRongyuntoken(), persion_main);
			Toast.makeText(ConversationListActivity.this,"连接断开", Toast.LENGTH_LONG).show();
			break;
		}

	}

	@Override
	protected void onResumeFragments() {
		// TODO Auto-generated method stub
		super.onResumeFragments();
	}


	public void initviews() {
		// TODO Auto-generated method stub
		if (RongIM.getInstance() != null)  {


			try {
				//		ConversationListFragment fragment = (ConversationListFragment) getSupportFragmentManager().findFragmentById(R.id.conversationlist);
				ConversationListFragment fragment = new ConversationListFragment();
				Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
						.appendPath("conversationlist")
						.appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
						.appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
						.build();
				fragment.setUri(uri); 
				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
				//rong_content 为你要加载的 id
				transaction.add(R.id.conversationlist, fragment);
				transaction.commitAllowingStateLoss();                                                                                                                       
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Log.e("rongyun", e.getMessage()+"------------------------------------------------");
			}
		}else {

			Toast.makeText(ConversationListActivity.this,"jsdkjs", Toast.LENGTH_LONG).show();
		}

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
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

	public void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(),
					this.getResources().getString(R.string.exit),
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			((TAApplication) getApplication()).exitApp(false);
		}
	}
	public void sss(final UIConversation uiConversation,int type) {
		// TODO Auto-generated method stub
		//		String[] items = new String[2];
		//
		//		if (uiConversation.isTop()) {
		//			items[0]="取消置顶";
		//		}else {
		//			items[0]="置顶该会话";
		//		}
		//		items[0]="从会话列表中移除";

		switch (type) {
		case 1:
			RongIM.getInstance().getRongIMClient().setConversationToTop(uiConversation.getConversationType(), uiConversation.getConversationTargetId(),
					!uiConversation.isTop());

			break;
		case 2:
			RongIM.getInstance().getRongIMClient().removeConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
			break;
		default:
			break;
		}

	}


	/**
	 * 收到 push 消息后，选择进入哪个 Activity
	 * 如果程序缓存未被清理，进入 SplashActivity
	 * 程序缓存被清理，进入 LoginActivity，重新获取token
	 * <p/>
	 * 作用：由于在 manifest 中 intent-filter 是配置在 ConversationActivity 下面，所以收到消息后点击notifacition 会跳转到 DemoActivity。
	 * 以跳到 SplashActivity 为例：
	 * 在 ConversationActivity 收到消息后，选择进入 SplashActivity，这样就把 SplashActivity 激活了，当你读完收到的消息点击 返回键 时，程序会退到
	 * SplashActivity 页面，而不是直接退回到 桌面。
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
			//			startActivity(new Intent(ConversationActivity.this, LoginActivity.class));
			//			finish();
			Toast.makeText(ConversationListActivity.this, "加载", Toast.LENGTH_SHORT).show();
			reconnect(tokens);
		} else {
			reconnect(tokens);
		}
	}

	private void reconnect(String token) {

		RongIM.connect(token, new RongIMClient.ConnectCallback() {
			@Override
			public void onTokenIncorrect() {


			}

			@Override
			public void onSuccess(String s) {


				if (mDialog != null)
					mDialog.dismiss();           

				//
				//                Intent intent = new Intent();
				//                intent.setClass(ConversationActivity.this, SplashActivity.class);
				//                intent.putExtra("PUSH_CONVERSATIONTYPE", mConversationType.toString());
				//                intent.putExtra("PUSH_TARGETID", mTargetId);
				//                startActivity(intent);
				//                finish();




				try {
					//		ConversationListFragment fragment = (ConversationListFragment) getSupportFragmentManager().findFragmentById(R.id.conversationlist);
					ConversationListFragment fragment = new ConversationListFragment();
					Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
							.appendPath("conversationlist")
							.appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
							.appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
							.build();
					fragment.setUri(uri); 
					FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
					//rong_content 为你要加载的 id
					transaction.add(R.id.conversationlist, fragment);
					transaction.commitAllowingStateLoss();                                                                                                                       
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();

				}



			}

			@Override
			public void onError(RongIMClient.ErrorCode e) {

				if (mDialog != null)
					mDialog.dismiss();
				try {
					//		ConversationListFragment fragment = (ConversationListFragment) getSupportFragmentManager().findFragmentById(R.id.conversationlist);
					ConversationListFragment fragment = new ConversationListFragment();
					Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
							.appendPath("conversationlist")
							.appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
							.appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
							.build();
					fragment.setUri(uri); 
					FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
					//rong_content 为你要加载的 id
					transaction.add(R.id.conversationlist, fragment);
					transaction.commitAllowingStateLoss();                                                                                                                       
				} catch (Exception e1) {
					// TODO: handle exception
					e1.printStackTrace();

				}


			}
		});

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

					initviews();
				}

				/**
				 * 连接融云失败
				 * @param errorCode 错误码，可到官网 查看错误码对应的注释
				 */
				@Override
				public void onError(RongIMClient.ErrorCode errorCode) {

				}
			});
		}else {


		}
	}	
}
