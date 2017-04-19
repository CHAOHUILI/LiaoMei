package com.vidmt.lmei.activity;


import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.R.menu;
import com.vidmt.lmei.adapter.AdapterFriends;
import com.vidmt.lmei.adapter.IncomeListAdapter;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.entity.BalanceOfPayments;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.think.JsonUtil;
import com.vidmt.lmei.widget.PullToRefreshView;
import com.vidmt.lmei.widget.PullToRefreshView.OnFooterRefreshListener;
import com.vidmt.lmei.widget.PullToRefreshView.OnHeaderRefreshListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 支出记录页
 */
public class UserExpendActivity extends BaseActivity implements OnHeaderRefreshListener, OnFooterRefreshListener{

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

	@TAInjectView(id = R.id.pullview)
	PullToRefreshView pullview;
	@TAInjectView(id = R.id.expend_list)
	ListView expend_list;
	AdapterFriends adapterhome;
	List<Persion> personlist;
	@TAInjectView(id = R.id.rela_error)
	RelativeLayout rela_error;
	@TAInjectView(id = R.id.rela_broken)
	RelativeLayout rela_broken;
	int type;//
	List<BalanceOfPayments> list= new ArrayList<BalanceOfPayments>();
	int ym = 1;
	IncomeListAdapter incomeListAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_expend);
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
		headercontent.setText("支出记录");
		pullview.setOnHeaderRefreshListener(this);
		pullview.setOnFooterRefreshListener(this);
		Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
		user.setBackgroundDrawable(drawable);
		if(list.isEmpty()){
			rela_error.setVisibility(View.VISIBLE);
		}else{
			rela_error.setVisibility(View.GONE);
			IncomeListAdapter incomeListAdapter = new IncomeListAdapter(this, list,2,1);
			expend_list.setAdapter(incomeListAdapter);
			expend_list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent in = new Intent();
					in.setClass(UserExpendActivity.this, Income_list2Activity.class);
					int oid = list.get(position).getOther_id();
					in.putExtra("id", oid);
					//class 2 支出
					in.putExtra("class", 2);
					startActivity(in);
				}
			});
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
				String json = Person_Service.balance(b_person.getId(), 2, ym,"first");
				if(ym==1){
					Message msg = mUIHandler.obtainMessage(1);
					msg.obj = json;
					msg.sendToTarget();	
				}else{
					Message msg = mUIHandler.obtainMessage(2);
					msg.obj = json;
					msg.sendToTarget();	
				}
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
						list = JsonUtil.JsonToObj(mes, new TypeToken<List<BalanceOfPayments>>(){}.getType());
						InitView();
					}
				}
				pullview.onHeaderRefreshComplete();
				pullview.onFooterRefreshComplete();
				break;
			case 2:
				if(msg.obj!=null){
					String mes = (String)msg.obj;
					if(mes.equals("")){
						ToastShow("请求超时");
					}else{

						List<BalanceOfPayments> li= JsonUtil.JsonToObj(mes, new TypeToken<List<BalanceOfPayments>>(){}.getType());
						if(!li.isEmpty()){
							list.addAll(li);
						}else{
							ym=1;
						}
						InitView();
					}
				}
				pullview.onFooterRefreshComplete();
				break;
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

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		ym += 1;
		LoadData();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		pullview.onHeaderRefreshComplete();
	}
}
