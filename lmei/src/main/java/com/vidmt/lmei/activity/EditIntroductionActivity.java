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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 个人简介，个性签名页
 */
public class EditIntroductionActivity extends BaseActivity {

	@TAInjectView(id = R.id.linheader)
	LinearLayout linheader;
	@TAInjectView(id = R.id.headerthemeleft)
	RelativeLayout headerthemeleft;
	@TAInjectView(id = R.id.user)
	ImageView user;
	@TAInjectView(id = R.id.headerright)
	LinearLayout headerright;
	@TAInjectView(id = R.id.typelog)
	ImageView typelog;
	@TAInjectView(id = R.id.headconfrim)
	TextView headconfrim;
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
	@TAInjectView(id = R.id.sig_text)
	EditText sig_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_introduction);
		themes();
		InitView();
	}

	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
		headerthemeleft.setVisibility(View.VISIBLE);
		headerright.setVisibility(View.GONE);
		headconrel1.setVisibility(View.GONE);
		headercontentv.setVisibility(View.GONE);
		Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
		user.setBackgroundDrawable(drawable);
		headconfrim.setVisibility(View.VISIBLE);
		headercontent.setText("个性签名");
		
	}

	@Override
	protected void onAfterSetContentView() {
		super.onAfterSetContentView();
		OnClickListener onclic = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.headerthemeleft:
					
					Intent intent = getIntent();
					Bundle data = new Bundle();
					data.putString("sig",sig_text.getText().toString());
					intent.putExtras(data);
					// 设置SecondActivity的结果码(resultCode)，并设置在当前结束后退回去的Activity
					EditIntroductionActivity.this.setResult(3, intent);
					EditIntroductionActivity.this.finish();
					break;
				}
			}
		};
		headerthemeleft.setOnClickListener(onclic);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_introduction, menu);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Intent intent = getIntent();
			Bundle data = new Bundle();
			  data.putString("sig",sig_text.getText().toString());
			intent.putExtras(data);
			// 设置SecondActivity的结果码(resultCode)，并设置在当前结束后退回去的Activity
			EditIntroductionActivity.this.setResult(3, intent);
			EditIntroductionActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
