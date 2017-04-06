package com.vidmt.lmei.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.drawable;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.adapter.AdapterSelectFriends;
import com.vidmt.lmei.adapter.AdapterStranger;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.think.JsonUtil;
import com.vidmt.lmei.widget.ClearEditText;

import android.app.Activity;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class SelectStrangerActivity extends BaseActivity {
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

	@TAInjectView(id = R.id.selectstrangerslist)
	ListView selectstrangerslist;
	AdapterStranger adapterhome;
	List<Persion> personlist;
	@TAInjectView(id = R.id.rela_errorstrange)
	RelativeLayout rela_errorstrange;
	String key = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_stranger);
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
		headercontent.setText("查找");
		selectstrangerslist.setVisibility(View.GONE);
		rela_errorstrange.setVisibility(View.VISIBLE);
		personlist = new ArrayList<Persion>();
		adapterhome = new AdapterStranger(SelectStrangerActivity.this, personlist, imageLoader, options,b_person.getId());
		selectstrangerslist.setAdapter(adapterhome);
		selectstrangerslist.setSelector(new ColorDrawable(Color.TRANSPARENT));
		selectstrangerslist.setFocusable(false);
		selectstrangerslist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SelectStrangerActivity.this, HomeDetailActivity.class);
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
					key=search_edit.getText().toString();
					if(search_edit.getText().toString().equals("")){
						selectstrangerslist.setVisibility(View.GONE);
						rela_errorstrange.setVisibility(View.VISIBLE);
					}else{
						selectstrangerslist.setVisibility(View.VISIBLE);
						rela_errorstrange.setVisibility(View.GONE);
						LoadStrangerData();
					}
					break;

				default:
					break;
				}
				return false;
			}
		});
		/*search_edit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				key=search_edit.getText().toString();
				if(search_edit.getText().toString().equals("")){
					selectstrangerslist.setVisibility(View.GONE);
					rela_errorstrange.setVisibility(View.VISIBLE);
				}else{
					selectstrangerslist.setVisibility(View.VISIBLE);
					rela_errorstrange.setVisibility(View.GONE);
					LoadStrangerData();
				}
			}
		});*/
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
					Intent intents = new Intent("friends");
					sendBroadcast(intents);
					break;		
				default:
					break;
				}
			}
		};
		headerthemeleft.setOnClickListener(onClickListener);
	}
	public void LoadStrangerData() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// dialog.show();
				Message msg = mUHandler.obtainMessage(1);
				try {
					msg.obj = Person_Service.selectstranger(b_person.getId(), key);
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
						selectstrangerslist.setVisibility(View.VISIBLE);
						rela_errorstrange.setVisibility(View.GONE);
					} else {

						selectstrangerslist.setVisibility(View.GONE);
						rela_errorstrange.setVisibility(View.VISIBLE);

					}
				} else {
					selectstrangerslist.setVisibility(View.GONE);
					rela_errorstrange.setVisibility(View.VISIBLE);
				}
				//loadingDialog.dismiss();
				adapterhome.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}

	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			this.finish();
			Intent intents = new Intent("friends");
			sendBroadcast(intents);
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
