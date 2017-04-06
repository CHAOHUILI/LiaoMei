package com.vidmt.lmei.activity;

import java.util.ArrayList;
import java.util.List;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.R.menu;
import com.vidmt.lmei.activity.HomeActivity.MyOnClickListener;
import com.vidmt.lmei.activity.HomeActivity.MyOnPageChangeListener;
import com.vidmt.lmei.activity.HomeActivity.MyPagerAdapter;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyFriendsActivity extends BaseActivity {
	private long exitTime = 0;
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
	@TAInjectView(id = R.id.friendstv)
	TextView friendstv;
	@TAInjectView(id = R.id.attentiontv)
	TextView attentiontv;
	@TAInjectView(id = R.id.fanstv)
	TextView fanstv;
	@TAInjectView(id = R.id.selectfriendslin)
	LinearLayout selectfriendslin;
	LocalActivityManager manager = null;
	@TAInjectView(id = R.id.home_viewpager)
	ViewPager viewPager;
	private int code = 1;
	MyPagerAdapter mapageadapter;
	int friendstype=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_friends);
		themes();
		InitView();
	}

	@Override
	public void InitView() {
		// TODO Auto-generated method stub
		super.InitView();
		headerthemeleft.setVisibility(View.GONE);
		headerright.setVisibility(View.VISIBLE);
		headconrel1.setVisibility(View.GONE);
		headercontentv.setVisibility(View.GONE);
		headercontent.setText("好友");
		Drawable drawablemore = context.getResources().getDrawable(R.drawable.addfriends);
		typelog.setBackgroundDrawable(drawablemore);
		FriendsActivity.dialog = loadingDialog;
		AttentionActivity.dialog = loadingDialog;
		FansActivity.dialog = loadingDialog;
		manager = new LocalActivityManager(this, true);
		manager.dispatchResume();
		initTextView();
		initPagerViewer();
		selectfriendslin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyFriendsActivity.this, SelectFriendsActivity.class);
				intent.putExtra("flag", code);
				intent.putExtra("friendstype", friendstype);
				startActivity(intent);
			}
		});
		headerright.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StartActivity(SelectStrangerActivity.class);
			}
		});
		registerclear();
	}

	private void registerclear()
	{
		BroadcastReceiver broadcastReceiver = new BroadcastReceiver()

		{
			@Override
			public void onReceive(Context context, Intent intent)
			{
				Intent intents = new Intent("friendschild");
				sendBroadcast(intents);
			}
		};
		IntentFilter intentToReceiveFilter = new IntentFilter();
		intentToReceiveFilter.addAction("friends");
		registerReceiver(broadcastReceiver, intentToReceiveFilter);
	}
	/**
	 * 初始化标题
	 */
	private void initTextView() {
		friendstv.setOnClickListener(new MyOnClickListener(1));
		attentiontv.setOnClickListener(new MyOnClickListener(2));
		fanstv.setOnClickListener(new MyOnClickListener(3));
	}

	/**
	 * 初始化PageViewer
	 */
	private void initPagerViewer() {
		final ArrayList<View> list = new ArrayList<View>();
		View view = null;
		for (int i = 0; i < 3; i++) {
			list.add(view);
		}
		mapageadapter=new MyPagerAdapter(list);
		viewPager.setAdapter(mapageadapter);
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		Intent intents;
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				friendstv.setTextColor(getResources().getColor(R.color.authentication));
				attentiontv.setTextColor(getResources().getColor(R.color.gray));
				fanstv.setTextColor(getResources().getColor(R.color.gray));
				 intents = new Intent("friendschild");
				sendBroadcast(intents);
				friendstype=1;
				break;
			case 1:
				friendstv.setTextColor(getResources().getColor(R.color.gray));
				attentiontv.setTextColor(getResources().getColor(R.color.authentication));
				fanstv.setTextColor(getResources().getColor(R.color.gray));
				 intents = new Intent("friendschild");
				sendBroadcast(intents);
				friendstype=2;
				break;
			case 2:
				friendstv.setTextColor(getResources().getColor(R.color.gray));
				attentiontv.setTextColor(getResources().getColor(R.color.gray));
				fanstv.setTextColor(getResources().getColor(R.color.authentication));
				 intents = new Intent("friendschild");
				sendBroadcast(intents);
				friendstype=3;
				break;
			}
			code = arg0 + 1;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}
		Intent intents;
		@Override
		public void onClick(View v) {
			if (index == 1) {
				friendstv.setTextColor(getResources().getColor(R.color.authentication));
				attentiontv.setTextColor(getResources().getColor(R.color.gray));
				fanstv.setTextColor(getResources().getColor(R.color.gray));
				friendstype=1;
			} else if (index == 2) {
				friendstv.setTextColor(getResources().getColor(R.color.gray));
				attentiontv.setTextColor(getResources().getColor(R.color.authentication));
				fanstv.setTextColor(getResources().getColor(R.color.gray));
				friendstype=2;
			} else if (index == 3) {
				friendstv.setTextColor(getResources().getColor(R.color.gray));
				attentiontv.setTextColor(getResources().getColor(R.color.gray));
				fanstv.setTextColor(getResources().getColor(R.color.authentication));
				friendstype=3;
				
			}
			code = index;
			viewPager.setCurrentItem(index - 1);
		}
	};

	/**
	 * Pager适配器
	 */

	public class MyPagerAdapter extends PagerAdapter {
		List<View> list = new ArrayList<View>();

		public MyPagerAdapter(ArrayList<View> list) {
			this.list = list;
		}

		private int mChildCount = 0;

		@Override
		public void notifyDataSetChanged() {
			mChildCount = getCount();
			super.notifyDataSetChanged();
		}

		@Override
		public int getItemPosition(Object object) {
			if (mChildCount > 0) {
				mChildCount--;
				return POSITION_NONE;
			}
			return super.getItemPosition(object);
		}

		// 只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ViewPager pViewPager = ((ViewPager) container);
			pViewPager.removeView(list.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {

			ViewPager pViewPager = ((ViewPager) arg0);
			View view = list.get(arg1);
			Intent intent = null;
			if (arg1 == 0 && view == null) {
				intent = new Intent(MyFriendsActivity.this, FriendsActivity.class);
				view = getView("FriendsActivity", intent);
				list.remove(0);
				list.add(0, view);

			} else if (arg1 == 1 && view == null) {
				intent = new Intent(MyFriendsActivity.this, AttentionActivity.class);
				view = getView("AttentionActivity", intent);
				list.remove(1);
				list.add(1, view);
			} else if (arg1 == 2 && view == null) {
				intent = new Intent(MyFriendsActivity.this, FansActivity.class);
				view = getView("FansActivity", intent);
				list.remove(2);
				list.add(2, view);
			}
			pViewPager.addView(view);
			return view;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}

	/**
	 * 通过activity获取视图
	 * 
	 * @param id
	 * @param intent
	 * @return
	 */
	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.exit), Toast.LENGTH_SHORT)
					.show();
			exitTime = System.currentTimeMillis();
		} else {
			exitApp();
		}
	}
}
