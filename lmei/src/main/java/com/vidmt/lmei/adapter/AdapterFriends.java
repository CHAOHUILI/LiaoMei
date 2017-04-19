package com.vidmt.lmei.adapter;

import java.util.Collections;
import java.util.List;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vidmt.lmei.R;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.think.PinyinComparator;
import com.vidmt.lmei.widget.RoundImageView;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * 黑名单，朋友，关注，粉丝
 */
public class AdapterFriends extends BaseAdapter {
	private Context context;
	private List<Persion> list;
	protected ImageLoader imageLoader;
	protected DisplayImageOptions options;
	public AdapterFriends(Context context,List<Persion> list,ImageLoader imageLoader,
			DisplayImageOptions options)
	{
		this.context = context;
		this.list = list;
		this.imageLoader = imageLoader;
		this.options = options;
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
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null)
		{
			holder = new ViewHolder();
			LayoutInflater lif = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = lif.inflate(R.layout.adapter_friends, null);
			holder.letterTV = (TextView) convertView.findViewById(R.id.letter);
			holder.friendsico= (RoundImageView) convertView.findViewById(R.id.friendsico);
			holder.friendsname= (TextView) convertView.findViewById(R.id.friendsname);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		
			Persion person = (Persion)list.get(position);
			
			if(person!=null)
			{
				if(isFirst(person, position)){
					holder.letterTV.setVisibility(View.VISIBLE);
					holder.letterTV.setText(person.getEnglishSorting());
					imageLoader.displayImage(person.getPhoto(), holder.friendsico, options);				
					holder.friendsname.setText(person.getNick_name());
				}else{
					holder.letterTV.setVisibility(View.GONE);
					imageLoader.displayImage(person.getPhoto(), holder.friendsico, options);				
					holder.friendsname.setText(person.getNick_name());
				}
				
								
			}
		return convertView;
	}
	public boolean isFirst(Persion item, int position){
		int pos = getFirstPosAtList(item.getEnglishSorting());
		if(pos == position){
			return true;
		}
		return false;
	}
	public int getFirstPosAtList(String letter){
		for(int i = 0; i < list.size(); i++){
			Persion item = (Persion) list.get(i);
			if(item.getEnglishSorting().equals(letter)){
				return i;
			}
		}
		return -1;
	}
	class ViewHolder
	{
		
	
		TextView friendsname;
		RoundImageView friendsico;
		TextView letterTV;
	}
}
