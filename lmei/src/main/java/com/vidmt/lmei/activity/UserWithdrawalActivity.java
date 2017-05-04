package com.vidmt.lmei.activity;


import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.R.menu;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.util.think.JsonUtil;

import android.app.Activity;
import android.graphics.drawable.Drawable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 提现页
 */
public class UserWithdrawalActivity extends BaseActivity {
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
	@TAInjectView(id=R.id.withdrawalmoney)
	EditText withdrawalmoney;
	@TAInjectView(id=R.id.withdrawalnex)
	TextView withdrawalnex;
	String money="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_withdrawal);
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
		Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
		user.setBackgroundDrawable(drawable);
		headercontent.setText("提现");
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
				case R.id.withdrawalnex:
					money = withdrawalmoney.getText().toString().trim();							
					if (money.equals("")) {
						ToastShow("请填写金额");
					}else if(Double.valueOf(money)<50){
						ToastShow("每次提现金额必须大于或等于50元");
					}else if(b_person.getCapitalBalance()<50){
						ToastShow("资金不足50元，不能提现哦");
					} else{
						LoadData();
					}
					break;

				case R.id.headerthemeleft:
					finish();				
					break;
				default:
					break;
				}
			}
		};
		withdrawalnex.setOnClickListener(onClickListener);
		headerthemeleft.setOnClickListener(onClickListener);
	}

	/**
	 * 提现申请
	 */
	public void LoadData() {
		super.LoadData();
		loadingDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String json = Person_Service.applicationWithdraw(b_person.getId(), money);
				Message msg = mUIHandler.obtainMessage(1);
				msg.obj = JsonUtil.JsonToObj(json, String.class);
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
					String url = (String) msg.obj;
					if(url.equals("")){
						ToastShow("网络异常，请稍后重试");
					}else if("success".equals(url)){
						StartActivity(UserWithdrawalOkActivity.class);
						finish();
					}else if("error".equals(url)){
						ToastShow("提现失败，请稍后重试");
					}else if("fail".equals(url)){
						ToastShow("您还没有绑定支付宝账户");
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_withdrawal, menu);
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
