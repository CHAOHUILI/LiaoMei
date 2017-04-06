package com.vidmt.lmei.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.rong.common.RLog;

public class RongCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        RLog.d("RongCallReceiver", "intent = " + intent);
        
        
        
        try {
        	 if (intent.getAction().equals(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_INIT)) {
                 RongCallService.onInit(context);
                 RongCallService.onConnected();
             } else if (intent.getAction().equals(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_UI_READY)) {
                 RongCallService.onUiReady();
             } else if (intent.getAction().equals(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_CONNECTED)) {
                 RongCallService.onConnected();
             }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

       

    }


}
