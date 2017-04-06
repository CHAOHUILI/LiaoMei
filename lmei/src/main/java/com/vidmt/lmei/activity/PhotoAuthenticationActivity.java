package com.vidmt.lmei.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.R.menu;
import com.vidmt.lmei.util.rule.Base64Coder;
import com.vidmt.lmei.util.rule.Bimp;
import com.vidmt.lmei.util.rule.FileUtils;
import com.vidmt.lmei.util.rule.ScreenUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

public class PhotoAuthenticationActivity extends BaseActivity {
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

	@TAInjectView(id = R.id.cf) // 认证按钮
	TextView cf;
	int sdk;
	private String path;// 图片地址
	private String flieName = "";
	private boolean isTrue = true;
	private Uri uri;
	Bitmap photo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_authentication);
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
		headercontent.setText("上传认证图片");
		Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
		user.setBackgroundDrawable(drawable);
	}

	// 创建一个以当前时间为名称的文件
	File tempFile = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());

	@Override
	protected void onAfterSetContentView() {
		OnClickListener onclick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.cf:
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra("camerasensortype", -1); // 调用前置摄像头
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
					startActivityForResult(intent, 1);
					break;
				case R.id.headerthemeleft:
					finish();
					break;
				}

			}
		};
		cf.setOnClickListener(onclick);
		headerthemeleft.setOnClickListener(onclick);
	};

	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	private void startPhotoZoom(Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 9);
		intent.putExtra("aspectY", 10);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, 2);
	}

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
				startPhotoZoom(Uri.fromFile(tempFile), 200);
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
			compressImageByPixel(tempFile.getPath());
			PhotoUploadAuthenticationActivity.photo = bundle.getParcelable("data");

		}
	}

	// 压缩图片后跳转页面
	public void setphoto() {
		PhotoUploadAuthenticationActivity.file = tempFile;
		StartActivity(PhotoUploadAuthenticationActivity.class);
		finish();
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
					setphoto();
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
