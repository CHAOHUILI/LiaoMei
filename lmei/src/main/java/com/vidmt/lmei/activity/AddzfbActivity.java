package com.vidmt.lmei.activity;

import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.entity.Aliacount;
import com.vidmt.lmei.util.think.JsonUtil;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 钱包，提现账户，添加支付宝，填写信息页
 * 
 * @author as
 *
 */
public class AddzfbActivity extends BaseActivity {

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

	@TAInjectView(id = R.id.zh)
	EditText zh;
	@TAInjectView(id = R.id.name)
	EditText name;
	@TAInjectView(id = R.id.addzfb)
	TextView addzfb;
	int alaccount = 0;
	public static Aliacount ali;
	String account = "";
	String namezfb = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addzfb);
		themes();
		InitView();
	}

	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
		alaccount = getIntent().getIntExtra("alaccount", 0);
		headerthemeleft.setVisibility(View.VISIBLE);
		headerright.setVisibility(View.VISIBLE);
		headconrel1.setVisibility(View.GONE);
		headercontentv.setVisibility(View.GONE);
		Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
		user.setBackgroundDrawable(drawable);
		if (alaccount == 1) {
			headercontent.setText("编辑支付宝");
			zh.setText(ali.getAccount() + "");
			name.setText(ali.getRealname() + "");
		} else {
			headercontent.setText("添加支付宝");
			
		}
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
				case R.id.addzfb:
					account = zh.getText().toString().trim();
					namezfb = name.getText().toString().trim();					
					if (account.equals("")) {
						ToastShow("请填写账户");
					} else if (namezfb.equals("")){
						ToastShow("请填写姓名");
					}else{
						AddAli();
					}
					break;

				case R.id.headerthemeleft:
					finish();						
					Intent intents = new Intent("wallet");
					sendBroadcast(intents);
					break;
				default:
					break;
				}
			}
		};
		addzfb.setOnClickListener(onClickListener);
		headerthemeleft.setOnClickListener(onClickListener);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			this.finish();
			Intent intents = new Intent("wallet");
			sendBroadcast(intents);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	// 绑定支付宝
	public void AddAli() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String json = Person_Service.AddAliCount(b_person.getId(), account, namezfb);
				Message msg = mUIHandler.obtainMessage(1);
				msg.obj = json;
				msg.sendToTarget();
			}
		}).start();
	}

	public Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (msg.obj != null) {
					String mes = JsonUtil.JsonToObj((String) msg.obj, String.class);
					if ("".equals(mes)) {
						ToastShow("访问网络超时");
					} else if ("success".equals(mes)) {
						ToastShow("绑定成功");
						finish();
						Intent intents = new Intent("wallet");
						sendBroadcast(intents);
					} else if ("fail".equals(mes)) {
						ToastShow("绑定失败");
					}
				}
				break;
			}
		}
	};

}
