package com.vidmt.lmei.activity;

import java.util.List;

import com.ta.TAApplication;
import com.vidmt.lmei.R;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.rule.ManageDataBase;
import com.vidmt.lmei.util.rule.SharedPreferencesUtil;
import com.vidmt.lmei.util.think.DbUtil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;
import io.rong.calllib.message.CallSTerminateMessage;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.LinkTextView;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.widget.ArraysDialogFragment;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * 呼叫结束后聊天界面发送消息，记录聊天（语言，视频接听，未接听，取消）情况
 */
@ProviderTag(messageContent = CallSTerminateMessage.class, showSummaryWithName = false, showProgress = false, showWarning = false)
public class CallEndMessageItemProvider extends IContainerItemProvider.MessageProvider<CallSTerminateMessage> {
    class ViewHolder {
        LinkTextView message;
    }
     private Context context;

    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_text_message, null);

        ViewHolder holder = new ViewHolder();
        holder.message = (LinkTextView) view.findViewById(android.R.id.text1);
        view.setTag(holder);
        this.context=context  ;
        return view;
    }

    @Override
    public void bindView(View v, int position, CallSTerminateMessage content, UIMessage data) {
        ViewHolder holder = (ViewHolder) v.getTag();

        if (data == null || content == null) {
            return;
        }
        if (data.getMessageDirection() == Message.MessageDirection.SEND) {
            holder.message.setBackgroundResource(R.drawable.rc_ic_bubble_right);
        } else {
            holder.message.setBackgroundResource(R.drawable.rc_ic_bubble_left);
        }

        RongCallCommon.CallMediaType mediaType = content.getMediaType();
        String direction = content.getDirection();
        Drawable drawable = null;

        String msgContent = "";
        switch (content.getReason()) {
            case CANCEL:
                msgContent = v.getResources().getString(R.string.rc_voip_mo_cancel);
                break;
            case REJECT:
                msgContent = v.getResources().getString(R.string.rc_voip_mo_reject);
                break;
            case NO_RESPONSE:
            case BUSY_LINE:
                msgContent = v.getResources().getString(R.string.rc_voip_mo_no_response);
                break;
            case REMOTE_BUSY_LINE:
                msgContent = v.getResources().getString(R.string.rc_voip_mt_busy);
                break;
            case REMOTE_CANCEL:
                msgContent = v.getResources().getString(R.string.rc_voip_mt_cancel);
                break;
            case REMOTE_REJECT:
                msgContent = v.getResources().getString(R.string.rc_voip_mt_reject);
                break;
            case REMOTE_NO_RESPONSE:
                msgContent = v.getResources().getString(R.string.rc_voip_mt_no_response);
                break;
            case HANGUP:
            case REMOTE_HANGUP:
                msgContent = v.getResources().getString(R.string.rc_voip_call_time_length);
                msgContent += content.getExtra();
                break;
            case NETWORK_ERROR:
            case REMOTE_NETWORK_ERROR:
                msgContent = v.getResources().getString(R.string.rc_voip_call_interrupt);
                break;
        }

        holder.message.setText(msgContent);
        holder.message.setCompoundDrawablePadding(15);

        if (mediaType.equals(RongCallCommon.CallMediaType.VIDEO)) {
            if (direction != null && direction.equals("MO")) {
                drawable = v.getResources().getDrawable(R.drawable.rc_voip_video_right);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                holder.message.setCompoundDrawables(null, null, drawable, null);
            } else {
                drawable = RongContext.getInstance().getResources().getDrawable(R.drawable.rc_voip_video_left);
                drawable.setBounds(0, 0,  drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                holder.message.setCompoundDrawables(drawable, null, null, null);
            }
        } else {
            if (direction != null && direction.equals("MO")) {
                if (content.getReason().equals(RongCallCommon.CallDisconnectedReason.HANGUP) ||
                        content.getReason().equals(RongCallCommon.CallDisconnectedReason.REMOTE_HANGUP)) {
                    drawable = RongContext.getInstance().getResources().getDrawable(R.drawable.rc_voip_audio_right_connected);
                } else {
                    drawable = RongContext.getInstance().getResources().getDrawable(R.drawable.rc_voip_audio_right_cancel);
                }
                drawable.setBounds(0, 0,  drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                holder.message.setCompoundDrawables(null, null, drawable, null);
            } else {
                if (content.getReason().equals(RongCallCommon.CallDisconnectedReason.HANGUP) ||
                        content.getReason().equals(RongCallCommon.CallDisconnectedReason.REMOTE_HANGUP)) {
                    drawable = RongContext.getInstance().getResources().getDrawable(R.drawable.rc_voip_audio_left_connected);
                } else {
                    drawable = RongContext.getInstance().getResources().getDrawable(R.drawable.rc_voip_audio_left_cancel);
                }
                drawable.setBounds(0, 0,  drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                holder.message.setCompoundDrawables(drawable, null, null, null);
            }
        }
    }

    @Override
    public Spannable getContentSummary(CallSTerminateMessage data) {

        RongCallCommon.CallMediaType mediaType = data.getMediaType();
        if (mediaType.equals(RongCallCommon.CallMediaType.AUDIO)) {
            return new SpannableString(RongContext.getInstance().getString(R.string.rc_voip_message_audio));
        } else {
            return new SpannableString(RongContext.getInstance().getString(R.string.rc_voip_message_video));
        }
    }

    @Override
    public void onItemClick(View view, int position, CallSTerminateMessage content, UIMessage message) {
    	
    	
        RongCallSession profile = RongCallClient.getInstance().getCallSession();
        if (profile != null && profile.getActiveTime() > 0) {
            Toast.makeText(view.getContext(), view.getContext().getString(R.string.rc_voip_call_start_fail), Toast.LENGTH_SHORT).show();
            return;
        }
        RongCallCommon.CallMediaType mediaType = content.getMediaType();
        String action = null;
        if (mediaType.equals(RongCallCommon.CallMediaType.VIDEO)) {
        	

            action = RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEVIDEO;
            int ltypes = SharedPreferencesUtil.getInt(view.getContext(), "vstype", 0);
         			if (ltypes==1) {
         	            Toast.makeText(view.getContext(), "对方未开启视频聊天", Toast.LENGTH_SHORT).show();

         				
         				return;	
         			}else {
         				
         			}
        } else {
            action = RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEAUDIO;
            int ltype = SharedPreferencesUtil.getInt(view.getContext(), "astype", 0);
        	
	        if (ltype==1) {
 	            Toast.makeText(view.getContext(), "对方未开启语音聊天", Toast.LENGTH_SHORT).show();

	        	
	        	return;
			}else {
				
			}
        }
        
        
        DbUtil  dbutil = new DbUtil((TAApplication) view.getContext().getApplicationContext());
		List<Persion> list = ManageDataBase.SelectList(dbutil, Persion.class, null);
		if (list.size() > 0) {
			Persion	b_person = list.get(0);
			if (b_person.getToken()!=null&&b_person.getToken()>0) {

			}else {
	            Toast.makeText(view.getContext(), "您的金币不足，请充值后再聊天吧！", Toast.LENGTH_SHORT).show();

				return;
			}
		}
		 
		
	     
        Intent intent = new Intent(action);
        intent.setPackage(view.getContext().getPackageName());
        intent.putExtra("conversationType", message.getConversationType().getName().toLowerCase());
        intent.putExtra("targetId", message.getTargetId());
        intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
        view.getContext().startActivity(intent);
    }

    @Override
    public void onItemLongClick(final View view, int position, final CallSTerminateMessage content, final UIMessage message) {
        String name = null;

        if (message.getSenderUserId() != null) {
            UserInfo userInfo = message.getUserInfo();
            if (userInfo == null || userInfo.getName() == null)
                userInfo = RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId());

            if (userInfo != null)
                name = userInfo.getName();
        }

        String[] items;

        items = new String[] {view.getContext().getResources().getString(R.string.rc_dialog_item_message_delete)};

        ArraysDialogFragment.newInstance(name, items).setArraysDialogItemListener(new ArraysDialogFragment.OnArraysDialogItemListener() {
            @Override
            public void OnArraysDialogItemClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    RongIM.getInstance().deleteMessages(new int[] {message.getMessageId()}, null);
                }

            }
        }).show(((FragmentActivity) view.getContext()).getSupportFragmentManager());
    }
}
