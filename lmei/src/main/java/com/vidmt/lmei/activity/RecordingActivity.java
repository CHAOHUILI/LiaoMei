package com.vidmt.lmei.activity;

import java.io.File;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.recording.RecordDialog;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 暂无用
 */
public class RecordingActivity extends BaseActivity {
	@TAInjectView(id = R.id.button1)
	private Button button1;
	@TAInjectView(id = R.id.button2)
	private Button button2;
	public File file=null;
	String filename="";
	private RecordDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recording);
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
				case R.id.button1:
					//新建一个对话框
					dialog.setCountTime(1)//设置倒计时20秒
					.setCountDownTime(true);//设置启动倒计时
					dialog.showDialog();
					break;
				case R.id.button2:
					MediaPlayer mp=dialog.getTalk().MediaPlayer();
						try {
							if(file!=null){
								filename=file.getName();
							}else{
								if(filename.equals("")){
									filename=file.getName();
								}
							}
							dialog.getTalk().downloadFileAndPlay(filename);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					break;
				default:
					break;
				}
			}
		};
	}
}
