package com.vidmt.lmei.adapter;

import java.util.List;

import com.vidmt.lmei.R;
import com.vidmt.lmei.entity.Recharge;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RecordAdapter extends BaseAdapter {

	Context context;
	List<Recharge> list;
	String name;
	int type;//1 充值，2提现
	public RecordAdapter(Context context, List<Recharge> list,String name,int type) {
		super();
		this.context = context;
		this.list = list;
		this.name = name;
		this.type = type;
	}

	public int getCount() {
		return list.size();
	}

	public Recharge getItem(int arg0) {
		return list.get(arg0);
	}

	
	public long getItemId(int arg0) {
		return arg0;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			holder= new ViewHolder();
			LayoutInflater layoutInflate = LayoutInflater.from(context);
			convertView = layoutInflate.inflate(R.layout.record_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.time);
			holder.time = (TextView) convertView.findViewById(R.id.text3);
			holder.match_num = (TextView) convertView.findViewById(R.id.text4);
			holder.text = (TextView) convertView.findViewById(R.id.num);
			convertView.setTag(holder);  
		}else{
			holder=(ViewHolder) convertView.getTag();	
		}
		Recharge  li= list.get(position);
		holder.name.setText(name);
		holder.time.setText(li.getCreate_date()+"");
		holder.text.setVisibility(View.GONE);
		if(type == 1){
			//充值
			holder.match_num.setText("￥"+li.getAmount());
			holder.match_num.setTextColor(Color.RED);
			
		}else if(type == 2){
			//提现
			holder.match_num.setText("￥"+li.getAmount());
		}
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView name;
		TextView time;
		TextView match_num;
		TextView text;
		
		
	}

	

}

