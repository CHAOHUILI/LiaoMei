package com.vidmt.lmei.activity;




import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.util.rule.BitmapBlurUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * 视频播放页
 */
public class VideoPlayActivity extends BaseActivity {
	@TAInjectView(id = R.id.videoview)
	VideoView videoview;// 视频
	MediaController mediaController;
	public static String mp4url;
	public static String photourl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //设置无标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
				WindowManager.LayoutParams.FLAG_FULLSCREEN);  //设置全屏
		setContentView(R.layout.activity_video_play);
		mediaController = new MediaController(this);
		videoview.setVideoPath(mp4url);
		videoview.setMediaController(mediaController);
		mediaController.setMediaPlayer(videoview); 
		videoview.requestFocus();
		videoview.start();  
		
		//播放错误监听
		videoview.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				ToastShow("播放错误");
				//播放错误，
				
				finish();
				Intent intents = new Intent("personupdate");
				sendBroadcast(intents);				
				return false;
			}
		});
		
		//播放完成监听
		videoview.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				//播放完成，隐藏视频视图，显示照片
				finish();
				Intent intents = new Intent("personupdate");
				sendBroadcast(intents);	
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			this.finish();
				Intent intents = new Intent("personupdate");
				sendBroadcast(intents);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
