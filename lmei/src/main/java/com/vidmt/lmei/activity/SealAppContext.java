package com.vidmt.lmei.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.ta.TAApplication;
import com.vidmt.lmei.Application;
import com.vidmt.lmei.dialog.ConversionlistDialog;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.rule.ManageDataBase;
import com.vidmt.lmei.util.rule.SharedPreferencesUtil;
import com.vidmt.lmei.util.think.DbUtil;

import io.rong.calllib.AgoraVideoFrame;
import io.rong.calllib.IVideoFrameListener;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.message.CallSTerminateMessage;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.AlterDialogFragment;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.FileInputProvider;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.LocationInputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectionStatusListener;
import io.rong.imlib.location.RealTimeLocationConstant;
import io.rong.imlib.location.message.RealTimeLocationStartMessage;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.GroupNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;

/**
 * 融云相关监听 事件集合类
 * Created by AMing on 16/1/7.
 * Company RongCloud
 */
public class SealAppContext implements RongIM.ConversationListBehaviorListener, RongIMClient.OnReceiveMessageListener, RongIM.UserInfoProvider, RongIM.GroupInfoProvider, RongIM.GroupUserInfoProvider, RongIMClient.ConnectionStatusListener, RongIM.LocationProvider,RongIM.ConversationBehaviorListener,IVideoFrameListener {


	public static final String UPDATE_FRIEND = "update_friend";
	public static final String UPDATE_RED_DOT = "update_red_dot";
	private static final String CallSTerminateMessage = null;
	private Context mContext;

	private static SealAppContext mRongCloudInstance;

	private RongIM.LocationProvider.LocationCallback mLastLocationCallback;

	private static ArrayList<Activity> mActivities;
	
	public SealAppContext(Context mContext) {
		this.mContext = mContext;
		initListener();
		mActivities = new ArrayList<Activity>();

	}

	/**
	 * 初始化 RongCloud.
	 *
	 * @param context 上下文。
	 */
	public static void init(Context context) {

		if (mRongCloudInstance == null) {

			synchronized (SealAppContext.class) {

				if (mRongCloudInstance == null) {
					mRongCloudInstance = new SealAppContext(context);
				}
			}
		}

	}

	/**
	 * 获取RongCloud 实例。
	 *
	 * @return RongCloud。
	 */
	public static SealAppContext getInstance() {
		return mRongCloudInstance;
	}

	/**
	 * init 后就能设置的监听
	 */
	private void initListener() {
		RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器。
		RongIM.setConversationListBehaviorListener(this);
		RongIM.setUserInfoProvider(this, true);
		RongIM.setGroupInfoProvider(this, true);
		RongIM.setConnectionStatusListener(connectionStatusListener);
		
		
		//  RongIM.setLocationProvider(this);//设置地理位置提供者,不用位置的同学可以注掉此行代码
		setInputProvider();
		setUserInfoEngineListener();
		setReadReceiptConversationType();
		//        RongIM.setGroupUserInfoProvider(this, true);


	}
	
	

	ConnectionStatusListener connectionStatusListener =new ConnectionStatusListener() {

		@Override
		public void onChanged(ConnectionStatus arg0) {
			// TODO Auto-generated method stub

			switch (arg0){

			case CONNECTED://连接成功。



				break;
			case DISCONNECTED://断开连接。


               

				break;
			case CONNECTING://连接中。



				break;
			case NETWORK_UNAVAILABLE://网络不可用。


				break;
			case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线


				Toast.makeText(mContext, "您的账号在其他地方登录", Toast.LENGTH_SHORT).show();
				break;
			}



		}
	};
	private void setReadReceiptConversationType() {
		Conversation.ConversationType[] types = new Conversation.ConversationType[] {
				Conversation.ConversationType.PRIVATE,
				Conversation.ConversationType.GROUP,
				Conversation.ConversationType.DISCUSSION
		};
		RongIM.getInstance().setReadReceiptConversationTypeList(types);
	}

	private void setInputProvider() {

		RongIM.setOnReceiveMessageListener(this);
		RongIM.setConnectionStatusListener(this);

		InputProvider.ExtendProvider[] singleProvider = {
				new ImageInputProvider(RongContext.getInstance()),
				new CameraInputProvider(RongContext.getInstance())
				//new RealTimeLocationInputProvider(RongContext.getInstance()), //带位置共享的地理位置
//				new FileInputProvider(RongContext.getInstance())//文件消息
		};

//		InputProvider.ExtendProvider[] muiltiProvider = {
//				new ImageInputProvider(RongContext.getInstance()),
//				new LocationInputProvider(RongContext.getInstance()),//地理位置
//				new FileInputProvider(RongContext.getInstance())//文件消息
//		};

		RongIM.resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, singleProvider);
//		RongIM.resetInputExtensionProvider(Conversation.ConversationType.DISCUSSION, muiltiProvider);
//		RongIM.resetInputExtensionProvider(Conversation.ConversationType.CUSTOMER_SERVICE, muiltiProvider);
//		RongIM.resetInputExtensionProvider(Conversation.ConversationType.GROUP, muiltiProvider);
//		RongIM.resetInputExtensionProvider(Conversation.ConversationType.CHATROOM, muiltiProvider);
	}

	/**
	 * 需要 rongcloud connect 成功后设置的 listener
	 */
	public void setUserInfoEngineListener() {
		UserInfoEngine.getInstance(mContext).setListener(new UserInfoEngine.UserInfoListener() {
			@Override
			public void onResult(UserInfo info) {
				if (info != null && RongIM.getInstance() != null) {
					if (TextUtils.isEmpty(String.valueOf(info.getPortraitUri()))) {
						info.setPortraitUri(Uri.parse(RongGenerate.generateDefaultAvatar(info.getName(), info.getUserId())));
					}
					Log.e("UserInfoEngine", info.getName() + info.getPortraitUri());
					RongIM.getInstance().refreshUserInfoCache(info);
				}
			}
		});
		//        GroupInfoEngine.getInstance(mContext).setmListener(new GroupInfoEngine.GroupInfoListeners() {
		//            @Override
		//            public void onResult(Group info) {
		//                if (info != null && RongIM.getInstance() != null) {
		//                    NLog.e("GroupInfoEngine:" + info.getId() + "----" + info.getName() + "----" + info.getPortraitUri());
		//                    if (TextUtils.isEmpty(String.valueOf(info.getPortraitUri()))) {
		//                        info.setPortraitUri(Uri.parse(RongGenerate.generateDefaultAvatar(info.getName(), info.getId())));
		//                    }
		//                    RongIM.getInstance().refreshGroupInfoCache(info);
		//
		//                }
		//            }
		//        });
	}

	@Override
	public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {
		Log.e("rongyun", context.toString());

		ConversationListActivity.conversationListActivity.selectisblack(s);
		return true;
	}

	@Override
	public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
		Log.e("rongyun", context.toString());
		return false;

	}

	@Override
	public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {





		Log.e("rongyun", "---------------sdsdd---------------------------------");
		Log.e("rongyun", context.toString());
		Log.e("rongyun", view.toString());
		Log.e("rongyun", uiConversation.toString());
		//Toast.makeText(context, "长按", Toast.LENGTH_SHORT).show();
		ConversionlistDialog conversionlistDialog = new ConversionlistDialog(context,uiConversation);
		conversionlistDialog.show();
		return true;
	}



	@Override
	public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
		MessageContent messageContent = uiConversation.getMessageContent();
		if (messageContent instanceof ContactNotificationMessage) {
			ContactNotificationMessage contactNotificationMessage = (ContactNotificationMessage) messageContent;
			if (contactNotificationMessage.getOperation().equals("AcceptResponse")) {
				// 被加方同意请求后
				if (contactNotificationMessage.getExtra() != null) {
					//                    ContactNotificationMessageData bean = null;
					//                    try {
					//                        bean = JsonMananger.jsonToBean(contactNotificationMessage.getExtra(), ContactNotificationMessageData.class);
					//                    } catch (HttpException e) {
					//                        e.printStackTrace();
					//                    }
					//.getInstance().startPrivateChat(context, uiConversation.getConversationSenderId(), bean.getSourceUserNickname());

				}
			} else {
				// context.startActivity(new Intent(context, NewFriendListActivity.class));
			}


			return true;
		}


		ConversationListActivity.conversationListActivity.selectisblack(uiConversation);
		return true;
	}

	@Override
	public boolean onReceived(Message message, int i) {
		MessageContent messageContent = message.getContent();
		if (messageContent instanceof ContactNotificationMessage) {
			ContactNotificationMessage contactNotificationMessage = (ContactNotificationMessage) messageContent;
			if (contactNotificationMessage.getOperation().equals("Request")) {
				//对方发来好友邀请
				//  BroadcastManager.getInstance(mContext).sendBroadcast(SealAppContext.UPDATE_RED_DOT);
			} else if (contactNotificationMessage.getOperation().equals("AcceptResponse")) {
				//对方同意我的好友请求
				//                ContactNotificationMessageData c = null;
				//                try {
				//                    c = JsonMananger.jsonToBean(contactNotificationMessage.getExtra(), ContactNotificationMessageData.class);
				//                } catch (HttpException e) {
				//                    e.printStackTrace();
				//                }
				//                if (c != null) {
				//                    DBManager.getInstance(mContext).getDaoSession().getFriendDao().insertOrReplace(new Friend(contactNotificationMessage.getSourceUserId(), c.getSourceUserNickname(), null, null, null, null));
				//                }
				//                BroadcastManager.getInstance(mContext).sendBroadcast(UPDATE_FRIEND);
				//                BroadcastManager.getInstance(mContext).sendBroadcast(SealAppContext.UPDATE_RED_DOT);
			}
			//                // 发广播通知更新好友列表
			//            BroadcastManager.getInstance(mContext).sendBroadcast(UPDATE_RED_DOT);
			//            }
		} else if (messageContent instanceof GroupNotificationMessage) {
			GroupNotificationMessage groupNotificationMessage = (GroupNotificationMessage) messageContent;
			Log.e("" + groupNotificationMessage.getMessage(), null);
			if (groupNotificationMessage.getOperation().equals("Kicked")) {
			} else if (groupNotificationMessage.getOperation().equals("Add")) {
			} else if (groupNotificationMessage.getOperation().equals("Quit")) {
			} else if (groupNotificationMessage.getOperation().equals("Rename")) {
			}

		} else if (messageContent instanceof ImageMessage) {
			ImageMessage imageMessage = (ImageMessage) messageContent;
			Log.e("imageMessage", imageMessage.getRemoteUri().toString());
		}
		return false;
	}

	@Override
	public UserInfo getUserInfo(String s) {
		return UserInfoEngine.getInstance(mContext).startEngine(s);
	}

	@Override
	public Group getGroupInfo(String s) {
		return null;
	}

	@Override
	public GroupUserInfo getGroupUserInfo(String groupId, String userId) {
		//        return GroupUserInfoEngine.getInstance(mContext).startEngine(groupId, userId);
		return null;
	}

	@Override
	public void onChanged(ConnectionStatus connectionStatus) {
		if (connectionStatus.getMessage().equals(ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {

		}
	}

	@Override
	public void onStartLocation(Context context, LocationCallback locationCallback) {
		/**
		 * demo 代码  开发者需替换成自己的代码。
		 */
		SealAppContext.getInstance().setLastLocationCallback(locationCallback);
		//        Intent intent = new Intent(context, AMAPLocationActivity.class);
		//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//        context.startActivity(intent);

	}

	@Override
	public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
		if (conversationType == Conversation.ConversationType.CUSTOMER_SERVICE || conversationType == Conversation.ConversationType.PUBLIC_SERVICE || conversationType == Conversation.ConversationType.APP_PUBLIC_SERVICE) {
			return false;
		}
		if (conversationType == Conversation.ConversationType.PRIVATE) {
			if (userInfo != null) {

			DbUtil	dbutil = new DbUtil((TAApplication) context.getApplicationContext());
			Persion	p_person = null;
			List<Persion> list = ManageDataBase.SelectList(dbutil, Persion.class, null);
			if (list.size() > 0) {
		      p_person = list.get(0);
			}
			if (p_person==null) {
				
			}else {
			
					if (userInfo.getUserId().equals(p_person.getId().toString())) {
						Intent intent = new Intent(context, PersonUpdateActivity.class);
						context.startActivity(intent);
					}else {
						Intent intent = new Intent(context, HomeDetailActivity.class);
						intent.putExtra("userid",Integer.valueOf(userInfo.getUserId()).intValue() );
						context.startActivity(intent);
					}
				}

			}
			
		}

		return true;
	}

	@Override
	public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
		
	String sj;
		
		return false;
	}

	@Override
	public boolean onMessageClick(final Context context, final View view, final Message message) {


		//real-time location message begin
		if (message.getContent() instanceof RealTimeLocationStartMessage) {
			RealTimeLocationConstant.RealTimeLocationStatus status = RongIMClient.getInstance().getRealTimeLocationCurrentState(message.getConversationType(), message.getTargetId());

			//            if (status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_IDLE) {
			//                startRealTimeLocation(context, message.getConversationType(), message.getTargetId());
			//            } else
			if (status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_INCOMING) {


				final AlterDialogFragment alterDialogFragment = AlterDialogFragment.newInstance("", "加入位置共享", "取消", "加入");
				alterDialogFragment.setOnAlterDialogBtnListener(new AlterDialogFragment.AlterDialogBtnListener() {

					@Override
					public void onDialogPositiveClick(AlterDialogFragment dialog) {
						RealTimeLocationConstant.RealTimeLocationStatus status = RongIMClient.getInstance().getRealTimeLocationCurrentState(message.getConversationType(), message.getTargetId());

						if (status == null || status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_IDLE) {
							startRealTimeLocation(context, message.getConversationType(), message.getTargetId());
						} else {
							joinRealTimeLocation(context, message.getConversationType(), message.getTargetId());
						}

					}

					@Override
					public void onDialogNegativeClick(AlterDialogFragment dialog) {
						alterDialogFragment.dismiss();
					}
				});

				alterDialogFragment.show(((FragmentActivity) context).getSupportFragmentManager());
			} else {

				if (status != null && (status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_OUTGOING || status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_CONNECTED)) {

					//                    Intent intent = new Intent(((FragmentActivity) context), RealTimeLocationActivity.class);
					//                    intent.putExtra("conversationType", message.getConversationType().getValue());
					//                    intent.putExtra("targetId", message.getTargetId());
					//                    context.startActivity(intent);
				}
			}
			return true;
		}

		//real-time location message end
		/**
		 * demo 代码  开发者需替换成自己的代码。
		 */
		if (message.getContent() instanceof LocationMessage) {
			//            Intent intent = new Intent(context, AMAPLocationActivity.class);
			//            intent.putExtra("location", message.getContent());
			//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//            context.startActivity(intent);
		} else if (message.getContent() instanceof ImageMessage) {
			//            Intent intent = new Intent(context, PhotoActivity.class);
			//            intent.putExtra("message", message);
			//            context.startActivity(intent);
		}else if (message.getContent() instanceof CallSTerminateMessage ) {
			
			
			CallSTerminateMessage callSTerminateMessage = (io.rong.calllib.message.CallSTerminateMessage) message.getContent();
			if (callSTerminateMessage.getMediaType().equals(RongCallCommon.CallMediaType.AUDIO)) {
				DbUtil  dbutil = new DbUtil((TAApplication)context.getApplicationContext());

				List<Persion> list = ManageDataBase.SelectList(dbutil, Persion.class, null);
				if (list.size() > 0) {
					Persion	b_person = list.get(0);
					if (b_person.getToken()!=null&&b_person.getToken()>0) {
						
						int atype = SharedPreferencesUtil.getInt(context, "ameney", 0);
						if (b_person.getToken().intValue()<atype) {
							Toast.makeText(context, "您的金币不足，请充值后再聊天吧！", Toast.LENGTH_SHORT).show();
							return true;
						}else {
							
						}
					}else {
						Toast.makeText(context, "您的金币不足，请充值后再聊天吧！", Toast.LENGTH_SHORT).show();
						return true;	
					}
				}
				int ltype = SharedPreferencesUtil.getInt(context, "vstype", 0);
				

				if (ltype==1) {

					Toast.makeText(context, "对方未开启语音聊天", Toast.LENGTH_SHORT).show();
					return true;	
				}
				
				int btype = SharedPreferencesUtil.getInt(context, "lblack", 0);
				if (btype==1) {
					Toast.makeText(context, "您已被对方拉黑，不能继续聊天", Toast.LENGTH_SHORT).show();
					return true;
				}
				
				
				
				
			}else if (callSTerminateMessage.getMediaType().equals(RongCallCommon.CallMediaType.VIDEO)) {
				DbUtil  dbutil = new DbUtil((TAApplication) context.getApplicationContext());

				List<Persion> list = ManageDataBase.SelectList(dbutil, Persion.class, null);
				if (list.size() > 0) {
					Persion	b_person = list.get(0);
					if (b_person.getToken()!=null&&b_person.getToken()>0) {

						int vmeney = SharedPreferencesUtil.getInt(context, "vmeney", 0);
						if (b_person.getToken().intValue()<vmeney) {
							Toast.makeText(context, "您的金币不足，请充值后再聊天吧！", Toast.LENGTH_SHORT).show();
							return true;
						}else {
							
						}
						
						
					}else {
						Toast.makeText(context, "您的金币不足，请充值后再聊天吧！", Toast.LENGTH_SHORT).show();
						return true;	
					}
				}


				int ltype = SharedPreferencesUtil.getInt(context, "astype", 0);
				if (ltype==1) {

					Toast.makeText(context, "对方未开启视频聊天", Toast.LENGTH_SHORT).show();
					return true;	
				}
				int btype = SharedPreferencesUtil.getInt(context, "lblack", 0);
				if (btype==1) {
					Toast.makeText(context, "您已被对方拉黑，不能继续聊天", Toast.LENGTH_SHORT).show();
					return true;
				}
			}


		}

		return false;
	}


	private void startRealTimeLocation(Context context, Conversation.ConversationType conversationType, String targetId) {
		RongIMClient.getInstance().startRealTimeLocation(conversationType, targetId);

		//        Intent intent = new Intent(((FragmentActivity) context), RealTimeLocationActivity.class);
		//        intent.putExtra("conversationType", conversationType.getValue());
		//        intent.putExtra("targetId", targetId);
		//        context.startActivity(intent);
	}

	private void joinRealTimeLocation(Context context, Conversation.ConversationType conversationType, String targetId) {
		RongIMClient.getInstance().joinRealTimeLocation(conversationType, targetId);

		//        Intent intent = new Intent(((FragmentActivity) context), RealTimeLocationActivity.class);
		//        intent.putExtra("conversationType", conversationType.getValue());
		//        intent.putExtra("targetId", targetId);
		//        context.startActivity(intent);
	}

	@Override
	public boolean onMessageLinkClick(Context context, String s) {


		return false;
	}

	@Override
	public boolean onMessageLongClick(Context context, View view, Message message) {
		return false;
	}


	public RongIM.LocationProvider.LocationCallback getLastLocationCallback() {
		return mLastLocationCallback;
	}

	public void setLastLocationCallback(RongIM.LocationProvider.LocationCallback lastLocationCallback) {
		this.mLastLocationCallback = lastLocationCallback;
	}

	public void pushActivity(Activity activity) {
		mActivities.add(activity);
	}

	public void popActivity(Activity activity) {
		if (mActivities.contains(activity)) {
			activity.finish();
			mActivities.remove(activity);
		}
	}

	public void popAllActivity() {
		try {
			for (Activity activity : mActivities) {
				if (activity != null) {
					activity.finish();
				}
			}
			mActivities.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 建立与融云服务器的连接
	 *
	 * @param token
	 */
	private void connect(final String token,final Persion persion) {
		//ToastShow(token);
		if (RongIM.getInstance().getRongIMClient()!=null) {



		}else {

		}

		if (mContext.getApplicationInfo().packageName.equals(Application.getApplication().getPackageName())) {

			/**
			 * IMKit SDK调用第二步,建立与服务器的连接
			 */
			RongIM.connect(token, new RongIMClient.ConnectCallback() {

				/**
				 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
				 */
				@Override
				public void onTokenIncorrect() {
					//ToastShow("--onTokenIncorrect");
					Log.d("LoginActivity", "--onTokenIncorrect");
					//	                StartActivity(HomePageActivity.class);
					//					finish();
				}

				/**
				 * 连接融云成功
				 * @param userid 当前 token
				 */
				@Override
				public void onSuccess(String userid) {
					//ToastShow("Success");
					Log.d("LoginActivity", "--onSuccess" + userid);
					RongIM.getInstance().refreshUserInfoCache(new UserInfo(persion.getId()+"", persion.getNick_name(), Uri.parse(persion.getPhoto())));
					RongIM.getInstance().setMessageAttachedUserInfo(true);


				}

				/**
				 * 连接融云失败
				 * @param errorCode 错误码，可到官网 查看错误码对应的注释
				 */
				@Override
				public void onError(RongIMClient.ErrorCode errorCode) {

				}
			});
		}else {


		}
	}	
	
	@Override
	public boolean onCaptureVideoFrame(AgoraVideoFrame arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}
