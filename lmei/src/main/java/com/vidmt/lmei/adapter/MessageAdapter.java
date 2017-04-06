package com.vidmt.lmei.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.vidmt.lmei.R;
import com.vidmt.lmei.entity.Notice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter{

	Context context;
	List<Notice> Notices;

	public MessageAdapter(Context context, List<Notice> notices) {
		super();
		this.context = context;
		Notices = notices;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Notices.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return Notices.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder;
		if(convertView==null){
			holder= new ViewHolder();
			LayoutInflater layoutInflate = LayoutInflater.from(context);
			convertView = layoutInflate.inflate(R.layout.activity_messages_item, null);
			holder.messagetime=(TextView) convertView.findViewById(R.id.messagetime);
			holder.messagestext = (TextView) convertView.findViewById(R.id.messagestext);
			convertView.setTag(holder);  
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		Notice notice= Notices.get(position);
		
		holder.messagestext.setText(ToDBC(notice.getContent()));
		holder.messagetime.setText(notice.getCreate_date());
		return convertView;
	}
	static class ViewHolder {
		TextView messagetime;
		TextView messagestext;

	}
	//转换为时间差
	public String settime(String time){

		String str= null;
		//获取当前时间
		SimpleDateFormat    sDateFormat    =   new    SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     
		String    date    =    sDateFormat.format(new    java.util.Date());  
		//获取时间差
		//		 SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date begin = null;
		try {
			begin = sDateFormat.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.util.Date end = null;
		try {
			end = sDateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long between=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒
		if (between<0) {
			str="您的时间不正确，请校验时间！";
		}
		long day1=between/(24*3600);
		long hour1=between%(24*3600)/3600;
		long minute1=between%3600/60;
		long second1=between%60/60;

		if (day1>0) {
			if (day1==1) {
				if (hour1>12) {
					str="两天前";
				}else {
					str="昨天";
				}

			}else {
				if (day1<3) {
					if (day1==2) {

						if (hour1>12) {
							str="三天前";
						}else {
							str="两天前";
						}
					}else {
						if (hour1<12) {
							str="三天前";
						}
					}

				}else {
					str=time;
				}
			}

		}else {

			if (hour1>0) {
				str=hour1+""+"小时前";
			}else {

				if (minute1>0) {
					str=minute1+""+"分钟前";
				}else {

					if (second1>0) {

						//						if (second1<30) {
						str=second1+""+"秒前";
						//						}else {
						//							str=second1+""+"秒前";
						//						}
						//					}else {
						//						str=second1+""+"秒前";
					}else {
						str="刚刚";
					}
				}
			}		
		}
		return str;
	}

	public static String ToDBC(String input) {  
		   char[] c = input.toCharArray();  
		   for (int i = 0; i< c.length; i++) {  
		       if (c[i] == 12288) {  
		         c[i] = (char) 32;  
		         continue;  
		       }if (c[i]> 65280&& c[i]< 65375)  
		          c[i] = (char) (c[i] - 65248);  
		       }  
		   return new String(c);  
		} 


}
