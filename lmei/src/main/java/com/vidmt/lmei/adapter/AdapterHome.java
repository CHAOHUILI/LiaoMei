package com.vidmt.lmei.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vidmt.lmei.R;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.rule.ScreenUtils;
import com.vidmt.lmei.widget.RoundImageView;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AdapterHome extends BaseAdapter {
	private Context context;
	private List list;
	protected ImageLoader imageLoader;
	protected DisplayImageOptions options;

	public AdapterHome(Context context, List list, ImageLoader imageLoader, DisplayImageOptions options) {
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
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater lif = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = lif.inflate(R.layout.adapter_home, null);
			holder.userico = (RoundImageView) convertView.findViewById(R.id.userico);
			holder.usercertification = (ImageView) convertView.findViewById(R.id.usercertification);
			holder.chatvideo = (TextView) convertView.findViewById(R.id.chatvideo);
			holder.chatvoice = (TextView) convertView.findViewById(R.id.chatvoice);
			holder.chatname = (TextView) convertView.findViewById(R.id.chatname);
			holder.chatsex = (ImageView) convertView.findViewById(R.id.chatsex);
			holder.chatage = (TextView) convertView.findViewById(R.id.chatage);
			holder.chatageleave = (TextView) convertView.findViewById(R.id.chatageleave);
			holder.videovoicelin = (LinearLayout) convertView.findViewById(R.id.videovoicelin);
			holder.homimg_rel = (RelativeLayout) convertView.findViewById(R.id.homimg_rel);
			holder.homimg_linz = (LinearLayout) convertView.findViewById(R.id.homimg_linz);
			LinearLayout.LayoutParams lty = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					(parent.getWidth() / 2 - 40) / 9 * 11);
			holder.homimg_rel.setLayoutParams(lty);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Persion person = (Persion) list.get(position);

		if (person != null) {

			if (person.getPhoto().equals(holder.userico.getTag())) {

			} else {
				// 如果不相同，就加载。现在在这里来改变闪烁的情况
				imageLoader.displayImage(person.getPhoto(), holder.userico, options);
				holder.userico.setTag(person.getPhoto());
			}
			// imageLoader.displayImage(person.getPhoto(), holder.userico,
			// options);

			if (person.getIdent_state() != null || person.getVideo_ident() != null) {
				if (person.getVideo_ident() == 3) {
					holder.usercertification.setVisibility(View.VISIBLE);
					Drawable photoident = context.getResources().getDrawable(R.drawable.certificationvideo);
					holder.usercertification.setBackgroundDrawable(photoident);
				} else if (person.getIdent_state() == 5) {
					holder.usercertification.setVisibility(View.VISIBLE);
					Drawable photoident = context.getResources().getDrawable(R.drawable.certificationphoto);
					holder.usercertification.setBackgroundDrawable(photoident);
				}else {
					// Drawable photoident =
					// context.getResources().getDrawable(R.drawable.certificationwrz);
					holder.usercertification.setVisibility(View.GONE);
				}
			} else {
				// Drawable photoident =
				// context.getResources().getDrawable(R.drawable.certificationwrz);
				holder.usercertification.setVisibility(View.GONE);
			}

			holder.chatvideo.setText(person.getVideo_money() + "金币/分钟");
			holder.chatvoice.setText(person.getVoice_money() + "金币/分钟");
			ColorDrawable dw = new ColorDrawable(0xb0000000);
			// 设置SelectPicPopupWindow弹出窗体的背景
			holder.videovoicelin.setBackgroundDrawable(dw);
			holder.chatname.setText(person.getNick_name());
			holder.chatage.setText(person.getAge() + "");
			if (person.getSex() == 1) {
				Drawable drawable = context.getResources().getDrawable(R.drawable.chatboy);
				holder.chatsex.setBackgroundDrawable(drawable);
			} else {
				Drawable drawable = context.getResources().getDrawable(R.drawable.chatgirl);
				holder.chatsex.setBackgroundDrawable(drawable);
			}

			holder.chatageleave.setText(person.getSign());

		}

		// convertView.findViewById(R.id.homimg_rel).setLayoutParams(new
		// AbsListView.LayoutParams(parent.getWidth()/2-20,
		// (parent.getWidth()/2-20) / 3 * 5));
		return convertView;
	}

	class ViewHolder {

		RoundImageView userico;
		ImageView usercertification;
		TextView chatvideo;
		TextView chatvoice;
		TextView chatname;
		ImageView chatsex;
		TextView chatage;
		TextView chatageleave;
		LinearLayout videovoicelin;
		RelativeLayout homimg_rel;
		LinearLayout homimg_linz;
	}
}
