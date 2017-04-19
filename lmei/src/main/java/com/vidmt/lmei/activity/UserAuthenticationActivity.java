package com.vidmt.lmei.activity;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.R.menu;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.rule.ManageDataBase;
import com.vidmt.lmei.util.think.JsonUtil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 认证选项页，包括照片和视频认证
 */
public class UserAuthenticationActivity extends BaseActivity {
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

	@TAInjectView(id = R.id.photo_up)
	TextView photo_up;
	@TAInjectView(id = R.id.viedo_up)
	TextView viedo_up;
	@TAInjectView(id = R.id.phototmoney)
	TextView phototmoney;
	@TAInjectView(id = R.id.videotmoney)
	TextView videotmoney;
	@TAInjectView(id = R.id.authenticationphotolin)
	RelativeLayout authenticationphotolin;
	@TAInjectView(id = R.id.authenticationvideolin)
	RelativeLayout authenticationvideolin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_authentication);
		themes();
		InitView();
		LoadAuthenticationData();
		LoadDataUpdate(b_person.getId());
	}

	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
		headerthemeleft.setVisibility(View.VISIBLE);
		headerright.setVisibility(View.VISIBLE);
		headconrel1.setVisibility(View.GONE);
		headercontentv.setVisibility(View.GONE);
		headercontent.setText("上传认证");
		Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
		user.setBackgroundDrawable(drawable);
		
	}

	public void LoadDataUpdate(final int pid) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String str = Person_Service.loginupdage(pid);
				Message msg = mUIHandler.obtainMessage(4);
				msg.obj = str;
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
			case 4:
				if (msg.obj != null && !msg.obj.equals("")) {
					String str = (String) msg.obj;
					if (!str.equals("") && !str.equals(JsonUtil.ObjToJson(Constant.ERROR))) {
						Persion p = JsonUtil.JsonToObj(str, Persion.class);
						ManageDataBase.Delete(dbutil, Persion.class, p.getId() + "");
						ManageDataBase.Insert(dbutil, Persion.class, p);
						b_person = p;
						if (b_person.getYet_photo() == 0  ) {
							phototmoney.setVisibility(View.VISIBLE);
							
						}else{
							phototmoney.setVisibility(View.GONE);
						
						}
                        if(b_person.getYet_video() == 0){
                        	videotmoney.setVisibility(View.VISIBLE);
                        }else{
                        	videotmoney.setVisibility(View.GONE);
                        }
						switch (b_person.getVideo_ident()) {
						case 2:
							viedo_up.setText("审核中");
							break;
						case 3:
							viedo_up.setText("已认证");
							break;
						case 4:
							viedo_up.setText("未通过");
							break;
						default:
							photo_up.setText("未认证");
							break;
						}
						switch (b_person.getIdent_state()) {
						case 4:
							photo_up.setText("审核中");
							break;
						case 5:
							photo_up.setText("已认证");
							break;
						case 6:
							photo_up.setText("未通过");
							break;
						default:
							photo_up.setText("未认证");
							break;
						}
					}
				}
				loadingDialog.dismiss();
				break;
			default:
				break;
			}
		}
	};			
	private void LoadAuthenticationData() {
		BroadcastReceiver broadcastReceiver = new BroadcastReceiver()

		{
			@Override
			public void onReceive(Context context, Intent intent) {
				int type = intent.getIntExtra("userauthenticattype", 0);
				if (type == 1) {
					viedo_up.setText("审核中");
				}else if (type == 2){
					photo_up.setText("审核中");
				}
			}
		};
		IntentFilter intentToReceiveFilter = new IntentFilter();
		intentToReceiveFilter.addAction("userauthenticat");
		registerReceiver(broadcastReceiver, intentToReceiveFilter);
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
				case R.id.authenticationphotolin:
					if (b_person.getIdent_state()!=null&&b_person.getIdent_state() == 4) {
						StartActivity(UserAuditActivity.class);
					}else  {
						StartActivity(PhotoAuthenticationActivity.class);
					}
					break;
				case R.id.authenticationvideolin:
					if (b_person.getVideo_ident()!=null&&b_person.getVideo_ident()==2) {
						StartActivity(UserAuditActivity.class);
					}else 
					{
						StartActivity(VideoAuthenticationActivity.class);
					}
					break;
				default:
					break;
				}
			}
		};
		headerthemeleft.setOnClickListener(onClickListener);
		authenticationphotolin.setOnClickListener(onClickListener);
		authenticationvideolin.setOnClickListener(onClickListener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_authentication, menu);
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
}
