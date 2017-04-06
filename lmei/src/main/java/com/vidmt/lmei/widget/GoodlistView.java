package com.vidmt.lmei.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ListView;

public class GoodlistView extends ListView{

	public GoodlistView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public  GoodlistView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public GoodlistView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec ) {
		// TODO Auto-generated method stub
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
