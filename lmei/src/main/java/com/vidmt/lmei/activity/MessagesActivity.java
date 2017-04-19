package com.vidmt.lmei.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.adapter.MessageAdapter;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.dialog.MessageDialog;
import com.vidmt.lmei.entity.Notice;
import com.vidmt.lmei.util.think.JsonUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 系统消息页
 */
public class MessagesActivity extends BaseActivity {
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


	@TAInjectView(id = R.id.messagelist)
	private ListView messagelist;

	private MessageAdapter messageAdapter;
	private ArrayList<Notice> notices;

	@TAInjectView(id = R.id.rela_error)
	RelativeLayout rela_error;
	public static  MessagesActivity messagesActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);
        	InitView();
    		themes();
    		processCustomMessage(MessagesActivity.this);
    		LoadData();
    		messagesActivity= this;
    		
		//		changeno();
	}
	private void processCustomMessage(Context context) {
		Intent msgIntent = new Intent("vanish");
		msgIntent.putExtra("vanish", "");
		context.sendBroadcast(msgIntent);

	}
	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
		headerthemeleft.setVisibility(View.VISIBLE);
		headerright.setVisibility(View.VISIBLE);
		headconrel1.setVisibility(View.GONE);
		headercontentv.setVisibility(View.GONE);
		headercontent.setText("系统消息");
		Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
		user.setBackgroundDrawable(drawable);
		notices = new ArrayList<Notice>();	
		messageAdapter = new MessageAdapter(MessagesActivity.this, notices);
		messagelist.setAdapter(messageAdapter);
		messagelist.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				MessageDialog messageDialog= new MessageDialog(MessagesActivity.this,position);
				messageDialog.show();
				return false;
			}
		});
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
		// TODO Auto-generated method stub
		super.LoadData();
		loadingDialog.show();
		new Thread(new Runnable(){
			@Override
			public void run() {

				String json = Person_Service.getnotice(b_person.getId());
				Message msg = mUIHandler.obtainMessage(1);
				msg.obj = json;
				msg.sendToTarget();	
			}
		}).start();

		//		notices.add(new Notice(12, "2016-01-04 19:26:59", "欢迎来到趣恋，初次登录的用户获得10爱意"));
		//		notices.add(new Notice(13, "2016-03-04 19:26:59", "您已经充值100爱意，可以了聊天了哦"));
		//		notices.add(new Notice(134, "2016-04-04 19:26:59", "您送给帅到没朋友一朵玫瑰花"));
		//		notices.add(new Notice(13, "2016-04-13 19:26:59", "您收到陌上柳絮送的跑车，真是土豪啊"));
	}
	
	private void delnotent(final int id) {
		// TODO Auto-generated method stub
		loadingDialog.show();
		new Thread(new Runnable(){
			@Override
			public void run() {

				String json = Person_Service.delnotice(id, 3);
				Message msg = mUIHandler.obtainMessage(2);
				msg.obj = json;
				msg.sendToTarget();	
			}
		}).start();
	}
	//	private void changeno() {
	//		// TODO Auto-generated method stub
	//		new Thread(new Runnable(){
	//			@Override
	//			public void run() {
	//				
	//				String json = Persion_Service.changenotice(b_person.getId());
	//				Message msg = mUIHandler.obtainMessage(2);
	//				msg.obj = json;
	//				msg.sendToTarget();	
	//			}
	//		}).start();
	//	}
	public   Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (msg.obj != null) {
					String mes = (String) msg.obj;
					if("".equals(mes)){
						ToastShow("网络异常，请检查网络连接");
						rela_error.setVisibility(View.VISIBLE);
						messagelist.setVisibility(View.GONE);
					}else{
						List<Notice> noticeslist = JsonUtil.JsonToObj(mes,new TypeToken<List<Notice>>(){}.getType());
						notices.addAll(noticeslist);
						rela_error.setVisibility(View.GONE);
						messagelist.setVisibility(View.VISIBLE);
					}
				}
				messageAdapter.notifyDataSetChanged();
				loadingDialog.dismiss();
				break;
			case 2:
				if(msg.obj != null) {
					String mes = (String) msg.obj;
					if (mes.contains("success")) {
						ToastShow("删除成功");
					}
				}
				loadingDialog.dismiss();
				break;
			}
		}

	};

	public void delmessage(int position){
		delnotent(notices.get(position).getId());
		notices.remove(position);
		messageAdapter.notifyDataSetChanged();
	}
}
