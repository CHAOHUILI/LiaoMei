package com.vidmt.lmei.activity;

import java.io.File;
import java.math.BigDecimal;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;

import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.rule.ManageDataBase;

import com.vidmt.lmei.util.rule.SharedPreferencesUtil;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.CSCustomServiceInfo;


/**
 * 用户设置页
 */
public class UserSettingsActivity extends BaseActivity {

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
	@TAInjectView(id = R.id.feedback)
	RelativeLayout feedback;


	@TAInjectView(id = R.id.muterel)
	RelativeLayout muterel;
	@TAInjectView(id = R.id.mute)
	ImageView mute;

	@TAInjectView(id = R.id.amendpswrel)
	RelativeLayout amendpswrel;
	@TAInjectView(id=R.id.qc)
	RelativeLayout qc;
	@TAInjectView(id = R.id.cache)
	TextView cache;

	@TAInjectView(id = R.id.aboutrel)
	RelativeLayout aboutrel;

	@TAInjectView(id = R.id.eixt)
	TextView eixt;

	private int type = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_settings);
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
		headercontent.setText("设置");
		Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
		user.setBackgroundDrawable(drawable);
		type = SharedPreferencesUtil.getInt(UserSettingsActivity.this, "editor", 0);
		if (type==0) {
			mute.setImageResource(R.drawable.switchoff);
		}else {
			mute.setImageResource(R.drawable.switchon);
		}
		try {
			String 	ov = getCacheSize(context.getExternalCacheDir());
			cache.setText(ov);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			cache.setText("0k");
		}
 
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
				case R.id.mute:
					loadingDialog.show();
					if (type==1) {
						RongIM.getInstance().removeNotificationQuietHours(new RongIMClient.OperationCallback() {
							@Override
							public void onSuccess() {
								//								SharedPreferences.Editor editor = getSharedPreferences("config", MODE_PRIVATE).edit();
								//								editor.putBoolean("isDisturb", false);
								//								editor.apply();
								
								setnotrn("2");
								JPushInterface.resumePush(getApplicationContext());
							
								//mute.setImageResource(R.drawable.switchon);
							
							}

							@Override
							public void onError(RongIMClient.ErrorCode errorCode) {

							}
						});
						
					}else {
						


						RongIM.getInstance().setNotificationQuietHours("00:00:00", 1439, new RongIMClient.OperationCallback() {
							@Override
							public void onSuccess() {
								//.Log.e(TAG, "----yb----设置会话通知周期-onSuccess");
								//								SharedPreferences.Editor editor = SharedPreferencesContext.getInstance().getSharedPreferences().edit();
								//								editor.putBoolean("IS_SETTING", true);
								//								editor.apply();
								 setnotrn("1");
								 JPushInterface.stopPush(getApplicationContext());
							
								//mute.setImageResource(R.drawable.switchoff);
										     
							}

							@Override
							public void onError(RongIMClient.ErrorCode errorCode) {
								ToastShow("errorCode");
							}
						});
					
						
					} 




					break;
				case R.id.amendpswrel:
					if(b_person.getThird_login()==null){
						Intent intent=new Intent(UserSettingsActivity.this, ChangePasswordActivity.class);
						intent.putExtra("type", 1);
						startActivity(intent);	
					}else{
						ToastShow("此功能暂不对第三方用户开放");
					}
				
					break;
				case R.id.qc:
					cleanExternalCache(context);
					ToastShow("清理缓存成功");
					cache.setText("0K");
					break;
				case R.id.aboutrel:
					StartActivity(AboutLmeiActivity.class);
					break;

				case R.id.eixt:
					String jhuser = Person_Service.sendGet(b_person.getId());
					RongIM.getInstance().logout();
					ManageDataBase.Delete(dbutil, Persion.class, null);
					ToastShow("退出成功");
					finish();
					//StartActivity(LoginActivity.class);
					Intent intents = new Intent("closeapp");
					sendBroadcast(intents);
					break;

				case R.id.feedback:
					//首先需要构造使用客服者的用户信息
					CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
					CSCustomServiceInfo csInfo = csBuilder.nickName("聊妹").build();

					/**
					 * 启动客户服聊天界面。
					 *
					 * @param context           应用上下文。
					 * @param customerServiceId 要与之聊天的客服 Id。
					 * @param title             聊天的标题，如果传入空值，则默认显示与之聊天的客服名称。
					 * @param customServiceInfo 当前使用客服者的用户信息。{@link io.rong.imlib.model.CSCustomServiceInfo}
					 */
					RongIM.getInstance().startCustomerServiceChat(UserSettingsActivity.this, "KEFU148999086589314", "在线客服",csInfo);
					
					break;


				default:
					break;
				}
			}
		};
		headerthemeleft.setOnClickListener(onClickListener);
		amendpswrel.setOnClickListener(onClickListener);
		qc.setOnClickListener(onClickListener);
		aboutrel.setOnClickListener(onClickListener);

		eixt.setOnClickListener(onClickListener);

		mute.setOnClickListener(onClickListener);
		feedback.setOnClickListener(onClickListener);

	}

	/**
	 * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
	 * 
	 * @param context
	 */
	@SuppressLint("NewApi")
	public void cleanExternalCache(Context context) {


		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			deleteFilesByDirectory(context.getExternalCacheDir());
		}

	}


	public void setnotrn(final String type) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// dialog.show();
				Message msg = mUHandler.obtainMessage(2);
				try {
					msg.obj = type;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					msg.obj = null;
				}
				msg.sendToTarget();
			}
		}).start();
	}

	private Handler mUHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (msg.obj != null) {

				} else {
					ToastShow("网络异常");
				}

				break;
			case 2:
				if (msg.obj != null) {
					String json = (String) msg.obj;
					if (json.contains("2")) {
						mute.setImageResource(R.drawable.switchoff);
						SharedPreferencesUtil.putInt(UserSettingsActivity.this, "editor", 0);
						ToastShow("消息免打扰已关闭");	
						type=0;
					}else if (json.contains("1")) {
						mute.setImageResource(R.drawable.switchon);
						SharedPreferencesUtil.putInt(UserSettingsActivity.this, "editor", 1);
						type=1;
						ToastShow("消息免打扰已开启");
					} else {

					}
				} else {
					ToastShow("网络异常");
				}
				loadingDialog.dismiss();
				break;
			default:
				break;
			}
		}

	};
	/**
	 * * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * *
	 * 
	 * @param directory
	 */
	private static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				item.delete();
			}
		}
	}
	// 获取文件
	//Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
	//Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}
	/**
	 * 格式化单位
	 * 
	 * @param size
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			return size + "B";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
				+ "TB";
	}
	public static String getCacheSize(File file) throws Exception {


		return getFormatSize(getFolderSize(file));
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
