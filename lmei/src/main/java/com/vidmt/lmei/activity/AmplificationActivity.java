package com.vidmt.lmei.activity;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.ta.TAApplication;
import com.vidmt.lmei.R;
import com.vidmt.lmei.adapter.ViewPagerAdapter;
import com.vidmt.lmei.entity.Image;
import com.vidmt.lmei.photoview.PhotoView;

import android.os.Bundle;
import android.os.Parcelable;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * 照片查看，放大 缩小
 * 
 * @author as
 *
 */
public class AmplificationActivity extends BaseActivity implements OnPageChangeListener {
	private Intent intent;
	private ViewPager viewPager;
	ViewPagerAdapter adapter;
	String t_photo;
	public static List<Image> goodsimglists = new ArrayList<Image>();
	int index;
	List<Image> list = new ArrayList<Image>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_amplification);
		list.addAll(goodsimglists);
		intent = getIntent();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getPath() != null) {
				if (list.get(i).getPath().equals("up")) {
					list.remove(i);
				}
			}
		}
		index = intent.getIntExtra("index", 0);
		viewPager = (ViewPager) findViewById(R.id.view_pager_amp);
		viewPager.setOnPageChangeListener(this);
		try {
			MyPagerAdapter adapter = new MyPagerAdapter(AmplificationActivity.this, getTAApplication());
			viewPager.setAdapter(adapter);
			viewPager.setCurrentItem(index);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public class MyPagerAdapter extends PagerAdapter {
		/*
		 * List<View> list = new ArrayList<View>(); public
		 * MyPagerAdapter(ArrayList<View> list) { this.list = list; }
		 */
		// String[] ima_path = t_photo.split("\\|");
		private TAApplication application;
		private Context context;

		public MyPagerAdapter(Context context, TAApplication application) {
			this.context = context;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = (View) object;
			container.removeView(view);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			try {
				return list.size();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return 0;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int index) {
			LayoutInflater lif = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = lif.inflate(R.layout.adapter_post_details, null, false);
			PhotoView photoView = null;
			try {
				photoView = (PhotoView) view.findViewById(R.id.post_ima_view);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.pBar_post);
			spinner.setVisibility(View.GONE);
			if (list.get(index).getStatus() == 1) {
				if (list.get(index).getBitmap() != null) {
					photoView.setImageBitmap((Bitmap) list.get(index).getBitmap());
				}
			} else {
				imageLoader.displayImage(list.get(index).getPath(), photoView, options,
						new SimpleImageLoadingListener() {
							@Override
							public void onLoadingStarted(String imageUri, View view) {
								// TODO Auto-generated method stub
								spinner.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
								// TODO Auto-generated method stub
								spinner.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingCancelled(String imageUri, View view) {
								// TODO Auto-generated method stub
								spinner.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
								// TODO Auto-generated method stub
								super.onLoadingComplete(imageUri, view, loadedImage);
								spinner.setVisibility(View.GONE);
							}

						});
			}
			container.addView(view);
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

	@Override
	public void InitView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		 int aa =list.size()-1;
		 if(aa==arg0){
			 ToastShow("已经是最后一页");
		 }else if(arg0==0){
			 ToastShow("已经是第一页");
		 }
	}

}
