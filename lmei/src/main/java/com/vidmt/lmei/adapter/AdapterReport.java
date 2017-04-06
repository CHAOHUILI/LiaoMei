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
import com.vidmt.lmei.entity.Report;
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

public class AdapterReport extends BaseAdapter {
	private Context context;
	private List<Report> list;
	protected ImageLoader imageLoader;
	protected DisplayImageOptions options;
	ViewHolder holder = null;
	// 记录当前展开项的索引
	private int expandPosition = -1;
	Persion person;
	private int persion_id;
	private int clickTemp = 0;
	public AdapterReport(Context context, List<Report> list, ImageLoader imageLoader, DisplayImageOptions options) {
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

		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater lif = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = lif.inflate(R.layout.adapter_report, null);
			holder.reportname = (TextView) convertView.findViewById(R.id.reportname);
			holder.reportselect = (ImageView) convertView.findViewById(R.id.reportselect);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Report report = list.get(position);

		if (report != null) {
			holder.reportname.setText(report.getReportcontent());
			if(clickTemp == position){
				holder.reportselect.setVisibility(View.VISIBLE);
			}else{
				holder.reportselect.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	public void setSeclection(int position) {
		clickTemp = position;
		}
	class ViewHolder {
		TextView reportname;
		ImageView reportselect;
	}
}
