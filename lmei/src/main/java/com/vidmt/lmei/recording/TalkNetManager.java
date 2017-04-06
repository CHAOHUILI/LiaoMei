package com.vidmt.lmei.recording;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;


import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;

public class TalkNetManager {
	int i=0;
	/** 录音管理类 */
	private RecordManger recordManger = new RecordManger();
	private Context context;
	/** 下载文件服务器地址 */
	private String downloadFileServerUrl;
	/** 上传文件服务器地址 */
	private String uploadFileServerUrl;
	private OnStateListener uploadFileStateListener;
	private OnStateListener downloadFileFileStateListener;
	/** 启动录音不进行网络上传 */
	public void startRecord() {
		try {
			recordManger.startRecordCreateFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/** 停止录音 */
	public File stopRecord() {
		return recordManger.stopRecord();// ֹͣ¼��
	}
	/** 停止录音后上传 */
	public File stopRecordAndUpload() {
		File file = recordManger.stopRecord();// ֹͣ¼��
		if (file == null || !file.exists() || file.length() == 0) {
			if (uploadFileStateListener != null)
				uploadFileStateListener.onState(-1,  "�ļ������ڻ��Ѿ���");
			return null;
		}
		//	
		return recordManger.getFile();
	}
	public String uploadFile(String file){
		new UpLoadecordFile().execute(file);// �����첽����
		return new File(file).getName();
	}
	/** 异步任务-录音上传 */
	public class UpLoadecordFile extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... parameters) {
			// TODO Auto-generated method stub11
			return UploadUtil.uploadFile(new File(parameters[0]),
					uploadFileServerUrl);
		}
		@Override
		protected void onPostExecute(String result) {
			//			if (result == null) {
			//				if (uploadFileStateListener != null)
			//					uploadFileStateListener.onState(-2,"-2");
			//				return;
			//			}
			//			if (result != null)
			//				if (uploadFileStateListener != null)
			//					uploadFileStateListener.onState(0,  "0");
		}
	}
	final Handler handler = new Handler();  
	final Runnable runnable = new Runnable() {  
		@Override  
		public void run() {  
			// handler�Դ�ʵ�ֶ�ʱ��  
			try {  

				handler.postDelayed(this, 1000);  
				System.out.println("lig"+Integer.toString(i++));  
			} catch (Exception e) {  
				// TODO Auto-generated catch block  
				e.printStackTrace();  
				System.out.println("exception...");  
			}  
		}  
	}; 
	MediaPlayer mediaPlayer = null;
	/** 先查询本地后加载服务器播放
	 * @throws IOException */
	public void downloadFileAndPlay(String uploadFilename) throws IOException {
		File file = new File(Environment.getExternalStorageDirectory()
				.getCanonicalFile() + "/juyouim/" + uploadFilename);//
		if(file.isFile()){
			handler.postDelayed(runnable, 1000);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mediaPlayer) {
					// TODO Auto-generated method stub
					mediaPlayer.release();
					handler.removeCallbacks(runnable);
					TalkNetManager.this.mediaPlayer=null;
				}
			});
			mediaPlayer.setDataSource(file.getPath());
			mediaPlayer.prepare();
			mediaPlayer.start();
			String a=gettime(file.getPath());
		}else{
			new DownloadRecordFile().execute(uploadFilename);
		}
	}
	/**
	 * mediaPlayer是否为空
	 */
	public MediaPlayer  MediaPlayer(){
		return mediaPlayer;
	}
	/**
	 * 停止播放录音
	 */
	public MediaPlayer  stop(){
		if(mediaPlayer!=null){
			mediaPlayer.release();
			mediaPlayer=null;
		}
		return mediaPlayer;
	}
	//获取语音时长
	private String gettime(String string)
	{   
		MediaPlayer player = new MediaPlayer();  //�������ȶ���һ��mediaplayer  
		try {
			player.setDataSource(string);  //String��ָ��Ƶ�ļ���·��
			player.prepare();        //�����mediaplayer�Ĳ���׼�� ����

		} catch (IllegalArgumentException e)
		{ e.printStackTrace();  } 
		catch (SecurityException e)
		{   e.printStackTrace();  } 
		catch (IllegalStateException e) 
		{e.printStackTrace();  } catch (IOException e) 
		{e.printStackTrace();  }
		player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() 
		{//����׼��

			@Override
			public void onPrepared(MediaPlayer player) 
			{
				int size = player.getDuration();
				String timelong = size / 1000 + "''";

			}
		});  
		double size =player.getDuration();//�õ���Ƶ��ʱ��
		String  timelong1 = (int) Math.ceil((size / 1000)) + "''";//ת��Ϊ�� ��λΪ''
		player.stop();//��ͣ����
		player.release();//�ͷ���Դ
		return  timelong1;  //������Ƶʱ��
		//TODO Auto-generated method stub
	}
	/** 下载后播放 */
	public class DownloadRecordFile extends AsyncTask<String, Integer, File> {
		@Override
		protected File doInBackground(String... parameters) {
			// TODO Auto-generated method stub11
			try {
				//				String filename = new DateFormat().format("yyyyMMdd_HHmmss",
				//						Calendar.getInstance(Locale.CHINA)) + ".amr";
				return FileHelper.DownloadFromUrlToData(downloadFileServerUrl
						+ parameters[0], parameters[0], context);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(File result) {
			if (result == null || !result.exists() || result.length() == 0) {
				if (downloadFileFileStateListener != null) {
					downloadFileFileStateListener.onState(-1,  "-1");
					return;
				}
			}
			MediaPlayer mediaPlayer = new MediaPlayer();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mediaPlayer) {
					// TODO Auto-generated method stub
					mediaPlayer.release();
				}
			});
			try {
				mediaPlayer.setDataSource(result.getPath());
				mediaPlayer.prepare();
				mediaPlayer.start();
				if (downloadFileFileStateListener != null) {
					downloadFileFileStateListener.onState(0, "0");
					return;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public RecordManger getRecordManger() {
		return recordManger;
	}

	public void setRecordManger(RecordManger recordManger) {
		this.recordManger = recordManger;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void setDownloadFileServerUrl(String downloadFileServerUrl) {
		this.downloadFileServerUrl = downloadFileServerUrl;
	}

	public String getUploadFileServerUrl() {
		return uploadFileServerUrl;
	}

	public void setUploadFileServerUrl(String uploadFileServerUrl) {
		this.uploadFileServerUrl = uploadFileServerUrl;
	}

	public OnStateListener getUploadFileStateListener() {
		return uploadFileStateListener;
	}

	public void setUploadFileStateListener(
			OnStateListener uploadFileStateListener) {
		this.uploadFileStateListener = uploadFileStateListener;
	}

	public OnStateListener getDownloadFileFileStateListener() {
		return downloadFileFileStateListener;
	}

	public void setDownloadFileFileStateListener(
			OnStateListener downloadFileFileStateListener) {
		this.downloadFileFileStateListener = downloadFileFileStateListener;
	}

	public String getDownloadFileServerUrl() {
		return downloadFileServerUrl;
	}
}