package com.vidmt.lmei.activity;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 审核中页面
 */
public class UserAuditActivity extends BaseActivity {
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
	int voidetype = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_audit);
		themes();
		InitView();
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
	voidetype = getIntent().getIntExtra("voidetype", 0);
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
 					if (voidetype == 1) {
						Intent intents = new Intent("personupdate");
						sendBroadcast(intents);
					}
 					break;
 				default:
 					break;
 				}
 			}
 		};
 		headerthemeleft.setOnClickListener(onClickListener);
 	}
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			this.finish();
			if (voidetype == 1) {
				Intent intents = new Intent("personupdate");
				sendBroadcast(intents);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_audit, menu);
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
