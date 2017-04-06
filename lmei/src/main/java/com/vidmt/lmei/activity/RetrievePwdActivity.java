package com.vidmt.lmei.activity;

import java.util.Random;
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
import com.vidmt.lmei.util.think.SmsUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RetrievePwdActivity extends BaseActivity {

	@TAInjectView(id = R.id.retreve_tel)
	EditText retreve_tel;
	@TAInjectView(id = R.id.retreve_code)
	EditText retreve_code;
	private String phone="";
	@TAInjectView(id = R.id.retrieve_code_txt)
	TextView retrieve_code_txt;
	private int seconds = 60; //定义120秒
	private boolean isClick;
	Timer timer;
	int code_num;
	private String[] code_error = new String[]{"-1","-2","-3","-4","-6","-11","-14","-21","-41","-42","-51"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retrieve_pwd);
		InitView();
	}

	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();

	}

	@Override
	public void LoadData() {
		// TODO Auto-generated method stub
		super.LoadData();
	}

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
				case R.id.headerright:
					String code = retreve_code.getText().toString().trim();
					if(!code.equals(""))
					{
						if(Integer.valueOf(code)==code_num)
						{
							finish();
							Intent intent = new Intent(RetrievePwdActivity.this, UpdatePwdActivity.class);
							intent.putExtra("tel", retreve_tel.getText().toString());
							startActivity(intent);
						}
						else
						{
							ToastShow("验证码有误");
						}
					}
					else
					{
						ToastShow("验证码不允许为空");
					}


					break;
				case R.id.retrieve_code_txt:
					if(isClick ==false)
					{
						phone  = retreve_tel.getText().toString();
						if(!phone.equals(""))
						{
							GetUtel(phone);
						}else {

							ToastShow("手机号不允许空");
						}
					}
					break;
				default:
					break;
				}
			}
		};
		//header_left_img.setOnClickListener(onClickListener);
		retrieve_code_txt.setOnClickListener(onClickListener);
		//header_right_txt.setOnClickListener(onClickListener);
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
					}
					else if(JsonUtil.JsonToObj(str, String.class).equals(Constant.SUCCESS))
					{
						GetCode();
						//GetSms();
					}
					else if(JsonUtil.JsonToObj(str, String.class).equals(Constant.ERROR))
					{
						ToastShow("手机号不存在");
					}
				}
				break;
			case 2:
				if(msg.obj!=null) //定时器关闭
				{
					timer.cancel();
					String mes = (String)msg.obj;
					retrieve_code_txt.setText(mes);
				}	
				break;
			case 3:
				if(msg.obj!=null)  //启动定时器
				{
					String mes = (String)msg.obj;
					retrieve_code_txt.setText(mes);
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
	 * @param     参数
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


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.retrieve_pwd, menu);
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
