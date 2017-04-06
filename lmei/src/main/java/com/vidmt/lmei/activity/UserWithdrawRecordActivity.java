package com.vidmt.lmei.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.R.menu;
import com.vidmt.lmei.adapter.RecordAdapter;
import com.vidmt.lmei.adapter.WithdrawAdapter;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.entity.Recharge;
import com.vidmt.lmei.entity.Withdraw;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserWithdrawRecordActivity extends BaseActivity {

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
	
	@TAInjectView(id = R.id.text1)
	TextView text1;
	@TAInjectView(id = R.id.text2)
	TextView text2;
	@TAInjectView(id = R.id.text3)
	TextView text3;
	@TAInjectView(id=R.id.text4)
	TextView text4;
	@TAInjectView(id = R.id.listview1)
	ListView listview1;
	List<Withdraw> list = new ArrayList<Withdraw>();
	@TAInjectView(id = R.id.rela_error)
	RelativeLayout rela_error;
	@TAInjectView(id = R.id.rela_broken)
	RelativeLayout rela_broken;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_withdraw_record);
		themes();
		InitView();
		LoadData();
	}
	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
		headerthemeleft.setVisibility(View.VISIBLE);
		headerright.setVisibility(View.VISIBLE);
		headconrel1.setVisibility(View.GONE);
		headercontentv.setVisibility(View.GONE);
		headercontent.setText("提现记录");
		Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
		user.setBackgroundDrawable(drawable);
		text1.setText("时间");
		text2.setText("金额");
		text3.setText("手续费");
		text4.setText("状态");
		if(list.isEmpty()){
			rela_error.setVisibility(View.VISIBLE);
		}else{
			rela_error.setVisibility(View.GONE);
		WithdrawAdapter withdrawadapter = new WithdrawAdapter(this,list);
		listview1.setAdapter(withdrawadapter);
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

				case R.id.headerthemeleft:
					finish();
					break;
				default:
					break;
				}
			}
		};
		headerthemeleft.setOnClickListener(onClickListener);
	}

	@Override
	public void LoadData(){
		super.LoadData();
		new Thread(new Runnable(){
			@Override
			public void run() {
				String json = Person_Service.selectWithdrawByPid(b_person.getId());//1 充值，2提现
				Message msg = mUIHandler.obtainMessage(1);
				msg.obj = json;
				msg.sendToTarget();	
			}
		}).start();
	}
	public  Handler mUIHandler = new Handler()	{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if(msg.obj!=null){
					String mes = (String)msg.obj;
					if(mes.equals("")){
						ToastShow("请求超时");
					}else{
					list = JsonUtil.JsonToObj(mes, new TypeToken<List<Withdraw>>(){}.getType());
					InitView();
				}
			}
		}
		}
	};
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
