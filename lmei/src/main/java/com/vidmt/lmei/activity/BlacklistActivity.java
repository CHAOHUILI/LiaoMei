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
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.dialog.ConnectionUtil;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.think.CnToSpellUtils;
import com.vidmt.lmei.util.think.JsonUtil;
import com.vidmt.lmei.util.think.SideBar;
import com.vidmt.lmei.util.think.SideBar.OnTouchingLetterChangedListener;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BlacklistActivity extends BaseActivity implements OnHeaderRefreshListener, OnFooterRefreshListener{
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

	@TAInjectView(id = R.id.pull_blacklist)
	PullToRefreshView pull_blacklist;
	@TAInjectView(id = R.id.blacklist)
	ListView blacklist;
	AdapterFriends adapterhome;
	List<Persion> personlist;
	@TAInjectView(id = R.id.rela_error)
	RelativeLayout rela_error;
	@TAInjectView(id = R.id.rela_broken)
	RelativeLayout rela_broken;
	@TAInjectView(id = R.id.sidebarf)
	SideBar sidebar;
	@TAInjectView(id = R.id.label)
	TextView label;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blacklist);
		themes();
		InitView();
		LoadBlackListData();
		showdata();
	}

	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
		Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
		user.setBackgroundDrawable(drawable);
		headerthemeleft.setVisibility(View.VISIBLE);
		headerright.setVisibility(View.GONE);
		headercontent.setText("黑名单");
		
		pull_blacklist.setOnHeaderRefreshListener(this);
		pull_blacklist.setOnFooterRefreshListener(this);
		sidebar.setTextView(label);
		personlist = new ArrayList<Persion>();
		adapterhome = new AdapterFriends(context, personlist, imageLoader, options);
		blacklist.setAdapter(adapterhome);
		blacklist.setSelector(new ColorDrawable(Color.TRANSPARENT));
		blacklist.setFocusable(false);
		blacklist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(BlacklistActivity.this, HomeDetailActivity.class);
				intent.putExtra("userid", personlist.get(position).getId());
				intent.putExtra("pdshowdate", 1);
				startActivity(intent);
			}
		});
		sidebar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				int position = adapterhome.getFirstPosAtList(s);
				if(position != -1){
					blacklist.setSelection(position);
				}
			}
		});
	}
	private void showdata() {
		BroadcastReceiver broadcastReceiver = new BroadcastReceiver()

		{
			@Override
			public void onReceive(Context context, Intent intent) {
				LoadBlackListData();
			}
		};
		IntentFilter intentToReceiveFilter = new IntentFilter();
		intentToReceiveFilter.addAction("friendschild");
		registerReceiver(broadcastReceiver, intentToReceiveFilter);
	}
	public void LoadBlackListData() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// dialog.show();
				Message msg = mUHandler.obtainMessage(1);
				try {
					msg.obj = Person_Service.myblacklist(b_person.getId());
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
						personlist.clear();
						personlist.addAll(filledData(lists));
						//Collections.sort(personlist, new PinyinComparator());
						pull_blacklist.setVisibility(View.VISIBLE);
						rela_error.setVisibility(View.GONE);
						rela_broken.setVisibility(View.GONE);
					} else {
						
							if (ConnectionUtil.isConn(activity) == false) {
								// ConnectionUtil.setNetworkMethod(activity);
								pull_blacklist.setVisibility(View.GONE);
								rela_error.setVisibility(View.GONE);
								rela_broken.setVisibility(View.VISIBLE);
							} else {
								pull_blacklist.setVisibility(View.GONE);
								rela_error.setVisibility(View.VISIBLE);
								rela_broken.setVisibility(View.GONE);
							}					

					}
				} else {
					pull_blacklist.setVisibility(View.GONE);
					rela_error.setVisibility(View.VISIBLE);
					rela_broken.setVisibility(View.GONE);
				}
				loadingDialog.dismiss();
				adapterhome.notifyDataSetChanged();
				pull_blacklist.onFooterRefreshComplete();
				break;
			default:
				break;
			}
		}

	};
	private List<Persion> filledData(List<Persion>  data){
		List<Persion> mSortList = new ArrayList<Persion>();
		
		for(int i=0; i<data.size(); i++){
			Persion sortModel = new Persion();
			sortModel=data.get(i);
			String pinyin = CnToSpellUtils.getPinYin(data.get(i).getNick_name());
			String sortString = pinyin.substring(0, 1).toUpperCase();
			if(sortString.matches("[A-Z]")){
				sortModel.setEnglishSorting(sortString.toUpperCase());
			}else{
				sortModel.setEnglishSorting("#");
			}
			mSortList.add(sortModel);
		}
		return mSortList;
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
				case R.id.rela_error:
				case R.id.rela_broken:
					LoadBlackListData();
					break;
				case R.id.headerthemeleft:
					finish();
					break;
				default:
					break;
				}
			}
		};
		rela_error.setOnClickListener(onClickListener);
		rela_broken.setOnClickListener(onClickListener);
		headerthemeleft.setOnClickListener(onClickListener);
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
		LoadBlackListData();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		
	}
}
