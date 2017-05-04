package com.vidmt.lmei;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.activity.BaseActivity;
import com.vidmt.lmei.activity.HomeDetailActivity;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Chat_Service;
import com.vidmt.lmei.dialog.ConnectionUtil;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.entity.Present;
import com.vidmt.lmei.util.rule.SharedPreferencesUtil;
import com.vidmt.lmei.util.think.JsonUtil;
import com.vidmt.lmei.widget.CircleImageView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import io.rong.calllib.RongCallCommon;

/**
 * 评价界面，给聊天的人评分
 */
public class EvaluateActivity extends BaseActivity {
	@TAInjectView(id = R.id.evaratingBar)
	private RatingBar evaratingBar;//评价星级
	@TAInjectView(id = R.id.confirmbtn)
	private  TextView  confirmbtn;//确定
	@TAInjectView(id = R.id.headerthemeleft)
	RelativeLayout headerthemeleft;
	@TAInjectView(id = R.id.headercontent)
	TextView headercontent;
	@TAInjectView(id = R.id.user)
	private ImageView left1;
	@TAInjectView(id = R.id.p_img)
	private CircleImageView p_img;
	private int star;
	private int orderid;
	private String img;
	private int chatid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluate);
		InitView();
		themes();
	}
	@Override
	public void LoadData() {
		// TODO Auto-generated method stub
		super.LoadData();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String json =Chat_Service.grade(orderid, star);
				android.os.Message  msg = mUIHandler.obtainMessage(1);
				msg.obj = json;
				msg.sendToTarget();		
			}
		}).start();


	}
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {

			case 1:
				//   参数有误 = param_error
				//				订单不存在 = order_notexist
				//				需要重新提交 = change_again
				//				成功 = success

				if (msg.obj != null) {
					String mes = (String) msg.obj;
					if("".equals(mes)){
						ToastShow("网络异常，请检查网络连接");
					}else if (mes.contains("param_error")) {
						ToastShow("参数有误");
					}else if (mes.contains("order_notexist")) {
						ToastShow("订单不存在");
					}else if (mes.contains("change_again")) {
						ToastShow("需要重新提交");
					}else if (mes.contains("success")) {
						ToastShow("评价成功");
						
						SharedPreferencesUtil.putInt(EvaluateActivity.this, "chatid", 0);
						confirmbtn.setVisibility(View.GONE);
						finish();
					}
				}
				break;
			default:
				break;
			}
		}
	};
	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
		headerthemeleft.setVisibility(View.VISIBLE);
		headercontent.setText("评分");
		left1.setImageResource(R.drawable.goback);

		Intent intent = getIntent();
		img = intent.getExtras().getString("img");
		orderid = intent.getExtras().getInt("chatid");
		imageLoader.displayImage(img, p_img, options);
		final int chatid = SharedPreferencesUtil.getInt(EvaluateActivity.this, "chatid", 0);

		left1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}

		}
				);


//		vend(orderid);
		confirmbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if ((int)evaratingBar.getRating()==0) {

					ToastShow("您还没有评分");
				}else {

					star = (int)evaratingBar.getRating(); 


				   
					if (chatid==orderid) {
						LoadData();
					}else {
						orderid =chatid;
						LoadData();
					}

				}
			}
		});

	}
	public void vend(final int chatid) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String Rankingmap = null;
				Rankingmap = Chat_Service.vcutmoney(chatid);


				android.os.Message msg = mUIHandler.obtainMessage(2);
				msg.obj = Rankingmap;
				msg.sendToTarget();				
			}
		}).start();
	}

}
