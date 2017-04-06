package com.vidmt.lmei.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.R.menu;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.entity.Address;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.recording.UploadUtil;
import com.vidmt.lmei.util.rule.Base64Coder;
import com.vidmt.lmei.util.rule.SDcardTools;
import com.vidmt.lmei.util.rule.SharedPreferencesUtil;
import com.vidmt.lmei.util.think.JsonUtil;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoAuthenticationActivity extends BaseActivity {
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

	@TAInjectView(id = R.id.cf)
	TextView cf;
	@TAInjectView(id = R.id.example_img1)
	ImageView example_img1;// 播放按钮
	@TAInjectView(id = R.id.example_img)
	ImageView example_img;// 视频缩略图
	@TAInjectView(id = R.id.example_video)
	VideoView example_video;
	int sdk;
	String load;
	String file_name;
	String path;
	// String requestURL = "http://192.168.1.66:8080/loveApi/fileUpload";
	String VideoPath = "";// 返回来的 视频地址
	int voidetype = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_authentication);
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
		headercontent.setText("上传认证视频");
		Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
		user.setBackgroundDrawable(drawable);
		voidetype = getIntent().getIntExtra("voidetype", 0);
		try {
			String path = inputMp4("viedo_example.mp4");
			example_video.setVideoPath(path);
			example_video.requestFocus();
			MediaMetadataRetriever media = new MediaMetadataRetriever();
			media.setDataSource(path);
			Bitmap bitmap = media.getFrameAtTime();
			example_img.setImageBitmap(bitmap);
			// example_img.setImageBitmap(getVideoThumbnail(path, 96, 96,
			// MediaStore.Images.Thumbnails.MICRO_KIND));
			// example_video.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 播放错误监听
		example_video.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				ToastShow("播放错误");
				return false;
			}
		});
		// 播放完成监听
		example_video.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				// 播放完成，隐藏视频视图，显示照片
				example_img1.setVisibility(View.VISIBLE);
			}
		});
	}

	public String inputMp4(String fileName) {
		String newFilename = SDcardTools.getSDPath() + "/example_mp4.mp4";
		try {
			File file = new File(newFilename);
			// 如果目标文件已经存在，则删除。产生覆盖旧文件的效果
			if (file.exists()) {
				file.delete();
			}
			InputStream in = getResources().getAssets().open(fileName);

			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流
			OutputStream os = new FileOutputStream(newFilename);
			// 开始读取
			while ((len = in.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			// 完毕，关闭所有链接
			os.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newFilename;
	}

	protected void voiceAdd(final File file) {
		loadingDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String request = UploadUtil.uploadFile(file, Constant.FILEUPLOAD);
				Message msg = mUIHandler.obtainMessage(1);
				msg.obj = request;
				msg.sendToTarget();
			}
		}).start();
	}

	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (msg.obj != null) {
					String mes = (String) msg.obj;
					if (mes.equals("")) {
						ToastShow("上传失败请重试");
					} else {
						Address ads = JsonUtil.JsonToObj(mes, Address.class);
						VideoPath = ads.getReturnPath();
						Upvideo();
					}
				}else{
					ToastShow("上传失败请重试");
				}
				loadingDialog.dismiss();
				break;
			case 2:
				if (msg.obj != null) {
					String mes = (String) msg.obj;
					if (mes.equals("")) {
						ToastShow("上传失败请重试");
					} else {
						ToastShow("上传成功");
						Persion p = JsonUtil.JsonToObj(mes, Persion.class);
						dbutil.updateData(p, "id=" + b_person.getId());
						b_person = p;
						Intent intents = new Intent("userauthenticat");
						intents.putExtra("userauthenticattype", 1);
						sendBroadcast(intents);
						finish();
					}
				}else{
					ToastShow("上传失败请重试");
				}
				loadingDialog.dismiss();
				break;
			}
		}
	};

	public void Upvideo() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Persion p = new Persion();
				p.setId(b_person.getId());
				p.setVideo(VideoPath);
				p.setVideo_ident(2);
				String persion = Person_Service.editDateD(JsonUtil.ObjToJson(p));
				Message msg = mUIHandler.obtainMessage(2);
				msg.obj = persion;
				msg.sendToTarget();
			}
		}).start();
	}

	/**
	 * encodeBase64File:(将文件转成base64 字符串). <br/>
	 * 
	 * @author guhaizhou@126.com
	 * @param path
	 *            文件路径
	 * @return
	 * @throws Exception
	 * @since JDK 1.6
	 */
	public String encodeBase64File(String path) throws Exception {
		File file = null;
		ContentResolver contentResolver = this.getContentResolver();
		String[] projection = new String[] { MediaStore.Video.Media.DATA };
		Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null,
				MediaStore.Video.Media.DEFAULT_SORT_ORDER);
		int column_index = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
		cursor.moveToFirst();
		file = new File(cursor.getString(column_index));
		cursor.close();
		FileInputStream inputFile = null;
		try {
			inputFile = new FileInputStream(file);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return new String(Base64Coder.encode(buffer));// Base64.encodeToString(buffer,Base64.DEFAULT);
	}


	/**
	 * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
	 * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
	 * 
	 * @param videoPath
	 *            视频的路径
	 * @param width
	 *            指定输出视频缩略图的宽度
	 * @param height
	 *            指定输出视频缩略图的高度度
	 * @param kind
	 *            参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
	 *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
	 * @return 指定大小的视频缩略图
	 */
	private Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
		Bitmap bitmap = null;
		// 获取视频的缩略图
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		System.out.println("w" + bitmap.getWidth());
		System.out.println("h" + bitmap.getHeight());
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
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
				
				case R.id.cf:
					String sdk = android.os.Build.VERSION.SDK;
                                        
					Intent intent = new Intent();
					intent.setType("video/*"); // 选择视频 （mp4 3gp 是android支持的视频格式）
					if ((Integer.valueOf(sdk).intValue())>=19) {
						intent.setAction(Intent.ACTION_PICK);
						intent.setData(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);// 使用以上这种模式，并添加以上两句
					} else {
						intent.setAction(Intent.ACTION_GET_CONTENT);
					}
					startActivityForResult(intent, 1);
					break;
				case R.id.example_img1:
					example_img1.setVisibility(View.GONE);
					example_img.setVisibility(View.GONE);
					example_video.start();
					break;
				default:
					break;
				}
			}
		};
		cf.setOnClickListener(onClickListener);
		example_img1.setOnClickListener(onClickListener);
		headerthemeleft.setOnClickListener(onClickListener);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			File file = null;
			if (data.getDataString().contains("file://")) {
				try {
					file = new File(URLDecoder.decode(data.getDataString().replace("file://", ""),"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					ToastShow("无法转换为英文");
				}
			} else {

				ContentResolver contentResolver = VideoAuthenticationActivity.this.getContentResolver();
				String[] projection = new String[] { MediaStore.Video.Media.DATA };
				Cursor cursor = contentResolver.query(data.getData(), projection, null, null,
						MediaStore.Video.Media.DEFAULT_SORT_ORDER);
				int count = cursor.getCount();
				try {
					cursor.moveToFirst();
					// int id =
					// cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
					int column_index = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
					String path = cursor.getString(column_index);
					if (path != null) {
						file = new File(path);
						cursor.close();
					} else {
						ToastShow("视频路径不正确，请重新选择！");
					}

				} catch (Exception e) {
					// TODO: handle exception
					ToastShow("视频路径不正确，请重新选择！");
				}
			}
			Log.e("*********************************视频大小", file.length() / 1024 / 1024 + "M");

			String file_name;
			try {
				file_name = URLDecoder.decode(file.getName(),"UTF-8");
				String namea = (String) file_name.subSequence(file_name.length() - 3, file_name.length());
				Log.e("*********************************视频name", namea);
				if ("mp4".equals(namea) || "Mp4".equals(namea)) {
					if ((file.length() / 1024 / 1024) > 20) {
						ToastShow("文件太大了，请上传小于20M的视频");
					} else {
						voiceAdd(file);
					}
				} else {
					ToastShow("请选择MP4格式的视频文件");
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				ToastShow("无法转换为英文");
			};
			
		}
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
		getMenuInflater().inflate(R.menu.video_authentication, menu);
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
