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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HotChatActivity extends BaseActivity implements OnHeaderRefreshListener, OnFooterRefreshListener {
	public static LoadingDialog dialog = null;
	@TAInjectView(id = R.id.pull_hot)
	PullToRefreshView pull_hot;
	@TAInjectView(id = R.id.hot_grid)
	GridView hot_grid;
	AdapterHome adapterhome;
	List<Persion> personlist;
	@TAInjectView(id = R.id.rela_error)
	RelativeLayout rela_error;
	@TAInjectView(id = R.id.rela_broken)
	RelativeLayout rela_broken;
	int id = 0;
	int orderCondition = 1;
	int sex = 0;
	int ident_state = 1;
	int pageIndex = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hot_chat);
		InitView();
		dialog.show();
		LoadUserData();
		userrefresh();
	}

	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
		pull_hot.setOnHeaderRefreshListener(this);
		pull_hot.setOnFooterRefreshListener(this);
		personlist = new ArrayList<Persion>();
		adapterhome = new AdapterHome(context, personlist, imageLoader, options);
		hot_grid.setAdapter(adapterhome);
		hot_grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
		hot_grid.setFocusable(false);
		hot_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(HotChatActivity.this, HomeDetailActivity.class);
				intent.putExtra("userid", personlist.get(position).getId());
				startActivity(intent);
				//StartActivity(HomeDetailActivity.class);
			}
		});
		//hot_grid.setOnScrollListener(new PauseOnScrollListener(imageLoader, true, false));  
	    id=b_person.getId();
	  
	}

	private void userrefresh()
	{
		BroadcastReceiver broadcastReceiver = new BroadcastReceiver()

		{
			@Override
			public void onReceive(Context context, Intent intent)
			{
				personlist.clear();
				pageIndex=1;
				ident_state=intent.getIntExtra("certification", 0);
				sex=intent.getIntExtra("sex", 0);
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
					if (!json.equals("") && !json.equals(JsonUtil.ObjToJson(Constant.ERROR))&&!json.equals(JsonUtil.ObjToJson("param_error"))) {
						List<Persion> lists = JsonUtil.JsonToObj(json, new TypeToken<List<Persion>>() {
						}.getType());
					
						//
						personlist.addAll(lists);
						hot_grid.setVisibility(View.VISIBLE);
						rela_error.setVisibility(View.GONE);
						rela_broken.setVisibility(View.GONE);
					} else {
						if (pageIndex == 1) {
							if (ConnectionUtil.isConn(activity) == false) {
								// ConnectionUtil.setNetworkMethod(activity);
								hot_grid.setVisibility(View.GONE);
								rela_error.setVisibility(View.GONE);
								rela_broken.setVisibility(View.VISIBLE);
							} else {
								hot_grid.setVisibility(View.GONE);
								rela_error.setVisibility(View.VISIBLE);
								rela_broken.setVisibility(View.GONE);
							}
						} else {
							ToastShow("没有更多数据");
						}

					}
				} else {
					hot_grid.setVisibility(View.GONE);
					rela_error.setVisibility(View.VISIBLE);
					rela_broken.setVisibility(View.GONE);
				}
				dialog.dismiss();
				adapterhome.notifyDataSetChanged();
				pull_hot.onHeaderRefreshComplete();
				pull_hot.onFooterRefreshComplete();
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
