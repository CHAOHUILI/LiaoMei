package com.vidmt.lmei.adapter;

import java.util.Collections;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vidmt.lmei.R;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.entity.Hobby;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.think.JsonUtil;
import com.vidmt.lmei.util.think.PinyinComparator;
import com.vidmt.lmei.widget.RoundImageView;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class AdapterSysHobby extends BaseAdapter {
	private Context context;
	private List<Hobby> list;
	protected ImageLoader imageLoader;
	protected DisplayImageOptions options;
	ViewHolder holder = null;
	Persion person;
	private int persion_id;

	public AdapterSysHobby(Context context, List<Hobby> list) {
		this.context = context;
		this.list = list;
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

		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater lif = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = lif.inflate(R.layout.adapter_sys_hobby, null);
			holder.hobbyname = (TextView) convertView.findViewById(R.id.hobbyname);
			holder.hobbylin = (LinearLayout) convertView.findViewById(R.id.hobbylin);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Hobby hobby = list.get(position);

		if (hobby != null) {
			holder.hobbyname.setText(hobby.getHobbycontent());
			if(hobby.getIsSelected()==1){
				Drawable drawable = context.getResources().getDrawable(R.drawable.hobbyw);
				holder.hobbylin.setBackgroundDrawable(drawable);
				holder.hobbyname.setTextColor(context.getResources().getColor(R.color.authentication));
			}else{
				Drawable drawable = context.getResources().getDrawable(R.drawable.hobbyx);
				holder.hobbylin.setBackgroundDrawable(drawable);
				holder.hobbyname.setTextColor(context.getResources().getColor(R.color.white));
			}
		}
		return convertView;
	}

	class ViewHolder {
		TextView hobbyname;
		LinearLayout hobbylin;
	}
}
