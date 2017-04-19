package com.vidmt.lmei.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.R.menu;
import com.vidmt.lmei.adapter.AdapterHome;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.dialog.ConnectionUtil;
import com.vidmt.lmei.dialog.CustomProgressDialog;
import com.vidmt.lmei.dialog.LoadingDialog;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.rule.SharedPreferencesUtil;
import com.vidmt.lmei.util.think.JsonUtil;
import com.vidmt.lmei.widget.PullToRefreshView;
import com.vidmt.lmei.widget.PullToRefreshView.OnFooterRefreshListener;
import com.vidmt.lmei.widget.PullToRefreshView.OnHeaderRefreshListener;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import junit.runner.BaseTestRunner;

/**
 * 主界面 广场页的 最新activity
 */
public class LatestChatActivity extends BaseActivity implements OnHeaderRefreshListener, OnFooterRefreshListener {
	public static LoadingDialog dialog = null;
	@TAInjectView(id = R.id.pull_latest)
	PullToRefreshView pull_latest;
	@TAInjectView(id = R.id.latest_grid)
	GridView latest_grid;
	AdapterHome adapterhome;
	List<Persion> personlist;
	@TAInjectView(id = R.id.rela_error)
	RelativeLayout rela_error;
	@TAInjectView(id = R.id.rela_broken)
	RelativeLayout rela_broken;

	int id = 0;
	int orderCondition = 0;
	int sex = 0;
	int ident_state = 1;
	int pageIndex = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_latest_chat);
		InitView();
		dialog.show();
		if(b_person.getSex()==1){
			sex=2;
	        SharedPreferencesUtil.putInt(getApplicationContext(),"filter_sex",2);
		}
		LoadUserData();
		userrefresh();
	}

	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
		pull_latest.setOnHeaderRefreshListener(this);
		pull_latest.setOnFooterRefreshListener(this);
		personlist = new ArrayList<Persion>();
		adapterhome = new AdapterHome(context, personlist, imageLoader, options);
		latest_grid.setAdapter(adapterhome);
		latest_grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
		latest_grid.setFocusable(false);
		latest_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LatestChatActivity.this, HomeDetailActivity.class);
		 		intent.putExtra("userid", personlist.get(position).getId());
				//intent.putExtra("user", personlist.get(position));
				startActivity(intent);
			}
		});
		//latest_grid.setOnScrollListener(new PauseOnScrollListener(imageLoader, true, false));  
		id = b_person.getId();
	}

	private void userrefresh() {
		BroadcastReceiver broadcastReceiver = new BroadcastReceiver()

		{
			@Override
			public void onReceive(Context context, Intent intent) {
				personlist.clear();
				pageIndex=1;
				ident_state = SharedPreferencesUtil.getInt(getApplicationContext(),"filter_ident_state",0);
				sex = SharedPreferencesUtil.getInt(getApplicationContext(),"filter_sex",0);
				dialog.show();
				LoadUserData();
			}
		};
		IntentFilter intentToReceiveFilter = new IntentFilter();
		intentToReceiveFilter.addAction("home");
		registerReceiver(broadcastReceiver, intentToReceiveFilter);
	}

	public void LoadUserData() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// dialog.show();
				Message msg = mUHandler.obtainMessage(1);
				try {
					msg.obj = Person_Service.getUserAll(id, orderCondition, sex, ident_state, pageIndex);
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
					if (!json.equals("") && !json.equals(JsonUtil.ObjToJson(Constant.ERROR))
							&& !json.equals(JsonUtil.ObjToJson("param_error"))) {
						List<Persion> lists = JsonUtil.JsonToObj(json, new TypeToken<List<Persion>>() {
						}.getType());
						personlist.addAll(lists);
						latest_grid.setVisibility(View.VISIBLE);
						rela_error.setVisibility(View.GONE);
						rela_broken.setVisibility(View.GONE);
					} else {
						if (pageIndex == 1) {
							if (ConnectionUtil.isConn(activity) == false) {
								// ConnectionUtil.setNetworkMethod(activity);
								latest_grid.setVisibility(View.GONE);
								rela_error.setVisibility(View.GONE);
								rela_broken.setVisibility(View.VISIBLE);
							} else {
								latest_grid.setVisibility(View.GONE);
								rela_error.setVisibility(View.VISIBLE);
								rela_broken.setVisibility(View.GONE);
							}
						} else {
							ToastShow("没有更多数据");
						}

					}
				} else {
					latest_grid.setVisibility(View.GONE);
					rela_error.setVisibility(View.VISIBLE);
					rela_broken.setVisibility(View.GONE);
				}
				dialog.dismiss();
				adapterhome.notifyDataSetChanged();
				pull_latest.onFooterRefreshComplete();
				pull_latest.onHeaderRefreshComplete();
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
				case R.id.rela_error:
				case R.id.rela_broken:
					LoadUserData();
					break;

				default:
					break;
				}
			}
		};
		rela_error.setOnClickListener(onClickListener);
		rela_broken.setOnClickListener(onClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.latest_chat, menu);
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
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		// pull_latest.onFooterRefreshComplete();
		pageIndex++;
		LoadUserData();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		pageIndex++;
		LoadUserData();
	}
}
