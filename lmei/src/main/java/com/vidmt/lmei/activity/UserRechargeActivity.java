package com.vidmt.lmei.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ta.annotation.TAInjectView;
import com.ta.util.http.RequestParams;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.R.menu;
import com.vidmt.lmei.activity.HomeDetailActivity.AttentionView;
import com.vidmt.lmei.adapter.AdapterRecharegePackage;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.dialog.ConnectionUtil;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.entity.Recharge;
import com.vidmt.lmei.entity.TokenPackage;
import com.vidmt.lmei.util.rule.HttpUtil;
import com.vidmt.lmei.util.rule.ManageDataBase;
import com.vidmt.lmei.util.think.JsonUtil;
import com.vidmt.lmei.widget.GoodlistView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import pay.alipay.sign.AuthResult;
import pay.alipay.sign.PayResult;

public class UserRechargeActivity extends BaseActivity {
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

	List<TokenPackage> lsittoken;
	AdapterRecharegePackage adapterrecharegepackage;
	@TAInjectView(id = R.id.rechargeuserid)
	TextView rechargeuserid;
	@TAInjectView(id = R.id.rechangeuserye)
	TextView rechangeuserye;
	@TAInjectView(id = R.id.rechargelist)
	GoodlistView rechargelist;
	@TAInjectView(id = R.id.rela_userphoterror)
	RelativeLayout rela_userphoterror;
	@TAInjectView(id = R.id.zfbimg)
	ImageView zfbimg;
	@TAInjectView(id = R.id.wximg)
	ImageView wximg;
	@TAInjectView(id = R.id.rechargepay)
	TextView rechargepay;
	@TAInjectView(id = R.id.mywxplay)
	LinearLayout mywxplay;
	@TAInjectView(id = R.id.myzfbplay)
	LinearLayout myzfbplay;
	@TAInjectView(id = R.id.ll)
	LinearLayout ll;
	int zftype = 0;
	int money = 0;
	String ddh;
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_AUTH_FLAG = 2;

	TokenPackage tokenpackage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_recharge);
		themes();
		loadingDialog.show();
		InitView();
		LoadTokenData();
	}

	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
		headerthemeleft.setVisibility(View.VISIBLE);
		headerright.setVisibility(View.VISIBLE);
		headconrel1.setVisibility(View.GONE);
		headercontentv.setVisibility(View.GONE);
		headercontent.setText("充值");
		Drawable drawablezfb = context.getResources().getDrawable(R.drawable.rechangex);
		zfbimg.setBackgroundDrawable(drawablezfb);
		zftype = 1;
		Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
		user.setBackgroundDrawable(drawable);
		lsittoken = new ArrayList<TokenPackage>();
		adapterrecharegepackage = new AdapterRecharegePackage(lsittoken, context, imageLoader, options);
		rechargelist.setAdapter(adapterrecharegepackage);
		rechargelist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				tokenpackage = lsittoken.get(position);
				adapterrecharegepackage.setSeclection(position);
				adapterrecharegepackage.notifyDataSetChanged();
			}
		});
		rechargeuserid.setText(b_person.getOtherkey() + "");
		rechangeuserye.setText(b_person.getToken() + "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_recharge, menu);
		return true;
	}

	public void LoadTokenData() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// dialog.show();
				Message msg = mUHandler.obtainMessage(1);
				try {
					msg.obj = Person_Service.selecttokeninfo();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					msg.obj = null;
				}
				msg.sendToTarget();
			}
		}).start();
	}

	private Handler mUHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (msg.obj != null) {
					String json = (String) msg.obj;
					if (!json.equals("") && !json.equals(JsonUtil.ObjToJson(Constant.ERROR))
							&& !json.equals(JsonUtil.ObjToJson("param_error"))) {
						List<TokenPackage> lists = JsonUtil.JsonToObj(json, new TypeToken<List<TokenPackage>>() {
						}.getType());
						lsittoken.clear();
						lsittoken.addAll(lists);
						// Collections.sort(personlist, new PinyinComparator());
						rechargelist.setVisibility(View.VISIBLE);
						rela_userphoterror.setVisibility(View.GONE);
					} else {
						rechargelist.setVisibility(View.GONE);
						rela_userphoterror.setVisibility(View.VISIBLE);
					}
				} else {
					rechargelist.setVisibility(View.GONE);
					rela_userphoterror.setVisibility(View.VISIBLE);
				}

				adapterrecharegepackage.setSeclection(-1);
				adapterrecharegepackage.notifyDataSetChanged();
				loadingDialog.dismiss();
				break;
			default:
				break;
			}
		}

	};

	public void LoadDataUpdate(final int pid) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String str = Person_Service.loginupdage(pid);
				Message msg = mUIHandler.obtainMessage(4);
				msg.obj = str;
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

			case 4:
				if (msg.obj != null && !msg.obj.equals("")) {
					String str = (String) msg.obj;
					if (!str.equals("") && !str.equals(JsonUtil.ObjToJson(Constant.ERROR))) {
						Persion p = JsonUtil.JsonToObj(str, Persion.class);
						ManageDataBase.Delete(dbutil, Persion.class, null);
						ManageDataBase.Insert(dbutil, Persion.class, p);
						b_person = p;
						rechangeuserye.setText(b_person.getToken() + "");
					}
				}
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onAfterSetContentView() {
		// TODO Auto-generated method stub
		super.onAfterSetContentView();
		OnClickListener onClickListener = new OnClickListener() {
			Drawable drawablezfb;
			Drawable drawablewx;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.headerthemeleft:
					finish();
					Intent intents = new Intent("wallet");
					sendBroadcast(intents);
					break;
				case R.id.myzfbplay:

					drawablezfb = context.getResources().getDrawable(R.drawable.rechangex);
					zfbimg.setBackgroundDrawable(drawablezfb);
					drawablewx = context.getResources().getDrawable(R.drawable.rechangew);
					wximg.setBackgroundDrawable(drawablewx);
					zftype = 1;
					break;
				case R.id.mywxplay:
					drawablezfb = context.getResources().getDrawable(R.drawable.rechangew);
					zfbimg.setBackgroundDrawable(drawablezfb);
					drawablewx = context.getResources().getDrawable(R.drawable.rechangex);
					wximg.setBackgroundDrawable(drawablewx);
					zftype = 2;
					break;
				case R.id.rechargepay:
					if (zftype == 1) {
						if(tokenpackage==null){
							ToastShow("请选择 套餐");
						}else{
							loadingDialog.show();
						PayIns();
						}
					} else if (zftype == 2) {
						ToastShow("暂未开放");
					} else {
						ToastShow("请选择支付方式!");
					}
					break;
				default:
					break;
				}
			}

		};
		headerthemeleft.setOnClickListener(onClickListener);
		myzfbplay.setOnClickListener(onClickListener);
		mywxplay.setOnClickListener(onClickListener);
		rechargepay.setOnClickListener(onClickListener);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			this.finish();
			Intent intents = new Intent("wallet");
			sendBroadcast(intents);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	private void PayIns() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Recharge re = new Recharge();
				re.setPersion_id(b_person.getId());// 用户id
				re.setToken_package(tokenpackage.getId());// 套餐id
				re.setAmount(tokenpackage.getMoney());// 价格
				// 2016111702913424
				String json = Person_Service.PayIns(tokenpackage.getId(), b_person.getId());
				Message msg = mHandler.obtainMessage(3);
				msg.obj = json;
				msg.sendToTarget();
			}
		}).start();

	}

	public class RechargeView extends PopupWindow {

		public RechargeView(Context mContext, View parent,int type) {

			View view = View.inflate(mContext, R.layout.recharge_loading, null);
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
			if(type==1){
				attentionts.setText("充值成功");
			}else if(type==2){
				attentionts.setText("充值失败");
			}
			btn_dialog_confirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					LoadDataUpdate(b_person.getId());
					dismiss();
				}
			});			
		}
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				@SuppressWarnings("unchecked")
				PayResult payResult = new PayResult((Map<String, String>) msg.obj);
				/**
				 * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
				 */
				loadingDialog.dismiss();
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为9000则代表支付成功
				if (TextUtils.equals(resultStatus, "9000")) {
					// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
					//Toast.makeText(UserRechargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
					new RechargeView(UserRechargeActivity.this, ll, 1);
					
				} else {
					// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
					//Toast.makeText(UserRechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
					new RechargeView(UserRechargeActivity.this, ll, 2);
				}			
				break;
			}
			case SDK_AUTH_FLAG: {
				@SuppressWarnings("unchecked")
				AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
				String resultStatus = authResult.getResultStatus();

				// 判断resultStatus 为“9000”且result_code
				// 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
				if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
					// 获取alipay_open_id，调支付时作为参数extern_token 的value
					// 传入，则支付账户为该授权账户
					Toast.makeText(UserRechargeActivity.this,
							"授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
							.show();
				} else {
					// 其他状态值则为授权失败
					Toast.makeText(UserRechargeActivity.this,
							"授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

				}
				break;

			}
			case 3:
				if (msg.obj != null) {
					String string = (String) msg.obj;
					if ("tokenpackage_error".equals(string)||"save_error".equals(string)||"".equals(string)||"param_error".equals(string)) {
						ToastShow("操作失败，请重试");
						loadingDialog.dismiss();
					} else {
						// ddh=JsonUtil.JsonToObj(string, String.class);
						ddh = string;
						pay();// 支付宝付款
					}
				}
				break;

			case 4:
				if (msg.obj != null && !msg.obj.equals("")) {
					String str = (String) msg.obj;
					if (!str.equals("") && !str.equals(JsonUtil.ObjToJson(Constant.ERROR))) {
						Persion p = JsonUtil.JsonToObj(str, Persion.class);
						ManageDataBase.Delete(dbutil, Persion.class, p.getId() + "");
						ManageDataBase.Insert(dbutil, Persion.class, p);
						b_person = p;
					}
				}
				break;
			default:
				break;
			}
		};
	};

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay() {
		// 订单
		// String orderInfo = getOrderInfo(subject,body,price,ddh);
		// 对订单做RSA 签名
		// String sign = sign(orderInfo);

		//
		// // 完整的符合支付宝参数规范的订单信息
		// final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
		// + getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(UserRechargeActivity.this);

				JSONObject jsStr;
				String ranking = null;
				try {
					jsStr = new JSONObject(ddh);
					ranking = jsStr.getString("orderString");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 调用支付接口，获取支付结果

				Map<String, String> result = alipay.payV2(ranking, true);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
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
