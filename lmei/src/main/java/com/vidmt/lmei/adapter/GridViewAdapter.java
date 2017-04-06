package com.vidmt.lmei.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.vidmt.lmei.R;
import com.vidmt.lmei.entity.Model;
import com.vidmt.lmei.entity.Present;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter{
	private List<Model> mDatas;
    private LayoutInflater inflater;
    /**
     * 页数下标,从0开始(当前是第几页)
     */
    private int curIndex;
    
	private ArrayList<Present> presents; 
    /**
     * 每一页显示的个数
     */
    private int pageSize;
    
    protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options;
    public GridViewAdapter(Context context, int curIndex, int pageSize,ArrayList<Present> presents) {
        inflater = LayoutInflater.from(context);
        this.curIndex = curIndex;
        this.pageSize = pageSize;
        this.presents =presents;
        options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.sisterloadlog)
				.showImageForEmptyUri(R.drawable.sisterloadlog)
				.showImageOnFail(R.drawable.sisterloadlog)
				.cacheInMemory()
				.cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(0))
				.build();
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		 return presents.size() > (curIndex + 1) * pageSize ? pageSize : (presents.size() - curIndex * pageSize);
	}
	@Override
    public Object getItem(int position) {
        return presents.get(position + curIndex * pageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + curIndex * pageSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gridview, parent, false);
    		convertView.setLayoutParams(new AbsListView.LayoutParams((int)(parent.getWidth()/4), (int) (parent.getHeight()/2)));	
            viewHolder = new ViewHolder();
            viewHolder.gn = (TextView) convertView.findViewById(R.id.giftname);
            viewHolder.gp = (TextView) convertView.findViewById(R.id.giftprice);
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
    		convertView.setLayoutParams(new AbsListView.LayoutParams((int)(parent.getWidth()/4), (int) (parent.getHeight()/2)));	
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /**
         * 在给View绑定显示的数据时，计算正确的position = position + curIndex * pageSize
         */
        int pos = position + curIndex * pageSize;
        viewHolder.gn.setText(presents.get(pos).getPresent_name());
        viewHolder.gp.setText(presents.get(pos).getPrice()+"聊币");
		imageLoader.displayImage(presents.get(pos).getPresent_img(), viewHolder.iv, options);

//        viewHolder.iv.setImageResource(R.drawable.liwu);
		
		convertView.setLayoutParams(new AbsListView.LayoutParams((int)(parent.getWidth()/4), (int) (parent.getHeight()/2)));	
        return convertView;
    }
}
    class ViewHolder {
        public TextView gn;//礼物名
        public TextView gp;//礼物价格
        public ImageView iv;
    }
