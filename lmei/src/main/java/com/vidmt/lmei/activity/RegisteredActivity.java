package com.vidmt.lmei.activity;


import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.util.think.JsonUtil;
import com.vidmt.lmei.util.think.SmsUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 暂无
 */
public class RegisteredActivity extends BaseActivity {
	
	@TAInjectView(id = R.id.rs_register)
	Button rs_register; 
	private String p_pwd;
	private boolean isClick = false;
	@TAInjectView(id = R.id.user_tel)
	EditText user_tel;
	@TAInjectView(id = R.id.user_code)
	EditText user_code;
	@TAInjectView(id = R.id.user_pwd)
	EditText user_pwd;
	@TAInjectView(id = R.id.user_pwd1)
	EditText user_pwd1;
	@TAInjectView(id = R.id.user_code_txt)
	TextView user_code_txt;
	private int code_num;
	private String phone;
	private int seconds = 60; //定义120秒
	Timer timer;
	private String[] code_error = new String[]{"-1","-2","-3","-4","-6","-11","-14","-21","-41","-42","-51"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registered);
		InitView();
	}
	
	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
	
	}
	public void Jude()
	{
		phone = user_tel.getText().toString().trim();
		p_pwd = user_pwd.getText().toString().trim();
		String code = user_code.getText().toString().trim();
		if(phone.equals(""))
		{
			ToastShow("手机号不允许为空");
		}
		else if(code.equals(""))
		{
			ToastShow("验证码不允许为空");
		}
		else if(p_pwd.equals(""))
		{
			ToastShow("密码不允许为空");
		}
		else if(!p_pwd.equals(user_pwd1.getText().toString().trim()))
		{
			ToastShow("俩次输入密码不一致");
		}
		else
		{
			if(code_num == Integer.valueOf(code))
			{
				loadingDialog.show();
				LoadData();
			}
				
			else
				ToastShow("验证不正确");
		}
	}
	@Override
	public void LoadData() {
		// TODO Auto-generated method stub
		super.LoadData();
	}
	private Handler muHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if(msg.obj != null)
				{
					String str = (String)msg.obj;
					if(str.equals(Constant.ERROR))
					{
						ToastShow("注册失败");
					}
					else if(str.equals(Constant.SUCCESS))
					{
						finish();
						ToastShow("注册成功");
					}
					else if(str.equals(Constant.WARNING))
					{
						ToastShow("手机号已存在");
					}
					else
					{
						ToastShow(Constant.ERROR_MES);
					}
					loadingDialog.dismiss();
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
				case R.id.user_code_txt:
					if(isClick ==false)
					{
						phone = user_tel.getText().toString().trim();
						if(!phone.equals(""))
						{
							boolean is_tel = ValidationPhone(phone);
							if(is_tel == true)
							{
								//loadingDialog.show();
								GetUtel(phone);
							}
							else
							{
								ToastShow("手机号格式不正确");
							}
							
						}
						else
							ToastShow("手机号不允许空");
					}
					break;
				case R.id.rs_register:
					Jude();
					break;
				default:
					break;
				}
			}
		};
		//headerthemeleft.setOnClickListener(onClickListener);
		rs_register.setOnClickListener(onClickListener);
		user_code_txt.setOnClickListener(onClickListener);
	}
	
	/**
	 * 验证手机是否存在
	 * @Title GetUtel
	 * @Description TODO(这里用一句话描述这个方法的作用)
	 * @param @param tel    参数
	 * @return void    返回类型
	 */
	public void GetUtel(final String tel)
	{
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = mUIHandler.obtainMessage(1);
				//msg.obj = Person_Service.isPhone(tel);
				msg.sendToTarget();
			}
		}).start();
	}
	
	private  Handler mUIHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if(msg.obj!=null)
				{
					String str = (String)msg.obj;
					if(JsonUtil.JsonToObj(str, String.class)==null)
					{
						ToastShow(Constant.ERROR_MES);
						isClick=false;
					}
					else if(JsonUtil.JsonToObj(str, String.class).equals(Constant.SUCCESS))
					{
						ToastShow("手机号已注册");
						isClick=false;
					}
					else if(JsonUtil.JsonToObj(str, String.class).equals(Constant.ERROR))
					{
						GetCode();
					}
				}
				loadingDialog.dismiss();
				break;
			case 2:
				if(msg.obj!=null) //定时器关闭
				{
					timer.cancel();
					String mes = (String)msg.obj;
					user_code_txt.setText(mes);
				}	
				break;
			case 3:
				if(msg.obj!=null)  //启动定时器
				{
					String mes = (String)msg.obj;
					user_code_txt.setText(mes);
				}
				break;
			case 4:
				if(msg.obj!=null)
				{
					String  mes = (String)msg.obj;
					if(mes!=null)
					{
						String sms = mes;						
						for (int i = 0; i < code_error.length; i++) {
							if(sms.equals(code_error[i]))
							{
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
	
	
	public void GetCode()
	{
		if(isClick==false)
		{
			timer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					seconds--;
					try {
						if(seconds==0)
						{
							Message msg = mUIHandler.obtainMessage(2);
							msg.obj = "获取验证码";
							msg.sendToTarget();
							isClick = false;
							seconds=60;
						}
						else
						{
							Message msg = mUIHandler.obtainMessage(3);
							msg.obj = seconds+"秒重新获取";
							msg.sendToTarget();
							isClick = true;
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}


				}
			};
			timer.schedule(task, 1000,1000);
			ToastShow("请求已发送,请注意查收");
			GetSms();
		}
	}
	/**
	 * 获取手机验证码
	 * @Title GetSms
	 * @Description TODO(这里用一句话描述这个方法的作用)
	 * @return void    返回类型
	 */
	public void GetSms()
	{
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Random random = new Random();
				code_num = random.nextInt(1000000);
				String content = "验证码为:"+code_num+",验证码切不可泄露";
				String mes = SmsUtil.SendSms(phone, content);
				Message msg = mUIHandler.obtainMessage(4);
				msg.obj = mes;
				msg.sendToTarget();
			}
		}).start();
	}
	
	/**
	 * 手机号 验证
	 * @param phone
	 * @return
	 */
	public boolean ValidationPhone(String phone)
	{
		String ph = "1[3,4,5,7,8][0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|18[0-9]{9}$";
		if(phone.matches(ph))
		{
			return true;
		}
		return false;
	}
}
