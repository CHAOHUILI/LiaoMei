package com.vidmt.lmei.dialog;

import com.vidmt.lmei.activity.ConversationListActivity;
import com.vidmt.lmei.R;
import com.vidmt.lmei.activity.MessagesActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import io.rong.imkit.model.UIConversation;

/**
 * 消息页长按消息，弹出从会话列表移除提示
 */
public class ConversionlistDialog extends BaseDialog{


	private TextView contop;//置顶
	private TextView contile;//头部
	private TextView condel;//删除
	private TextView quxiao;//取消
	private LinearLayout contopout;
    private UIConversation uiConversation;
     private int position;

	public ConversionlistDialog(Context context,UIConversation uiConversation) {
		super(context, R.style.WinDialog);
		this.context=context;
		this.uiConversation =uiConversation;
		// TODO Auto-generated constructor stub
	}
	public ConversionlistDialog(Context context,int position) {
		super(context, R.style.WinDialog);
		this.context=context;
		this.position = position;
		
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.condialog);	
		contop = (TextView) findViewById(R.id.contop);
		contile = (TextView) findViewById(R.id.contile);
		condel = (TextView) findViewById(R.id.condel);
		quxiao = (TextView) findViewById(R.id.quxiao);
		contopout= (LinearLayout) findViewById(R.id.contopout);
		
		
		if (uiConversation==null) {
			contopout.setVisibility(View.GONE);
			contop.setVisibility(View.GONE);
			contile.setText("删除消息");
			condel.setText("从消息列表删除");
			quxiao.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
				}
			});
			condel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					MessagesActivity.messagesActivity.delmessage(position);
					dismiss();
				}
			});

		}else {
			if (uiConversation.isTop()) {
				
				contop.setText("取消置顶");
			}else {
				contop.setText("置顶该会话");
			}
			contile.setText(uiConversation.getUIConversationTitle());
			

			quxiao.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
				}
			});
			contop.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ConversationListActivity.conversationListActivity.sss(uiConversation,1);
					dismiss();
				}
			});
			
			condel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ConversationListActivity.conversationListActivity.sss(uiConversation,2);
					dismiss();
				}
			});

		}
	
	}
}