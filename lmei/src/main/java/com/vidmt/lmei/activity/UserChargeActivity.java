package com.vidmt.lmei.activity;

import java.util.ArrayList;
import java.util.List;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.R.menu;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.rule.ManageDataBase;
import com.vidmt.lmei.util.think.JsonUtil;
import com.vidmt.lmei.widget.ChangeTextStyleNumberPicker;
import com.vidmt.lmei.widget.PickerView;
import com.vidmt.lmei.widget.PickerView.onSelectListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;
import android.widget.NumberPicker.OnScrollListener;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 用户设置如何收费，聊天，视频和语言
 */
public class UserChargeActivity extends BaseActivity {

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

	@TAInjectView(id = R.id.chatchargerel)
	RelativeLayout chatchargerel;
	@TAInjectView(id = R.id.videochargerel)
	RelativeLayout videochargerel;
	@TAInjectView(id = R.id.voicechargerel)
	RelativeLayout voicechargerel;
	@TAInjectView(id = R.id.ll)
	LinearLayout ll;
	@TAInjectView(id = R.id.chatcharge)
	TextView chatcharge;
	@TAInjectView(id = R.id.videocharge)
	TextView videocharge;
	@TAInjectView(id = R.id.voicecharge)
	TextView voicecharge;
	String chargemoney = "";
	int voice_money = 0;
	int video_money = 0;
	int sms_money = 5;
	int typecharge = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_charge);
		themes();
		loadingDialog.show();
		InitView();
		LoadData();
	}

	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
		headerthemeleft.setVisibility(View.VISIBLE);
		headerright.setVisibility(View.VISIBLE);
		headconrel1.setVisibility(View.GONE);
		headercontentv.setVisibility(View.GONE);
		headercontent.setText("收费设置");
		Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
		user.setBackgroundDrawable(drawable);
		typecharge = getIntent().getIntExtra("typecharge", 0);
		if (b_person.getSms_money() != null) {
			chatcharge.setText(b_person.getSms_money() + "金币/条");
		}
		if (b_person.getVideo_money() != null) {
			videocharge.setText(b_person.getVideo_money() + "金币/分钟 ");
		}
		if (b_person.getVoice_money() != null) {
			voicecharge.setText(b_person.getVoice_money() + "金币/分钟");
		}
	}

	@Override
	protected void onAfterSetContentView() {
		// TODO Auto-generated method stub
		super.onAfterSetContentView();
		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {

				case R.id.headerthemeleft:

					finish();
					if (typecharge == 1) {
						Intent intents = new Intent("personupdate");
						sendBroadcast(intents);
					}
					break;
				case R.id.chatchargerel:
					new ChargeSettingsView(context, ll, Integer.valueOf(chargemoney), 3);
					break;
				case R.id.videochargerel:
					new ChargeSettingsView(context, ll, Integer.valueOf(chargemoney), 2);
					break;
				case R.id.voicechargerel:
					new ChargeSettingsView(context, ll, Integer.valueOf(chargemoney), 1);
					break;
				default:
					break;
				}
			}
		};
		headerthemeleft.setOnClickListener(onClickListener);
		videochargerel.setOnClickListener(onClickListener);
		voicechargerel.setOnClickListener(onClickListener);
		chatchargerel.setOnClickListener(onClickListener);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			if (typecharge == 1) {
				Intent intents = new Intent("personupdate");
				sendBroadcast(intents);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public class ChargeSettingsView extends PopupWindow {
		String money = "";

		@SuppressLint("NewApi")
		public ChargeSettingsView(Context mContext, View parent, int maxvalue, final int type) {

			View view = View.inflate(mContext, R.layout.charge_settings_loading, null);
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

			PickerView show_num_picker = (PickerView) view.findViewById(R.id.show_num_picker);
			List<String> seconds = new ArrayList<String>();
			if (type == 3) {
				for (int i = 0; i <= maxvalue / 10; i++) {
					// seconds.add(i < 10 ? "0" + i : "" + i);
					seconds.add("" + i * 10);
				}
			} else {
				for (int i = 1; i <= maxvalue / 10; i++) {
					// seconds.add(i < 10 ? "0" + i : "" + i);
					seconds.add("" + i * 10);
				}
			}
			show_num_picker.setData(seconds);
			int num = show_num_picker.mCurrentSelected;
			money = seconds.get(num);
			show_num_picker.setOnSelectListener(new onSelectListener() {

				@Override
				public void onSelect(String text) {
					money = text;
				}
			});
			TextView btn_dialog_confirm = (TextView) view.findViewById(R.id.btn_dialog_confirm);
			TextView btn_dialog_cancel = (TextView) view.findViewById(R.id.btn_dialog_cancel);
			btn_dialog_confirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (type == 1) {
						voice_money = Integer.valueOf(money);
						updatevoicecharge();
					} else if (type == 2) {
						video_money = Integer.valueOf(money);
						updatevideocharge();
					} else if (type == 3) {
						sms_money = Integer.valueOf(money);
						updatesmscharge();
					}

					dismiss();
				}
			});
			btn_dialog_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
				}
			});
		}

	}

	public void LoadData() {
		super.LoadData();
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String jhuser = Person_Service.getcharge(b_person.getLevel());
				Message msg = mUIHandler.obtainMessage(1);
				msg.obj = jhuser;
				msg.sendToTarget();
			}
		}).start();
	}

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

	public void updatevoicecharge() {
		// TODO Auto-generated method stub
		loadingDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String jhuser = Person_Service.updatevoicecharge(b_person.getId(), voice_money);
				Message msg = mUIHandler.obtainMessage(2);
				msg.obj = jhuser;
				msg.sendToTarget();
			}
		}).start();
	}

	public void updatevideocharge() {
		// TODO Auto-generated method stub
		loadingDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String jhuser = Person_Service.updatevideocharge(b_person.getId(), video_money);
				Message msg = mUIHandler.obtainMessage(5);
				msg.obj = jhuser;
				msg.sendToTarget();
			}
		}).start();
	}

	public void updatesmscharge() {
		// TODO Auto-generated method stub
		loadingDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String jhuser = Person_Service.updatesmscharge(b_person.getId(), sms_money);
				Message msg = mUIHandler.obtainMessage(6);
				msg.obj = jhuser;
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
			case 1:
				if (msg.obj != null) {
					String mes = (String) msg.obj;

					if (mes.equals(JsonUtil.ObjToJson("")) || mes.equals(JsonUtil.ObjToJson("error"))
							|| mes.equals(JsonUtil.ObjToJson("param_error"))) {
						ToastShow("收费获取失败");
					} else {

						chargemoney = JsonUtil.JsonToObj(mes, String.class);
					}
					loadingDialog.dismiss();
				}

				break;
			case 2:
				if (msg.obj != null) {
					String mes = (String) msg.obj;
					if (mes.equals(JsonUtil.ObjToJson(Constant.SUCCESS))) {
						voicecharge.setText(voice_money + "金币/分钟");
						voice_money = 0;
						ToastShow("语音收费修改成功");

					} else {
						voice_money = 0;
						ToastShow("语音收费修改失败");
					}
					LoadDataUpdate(b_person.getId());

				}

				break;

			case 6:
				if (msg.obj != null) {
					String mes = (String) msg.obj;
					if (mes.equals(JsonUtil.ObjToJson(Constant.SUCCESS))) {

						chatcharge.setText(sms_money + "金币/条");
						sms_money = 0;
						ToastShow("聊天收费修改成功");

					} else {

						sms_money = 0;
						ToastShow("聊天收费修改失败");

					}
					LoadDataUpdate(b_person.getId());

				}

				break;
			case 5:
				if (msg.obj != null) {
					String mes = (String) msg.obj;
					if (mes.equals(JsonUtil.ObjToJson(Constant.SUCCESS))) {

						videocharge.setText(video_money + "金币/分钟");
						video_money = 0;
						ToastShow("视频收费修改成功");

					} else {

						video_money = 0;
						ToastShow("视频收费修改失败");

					}
					LoadDataUpdate(b_person.getId());

				}

				break;
			case 4:
				if (msg.obj != null && !msg.obj.equals("")) {
					String str = (String) msg.obj;
					if (!str.equals("") && !str.equals(JsonUtil.ObjToJson(Constant.ERROR))) {
						Persion p = JsonUtil.JsonToObj(str, Persion.class);
						ManageDataBase.Delete(dbutil, Persion.class, null);
						ManageDataBase.Insert(dbutil, Persion.class, p);
						b_person = p;
						if (b_person.getSms_money() != null) {
							chatcharge.setText(b_person.getSms_money() + "金币/条");
						}
						if (b_person.getVideo_money() != null) {
							videocharge.setText(b_person.getVideo_money() + "金币/分钟");
						}
						if (b_person.getVoice_money() != null) {
							voicecharge.setText(b_person.getVoice_money() + "金币/分钟");
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
