package com.vidmt.lmei.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.vidmt.lmei.R;
import com.vidmt.lmei.entity.Present;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PersentAdapter extends BaseAdapter{
	private ArrayList<Present> list;
	private Context context;
	private LayoutInflater inflater;
	private int selectedPosition;
	private int type;

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options;
	public PersentAdapter(Context context, 
			ArrayList<Present> list ,int type) {
		this.context = context;
		this.list = list;
		inflater =LayoutInflater.from(context);
		this.type = type;
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.sisterlog)
				.showImageForEmptyUri(R.drawable.sisterlog)
				.showImageOnFail(R.drawable.sisterlog)
				.cacheInMemory()
				.cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(0))
				.build();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null)
		{
			holder = new ViewHolder();		
			convertView = inflater.inflate(R.layout.presentitem, null);
			holder.text_price = (TextView) convertView.findViewById(R.id.text_price);
			holder.persentimg = (ImageView) convertView.findViewById(R.id.persentimg);
			holder.peritem = (RelativeLayout) convertView.findViewById(R.id.peritem);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}

		Present cate = new Present();
		cate =list.get(position);
		holder.text_name.setText(cate.getPresent_name());
		imageLoader.displayImage(cate.getPresent_img(), holder.persentimg, options);
		if (cate.getPrice()==0) {
			holder.text_price.setText("免费");
		}else {
			holder.text_price.setText(cate.getPrice()+""+"金币");
		}

		if (type==1) {


			if (cate.getSelected()==1) {
				holder.peritem.setBackgroundResource(R.drawable.persentsbgs);
			}
			else
			{
				holder.peritem.setBackgroundResource(R.drawable.persentsbged);
			}
		}


		return convertView;
	}

	class ViewHolder
	{
		TextView text_name;//礼物名
		TextView text_price;//礼物价格
		ImageView persentimg;//礼物图片	
		RelativeLayout peritem;


	}
	public void setSelectedPosition(int position) {   
		selectedPosition = position;   
	}  

}
