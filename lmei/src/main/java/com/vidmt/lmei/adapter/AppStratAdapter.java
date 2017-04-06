package com.vidmt.lmei.adapter;

import java.util.List;






import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class AppStratAdapter extends PagerAdapter{
	private List<View> data;
	private Activity activity;
	public AppStratAdapter(List<View> data, Activity activity) {
		this.data = data;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
		
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}
	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated method stub
		((ViewPager) container).addView(data.get(position));
		
		if(data.size()-1==position){//判断导航页是不是最后一页
//			Button submit = (Button) container.findViewById(R.id.guide_start_app);
//			submit.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					
//					Intent intent = new Intent(activity, FooterPageActivity.class);
//					activity.startActivity(intent);
//				}
//			});
			
		}
		return data.get(position);
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}
