package com.vidmt.lmei.adapter;

import java.util.List;

import com.vidmt.lmei.R;
import com.vidmt.lmei.entity.Withdraw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WithdrawAdapter extends BaseAdapter {

	Context context;
	List<Withdraw> list;
	public WithdrawAdapter(Context context, List<Withdraw> list) {
		super();
		this.context = context;
		this.list = list;
	}

	public int getCount() {
		return list.size();
	}

	public Withdraw getItem(int arg0) {
		return list.get(arg0);
	}

	
	public long getItemId(int arg0) {
		return arg0;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
			//充值
			if(convertView==null){
				holder= new ViewHolder();
				LayoutInflater layoutInflate = LayoutInflater.from(context);
				convertView = layoutInflate.inflate(R.layout.record_item, null);
				holder.time = (TextView) convertView.findViewById(R.id.time);
				holder.num = (TextView) convertView.findViewById(R.id.num);
				holder.text3 = (TextView) convertView.findViewById(R.id.text3);
				holder.text4 = (TextView) convertView.findViewById(R.id.text4);
				convertView.setTag(holder);  
			}else{
				holder=(ViewHolder) convertView.getTag();	
			}
			Withdraw  li= list.get(position);
			Double num = li.getMoney().doubleValue()+li.getPlatform_cost().doubleValue();
			holder.num.setText("￥"+num);
			holder.time.setText(li.getCreatedate());
			holder.text3.setText("￥"+li.getPlatform_cost());
			//提现状态：1提现申请  2同意提现  3 提现成功  4 提现失败
			switch(li.getState()){
			case 1:
				holder.text4.setText("申请中");
				break;
			case 2:
				holder.text4.setText("提现中");
				break;
			case 3:
				holder.text4.setText("提现成功");
				break;
			case 4:
				holder.text4.setText("提现失败");
				break;
			}
		
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView num;
		TextView time;
		TextView text3;
		TextView text4;
		
	}

	

}

