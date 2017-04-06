package com.vidmt.lmei.activity;

import android.content.Context;
import android.view.View;
import io.rong.imkit.RongIM.ConversationListBehaviorListener;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.model.Conversation.ConversationType;

public class MyConversationListBehaviorListener implements ConversationListBehaviorListener{

	@Override
	public boolean onConversationClick(Context arg0, View arg1, UIConversation arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onConversationLongClick(Context arg0, View arg1, UIConversation arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onConversationPortraitClick(Context arg0, ConversationType arg1, String arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onConversationPortraitLongClick(Context arg0, ConversationType arg1, String arg2) {
		// TODO Auto-generated method stub
		return false;
	}

}
