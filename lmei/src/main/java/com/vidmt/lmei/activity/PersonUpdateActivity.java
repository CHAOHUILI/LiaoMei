package com.vidmt.lmei.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.drawable;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.activity.RegisterImproveActivity.PhotoView;
import com.vidmt.lmei.activity.UserRechargeActivity.RechargeView;
import com.vidmt.lmei.adapter.AdapterHobby;
import com.vidmt.lmei.adapter.AdapterSysHobby;
import com.vidmt.lmei.adapter.AdapterUserPhoto;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.dialog.ConnectionUtil;
import com.vidmt.lmei.dialog.LoadingDialog;
import com.vidmt.lmei.entity.Address;
import com.vidmt.lmei.entity.Hobby;
import com.vidmt.lmei.entity.Image;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.recording.UploadUtil;
import com.vidmt.lmei.util.rule.Base64Coder;
import com.vidmt.lmei.util.rule.Bimp;
import com.vidmt.lmei.util.rule.BitmapBlurUtil;
import com.vidmt.lmei.util.rule.FileUtils;
import com.vidmt.lmei.util.rule.ManageDataBase;
import com.vidmt.lmei.util.rule.OnWheelScrollListener;
import com.vidmt.lmei.util.rule.SDcardTools;
import com.vidmt.lmei.util.rule.ScreenUtils;
import com.vidmt.lmei.util.rule.SharedPreferencesUtil;
import com.vidmt.lmei.util.rule.WheelView;
import com.vidmt.lmei.util.think.JsonUtil;
import com.vidmt.lmei.widget.CircleImageView;
import com.vidmt.lmei.widget.HorizontalListView;
import com.vidmt.lmei.widget.MyGridView;
import com.vidmt.lmei.widget.NumericWheelAdapter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallSession;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView1;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

/**
 * 个人详情页，点击个人信息页头像进入
 */
public class PersonUpdateActivity extends BaseActivity {

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
	@TAInjectView(id = R.id.userdetailico)
	ImageView userdetailico;
	@TAInjectView(id = R.id.rela_error)
	RelativeLayout rela_error;
	@TAInjectView(id = R.id.rela_broken)
	RelativeLayout rela_broken;
	@TAInjectView(id = R.id.scroll_my)
	ScrollView scroll_my;
	@TAInjectView(id = R.id.userdetailsex)
	ImageView userdetailsex;
	@TAInjectView(id = R.id.userdetailage)
	TextView userdetailage;
	@TAInjectView(id = R.id.userdetailaddres)
	TextView userdetailaddres;
	@TAInjectView(id = R.id.userdetailnum)
	TextView userdetailnum;
	@TAInjectView(id = R.id.userdetailcivico)
	CircleImageView userdetailcivico;
	@TAInjectView(id = R.id.userdetailchatvideo)
	TextView userdetailchatvideo;
	@TAInjectView(id = R.id.userdetailchatvoice)
	TextView userdetailchatvoice;
	@TAInjectView(id = R.id.userdetailphotonum)
	TextView userdetailphotonum;
	@TAInjectView(id = R.id.rela_userphotadd)
	RelativeLayout rela_userphotadd;
	@TAInjectView(id = R.id.userdetailhlist)
	HorizontalListView userdetailhlist;
	@TAInjectView(id = R.id.rela_userphoterror)
	RelativeLayout rela_userphoterror;
	@TAInjectView(id = R.id.userdetailsign)
	TextView userdetailsign;
	@TAInjectView(id = R.id.userdetailbirthday)
	TextView userdetailbirthday;
	@TAInjectView(id = R.id.userdetailarea)
	TextView userdetailarea;
	@TAInjectView(id = R.id.userdetaivideoimg)
	ImageView userdetaivideoimg;
	@TAInjectView(id = R.id.rela_uservideoshow)
	RelativeLayout rela_uservideoshow;
	@TAInjectView(id = R.id.rela_uservideoerror)
	RelativeLayout rela_uservideoerror;
	int userid = 0;
	@TAInjectView(id = R.id.userdetailvideonum)
	TextView userdetailvideonum;
	@TAInjectView(id = R.id.rela_uservoicecontent)
	RelativeLayout rela_uservoicecontent;
	@TAInjectView(id = R.id.rela_uservoiceerror)
	RelativeLayout rela_uservoiceerror;
	@TAInjectView(id = R.id.rela_uservoiceadd)
	RelativeLayout rela_uservoiceadd;
	@TAInjectView(id = R.id.rela_userchargeadd)
	RelativeLayout rela_userchargeadd;
	@TAInjectView(id = R.id.userdetailhobygridview)
	MyGridView userdetailhobygridview;
	@TAInjectView(id = R.id.rela_userhpbyerror)
	RelativeLayout rela_userhpbyerror;
	@TAInjectView(id = R.id.rela_uservideoadd)
	RelativeLayout rela_uservideoadd;
	@TAInjectView(id = R.id.hobbyaddrel)
	RelativeLayout hobbyaddrel;
	@TAInjectView(id = R.id.rela_userhobbyadd)
	RelativeLayout rela_userhobbyadd;
	@TAInjectView(id = R.id.hobbysex)
	TextView hobbysex;
	@TAInjectView(id = R.id.userdetailhobbyonum)
	TextView userdetailhobbyonum;
	@TAInjectView(id = R.id.userdetailvoiceonum)
	TextView userdetailvoiceonum;
	@TAInjectView(id = R.id.userdetailvoicetime)
	TextView userdetailvoicetime;

	@TAInjectView(id = R.id.userdetailnicknameshow)
	TextView userdetailnicknameshow;
	@TAInjectView(id = R.id.userdetailnicknameedit)
	EditText userdetailnicknameedit;
	@TAInjectView(id = R.id.uservoice)
	LinearLayout uservoice;
	@TAInjectView(id = R.id.ll)
	LinearLayout ll;
	AdapterHobby adapterhobby;
	List<String> userhobbylist;
	@TAInjectView(id = R.id.uservoiceimg)
	ImageView uservoiceimg;
	AdapterUserPhoto adapteruserphotot;
	List<String> userphotolist;
	public static Bitmap bmp;
	boolean isplay;// 播放状态
	boolean isprepare;// 缓存状态
	String videopath = "";
	String voicespath = "";
	int num = 0;
	int maxnum = 0;
	Timer T = new Timer();
	static MediaPlayer mPlayer = new MediaPlayer();
	private boolean isCity = true; // 控制地区不重复显示
	int voicetype = 0;//
	Persion p;
	@TAInjectView(id = R.id.userdetaillive)
	TextView userdetaillive;
	TextView lubutton;// 录音button
	Chronometer ch_jsq;// 计时器
	Boolean lyswitch = false;
	File f;
	private Timer mTimer3; // 定时器3
	private TimerTask mTimerTask3;
	int sj = 0;// 记录录音时间
	MediaRecorder mRecorder;
	File soundFile;
	long ss = 0;// 音频时长 单位秒
	String hobbytype = "";
	int htp = 0;
	String birthday1;// 生日
	private int mYear = 1996;
	private int mMonth = 0;
	private int mDay = 1;
	private WheelView year;
	private WheelView month;
	private WheelView day;
	String VoicePath = "";// 返回来的 地址
	private Uri uri;
	private String path;// 图片地址
	private String flieName = "";
	private boolean isTrue = true;
	String photo = "";// 头像
	List<Image> list;
	int ncisupdate = 0;
	int exitshow = 0;
	String mp4url = null;
	int pdclose = 0;
	BroadcastReceiver broadcastReceiver;

	static LoadingDialog dialog = null;
	private IntentFilter intentToReceiveFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_update);
		loadingDialog.show();
		themes();
		InitView();
		loaduserinfo();
		personfresh();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(broadcastReceiver!=null){
			unregisterReceiver(broadcastReceiver);

		}
	}

	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
		headerthemeleft.setVisibility(View.VISIBLE);
		headerright.setVisibility(View.VISIBLE);
		headconrel1.setVisibility(View.GONE);
		headercontentv.setVisibility(View.GONE);
		userid = getIntent().getIntExtra("userid", 0);
		dialog = new LoadingDialog(PersonUpdateActivity.this);
		Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
		user.setBackgroundDrawable(drawable);
		typelog.setVisibility(View.GONE);
		headconfrim.setText("保存");
		headconfrim.setVisibility(View.VISIBLE);
		userphotolist = new ArrayList<String>();
		list = new ArrayList<Image>();
		adapteruserphotot = new AdapterUserPhoto(context, userphotolist, imageLoader, options);
		userdetailhlist.setAdapter(adapteruserphotot);
		userdetailhlist.setFocusable(false);
		userdetailhlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent in = new Intent(PersonUpdateActivity.this, AmplificationActivity.class);
				in.putExtra("index", position);
				startActivity(in);
				AmplificationActivity.goodsimglists = list;
			}
		});
		userhobbylist = new ArrayList<String>();
		adapterhobby = new AdapterHobby(context, userhobbylist, imageLoader, options);
		userdetailhobygridview.setAdapter(adapterhobby);
		userdetailhobygridview.setFocusable(false);
		userdetailhobygridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		voicetype = 1;

	}

	private void personfresh() {
		broadcastReceiver = new BroadcastReceiver()

		{
			@Override
			public void onReceive(Context context, Intent intent) {
				loaduserinfo();
			}
		};
		intentToReceiveFilter = new IntentFilter();
		intentToReceiveFilter.addAction("personupdate");
		registerReceiver(broadcastReceiver, intentToReceiveFilter);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		// setListViewHeight(adverlist);
		ViewGroup.LayoutParams params = userdetaivideoimg.getLayoutParams();
		// advertising.setImageResources(gdbilllist, mAdCycleViewListener,2);
		params.height = (int) (ScreenUtils.getScreenWidth(PersonUpdateActivity.this) / 5 * 3);
		userdetaivideoimg.setLayoutParams(params);
		super.onWindowFocusChanged(hasFocus);
	}

	public void loaduserinfo() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String jhuser = Person_Service.persionOne(b_person.getId());
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

					if (!mes.equals("") && !mes.equals(JsonUtil.ObjToJson(Constant.FAIL))) {
						p = JsonUtil.JsonToObj(mes, Persion.class);
						headercontent.setText(p.getNick_name());
						userdetailnicknameshow.setText(p.getNick_name());
						userdetailnicknameedit.setText(p.getNick_name());
						userdetaillive.setText(p.getLevel() + "级");
						hobbysex.setText("我的爱好");
						if (p.getSex() == 1) {

							Drawable drawable = context.getResources().getDrawable(R.drawable.detailboy);
							userdetailsex.setBackgroundDrawable(drawable);
						} else {

							Drawable drawable = context.getResources().getDrawable(R.drawable.detailgirl);
							userdetailsex.setBackgroundDrawable(drawable);
						}
						if (p.getAlbum() != null) {
							String[] photo = p.getAlbum().split("_");
							userphotolist.clear();
							list.clear();
							for (int i = 0; i < photo.length; i++) {

								userphotolist.add(photo[i]);
								Image image = new Image();
								image.setPath(photo[i]);
								image.setStatus(2);
								list.add(image);
							}
							userdetailphotonum.setText("" + photo.length);
							userdetailhlist.setVisibility(View.VISIBLE);
							rela_userphoterror.setVisibility(View.GONE);

						} else {
							userdetailphotonum.setText("0");
							userdetailhlist.setVisibility(View.GONE);
							rela_userphoterror.setVisibility(View.VISIBLE);
						}
						imageLoader.displayImage(p.getPhoto(), userdetailcivico, options);
						if (p.getArea() == null) {

						} else {
							if (p.getArea().split("-").length > 1) {
								userdetailaddres.setText(p.getArea().split("-")[1]);
							} else {
								userdetailaddres.setText(p.getArea());
							}
							userdetailarea.setText(p.getArea());
						}
						if (p.getBirthday() == null) {

						} else {
							userdetailbirthday.setText(p.getBirthday());
						}
						userdetailsign.setText(p.getSign());
						userdetailchatvideo.setText(p.getVideo_money() + "金币/分钟");
						userdetailchatvoice.setText(p.getVoice_money() + "金币/分钟");

						if (p.getVideo() != null) {

							ImageLoader.getInstance().loadImage(p.getPhoto(), new ImageLoadingListener() {
								@Override
								public void onLoadingStarted(String imageUri, View view) {
								}

								@Override
								public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
								}

								@Override
								public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
									if (loadedImage != null) {

										BitmapBlurUtil.addTask(loadedImage, new Handler() {
											@Override
											public void handleMessage(Message msg) {
												super.handleMessage(msg);
												Drawable drawable = (Drawable) msg.obj;
												userdetaivideoimg.setImageDrawable(drawable);
												loadedImage.recycle();
											}
										});
									}
								}

								@Override
								public void onLoadingCancelled(String imageUri, View view) {

								}
							});

							videopath = p.getVideo();
							rela_uservideoshow.setVisibility(View.VISIBLE);
							rela_uservideoerror.setVisibility(View.GONE);
							userdetailvideonum.setText("1");
						} else {
							pdclose = 1;
							rela_uservideoshow.setVisibility(View.GONE);
							rela_uservideoerror.setVisibility(View.VISIBLE);
							userdetailvideonum.setText("0");
						}

						if (p.getSup_ability() != null) {
							String[] photo = p.getSup_ability().split("_");
							userhobbylist.clear();
							for (int i = 0; i < photo.length; i++) {

								userhobbylist.add(photo[i]);
							}
							userdetailhobbyonum.setText("" + photo.length);
							userdetailhobygridview.setVisibility(View.VISIBLE);
							rela_userhpbyerror.setVisibility(View.GONE);

						} else {
							userdetailhobbyonum.setText("0");
							userdetailhobygridview.setVisibility(View.GONE);
							rela_userhpbyerror.setVisibility(View.VISIBLE);
						}
						voicespath = p.getVoice_ident();

						if (voicespath != null) {
							userdetailvoiceonum.setText("1");
							maxnum = p.getVoice_time_length();
							rela_uservoicecontent.setVisibility(View.VISIBLE);
							rela_uservoiceerror.setVisibility(View.GONE);
							userdetailvoicetime.setText(secToTime(p.getVoice_time_length()) + "“");
							PrepareM();
						} else {
							userdetailvoiceonum.setText("0");
							rela_uservoicecontent.setVisibility(View.GONE);
							rela_uservoiceerror.setVisibility(View.VISIBLE);
						}
						userdetailnicknameedit.setText(p.getNick_name());
						userdetailage.setText(p.getAge() + "");
						userdetailnum.setText("ID:" + p.getOtherkey());
						scroll_my.setVisibility(View.VISIBLE);
						rela_error.setVisibility(View.GONE);
						rela_broken.setVisibility(View.GONE);

					} else {
						if (ConnectionUtil.isConn(activity) == false) {
							// ConnectionUtil.setNetworkMethod(activity);
							scroll_my.setVisibility(View.GONE);
							rela_error.setVisibility(View.GONE);
							rela_broken.setVisibility(View.VISIBLE);
						} else {
							scroll_my.setVisibility(View.GONE);
							rela_error.setVisibility(View.VISIBLE);
							rela_broken.setVisibility(View.GONE);
						}
					}
				} else {
					scroll_my.setVisibility(View.GONE);
					rela_error.setVisibility(View.VISIBLE);
					rela_broken.setVisibility(View.GONE);
				}
				adapteruserphotot.notifyDataSetChanged();
				adapterhobby.notifyDataSetChanged();
					loadingDialog.dismiss();
				break;
			case 2:
				if (msg.obj != null) {
					String mes = (String) msg.obj;
					if (mes.equals("") || mes.equals(JsonUtil.ObjToJson("error"))) {
						ToastShow("更新失败");
					} else {
						ToastShow("更新成功");
						Persion p = JsonUtil.JsonToObj(mes, Persion.class);
						ManageDataBase.Delete(dbutil, Persion.class, p.getId() + "");
						ManageDataBase.Insert(dbutil, Persion.class, p);
						b_person = p;
						userdetailnicknameshow.setVisibility(View.VISIBLE);
						userdetailnicknameedit.setVisibility(View.GONE);
						userdetailnicknameshow.setText(p.getNick_name());
						if(p.getVideo_ident() != null || p.getIdent_state() != null){
							if(p.getVideo_ident() == 3 || p.getIdent_state() == 5){
								if (!photo.equals("")) {
									new PhotoUpdateView(PersonUpdateActivity.this, ll, 1);
								}
							}	
						}						
					}
				}
				loadingDialog.dismiss();
				break;

			case 3:
				if (msg.obj != null) {
					String mes = (String) msg.obj;
					if (mes.equals("") || mes.equals(JsonUtil.ObjToJson("error"))) {
						ToastShow("上传失败请重试");
					} else {
						Address ads = JsonUtil.JsonToObj(mes, Address.class);
						VoicePath = ads.getReturnPath();
						Upvideo();
					}
				}
				loadingDialog.dismiss();
				break;
			case 5:
				num++;

				userdetailvoicetime.setText(secToTime(num) + "“");
				if (voicetype == 0) {
					if (num >= new Long(ss).intValue()) {
						num = 0;
					}
				} else {
					if (num >= maxnum) {
						num = 0;
					}
				}
				break;

			case 1111:
				if (msg.obj != null) {
					String mes = (String) msg.obj;
					final Bitmap vidoeimg = createVideoThumbnail(mes);
					BitmapBlurUtil.addTask(vidoeimg, new Handler() {
						@Override
						public void handleMessage(Message msg) {
							super.handleMessage(msg);
							Drawable drawable = (Drawable) msg.obj;
							userdetaivideoimg.setImageDrawable(drawable);
							vidoeimg.recycle();
						}
					});
					// userdetaivideoimg.setImageBitmap(vidoeimg);

					VideoPlayActivity.mp4url = mp4url;
					// 下载好视频后 记录这个用户的id,方便下次直接调用
					if (mp4url != null) {
						SharedPreferencesUtil.putInt(PersonUpdateActivity.this, "Cachemp4", p.getId());
						SharedPreferencesUtil.putString(PersonUpdateActivity.this, "Cachemp4path", mp4url);
						// StartActivity(VideoPlayActivity.class);
					}
				} else {
					ImageLoader.getInstance().loadImage(p.getPhoto(), new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
						}

						@Override
						public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
						}

						@Override
						public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
							if (loadedImage != null) {

								BitmapBlurUtil.addTask(loadedImage, new Handler() {
									@Override
									public void handleMessage(Message msg) {
										super.handleMessage(msg);
										Drawable drawable = (Drawable) msg.obj;
										userdetaivideoimg.setImageDrawable(drawable);
										loadedImage.recycle();
									}
								});
							}
						}

						@Override
						public void onLoadingCancelled(String imageUri, View view) {

						}
					});
				}
				loadingDialog.dismiss();
				break;
			default:
				break;
			}
		}
	};

	public class PhotoUpdateView extends PopupWindow {

		public PhotoUpdateView(Context mContext, View parent, int type) {

			View view = View.inflate(mContext, R.layout.photoupdate_loading, null);
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
			btn_dialog_confirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
				}
			});
		}
	}

	public class ExitShowView extends PopupWindow {

		public ExitShowView(Context mContext, View parent) {

			View view = View.inflate(mContext, R.layout.exitshow_loading, null);
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
					exitshow = 0;
					loadingDialog.show();
					if (f != null) {
						voiceAdd(f);
					} else {
						Upvideo();
					}
					dismiss();
				}
			});
			btn_dialog_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					exitshow = 0;
					finish();
					Intent intents = new Intent("userupdatezy");
					sendBroadcast(intents);
					dismiss();
				}
			});
		}
	}

	public class YouqingtishiShowView extends PopupWindow {

		public YouqingtishiShowView(Context mContext, View parent) {

			View view = View.inflate(mContext, R.layout.youqingtishi_loading, null);
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
			TextView btn_dialog_confirm = (TextView) view.findViewById(R.id.btn_dialog_confirm);
			btn_dialog_confirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
				}
			});
		}
	}

	@Override
	protected void onAfterSetContentView() {
		// TODO Auto-generated method stub
		super.onAfterSetContentView();

		OnClickListener onClickListener = new OnClickListener() {
			public static final int MIN_CLICK_DELAY_TIME = 1000;
			private long lastClickTime = 0;

			@Override
			public void onClick(View v) {
				Intent intent = null;
				// TODO Auto-generated method stub
				switch (v.getId()) {

				case R.id.headerthemeleft:
					if (isplay == true) {
						mPlayer.stop();
						try {
							mPlayer.prepare();
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						isplay = false;
						if (T != null) {
							T.cancel();
						}
						T = null;
					}
					if (exitshow == 1) {
						new ExitShowView(PersonUpdateActivity.this, ll);
					} else {
						finish();
						Intent intents = new Intent("userupdatezy");
						sendBroadcast(intents);
					}
					break;
				case R.id.rela_userchargeadd:
					intent = new Intent(PersonUpdateActivity.this, UserChargeActivity.class);
					intent.putExtra("typecharge", 1);
					startActivity(intent);
					break;
				case R.id.rela_uservideoshow:
					/*
					 * intent = new Intent(Intent.ACTION_VIEW); Uri uri =
					 * Uri.parse(videopath); intent.setDataAndType(uri,
					 * "video/mp4"); startActivity(intent);
					 */

					dialog.show();
					int Cache1 = SharedPreferencesUtil.getInt(PersonUpdateActivity.this, "Cachemp4", 0);
					if (Cache1 == p.getId()) {
						
						VideoPlayActivity.mp4url =p.getVideo();
						StartActivity(VideoPlayActivity.class);
					    dialog.dismiss();
				    } else {
						inputMp4();
					}
					break;
				case R.id.rela_userphotadd:
				case R.id.rela_userphoterror:
					intent = new Intent(PersonUpdateActivity.this, PersonPhotoActivity.class);
					// intent.putExtra("voidetype", 1);
					startActivity(intent);
					break;
				case R.id.rela_uservideoadd:
				case R.id.rela_uservideoerror:

					if (p.getVideo_ident() != null && p.getVideo_ident() == 2) {
						intent = new Intent(PersonUpdateActivity.this, UserAuditActivity.class);
						intent.putExtra("voidetype", 1);
						startActivity(intent);
						// StartActivity(UserAuditActivity.class);
					} else {
						intent = new Intent(PersonUpdateActivity.this, VideoAuthenticationActivity.class);
						intent.putExtra("voidetype", 1);
						startActivity(intent);
						// StartActivity(VideoAuthenticationActivity.class);
					}
					break;
				case R.id.rela_userhobbyadd:
				case R.id.rela_userhpbyerror:
					exitshow = 1;

					new HobbyView(PersonUpdateActivity.this, rela_uservideoadd, userhobbylist);
					break;
				case R.id.userdetailbirthday:
					exitshow = 1;
					new BirthdayPopuWindow(PersonUpdateActivity.this, userdetailbirthday);
					break;
				case R.id.userdetailarea:
					exitshow = 1;
					if (isCity == true) {
						isCity = false;
						new PopupWindows(PersonUpdateActivity.this, userdetailarea);
					}
					break;
				case R.id.userdetailsign:
					exitshow = 1;
					intent = new Intent(PersonUpdateActivity.this, EditIntroductionActivity.class);
					startActivityForResult(intent, 61);
					break;
				case R.id.userdetailcivico:
					exitshow = 1;
					new PhotoView(PersonUpdateActivity.this, userdetailcivico, 1);
					break;
				case R.id.headerright:
					exitshow = 0;
					loadingDialog.show();
					if (f != null) {
						voiceAdd(f);
					} else {
						Upvideo();
					}
					break;
				case R.id.rela_uservoiceadd:
				case R.id.rela_uservoiceerror:
					exitshow = 1;
					new LyPopuWindow(PersonUpdateActivity.this, ll);
					break;
				case R.id.uservoiceimg:
					long currentTime = Calendar.getInstance().getTimeInMillis();
					if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
						lastClickTime = currentTime;
						if (voicetype == 0) {
							if (f != null && f.exists()) {
								mPlayer.start();// 播放
								isplay = true;
								T = new Timer();
								T.schedule(new TimerTask() {
									@Override
									public void run() {
										Message msg = new Message();
										msg.what = 5;
										mUIHandler.sendMessage(msg);
									}
								}, 1000, 1000);
							} else {
								ToastShow("您还没有录音哦！");
							}
						} else if (voicetype == 1) {
							if (!isplay) {

								if (isprepare) {
									mPlayer.start();// 播放
									isplay = true;
									T = new Timer();
									T.schedule(new TimerTask() {
										@Override
										public void run() {
											Message msg = new Message();
											msg.what = 5;

											mUIHandler.sendMessage(msg);
										}
									}, 1000, 1000);
								} else {
									ToastShow("音频还在缓存中...");
								}
							} else {
								mPlayer.stop();
								try {
									mPlayer.prepare();
								} catch (IllegalStateException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
								isplay = false;
								if (T != null) {
									T.cancel();
								}
								T = null;
							}
						}
					} /*
						 * else{ new
						 * YouqingtishiShowView(PersonUpdateActivity.this, ll);
						 * }
						 */
					break;
				case R.id.userdetailnicknameshow:
					ncisupdate = 1;
					exitshow = 1;
					userdetailnicknameshow.setVisibility(View.GONE);
					userdetailnicknameedit.setVisibility(View.VISIBLE);
					break;
				default:
					break;
				}
			}
		};
		rela_uservideoerror.setOnClickListener(onClickListener);
		rela_userhpbyerror.setOnClickListener(onClickListener);
		rela_uservoiceerror.setOnClickListener(onClickListener);
		rela_userphoterror.setOnClickListener(onClickListener);
		userdetailnicknameshow.setOnClickListener(onClickListener);
		rela_userhobbyadd.setOnClickListener(onClickListener);
		rela_uservoiceadd.setOnClickListener(onClickListener);
		headerright.setOnClickListener(onClickListener);
		userdetailcivico.setOnClickListener(onClickListener);
		userdetailsign.setOnClickListener(onClickListener);
		userdetailbirthday.setOnClickListener(onClickListener);
		userdetailarea.setOnClickListener(onClickListener);
		hobbyaddrel.setOnClickListener(onClickListener);
		headerthemeleft.setOnClickListener(onClickListener);
		rela_uservideoshow.setOnClickListener(onClickListener);
		uservoiceimg.setOnClickListener(onClickListener);
		rela_userphotadd.setOnClickListener(onClickListener);
		rela_userchargeadd.setOnClickListener(onClickListener);
		rela_uservideoadd.setOnClickListener(onClickListener);
		// 播放完的监听
		mPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				isplay = false;
				if (voicetype == 0) {
					userdetailvoicetime.setText(secToTime(sj) + "“");
				} else {
					userdetailvoicetime.setText(secToTime(maxnum) + "“");
				}
				if (T != null) {
					T.cancel();
				}
			}
		});
		// 缓存完的监听
		mPlayer.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				isprepare = true;
				// ToastShow("音频已缓存好");
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

			if (isplay == true) {
				mPlayer.stop();
				try {
					mPlayer.prepare();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				isplay = false;
				if (T != null) {
					T.cancel();
				}
				T = null;
			}
			if (exitshow == 1) {
				new ExitShowView(PersonUpdateActivity.this, ll);
			} else {
				finish();
				Intent intents = new Intent("userupdatezy");
				sendBroadcast(intents);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void Upvideo() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Persion p = new Persion();
				p.setId(b_person.getId());
				p.setArea(userdetailarea.getText().toString());
				p.setBirthday(userdetailbirthday.getText().toString());
				p.setSign(userdetailsign.getText().toString());
				if (ncisupdate == 1) {
					p.setNick_name(userdetailnicknameedit.getText().toString());
				}
				if (!hobbytype.equals("")) {
					p.setSup_ability(hobbytype);
				}
				if (!VoicePath.equals("")) {
					p.setVoice_ident(VoicePath);
					p.setVoice_time_length(sj);
				}
				if (!photo.equals("")) {
					p.setThumbnails(photo);
				}
				String persion = Person_Service.editDate(JsonUtil.ObjToJson(p));
				Message msg = mUIHandler.obtainMessage(2);
				msg.obj = persion;
				msg.sendToTarget();
			}
		}).start();
	}

	protected void voiceAdd(final File file) {
		loadingDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String request = UploadUtil.uploadFile(file, Constant.FILEUPLOAD);
				Message msg = mUIHandler.obtainMessage(3);
				msg.obj = request;
				msg.sendToTarget();
			}
		}).start();
	}

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
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri, int cjtype) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的，小马不懂C C++
		 * 这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么 制做的了...吼吼
		 */
		// tempFile=null;
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 9);
		intent.putExtra("aspectY", 9);
		intent.putExtra("scale", true);
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
				// bitmap = Bimp.comp(bitmap);
				// releaseimg1.setBackgroundDrawable(new
				// BitmapDrawable(bitmap));
				// Bitmap compression = Bimp.comp(bitmap);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] by = baos.toByteArray();
				String str = new String(Base64Coder.encode(by)); // 把图片转换成string
				photo = str;
				userdetailcivico.setImageBitmap(bitmap);
				// imageLoader.displayImage(tempFile.toString(),
				// userdetailcivico, roundptions);
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
		if (requestCode == 61) {
			Bundle bundleData = data.getExtras();

			if (bundleData.getString("sig") != null && !bundleData.getString("sig").equals("")) {
				userdetailsign.setText(bundleData.getString("sig"));
			}
		}
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
				imgPath = FileUtils.resolvePhotoFromIntent(PersonUpdateActivity.this, data, path);
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

	public class BirthdayPopuWindow extends PopupWindow {
		Calendar c = Calendar.getInstance();
//		int norYear = c.get(Calendar.YEAR);
//		int curYear = mYear;
//		int curMonth = mMonth + 1;
//		int curDate = mDay;

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
					userdetailbirthday.setText(birthday1 + "");
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
			NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(PersonUpdateActivity.this, 1950,
					norYear);
			numericWheelAdapter1.setLabel("年");
			year.setViewAdapter(numericWheelAdapter1);
			year.setCyclic(true);// 是否可循环滑动
			year.addScrollingListener(scrollListener);
			month = (WheelView) view.findViewById(R.id.month);
			NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(PersonUpdateActivity.this, 1, 12,
					"%02d");
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

			year.setCurrentItem(1999);
			month.setCurrentItem(1);
			day.setCurrentItem(1);

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
			NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(PersonUpdateActivity.this, 1,
					getDay(arg1, arg2), "%02d");
			numericWheelAdapter.setLabel("日");
			day.setViewAdapter(numericWheelAdapter);
		}

	}

	public class PopupWindows extends PopupWindow implements OnClickListener, OnWheelChangedListener {
		WheelView1 mViewCity;
		WheelView1 mViewProvince;
		TextView citycel;
		TextView citydef;
		Context mContext = null;

		public PopupWindows(Context mContext, View parent) {
			this.mContext = mContext;
			View view = null;

			view = View.inflate(mContext, R.layout.persoanl_city, null);
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
			mViewProvince = (WheelView1) view.findViewById(R.id.province_id);
			mViewCity = (WheelView1) view.findViewById(R.id.city_id);
			citycel = (TextView) view.findViewById(R.id.citycel);
			citydef = (TextView) view.findViewById(R.id.citydef);
			setUpListener();
			setUpData();
		}

		private void setUpListener() {
			// 添加change事件
			mViewProvince.addChangingListener(this);// addChangingListener(this);
			// 添加change事件
			mViewCity.addChangingListener(this);
			// 添加change事件

			// 添加onclick事件
			citydef.setOnClickListener(this);
			citycel.setOnClickListener(this);
		}

		private void setUpData() {
			initProvinceDatas();
			mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(mContext, mProvinceDatas));
			// 设置可见条目数量
			mViewProvince.setVisibleItems(7);
			mViewCity.setVisibleItems(7);
			// mViewDistrict.setVisibleItems(7);
			updateCities();
			updateAreas();
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.citydef:
				isCity = true;
				showSelectedResult();
				break;
			case R.id.citycel:
				isCity = true;
				dismiss();
				break;
			default:
				break;
			}
		}

		/**
		 * 根据当前的省，更新市WheelView的信息
		 */
		private void updateCities() {
			int pCurrent = mViewProvince.getCurrentItem();
			mCurrentProviceName = mProvinceDatas[pCurrent];
			String[] cities = mCitisDatasMap.get(mCurrentProviceName);
			if (cities == null) {
				cities = new String[] { "" };
			}
			mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(mContext, cities));
			mViewCity.setCurrentItem(0);
			updateAreas();
		}

		private void showSelectedResult() {
			userdetailarea.setText(mCurrentProviceName + "-" + mCurrentCityName + "");
			dismiss();
		}

		/**
		 * 根据当前的市，更新区WheelView的信息
		 */
		private void updateAreas() {
			int pCurrent = mViewCity.getCurrentItem();
			mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
			String[] areas = mDistrictDatasMap.get(mCurrentCityName);

			if (areas == null) {
				areas = new String[] { "" };
			}
		}

		@Override
		public void onChanged(WheelView1 wheel, int oldValue, int newValue) {
			// TODO Auto-generated method stub
			if (wheel == mViewProvince) {
				updateCities();
			} else if (wheel == mViewCity) {
				updateAreas();
			}
		}
	}

	public class HobbyView extends PopupWindow {
		int num = 0;
		List<Hobby> listHobbies;

		public HobbyView(Context mContext, View parent, List<String> xhobby) {

			View view = View.inflate(mContext, R.layout.hobby_loading, null);
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
			Resources res = getResources();
			listHobbies = new ArrayList<Hobby>();
			String[] planets = res.getStringArray(R.array.hobby);
			for (int i = 0; i < planets.length; i++) {
				Hobby bobby2 = new Hobby();
				bobby2.setHobbycontent(planets[i]);
				bobby2.setIsSelected(1);
				listHobbies.add(bobby2);
			}

			final AdapterSysHobby adaptersyshobby = new AdapterSysHobby(PersonUpdateActivity.this, listHobbies);
			ImageView hobbydel = (ImageView) view.findViewById(R.id.hobbydel);
			GridView hobygridview = (GridView) view.findViewById(R.id.hobygridview);
			hobygridview.setAdapter(adaptersyshobby);
			hobygridview.setFocusable(false);
			hobygridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
			hobygridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub

					Hobby hobby = listHobbies.get(position);
					if (hobby.getIsSelected() == 1) {
						num++;
						if (num <= 6) {
							hobby.setIsSelected(2);
						} else {
							ToastShow("最多选择6个");
						}
					} else {
						num--;
						hobby.setIsSelected(1);
					}
					listHobbies.set(position, hobby);
					adaptersyshobby.notifyDataSetChanged();
				}
			});
			hobbydel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (num < 1) {
						dismiss();
					} else {
						htp = 1;
						userhobbylist.clear();
						hobbytype = "";
						for (int i = 0; i < listHobbies.size(); i++) {
							Hobby hobby = listHobbies.get(i);
							if (hobby.getIsSelected() == 2) {
								if (hobbytype.equals("")) {
									hobbytype = hobby.getHobbycontent();
								} else {
									hobbytype = hobbytype + "_" + hobby.getHobbycontent();
								}
								userhobbylist.add(hobby.getHobbycontent());
							}
						}

						adapterhobby.notifyDataSetChanged();
						userdetailhobygridview.setVisibility(View.VISIBLE);
						rela_userhpbyerror.setVisibility(View.GONE);
						userdetailhobbyonum.setText("" + userhobbylist.size());
						dismiss();
					}
				}
			});
		}
	}

	// 录音弹窗
	public class LyPopuWindow extends PopupWindow {
		public LyPopuWindow(Context mcontext, View parent) {
			View view = View.inflate(mcontext, R.layout.luyin, null);
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
			lubutton = (TextView) view.findViewById(R.id.lubutton);
			ch_jsq = (Chronometer) view.findViewById(R.id.ch_jsq);
			ImageView view_break = (ImageView) view.findViewById(R.id.view_break);
			view_break.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					/*
					 * userdetailvoiceonum.setText("1");
					 * rela_uservoicecontent.setVisibility(View.GONE);
					 * rela_uservoiceerror.setVisibility(View.VISIBLE);
					 */
					dismiss();
				}
			});
			lubutton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (!lyswitch) {
						ch_jsq.setBase(SystemClock.elapsedRealtime());
						ch_jsq.start();
						voicetype = 0;
						f = null;
						Ly();
						sj = 0;
						mTimer3 = new Timer();
						mTimerTask3 = new TimerTask() {
							public void run() {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										sj++;
										if (sj == 60) {
											// 停止录音
											stopLy();
											dismiss();
										}
									}
								});
							}
						};
						mTimer3.schedule(mTimerTask3, 1000, 1000);
						lubutton.setText("停止录音");
						lyswitch = true;
					} else {
						userdetailvoiceonum.setText("1");
						rela_uservoicecontent.setVisibility(View.VISIBLE);
						rela_uservoiceerror.setVisibility(View.GONE);
						if (f != null && f.exists()) {
							stopLy();

						}
						dismiss();
					}
				}
			});
			this.OnDismissListener();
		}

		private void OnDismissListener() {
			if (mTimer3 != null) {
				mTimer3.cancel();
			}
			if (mTimerTask3 != null) {
				mTimerTask3.cancel();
			}
			if (mRecorder != null) {
				mRecorder.stop();
				mRecorder.release();
				mRecorder = null;
				lyswitch = false;
			}
			if (ch_jsq != null) {
				ch_jsq.stop();
			}
		}
	}

	public void Ly() {
		if (!SDcardTools.isHaveSDcard()) {
			Toast.makeText(PersonUpdateActivity.this, "请插入SD卡以便存储录音", Toast.LENGTH_LONG).show();
		}
		try {
			voicespath = SDcardTools.getSDPath() + "/" + "love";
			soundFile = new File(SDcardTools.getSDPath() + "/" + "Lmei");
			if (!soundFile.exists()) {
				// 如果文件夹不存在 则创建文件夹
				soundFile.mkdir();
			}
			f = new File(soundFile.getAbsolutePath() + "/love" + getPhotoFileName() + ".amr");
			// 如果目标文件已经存在，则删除。产生覆盖旧文件的效果

			mRecorder = new MediaRecorder();
			// 声音的来源
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			// 输出格式
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			// 编码格式
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
			mRecorder.setOutputFile(f.getAbsolutePath());
			mRecorder.prepare();
			mRecorder.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopLy() {
		mTimerTask3.cancel();
		mRecorder.stop();
		ch_jsq.stop();

		ss = (SystemClock.elapsedRealtime() - ch_jsq.getBase()) / 1000;
		userdetailvoicetime.setText(secToTime(sj) + "“");
		// 释放资源
		mRecorder.release();
		mRecorder = null;
		try {
			mPlayer.reset();
			mPlayer.setDataSource(f.getAbsolutePath());
			mPlayer.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
		lyswitch = false;
	}

	// 使用系统当前日期加以调整作为名称
	private String getPhotoFileName() {
		java.util.Random random = new java.util.Random();// 定义随机类
		int result = random.nextInt(10000);
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String filename = "" + result + dateFormat.format(date);
		return filename;
	}

	public static String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
			}
		}
		return timeStr;
	}

	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

	/**
	 * 缓存录音文件
	 */
	public void PrepareM() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				inputMp3(voicespath);// 缓存音频
			}
		}).start();
	}

	public static void PrepareM2() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mPlayer.setDataSource(SDcardTools.getSDPath() + "/mp3.mp3");
					mPlayer.prepareAsync();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 缓存MP3
	 * 
	 * @param path
	 * @return
	 */

	public static String inputMp3(String path) {
		String newFilename = SDcardTools.getSDPath() + "/mp3.mp3";
		File file = new File(newFilename);
		// 如果目标文件已经存在，则删除。产生覆盖旧文件的效果
		if (file.exists()) {
			file.delete();
		}
		try {
			// 构造URL
			URL url = new URL(path);
			// 打开连接
			URLConnection con = url.openConnection();
			// 获得文件的长度
			int contentLength = con.getContentLength();
			System.out.println("长度 :" + contentLength);
			// 输入流
			InputStream is = con.getInputStream();
			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流
			OutputStream os = new FileOutputStream(newFilename);
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			// 完毕，关闭所有链接
			os.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PrepareM2();
		return newFilename;
	}

	/**
	 * 缓存MP4
	 */
	public void inputMp4() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				mp4url = inputMp4(p.getVideo());
				VideoPlayActivity.mp4url = mp4url;
				//下载好视频后 记录这个用户的id,方便下次直接调用
				if(!PersonUpdateActivity.this.isFinishing()){
					SharedPreferencesUtil.putInt(PersonUpdateActivity.this, "Cachemp4", p.getId());
					StartActivity(VideoPlayActivity.class);
				}

			}
		}).start();

	}

	public static String inputMp4(String path) {
		String newFilename = SDcardTools.getSDPath() + "/mp4.mp4";
		File file = new File(newFilename);
		// 如果目标文件已经存在，则删除。产生覆盖旧文件的效果
		if (file.exists()) {
			file.delete();
		}
		try {
			// 构造URL
			URL url = new URL(path);
			// 打开连接
			URLConnection con = url.openConnection();
			// 获得文件的长度
			int contentLength = con.getContentLength();
			System.out.println("长度 :" + contentLength);
			// 输入流
			InputStream is = con.getInputStream();
			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流
			OutputStream os = new FileOutputStream(newFilename);
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			// 完毕，关闭所有链接
			os.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		dialog.dismiss();
		return newFilename;
	}

	/*
	 * public static Bitmap createVideoThumbnail(String filePath) { //
	 * MediaMetadataRetriever is available on API Level 8 // but is hidden until
	 * API Level 10 Class<?> clazz = null; Object instance = null; try { clazz =
	 * Class.forName("android.media.MediaMetadataRetriever"); instance =
	 * clazz.newInstance();
	 * 
	 * Method method = clazz.getMethod("setDataSource", String.class);
	 * method.invoke(instance, filePath);
	 * 
	 * // The method name changes between API Level 9 and 10. if
	 * (Build.VERSION.SDK_INT <= 9) { return (Bitmap)
	 * clazz.getMethod("captureFrame").invoke(instance); } else { byte[] data =
	 * (byte[]) clazz.getMethod("getEmbeddedPicture").invoke(instance); if (data
	 * != null) { Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
	 * data.length); if (bitmap != null) return bitmap; } return (Bitmap)
	 * clazz.getMethod("getFrameAtTime").invoke(instance); } } catch
	 * (IllegalArgumentException ex) { // Assume this is a corrupt video file }
	 * catch (RuntimeException ex) { // Assume this is a corrupt video file. }
	 * catch (InstantiationException e) { Log.e("1", "createVideoThumbnail", e);
	 * } catch (InvocationTargetException e) { Log.e("2",
	 * "createVideoThumbnail", e); } catch (ClassNotFoundException e) {
	 * Log.e("3", "createVideoThumbnail", e); } catch (NoSuchMethodException e)
	 * { Log.e("4", "createVideoThumbnail", e); } catch (IllegalAccessException
	 * e) { Log.e("5", "createVideoThumbnail", e); } finally { try { if
	 * (instance != null) { clazz.getMethod("release").invoke(instance); } }
	 * catch (Exception ignored) { } } return null; }
	 */
	public Bitmap createVideoThumbnail(String filePath) {
		Bitmap bitmap = null;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			retriever.setDataSource(filePath);
			bitmap = retriever.getFrameAtTime();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			try {
				retriever.release();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
		return Bimp.comp(bitmap);
	}
	/*
	 * public static Bitmap createVideoThumbnail(String filePath) { //
	 * MediaMetadataRetriever is available on API Level 8 // but is hidden until
	 * API Level 10 Class<?> clazz = null; Object instance = null; try { clazz =
	 * Class.forName("android.media.MediaMetadataRetriever"); instance =
	 * clazz.newInstance();
	 * 
	 * Method method = clazz.getMethod("setDataSource", String.class);
	 * method.invoke(instance, filePath);
	 * 
	 * // The method name changes between API Level 9 and 10. if
	 * (Build.VERSION.SDK_INT <= 9) { return (Bitmap)
	 * clazz.getMethod("captureFrame").invoke(instance); } else { byte[] data =
	 * (byte[]) clazz.getMethod("getEmbeddedPicture").invoke(instance); if (data
	 * != null) { Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
	 * data.length); if (bitmap != null) return bitmap; } return (Bitmap)
	 * clazz.getMethod("getFrameAtTime").invoke(instance); } } catch
	 * (IllegalArgumentException ex) { // Assume this is a corrupt video file }
	 * catch (RuntimeException ex) { // Assume this is a corrupt video file. }
	 * catch (InstantiationException e) { Log.e("1", "createVideoThumbnail", e);
	 * } catch (InvocationTargetException e) { Log.e("2",
	 * "createVideoThumbnail", e); } catch (ClassNotFoundException e) {
	 * Log.e("3", "createVideoThumbnail", e); } catch (NoSuchMethodException e)
	 * { Log.e("4", "createVideoThumbnail", e); } catch (IllegalAccessException
	 * e) { Log.e("5", "createVideoThumbnail", e); } finally { try { if
	 * (instance != null) { clazz.getMethod("release").invoke(instance); } }
	 * catch (Exception ignored) { } } return null; }
	 */

	@Override
	public void onResume() {
		mPlayer.reset();
		super.onResume();
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
