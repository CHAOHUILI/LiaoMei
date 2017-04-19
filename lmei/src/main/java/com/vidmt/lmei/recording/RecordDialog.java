package com.vidmt.lmei.recording;

import java.io.File;
import java.io.IOException;

import com.vidmt.lmei.R;
import com.vidmt.lmei.activity.HomeActivity;
import com.vidmt.lmei.recording.RecordManger.SoundAmplitudeListen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RecordDialog {

	private Context context;
	/** 控制录音和上传 */
	private TalkNetManager talk;
	/** 显示录音振幅 */
	private ImageView progress;
	/** 显示录音车状态或报错图标 */
	private ImageView mic_icon;

	/** 显示录音振幅的图片缓存 */
	private Drawable[] progressImg = new Drawable[7];
	/** 取消按钮 */
	private TextView btn_cancel;
	/** 显示录音振幅 */
	private TextView btn_submit;
	/** 显示计时器 */
	private TextView text_msg;
	/** 显示录音振幅 */
	private TextView dialog_title;
	private  int type=0;

	/** 录音对话框视图 */
	private View dialog_view;
	/** 录音对话框 */
	private AlertDialog dialog;
	/** 服务器上传地址 */
	private String uploadServerUrl = "http://123.57.176.141:80/englishSummarizingApi/UploadServlet";
	/** 服务器下载播放地址 */
	private String downloadServerUrl = "http://123.57.176.141:80/englishSummarizingApi/DownloadFile?filename=";
	private int countTime = 1;
	private boolean isCountDownTime = false;
	private Handler handler = new Handler();
	private Runnable run = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			text_msg.setText(countTime + "秒");
			countTime++;
			if (countTime < 0) {
				handler.removeCallbacks(run);
				onSubmit.onClick(null);// 提交
				return;
			}
			handler.postDelayed(run, 1000);

		}
	};
	public RecordDialog(Context context,int type) {
		this.context = context;
		this.type=type;
		dialog_view = LayoutInflater.from(context).inflate(R.layout.dialog_sound, null);
		progressImg[0] = context.getResources().getDrawable(R.drawable.mic_1);// 初始化振幅图片
		progressImg[1] = context.getResources().getDrawable(R.drawable.mic_2);// 初始化振幅图片
		progressImg[2] = context.getResources().getDrawable(R.drawable.mic_3);// 初始化振幅图片
		progressImg[3] = context.getResources().getDrawable(R.drawable.mic_4);// 初始化振幅图片
		progressImg[4] = context.getResources().getDrawable(R.drawable.mic_5);// 初始化振幅图片
		progressImg[5] = context.getResources().getDrawable(R.drawable.mic_6);// 初始化振幅图片
		progressImg[6] = context.getResources().getDrawable(R.drawable.mic_7);// 初始化振幅图片

		dialog = new AlertDialog.Builder(new ContextThemeWrapper(context,R.style.themetransparent)).setView(dialog_view).show();
		dialog.hide();
		dialog.setOnDismissListener(onDismissListener);// 设置对话框回退键监听

		progress = (ImageView) dialog_view.findViewById(R.id.sound_progress);// 振幅进度条
		btn_cancel = (TextView) dialog_view.findViewById(R.id.cancel);// 取消键
		btn_submit = (TextView) dialog_view.findViewById(R.id.submit);// 提交键
		mic_icon = (ImageView) dialog.findViewById(R.id.mic);// 状态图标
		dialog_title = (TextView) dialog.findViewById(R.id.title);// 标题
		text_msg = (TextView) dialog.findViewById(R.id.msg);

		btn_cancel.setOnClickListener(onCancel);
		btn_submit.setOnClickListener(onSubmit);

		talk = new TalkNetManager(); // 初始化一个网络对话管理类
		talk.setUploadFileServerUrl(uploadServerUrl);// 设置文件上传网址ַ
		talk.setDownloadFileServerUrl(downloadServerUrl);  // 设置文件下载网址ַ
		talk.getRecordManger().setSoundAmplitudeListen(onSoundAmplitudeListen);// 设置振幅监听器

		talk.setDownloadFileFileStateListener(onDownloadFileFileStateListener);// 设置下载播放状态监听器
		talk.setUploadFileStateListener(onUploadFileStateListener);// 设置文件上传状态监听器

	}

	public boolean isCountDownTime() {
		return isCountDownTime;
	}

	public RecordDialog setCountDownTime(boolean isCountDownTime) {
		this.isCountDownTime = isCountDownTime;
		return this;
	}

	public void showDialog() {
		talk.startRecord();// 开始录音
		dialog.show();// 显示对话框

		if (isCountDownTime) {
			handler.post(run);// 显示计时器
		}
	}
	/**
	 * 删除自己的录音文件
	 * @throws IOException 
	 */
	public static void scanOldFile(String uploadFilename) throws IOException {
		File file = new File(Environment.getExternalStorageDirectory().getCanonicalFile() + "/juyouim/" + uploadFilename);//
		if (file.exists()) {
			file.delete();
		}
	}
	public TalkNetManager getTalk() {
		return talk;
	}
	/** 下载播放状态监听器 */
	private OnStateListener onDownloadFileFileStateListener = new OnStateListener() {
		@Override
		public void onState(int error, String msg) {
			// TODO Auto-generated method stub

		}
	};
	/** 文件上传状态监听器 */
	private OnStateListener onUploadFileStateListener = new OnStateListener() {

		@Override
		public void onState(int error, String msg) {
			// TODO Auto-generated method stub
			stopTime();
			if (error != 0) {
				progress.setVisibility(View.GONE);
				mic_icon.setBackgroundDrawable(null);
				mic_icon.setImageResource(R.drawable.voice_search_recognize_error);
				btn_submit.setText("����");
				dialog_title.setText(msg);
				return;
			}

			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		}
	};
	/** 回调振幅，根据振幅设置图片 */
	private SoundAmplitudeListen onSoundAmplitudeListen = new SoundAmplitudeListen() {

		@Override
		public void amplitude(int amplitude, int db, int value) {
			if (value >= 6) {
				value = 6;
			}
			progress.setBackgroundDrawable(progressImg[value]);// ��ʾ���ͼƬ

		}
	};
	/** 监听器-当对话框取消 */
	private OnClickListener onCancel = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			talk.stopRecord();
			dialog.cancel();
			dialog.dismiss();
			stopTime();
		}
	};
	/** 监听器-当对话框提交 */
	private OnClickListener onSubmit = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			//			dialog_title.setText("���紦����");
			File file=talk.stopRecordAndUpload();
				dialog.dismiss();
			stopTime();
		}
	};
	/** 监听器-按下回退键时停止录音 */
	private OnDismissListener onDismissListener = new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface arg0) {
			// TODO Auto-generated method stub
			talk.stopRecord();
			dialog.cancel();
			stopTime();
		}
	};

	private void stopTime() {
		handler.removeCallbacks(run);// 移除计时器
		text_msg.setText("");
	}
	public int getCountTime() {
		return countTime;
	}
	public RecordDialog setCountTime(int countTime) {
		this.countTime = countTime;
		return this;
	}

}
