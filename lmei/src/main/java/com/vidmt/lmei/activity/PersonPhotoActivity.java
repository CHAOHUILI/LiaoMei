package com.vidmt.lmei.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.R.menu;
import com.vidmt.lmei.adapter.My_photoAdapter;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.entity.Image;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.rule.Base64Coder;
import com.vidmt.lmei.util.rule.Bimp;
import com.vidmt.lmei.util.think.JsonUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * 我的图片浏览页，gridview
 */
public class PersonPhotoActivity extends BaseActivity {

	@TAInjectView(id = R.id.linheader)
	LinearLayout linheader;
	@TAInjectView(id = R.id.headerthemeleft)
	RelativeLayout headerthemeleft;
	@TAInjectView(id = R.id.user)
	ImageView user;
	@TAInjectView(id = R.id.headerright)
	LinearLayout headerright;
	@TAInjectView(id = R.id.typelog)
	ImageView typelog;
	@TAInjectView(id = R.id.headconfrim)
	TextView headconfrim;
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
	
	@TAInjectView(id = R.id.userGridView)
	GridView userGridView;
	@TAInjectView(id = R.id.other)
	View other;
	List<Image> list;
	List<String> imguri;//上传时照片的集合
	Bitmap bitmap;
	Bitmap bitmap1;//转换时需要用到
	Uri path=null;
	My_photoAdapter myadapter;
	private Vibrator mVibrator;// 震动器
	int delnum;
	String album;//要删除的照片
	View del;
	String ps;
	String bit64;//要上传的
	int j = 0;//循环用到
	boolean Y;//是否有需要上传的
	Bitmap photo;//裁剪后的照片
	int pdphoto=0;
	private boolean clickAble=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_photo);
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
		Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
		user.setBackgroundDrawable(drawable);
		typelog.setVisibility(View.GONE);
		headconfrim.setText("保存");	
		headconfrim.setVisibility(View.VISIBLE);
		headercontent.setText("我的相册");	
		mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		list = new ArrayList<Image>();
		if (b_person.getAlbum() != null) {
			String[] photos = b_person.getAlbum().split("_");
			for (int i = 0; i < photos.length; i++) {
				Image image = new Image();
				image.setPath(photos[i]);
				image.setStatus(2);
				list.add(image);
			}
		}
		Image img = new Image();
		img.setPath("up");
		img.setStatus(1);
		list.add(img);
		myadapter = new My_photoAdapter(context, list, imageLoader, options);
		userGridView.setAdapter(myadapter);
		userGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				myadapter.delswitch = false;// 隐藏删除按钮
				myadapter.notifyDataSetChanged();
				if (position != list.size() - 1) {
					Intent in = new Intent(PersonPhotoActivity.this, AmplificationActivity.class);
					in.putExtra("index", position);
					startActivity(in);
					AmplificationActivity.goodsimglists = list;
				} else {
					pdphoto=1;
					new region_PopupWindows(context, userGridView);
				}
			}
		});
		final OnClickListener delclick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.GONE);
				myadapter.delswitch = false;// 隐藏删除按钮
				if (list.get(delnum).getStatus() == 2) {
					album = list.get(delnum).getPath();
					delAlbum();
				} else if (list.get(delnum).getStatus() == 1) {
					list.remove(delnum);
					myadapter.notifyDataSetChanged();
					ToastShow("删除成功");
				}

			}
		};
		userGridView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				myadapter.delswitch = true;
				mVibrator.vibrate(50);
				if (position != list.size() - 1) {
					delnum = position;
					del = view.findViewById(R.id.del);
					del.setVisibility(view.VISIBLE);
					del.setOnClickListener(delclick);
					return true;
				}
				return true;
			}
		});
	}
	
	public void getImgUri(List<Image> listimg) {
		for (int i = 0; i < listimg.size(); i++) {
			if (listimg.get(i).getStatus() == 1 && listimg.get(i).getPath() != "up") {
				listimg.get(i).setBit64(upload(listimg.get(i).getBitmap()));
				Y = true;
			}
		}

	}

	public static String upload(Bitmap bit) {
		Bitmap bitmap = null;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		// Image Image = new Image();
		bitmap = Bimp.comp(bit);
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
		byte[] b = stream.toByteArray();
		String photobit64 = new String(Base64Coder.encodeLines(b));
		return photobit64;
	}

	@Override
	protected void onAfterSetContentView() {
		super.onAfterSetContentView();
		OnClickListener onclic = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.headerthemeleft:
					if(pdphoto==1){
						new ExitShowView(PersonPhotoActivity.this, userGridView);
					}else{
						finish();	

					}
			
					break;
				case R.id.other:
					myadapter.delswitch = false;// 隐藏删除按钮
					myadapter.notifyDataSetChanged();
					break;
				case R.id.headerright:
					if(clickAble){
						clickAble=false;
						loadingDialog.show();
						pdphoto=0;
						// 遍历list 找到要上传的照片并转化成base64
						getImgUri(list);
						// Y =ture 说明有需要上传的照片
						if (Y) {
							// 循环从list中上传需要上传的照片
							upimg();
						}else {
							loadingDialog.dismiss();
							ToastShow("照片已上传...");
							clickAble=true;
						}
					}else {
						ToastShow("正在上传...");
					}
					break;

				}
			}
		};
		headerthemeleft.setOnClickListener(onclic);
		other.setOnClickListener(onclic);
		headerright.setOnClickListener(onclic);
	}
	
	public class ExitShowView extends PopupWindow {

		public ExitShowView(Context mContext, View parent) {

			View view = View.inflate(mContext, R.layout.exitshow_loading2, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));
			// LinearLayout ll_popup = (LinearLayout) view
			// .findViewById(R.id.ll_popup);
			// ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
			// R.anim.push_bottom_in_1));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			// 实例化一个ColorDrawable颜色为半透明
			ColorDrawable dw = new ColorDrawable(0xb0000000);
			// 设置SelectPicPopupWindow弹出窗体的背景
			this.setBackgroundDrawable(dw);
			setContentView(view);
			showAtLocation(parent, Gravity.CENTER, 0, 0);
			TextView attentionts = (TextView) view.findViewById(R.id.attentionts);
			TextView btn_dialog_confirm = (TextView) view.findViewById(R.id.btn_dialog_confirm);
			TextView btn_dialog_cancel = (TextView) view.findViewById(R.id.btn_dialog_cancel);
			btn_dialog_confirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
					pdphoto=0;
					// 遍历list 找到要上传的照片并转化成base64
					getImgUri(list);
					// Y =ture 说明有需要上传的照片
					if (Y) {
						// 循环从list中上传需要上传的照片
						upimg();
					} 					
				}
			});
			btn_dialog_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
					pdphoto = 0;
					finish();	

				}
			});
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

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	public void upimg() {
		for (; j < list.size() - 1; j++) {
			if (list.get(j).getStatus() == 1) {
				editDate(list.get(j).getBit64());
				break;
			}
		}
	}

	protected void editDate(final String json) {
		Persion p = new Persion();
		p.setId(b_person.getId());
		p.setAlbum(json);
		ps = JsonUtil.ObjToJson(p);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String jhuser = Person_Service.editDate(ps);
				Message msg = mUIHandler.obtainMessage(1);
				msg.obj = jhuser;
				msg.sendToTarget();
			}
		}).start();
	}

	protected void delAlbum() {
		loadingDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = Person_Service.delAlbum(album, b_person.getId());
				Message msg = mUIHandler.obtainMessage(2);
				msg.obj = result;
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
					if (mes.equals("")) {
						ToastShow("上传失败请重试");
						clickAble=true;
						loadingDialog.dismiss();
					} else {
						Persion p = JsonUtil.JsonToObj(mes, Persion.class);
						dbutil.updateData(p, "id=" + b_person.getId());
						b_person = p;
						list.get(j).setStatus(2);
						j++;
						if (j < list.size() - 1) {
							upimg();
						} else {
							Y = false;
							list.clear();
							String[] photos = b_person.getAlbum().split("_");
							for (int i = 0; i < photos.length; i++) {
								Image image = new Image();
								image.setPath(photos[i]);
								image.setStatus(2);
								list.add(image);
							}
							Image img = new Image();
							img.setPath("up");
							img.setStatus(1);
							list.add(img);
							myadapter.notifyDataSetChanged();
							ToastShow("上传成功");
							clickAble=true;
							Intent intents = new Intent("personupdate");
							sendBroadcast(intents);
							finish();
							loadingDialog.dismiss();						
						}
					}
				}
				break;
			case 2:
				if (msg.obj != null) {
					String mes = (String) msg.obj;
					if ("".equals(mes)||mes.equals(Constant.ERROR)) {
						ToastShow("删除失败，请重试");
					} else{
						Persion p = JsonUtil.JsonToObj(mes, Persion.class);
						dbutil.updateData(p, "id=" + b_person.getId());
						b_person = p;
						ToastShow("删除成功");
						list.remove(delnum);
						myadapter.notifyDataSetChanged();
					}
				}
				loadingDialog.dismiss();
				break;
			}
		}
	};

	public class region_PopupWindows extends PopupWindow {
		EditText editext = null;

		@SuppressLint("NewApi")
		public region_PopupWindows(Context mContext, View parent) {
			View view = View.inflate(mContext, R.layout.photograph, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
			LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in_1));
			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(false);
			// 实例化一个ColorDrawable颜色为半透明
			ColorDrawable dw = new ColorDrawable(0xb0000000);
			// 设置SelectPicPopupWindow弹出窗体的背景
			this.setBackgroundDrawable(dw);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			TextView photo = (TextView) view.findViewById(R.id.photo);
			TextView album = (TextView) view.findViewById(R.id.album);
			TextView cancel = (TextView) view.findViewById(R.id.cancel);

			photo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
					startActivityForResult(intent, 1);
					dismiss();
				}
			});
			album.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_PICK);
					intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
					// intent.setType("image/*");
					intent.putExtra("return-data", true);
					startActivityForResult(intent, 2);
					dismiss();
				}
			});
			cancel.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	/**
	 * 裁剪照片
	 * 
	 * @param uri
	 *            照片路径
	 * @paramsize
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
		//intent.putExtra("outputX", 800);
		//intent.putExtra("outputY", 800);
		intent.putExtra("onFaceDetection", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, 3);
	}

	// 创建一个以当前时间为名称的文件
	File tempFile = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());

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
					path = data.getData();
					startPhotoZoom(path);
				}
				break;
			case 3:
				if (data != null) {
					setPicToView(data);
				}
				break;
			default:
				break;
			}
			myadapter.notifyDataSetChanged();
		}
	}

	/**
	 * 将进行剪裁后的图片显示到UI界面上
	 * 
	 * @param picdata
	 */

	private void setPicToView(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		Image image = new Image();
//		if (bundle != null) {
			photo = getBitmapFromUri(getTempUri());
			image.setBitmap(photo);
			image.setStatus(1);
//		}
		list.remove(list.size() - 1);
		list.add(image);
		Image img = new Image();
		img.setPath("up");
		img.setStatus(1);
		list.add(img);
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
	 * 从uri 中获取bitmap
	 * 
	 * @param uri
	 * @return
	 */
	private Bitmap getBitmapFromUri(Uri uri) {

		try {
			// 读取uri所在的图片
			bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
			return bitmap1;
		} catch (Exception e) {
			Log.e("[Android]", e.getMessage());
			Log.e("[Android]", "目录为：" + uri);
			e.printStackTrace();
			return null;
		}
	}


	// 使用系统当前日期加以调整作为照片的名称
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.person_photo, menu);
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
