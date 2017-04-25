package com.vidmt.lmei.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.Application;
import com.vidmt.lmei.R;
import com.vidmt.lmei.adapter.AppStratAdapter;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.DownLoadManager;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.dialog.ConnectionUtil;
import com.vidmt.lmei.dialog.UpdateVersionDialog;
import com.vidmt.lmei.dialog.UpdateVersionMustDialog;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.entity.Version;
import com.vidmt.lmei.util.rule.ManageDataBase;
import com.vidmt.lmei.util.rule.SharedPreferencesUtil;
import com.vidmt.lmei.util.think.JsonUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * 首次登录介绍页
 */
public class SplashActivity extends BaseActivity implements OnPageChangeListener {
	@TAInjectView(id = R.id.main_line)
	LinearLayout line;
	private ImageView[] point;// 底部小圆点
	private int currentId = 0;// 当前ID
	private ViewPager pager;
	private List<View> views;//
	private int[] imageVeiwResourceId = { R.drawable.main_img1, R.drawable.main_img2, R.drawable.main_img3 };
	private AppStratAdapter adapter;
	public static SplashActivity mainactivity;
	private int type = 0;
	Version version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainactivity = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 设置全屏
		type = SharedPreferencesUtil.getInt(SplashActivity.this, "dog", 0);
		if (ConnectionUtil.isConn(activity) == false) {
			// ConnectionUtil.setNetworkMethod(activity);
			Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
			// toast(8);
		}

		if (type == 0) {
			SharedPreferencesUtil.putInt(SplashActivity.this, "dog", 1);//首次登录
			InitView();
			setPoint();
		} else {
			View view = View.inflate(this, R.layout.last_guide, null);
			setContentView(view);
			RelativeLayout rela = (RelativeLayout) view.findViewById(R.id.last_background);
			rela.setBackgroundResource(R.drawable.main_img4);
			Button submit = (Button) view.findViewById(R.id.guide_start_app);
			submit.setVisibility(View.GONE);
			// 渐变展示启动屏
			AlphaAnimation aa = new AlphaAnimation(0.0f, 0.0f);
			aa.setDuration(3000);
			view.startAnimation(aa);
			aa.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

					// TODO Auto-generated method stub


				    //toast(1);

				

					checkVersionUpdate();
					/*
					 * if(b_person!=null){ //ToastShow("connect");
					 * //ToastShow("--onTokenIncorrect");
					 * connect(b_person.getRongyuntoken(), b_person);
					 * //StartActivity(MainActivity.class); }else{
					 * StartActivity(LoginActivity.class); }
					 */

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub

				}
			});
		}

	}

	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.activity_main, null);
		setContentView(view);
		pager = (ViewPager) this.findViewById(R.id.main_viewpager);

		views = new ArrayList<View>();

		for (int i = 0; i < imageVeiwResourceId.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(imageVeiwResourceId[i]);
			imageView.setScaleType(ScaleType.FIT_XY);
			views.add(imageView);
		}
		adapter = new AppStratAdapter(views, SplashActivity.this);
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(this);

	}

	/**
	 * 设置小圆点
	 * 
	 */
	private void setPoint() {
		// LinearLayout ll = (LinearLayout)
		// this.findViewById(R.id.viewpager_ll);
		point = new ImageView[line.getChildCount()];
		for (int i = 0; i < line.getChildCount(); i++) {
			if (currentId == i) {
				point[i] = (ImageView) line.getChildAt(i);
				point[i].setImageResource(R.drawable.point_focus);
			} else {
				point[i] = (ImageView) line.getChildAt(i);
				point[i].setImageResource(R.drawable.point_normal);
			}
		}

	}

	public void checkVersionUpdate() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String info;
				try {

					info = Person_Service.VersionUpdate();
				} catch (Exception e) {
					info=null;
				}
				Message msg = muHandler.obtainMessage(1);
				msg.obj = info;
				msg.sendToTarget();
			}
		}).start();
	}

	private Handler muHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Intent intent = null;
			switch (msg.what) {
			case 1:
				if (msg.obj != null) {

				   // toast(11);
					String json = (String) msg.obj;
					if (!json.equals("") && !json.equals(JsonUtil.ObjToJson(Constant.FAIL))) {
						
						version = JsonUtil.JsonToObj(json, Version.class);
						Double apk_version = Double.valueOf(version.getTitle());
						// toast(version.getTitle());
						Double android_apk = Double.valueOf(getVersionName());
						if (apk_version.equals(android_apk)) {
							List<Persion> list = ManageDataBase.SelectList(dbutil, Persion.class, null);
							if (list.size() > 0) {
								b_person = list.get(0);
								if (b_person != null) {
									if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {
					
										connect(b_person.getRongyuntoken(), b_person);
									}else {
										StartActivity2(LoginActivity.class);
									}




									// StartActivity(MainActivity.class);
								} else {
									StartActivity(LoginActivity.class);
								}
							}else {
								StartActivity(LoginActivity.class);
							}
						} else if (android_apk < apk_version) {
							if (version.getMust() != null && version.getMust() == 1) {
								UpdateVersionMustDialog _UpdateVersionDialog = UpdateVersionMustDialog
										.show(SplashActivity.this, R.style.mobile_dialog_full_window_dialog, "", 1);
								_UpdateVersionDialog.show();
							} else {
								UpdateVersionDialog _UpdateVersionDialog = UpdateVersionDialog.show(SplashActivity.this,
										R.style.mobile_dialog_full_window_dialog, "", 1);
								_UpdateVersionDialog.show();
							}
						} else {
							List<Persion> list = ManageDataBase.SelectList(dbutil, Persion.class, null);
							if (list.size() > 0) {
								b_person = list.get(0);
								if (b_person != null) {
									// toast(3);


									if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {
					
										connect(b_person.getRongyuntoken(), b_person);
									}else {
						
										StartActivity2(LoginActivity.class);

									}



									// StartActivity(MainActivity.class);
								} else {
									StartActivity(LoginActivity.class);
								}
							}else {
								StartActivity(LoginActivity.class);
							}
						}
					} else {
						//toast(112);
						List<Persion> list = ManageDataBase.SelectList(dbutil, Persion.class, null);
						if (list.size() > 0) {
							b_person = list.get(0);
							if (b_person != null) {
							



								if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {
									
									connect(b_person.getRongyuntoken(), b_person);
								}else {
									 
									StartActivity2(LoginActivity.class);

								}

							} else {
								
								StartActivity(LoginActivity.class);
							}
						}else {
							
							StartActivity(LoginActivity.class);
						}

					}

				} else {
					//toast(12);
					List<Persion> list = ManageDataBase.SelectList(dbutil, Persion.class, null);
					if (list.size() > 0) {
						b_person = list.get(0);
						if (b_person != null) {
					

							if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {
			
								connect(b_person.getRongyuntoken(), b_person);
							}else {
				
								StartActivity2(LoginActivity.class);

							}

						} else {
							StartActivity(LoginActivity.class);
						}
					}else {
						StartActivity(LoginActivity.class);
					}
				}
				break;
			case 2:
				ToastShow("SD卡不可用");
				if (b_person != null) {
					

					if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {
	
						connect(b_person.getRongyuntoken(), b_person);
					}else {
		
						StartActivity2(LoginActivity.class);

					}
					//connect(b_person.getRongyuntoken(), b_person);
					// StartActivity(MainActivity.class);
				} else {
					StartActivity(LoginActivity.class);
				}
				break;
			case 3:
				ToastShow("下载新版本失败,请重新下载!");
				List<Persion> list = ManageDataBase.SelectList(dbutil, Persion.class, null);
				if (list.size() > 0) {
					b_person = list.get(0);
					if (b_person != null) {
						


						if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {
		
							connect(b_person.getRongyuntoken(), b_person);
						}else {
			
							StartActivity2(LoginActivity.class);

						}


						// StartActivity(MainActivity.class);
					} else {
						StartActivity(LoginActivity.class);
					}
				}else {
					StartActivity(LoginActivity.class);
				}
				break;
			default:
				break;
			}
		}

	};

	/*
	 * 获取当前程序的版本号
	 */
	private String getVersionName() {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return packInfo.versionName;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		currentId = arg0;
		setPoint();
		if (currentId == 2) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					List<Persion> list = ManageDataBase.SelectList(dbutil, Persion.class, null);
					if (list.size() > 0) {
						b_person = list.get(0);
						if (b_person != null) {
							
							connect(b_person.getRongyuntoken(), b_person);
							// StartActivity(MainActivity.class);
						} else {
							StartActivity2(LoginActivity.class);
						}
					}else {
						StartActivity(LoginActivity.class);
					}
				}
			}, 1000);

		}
	}

	/*
	 * 从服务器中下载APK
	 */
	public void downLoadApk() {
		final ProgressDialog pd = new ProgressDialog(SplashActivity.this); // 进度条对话框
		pd.setCancelable(false);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新");
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			pd.dismiss();
			Message msg = muHandler.obtainMessage(2);
			msg.obj = 2;
			msg.sendToTarget();
		} else {
			pd.show();
			new Thread() {
				@Override
				public void run() {
					try {
						File file = DownLoadManager.getFileFromServer(version.getAddress(), pd);
						sleep(1000);
						openFile(file);
						pd.dismiss(); // 结束掉进度条对话框
					} catch (Exception e) {
						pd.dismiss();
						Message msg = muHandler.obtainMessage(3);
						msg.obj = 3;
						msg.sendToTarget();
					}
				}
			}.start();
		}
	}

	private void openFile(File file) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		startActivity(intent);
	}

	/**
	 * 建立与融云服务器的连接
	 *
	 * @param token
	 */
	private void connect(final String token, final Persion persion) {
		// ToastShow(token);
		if (getApplicationInfo().packageName.equals(Application.getCurProcessName(getApplicationContext()))) {

			/**
			 * IMKit SDK调用第二步,建立与服务器的连接
			 */
			RongIM.connect(token, new RongIMClient.ConnectCallback() {

				/**
				 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的
				 * Token
				 */
				@Override
				public void onTokenIncorrect() {
					// ToastShow("--onTokenIncorrect");
					
					Log.d("LoginActivity", "--onTokenIncorrect");
					ToastShow("账号异常");
					StartActivity(LoginActivity.class);
					finish();
					// StartActivity(HomePageActivity.class);
					// finish();
				}

				/**
				 * 连接融云成功
				 * 
				 * @param userid
				 *            当前 token
				 */
				@Override
				public void onSuccess(String userid) {
					// ToastShow("Success");
					
					Log.d("LoginActivity", "--onSuccess" + userid);
					RongIM.getInstance().refreshUserInfoCache(
							new UserInfo(persion.getId() + "", persion.getNick_name(), Uri.parse(persion.getPhoto())));
					RongIM.getInstance().setMessageAttachedUserInfo(true);

					StartActivity2(LoginActivity.class);
					// finish();

				}

				/**
				 * 连接融云失败
				 * 
				 * @param errorCode
				 *            错误码，可到官网 查看错误码对应的注释
				 */
				@Override
				public void onError(RongIMClient.ErrorCode errorCode) {

				
					Log.d("LoginActivity", "--onError" + errorCode);
					// connect(b_person.getRongyuntoken(), b_person);
					StartActivity(LoginActivity.class);
					// finish();
					// StartActivity(HomePageActivity.class);
					// finish();
				}
			});
		} else {
			
			StartActivity(LoginActivity.class);
		}
	}

	public void gobreak() {
		dbutil.dropTable(Persion.class);
		RongIM.getInstance().logout();
		// JPushInterface.stopPush(this);
		StartActivity(LoginActivity.class);
		finish();
	}

	public void AcitvityStar() {
		List<Persion> list = ManageDataBase.SelectList(dbutil, Persion.class, null);
		if (list.size() > 0) {
			b_person = list.get(0);
			if (b_person != null) {
				// toast(3);
				connect(b_person.getRongyuntoken(), b_person);
				// StartActivity(MainActivity.class);
			} else {
				StartActivity(LoginActivity.class);
			}
		}else {
			StartActivity(LoginActivity.class);
		}
	}

	public void AcitvityStar1() {
		exitApp();
	}
}
