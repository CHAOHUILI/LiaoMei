package com.vidmt.lmei.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.drawable;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.R.string;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.util.think.JsonUtil;
import com.vidmt.lmei.util.think.SmsUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 注册页 * @author Ws
 *
 */
public class RegisterActivity extends BaseActivity  {

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

	@TAInjectView(id = R.id.tel)
	private EditText tel;
	@TAInjectView(id = R.id.pwd)
	private EditText pwd;
	@TAInjectView(id = R.id.yzm)
	private EditText yzm;
	@TAInjectView(id = R.id.send)
	public TextView send;
	@TAInjectView(id = R.id.Ok)
	private TextView Ok;

	private String phone = "";
	private String regpwd = "";
	private String code = "+86";
	private int seconds = 60; // 定义120秒
	private boolean isClick;
	// codeid
	public static String codeid = "42";
	EventHandler eh;
	String areacode = "86";
	Timer timer;
	private String code_num = "";
	public static RegisterActivity RegisterActivity;
	private String password;
	private String[] code_error = new String[] { "-1", "-2", "-3", "-4", "-6", "-11", "-14", "-21", "-41", "-42",
			"-51" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		themes();
		InitView();
		RegisterActivity = this;
	
	}

	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
		headerthemeleft.setVisibility(View.VISIBLE);
		headerright.setVisibility(View.VISIBLE);
		headconrel1.setVisibility(View.GONE);
		headercontentv.setVisibility(View.GONE);
		headercontent.setText("注册");
		Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
		user.setBackgroundDrawable(drawable);
		try {
			SMSSDK.initSDK(this, "16fac39d85416", "4fe253382095429f701c979201d84f2a", false);
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println(e);
		}
		eh = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		SMSSDK.registerEventHandler(eh);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			Log.e("event", "event=" + event);
			if (result == SMSSDK.RESULT_COMPLETE) {

				// 短信注册成功
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
					// loadingDialog.show();
					// LoadData();
					finish();
					Intent intent = new Intent(RegisterActivity.this, RegisterImproveActivity.class);
					intent.putExtra("phone", phone);
					intent.putExtra("regpwd", regpwd);
					startActivity(intent);
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					ToastShow("请求已发送,请注意查收");
					// SMSSDK.unregisterEventHandler(eh);
				} else {
					ToastShow("验证码不正确");
				}
			} else {
				ToastShow("已超出可发送条数");
			}

		}
	};


	@Override
	protected void onAfterSetContentView() {
		// TODO Auto-generated method stub
		super.onAfterSetContentView();
		OnClickListener onClickListener = new OnClickListener() {
			public static final int MIN_CLICK_DELAY_TIME = 5000;
			private long lastClickTime = 0;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				switch (v.getId()) {
				case R.id.send:
					long currentTime = Calendar.getInstance().getTimeInMillis();
					if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
						lastClickTime = currentTime;
						phone = tel.getText().toString().trim();
						if (!phone.equals("")) {
							boolean is_tel = ValidationPhone(phone);
							if (is_tel == true) {
								// loadingDialog.show();
								GetUtel(phone);
							} else {
								ToastShow("手机号格式不正确");
							}
						} else {
							ToastShow("手机号不允许空");
						}
					}else {
						Log.i("dianji","keyi");
						break;
					}
					break;
				case R.id.Ok:
					Validation();
					break;
				case R.id.headerthemeleft:
					finish();
					StartActivity(LoginActivity.class);

					break;
				default:
					break;
				}
			}
		};
		Ok.setOnClickListener(onClickListener);
		send.setOnClickListener(onClickListener);
		headerthemeleft.setOnClickListener(onClickListener);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			StartActivity(LoginActivity.class);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}

	/**
	 * 手机号 验证
	 * 
	 * @param phone
	 * @return
	 */
	public boolean ValidationPhone(String phone) {
		String ph = "1[3,4,5,7,8][0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|18[0-9]{9}$";
		if (phone.matches(ph)) {
			return true;
		}
		return false;
	}

	public void Validation() {

		phone = tel.getText().toString().trim();
		regpwd = pwd.getText().toString().trim();
		code_num = yzm.getText().toString().trim();
		if (phone.equals("")) // ||tel.equals("请输入您的手机号")
		{
			ToastShow("请输入您的手机号");
		} else if (regpwd.equals("")) // ||pwd.equals("请输入密码")
		{
			ToastShow("密码不能为空");
		} else if (yzm.getText().toString().trim().equals("")) {
			ToastShow("输入验证码");
		} else {
			/*
			 * finish(); Intent intent=new Intent(RegisterActivity.this,
			 * RegisterImproveActivity.class); intent.putExtra("phone", phone);
			 * intent.putExtra("regpwd", regpwd); startActivity(intent);
			 */
			SMSSDK.submitVerificationCode(areacode, phone, code_num);
		}
	}

	public void GetUtel(final String tel) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = mUIHandler.obtainMessage(1);
				msg.obj = Person_Service.isPhone(tel);
				msg.sendToTarget();
			}
		}).start();
	}

	private Handler mUIHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (msg.obj != null) {
					String str = (String) msg.obj;

					if (JsonUtil.JsonToObj(str, String.class) == null
							|| JsonUtil.JsonToObj(str, String.class).equals("")) {
						ToastShow(Constant.ERROR_MES);
						isClick = false;
					} else if (JsonUtil.JsonToObj(str, String.class).equals(Constant.SUCCESS)) {
						GetCode();//获取验证码

					} else if (JsonUtil.JsonToObj(str, String.class).equals("tel_yet")) {
						ToastShow("手机号已注册");
						isClick = false;
					}

				}
				loadingDialog.dismiss();
				break;
			case 2:
				if (msg.obj != null) // 定时器关闭
				{
					timer.cancel();
					send.setText("点击获取");

				}
				break;
			case 3:
				if (msg.obj != null) // 启动定时器
				{
					String mes = (String) msg.obj;
					send.setText(mes);
				}
				break;
			case 4:
				if (msg.obj != null) {
					String mes = (String) msg.obj;
					if (mes != null) {
						String sms = mes;
						for (int i = 0; i < code_error.length; i++) {
							if (sms.equals(code_error[i])) {
								ToastShow("网路不给力");
							}
						}
					}
				}
				break;
			default:
				break;
			}
		}

	};

	public void GetCode() {
		if (isClick == false) {
			timer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					seconds--;
					try {
						if (seconds == 0) {
							Message msg = mUIHandler.obtainMessage(2);
							msg.obj = "获取验证码";
							msg.sendToTarget();
							isClick = false;
							seconds = 60;
						} else {
							Message msg = mUIHandler.obtainMessage(3);
							msg.obj = seconds + "秒重新获取";
							msg.sendToTarget();
							isClick = true;
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
			};
			timer.schedule(task, 1000, 1000);

			GetSms();
		}
	}

	public void GetSms() {
		SMSSDK.getVerificationCode(areacode, phone);
	}

	
}
