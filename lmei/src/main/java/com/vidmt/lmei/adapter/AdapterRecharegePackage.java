package com.vidmt.lmei.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vidmt.lmei.R;
import com.vidmt.lmei.entity.TokenPackage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import cn.jpush.android.data.s;

public class AdapterRecharegePackage extends BaseAdapter {
	private List list;

	private Context context;
	protected ImageLoader imageLoader;
	protected DisplayImageOptions options;
	// 记录当前展开项的索引
	private int expandPosition = -1;
	private String recharge;
	private int clickTemp = 0;
	public AdapterRecharegePackage(List list, Context context,ImageLoader imageLoader,
			DisplayImageOptions options) {
		this.context = context;
		this.list = list;
		this.imageLoader = imageLoader;
		this.options = options;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == list ? 0 : list.size();
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

	public void setSeclection(int position) {
		clickTemp = position;
		}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (null == convertView) {
			LayoutInflater lif = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = lif.inflate(R.layout.adapter_recharge_package, null);
			holder = new ViewHolder();
			
			holder.rechargeamoney = (TextView) convertView.findViewById(R.id.rechargeamoney);
			holder.rechargeamoneys = (TextView) convertView.findViewById(R.id.rechargeamoneys);
			holder.rechargeamoneyreal = (TextView) convertView.findViewById(R.id.rechargeamoneyreal);
			holder.rechargetxzlin=(LinearLayout) convertView.findViewById(R.id.rechargetxzlin);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		TokenPackage payplan = (TokenPackage) list.get(position);
		if(payplan!=null){
			holder.rechargeamoney.setText(payplan.getToken_count()+"金币");
			if(payplan.getGive()!=0){
				holder.rechargeamoneys.setText("+赠"+payplan.getGive()+"金币");
			}else{
				holder.rechargeamoneys.setText("");
			}		
			holder.rechargeamoneyreal.setText("¥"+payplan.getMoney());
			if (clickTemp == position) {
				holder.rechargeamoneyreal.setTextColor(context.getResources().getColor(R.color.white));
				holder.rechargetxzlin.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rechargetcx));
			}
			else{
				holder.rechargeamoneyreal.setTextColor(context.getResources().getColor(R.color.authentication));
				holder.rechargetxzlin.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rechargetcw));
			}
		}
		return convertView;
	}

	public String getRecharge() {
		return recharge;
	}

	public void setRecharge(String recharge) {
		this.recharge = recharge;
	}

	
	class ViewHolder {

		TextView rechargeamoney;
		TextView rechargeamoneys;
		TextView rechargeamoneyreal;
		LinearLayout rechargetxzlin;
	}
}
