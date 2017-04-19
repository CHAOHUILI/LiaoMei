package com.vidmt.lmei;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.activity.BaseActivity;
import com.vidmt.lmei.activity.LoginActivity;
import com.vidmt.lmei.activity.UserRechargeActivity;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.rule.ManageDataBase;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import io.rong.imkit.RongIM;

/**
 * 提示用户账户关闭页，界面是dialog
 */
public class CloseAccountActivity extends BaseActivity {
	@TAInjectView(id = R.id.dialogleft)
	TextView button1;
	@TAInjectView(id = R.id.dialogquxiao)
	TextView dialogquxiao ;
	@TAInjectView(id = R.id.dialogcontext)
	TextView dialogcontext;
	int state;////1自己的号被禁用了，2别人号被禁用了3异地登录4金币不足
	int type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_close_account);
		Intent in = getIntent();
		state = in.getIntExtra("state", 2);
		type = in.getExtras().getInt("type");
		if (type==1) {
			if(state==1){
				dialogcontext.setText("您的账号被管理员禁用");
			}else{
				dialogcontext.setText("该账号已被管理员禁用");
			}
		}else if (type==4) {
			dialogquxiao.setVisibility(View.VISIBLE);
			dialogcontext.setText("您的金币不足，是否去充值?");
			button1.setText("确定");
			
		}else {
			dialogcontext.setText("当前用户在其他设备上登录，请确保信息安全");
			button1.setText("重新登录");
		}
		
		
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
		 		if(state==1){
					gobreak();
				}else {
					//UserActivity.useractivity.finish();
				}
		 		if (type==2) {	
		 			ManageDataBase.Delete(dbutil, Persion.class, null);
		 			RongIM.getInstance().logout();
		 			Intent intents = new Intent("closeapp");
		 			sendBroadcast(intents);	
					Intent intent = new Intent(CloseAccountActivity.this, LoginActivity.class);
					startActivity(intent);
				}else if (type==4) {
					Intent intent = new Intent(CloseAccountActivity.this, UserRechargeActivity.class);
					startActivity(intent);
				}
				
			}
		});
		
		dialogquxiao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			return true;
		}
		return true;
	}
	
}
