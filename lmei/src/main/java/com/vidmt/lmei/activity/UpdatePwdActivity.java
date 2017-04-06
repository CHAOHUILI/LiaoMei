package com.vidmt.lmei.activity;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.R.menu;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.util.think.JsonUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UpdatePwdActivity extends BaseActivity {

	@TAInjectView(id = R.id.update_password)
	EditText update_password;
	@TAInjectView(id = R.id.update_password1)
	EditText update_password1;
	@TAInjectView(id = R.id.update_btn)
	Button btn;
	private String tel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_pwd);
		InitView();
	}
		
	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
	
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
				case R.id.update_btn:
					String pwd = update_password.getText().toString().trim();
					String pwd1 = update_password1.getText().toString().trim();
					if(pwd.equals(""))
					{
						ToastShow("请输入密码");
					}
					else if(pwd1.equals(""))
					{
						ToastShow("请输入确认密码");
					}
					else if(!pwd.equals(pwd1))
					{
						ToastShow("俩次输入密码不一致,请重新输入");
					}
					else
					{
						loadingDialog.show();
						LoadData();
					}
					
					break;
				default:
					break;
				}
			}
		};
		//header_left_img.setOnClickListener(onClickListener);
		btn.setOnClickListener(onClickListener);
	}
	@Override
	public void LoadData() {
		// TODO Auto-generated method stub
		super.LoadData();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = mUHandler.obtainMessage(1);
				msg.obj = Person_Service.updatePwd(tel, update_password.getText().toString().trim());
				msg.sendToTarget();
			}
		}).start();
	}
	private Handler mUHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if(msg.obj!=null)
				{
					String str = (String)msg.obj;
					if(JsonUtil.JsonToObj(str, String.class)==null)
					{
						ToastShow(Constant.ERROR_MES);
					}
					if(JsonUtil.JsonToObj(str, String.class).equals(Constant.SUCCESS))
					{
						finish();
						ToastShow("修改成功");
					}
					else if(JsonUtil.JsonToObj(str,String.class).equals(Constant.ERROR))
					{
						ToastShow("修改失败");
					}
				}
				loadingDialog.dismiss();
				break;

			default:
				break;
			}
		}
		
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_pwd, menu);
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
