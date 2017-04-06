package com.vidmt.lmei.dialog;


import com.vidmt.lmei.R;
import com.vidmt.lmei.activity.MainActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;


public class UpdateVersionDialog extends BaseDialog implements
android.view.View.OnClickListener {
	private TextView mMemoTV;

	private Activity mAct;
	private int index;
	private String mUpdateMemo;

	public UpdateVersionDialog(Context context, String pMemo,int index) {
		super(context);
		initData(context, pMemo,index);
	}
	public UpdateVersionDialog(Context context, int theme, String pMemo,int index) {
		super(context, theme);
		initData(context, pMemo,index);
	}
	private void initData(Context context, String pMemo,int index) {
		mAct = (Activity) context;
		mUpdateMemo = pMemo;
		this.index=index;
	}
	private void initView() {
		this.setCanceledOnTouchOutside(false);
		findViewById(R.id.btn_dialog_confirm).setOnClickListener(this);
		findViewById(R.id.btn_dialog_cancel).setOnClickListener(this);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_version_dialog);//dialog_update_version
		initView();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_dialog_confirm:
			if(index==1){
				((MainActivity) mAct).downLoadApk();
			}
			else{
				
			}
			dismiss();
			break;
		case R.id.btn_dialog_cancel:
			dismiss();
			if(index==1){
				((MainActivity) mAct).AcitvityStar();
			}
			break;
		default:
			break;
		}

	}

	public static UpdateVersionDialog show(Context ctx, String pMemo,int index) {
		UpdateVersionDialog d = new UpdateVersionDialog(ctx, pMemo,index);
		d.setCancelable(false);
		return d;
	}

	public static UpdateVersionDialog show(Context ctx, int theme, String pMemo,int index) {
		UpdateVersionDialog d = new UpdateVersionDialog(ctx, theme, pMemo,index);
		d.setCancelable(false);
		return d;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if(index==1){
				//((MainActivity) mAct).startMain();
			}
			dismiss();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
