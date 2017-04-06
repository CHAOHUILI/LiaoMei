package com.vidmt.lmei.recording;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Environment;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

public class RecordManger {
	/**录音后文件*/
	private File file; 
	/**android媒体录音类*/
	private MediaRecorder mr;
	/**声波振幅监听器*/
	private SoundAmplitudeListen soundAmplitudeListen;
	/**启动计时器监听振幅波动*/
	private final Handler mHandler = new Handler();
	private Runnable mUpdateMicStatusTimer = new Runnable() {
		/**
		 * 分贝的计算公式K=20lg(Vo/Vi) Vo当前振幅值 Vi基准值为600
		 */
		private int BASE = 600;
		private int RATIO=5;
		private int postDelayed =200;
		public void run() {
			// int vuSize = 10 * mMediaRecorder.getMaxAmplitude() / 32768;  
			int ratio =mr.getMaxAmplitude() / BASE;  
			int db = (int) (20 * Math.log10(Math.abs(ratio)));  
			int value=db / RATIO;
			if(value<0)value=0;
			if(soundAmplitudeListen!=null)
				soundAmplitudeListen.amplitude(ratio, db, value);
			mHandler.postDelayed(mUpdateMicStatusTimer, postDelayed);
		}
	};

	/**启动录音并生成文件*/
	public void startRecordCreateFile() throws IOException {
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return;
		}
		File file1=new File(Environment.getExternalStorageDirectory()
				.getCanonicalFile() + "/juyouim/");
		if(!file1.isFile()){
			file1.mkdirs();
		}
		file = new File(Environment.getExternalStorageDirectory()
				.getCanonicalFile() + "/juyouim/"
				+ "YY"
				+ new DateFormat().format("yyyyMMdd_HHmmss",
						Calendar.getInstance(Locale.CHINA)) + ".amr");
		mr = new MediaRecorder(); // ����¼������
		mr.setAudioSource(MediaRecorder.AudioSource.MIC);  
		mr.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);  
		mr.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);  
		//		mr.setAudioSource(MediaRecorder.AudioSource.DEFAULT);// ����˷�Դ����¼��
		//		mr.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// ���������ʽ
		//		mr.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);// ���ñ����ʽ
		mr.setOutputFile(file.getAbsolutePath());// ��������ļ�

		// 创建文件
		file.createNewFile();
		// 准备录制
		mr.prepare();

		// 开始录制
		mr.start();
		//启动振幅监听计时器
		mHandler.post(mUpdateMicStatusTimer);

	}
    /**启动录音并生成文件*/
	public void startRecordCreateFile(String path) throws IOException {
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return;
		}
		file = new File("/sdcard/"+path);
		mr = new MediaRecorder(); // ����¼������
		mr.setAudioSource(MediaRecorder.AudioSource.MIC);  
		mr.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);  
		mr.setAudioEncoder(MediaRecorder.AudioEncoder.AAC); 
		//		mr.setAudioSource(MediaRecorder.AudioSource.DEFAULT);// ����˷�Դ����¼��
		//		mr.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// ���������ʽ
		//		mr.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);// ���ñ����ʽ
		mr.setOutputFile(file.getAbsolutePath());// ��������ļ�

		// 创建文件
		file.createNewFile();
		// 准备录制
		mr.prepare();

		// 开始录制
		mr.start();
		//启动振幅监听计时器
		mHandler.post(mUpdateMicStatusTimer);

	}
    /**停止录音并返回录音文件*/
	public File stopRecord() {

		if (mr != null) {
			mr.stop();
			mr.release();
			mr = null;
			mHandler.removeCallbacks(mUpdateMicStatusTimer);
		}
		return file;

	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public MediaRecorder getMr() {
		return mr;
	}

	public void setMr(MediaRecorder mr) {
		this.mr = mr;
	}
	public SoundAmplitudeListen getSoundAmplitudeListen() {
		return soundAmplitudeListen;
	}
	public void setSoundAmplitudeListen(SoundAmplitudeListen soundAmplitudeListen) {
		this.soundAmplitudeListen = soundAmplitudeListen;
	}
	public interface SoundAmplitudeListen{
		public void amplitude(int amplitude,int db,int value);
	}

}
