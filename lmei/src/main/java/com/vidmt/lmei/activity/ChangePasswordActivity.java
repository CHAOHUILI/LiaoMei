package com.vidmt.lmei.activity;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.R.menu;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.util.think.JsonUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 修改密码页
 */
public class ChangePasswordActivity extends BaseActivity {
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
	@TAInjectView(id = R.id.repwd)
	private EditText repwd;
	@TAInjectView(id = R.id.send)
	public TextView send;
	@TAInjectView(id = R.id.Ok)
	private TextView Ok;

	private String phone = "";
	private String regpwd = "";
	private String regagainpwd = "";
	private String code = "+86";
	private int seconds = 60; // 定义120秒
	private boolean isClick;
	// codeid
	public static String codeid = "42";
	EventHandler eh;
	String areacode = "86";
	private String code_num = "";
	Timer timer;
	int type = 0;
	private String password;
	private String[] code_error = new String[] { "-1", "-2", "-3", "-4", "-6", "-11", "-14", "-21", "-41", "-42",
			"-51" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		themes();
		InitView();
	}

	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
		headerthemeleft.setVisibility(View.VISIBLE);
		headerright.setVisibility(View.VISIBLE);
		headconrel1.setVisibility(View.GONE);
		headercontentv.setVisibility(View.GONE);
		type = getIntent().getIntExtra("type", 0);
		if (type == 1) {
			headercontent.setText("修改密码");
		} else {
			headercontent.setText("找回密码");
		}
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
					loadingDialog.show();
					LoadData();
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
	public void LoadData() {
		// TODO Auto-generated method stub
		super.LoadData();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = mUHandler.obtainMessage(1);
				msg.obj = Person_Service.updatePwd(phone, regagainpwd);
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
					String str = (String) msg.obj;
					if (JsonUtil.JsonToObj(str, String.class) == null) {
						ToastShow(Constant.ERROR_MES);
					}
					if (JsonUtil.JsonToObj(str, String.class).equals(Constant.SUCCESS)) {
						finish();
						ToastShow("修改成功");
					} else if (JsonUtil.JsonToObj(str, String.class).equals(Constant.ERROR)) {
						ToastShow("修改失败");
					}
				}
				loadingDialog.dismiss();
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
			public static final int MIN_CLICK_DELAY_TIME = 1000;
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
								GetCode();
							} else {
								ToastShow("手机号格式不正确");
							}
						} else {
							ToastShow("手机号不允许空");
						}
					}
					break;
				case R.id.Ok:
					Validation();
					break;
				case R.id.headerthemeleft:
					finish();
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
		regagainpwd = repwd.getText().toString().trim();
		code_num = yzm.getText().toString().trim();
		if (tel.equals("")) // ||tel.equals("请输入您的手机号")
		{
			ToastShow("请输入您的手机号");
		} else if (pwd.equals("")) // ||pwd.equals("请输入密码")
		{
			ToastShow("新密码不能为空");
		} else if (regagainpwd.equals("")) {
			ToastShow("确认密码不能为空");
		} else if (regagainpwd.equals(pwd)) {
			ToastShow("密码不一致");
		} else if (code_num.equals("")) {
			ToastShow("输入验证码");
		} else {
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
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (msg.obj != null) {
					String str = (String) msg.obj;
					if (JsonUtil.JsonToObj(str, String.class) == null) {
						ToastShow(Constant.ERROR_MES);
						isClick = false;
					} else if (JsonUtil.JsonToObj(str, String.class).equals(Constant.SUCCESS)) {
						ToastShow("手机号已注册");
						isClick = false;
					} else if (JsonUtil.JsonToObj(str, String.class).equals(Constant.ERROR)) {
						GetCode();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change_password, menu);
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
}
