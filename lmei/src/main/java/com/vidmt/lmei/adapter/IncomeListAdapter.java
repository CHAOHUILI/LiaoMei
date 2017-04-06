package com.vidmt.lmei.adapter;

import java.util.List;

import com.vidmt.lmei.R;
import com.vidmt.lmei.entity.BalanceOfPayments;
import com.vidmt.lmei.entity.Refund;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 收支 列表适配器
 * @author as
 *
 */
public class IncomeListAdapter extends BaseAdapter {

	Context context;
	List<BalanceOfPayments> list;
	List<Refund>Refundlist;
	int type;// 1 只收记录，2记录详情//3 3级列表
	int state;//1 收入  2支出 3退款
	public IncomeListAdapter(Context context, List<BalanceOfPayments> list,int state,int type) {
		super();
		this.context = context;
		this.list = list;
		this.state = state;
		this.type = type;
	}
	public IncomeListAdapter(Context context, List<Refund> Refundlist,int state) {
		super();
		this.context = context;
		this.Refundlist = Refundlist;
		this.state = state;
	}

	public int getCount() {
		if(state==3){
			return Refundlist.size();
		}else
		return list.size();
	}

	public BalanceOfPayments getItem(int arg0) {
		
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
			convertView = layoutInflate.inflate(R.layout.income_item, null);
			holder.name1 = (TextView) convertView.findViewById(R.id.incomenuminfo);
			holder.time = (TextView) convertView.findViewById(R.id.incomenumtime);
			holder.match_num = (TextView) convertView.findViewById(R.id.incomenum);
			holder.incomenumgoback=(ImageView) convertView.findViewById(R.id.incomenumgoback);
			convertView.setTag(holder);  
		}else{
			holder=(ViewHolder) convertView.getTag();	
		}
		if(state!=3){
		BalanceOfPayments  bfp= list.get(position);
		if(type==2){
			holder.incomenumgoback.setVisibility(View.GONE);
			switch(bfp.getType()){
			//陪聊
			case 1:
				if(bfp.getToken()!=null){
				switch((int)bfp.getToken()){
				case 2000:
					holder.name1.setText("领走24小时("+bfp.getOther_name()+")");
					break;
				case 12000:
					holder.name1.setText("领走一周("+bfp.getOther_name()+")");
					break;
				case 50000:
					holder.name1.setText("领走一个月("+bfp.getOther_name()+")");
					break;
				default:
					holder.name1.setText("普通聊天("+bfp.getOther_name()+")");
					break;
				}
				}else{
					holder.name1.setText("普通聊天("+bfp.getOther_name()+")");
				}
				break;
				//语音
			case 2:
				holder.name1.setText("语音通话("+bfp.getOther_name()+")");
				break;
				//视频
			case 3:
				holder.name1.setText("视频通话("+bfp.getOther_name()+")");
				break;
				//礼物
			case 4:
				if(state==1){
					holder.name1.setText("收到礼物"+bfp.getPresent_name()+"("+bfp.getOther_name()+")");
				}else if(state==2){
					holder.name1.setText("送出礼物"+bfp.getPresent_name()+"("+bfp.getOther_name()+")");
				}
				break;
			
			}
		}else if(type==1){
		holder.incomenumgoback.setVisibility(View.VISIBLE);
		holder.name1.setText("和"+bfp.getOther_name()+"聊天及礼物");
		}else if(type==3){
			holder.name1.setText("普通聊天("+bfp.getOther_name()+")");
		}
		holder.time.setText(bfp.getCreate_date()+"");
		if(state==1){
			//收入
			holder.match_num.setText("￥"+bfp.getCapital());
		}else if(state==2){
			holder.match_num.setText("-"+bfp.getToken()+"金币");
		}
		}else{
			//退款
			Refund refund = Refundlist.get(position);
				holder.name1.setText("退款（"+refund.getSeller_name()+"十五分钟内未回复）");
				holder.match_num.setText("+"+refund.getToken()+"金币");
				holder.time.setText(refund.getCreate_date()+"");
		}
		return convertView;
	}

	static class ViewHolder {
		TextView name1;
		TextView name2;
		TextView time;
		TextView match_num;
		ImageView incomenumgoback;
	}
}

