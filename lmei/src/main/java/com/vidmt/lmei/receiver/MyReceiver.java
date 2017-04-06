package com.vidmt.lmei.receiver;


import java.util.HashMap;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.vidmt.lmei.activity.MessagesActivity;
import com.vidmt.lmei.util.think.JsonUtil;
import com.vidmt.lmei.CloseAccountActivity;
import com.vidmt.lmei.activity.BaseActivity;
import com.vidmt.lmei.activity.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	public String service_id="";
	public Context context;
	private final String ACTION_NAME = "发送广播";  
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		Bundle bundle = intent.getExtras();
		//Log.e(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		//以下推送
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.e(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			//send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			Log.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			Log.e(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
			Object value = bundle.get(JPushInterface.EXTRA_EXTRA);
			Map<String,String> map =  JsonUtil.JsonToObj(value.toString(), new TypeToken<HashMap<String,String>>() {}.getType());
			Log.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + map);
			Object message = bundle.get(JPushInterface.EXTRA_ALERT);
			if("您的账号已被禁用。".equals(message)){
				//BaseActivity activity = (BaseActivity) context;
				Intent in = new Intent(context,CloseAccountActivity.class);
				in.putExtra("state", 1);//1自己的号被禁用了，2别人号被禁用了	
				in.putExtra("type", 1);
				in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
				context.startActivity(in);
				
			}else {
				Intent msgIntent = new Intent("mes");
				context.sendBroadcast(msgIntent);

			}
			processCustomMessage(context, bundle);


		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			Log.e(TAG, "[MyReceiver] 用户点击打开了通知");
			String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
			String Textcontent = bundle.getString(JPushInterface.EXTRA_ALERT);
			Object value = bundle.get(JPushInterface.EXTRA_EXTRA);
			Map<String,String> map =  JsonUtil.JsonToObj(value.toString(), new TypeToken<HashMap<String,String>>() {}.getType());	
			String type = map.get("m_type");
			String entity = map.get("m_content");
			
			if(!Textcontent.equals("您的账号已被禁用。")){
				Intent mIntent = new Intent(context, MessagesActivity.class);
				mIntent.putExtras(bundle);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(mIntent);

			}	




		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
			Log.e(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
			//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

		} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
			boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.e(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
		} else {
			Log.e(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} 
			else {
				sb.append("\nkey:" + key + ", value:" + bundle.get(key));
			}
		}
		return sb.toString();
	}

	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {

		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		Intent msgIntent = new Intent(ACTION_NAME);
		msgIntent.putExtra("look", message);
		context.sendBroadcast(msgIntent);
		/*if (MainActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			context.sendBroadcast(msgIntent);
		}*/
	}
	private Handler mUIHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if(msg.obj!=null)
				{
				}
				break;

			default:
				break;
			}
		}

	};
}
