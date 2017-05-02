package com.vidmt.lmei.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.ta.TAApplication;
import com.vidmt.lmei.R;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.rule.SharedPreferencesUtil;
import com.vidmt.lmei.util.think.DbUtil;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import io.rong.calllib.IRongCallListener;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallSession;
import io.rong.calllib.RongCallCommon;

/**
 * Created by weiqinxiao on 16/3/9.
 * 基础通话页activity
 */
public class BaseCallActivity extends Activity implements IRongCallListener {
	private final static long DELAY_TIME = 1000;
	static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 100;
	protected DbUtil dbutil;
	protected  Persion persion_main;
	protected  String mtargetId;//我的id

	private MediaPlayer mMediaPlayer;
	private int time = 0;
	private boolean shouldShowFloat;
	private boolean shouldRestoreFloat;
	private Handler handler;
	private int type = 0;
	public void setShouldShowFloat(boolean shouldShowFloat) {
		this.shouldShowFloat = shouldShowFloat;
	}

	public void showShortToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	public void postRunnableDelay(Runnable runnable) {
		handler.postDelayed(runnable, DELAY_TIME);
	}

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		RongCallClient.getInstance().setVoIPCallListener(BaseCallActivity.this);
		shouldRestoreFloat = true;

		PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		boolean isScreenOn = pm.isScreenOn();
		if (!isScreenOn) {
			PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
			wl.acquire();
			wl.release();
		}
		handler = new Handler();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("floatbox");
		if (shouldRestoreFloat)
			onRestoreFloatBox(bundle);
		shouldRestoreFloat = true;
		type = SharedPreferencesUtil.getInt(BaseCallActivity.this, "chattype", 0);
		if (type==0) {
			
		}else {
			SharedPreferencesUtil.putInt(BaseCallActivity.this, "chattype", 0);
		}
		refreshStatus();
	}

	private void refreshStatus() {
		dbutil = new DbUtil((TAApplication) this.getApplication());
		List<Persion>  	users= dbutil.selectData(Persion.class,null);
		if(users !=null)
		{
			if(users.size()>0)
			{
				try {
					persion_main = users.get(0);
					mtargetId =  persion_main.getId()+"";
					new Thread(new Runnable() {
						@Override
						public void run() {
							Person_Service.update_presence(mtargetId,2+"");
						}
					}).start();

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}

	//正在拨
	public void onOutgoingCallRinging() {
		mMediaPlayer = MediaPlayer.create(this, R.raw.voip_outgoing_ring);

		mMediaPlayer.setLooping(true);
		mMediaPlayer.start();
	}

	public void onIncomingCallRinging() {
		Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		mMediaPlayer = new MediaPlayer();
		try {
			mMediaPlayer.setDataSource(this, uri);
			mMediaPlayer.setLooping(true);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setupTimes(final TextView timeView,final TextView timeViews) {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						time++;
						if (time >= 3600) {
							if (timeViews==null) {

							}else {
								timeViews.setText(String.format("%d:%02d:%02d", time / 3600, (time % 3600) / 60, (time % 60)));
							}
							timeView.setText(String.format("%d:%02d:%02d", time / 3600, (time % 3600) / 60, (time % 60)));
						} else {
							if (timeViews==null) {

							}else {
								timeViews.setText(String.format("%02d:%02d", (time % 3600) / 60, (time % 60)));
							}
							timeView.setText(String.format("%02d:%02d", (time % 3600) / 60, (time % 60)));
						}
					}
				});
			}
		};

		Timer timer = new Timer();
		timer.schedule(task, 0, 1000);
	}

	public int getTime() {
		return time;
	}

	public void stopRing() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer = null;
		}
	}

	@Override
	public void onCallOutgoing(RongCallSession callProfile, SurfaceView localVideo) {
	}

	@Override
	public void onRemoteUserRinging(String userId) {

	}

	@Override
	public void onCallDisconnected(RongCallSession callProfile, RongCallCommon.CallDisconnectedReason reason) {
		shouldShowFloat = false;

		String text = null;
		switch (reason) {
		case CANCEL:
			text = getString(R.string.rc_voip_mo_cancel);
			break;
		case REJECT:
			text = getString(R.string.rc_voip_mo_reject);
			break;
		case NO_RESPONSE:
			
			
		case BUSY_LINE:
			text = getString(R.string.rc_voip_mo_no_response);
			break;
		case REMOTE_BUSY_LINE:
			text = getString(R.string.rc_voip_mt_busy);
			break;
		case REMOTE_CANCEL:
			text = getString(R.string.rc_voip_mt_cancel);
			break;
		case REMOTE_REJECT:
			text = getString(R.string.rc_voip_mt_reject);
			break;
		case REMOTE_NO_RESPONSE:
			text = getString(R.string.rc_voip_mt_no_response);
			break;
		case REMOTE_HANGUP:
			
		case HANGUP:
			
		case NETWORK_ERROR:
			
			text = getString(R.string.rc_voip_call_terminalted);
			type = SharedPreferencesUtil.getInt(BaseCallActivity.this, "chattype", 0);
			if (type==0) {
				SharedPreferencesUtil.putInt(BaseCallActivity.this, "chattype", 1);
			}else {
				
			}
			break;
		}
		if (text != null) { 
			showShortToast(text);
		}
		
		if (SingleCallActivity.singleCallActivity!=null) {
			SingleCallActivity.singleCallActivity.vends();	
		}
		RongCallClient.getInstance().setVoIPCallListener(null);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Person_Service.update_presence(mtargetId,1+"");
			}
		}).start();
	}

	@Override
	public void onRemoteUserInvited(String userId, RongCallCommon.CallMediaType mediaType) {

	}

	@Override
	public void onRemoteUserJoined(String userId, RongCallCommon.CallMediaType mediaType, SurfaceView remoteVideo) {
	
	}

	@Override
	public void onRemoteUserLeft(String userId, RongCallCommon.CallDisconnectedReason reason) {
		
	}

	@Override
	public void onMediaTypeChanged(String userId, RongCallCommon.CallMediaType mediaType, SurfaceView video) {
	
	}

	@Override
	public void onError(RongCallCommon.CallErrorCode errorCode) {
	

	}

	@Override
	public void onCallConnected(RongCallSession callProfile, SurfaceView localVideo) {
		shouldShowFloat = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		//        RongCallSession session = RongCallClient.getInstance().getCallSession();
		//        if (session == null) {
		//            finish();
		//            return;
		//        }

		if (shouldShowFloat) {
			Bundle bundle = new Bundle();
			String action = onSaveFloatBoxState(bundle);
			if (action != null) {
				bundle.putString("action", action);
				CallFloatBoxView.showFloatBox(getApplicationContext(), bundle, time,mtargetId);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Person_Service.update_presence(mtargetId,2+"");
                    }
                }).start();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		RongCallClient.getInstance().setVoIPCallListener(this);
		//        RongCallSession session = RongCallClient.getInstance().getCallSession();
		//        if (session == null) {
		//            finish();
		//            return;
		//        }
		time = CallFloatBoxView.hideFloatBox();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		shouldRestoreFloat = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onRemoteCameraDisabled(String userId, boolean muted) {

	}

	public void onRestoreFloatBox(Bundle bundle) {

	}

	public String onSaveFloatBoxState(Bundle bundle) {
		return null;
	}

	@SuppressLint("NewApi") @Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@TargetApi(23)
	boolean requestCallPermissions(RongCallCommon.CallMediaType type) {
		if (Build.VERSION.SDK_INT < 2)
			return true;

		if (type.equals(RongCallCommon.CallMediaType.VIDEO)) {
			List<String> permissionsNeeded = new ArrayList<String>();
			final List<String> permissionsList = new ArrayList<String>();

			//			if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO)) {
			//				permissionsNeeded.add("RecordAudio");
			//			}
			//			if (!addPermission(permissionsList, Manifest.permission.CAMERA)) {
			//				permissionsNeeded.add("Camera");
			//			}

			if (permissionsList.size() > 0) {
				if (permissionsNeeded.size() > 0) {
					String message = null;
					if (permissionsNeeded.size() > 1) {
						message = getResources().getString(R.string.rc_permission_grant_needed) + getResources().getString(R.string.rc_permission_microphone_and_camera);
					} else {
						if (permissionsNeeded.get(0).equals("RecordAudio")) {
							message = getResources().getString(R.string.rc_permission_grant_needed) + getResources().getString(R.string.rc_permission_microphone);
						} else if (permissionsNeeded.get(0).equals("Camera")) {
							message = getResources().getString(R.string.rc_permission_grant_needed) + getResources().getString(R.string.rc_permission_camera);
						}
					}
					new AlertDialog.Builder(this)
					.setMessage(message)
					.setPositiveButton(R.string.rc_confirm, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//ActivityCompat.requestPermissions(null, permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
						}
					})
					.setNegativeButton(R.string.rc_cancel, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					})
					.create().show();
					return false;
				}
				//ActivityCompat.requestPermissions(BaseCallActivity.this, permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
				return false;
			} else {
				return true;
			}
		} else if (type.equals(RongCallCommon.CallMediaType.AUDIO)) {
			//			int checkPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
			//			if (checkPermission != PackageManager.PERMISSION_GRANTED) {
			////				if (ActivityCompat.shouldShowRequestPermissionRationale(BaseCallActivity.this, Manifest.permission.RECORD_AUDIO)) {
			////					ActivityCompat.requestPermissions(BaseCallActivity.this, new String[] {Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
			////				} else {
			//					String message = getResources().getString(R.string.rc_permission_grant_needed) + getResources().getString(R.string.rc_permission_microphone);
			//					new AlertDialog.Builder(this)
			//					.setMessage(message)
			//					.setPositiveButton(R.string.rc_confirm, new DialogInterface.OnClickListener() {
			//						@Override
			//						public void onClick(DialogInterface dialog, int which) {
			//							//ActivityCompat.requestPermissions(BaseCallActivity.this,new String[] {Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
			//						}
			//					})
			//					.setNegativeButton(R.string.rc_cancel, new DialogInterface.OnClickListener() {
			//						@Override
			//						public void onClick(DialogInterface dialog, int which) {
			//							finish();
			//						}
			//					})
			//					.create().show();
			//				//}
			//				return false;
			//			} else {
			return true;
			//			}
		}
		return false;
	}

	//	@TargetApi(23)
	//	private boolean addPermission(List<String> permissionList, String permission) {
	//		if (ContextCompat.checkSelfPermission(BaseCallActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
	//			permissionList.add(permission);
	//			if (!ActivityCompat.shouldShowRequestPermissionRationale(BaseCallActivity.this,permission))
	//				return false;
	//		}
	//		return true;
	//	}

	//	@Override
	//	public void onRequestPermissionsResult(int arg0, @NonNull String[] arg1,
	//			@NonNull int[] arg2) {
	//		// TODO Auto-generated method stub
	//
	//	}



}
