package com.vidmt.lmei.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
/**
 * 园图
 * @author First
 *
 */
public class MyGridView extends GridView  {

	public MyGridView(Context context) {
		super(context);
	}

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);
	}
//	@Override
//	protected void dispatchDraw(Canvas canvas) {
//		super.dispatchDraw(canvas);
//		View localView1 = getChildAt(0);
//		int column = getWidth() / localView1.getWidth();
//		int childCount = getChildCount();
//		Paint localPaint= new Paint();
//		localPaint.setStyle(Paint.Style.STROKE);
//		localPaint.setColor(getContext().getResources().getColor(R.color.graytext));
//		for(int i = 0;i < childCount;i++){
//			View cellView = getChildAt(i);
//			if((i + 1) % column == 0){ //最右侧只画底部边框
//				canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
//			}else if((i + 1) > (childCount - (childCount % column))){//最下部只画右侧边框
//				canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
//			}else{//画底部和右侧边框
//				canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
//				canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
//			}
//		}
//		//如果最后一行不足，则补全缺省item的右侧竖线。
//		if(childCount % column != 0){
//			for(int j = 0 ;j < (column-childCount % column) ; j++){
//				View lastView = getChildAt(childCount - 1);
//				canvas.drawLine(lastView.getRight() + lastView.getWidth() * j, lastView.getTop(), lastView.getRight() + lastView.getWidth()* j, lastView.getBottom(), localPaint);
//			}
//		}
//	}

}
