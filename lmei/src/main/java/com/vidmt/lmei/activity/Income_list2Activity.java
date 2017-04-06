package com.vidmt.lmei.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.adapter.IncomeListAdapter;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.entity.BalanceOfPayments;
import com.vidmt.lmei.util.think.JsonUtil;
import com.vidmt.lmei.widget.PullToRefreshView;
import com.vidmt.lmei.widget.PullToRefreshView.OnFooterRefreshListener;
import com.vidmt.lmei.widget.PullToRefreshView.OnHeaderRefreshListener;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Income_list2Activity extends BaseActivity implements OnHeaderRefreshListener, OnFooterRefreshListener {
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

	int sdk;
	int ym = 1;
	int oid;
	int type;// 1 收入，2支出
	@TAInjectView(id = R.id.pullview)
	PullToRefreshView pullview;
	@TAInjectView(id = R.id.income_list)
	ListView income_list;
	@TAInjectView(id = R.id.rela_error)
	RelativeLayout rela_error;
	@TAInjectView(id = R.id.rela_broken)
	RelativeLayout rela_broken;
	List<BalanceOfPayments> list = new ArrayList<BalanceOfPayments>();
	int stype = 2;// 2 级列表 3级列表

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_income);
		themes();
		pullview.setOnHeaderRefreshListener(this);
		pullview.setOnFooterRefreshListener(this);
		Intent in = getIntent();
		oid = in.getIntExtra("id", 0);
		type = in.getIntExtra("class", 1);
		LoadData();
	}

	public void InitView() {
		headerthemeleft.setVisibility(View.VISIBLE);
		headerright.setVisibility(View.VISIBLE);
		headconrel1.setVisibility(View.GONE);
		headercontentv.setVisibility(View.GONE);
		Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
		user.setBackgroundDrawable(drawable);
		headercontent.setText("记录详情");
		if (list.isEmpty()) {
			rela_error.setVisibility(View.VISIBLE);
		} else {
			rela_error.setVisibility(View.GONE);
			// tyep==1 收入
			stype = 2;
			IncomeListAdapter incomeListAdapter = new IncomeListAdapter(this, list, type, stype);
			income_list.setAdapter(incomeListAdapter);
			/*income_list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

					if (list.get(position).getType() == 1) {
						Intent in = new Intent();
						in.setClass(Income_list2Activity.this, Income_list3Activity.class);
						in.putExtra("class", type);
						BalanceOfPayments b = list.get(position);
						if (b.getToken() == null) {
							stype = 3;
							in.putExtra("id", oid);
							startActivity(in);
						} else if (b.getToken() != 2000 && b.getToken() != 12000 && b.getToken() != 50000) {
							stype = 3;
							in.putExtra("id", oid);
							startActivity(in);
						}
					}
				}
			});*/
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
	public void LoadData() {
		loadingDialog.show();
		super.LoadData();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String json = Person_Service.balance2(b_person.getId(), oid, type, ym, "second");
				if (ym == 1) {
					Message msg = mUIHandler.obtainMessage(1);
					msg.obj = json;
					msg.sendToTarget();
				} else {
					Message msg = mUIHandler.obtainMessage(2);
					msg.obj = json;
					msg.sendToTarget();
				}
			}
		}).start();
	}

	public void LoadData2() {
		loadingDialog.show();
		super.LoadData();
		Intent in = getIntent();
		oid = in.getIntExtra("id", 0);
		type = in.getIntExtra("class", 1);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String json = Person_Service.balance2(b_person.getId(), oid, type, ym, "third");
				if (ym == 1) {
					Message msg = mUIHandler.obtainMessage(1);
					msg.obj = json;
					msg.sendToTarget();
				} else {
					Message msg = mUIHandler.obtainMessage(2);
					msg.obj = json;
					msg.sendToTarget();
				}
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
					String mes = (String) msg.obj;
					if (mes.equals("")) {
						ToastShow("请求超时");
					} else {
						list = JsonUtil.JsonToObj(mes, new TypeToken<List<BalanceOfPayments>>() {
						}.getType());
						InitView();
					}
					loadingDialog.dismiss();
					pullview.onHeaderRefreshComplete();
					pullview.onFooterRefreshComplete();
				}
				break;
			case 2:
				if (msg.obj != null) {
					String mes = (String) msg.obj;
					if (mes.equals("")) {
						ToastShow("请求超时");
					} else {
						List<BalanceOfPayments> lis = JsonUtil.JsonToObj(mes, new TypeToken<List<BalanceOfPayments>>() {
						}.getType());
						if (!lis.isEmpty()) {
							list.addAll(lis);
						}
						InitView();
					}
					loadingDialog.dismiss();
					pullview.onFooterRefreshComplete();
				}
			}
		}
	};

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		ym += 1;
		LoadData();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		ym = 1;
		LoadData();
	}

}
