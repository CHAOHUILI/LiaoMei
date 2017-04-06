package com.vidmt.lmei.adapter;

import java.util.Collections;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vidmt.lmei.R;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Person_Service;
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

public class AdapterStranger extends BaseAdapter {
	private Context context;
	private List<Persion> list;
	protected ImageLoader imageLoader;
	protected DisplayImageOptions options;
	ViewHolder holder = null;
	Persion person;
	private int persion_id;
	public AdapterStranger(Context context,List<Persion> list,ImageLoader imageLoader,
			DisplayImageOptions options,int persion_id)
	{
		this.context = context;
		this.list = list;
		this.imageLoader = imageLoader;
		this.options = options;
		this.persion_id=persion_id;
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
		
		if(convertView == null)
		{
			holder = new ViewHolder();
			LayoutInflater lif = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = lif.inflate(R.layout.adapter_stranger, null);
			holder.usersico= (RoundImageView) convertView.findViewById(R.id.usersico);
			holder.username= (TextView) convertView.findViewById(R.id.username);
			holder.usersex= (ImageView) convertView.findViewById(R.id.usersex);
			holder.userage= (TextView) convertView.findViewById(R.id.userage);
			holder.useraddres= (TextView) convertView.findViewById(R.id.useraddres);
			holder.usersign= (TextView) convertView.findViewById(R.id.usersign);
			holder.userconcerned= (TextView) convertView.findViewById(R.id.userconcerned);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		
			person = (Persion)list.get(position);
			
			if(person!=null)
			{
				imageLoader.displayImage(person.getPhoto(), holder.usersico, options);
				holder.username.setText(person.getNick_name());
				holder.userage.setText(person.getAge()+"");
				holder.usersign.setText(person.getSign());
				holder.useraddres.setText(person.getArea());
				if (person.getSex() == 1) {
					Drawable drawable = context.getResources().getDrawable(R.drawable.detailboy);
					holder.usersex.setBackgroundDrawable(drawable);
				} else {
					Drawable drawable = context.getResources().getDrawable(R.drawable.detailgirl);
					holder.usersex.setBackgroundDrawable(drawable);
				}		
				holder.userconcerned.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						LoadStrangerData();
					}
				});
			}
		return convertView;
	}
	public void LoadStrangerData() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// dialog.show();
				Message msg = mUHandler.obtainMessage(1);
				try {
					msg.obj = Person_Service.addpaste(persion_id, person.getId());
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
					if ( json.equals(JsonUtil.ObjToJson(Constant.FAIL))) {
						Toast.makeText(context, "关注失败", 1);
					} else {
						holder.userconcerned.setText("已关注");	
						holder.userconcerned.setOnClickListener(null);
					}
				} else {
					Toast.makeText(context, "关注失败", 2);
				}
				notifyDataSetChanged();
				break;
			default:
				break;
			}
		}

	};

	class ViewHolder
	{		
		RoundImageView usersico;
		TextView username;
		ImageView usersex;
		TextView userage;
		TextView useraddres;
		TextView usersign;
		TextView userconcerned;
	}
}
