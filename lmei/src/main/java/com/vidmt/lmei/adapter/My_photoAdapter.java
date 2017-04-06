package com.vidmt.lmei.adapter;

import java.util.HashMap;
import java.util.List;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vidmt.lmei.R;
import com.vidmt.lmei.entity.Image;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class My_photoAdapter extends BaseAdapter {

	Context context;
	List<Image> list;
	ImageLoader imageLoader;
	DisplayImageOptions options;
	int delnum;
	public boolean delswitch;//true 删除图标可见 false 不可见;
	public My_photoAdapter(Context context, List<Image> list, ImageLoader imageLoader, DisplayImageOptions options) {
		super();
		this.context = context;
		this.list = list;
		this.imageLoader = imageLoader;
		this.options = options;
	}

	public int getCount() {
		return list.size();
	}

	public Image getItem(int arg0) {
		return list.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}
	HashMap<Integer,View> lmap = new HashMap<Integer,View>();
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(lmap.get(position) == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.photo_grid, parent, false);
			holder.photo = (ImageView) convertView.findViewById(R.id.photo1);
			holder.del = (ImageView) convertView.findViewById(R.id.del);
			lmap.put(position, convertView);
			convertView.setTag(holder);
		} else {
			convertView=lmap.get(position);
			holder = (ViewHolder) convertView.getTag();
		}
		Image image = list.get(position);
		if(!delswitch){
			holder.del.setVisibility(View.GONE);
		}
		if(image.getStatus()==1){
			if("up".equals(image.getPath()))
			{
				holder.photo.setImageResource(R.drawable.up);
			}else if(image.getBitmap()!=null){
			holder.photo.setImageBitmap((Bitmap)image.getBitmap());
			}
		}else{
		imageLoader.displayImage(image.getPath(), holder.photo, options);
		}
		 convertView.setLayoutParams(new AbsListView.LayoutParams((int)(parent.getWidth()/4), (int) (parent.getWidth()/4)));
		 return convertView;
	}


	static class ViewHolder {
		ImageView photo;// 
		ImageView del;
	}
	

}
