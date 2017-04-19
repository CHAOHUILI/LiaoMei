package com.vidmt.lmei.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.R.menu;
import com.vidmt.lmei.adapter.AdapterFriends;
import com.vidmt.lmei.adapter.AdapterHome;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.dialog.ConnectionUtil;
import com.vidmt.lmei.dialog.CustomProgressDialog;
import com.vidmt.lmei.dialog.LoadingDialog;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.think.CnToSpellUtils;
import com.vidmt.lmei.util.think.JsonUtil;
import com.vidmt.lmei.util.think.PinyinComparator;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 主页好友，好友的activity
 */
public class FriendsActivity extends BaseActivity implements OnHeaderRefreshListener, OnFooterRefreshListener {
	public static LoadingDialog dialog = null;
	@TAInjectView(id = R.id.pull_friends)
	PullToRefreshView pull_friends;
	@TAInjectView(id = R.id.friendslist)
	ListView friendslist;
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
	int flag = 1;
	String key = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
		InitView();
		dialog.show();
		LoadFriendsData();
		showdata();
	}

	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
		pull_friends.setOnHeaderRefreshListener(this);
		pull_friends.setOnFooterRefreshListener(this);
		sidebar.setTextView(label);
		personlist = new ArrayList<Persion>();
		adapterhome = new AdapterFriends(context, personlist, imageLoader, options);
		friendslist.setAdapter(adapterhome);
		friendslist.setSelector(new ColorDrawable(Color.TRANSPARENT));
		friendslist.setFocusable(false);
		friendslist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FriendsActivity.this, HomeDetailActivity.class);
				intent.putExtra("userid", personlist.get(position).getId());
				intent.putExtra("pdshowdate", 1);
				startActivity(intent);
			}
		});
		sidebar.setOnTouchingLetterChangedListener(new LetterChanged());
	}

	private void showdata() {
		BroadcastReceiver broadcastReceiver = new BroadcastReceiver()

		{
			@Override
			public void onReceive(Context context, Intent intent) {
				LoadFriendsData();
			}
		};
		IntentFilter intentToReceiveFilter = new IntentFilter();
		intentToReceiveFilter.addAction("friendschild");
		registerReceiver(broadcastReceiver, intentToReceiveFilter);
	}

	public void LoadFriendsData() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// dialog.show();
				Message msg = mUHandler.obtainMessage(1);
				try {
					msg.obj = Person_Service.selectfriends(b_person.getId(), flag, key);
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
						personlist.clear();
						personlist.addAll(filledData(lists));
						 Collections.sort(personlist, new PinyinComparator());
						pull_friends.setVisibility(View.VISIBLE);
						rela_error.setVisibility(View.GONE);
						rela_broken.setVisibility(View.GONE);
					} else {

						if (ConnectionUtil.isConn(activity) == false) {
							// ConnectionUtil.setNetworkMethod(activity);
							pull_friends.setVisibility(View.GONE);
							rela_error.setVisibility(View.GONE);
							rela_broken.setVisibility(View.VISIBLE);
						} else {
							pull_friends.setVisibility(View.GONE);
							rela_error.setVisibility(View.VISIBLE);
							rela_broken.setVisibility(View.GONE);
						}

					}
				} else {
					pull_friends.setVisibility(View.GONE);
					rela_error.setVisibility(View.VISIBLE);
					rela_broken.setVisibility(View.GONE);
				}
				dialog.dismiss();
				adapterhome.notifyDataSetChanged();
				pull_friends.onFooterRefreshComplete();
				break;
			default:
				break;
			}
		}

	};

	private List<Persion> filledData(List<Persion> data) {
		List<Persion> mSortList = new ArrayList<Persion>();

		for (int i = 0; i < data.size(); i++) {
			Persion sortModel = new Persion();
			sortModel = data.get(i);
			String pinyin = CnToSpellUtils.getPinYin(data.get(i).getNick_name());
			String sortString = pinyin.substring(0, 1).toUpperCase();
			if (sortString.matches("[A-Z]")) {
				sortModel.setEnglishSorting(sortString.toUpperCase());
			} else {
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
					LoadFriendsData();
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
		LoadFriendsData();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		pull_friends.onHeaderRefreshComplete();
	}

	public class LetterChanged implements OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(String s) {
			// TODO Auto-generated method stub
			int position = adapterhome.getFirstPosAtList(s);
			if (position != -1) {
				friendslist.setSelection(position);
			}
		}

	}
}
