package com.vidmt.lmei.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.drawable;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.entity.Address;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.recording.UploadUtil;
import com.vidmt.lmei.util.think.JsonUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoUploadAuthenticationActivity extends BaseActivity {

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

	@TAInjectView(id = R.id.up_img)
	ImageView up_img;
	@TAInjectView(id = R.id.submit)
	TextView submit;
	@TAInjectView(id = R.id.repeat)
	TextView repeat;
	public static Bitmap photo;
	public static File file;
	Uri path = null;
	int id ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_upload_authentication);
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
		headercontent.setText("上传照片");
		Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
		user.setBackgroundDrawable(drawable);
		up_img.setImageBitmap(photo);
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
				case R.id.repeat:
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
					startActivityForResult(intent, 1);
					break;
				case R.id.submit:
					if (photo != null) {
						if (file != null) {
							Upphoto(file);
						}
					} else {
						Toast.makeText(context, "您还没有拍照哦！", Toast.LENGTH_LONG).show();
					}
					break;
				default:
					break;
				}
			}
		};
		headerthemeleft.setOnClickListener(onClickListener);
		repeat.setOnClickListener(onClickListener);
		submit.setOnClickListener(onClickListener);
	}

	// 使用系统当前日期加以调整作为照片的名称
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	// 创建一个以当前时间为名称的文件
	File tempFile = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());

	/**
	 * 上传文件
	 * 
	 * @param file
	 */
	public void Upphoto(final File file) {
		loadingDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String request = UploadUtil.uploadFile(file, Constant.FILEUPLOAD);
				Message msg = mUIHandler.obtainMessage(2);
				msg.obj = request;
				msg.sendToTarget();
			}
		}).start();
	}

	protected void editDate(final String json) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String jhuser = Person_Service.editDateD(json);
				Message msg = mUIHandler.obtainMessage(1);
				msg.obj = jhuser;
				msg.sendToTarget();
			}
		}).start();
	}

	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (msg.obj != null) {
					String mes = (String) msg.obj;
					if ("".equals(mes)) {
						ToastShow("上传失败请重试");
					} else {
						ToastShow("上传成功");
						Persion p = JsonUtil.JsonToObj(mes, Persion.class);
						// dbutil.deleteData(Persion.class,"id='"+id+"'");
						dbutil.updateData(p, "id='" + id + "'");
						b_person = p;
						Intent intents = new Intent("userauthenticat");
						intents.putExtra("userauthenticattype", 2);
						sendBroadcast(intents);
						finish();
					}
				}
				loadingDialog.dismiss();
				break;
			case 2:
				if (msg.obj != null) {
					String mes = (String) msg.obj;
					if (mes.equals("")) {
						ToastShow("上传失败请重试");
						loadingDialog.dismiss();
					} else {
						Address ads = JsonUtil.JsonToObj(mes, Address.class);
						id = b_person.getId();
						Persion p = new Persion();
						p.setId(id);
						p.setPhoto_ident(ads.getReturnPath());
						p.setIdent_state(4);
						String json = JsonUtil.ObjToJson(p);
						editDate(json);
					}
				} else {
					ToastShow("上传失败请重试");
					loadingDialog.dismiss();
				}
				break;

			}
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Log.i("TestFile", "SD 不可见");
				return;
			}
			switch (requestCode) {
			case 1:
				startPhotoZoom(Uri.fromFile(tempFile));
				break;
			case 2:
				if (data != null) {
					setPicToView(data);
				}
			default:
				break;
			}
		}
	}

	// 将进行剪裁后的图片显示到UI界面上
	private void setPicToView(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			photo = getBitmapFromUri(getTempUri());
			up_img.setImageBitmap(photo);
			compressImageByPixel(tempFile.getPath());
		}
	}

	/**
	 * 从uri 中获取bitmap
	 * 
	 * @param uri
	 * @return
	 */
	private Bitmap getBitmapFromUri(Uri uri) {
		try {
			// 读取uri所在的图片
			Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
			return bitmap1;
		} catch (Exception e) {
			Log.e("[Android]", e.getMessage());
			Log.e("[Android]", "目录为：" + uri);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 裁剪照片
	 * 
	 * @param uri
	 *            照片路径
	 * @param size
	 *            裁剪大小
	 */
	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX,outputY 是剪裁图片的宽高
		// intent.putExtra("outputX", 800);
		// intent.putExtra("outputY", 800);
		intent.putExtra("onFaceDetection", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, 2);
	}

	protected Uri getTempUri() {
		return Uri.fromFile(getTempFile());
	}

	protected File getTempFile() {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				Toast.makeText(this, "SD临时文件读取错误！", Toast.LENGTH_LONG).show();
			}
			return tempFile;
		} else {
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				Toast.makeText(this, "缓存目录临时文件读取错误！", Toast.LENGTH_LONG).show();
			}
			return tempFile;
		}
	}

	/**
	 * 多线程压缩图片的质量
	 * 
	 * @author JPH
	 * @param bitmap
	 *            内存中的图片
	 * @param imgPath
	 *            图片的保存路径
	 * @date 2014-12-5下午11:30:43
	 */
	public void compressImageByQuality(final Bitmap bitmap, final String imgPath) {
		new Thread(new Runnable() {// 开启多线程进行压缩处理
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int options = 100;
				bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 质量压缩方法，把压缩后的数据存放到baos中
																			// (100表示不压缩，0表示压缩到最小)
				while (baos.toByteArray().length / 1024 > 10) {// 循环判断如果压缩后图片是否大于100kb,大于继续压缩
					baos.reset();// 重置baos即让下一次的写入覆盖之前的内容
					options -= 2;// 图片质量每次减少10
					if (options < 0)
						options = 0;// 如果图片质量小于10，则将图片的质量压缩到最小值
					bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 将压缩后的图片保存到baos中
					if (options == 0)
						break;// 如果图片的质量已降到最低则，不再进行压缩
				}
				try {
					FileOutputStream fos = new FileOutputStream(new File(imgPath));// 将压缩后的图片保存的本地上指定路径中
					fos.write(baos.toByteArray());
					fos.flush();
					fos.close();
					file = tempFile;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 按比例缩小图片的像素以达到压缩的目的
	 * 
	 * @author JPH
	 * @param imgPath
	 * @date 2014-12-5下午11:30:59
	 */
	public void compressImageByPixel(String imgPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
		newOpts.inJustDecodeBounds = false;
		int width = newOpts.outWidth;
		int height = newOpts.outHeight;
		float maxSize = 200f;// 默认1000px
		int be = 1;
		// if (width > height && width > maxSize) {//缩放比,用高或者宽其中较大的一个数据进行计算
		// be = (int) (newOpts.outWidth / maxSize);
		// } else if (width < height && height > maxSize) {
		// be = (int) (newOpts.outHeight / maxSize);
		// }
		if (width > maxSize) {
			be = (int) (width / maxSize);
		}
		newOpts.inSampleSize = be;// 设置采样率
		newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
		bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
		compressImageByQuality(bitmap, imgPath);// 压缩好比例大小后再进行质量压缩
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
