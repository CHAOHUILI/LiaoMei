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
import com.vidmt.lmei.adapter.AdapterSelectFriends;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.dialog.ConnectionUtil;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.think.JsonUtil;
import com.vidmt.lmei.util.think.SideBar;
import com.vidmt.lmei.widget.ClearEditText;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.AdapterView.OnItemClickListener;

public class SelectFriendsActivity extends BaseActivity {

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

	@TAInjectView(id = R.id.search_edit)
	ClearEditText search_edit;

	@TAInjectView(id = R.id.selectfriendslist)
	ListView selectfriendslist;
	AdapterSelectFriends adapterhome;
	List<Persion> personlist;
	@TAInjectView(id = R.id.rela_error)
	RelativeLayout rela_error;

	int flag = 1;
	String key = "";
    int friendstype=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_friends);
		themes();
		InitView();
	}

	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
		Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
		user.setBackgroundDrawable(drawable);
		headerthemeleft.setVisibility(View.VISIBLE);
		headerright.setVisibility(View.VISIBLE);
		headconrel1.setVisibility(View.GONE);
		headercontentv.setVisibility(View.GONE);
		headercontent.setText("好友查找");
		selectfriendslist.setVisibility(View.GONE);
		rela_error.setVisibility(View.VISIBLE);
		flag = getIntent().getIntExtra("flag", 1);
		friendstype= getIntent().getIntExtra("friendstype", 1);
		if(friendstype==1){
			search_edit.setHint("查找好友");
		}else if(friendstype==2){
			search_edit.setHint("查找已关注用户");
		}else if(friendstype==3){
			search_edit.setHint("查找粉丝");
		}
		personlist = new ArrayList<Persion>();
		adapterhome = new AdapterSelectFriends(context, personlist, imageLoader, options);
		selectfriendslist.setAdapter(adapterhome);
		selectfriendslist.setSelector(new ColorDrawable(Color.TRANSPARENT));
		selectfriendslist.setFocusable(false);
		selectfriendslist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SelectFriendsActivity.this, HomeDetailActivity.class);
				intent.putExtra("userid", personlist.get(position).getId());
				startActivity(intent);
			}
		});
		search_edit.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				switch (actionId) {
				case EditorInfo.IME_ACTION_SEARCH:
					key = search_edit.getText().toString();
					if (search_edit.getText().toString().equals("")) {
						selectfriendslist.setVisibility(View.GONE);
						rela_error.setVisibility(View.VISIBLE);
					} else {
						selectfriendslist.setVisibility(View.VISIBLE);
						rela_error.setVisibility(View.GONE);
						// loadingDialog.show();
						LoadFriendsData();
					}
					break;

				default:
					break;
				}
				return false;
			}
		});
		/*
		 * search_edit.addTextChangedListener(new TextWatcher() {
		 * 
		 * @Override public void onTextChanged(CharSequence s, int start, int
		 * before, int count) { // TODO Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void beforeTextChanged(CharSequence s, int start,
		 * int count, int after) { // TODO Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void afterTextChanged(Editable s) { // TODO
		 * Auto-generated method stub key=search_edit.getText().toString();
		 * if(search_edit.getText().toString().equals("")){
		 * selectfriendslist.setVisibility(View.GONE);
		 * rela_error.setVisibility(View.VISIBLE); }else{
		 * selectfriendslist.setVisibility(View.VISIBLE);
		 * rela_error.setVisibility(View.GONE); //loadingDialog.show();
		 * LoadFriendsData(); } } });
		 */
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
						personlist.addAll(lists);
						selectfriendslist.setVisibility(View.VISIBLE);
						rela_error.setVisibility(View.GONE);
					} else {

						selectfriendslist.setVisibility(View.GONE);
						rela_error.setVisibility(View.VISIBLE);

					}
				} else {
					selectfriendslist.setVisibility(View.GONE);
					rela_error.setVisibility(View.VISIBLE);
				}
				// loadingDialog.dismiss();
				adapterhome.notifyDataSetChanged();
				break;
			default:
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
}
