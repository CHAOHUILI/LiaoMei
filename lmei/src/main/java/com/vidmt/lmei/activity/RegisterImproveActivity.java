package com.vidmt.lmei.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.Application;
import com.vidmt.lmei.R;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.rule.Base64Coder;
import com.vidmt.lmei.util.rule.Bimp;
import com.vidmt.lmei.util.rule.FileUtils;
import com.vidmt.lmei.util.rule.ManageDataBase;
import com.vidmt.lmei.util.rule.OnWheelScrollListener;
import com.vidmt.lmei.util.rule.SharedPreferencesUtil;
import com.vidmt.lmei.util.rule.WheelView;
import com.vidmt.lmei.util.think.JsonUtil;
import com.vidmt.lmei.widget.NumericWheelAdapter;

import android.app.ActionBar.LayoutParams;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

public class RegisterImproveActivity extends BaseActivity {
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
	
	TextView next;
	@TAInjectView(id=R.id.birthday)
	TextView birthday_text;
	@TAInjectView(id=R.id.img)
	ImageView img;
	@TAInjectView(id=R.id.usersex)
	TextView usersex;
	@TAInjectView(id=R.id.usernames)
	EditText usernames;
	@TAInjectView(id = R.id.birthdaylin)
	LinearLayout birthdaylin;
	@TAInjectView(id = R.id.usersexlin)
	LinearLayout usersexlin;
	@TAInjectView(id = R.id.ll)
	LinearLayout ll;
	
	int gender=0;//性别
	String username="";//昵称
	String birthday1="";//生日
	String photo="";//头像
	private int mYear=1999;
	private int mMonth=0;
	private int mDay=1;
	private WheelView year;
	private WheelView month;
	private WheelView day;
	String uid;
	String phone;
	String pwd;
	int ver;
	String channel;
	private String path;// 图片地址
	private String flieName = "";
	private boolean isTrue = true;
	private Uri uri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_improve);
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
	headercontent.setText("添加资料");
	headconfrim.setText("确定");
	headconfrim.setVisibility(View.VISIBLE);
	typelog.setVisibility(View.GONE);
	Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
	user.setBackgroundDrawable(drawable);
	phone=getIntent().getStringExtra("phone");
	pwd=getIntent().getStringExtra("regpwd");
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
					StartActivity(RegisterActivity.class);
					break;
				case R.id.usersexlin:
					closejp();
					new PersonSex(context, ll);
					break;
				case R.id.birthdaylin:
					closejp();
					new BirthdayPopuWindow(context, ll);
					break;
				case R.id.img:
					closejp();
					new PhotoView(RegisterImproveActivity.this, ll, 1);
					break;
				case R.id.headerright:
					username=usernames.getText().toString();
					if(photo==null||photo.equals("")){
						ToastShow("请选择头像");
					}else if(username.equals("")){
						ToastShow("请填写昵称");
					}else if(birthday1.equals("")){
						ToastShow("请选择生日");
					}else if(gender==0){
						ToastShow("请选择性别");
					}else{
						loadingDialog.show();
						SharedPreferencesUtil.putString(getApplicationContext(),"passWord",pwd);

						LoadData();
					}
					break;
				default:
					break;
				}
			}
		};
		headerthemeleft.setOnClickListener(onClickListener);
		usersexlin.setOnClickListener(onClickListener);
		birthdaylin.setOnClickListener(onClickListener);
		img.setOnClickListener(onClickListener);
		headerright.setOnClickListener(onClickListener);
	}
    public void closejp(){//关闭键盘
    	View view = getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(
					Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
    }
    @Override
	public void LoadData() {


		// TODO Auto-generated method stub
		super.LoadData();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ver = com.vidmt.lmei.Application.getInstance().getPackageManager().getPackageInfo(com.vidmt.lmei.Application.getInstance().getPackageName(),
							PackageManager.GET_CONFIGURATIONS).versionCode;
					channel = com.vidmt.lmei.Application.getInstance().getPackageManager().getApplicationInfo(com.vidmt.lmei.Application.getInstance().getPackageName(),
							PackageManager.GET_META_DATA).metaData.getString("UMENG_CHANNEL");
				} catch (PackageManager.NameNotFoundException e) {
					e.printStackTrace();
				}
				//String jhuser = Person_Service.insert(phone,pwd,birthday1,photo,gender,username);
				Message msg = mUIHandler.obtainMessage(1);
				msg.obj = Person_Service.insert(phone,pwd,birthday1,photo,gender,username,"a",ver+"",channel, Build.MODEL);
				msg.sendToTarget();
			}
		}).start();
	}
    Persion p_perPersion;
    private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (msg.obj != null) {
					String mes = (String) msg.obj;
					if (mes.equals(JsonUtil.ObjToJson("error"))||mes.equals("")||mes.equals(JsonUtil.ObjToJson(""))||mes.equals(JsonUtil.ObjToJson("reg_error"))||mes.equals(JsonUtil.ObjToJson("param_error"))) {
						ToastShow("注册失败请重试");
                        loadingDialog.dismiss();

                    }else {
						loadingDialog.dismiss();
						p_perPersion = JsonUtil.JsonToObj(mes, Persion.class);
	             		inityunong(p_perPersion);					
//						Intent intent = new Intent(RegisterImproveActivity.this, MainActivity.class);
//						startActivity(intent);
//						finish();
					}
				}else{
					ToastShow("注册失败请重试");
                    loadingDialog.dismiss();

                }
				break;

			default:
				break;
			}
		}
	};
	
	
	
	
	/**
	 * 
	 * 融云连接
	 * 
	 **/
	private void inityunong( final Persion  persion) {
		if (RongIM.getInstance().getRongIMClient()!=null) {
			String	as;
		}

		if (getApplicationInfo().packageName.equals(Application.getApplication().getPackageName())) {

			/**
			 * IMKit SDK调用第二步,建立与服务器的连接
			 */
			RongIM.connect(persion.getRongyuntoken(), new RongIMClient.ConnectCallback() {

				/**
				 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
				 */
				@Override
				public void onTokenIncorrect() {

					Log.d("LoginActivity", "--onTokenIncorrect");
					Toast.makeText(RegisterImproveActivity.this, "连接融云失败", Toast.LENGTH_SHORT).show();
					//	                StartActivity(HomePageActivity.class);
					//	                
					//					finish();
				}

				/**
				 * 连接融云成功
				 * @param userid 当前 token
				 */
				@Override
				public void onSuccess(String userid) {

					Log.d("LoginActivity", "--onSuccess" + userid);
				
					/**
					 * 刷新用户缓存数据。
					 *
					 * @param userInfo 需要更新的用户缓存数据。
					 */ 
				
					SharedPreferencesUtil.putInt(RegisterImproveActivity.this, "editor", 0);
					ManageDataBase.Delete(dbutil, Persion.class, null);
					ManageDataBase.Insert(dbutil, Persion.class, p_perPersion);
					initJpush(p_perPersion);
					RongIM.getInstance().refreshUserInfoCache(new UserInfo(persion.getId()+"",persion.getNick_name(), Uri.parse(persion.getPhoto())));
					RongIM.getInstance().setMessageAttachedUserInfo(true);
					SharedPreferencesUtil.putString(getApplicationContext(),"phoneNumber",phone);
					StartActivity(MainActivity.class);
					finish();
				}
				/**
				 * 连接融云失败
				 * @param errorCode 错误码，可到官网 查看错误码对应的注释
				 */
				@Override
				public void onError(RongIMClient.ErrorCode errorCode) {

					Log.d("LoginActivity", "--onError" + errorCode);
					Toast.makeText(RegisterImproveActivity.this, "连接融云失败", Toast.LENGTH_SHORT).show();
					//	                StartActivity(HomePageActivity.class);
					//					finish();
				}
			});
		}
	}	
	//设置别名用来发消息
		private  void initJpush(Persion p)
		{
			//设置消息栏样式
			BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(getApplicationContext());
			builder.statusBarDrawable = R.drawable.ic_launcher;
			builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
			builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）  
			JPushInterface.setPushNotificationBuilder(1, builder);
			//设置接受信息的数量
			JPushInterface.setLatestNotificationNumber(getApplicationContext(),3);
			//设置别名，用户id为别名
			Set<String> set = new HashSet<String>();
			set.add("all"); // 这指定all 是给所有安装爱狗之家发信息             set是TAG
			String phone =  p.getId().toString();    //别名  指定给某个用户发
			JPushInterface.setAliasAndTags(getApplicationContext(), phone, set, mAliasCallback);
		}

		private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

			@Override
			public void gotResult(int code, String alias, Set<String> tags) {
				String logs ;
				switch (code) {
				case 0:
					logs = "Set tag and alias success";
					Log.i("jpush success", logs);
					break;     
				case 6002:
					logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
					Log.i("jpush Failed", logs);
					break;
				default:
					logs = "Failed with errorCode = " + code;
					Log.i("jpush Failed", logs);
				}
			}

		};
	public class PhotoView extends PopupWindow {

		public PhotoView(Context mContext, View parent, final int type) {

			View view = View.inflate(mContext, R.layout.toast_keynote, null);
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
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);

			TextView bt1 = (TextView) view.findViewById(R.id.item_popupwindows_camera);
			TextView bt2 = (TextView) view.findViewById(R.id.item_popupwindows_Photo);
			TextView bt3 = (TextView) view.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File file = FileUtils.getTackPicFilePath();

					if (file != null) {
						uri = Uri.fromFile(file);
						if (uri != null) {
							intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
						}
						path = file.getAbsolutePath();
					}
					intent.putExtra("return-data", true);
					if (type == 1) {
						startActivityForResult(intent, 11);
					} 
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent2 = new Intent();
					intent2.setAction(Intent.ACTION_PICK);
					intent2.setType("image/*");
					intent2.putExtra("return-data", true);
					// 以需要返回值的模式开启一个Activity
					if (type == 1) {
						startActivityForResult(intent2, 12);
					} 
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					isTrue = true;
					dismiss();
				}
			});

		}
	}
	Uri tempFile;
	/**  
     * 裁剪图片方法实现  
     * @param uri  
     */ 
    public void startPhotoZoom(Uri uri,int cjtype) {  
        /*  
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页  
         * yourself_sdk_path/docs/reference/android/content/Intent.html  
         * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能,  
         * 是直接调本地库的，小马不懂C C++  这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么  
         * 制做的了...吼吼  
         */ 
    	
    	Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 9);
		intent.putExtra("aspectY", 9);
		intent.putExtra("scale",true);
		intent.putExtra("scaleUpIfNeeded", true);
		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", 360);
		intent.putExtra("outputY", 360);
		intent.putExtra("onFaceDetection", true);
		// intent.putExtra("return-data", true);
		tempFile = Uri
				.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + cjtype + ".jpg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, tempFile);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, cjtype);
    }  
   
    /**
	 * 保存裁剪之后的图片数据
	 * 
	 */
	private void setPicToView(Bitmap bitmaps, int cjtype) {

		if (bitmaps != null) {
			Bitmap bitmap = bitmaps;
			if (cjtype == 13) {
				bitmap = Bimp.comp(bitmap);
				//releaseimg1.setBackgroundDrawable(new BitmapDrawable(bitmap));
				Bitmap compression = Bimp.comp(bitmap);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				compression.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] by = baos.toByteArray();
				String str = new String(Base64Coder.encode(by)); // 把图片转换成string
				photo=str;
				imageLoader.displayImage(uri.toString(), img, options);
			} 
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		isTrue = true;
		String imgPath = "";
		/*
		 * if (resultCode != RESULT_OK) { return; }
		 */
		if (requestCode == 13 && data != null) {

			Bitmap bitmap;
			try {
				bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(tempFile));
				setPicToView(bitmap, 13);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				ToastShow("图片裁剪错误");
			}
		}

		

		

		if (requestCode == 11) {
			if (data != null) {
				imgPath = FileUtils.resolvePhotoFromIntent(RegisterImproveActivity.this, data, path);
				if (imgPath != null) {
					uri = Uri.parse(imgPath);
					startPhotoZoom(uri, 13);
				}
			} else {
				uri = Uri.parse("file://" + path);
				startPhotoZoom(uri, 13);
			}

		} else if (requestCode == 12) {
			if (data != null) {
				uri = data.getData();
				startPhotoZoom(uri, 13);
			}

		}

		

	}
    public class PersonSex extends PopupWindow {

		public PersonSex(Context mContext, View parent) {

			View view = View.inflate(mContext, R.layout.toast_sex, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));

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
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);

			TextView bt1 = (TextView) view.findViewById(R.id.toast_nan);
			TextView bt2 = (TextView) view.findViewById(R.id.toast_nv);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					usersex.setText("男");
					gender = 1;
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					gender = 2;
					usersex.setText("女");
					dismiss();
				}
			});
		}
	}
    
    public class BirthdayPopuWindow extends PopupWindow {
		Calendar c = Calendar.getInstance();
		int norYear = c.get(Calendar.YEAR);
		int curYear = mYear;
		int curMonth = mMonth + 1;
		int curDate = mDay;

		public BirthdayPopuWindow(Context mContext, View parent) {
			View view = View.inflate(mContext, R.layout.wheel_date_picker, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
			LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in_1));
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
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			TextView confrim = (TextView) view.findViewById(R.id.confrim);
			TextView cencel = (TextView) view.findViewById(R.id.cencel);
			getDataPick(view);
			cencel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dismiss();
				}
			});
			confrim.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					int n_year = year.getCurrentItem() + 1950;// 年
					int n_month = month.getCurrentItem() + 1;// 月
					initDay(n_year, n_month);
					birthday1 = new StringBuilder().append((year.getCurrentItem() + 1950)).append("-")
							.append((month.getCurrentItem() + 1) < 10 ? "0" + (month.getCurrentItem() + 1)
									: (month.getCurrentItem() + 1))
							.append("-").append(((day.getCurrentItem() + 1) < 10) ? "0" + (day.getCurrentItem() + 1)
									: (day.getCurrentItem() + 1))
							.toString();
					// Toast.makeText(UpdatePersionActivity.this, birthday1,
					// Toast.LENGTH_LONG).show();
					birthday_text.setText(birthday1 + "");
					dismiss();
				}
			});
		}

		private View getDataPick(View view) {
			Calendar c = Calendar.getInstance();
			int norYear = c.get(Calendar.YEAR);
			int curYear = mYear;
			int curMonth = mMonth + 1;
			int curDate = mDay;
			year = (WheelView) view.findViewById(R.id.year);
			NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(RegisterImproveActivity.this, 1950, norYear);
			numericWheelAdapter1.setLabel("年");
			year.setViewAdapter(numericWheelAdapter1);
			year.setCyclic(true);// 是否可循环滑动
			year.addScrollingListener(scrollListener);
			month = (WheelView) view.findViewById(R.id.month);
			NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(RegisterImproveActivity.this, 1, 12, "%02d");
			numericWheelAdapter2.setLabel("月");
			month.setViewAdapter(numericWheelAdapter2);
			month.setCyclic(true);
			month.addScrollingListener(scrollListener);
			day = (WheelView) view.findViewById(R.id.day);
			initDay(curYear, curMonth);
			day.setCyclic(true);

			year.setVisibleItems(7);// 设置显示行数
			month.setVisibleItems(7);
			day.setVisibleItems(7);

			year.setCurrentItem(curYear - 1950);
			month.setCurrentItem(curMonth - 1);
			day.setCurrentItem(curDate - 1);

			return view;
		}

		OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				initDay(year.getCurrentItem() + 1950, month.getCurrentItem() + 1);
				// initDay(curYear, curMonth);
			}
		};

		/**
		 * 
		 * @param year
		 * @param month
		 * @return
		 **/
		private int getDay(int year, int month) {
			int day = 30;
			boolean flag = false;
			switch (year % 4) {
			case 0:
				flag = true;
				break;
			default:
				flag = false;
				break;
			}
			switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				day = 31;
				break;
			case 2:
				day = flag ? 29 : 28;
				break;
			default:
				day = 30;
				break;
			}
			return day;
		}

		private void initDay(int arg1, int arg2) {
			NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(RegisterImproveActivity.this, 1,
					getDay(arg1, arg2), "%02d");
			numericWheelAdapter.setLabel("日");
			day.setViewAdapter(numericWheelAdapter);
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_improve, menu);
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
