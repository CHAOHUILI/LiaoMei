package com.vidmt.lmei.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.R.menu;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.entity.Aliacount;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.rule.ManageDataBase;
import com.vidmt.lmei.util.think.JsonUtil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 用户钱包页
 */
public class UserWalletActivity extends BaseActivity {
	@TAInjectView(id = R.id.headerthemeleft)
	RelativeLayout headerthemeleft;
	@TAInjectView(id = R.id.user)
	ImageView user;
	@TAInjectView(id = R.id.headerright)
	LinearLayout headerright;
	@TAInjectView(id = R.id.typelog)
	ImageView typelog;
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

	@TAInjectView(id = R.id.balance)
	TextView balance;
	@TAInjectView(id = R.id.zj)
	TextView zj;
	@TAInjectView(id = R.id.hint)
	TextView hint;
	@TAInjectView(id = R.id.img_top_up)
	TextView img_top_up;
	@TAInjectView(id = R.id.walletzh)
	TextView walletzh;
	@TAInjectView(id = R.id.img_withdraw)
	TextView img_withdraw;
	@TAInjectView(id = R.id.income)
	RelativeLayout income;
	@TAInjectView(id = R.id.expend)
	RelativeLayout expend;
	@TAInjectView(id = R.id.tuikuan)
	RelativeLayout tuikuan;
	@TAInjectView(id = R.id.account)
	RelativeLayout account;
	@TAInjectView(id = R.id.top_up)
	RelativeLayout top_up;
	@TAInjectView(id = R.id.withdraw)
	RelativeLayout withdraw;
	List<Aliacount> listali = new ArrayList<Aliacount>();;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_wallet);
		themes();
		InitView();
		// LoadDataUpdate();
		// LoadData();
		loadingDialog.show();
		userrefresh();
		LoadData();
		LoadDataUpdate();
	}

	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
		headerthemeleft.setVisibility(View.VISIBLE);
		headerright.setVisibility(View.VISIBLE);
		headconrel1.setVisibility(View.GONE);
		headercontentv.setVisibility(View.GONE);
		headercontent.setText("我的钱包");
		Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
		user.setBackgroundDrawable(drawable);
	}

	@Override
	protected void onAfterSetContentView() {
		// TODO Auto-generated method stub
		super.onAfterSetContentView();
		OnClickListener onClickListener = new OnClickListener() {
			/**
			 * @param v
			 */
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.headerthemeleft:
					finish();
					break;
				case R.id.img_top_up:
					StartActivity(UserRechargeActivity.class);
					break;
				case R.id.walletzh:
					//转成金币
					if(b_person.getCapitalBalance()>=1.0){
						new TransitionShowView(UserWalletActivity.this, walletzh);
					}else{
						ToastShow("资金不足，请核实你的可提现金额");
					}					
					break;
				// 打开提现界面
				case R.id.img_withdraw:
					if (listali.isEmpty()) {
						ToastShow("您还没有设置提现账号哦！");
					} else {
						StartActivity(UserWithdrawalActivity.class);
					}
					break;
				// 打开收入记录
				case R.id.income:
					Intent in1 = new Intent();
					in1.setClass(UserWalletActivity.this, UserIncomeActivity.class);
					in1.putExtra("name", "income");
					startActivity(in1);
					break;
				// 打开支出记录
				case R.id.expend:
					StartActivity(UserExpendActivity.class);
					break;

				// 打开充值记录
				case R.id.top_up:
					StartActivity(UserRechargeRecordActivity.class);
					break;
				// 打开提现记录
				case R.id.withdraw:
					StartActivity(UserWithdrawRecordActivity.class);
					break;
				// 打开提现账户
				case R.id.account:
					if (listali.isEmpty()) {
						// 如果没有数据 就去绑定
						StartActivity(UserWithdrawalAccountActivity.class);
					} else {
						// 有数据就是修改
						AddzfbActivity.ali = listali.get(0);
						Intent intent = new Intent(UserWalletActivity.this, AddzfbActivity.class);
						intent.putExtra("alaccount", 1);
						startActivity(intent);
					}
					// **********************************************************************************************
					break;
				}
			}
		};
		img_top_up.setOnClickListener(onClickListener);
		img_withdraw.setOnClickListener(onClickListener);
		income.setOnClickListener(onClickListener);
		expend.setOnClickListener(onClickListener);
		top_up.setOnClickListener(onClickListener);
		withdraw.setOnClickListener(onClickListener);
		account.setOnClickListener(onClickListener);
		headerthemeleft.setOnClickListener(onClickListener);
		walletzh.setOnClickListener(onClickListener);
	}

	public class TransitionShowView extends PopupWindow {

		public TransitionShowView(Context mContext, View parent) {

			View view = View.inflate(mContext, R.layout.transition_loading, null);
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

					dismiss();
					loadingDialog.show();
					transition();
				}
			});
			btn_dialog_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					dismiss();
				}
			});
		}
	}

	private void userrefresh() {
		BroadcastReceiver broadcastReceiver = new BroadcastReceiver()

		{
			@Override
			public void onReceive(Context context, Intent intent) {
				LoadData();
				LoadDataUpdate();
			}
		};
		IntentFilter intentToReceiveFilter = new IntentFilter();
		intentToReceiveFilter.addAction("wallet");
		registerReceiver(broadcastReceiver, intentToReceiveFilter);
	}

	/**
	 * 	加载绑定的支付宝信息
	 */
	@Override
	public void LoadData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String json = Person_Service.selectByPersionId(b_person.getId());
				Message msg = mUIHandler.obtainMessage(1);
				msg.obj = json;
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
				String str = Person_Service.loginupdage(b_person.getId());
				Message msg = mUIHandler.obtainMessage(4);
				msg.obj = str;
				msg.sendToTarget();
			}
		}).start();
	}

	public void transition() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String str = Person_Service.transition(b_person.getId());
				Message msg = mUIHandler.obtainMessage(5);
				msg.obj = str;
				msg.sendToTarget();
			}
		}).start();
	}

	public Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (msg.obj != null) {
					String mes = (String) msg.obj;
					// ToastShow(mes);
					if (!mes.equals("") && !mes.equals(JsonUtil.ObjToJson(Constant.ERROR))) {
						try {
							List<Aliacount> listali1 = JsonUtil.JsonToObj(mes, new TypeToken<List<Aliacount>>() {
							}.getType());
							listali.addAll(listali1);
							hint.setHint((listali.isEmpty()) ? "未绑定" : "已绑定");
						} catch (Exception e) {
							LoadData();
						}
						
					} else {
						hint.setHint("未绑定");
						// ToastShow("访问网络超时");
					}
				} else {
					hint.setHint("未绑定");
				}
				break;
			case 5:
				if (msg.obj != null && !msg.obj.equals("")) {
					String str = (String) msg.obj;
					// ToastShow(str);
					if (!str.equals("") && str.equals(JsonUtil.ObjToJson(Constant.SUCCESS))) {
						ToastShow("转换成功");
						LoadDataUpdate();
					}else if (!str.equals("") && str.equals(JsonUtil.ObjToJson("param_error"))){
						ToastShow("转换异常");
					}else if (!str.equals("") && str.equals(JsonUtil.ObjToJson("cap_error"))){
						ToastShow("资金不足，请核实你的可提现金额");
					}else{
						ToastShow("网络异常请稍后再试");
					}
					
				}
				loadingDialog.dismiss();
				break;
			case 4:
				if (msg.obj != null && !msg.obj.equals("")) {
					String str = (String) msg.obj;
					// ToastShow(str);
					if (!str.equals("") && !str.equals(JsonUtil.ObjToJson(Constant.ERROR))) {
						try {
							Persion p = JsonUtil.JsonToObj(str, Persion.class);
							ManageDataBase.Delete(dbutil, Persion.class, p.getId() + "");
							ManageDataBase.Insert(dbutil, Persion.class, p);
							b_person = p;
							balance.setText(b_person.getToken() + "金币");
							zj.setText("¥" + b_person.getCapitalBalance()+" 元");
						} catch (Exception e) {
							LoadDataUpdate();
						}						
					}
				}
				loadingDialog.dismiss();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_wallet, menu);
		return true;
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
