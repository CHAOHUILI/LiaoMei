package com.vidmt.lmei.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.AudioManager;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.ta.TAApplication;
import com.vidmt.lmei.EvaluateActivity;
import com.vidmt.lmei.R;
import com.vidmt.lmei.adapter.GridViewAdapter;
import com.vidmt.lmei.adapter.PersentAdapter;
import com.vidmt.lmei.adapter.ViewPagerAdapter;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Chat_Service;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.entity.Model;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.entity.Present;
import com.vidmt.lmei.util.rule.BitmapBlurUtil;
import com.vidmt.lmei.util.rule.ManageDataBase;
import com.vidmt.lmei.util.rule.SharedPreferencesUtil;
import com.vidmt.lmei.util.think.DbUtil;
import com.vidmt.lmei.util.think.JsonUtil;


import io.rong.calllib.AgoraVideoFrame;
import io.rong.calllib.CallUserProfile;
import io.rong.calllib.IVideoFrameListener;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;
import io.rong.calllib.message.CallSTerminateMessage;
import io.rong.common.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;

import io.rong.imkit.widget.AsyncImageView;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;

/**
 * 一对一通话页activity
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class SingleCallActivity extends BaseCallActivity implements Handler.Callback ,IVideoFrameListener{
	private static final String TAG = "VoIPSingleActivity";
	LayoutInflater inflater;
	RongCallSession callSession;
	FrameLayout mLPreviewContainer;
	FrameLayout mSPreviewContainer;
	FrameLayout mButtonContainer;
	LinearLayout mUserInfoContainer;
	Boolean isInformationShow = false;
	Handler mHandler;
	SurfaceView mLocalVideo = null;
	boolean muted = false;
	boolean handFree = false;
	boolean startForCheckPermissions = false;

	static final int EVENT_HIDDEN_INFO = 1;
	String targetId = null;

	public static  Runnable  erunnable;
	public static   Handler ehandler;
	public static  Runnable  serunnable;
	public static   Handler sehandler;

	Bitmap Bmp;
	private String strDate = null;
	private String pathImage = null;
	private String nameImage = null;

	private MediaProjection mMediaProjection = null;
	private VirtualDisplay mVirtualDisplay = null;

	private int windowWidth = 0;
	private int windowHeight = 0;
	private ImageReader mImageReader = null;
	private int mScreenDensity = 0;
	private MediaProjectionManager mMediaProjectionManager;
	private int REQUEST_MEDIA_PROJECTION = 1;

	private int  tonken;//金币
	private PersentAdapter  persentAdapter;
	private View parent;
	private Present cates;
	private int incalltype;//1为拨出2为接入

	private int chatid=0;//聊天id;
	View kk;
	private LinearLayout rc_voip_liiwu;//礼物out
	private TextView times;//时间
	private ImageView rc_voip_liiwud;//礼物按钮


	private LinearLayout rc_voip_vliiwus;//语音打赏关注out
	private ImageView dashangv;//语音打赏
	private RelativeLayout guangzhuv;//语音关注
	private RelativeLayout yiguangzhu;//已关注


	private static int RS = 0;  
	private static int G = 1;  
	private static int B = 2;  
	private ArrayList<Present> presents; 
	private int catetoken;//礼物的爱意数
	File photofile;
	private int paytoken;//支付金币数

	private List<View> mPagerList;
	private int type;
	/**
	 * 总的页数
	 */
	private int pageCount;
	/**
	 * 每一页显示的个数
	 */
	private int pageSize = 8;
	/**
	 * 当前显示的是第几页
	 */
	private int curIndex = 0;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options;

	private Persion	persion;
	private int  islike=0;

	public static SingleCallActivity singleCallActivity;
	private AgoraVideoFrame agoraVideoFrame;
	@Override
	@TargetApi(23)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rc_voip_activity_single_call);
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.sisterlog)
				.showImageForEmptyUri(R.drawable.sisterlog)
				.showImageOnFail(R.drawable.sisterlog)
				.cacheInMemory()
				.cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(0))
				.build();
		Intent intent = getIntent();
		mHandler = new Handler(Looper.getMainLooper());
		mLPreviewContainer = (FrameLayout) findViewById(R.id.rc_voip_call_large_preview);
		mSPreviewContainer = (FrameLayout) findViewById(R.id.rc_voip_call_small_preview);
		kk = findViewById(R.id.kk);
		mButtonContainer = (FrameLayout) findViewById(R.id.rc_voip_btn);
		mUserInfoContainer = (LinearLayout) findViewById(R.id.rc_voip_user_info);
		rc_voip_liiwu = (LinearLayout) findViewById(R.id.rc_voip_vliiwus);
		rc_voip_liiwud = (ImageView) findViewById(R.id.rc_voip_liiwud);
		times = (TextView) findViewById(R.id.times);


		//		dashang = (ImageView) findViewById(R.id.dashang);
		//		guangzhu = (ImageView) findViewById(R.id.guangzhu);



		rc_voip_vliiwus = (LinearLayout) findViewById(R.id.rc_voip_vliiwus);
		dashangv = (ImageView) findViewById(R.id.dashangv);
		guangzhuv = (RelativeLayout) findViewById(R.id.guangzhuv);
		yiguangzhu= (RelativeLayout) findViewById(R.id.yiguangzhu);

		startForCheckPermissions = intent.getBooleanExtra("checkPermissions", false);
		RongCallCommon.CallMediaType mediaType = null;
		RongCallAction callAction = RongCallAction.valueOf(intent.getStringExtra("callAction"));

		String str = intent.getStringExtra("targetId");
		if (callAction == null) {
			return;
		}
		if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {
			if (intent.getAction().equals(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEAUDIO)) {
				mediaType = RongCallCommon.CallMediaType.AUDIO;
			} else {
				mediaType = RongCallCommon.CallMediaType.VIDEO;
			}

		} else if (callAction.equals(RongCallAction.ACTION_INCOMING_CALL)) {
			callSession = intent.getParcelableExtra("callSession");
			mediaType = callSession.getMediaType();
			str =callSession.getCallerUserId();
		} else {

			callSession = RongCallClient.getInstance().getCallSession();

			if (callSession==null) {

			}else {
				mediaType = callSession.getMediaType();
				str =callSession.getCallerUserId();
			}

		}


		inflater = LayoutInflater.from(this);

		if (str!=null) {
			getPersions(str);
			initView(mediaType, callAction,str);
		}else {
			initView(mediaType, callAction,str);
		}


		if (!requestCallPermissions(mediaType)) {
			return;
		}
		int s = getAndroidSDKVersion();
		setupIntent();


		RongContext.getInstance().getEventBus().register(this);
		if (s<20) {

		}else {



		}

		rc_voip_liiwud.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new PopupWindows(SingleCallActivity.this,kk,1,1);
				Toast.makeText(SingleCallActivity.this, "wen", Toast.LENGTH_SHORT).show();
			}
		});
		//		dashang.setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View v) {
		//				// TODO Auto-generated method stub
		//				new PopupWindows(SingleCallActivity.this,kk,1,1);
		//				//dashang.setVisibility(View.GONE);
		//				//Toast.makeText(SingleCallActivity.this, "wen", Toast.LENGTH_SHORT).show();
		//			}
		//		});
		dashangv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new PopupWindows(SingleCallActivity.this,kk,1,2);
				//dashang.setVisibility(View.GONE);
				//	Toast.makeText(SingleCallActivity.this, "wen", Toast.LENGTH_SHORT).show();
			}
		});
		int ltype = SharedPreferencesUtil.getInt(SingleCallActivity.this, "ltype", 0);
		if (ltype==1) {

			guangzhuv.setVisibility(View.GONE);
			yiguangzhu.setVisibility(View.GONE);
		}else {

		}

		guangzhuv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(islike==0){
					LoadStrangerData();
				}else{
					unsubscribe();
				}
			}
		});
		yiguangzhu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(islike==0){
					LoadStrangerData();
				}else{
					unsubscribe();
				}
			}
		});

		genpresent();

		registerVideoFrameListener(iVideoFrameListener);
		singleCallActivity=this;

	}
	private void getviews() {
		// TODO Auto-generated method stub
		if (Bmp==null) {
			sehandler = new Handler();  
			serunnable = new Runnable() {  
				public void run() {  




					new Thread(new Runnable() {
						@Override
						public void run() {}
					}).start();

					sehandler.postDelayed(serunnable, 50000);


					//每0.5秒监听一次是否在播放视频  
				}
			};  
			serunnable.run();
		}else {

		}
	}

	IVideoFrameListener iVideoFrameListener = new IVideoFrameListener() {

		@Override                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
		public boolean onCaptureVideoFrame( AgoraVideoFrame arg0) {
			// TODO Auto-generated method stub
			arg0.getuBuffer();
			agoraVideoFrame=arg0;
			//			YuvImage image = new YuvImage(yuv, format, width, height, strides);

		


			return false;
		}
	};
	private static class RGB{  
		public int r, g, b;  
	}  


	private static RGB yuvTorgb(byte Y, byte U, byte V){  
		RGB rgb = new RGB();  
		rgb.r = (int)((Y&0xff) + 1.4075 * ((V&0xff)-128));  
		rgb.g = (int)((Y&0xff) - 0.3455 * ((U&0xff)-128) - 0.7169*((V&0xff)-128));  
		rgb.b = (int)((Y&0xff) + 1.779 * ((U&0xff)-128));  
		rgb.r =(rgb.r<0? 0: rgb.r>255? 255 : rgb.r);  
		rgb.g =(rgb.g<0? 0: rgb.g>255? 255 : rgb.g);  
		rgb.b =(rgb.b<0? 0: rgb.b>255? 255 : rgb.b);  
		return rgb;  
	}  
	public Bitmap rawByteArray2RGBABitmap2(byte[] yBuffer,byte[] uBuffer,byte[] vBuffer,int  yStride,int  uStride,int  vStride, int width, int height) {

		Bitmap bmp = Bitmap.createBitmap(width,  height, Bitmap.Config.ARGB_8888);
		bmp.setPixels(I420ToRGB(yBuffer,uBuffer,vBuffer, width, height), 0 , width, 0, 0, width, height);
		return bmp;
	}
	public static int[] I420ToRGB(byte[] yBuffer,byte[] uBuffer,byte[] vBuffer, int width, int height){  
		int numOfPixel = width * height;  
		int positionOfV = numOfPixel;  
		int positionOfU = numOfPixel/4 + numOfPixel;  
		int[] rgb = new int[numOfPixel*3];  
		for(int i=0; i<height; i++){  
			int startY = i*width;  
			int step = (i/2)*(width/2);  
			int startU = positionOfV + step;  
			int startV = positionOfU + step;  
			for(int j = 0; j < width; j++){  
				int Y = startY + j;  
				int U = startU + j/2;  
				int V = startV + j/2;  
				int index = Y*3;  
				try {
					RGB tmp = yuvTorgb(yBuffer[Y], uBuffer[U], vBuffer[V]);  

					rgb[index+RS] = tmp.r;  
					rgb[index+G] = tmp.g;  
					rgb[index+B] = tmp.b; 
				} catch (Exception e) {
					// TODO: handle exception
				}

			}  
		}  

		return rgb;  
	}  
	public void getPersion(final String id) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String Pdata = Person_Service.persionOne(id);
				Message msg = mUIHandler.obtainMessage(14);
				msg.obj = Pdata;
				msg.sendToTarget();
			}
		}).start();
	}
	public void getPersions(final String id) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String Pdata = Person_Service.persionOne(id);
				Message msg = mUIHandler.obtainMessage(13);
				msg.obj = Pdata;
				msg.sendToTarget();
			}
		}).start();
	}
	@Override
	protected void onNewIntent(Intent intent) {
		startForCheckPermissions = intent.getBooleanExtra("checkPermissions", false);
		RongCallCommon.CallMediaType mediaType;
		RongCallAction callAction = RongCallAction.valueOf(intent.getStringExtra("callAction"));
		if (callAction == null) {
			return;
		}
		if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {
			if (intent.getAction().equals(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEAUDIO)) {
				mediaType = RongCallCommon.CallMediaType.AUDIO;
			} else {
				mediaType = RongCallCommon.CallMediaType.VIDEO;
			}
		} else if (callAction.equals(RongCallAction.ACTION_INCOMING_CALL)) {
			callSession = intent.getParcelableExtra("callSession");
			mediaType = callSession.getMediaType();
		} else {
			callSession = RongCallClient.getInstance().getCallSession();
			mediaType = callSession.getMediaType();
		}

		if (!requestCallPermissions(mediaType)) {
			return;
		}
		setupIntent();

		super.onNewIntent(intent);
	}

	//    @TargetApi(23)
	//    @Override
	//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
	//        switch (requestCode) {
	//            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
	//                Map<String, Integer> mapPermissions = new HashMap<>();
	//                mapPermissions.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
	//                mapPermissions.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
	//
	//                for (int i = 0; i < permissions.length; i++) {
	//                    mapPermissions.put(permissions[i], grantResults[i]);
	//                }
	//                if (mapPermissions.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
	//                        && mapPermissions.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
	//                    if (startForCheckPermissions) {
	//                        startForCheckPermissions = false;
	//                        RongCallClient.getInstance().onPermissionGranted();
	//                    } else {
	//                        setupIntent();
	//                    }
	//                } else {
	//                    if (startForCheckPermissions) {
	//                        startForCheckPermissions = false;
	//                        RongCallClient.getInstance().onPermissionDenied();
	//                    } else {
	//                        finish();
	//                    }
	//                }
	//                break;
	//            default:
	//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	//        }
	//
	//    }
	//这里接听到消息
	private void setupIntent () {
		RongCallCommon.CallMediaType mediaType;
		Intent intent = getIntent();
		RongCallAction callAction = RongCallAction.valueOf(intent.getStringExtra("callAction"));
		if (callAction == null || callAction.equals(RongCallAction.ACTION_RESUME_CALL)) {
			return;
		}
		if (callAction.equals(RongCallAction.ACTION_INCOMING_CALL)) {
			callSession = intent.getParcelableExtra("callSession");
			mediaType = callSession.getMediaType();
			targetId = callSession.getInviterUserId();
			incalltype=2;
			getPersions(targetId);
		} else if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {
			if (intent.getAction().equals(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEAUDIO)) {
				mediaType = RongCallCommon.CallMediaType.AUDIO;
			} else {
				mediaType = RongCallCommon.CallMediaType.VIDEO;
			}
			Conversation.ConversationType conversationType = Conversation.ConversationType.valueOf(intent.getStringExtra("conversationType").toUpperCase(Locale.getDefault()));
			targetId = intent.getStringExtra("targetId");

			List<String> userIds = new ArrayList<String>();
			userIds.add(targetId);
			incalltype=1;
			RongCallClient.getInstance().startCall(conversationType, targetId, userIds, mediaType, null);
			getPersion(targetId);
		} else {
			callSession = RongCallClient.getInstance().getCallSession();
			mediaType = callSession.getMediaType();
		}

		if (mediaType.equals(RongCallCommon.CallMediaType.AUDIO)) {
			handFree = false;
		} else if (mediaType.equals(RongCallCommon.CallMediaType.VIDEO)) {
			handFree = true;
		}


		UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(targetId);
		if (userInfo != null) {
			TextView userName = (TextView) mUserInfoContainer.findViewById(R.id.rc_voip_user_name);
			userName.setText(userInfo.getName());

			AsyncImageView userPortrait = (AsyncImageView) mUserInfoContainer.findViewById(R.id.rc_voip_user_portrait);
			if (userPortrait!= null) {
				if (userInfo.getPortraitUri().toString()==null) {

					if (persion!=null) {
						userPortrait.setResource(persion.getPhoto(), R.drawable.sisterlog);
					}else {
						userPortrait.setResource(userInfo.getPortraitUri().toString(), R.drawable.sisterlog);

					}
				}else {
					userPortrait.setResource(userInfo.getPortraitUri().toString(), R.drawable.sisterlog);

				}
			}
			if (mediaType.equals(RongCallCommon.CallMediaType.AUDIO)) {
				//AsyncImageView userPortrait = (AsyncImageView) mUserInfoContainer.findViewById(R.id.rc_voip_user_portrait);
				if (userPortrait != null) {
					if (userInfo.getPortraitUri().toString()==null) {

						if (persion!=null) {
							userPortrait.setResource(persion.getPhoto(), R.drawable.sisterlog);
						}else {
							userPortrait.setResource(userInfo.getPortraitUri().toString(), R.drawable.sisterlog);

						}
					}else {
						userPortrait.setResource(userInfo.getPortraitUri().toString(), R.drawable.sisterlog);

					}

					final RelativeLayout rc_voip_call_information =(RelativeLayout) findViewById(R.id.rc_voip_call_information);
					if (userInfo!=null){
						ImageLoader.getInstance().loadImage(userInfo.getPortraitUri().toString(), new ImageLoadingListener() {
							@Override
							public void onLoadingStarted(String imageUri, View view) {
							}

							@Override
							public void onLoadingFailed(String imageUri, View view,FailReason failReason) {
							}
							@Override
							public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
								if(loadedImage!=null){

									BitmapBlurUtil.addTask(loadedImage, new Handler(){
										@Override
										public void handleMessage(Message msg) {
											super.handleMessage(msg);
											Drawable drawable = (Drawable) msg.obj;
											rc_voip_call_information.setBackgroundDrawable(drawable);
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
				}
			}else {
				//AsyncImageView userPortrait = (AsyncImageView) mUserInfoContainer.findViewById(R.id.rc_voip_user_portrait);
				if (userPortrait != null) {
					if (userInfo.getPortraitUri().toString()==null) {

						if (persion!=null) {
							userPortrait.setResource(persion.getPhoto(), R.drawable.sisterlog);
						}else {
							userPortrait.setResource(userInfo.getPortraitUri().toString(), R.drawable.sisterlog);

						}
					}else {
						userPortrait.setResource(userInfo.getPortraitUri().toString(), R.drawable.sisterlog);

					}

					final RelativeLayout rc_voip_call_information =(RelativeLayout) findViewById(R.id.rc_voip_call_information);
					if (userInfo!=null){
						ImageLoader.getInstance().loadImage(userInfo.getPortraitUri().toString(), new ImageLoadingListener() {
							@Override
							public void onLoadingStarted(String imageUri, View view) {
							}

							@Override
							public void onLoadingFailed(String imageUri, View view,FailReason failReason) {
							}
							@Override
							public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
								if(loadedImage!=null){

									BitmapBlurUtil.addTask(loadedImage, new Handler(){
										@Override
										public void handleMessage(Message msg) {
											super.handleMessage(msg);
											Drawable drawable = (Drawable) msg.obj;
											rc_voip_call_information.setBackgroundDrawable(drawable);
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
				}
			}
		}

		//		
		//		
	}
	//加载界面
	private void initView(RongCallCommon.CallMediaType mediaType, RongCallAction callAction,String targetId) {
		FrameLayout buttonLayout = (FrameLayout) inflater.inflate(R.layout.rc_voip_call_bottom_connected_button_layout, null);
		RelativeLayout userInfoLayout = (RelativeLayout) inflater.inflate(R.layout.rc_voip_audio_call_user_info, null);
		userInfoLayout.findViewById(R.id.rc_voip_call_minimize).setVisibility(View.GONE);

		if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {
			buttonLayout.findViewById(R.id.rc_voip_call_mute).setVisibility(View.GONE);
			buttonLayout.findViewById(R.id.rc_voip_handfree).setVisibility(View.GONE);
		}

		if (mediaType.equals(RongCallCommon.CallMediaType.AUDIO)) {


			UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(targetId);
			final RelativeLayout rc_voip_call_information=(RelativeLayout) findViewById(R.id.rc_voip_call_information);
			rc_voip_call_information.setBackgroundResource(R.drawable.chatbg);
			if (userInfo!=null){
				ImageLoader.getInstance().loadImage(userInfo.getPortraitUri().toString(), new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,FailReason failReason) {
					}
					@Override
					public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
						if(loadedImage!=null){

							BitmapBlurUtil.addTask(loadedImage, new Handler(){
								@Override
								public void handleMessage(Message msg) {
									super.handleMessage(msg);
									Drawable drawable = (Drawable) msg.obj;
									rc_voip_call_information.setBackgroundDrawable(drawable);
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



			mLPreviewContainer.setVisibility(View.GONE);
			mSPreviewContainer.setVisibility(View.GONE);

			if (callAction.equals(RongCallAction.ACTION_INCOMING_CALL)) {
				buttonLayout = (FrameLayout) inflater.inflate(R.layout.rc_voip_call_bottom_incoming_button_layout, null);
				TextView callInfo = (TextView) userInfoLayout.findViewById(R.id.rc_voip_call_remind_info);
				callInfo.setText(R.string.rc_voip_audio_call_inviting);
				onIncomingCallRinging();
			}else {

				rc_voip_vliiwus.setVisibility(View.VISIBLE);
			}
		} else {

			UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(targetId);
			userInfoLayout = (RelativeLayout) inflater.inflate(R.layout.rc_voip_video_call_user_info, null);
			if (callAction.equals(RongCallAction.ACTION_INCOMING_CALL)) {



				final RelativeLayout rc_voip_call_information =(RelativeLayout) findViewById(R.id.rc_voip_call_information);
				rc_voip_call_information .setBackgroundResource(R.drawable.chatbg);
				buttonLayout = (FrameLayout) inflater.inflate(R.layout.rc_voip_call_bottom_incoming_button_layout, null);
				TextView callInfo = (TextView) userInfoLayout.findViewById(R.id.rc_voip_call_remind_info);
				callInfo.setText(R.string.rc_voip_video_call_inviting);
				onIncomingCallRinging();
				if (userInfo!=null){
					ImageLoader.getInstance().loadImage(userInfo.getPortraitUri().toString(), new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {


						}

						@Override
						public void onLoadingFailed(String imageUri, View view,FailReason failReason) {

						}
						@Override
						public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
							if(loadedImage!=null){

								BitmapBlurUtil.addTask(loadedImage, new Handler(){
									@Override
									public void handleMessage(Message msg) {
										super.handleMessage(msg);
										Drawable drawable = (Drawable) msg.obj;
										rc_voip_call_information.setBackgroundDrawable(drawable);
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
				ImageView answerV = (ImageView)buttonLayout.findViewById(R.id.rc_voip_call_answer_btn);
				answerV.setImageResource(R.drawable.rc_voip_audio);
			}
		}
		mButtonContainer.removeAllViews();
		mButtonContainer.addView(buttonLayout);
		mUserInfoContainer.removeAllViews();
		mUserInfoContainer.addView(userInfoLayout);
	}
	///拨出中
	@Override
	public void onCallOutgoing(RongCallSession callSession, SurfaceView localVideo) {
		super.onCallOutgoing(callSession, localVideo);
		this.callSession = callSession;
		if (callSession.getMediaType().equals(RongCallCommon.CallMediaType.VIDEO)) {
			mLPreviewContainer.setVisibility(View.VISIBLE);
			localVideo.setTag(callSession.getSelfUserId());
			mLPreviewContainer.addView(localVideo);
		}
		onOutgoingCallRinging();
	}
	//会话建立
	@Override
	public void onCallConnected(RongCallSession callSession, SurfaceView localVideo) {
		super.onCallConnected(callSession, localVideo);
		this.callSession = callSession;
		TextView remindInfo = (TextView) mUserInfoContainer.findViewById(R.id.rc_voip_call_remind_info);
		setupTimes(remindInfo,times);


		if (callSession.getMediaType().equals(RongCallCommon.CallMediaType.AUDIO)) {
			findViewById(R.id.rc_voip_call_minimize).setVisibility(View.VISIBLE);
			FrameLayout btnLayout = (FrameLayout) inflater.inflate(R.layout.rc_voip_call_bottom_connected_button_layout, null);
			mButtonContainer.removeAllViews();
			mButtonContainer.addView(btnLayout);
		} else {
			mLocalVideo = localVideo;
			mLocalVideo.setTag(callSession.getSelfUserId());
		}

		RongCallClient.getInstance().setEnableLocalAudio(!muted);
		View muteV = mButtonContainer.findViewById(R.id.rc_voip_call_mute);
		if (muteV != null) {
			muteV.setSelected(muted);
		}

		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		if (audioManager.isWiredHeadsetOn()) {
			RongCallClient.getInstance().setEnableSpeakerphone(false);
		} else {
			RongCallClient.getInstance().setEnableSpeakerphone(handFree);
		}

		View handFreeV = mButtonContainer.findViewById(R.id.rc_voip_handfree);
		if (handFreeV != null) {
			handFreeV.setSelected(handFree);
		}

		stopRing();


		if (persion_main==null) {

		}else {
			if (persion_main.getToken()==null) {;

			Toast.makeText(SingleCallActivity.this, "您的金币不足，将自动关闭。", Toast.LENGTH_SHORT).show();
			LoadDataUpdate();
			if (callSession.getCallId()!=null) {
				RongCallClient.getInstance().hangUpCall(callSession.getCallId()); 
			}
			if (persion_main.getToken()==0) {

				Toast.makeText(SingleCallActivity.this, "您的金币不足，将自动关闭。", Toast.LENGTH_SHORT).show();

				if (callSession.getCallId()!=null) {
					RongCallClient.getInstance().hangUpCall(callSession.getCallId()); 
				} 

			}else {


			}
			}

		}

		if (incalltype==1) {
			vstart();


		}else if (incalltype==2) {

			try {
				Thread.currentThread().sleep(800);//阻断2秒
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			vstart();
			getviews();
			

		}

		if (ehandler!=null) {

			type = SharedPreferencesUtil.getInt(SingleCallActivity.this, "chattype", 0);
			if (type ==1) {
				//						vend();
				ehandler.removeCallbacks(erunnable);
				unregisterVideoFrameObserver();

			}else {

				ehandler = new Handler();  
				erunnable = new Runnable() {  
					public void run() {  


						type = SharedPreferencesUtil.getInt(SingleCallActivity.this, "chattype", 0);

						if (type ==1) {
							//						vend();
							ehandler.removeCallbacks(erunnable);
							unregisterVideoFrameObserver();

						}else {

							vend(chatid);



							new Thread(new Runnable() {
								@Override
								public void run() {
									
									if (Bmp==null) {
										if (agoraVideoFrame==null) {
											
										}else {
											Bmp =rawByteArray2RGBABitmap2(agoraVideoFrame.getyBuffer(),agoraVideoFrame.getuBuffer(),agoraVideoFrame.getvBuffer(),agoraVideoFrame.getWidth(),agoraVideoFrame.getyStride(),agoraVideoFrame.getvStride(), agoraVideoFrame.getWidth(), agoraVideoFrame.getHeight());
											String file=bitmapToBase64(Bmp);
											tophoto(file);
										}
										
										
									}else {
										Bmp=null;
										if (agoraVideoFrame==null) {
											
										}else {
											
											Bmp =rawByteArray2RGBABitmap2(agoraVideoFrame.getyBuffer(),agoraVideoFrame.getuBuffer(),agoraVideoFrame.getvBuffer(),agoraVideoFrame.getWidth(),agoraVideoFrame.getyStride(),agoraVideoFrame.getvStride(), agoraVideoFrame.getWidth(), agoraVideoFrame.getHeight());
											String file=bitmapToBase64(Bmp);
											tophoto(file);
										}
										
									}
								}
							}).start();
							




						
							ehandler.postDelayed(erunnable, 59000);

						}
						//每0.5秒监听一次是否在播放视频  
					}
				};  
				erunnable.run();
			}


		}else {

			ehandler = new Handler();  
			erunnable = new Runnable() {  
				public void run() {  


					type = SharedPreferencesUtil.getInt(SingleCallActivity.this, "chattype", 0);

					if (type ==1) {
						//						vend();
						ehandler.removeCallbacks(erunnable);
						unregisterVideoFrameObserver();

					}else {

						vend(chatid);

						ehandler.postDelayed(erunnable, 59000);

					}
					//每0.5秒监听一次是否在播放视频  
				}
			};  
			erunnable.run();
		}



	}

	public  void vends(){


		if (chatid==0) {

			if (ehandler==null) {

			}else {

				ehandler.removeCallbacks(erunnable);
				unregisterVideoFrameObserver();
			}

		}else {
			//Toast.makeText(SingleCallActivity.this, "onBackPressed", Toast.LENGTH_SHORT);
			vend(chatid);
			RongCallAction callAction = RongCallAction.valueOf(getIntent().getStringExtra("callAction"));
			if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {
				//				Intent	intent = new Intent(SingleCallActivity.this, EvaluateActivity.class);
				//				UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(targetId);
				//				intent.putExtra("img", userInfo.getPortraitUri().toString());
				//				intent.putExtra("chatid", chatid);
				//				startActivity(intent);
			}
			if (ehandler==null) {

			}else {
				ehandler.removeCallbacks(erunnable); 
				unregisterVideoFrameObserver();
			}

			chatid=0;
		}



		//		vend();
	}
	@Override
	protected void onDestroy() {
		RongContext.getInstance().getEventBus().unregister(this);
		super.onDestroy();
	}
	//返回时刷新数据
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		dbutil = new DbUtil((TAApplication) this.getApplication());
		List<Persion>  	users= dbutil.selectData(Persion.class,null);
		if(users !=null)
		{
			if(users.size()>0)
			{
				try {
					persion_main = users.get(0);
					mtargetId =  persion_main.getId()+"";
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onRemoteUserJoined(final String userId, RongCallCommon.CallMediaType mediaType, SurfaceView remoteVideo) {
		super.onRemoteUserJoined(userId, mediaType, remoteVideo);
		if (mediaType.equals(RongCallCommon.CallMediaType.VIDEO)) {
			findViewById(R.id.rc_voip_call_information).setBackgroundColor(getResources().getColor(android.R.color.transparent));
			mLPreviewContainer.setVisibility(View.VISIBLE);
			mLPreviewContainer.removeAllViews();
			remoteVideo.setTag(userId);

			mLPreviewContainer.addView(remoteVideo);
			mLPreviewContainer.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (isInformationShow) {
						hideVideoCallInformation();
					} else {
						showVideoCallInformation();
					}

					if (isInformationShow) {
						Timer timer = new Timer();
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								mHandler.sendEmptyMessage(EVENT_HIDDEN_INFO);
							}
						}, 5 * 1000);
					}
				}
			});
			mSPreviewContainer.setVisibility(View.VISIBLE);
			mSPreviewContainer.removeAllViews();
			if (mLocalVideo != null) {
				mLocalVideo.setZOrderMediaOverlay(true);
				mLocalVideo.setZOrderOnTop(true);
				mSPreviewContainer.addView(mLocalVideo);
			}
			mSPreviewContainer.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SurfaceView fromView = (SurfaceView) mSPreviewContainer.getChildAt(0);
					SurfaceView toView = (SurfaceView) mLPreviewContainer.getChildAt(0);

					mLPreviewContainer.removeAllViews();
					mSPreviewContainer.removeAllViews();
					fromView.setZOrderOnTop(false);
					fromView.setZOrderMediaOverlay(false);
					mLPreviewContainer.addView(fromView);
					toView.setZOrderOnTop(true);
					toView.setZOrderMediaOverlay(true);
					mSPreviewContainer.addView(toView);
				}
			});
			mButtonContainer.setVisibility(View.INVISIBLE);
			mUserInfoContainer.setVisibility(View.INVISIBLE);

		}
	}

	@Override
	public void onMediaTypeChanged(String userId, RongCallCommon.CallMediaType mediaType, SurfaceView video) {
		if (callSession.getSelfUserId().equals(userId))
			showShortToast(getString(R.string.rc_voip_switch_to_audio));
		else{
			showShortToast(getString(R.string.rc_voip_remote_switch_to_audio));
		}

		initAudioCallView();
	}

	private void initAudioCallView() {
		mLPreviewContainer.removeAllViews();
		mLPreviewContainer.setVisibility(View.GONE);
		mSPreviewContainer.removeAllViews();
		mSPreviewContainer.setVisibility(View.GONE);

		findViewById(R.id.rc_voip_call_information).setBackgroundColor(getResources().getColor(R.color.rc_voip_background_color));
		findViewById(R.id.rc_voip_audio_chat).setVisibility(View.GONE);

		View userInfoView = inflater.inflate(R.layout.rc_voip_audio_call_user_info, null);
		TextView timeView = (TextView) userInfoView.findViewById(R.id.rc_voip_call_remind_info);
		setupTimes(timeView,null);

		mUserInfoContainer.removeAllViews();
		mUserInfoContainer.addView(userInfoView);
		UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(targetId);
		if (userInfo != null) {
			TextView userName = (TextView) mUserInfoContainer.findViewById(R.id.rc_voip_user_name);
			userName.setText(userInfo.getName());

			if (callSession.getMediaType().equals(RongCallCommon.CallMediaType.AUDIO)) {
				AsyncImageView userPortrait = (AsyncImageView) mUserInfoContainer.findViewById(R.id.rc_voip_user_portrait);
				if (userPortrait != null) {
					if (userInfo.getPortraitUri().toString()==null) {

						if (persion!=null) {
							userPortrait.setAvatar(persion.getPhoto(), R.drawable.sisterlog);
						}else {
							userPortrait.setAvatar(userInfo.getPortraitUri().toString(), R.drawable.sisterlog);

						}
					}else {
						userPortrait.setAvatar(userInfo.getPortraitUri().toString(), R.drawable.sisterlog);

					}
				}
			}else {

				AsyncImageView userPortrait = (AsyncImageView) mUserInfoContainer.findViewById(R.id.rc_voip_user_portrait);
				if (userPortrait != null) {
					if (userInfo.getPortraitUri().toString()==null) {

						if (persion!=null) {
							userPortrait.setAvatar(persion.getPhoto(), R.drawable.sisterlog);
						}else {
							userPortrait.setAvatar(userInfo.getPortraitUri().toString(), R.drawable.sisterlog);

						}
					}else {
						userPortrait.setAvatar(userInfo.getPortraitUri().toString(), R.drawable.sisterlog);

					}
				}

			}
		}
		mUserInfoContainer.setVisibility(View.VISIBLE);
		mUserInfoContainer.findViewById(R.id.rc_voip_call_minimize).setVisibility(View.VISIBLE);

		View button = inflater.inflate(R.layout.rc_voip_call_bottom_connected_button_layout, null);
		mButtonContainer.removeAllViews();
		mButtonContainer.addView(button);
		mButtonContainer.setVisibility(View.VISIBLE);
		View handFreeV = mButtonContainer.findViewById(R.id.rc_voip_handfree);
		handFreeV.setSelected(handFree);
	}
	// 挂断按钮监听
	public void onHangupBtnClick(View view) {
		RongCallClient.getInstance().hangUpCall(callSession.getCallId());
		stopRing();
		if (chatid==0) {

			if (ehandler==null) {


			}else {
				ehandler.removeCallbacks(erunnable);
				unregisterVideoFrameObserver();
			}


		}else {
			//Toast.makeText(SingleCallActivity.this, "onBackPressed", Toast.LENGTH_SHORT);
			vend(chatid);
			RongCallAction callAction = RongCallAction.valueOf(getIntent().getStringExtra("callAction"));
			if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {
				if (chatid==0) {

				}else {
					Intent	intent = new Intent(SingleCallActivity.this, EvaluateActivity.class);
					UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(targetId);
					intent.putExtra("img", userInfo.getPortraitUri().toString());
					intent.putExtra("chatid", chatid);
					startActivity(intent);
				}

			}
			if (ehandler==null) {

			}else {
				ehandler.removeCallbacks(erunnable); 
				unregisterVideoFrameObserver();
			}

			chatid=0;
		}


	}
	//接听按钮监听
	public void onReceiveBtnClick(View view) {
		RongCallClient.getInstance().acceptCall(callSession.getCallId());
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (msg != null && msg.what == EVENT_HIDDEN_INFO) {
			hideVideoCallInformation();
		}
		return false;
	}

	//mButtonContainer隐藏
	public void hideVideoCallInformation() {
		isInformationShow = false; 
		mUserInfoContainer.setVisibility(View.GONE);
		mButtonContainer.setVisibility(View.INVISIBLE);
		findViewById(R.id.rc_voip_audio_chat).setVisibility(View.GONE);
	}
	// mButtonContainer显示
	public void showVideoCallInformation() {

		isInformationShow = true;
		mUserInfoContainer.setVisibility(View.VISIBLE);
		mUserInfoContainer.findViewById(R.id.rc_voip_call_minimize).setVisibility(View.VISIBLE);
		rc_voip_liiwu.setVisibility(View.VISIBLE);
		mButtonContainer.setVisibility(View.VISIBLE);
		FrameLayout btnLayout = (FrameLayout) inflater.inflate(R.layout.rc_voip_call_bottom_connected_button_layout, null);
		btnLayout.findViewById(R.id.rc_voip_call_mute).setSelected(muted);
		btnLayout.findViewById(R.id.rc_voip_handfree).setVisibility(View.GONE);
		btnLayout.findViewById(R.id.rc_voip_camera).setVisibility(View.VISIBLE);
		mButtonContainer.removeAllViews();
		mButtonContainer.addView(btnLayout);
		View view = findViewById(R.id.rc_voip_audio_chat);
		view.setVisibility(View.GONE);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RongCallClient.getInstance().changeCallMediaType(RongCallCommon.CallMediaType.AUDIO);
				initAudioCallView();
			}
		});
	}

	public void onHandFreeButtonClick(View view) {
		RongCallClient.getInstance().setEnableSpeakerphone(!view.isSelected());
		view.setSelected(!view.isSelected());
		handFree = view.isSelected();
	}

	//禁音
	public void onMuteButtonClick(View view) {
		RongCallClient.getInstance().setEnableLocalAudio(view.isSelected());
		view.setSelected(!view.isSelected());
		muted = view.isSelected();
	}

	@Override
	public void onCallDisconnected(RongCallSession callSession, RongCallCommon.CallDisconnectedReason reason) {
		super.onCallDisconnected(callSession, reason);

		String senderId;
		String extra = "";
		stopRing();

		if (callSession == null) {
			RLog.e(TAG, "onCallDisconnected. callSession is null!");
			postRunnableDelay(new Runnable() {
				@Override
				public void run() {
					ehandler.removeCallbacks(erunnable); 
					unregisterVideoFrameObserver();
					chatid=0;
					finish();
				}
			});
			return;
		}
		senderId = callSession.getInviterUserId();
		switch (reason) {
		case HANGUP:
		case REMOTE_HANGUP:
			int time = getTime();
			if (time >= 3600) {
				extra = String.format("%d:%02d:%02d", time / 3600, (time % 3600) / 60, (time % 60));
			} else {
				extra = String.format("%02d:%02d", (time % 3600) / 60, (time % 60));
			}
			break;
		}

		if (!TextUtils.isEmpty(senderId)) {

			CallSTerminateMessage message = new CallSTerminateMessage();
			message.setReason(reason);
			message.setMediaType(callSession.getMediaType());
			message.setExtra(extra);
			if (senderId.equals(callSession.getSelfUserId())) {
				message.setDirection("MO");
			} else {
				message.setDirection("MT");
			}

			RongIM.getInstance().insertMessage(Conversation.ConversationType.PRIVATE, callSession.getTargetId(), senderId, message, null);

		}
		postRunnableDelay(new Runnable() {
			@Override
			public void run() {
				if (ehandler==null) {

				}else {
					ehandler.removeCallbacks(erunnable);
					unregisterVideoFrameObserver();
				}

				chatid=0;
				finish();

			}
		});
	}

	@Override
	public void onRestoreFloatBox(Bundle bundle) {
		super.onRestoreFloatBox(bundle);
		if (bundle == null)
			return;
		muted = bundle.getBoolean("muted");
		handFree = bundle.getBoolean("handFree");
		if (bundle!=null) {
			chatid = bundle.getInt("chatid");


			incalltype = bundle.getInt("incalltype");
		}



		callSession = RongCallClient.getInstance().getCallSession();
		RongCallCommon.CallMediaType mediaType = callSession.getMediaType();
		RongCallAction callAction = RongCallAction.valueOf(getIntent().getStringExtra("callAction"));
		inflater = LayoutInflater.from(this);
		targetId = callSession.getTargetId();
		initView(mediaType, callAction,targetId);

		UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(targetId);
		if (userInfo != null) {
			TextView userName = (TextView) mUserInfoContainer.findViewById(R.id.rc_voip_user_name);
			userName.setText(userInfo.getName());
			if (mediaType.equals(RongCallCommon.CallMediaType.AUDIO)) {
				AsyncImageView userPortrait = (AsyncImageView) mUserInfoContainer.findViewById(R.id.rc_voip_user_portrait);
				if (userPortrait != null) {
					if (userInfo.getPortraitUri().toString()==null) {

						if (persion!=null) {
							userPortrait.setAvatar(persion.getPhoto(), R.drawable.sisterlog);
						}else {
							userPortrait.setAvatar(userInfo.getPortraitUri().toString(), R.drawable.sisterlog);

						}
					}else {
						userPortrait.setAvatar(userInfo.getPortraitUri().toString(), R.drawable.sisterlog);

					}
				}
			}else {
				AsyncImageView userPortrait = (AsyncImageView) mUserInfoContainer.findViewById(R.id.rc_voip_user_portrait);
				if (userPortrait != null) {
					if (userInfo.getPortraitUri().toString()==null) {

						if (persion!=null) {
							userPortrait.setAvatar(persion.getPhoto(), R.drawable.sisterlog);
						}else {
							userPortrait.setAvatar(userInfo.getPortraitUri().toString(), R.drawable.sisterlog);

						}
					}else {
						userPortrait.setAvatar(userInfo.getPortraitUri().toString(), R.drawable.sisterlog);

					}
				}
			}
		}
		SurfaceView localVideo = null;
		SurfaceView remoteVideo = null;
		String remoteUserId = null;
		for (CallUserProfile profile : callSession.getParticipantProfileList()) {
			if (profile.getUserId().equals(RongIMClient.getInstance().getCurrentUserId())) {
				localVideo = profile.getVideoView();
			} else {
				remoteVideo = profile.getVideoView();
				remoteUserId = profile.getUserId();
			}
		}
		if (localVideo != null && localVideo.getParent() != null) {
			((ViewGroup)localVideo.getParent()).removeView(localVideo);
		}
		onCallOutgoing(callSession, localVideo);
		onCallConnected(callSession, localVideo);
		if (remoteVideo != null && remoteVideo.getParent() != null) {
			((ViewGroup)remoteVideo.getParent()).removeView(remoteVideo);
		}
		onRemoteUserJoined(remoteUserId, mediaType, remoteVideo);
	}

	@Override
	public String onSaveFloatBoxState(Bundle bundle) {
		super.onSaveFloatBoxState(bundle);
//		vend(chatid);//处理扣钱多一次问题
		callSession = RongCallClient.getInstance().getCallSession();
		bundle.putBoolean("muted", muted);
		bundle.putBoolean("handFree", handFree);
		bundle.putInt("chatid", chatid);
		bundle.putInt("incalltype", incalltype);
		bundle.putInt("mediaType", callSession.getMediaType().getValue());
		return getIntent().getAction();
	}

	public void onMinimizeClick (View view) {

		finish();
	}

	public void onSwitchCameraClick (View view) {
		RongCallClient.getInstance().switchCamera();

	}


	@Override
	public void onBackPressed() {
		List<CallUserProfile> participantProfiles = callSession.getParticipantProfileList();
		RongCallCommon.CallStatus callStatus = null;
		for (CallUserProfile item : participantProfiles) {
			if (item.getUserId().equals(callSession.getSelfUserId())) {
				callStatus = item.getCallStatus();
				break;
			}
		}

		if (callStatus != null && callStatus.equals(RongCallCommon.CallStatus.CONNECTED)) {

			super.onBackPressed();



		} else {
			RongCallClient.getInstance().hangUpCall(callSession.getCallId());
			if (chatid==0) {
				ehandler.removeCallbacks(erunnable); 
				unregisterVideoFrameObserver();
			}else {

				//Toast.makeText(SingleCallActivity.this, "onBackPressed", Toast.LENGTH_SHORT);

				vend(chatid);	
				RongCallAction callAction = RongCallAction.valueOf(getIntent().getStringExtra("callAction"));
				if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {

					Intent	intent = new Intent(SingleCallActivity.this, EvaluateActivity.class);
					UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(targetId);
					intent.putExtra("img", userInfo.getPortraitUri().toString());
					intent.putExtra("chatid", chatid);
					startActivity(intent);

				}
				ehandler.removeCallbacks(erunnable); 
				unregisterVideoFrameObserver();
				chatid=0;
			}

		}
	}

	public void onEventMainThread(UserInfo userInfo) {
		if (targetId != null && targetId.equals(userInfo.getUserId())) {
			TextView userName = (TextView) mUserInfoContainer.findViewById(R.id.rc_voip_user_name);
			if (userInfo.getName() != null)
				userName.setText(userInfo.getName());
			AsyncImageView userPortrait = (AsyncImageView) mUserInfoContainer.findViewById(R.id.rc_voip_user_portrait);

			if (userPortrait==null) {



			}else {
				if (userPortrait != null && userInfo.getPortraitUri() != null) {

					if (userInfo.getPortraitUri().toString()==null) {

						if (persion!=null) {
							userPortrait.setResource(persion.getPhoto(), R.drawable.sisterlog);
						}else {
							userPortrait.setResource(userInfo.getPortraitUri().toString(), R.drawable.sisterlog);

						}
					}else {
						userPortrait.setResource(userInfo.getPortraitUri().toString(), R.drawable.sisterlog);

					}

					//userPortrait.setResource(userInfo.getPortraitUri().toString(), R.drawable.sisterlog);
				}
			}

		}
	}
	public  void getview() {
		// TODO Auto-generated method stub



		final Handler	handlers = new Handler();  
		final Runnable 	runnables = new Runnable() {  
			public void run() {  

				if (mLocalVideo!=null) {
					//
					//				WindowManager windowManager = getWindowManager();    
					//				Display display = windowManager.getDefaultDisplay();    
					//				int w = display.getWidth();    
					//				int h = display.getHeight();    
					//				Bmp = Bitmap.createBitmap( w, h, Config.ARGB_8888 );    
					//				Bmp =getbitmap();
					//				//2.获取屏幕    
					//				decorview = getWindow().getDecorView();     
					//				decorview.setDrawingCacheEnabled(true);   
					//
					//
					//
					//				//      Bmp = decorview.getDrawingCache();  
					//				Bmp = savaScreenShot(1);

					//								View vv = getWindow().getDecorView().getRootView();
					//								//View vv = mLocalVideo.getRootView();;
					//								// 设置属性
					//								vv.setDrawingCacheEnabled(true);
					//								//Bitmap	bm = vv.getDrawingCache();
					//								Bitmap	bm = snapShotWithoutStatusBar(SingleCallActivity.this);
					getbitmap();
					//				try {
					//					//String file = encodeBase64File(tempFile);
					//					String file=bitmapToBase64(Bmp);
					//					tophoto(file);
					//				} catch (Exception e) {
					//					// TODO Auto-generated catch block
					//					e.printStackTrace();
					//				}



					//				 


					//					mLocalVideo  
					//					// 取得位图
					//					Bitmap	bm = vv.getDrawingCache();

					//getBitmap(bm);
					//					Canvas canvas=new Canvas(bm);
					//					canvas.drawBitmap(curBitmap, 0, 0, null);

				}

				//handlers.postDelayed(runnables, 60000);//每0.5秒监听一次是否在播放视频  
			}

		};  
		//		runnable.run();
	}
	//系统截图
	@SuppressLint("NewApi")
	private void getbitmap() {


		startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);




		// TODO Auto-generated method stub
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_MEDIA_PROJECTION) {
			if (resultCode != Activity.RESULT_OK) {
				return;
			}else if(data != null && resultCode != 0){
				if (mMediaProjection==null) {


					mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
					mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
							windowWidth, windowHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
							mImageReader.getSurface(), null, null);
				}else {

					mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
							windowWidth, windowHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
							mImageReader.getSurface(), null, null);
				}  	
				nameImage = pathImage+strDate+".png";

				Image image = mImageReader.acquireLatestImage();
				int width = image.getWidth();
				int height = image.getHeight();
				final Image.Plane[] planes = image.getPlanes();
				final ByteBuffer buffer = planes[0].getBuffer();
				int pixelStride = planes[0].getPixelStride();
				int rowStride = planes[0].getRowStride();
				int rowPadding = rowStride - pixelStride * width;
				Bmp  = Bitmap.createBitmap(width+rowPadding/pixelStride, height, Bitmap.Config.ARGB_8888);
				Bmp.copyPixelsFromBuffer(buffer);
				Bmp = Bitmap.createBitmap(Bmp, 0, 0,width, height);

				try {
					//String file = encodeBase64File(tempFile);
					String file=bitmapToBase64(Bmp);
					tophoto(file);
					Bmp=null;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}    	
			}
		}
	}
	private void tophoto(final String file) {
		// TODO Auto-generated method stub


		new Thread(new Runnable() {
			@Override
			public void run() {
				
				if (chatid==0) {
				    int chatids = SharedPreferencesUtil.getInt(SingleCallActivity.this, "chatid", 0);
					chatid=	chatids;
				}else {
					String jhuser = Chat_Service.upcutphoto(chatid,file);
					jhuser.length();
				}
				
				//  				Message msg = mUIHandler.obtainMessage(2);
				//  				msg.obj = jhuser;
				//	msg.sendToTarget();
			}
		}).start();
	}
	/** 
	 * bitmap转为base64 
	 * @param bitmap 
	 * @return 
	 */  
	public static String bitmapToBase64(Bitmap bitmap) {  

		String result = null;  
		ByteArrayOutputStream baos = null;  
		try {  
			if (bitmap != null) { 

				baos = new ByteArrayOutputStream();  
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);  
				baos.flush();  
				baos.close();  
				byte[] bitmapBytes = baos.toByteArray();  
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);  

			}  
		} catch (IOException e) {  
			e.printStackTrace();  
		} finally {  
			try {  
				if (baos != null) {

					baos.flush();  
					baos.close(); 

				}  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}  
		return result;  
	}  
	public static int getAndroidSDKVersion() { 
		int version = 0; 
		try { 
			version = Integer.valueOf(android.os.Build.VERSION.SDK); 
		} catch (NumberFormatException e) { 
			Log.e("sdjksnd",e.toString());
		} 
		return version; 
	}
	private void vstart() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String	type;
				RongCallCommon.CallMediaType mediaType = callSession.getMediaType();
				if (mediaType.equals(RongCallCommon.CallMediaType.AUDIO)) {
					type ="voice";
				}else {
					type ="video";
				}
				String Rankingmap = null;
				if (incalltype==1) {
					Rankingmap = Chat_Service.vstart(mtargetId, targetId,type );
				}else if (incalltype==2) {
					Rankingmap = Chat_Service.vstart(targetId, mtargetId,type );

				}

				android.os.Message msg = mUIHandler.obtainMessage(1);

				msg.obj = Rankingmap;
				msg.sendToTarget();				
			}
		}).start();
	}


	public void vend(final int chatid) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String	type;
				RongCallCommon.CallMediaType mediaType = callSession.getMediaType();
				if (mediaType.equals(RongCallCommon.CallMediaType.AUDIO)) {
					type ="voice";
				}else {
					type ="video";
				}
				String Rankingmap = null;
				if (incalltype==1) {
					Rankingmap = Chat_Service.vcutmoney(chatid);
				}else if (incalltype==2) {
					Rankingmap = Chat_Service.vcutmoney(chatid);
				}

				android.os.Message msg = mUIHandler.obtainMessage(2);
				msg.obj = Rankingmap;
				msg.sendToTarget();				
			}
		}).start();
	}
	public void LoadStrangerData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// dialog.show();
				Message msg = mUIHandler.obtainMessage(5);
				try {


					if (persion==null) {

						msg.obj = Person_Service.addpaste(persion_main.getId(),Integer.valueOf(targetId).intValue());

					}else {
						msg.obj = Person_Service.addpaste(persion_main.getId(),persion.getId());

					}
				} catch (Exception e) {
					msg.obj = null;
				}
				msg.sendToTarget();
			}
		}).start();
	}
	public void unsubscribe(){
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// dialog.show();
				Message msg = mUIHandler.obtainMessage(6);
				try {
					if (islike!=0) {
						msg.obj = Person_Service.unsubscribe(islike);
					}else {
						msg.obj = Person_Service.unsubscribe(islike);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					msg.obj = null;
				}
				msg.sendToTarget();
			}
		}).start();
	}

	private Handler mUIHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if(msg.obj!=null)
				{

					/**
					 * 
					 * 异常 = error
					 对方被禁用 = sell_error
					 自己被禁用 = buy_error
					 对方不开启语音 = sell_novoice
					 对方不开启视频 = sell_novideo
					 自己与对方有未处理的语音消费 = voice_noend正常情况不会出现此反馈
					 自己与对方有未处理的视频消费 = video_noend正常情况不会出现此反馈
					 */
					String mes = (String) msg.obj;

					if("".equals(mes)){

						Toast.makeText(SingleCallActivity.this, "网络异常，请稍后再试", Toast.LENGTH_SHORT).show();
					}if (mes.contains("error")) {
						//Toast.makeText(SingleCallActivity.this, "网络异常，请稍后再试", Toast.LENGTH_SHORT).show();
					}else if (mes.contains("sell_error")) {
						Toast.makeText(SingleCallActivity.this, "对方被禁用", Toast.LENGTH_SHORT).show();
						RongCallClient.getInstance().hangUpCall(callSession.getCallId());;
					}else if (mes.contains("buy_error")) {
						Toast.makeText(SingleCallActivity.this, "自己被禁用", Toast.LENGTH_SHORT).show();
						RongCallClient.getInstance().hangUpCall(callSession.getCallId());
					}else if (mes.contains("sell_novoice")) {
						Toast.makeText(SingleCallActivity.this, "对方不开启语音", Toast.LENGTH_SHORT).show();
						RongCallClient.getInstance().hangUpCall(callSession.getCallId());
					}else if (mes.contains("sell_novideo")) {
						Toast.makeText(SingleCallActivity.this, "对方不开启视频", Toast.LENGTH_SHORT).show();
						RongCallClient.getInstance().hangUpCall(callSession.getCallId());
					}else if (mes.contains("voice_noend")) {
						Toast.makeText(SingleCallActivity.this, "自己与对方有未处理的语音消费", Toast.LENGTH_SHORT).show();
						RongCallClient.getInstance().hangUpCall(callSession.getCallId());
					}else if (mes.contains("video_noend")) {
						Toast.makeText(SingleCallActivity.this, "自己与对方有未处理的视频消费", Toast.LENGTH_SHORT).show();
						RongCallClient.getInstance().hangUpCall(callSession.getCallId());
					}else {


						chatid=Integer.valueOf(mes).intValue();
						SharedPreferencesUtil.putInt(SingleCallActivity.this, "chatid", chatid);
						/**
						 * 
						 * 异常 = error
                                                     对方被禁用 = sell_error
                                                     自己被禁用 = buy_error
                                                     对方不开启语音 = sell_novoice
                                                     对方不开启视频 = sell_novideo
                                                     自己与对方有未处理的语音消费 = voice_noend正常情况不会出现此反馈
                                                    自己与对方有未处理的视频消费 = video_noend正常情况不会出现此反馈
                                                     成功 = 语音视频的订单号id   int  类型
						 */
						//						Persion p = JsonUtil.JsonToObj(mes, Persion.class);
						//						persion =p;
						//						token = persion.getToken();
					}


				}else {


				}

				break;
			case 2:
				if (msg.obj != null) {
					String mes = (String) msg.obj;

					if (mes.contains("error")) {

					}else if (mes.contains("sell_error")) {

					}else if (mes.contains("buy_error")) {

					}else if (mes.contains("buy_nomoney")) {


						Toast.makeText(SingleCallActivity.this, "金币不足,将关闭连接", Toast.LENGTH_SHORT).show();
						LoadDataUpdate();
						RongCallClient.getInstance().hangUpCall(callSession.getCallId());
						RongCallAction callAction = RongCallAction.valueOf(getIntent().getStringExtra("callAction"));
						if (chatid==0) {

						}else {
							if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {
								Intent	intent = new Intent(SingleCallActivity.this, EvaluateActivity.class);
								UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(targetId);
								intent.putExtra("img", userInfo.getPortraitUri().toString());
								intent.putExtra("chatid", chatid);
								startActivity(intent);
							}
						}



					}else if (mes.contains(" sell_free")) {


					}else {

						if (mes.equals("")) {

						}else {
							tonken=Integer.valueOf(mes).intValue(); 

							if (tonken==0) {

								if (incalltype==2) {

								}else {

									Toast.makeText(SingleCallActivity.this, "金币不足,将关闭连接", Toast.LENGTH_SHORT).show();
									LoadDataUpdate();
									RongCallClient.getInstance().hangUpCall(callSession.getCallId());  
									RongCallAction callAction = RongCallAction.valueOf(getIntent().getStringExtra("callAction"));
									if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {

										if (chatid==0) {

										}else {

											Intent	intent = new Intent(SingleCallActivity.this, EvaluateActivity.class);
											UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(targetId);
											intent.putExtra("img", userInfo.getPortraitUri().toString());
											intent.putExtra("chatid", chatid);
											startActivity(intent);
										}

									}

								}

							}else {


								if (paytoken==0) {


								}else {
									if (tonken<paytoken) {
										if (incalltype==2) {

										}else {


											Toast.makeText(SingleCallActivity.this, "金币不足,将关闭连接", Toast.LENGTH_SHORT).show();

											LoadDataUpdate();


											RongCallClient.getInstance().hangUpCall(callSession.getCallId());  
											RongCallAction callAction = RongCallAction.valueOf(getIntent().getStringExtra("callAction"));
											if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {

												if (chatid==0) {

												}else {
													Intent	intent = new Intent(SingleCallActivity.this, EvaluateActivity.class);
													UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(targetId);
													intent.putExtra("img", userInfo.getPortraitUri().toString());
													intent.putExtra("chatid", chatid);
													startActivity(intent);
												}

											}

										}
									}else {

									}
								}
							}
						}


					}
				} else{
					/**
					 * 
					 * 异常 = error
                                                       对方被禁用 = sell_error
                                                       自己被禁用 = buy_error
                                                      自己金币不足 = buy_nomoney 
                                                      对方聊天不收费 = sell_free （当返回该值时，此次聊天中不再请求接口）
                                                        成功 = 返回自己的剩余金币   int  类型
					 */
					Toast.makeText(SingleCallActivity.this, "金币不足,将关闭连接", Toast.LENGTH_SHORT).show();
					LoadDataUpdate();
					RongCallClient.getInstance().hangUpCall(callSession.getCallId());  
					RongCallAction callAction = RongCallAction.valueOf(getIntent().getStringExtra("callAction"));
					if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {
						if (chatid==0) {

						}else {

							Intent	intent = new Intent(SingleCallActivity.this, EvaluateActivity.class);
							UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(targetId);
							intent.putExtra("img", userInfo.getPortraitUri().toString());
							intent.putExtra("chatid", chatid);
							startActivity(intent);
						}
					}


				}
				break;
			case 3:
				if (msg.obj != null) {
					String str = (String)msg.obj;

					if (str.equals("error")||str.equals("")) {

						Toast .makeText(SingleCallActivity.this, "网络连接失败，请检查您的网络连接", Toast.LENGTH_SHORT).show();

					}else {


						presents = new ArrayList<Present>();
						presents = JsonUtil.JsonToObj(str, new TypeToken<List<Present>>(){}.getType());
						persentAdapter = new PersentAdapter(SingleCallActivity.this, presents, 1);
						//forumAllCateAdapter = new ForumAllCateAdapter(ForumActivity.this, fcategorys,1);

					}
					//loadingDialog.dismiss();
				}
				break;
			case 4:
				//   异常 = error
				//		           对方被禁用 = sell_error
				//		           自己被禁用 = buy_error
				//		          自己金币不足 = buy_nomoney
				//		          成功 = 返回自己的剩余金币   int  类型
				if (msg.obj != null) {
					String mes = (String) msg.obj;


					if("".equals(mes)){
						//	ToastShow("网络异常，请检查网络连接");
					}else if (mes.contains("sell_error")) {

					}else if (mes.contains("buy_error")) {

					}else if (mes.contains("buy_nomoney")) {

						Toast.makeText(SingleCallActivity.this, "金币不足,将关闭连接", Toast.LENGTH_SHORT).show();
						LoadDataUpdate();

						RongCallClient.getInstance().hangUpCall(callSession.getCallId());
						RongCallAction callAction = RongCallAction.valueOf(getIntent().getStringExtra("callAction"));
						if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {
							if (chatid==0) {

							}else {


								Intent	intent = new Intent(SingleCallActivity.this, EvaluateActivity.class);
								UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(targetId);
								intent.putExtra("img", userInfo.getPortraitUri().toString());
								intent.putExtra("chatid", chatid);
								startActivity(intent);
							}
						}


					}else {


						tonken=Integer.valueOf(mes).intValue();
						sendTextBlackMessage("消费了"+catetoken+"金币", 2);
						Toast.makeText(SingleCallActivity.this,"发送成功", Toast.LENGTH_SHORT).show();
						if (paytoken==0) {

						}else {
							if (tonken<paytoken) {
								Toast.makeText(SingleCallActivity.this, "金币不足,将关闭连接", Toast.LENGTH_SHORT).show();
								LoadDataUpdate();
								RongCallClient.getInstance().hangUpCall(callSession.getCallId()); 
								RongCallAction callAction = RongCallAction.valueOf(getIntent().getStringExtra("callAction"));
								if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {
									if (chatid==0) {

									}else {
										Intent	intent = new Intent(SingleCallActivity.this, EvaluateActivity.class);
										UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(targetId);
										intent.putExtra("img", userInfo.getPortraitUri().toString());
										intent.putExtra("chatid", chatid);
										startActivity(intent);	
									}




								}

							}else {

							}
						}


						//ToastShow(mes+ip);
					}
				}
				break;
			case 5:
				if (msg.obj != null) {
					String json = (String) msg.obj;
					if ( json.equals(JsonUtil.ObjToJson(Constant.FAIL))) {

					}else if (json.contains("exist")) {
						Toast.makeText(SingleCallActivity.this, "您已关注过了", Toast.LENGTH_SHORT).show();
						yiguangzhu.setVisibility(View.VISIBLE);
						guangzhuv.setVisibility(View.GONE);

					}else {

						try {
							JSONObject jsonint = new JSONObject(json);
							islike = jsonint.getInt("isLike");
							int ltype = SharedPreferencesUtil.getInt(SingleCallActivity.this, "ltype", 0);
							if (ltype==1) {

							}else {
								SharedPreferencesUtil.putInt(SingleCallActivity.this, "ltype", 1);
							}
							guangzhuv.setVisibility(View.GONE);
							yiguangzhu.setVisibility(View.VISIBLE);
							Toast.makeText(SingleCallActivity.this, "关注成功", Toast.LENGTH_SHORT).show();

						} catch (JSONException e) {
							// TODO Auto-generated catch block

						}

					}
				} else {

				}
				break;

			case 6:
				if (msg.obj != null) {
					String json = (String) msg.obj;
					if (json.equals(JsonUtil.ObjToJson(Constant.SUCCESS))) {
						islike=0;
						guangzhuv.setVisibility(View.VISIBLE);
						yiguangzhu.setVisibility(View.GONE);

					}else { 

					}
				} else {

				}
				break;

			case 13:
				if (msg.obj != null){
					String mes = (String) msg.obj;
					if ("".equals(mes)) {
						//ToastShow("请求网络超时");
					} else {
						Persion persions = JsonUtil.JsonToObj(mes, Persion.class);
						Log.e("persion", mes.toString());
						RongIM.getInstance().refreshUserInfoCache(new UserInfo(persions.getId() + "",
								persions.getNick_name(), Uri.parse(persions.getPhoto())));


					}
				}																																																																																																																																																				
				//				if(first){
				//					InitView();
				//					first = false;
				//				}else{
				//					RefreshData();
				//				}
				break;
			case 14:
				if (msg.obj != null){
					String mes = (String) msg.obj;
					if ("".equals(mes)) {
						//ToastShow("请求网络超时");
					} else {
						persion = JsonUtil.JsonToObj(mes, Persion.class);
						Log.e("persion", mes.toString());
						if(persion.getIsLike()!=null){
							islike = persion.getIsLike();

						}else {  



						}



						if (callSession==null) {

						}else {
							RongCallCommon.CallMediaType mediaType = callSession.getMediaType();
							if (mediaType.equals(RongCallCommon.CallMediaType.AUDIO)) { 
								paytoken = Integer.valueOf(persion.getVoice_money()).intValue();
							}else {
								paytoken = Integer.valueOf(persion.getVideo_money()).intValue();
							}
						}

					}
				}																																																																																																																																																				
				//				if(first){
				//					InitView();
				//					first = false;
				//				}else{
				//					RefreshData();
				//				}
				break;
			default:
				break;
			}
		};
	};
	/**
	 *请求礼物
	 *
	 * 
	 */
	private void genpresent() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() { 
				// TODO Auto-generated method stub
				String json =Chat_Service.getpresent();

				android.os.Message  msg = mUIHandler.obtainMessage(3);
				msg.obj = json;
				msg.sendToTarget();		
			}
		}).start();

	}
	/**
	 * 发送礼物
	 * @param buy_id
	 * @param sell_id
	 * @param_token
	 * @param id
	 */
	private void sendpresent(final String buy_id,final String sell_id,final int id) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				String json =Chat_Service.sendpresent(buy_id, sell_id, id);
				android.os.Message msg = mUIHandler.obtainMessage(4);
				msg.obj = JsonUtil.JsonToObj(json, String.class);
				msg.sendToTarget();
			}
		}).start();
	}

	public void LoadDataUpdate() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<Persion> list = ManageDataBase.SelectList(dbutil, Persion.class, null);
				if (list.size() > 0) {
					Persion bn = list.get(0);
					String str = Person_Service.loginupdage(bn.getId());
					android.os.Message msg = mUIHandlerres.obtainMessage(1);
					msg.obj = str;
					msg.sendToTarget();
				}
			}
		}).start();
	}

	private Handler mUIHandlerres = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {

			case 1:
				if (msg.obj != null && !msg.obj.equals("")) {
					String str = (String) msg.obj;
					if (!str.equals("") && !str.equals(JsonUtil.ObjToJson(Constant.ERROR))
							&& !str.equals(JsonUtil.ObjToJson("param_error"))) {
						try {
							// new
							// ShowContentView(HomeDetailActivity.this,ll,str,3);
							Persion p1 = JsonUtil.JsonToObj(str, Persion.class);

							if (persion_main.getId().intValue()== p1.getId().intValue()) {

								ManageDataBase.Delete(dbutil, Persion.class, null);
								ManageDataBase.Insert(dbutil, Persion.class, p1);
								persion_main = p1;
							}
						} catch (Exception e) {
							LoadDataUpdate();
							//new ShowContentView(HomeDetailActivity.this, ll, str, 3);
							// TODO: handle exception
							// SharedPreferencesUtil.putInt(HomeDetailActivity.this,
							// "sxdate", 0);
						}

					}
				}
				break;
			default:
				break;
			}
		}
	};
	/**
	 * 
	 *礼物选取界面
	 * 
	 **/
	public class PopupWindows extends PopupWindow {


		public PopupWindows() {
			// TODO Auto-generated method stub

			dismiss();

		}

		public PopupWindows(Context mContext, View parent,int type,final int vioce) {


			if (type==1) {

				View view=null;
				view = View
						.inflate(mContext, R.layout.activity_demo, null);
				view.startAnimation(AnimationUtils.loadAnimation(mContext,
						R.anim.filter));
				setWidth(LayoutParams.FILL_PARENT);
				setHeight(LayoutParams.FILL_PARENT);
				setFocusable(true);
				setBackgroundDrawable(new BitmapDrawable());
				setOutsideTouchable(true);	
				setContentView(view);
				showAtLocation(parent, Gravity.BOTTOM, 0, 0);
				showAsDropDown(parent);  //显示到那个视图之下
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
						WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); 
				ViewPager	mPager = (ViewPager) view.findViewById(R.id.viewpager);
				LinearLayout  mLlDot = (LinearLayout)view. findViewById(R.id.ll_dot);
				RelativeLayout giftout =(RelativeLayout)view. findViewById(R.id.giftout);
				TextView  chunzhi = (TextView)view. findViewById(R.id.chunzhi);
				ImageView  giftguan = (ImageView)view. findViewById(R.id.giftguan);
				inflater = LayoutInflater.from(SingleCallActivity.this);
				//总的页数=总数/每页数量，并取整
				pageCount = (int) Math.ceil(presents.size() * 1.0 / pageSize);
				mPagerList = new ArrayList<View>();

				for (int i = 0; i < pageCount; i++) {
					// 每个页面都是inflate出一个新实例
					GridView gridView = (GridView) inflater.inflate(R.layout.gridview, mPager, false);
					gridView.setAdapter(new GridViewAdapter(SingleCallActivity.this, i, pageSize,presents));
					mPagerList.add(gridView);

					gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							int pos = position + curIndex * pageSize;

							// TODO Auto-generated method stub

							// TODO Auto-generated method stub
							if (checkNetworkState()==false) {

								Toast.makeText(SingleCallActivity.this, "当前网络已断开，请网络连接后再送礼物吧。", Toast.LENGTH_SHORT).show();

							}else {

								//							if (psentext.getText().length()==0) {
								//								Toast.makeText(ECChattingActivity.this, "您还没有输入想说的话", Toast.LENGTH_SHORT).show();
								//
								//							}else {
								if (persion_main.getRongyuntoken()==null) {
									Toast.makeText(SingleCallActivity.this, "对方还没有融云账号，不能送礼给她", Toast.LENGTH_SHORT).show();
									//ToastShow("对方还没有融云账号，不能送礼给她");
								}else {
									cates =presents.get(pos);
									catetoken=cates.getPrice();


									if (tonken==0) {
										tonken=	persion_main.getToken();
									}else {

									}
									if (tonken<catetoken) {
										Toast.makeText(SingleCallActivity.this, "您的金币值不够，请充值或者选其他的礼物吧。", Toast.LENGTH_SHORT).show();
										//ToastShow("您的爱意值不够，请充值或者选其他的礼物吧。");
									}else {


										//							if (psentext.getText().length()==0) {
										//								Toast.makeText(ECChattingActivity.this, "您还没有输入想说的话", Toast.LENGTH_SHORT).show();
										//
										//							}else {
										//								if (persion_main.getToken()<catetoken) {
										//
										//									Toast.makeText(UserActivity.this, "您的爱意值不够，请充值或者选其他的礼物吧。", Toast.LENGTH_SHORT).show();
										//
										//								}else {


										new Thread(new Runnable() {
											@Override
											public void run() {
												//
												//
												//												if (cates.getResult_img()==null) {
												photofile=	getphpto(cates.getResult_img());
												//												}else {
												//													
												//													photofile=	getphpto(cates.getResult_img());

												if (photofile==null) {


												}else {
													sendimg(photofile);
												}


											}
										}).start();


										//Toast.makeText(ECChattingActivity.this, "选择礼物"+cates.getPresent_name(),Toast.LENGTH_SHORT).show();
									}

									//									if (vioce==1) {
									//										dashang.setVisibility(View.VISIBLE);
									//									}else {
									//										dashangv.setVisibility(View.VISIBLE);
									//									}
									dismiss();

								}	

							}
							//						}

							//						}

							Toast.makeText(SingleCallActivity.this, presents.get(pos).getPresent_name(), Toast.LENGTH_SHORT).show();
							//Toast.makeText(ConversationActivity.this, mDatas.get(pos).getName(), Toast.LENGTH_SHORT).show();
						}
					});
				}
				//设置适配器
				mPager.setAdapter(new ViewPagerAdapter(mPagerList));
				//设置圆点
				setOvalLayout(mLlDot,mPager);
				giftguan.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//						if (vioce==1) {
						//							dashang.setVisibility(View.VISIBLE);
						//						}else {
						//							dashangv.setVisibility(View.VISIBLE);
						//						}
						dismiss();
					}
				});
				giftout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//						if (vioce==1) {
						//							dashang.setVisibility(View.VISIBLE);
						//						}else {
						//							dashangv.setVisibility(View.VISIBLE);
						//						}
						dismiss();
					}
				});
				//充值
				chunzhi.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(SingleCallActivity.this, UserRechargeActivity.class);
						startActivity(intent);
						dismiss();
					}
				});



				//				psentext.setVisibility(View.GONE);
				//				psentext.addTextChangedListener( new TextWatcher() {
				//					private CharSequence temp;
				//					private int selectionStart;
				//					private int selectionEnd;
				//					@Override
				//					public void onTextChanged(CharSequence s, int start, int before, int count) {
				//						// TODO Auto-generated method stub
				//
				//					}
				//
				//					@Override
				//					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				//						// TODO Auto-generated method stub
				//						temp = s;
				//					}
				//
				//					@Override
				//					public void afterTextChanged(Editable s) {
				//						// TODO Auto-generated method stub
				//
				//						int number =s.length();
				//						sentnums.setText( number+"");
				//						selectionStart = psentext.getSelectionStart();
				//						selectionEnd = psentext.getSelectionEnd();
				//						if (temp.length() > 20) {
				//							Toast.makeText(ECChattingActivity.this, "您输入的超过字数限制了", Toast.LENGTH_SHORT).show();
				//							s.delete(selectionStart - 1, selectionEnd);
				//							int tempSelection = selectionEnd;
				//							psentext.setSelection(tempSelection);//设置光标在最后
				//						}
				//					}
				//				});

				//				sendpersents.setOnClickListener(new OnClickListener() {
				//
				//					@Override
				//					public void onClick(View v) {
				//						// TODO Auto-generated method stub
				//						if (checkNetworkState()==false) {
				//
				//							Toast.makeText(SingleCallActivity.this, "当前网络已断开，请网络连接后再送礼物吧。", Toast.LENGTH_SHORT).show();
				//
				//						}else {
				//							if (cates==null) {
				//								Toast.makeText(SingleCallActivity.this, "您还没有选择礼物", Toast.LENGTH_SHORT).show();
				//
				//
				//							}else {
				//								//							if (psentext.getText().length()==0) {
				//								//								Toast.makeText(ECChattingActivity.this, "您还没有输入想说的话", Toast.LENGTH_SHORT).show();
				//								//
				//								//							}else {
				//								if (persion_main.getRongyuntoken()==null) {
				//									Toast.makeText(SingleCallActivity.this, "对方还没有融云账号，不能送礼给她", Toast.LENGTH_SHORT).show();
				//									//ToastShow("对方还没有融云账号，不能送礼给她");
				//								}else {
				//
				//									catetoken=cates.getPrice();
				//
				//
				//									if (tonken==0) {
				//										tonken=	persion_main.getToken();
				//									}else {
				//
				//									}
				//									if (tonken<catetoken) {
				//										Toast.makeText(SingleCallActivity.this, "您的金币值不够，请充值或者选其他的礼物吧。", Toast.LENGTH_SHORT).show();
				//										//ToastShow("您的爱意值不够，请充值或者选其他的礼物吧。");
				//									}else {
				//
				//
				//										//							if (psentext.getText().length()==0) {
				//										//								Toast.makeText(ECChattingActivity.this, "您还没有输入想说的话", Toast.LENGTH_SHORT).show();
				//										//
				//										//							}else {
				//										//								if (persion_main.getToken()<catetoken) {
				//										//
				//										//									Toast.makeText(UserActivity.this, "您的爱意值不够，请充值或者选其他的礼物吧。", Toast.LENGTH_SHORT).show();
				//										//
				//										//								}else {
				//
				//
				//										new Thread(new Runnable() {
				//											@Override
				//											public void run() {
				//												//
				//												//
				//												//												if (cates.getResult_img()==null) {
				//												photofile=	getphpto(cates.getResult_img());
				//												//												}else {
				//												//													
				//												//													photofile=	getphpto(cates.getResult_img());
				//
				//												if (photofile==null) {
				//
				//
				//												}else {
				//													sendimg(photofile);
				//												}
				//
				//
				//											}
				//										}).start();
				//
				//
				//										//Toast.makeText(ECChattingActivity.this, "选择礼物"+cates.getPresent_name(),Toast.LENGTH_SHORT).show();
				//									}
				//									dismiss();
				//
				//								}	
				//							}
				//						}
				//						//						}
				//					}
				//				});
			}

		}

	}
	/** 
	 * 检测网络是否连接 
	 * @return 
	 */  
	private boolean checkNetworkState() {  
		boolean flag = false;  
		//得到网络连接信息  
		ConnectivityManager    manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);  
		//去进行判断网络是否连接  
		if (manager.getActiveNetworkInfo() != null) {  
			flag = manager.getActiveNetworkInfo().isAvailable();  
		}  
		//        if (!flag) {  
		//            setNetwork();  
		//        } else {  
		//            isNetworkAvailable();  
		//        }  

		return flag;  
	}
	/**
	 * 获取图片
	 * @param url
	 */
	private File getphpto(String url) {
		// TODO Auto-generated method stub
		File file = null;
		byte[] responseBody=getImageFromURL(url);

		//	String str=url.substring(url.lastIndexOf("schoolphoto/"));
		try {
			String path=Environment.getExternalStorageDirectory()+"/aa.png";				
			file = new File(path);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(responseBody);                              
			fos.flush();
			fos.close();

		} catch (Exception e) {

		}
		return file;
	}	
	//	Java代码 
	public static byte[] getImageFromURL(String urlPath) { 
		byte[] data = null; 
		InputStream is = null; 
		HttpURLConnection conn = null; 
		try { 
			URL url = new URL(urlPath); 
			conn = (HttpURLConnection) url.openConnection(); 
			conn.setDoInput(true); 
			// conn.setDoOutput(true); 
			conn.setRequestMethod("GET"); 
			conn.setConnectTimeout(6000); 
			is = conn.getInputStream(); 
			if (conn.getResponseCode() == 200) { 
				data = readInputStream(is); 
			} else{ 
				data=null; 
			} 
		} catch (MalformedURLException e) { 
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} finally { 
			try { 
				if(is != null){ 
					is.close(); 
				}                
			} catch (IOException e) { 
				e.printStackTrace(); 
			} 
			conn.disconnect();           
		} 
		return data; 
	} 

	/**
	 * 读取InputStream数据，转为byte[]数据类型
	 * @param is
	 *            InputStream数据
	 * @return 返回byte[]数据
	 */

	//	Java代码 
	public static byte[] readInputStream(InputStream is) { 
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		byte[] buffer = new byte[1024]; 
		int length = -1; 
		try { 
			while ((length = is.read(buffer)) != -1) { 
				baos.write(buffer, 0, length); 
			} 
			baos.flush(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
		byte[] data = baos.toByteArray(); 
		try { 
			is.close(); 
			baos.close(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
		return data; 
	} 



	private void sendimg(File imageFileThumbs) {
		// TODO Auto-generated method stub
		//发送图片消息

		File imageFileSource = new File(getCacheDir(), "source.png");
		File imageFileThumb = new File(getCacheDir(), "thumb.png");

		//File imageFileSource = 	getphpto("http://192.168.1.127:8080/loveInterest/upload/present/1f43b4c0-e910-4997-9bb2-666909a39501.png");
		try {
			// 读取图片。
			// InputStream is = getAssets().open(imageFileThumbs.getPath());

			// Bitmap bmpSource = BitmapFactory.decodeStream(is);
			Bitmap bmpSource = BitmapFactory.decodeFile(imageFileThumbs.getPath());
			imageFileSource.createNewFile();
			FileOutputStream fosSource = new FileOutputStream(imageFileSource);

			// 保存原图。
			bmpSource.compress(Bitmap.CompressFormat.PNG, 100, fosSource);

			// 创建缩略图变换矩阵。
			Matrix m = new Matrix();
			m.setRectToRect(new RectF(0, 0, bmpSource.getWidth(), bmpSource.getHeight()), new RectF(0, 0, 160, 160), Matrix.ScaleToFit.CENTER);

			// 生成缩略图。
			Bitmap bmpThumb = Bitmap.createBitmap(bmpSource, 0, 0, bmpSource.getWidth(), bmpSource.getHeight(), m, true);

			imageFileThumb.createNewFile();

			FileOutputStream fosThumb = new FileOutputStream(imageFileThumb);

			// 保存缩略图。
			bmpThumb.compress(Bitmap.CompressFormat.PNG, 60, fosThumb);

		} catch (IOException e) {
			e.printStackTrace();
		}

		ImageMessage imgMsg = ImageMessage.obtain(Uri.fromFile(imageFileThumb), Uri.fromFile(imageFileSource));

		/**
		 * 发送图片消息。
		 *       
		 * @param conversationType         会话类型。
		 * @param targetId                 会话目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
		 * @param imgMsg                   消息内容。
		 * @param pushContent              接收方离线时需要显示的push消息内容。
		 * @param pushData                 接收方离线时需要在push消息中携带的非显示内容。
		 * @param SendImageMessageCallback 发送消息的回调。
		 */
		RongIM.getInstance().getRongIMClient().sendImageMessage(ConversationType.PRIVATE, targetId, imgMsg, null, null, new RongIMClient.SendImageMessageCallback() {


			@Override
			public void onSuccess(io.rong.imlib.model.Message arg0) {
				// TODO Auto-generated method stub
				arg0.describeContents();
				sendpresent(mtargetId, targetId, cates.getId());

			}

			@Override
			public void onProgress(io.rong.imlib.model.Message arg0, int arg1) {
				// TODO Auto-generated method stub
				arg0.describeContents();
			}

			@Override
			public void onError(io.rong.imlib.model.Message arg0, ErrorCode arg1) {
				// TODO Auto-generated method stub
				arg0.describeContents();
			}

			@Override
			public void onAttached(io.rong.imlib.model.Message arg0) {
				// TODO Auto-generated method stub

			}
		} );

	}  
	/**
	 * 设置圆点
	 */
	public void setOvalLayout(final LinearLayout  mLlDot,ViewPager	mPager) {
		for (int i = 0; i < pageCount; i++) {
			mLlDot.addView(inflater.inflate(R.layout.dot, null));
		}
		// 默认显示第一页
		mLlDot.getChildAt(0).findViewById(R.id.v_dot)
		.setBackgroundResource(R.drawable.dot_selected);
		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			public void onPageSelected(int position) {
				// 取消圆点选中
				mLlDot.getChildAt(curIndex)
				.findViewById(R.id.v_dot)
				.setBackgroundResource(R.drawable.dot_normal);
				// 圆点选中
				mLlDot.getChildAt(position)
				.findViewById(R.id.v_dot)
				.setBackgroundResource(R.drawable.dot_selected);
				curIndex = position;
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	// 发送小灰条消息。
	@SuppressWarnings("deprecation")
	public void sendTextBlackMessage(String text,int type) {
		InformationNotificationMessage  informationNotificationMessage;
		if (type==1) {
			informationNotificationMessage = InformationNotificationMessage.obtain("聊天时请保持社交礼仪，如果出现谩骂、\n色情及骚扰信息，请及时举报，一旦核实\n立即封号。");

		}else {
			informationNotificationMessage = InformationNotificationMessage.obtain(text);

		}
		//	RongIM.getInstance().insertMessage(ConversationType.PRIVATE, targetId, targetId, informationNotificationMessage );
		RongIM.getInstance().insertMessage(ConversationType.PRIVATE,targetId ,mtargetId , informationNotificationMessage);

	}
	@Override
	public boolean onCaptureVideoFrame(AgoraVideoFrame arg0) {
		// TODO Auto-generated method stub
		arg0.toString();
		arg0.getuBuffer();
		arg0.getvBuffer();
		arg0.getyBuffer();

		return true;
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {


			RongCallClient.getInstance().hangUpCall(callSession.getCallId());
			stopRing();
			if (chatid==0) {

				if (ehandler==null) {

				}else {
					ehandler.removeCallbacks(erunnable);
					unregisterVideoFrameObserver();

				}


			}else {
				//Toast.makeText(SingleCallActivity.this, "onBackPressed", Toast.LENGTH_SHORT);
				vend(chatid);
				RongCallAction callAction = RongCallAction.valueOf(getIntent().getStringExtra("callAction"));
				if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {

					if (chatid==0) {

					}else {
						Intent	intent = new Intent(SingleCallActivity.this, EvaluateActivity.class);
						UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(targetId);
						intent.putExtra("img", userInfo.getPortraitUri().toString());
						intent.putExtra("chatid", chatid);
						startActivity(intent);
					}


				}
				if (ehandler==null) {

				}else {
					ehandler.removeCallbacks(erunnable); 
					unregisterVideoFrameObserver();
				}

				chatid=0;
			}



			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/**

	 * 注册监听，用于处理视频通话中的视频数据。请在connect成功之后设置。

	 *

	 * @param listener 视频数据监听

	 */

	public void registerVideoFrameListener(IVideoFrameListener listener) {

		RongCallClient.getInstance().registerVideoFrameListener(listener);


	}
	public void unregisterVideoFrameObserver() {

		RongCallClient.getInstance().unregisterVideoFrameObserver();;

	}
}
