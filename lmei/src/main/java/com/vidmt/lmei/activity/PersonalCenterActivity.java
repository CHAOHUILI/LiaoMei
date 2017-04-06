package com.vidmt.lmei.activity;

import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.ConversationListActivity;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.R.menu;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.dialog.ConnectionUtil;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.rule.ManageDataBase;
import com.vidmt.lmei.util.think.JsonUtil;
import com.vidmt.lmei.widget.RoundImageView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalCenterActivity extends BaseActivity {
	private long exitTime = 0;
	@TAInjectView(id = R.id.headerthemeleft)
	RelativeLayout headerthemeleft;
	@TAInjectView(id = R.id.user)
	ImageView user;
	@TAInjectView(id = R.id.headerright)
	LinearLayout headerright;
	@TAInjectView(id = R.id.typelog)
	ImageView typelog;
	@TAInjectView(id = R.id.headercontent)
	TextView headercontent;
	@TAInjectView(id = R.id.headercontentv)
	View headercontentv;
	@TAInjectView(id = R.id.headercontent1)
	TextView headercontent1;
	@TAInjectView(id = R.id.headercontentv1)
	View headercontentv1;
	@TAInjectView(id = R.id.headconrel1)
	RelativeLayout headconrel1;
	@TAInjectView(id = R.id.user_portrait)
	RoundImageView user_portrait;
	@TAInjectView(id = R.id.user_name)
	TextView user_name;
	@TAInjectView(id = R.id.user_num)
	TextView user_num;
	@TAInjectView(id = R.id.authenticationmoney)
	TextView authenticationmoney;
	@TAInjectView(id = R.id.updateuserlin)
	LinearLayout updateuserlin;
	@TAInjectView(id = R.id.authenticationlin)
	LinearLayout authenticationlin;
	@TAInjectView(id = R.id.walletlin)
	LinearLayout walletlin;
	@TAInjectView(id = R.id.blacklistlin)
	LinearLayout blacklistlin;
	@TAInjectView(id = R.id.allowvoicelin)
	LinearLayout allowvoicelin;
	@TAInjectView(id = R.id.allowvoice)
	ImageView allowvoice;
	@TAInjectView(id = R.id.allowvideolin)
	LinearLayout allowvideolin;
	@TAInjectView(id = R.id.allowvideo)
	ImageView allowvideo;
	@TAInjectView(id = R.id.tolllin)
	LinearLayout tolllin;
	@TAInjectView(id = R.id.rechargelin)
	LinearLayout rechargelin;
	@TAInjectView(id = R.id.message)
	LinearLayout message;
	@TAInjectView(id = R.id.helpcenterlin)
	LinearLayout helpcenterlin;
	int voice_state = 0;
	int video_state = 0;
	@TAInjectView(id = R.id.lookme)
	ImageView lookme;
	public static boolean isshowHd;// true 显示

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_center);
		InitView();
		themes();
		registerBoradcastReceiver();
		registerBoradcastReceiver2();
		LoadDataUpdate();
		LoadAuthenticationData();
		
	}

	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
		headerthemeleft.setVisibility(View.GONE);
		headerright.setVisibility(View.VISIBLE);
		headconrel1.setVisibility(View.GONE);
		headercontentv.setVisibility(View.GONE);
		headercontent.setText("我");
		Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
		user.setBackgroundDrawable(drawable);
		Drawable drawablemore = context.getResources().getDrawable(R.drawable.settings);
		typelog.setBackgroundDrawable(drawablemore);
		imageLoader.displayImage(b_person.getPhoto(), user_portrait, options);
		user_name.setText(b_person.getNick_name());
		user_num.setText("ID:" + b_person.getOtherkey());
		if (b_person.getVoice_state() == 1) {
			Drawable drawableVoice = context.getResources().getDrawable(R.drawable.switchon);
			allowvoice.setBackgroundDrawable(drawableVoice);
		} else {

			Drawable drawableVoice1 = context.getResources().getDrawable(R.drawable.switchoff);
			allowvoice.setBackgroundDrawable(drawableVoice1);
		}
		if (b_person.getVideo_state() == 1) {
			Drawable drawableVideo = context.getResources().getDrawable(R.drawable.switchon);
			allowvideo.setBackgroundDrawable(drawableVideo);
		} else {
			Drawable drawableVideo1 = context.getResources().getDrawable(R.drawable.switchoff);
			allowvideo.setBackgroundDrawable(drawableVideo1);
		}
		if (b_person.getYet_photo() == 0 || b_person.getYet_video() == 0) {
			authenticationmoney.setVisibility(View.VISIBLE);
		}else{
			authenticationmoney.setVisibility(View.GONE);
		}
	}

	private void LoadAuthenticationData() {
		BroadcastReceiver broadcastReceiver = new BroadcastReceiver()

		{
			@Override
			public void onReceive(Context context, Intent intent) {
				List<Persion> list = ManageDataBase.SelectList(dbutil, Persion.class, null);
				if (list.size() > 0) {
					b_person = list.get(0);
					LoadDataUpdate();
				}
				
			}
		};
		IntentFilter intentToReceiveFilter = new IntentFilter();
		intentToReceiveFilter.addAction("userupdatezy");
		registerReceiver(broadcastReceiver, intentToReceiveFilter);
	}

	public void LoadDataUpdate() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String str = Person_Service.loginupdage(b_person.getId());
				Message msg = mUIHandler.obtainMessage(4);
				msg.obj = str;
				msg.sendToTarget();
			}
		}).start();
	}

	public void registerBoradcastReceiver() {
		// 有人看过我
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("发送广播");
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);

	}

	public void registerBoradcastReceiver2() {
		// 查看谁看过我
		IntentFilter myIntent = new IntentFilter();
		myIntent.addAction("vanish");
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntent);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("发送广播")) {
				lookme.setVisibility(View.VISIBLE);
				isshowHd = true;
			} else if (action.equals("vanish")) {
				lookme.setVisibility(View.GONE);
				isshowHd = false;
			}

		}

	};
	private Handler mUIHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {

			case 4:
				if (msg.obj != null && !msg.obj.equals("")) {
					String str = (String) msg.obj;
					if (!str.equals("") && !str.equals(JsonUtil.ObjToJson(Constant.ERROR))) {
						Persion p = JsonUtil.JsonToObj(str, Persion.class);
						ManageDataBase.Delete(dbutil, Persion.class,null);
						ManageDataBase.Insert(dbutil, Persion.class, p);
						b_person = p;
						if (b_person.getYet_photo() == 0 || b_person.getYet_video() == 0) {
							authenticationmoney.setVisibility(View.VISIBLE);
						}else{
							authenticationmoney.setVisibility(View.GONE);
						}
						imageLoader.displayImage(b_person.getPhoto(), user_portrait, options);
						user_name.setText(b_person.getNick_name());
						user_num.setText("ID:" + b_person.getOtherkey());
						if (b_person.getVoice_state() == 1) {
							Drawable drawableVoice = context.getResources().getDrawable(R.drawable.switchon);
							allowvoice.setBackgroundDrawable(drawableVoice);
						} else {

							Drawable drawableVoice1 = context.getResources().getDrawable(R.drawable.switchoff);
							allowvoice.setBackgroundDrawable(drawableVoice1);
						}
						if (b_person.getVideo_state() == 1) {
							Drawable drawableVideo = context.getResources().getDrawable(R.drawable.switchon);
							allowvideo.setBackgroundDrawable(drawableVideo);
						} else {
							Drawable drawableVideo1 = context.getResources().getDrawable(R.drawable.switchoff);
							allowvideo.setBackgroundDrawable(drawableVideo1);
						}

					}
				}
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onAfterSetContentView() {
		// TODO Auto-generated method stub
		super.onAfterSetContentView();
		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.headerthemeleft:
					finish();
					break;
				case R.id.allowvoicelin:
					if (b_person.getVoice_state() == 1) {
						voice_state = 2;
						video_state = 0;
					} else {
						voice_state = 1;
						video_state = 0;
					}
					LoadAlowedAnswerVoiceData();
					break;
				case R.id.allowvideolin:
					if (b_person.getVideo_state() == 1) {
						voice_state = 0;
						video_state = 2;
					} else {
						voice_state = 0;
						video_state = 1;
					}
					LoadAlowedAnswerVideoData();
					break;
				case R.id.updateuserlin:
					StartActivity(PersonUpdateActivity.class);
					break;
				case R.id.authenticationlin:
					StartActivity(UserAuthenticationActivity.class);
					break;
				case R.id.walletlin:
					StartActivity(UserWalletActivity.class);
					break;
				case R.id.blacklistlin:
					StartActivity(BlacklistActivity.class);
					break;
				case R.id.headerright:

					StartActivity(UserSettingsActivity.class);
					break;
				case R.id.tolllin:
					StartActivity(UserChargeActivity.class);
					break;
				case R.id.rechargelin:
					StartActivity(UserRechargeActivity.class);
					break;
				case R.id.message:
					StartActivity(MessagesActivity.class);
					break;
				case R.id.helpcenterlin:
					StartActivity(UserHelpCenterActivity.class);
					break;
				default:

					break;
				}
			}
		};
		headerthemeleft.setOnClickListener(onClickListener);
		updateuserlin.setOnClickListener(onClickListener);
		authenticationlin.setOnClickListener(onClickListener);
		walletlin.setOnClickListener(onClickListener);
		blacklistlin.setOnClickListener(onClickListener);
		allowvoicelin.setOnClickListener(onClickListener);
		allowvideolin.setOnClickListener(onClickListener);
		headerright.setOnClickListener(onClickListener);
		tolllin.setOnClickListener(onClickListener);
		rechargelin.setOnClickListener(onClickListener);
		message.setOnClickListener(onClickListener);
		helpcenterlin.setOnClickListener(onClickListener);
	}

	public void LoadAlowedAnswerVoiceData() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// dialog.show();
				Message msg = mUHandler.obtainMessage(1);
				try {
					msg.obj = Person_Service.alowed_to_answer(b_person.getId(), voice_state, video_state);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					msg.obj = null;
				}
				msg.sendToTarget();
			}
		}).start();
	}

	public void LoadAlowedAnswerVideoData() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// dialog.show();
				Message msg = mUHandler.obtainMessage(2);
				try {
					msg.obj = Person_Service.alowed_to_answer(b_person.getId(), voice_state, video_state);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					msg.obj = null;
				}
				msg.sendToTarget();
			}
		}).start();
	}

	private Handler mUHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (msg.obj != null) {
					String json = (String) msg.obj;
					if (json.equals(JsonUtil.ObjToJson(Constant.SUCCESS))) {
						if (b_person.getVoice_state() == 2) {
							Persion p = b_person;
							p.setVoice_state(1);
							ManageDataBase.Delete(dbutil, Persion.class, null);
							ManageDataBase.Insert(dbutil, Persion.class, p);
							Drawable drawable = context.getResources().getDrawable(R.drawable.switchon);
							allowvoice.setBackgroundDrawable(drawable);
							ToastShow("已启动语音");
						} else {
							Persion p = b_person;
							p.setVoice_state(2);
							ManageDataBase.Delete(dbutil, Persion.class, null);
							ManageDataBase.Insert(dbutil, Persion.class, p);
							Drawable drawable = context.getResources().getDrawable(R.drawable.switchoff);
							allowvoice.setBackgroundDrawable(drawable);
							ToastShow("已关闭语音");
						}

					} else {
						ToastShow("语音启动失败");
					}
				} else {
					ToastShow("网络异常");
				}

				break;
			case 2:
				if (msg.obj != null) {
					String json = (String) msg.obj;
					if (json.equals(JsonUtil.ObjToJson(Constant.SUCCESS))) {
						if (b_person.getVideo_state() == 2) {
							Persion p = b_person;
							p.setVideo_state(1);
							ManageDataBase.Delete(dbutil, Persion.class, null);
							ManageDataBase.Insert(dbutil, Persion.class, p);
							Drawable drawable = context.getResources().getDrawable(R.drawable.switchon);
							allowvideo.setBackgroundDrawable(drawable);
							ToastShow("已启动视频");
						} else {
							Persion p = b_person;
							p.setVideo_state(2);
							ManageDataBase.Delete(dbutil, Persion.class, null);
							ManageDataBase.Insert(dbutil, Persion.class, p);
							Drawable drawable = context.getResources().getDrawable(R.drawable.switchoff);
							allowvideo.setBackgroundDrawable(drawable);
							ToastShow("已关闭视频");
						}

					} else {
						ToastShow("视频启动失败");
					}
				} else {
					ToastShow("网络异常");
				}

				break;
			default:
				break;
			}
		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.personal_center, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.exit), Toast.LENGTH_SHORT)
					.show();
			exitTime = System.currentTimeMillis();
		} else {
			exitApp();
		}
	}
}
