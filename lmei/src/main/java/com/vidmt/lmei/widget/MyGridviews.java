package com.vidmt.lmei.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class MyGridviews extends GridView{

	public MyGridviews(Context context) {
		super(context);
	}

	public MyGridviews(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyGridviews(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
