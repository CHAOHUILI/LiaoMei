package com.vidmt.lmei.util.think;

import com.vidmt.lmei.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class SideBar extends View{
	// �����¼�
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	private TextView mTextDialog; 
	public static String[] characters = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
		"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
		"W", "X", "Y", "Z", "#" };

	public SideBar(Context context) {
		super(context);
	}

	public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	

	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
	}
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = getWidth(); // ��ȡ��Ӧ���
		int height = getHeight();// ��ȡ��Ӧ�߶�
		int singleHeight = height / characters.length; // ��ȡÿһ����ĸ�ĸ�
		Paint paint = new Paint();
		
		for(int i = 0; i < characters.length; i++){
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(16);
			paint.setColor(Color.rgb(33, 65, 98));
			
			float x = width / 2 - paint.measureText(characters[i]) / 2;
			float y = singleHeight * (i + 1); 
			canvas.drawText(characters[i], x, y, paint);
			paint.reset();
		}
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		float y = event.getY();
		int pos = (int) (y / getHeight() * characters.length);
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			//setBackgroundResource(R.drawable.sidebar);
			mTextDialog.setVisibility(View.GONE);
			break;
			
		default:
			//setBackgroundResource(R.color.);
			if(pos >= 0 && pos <= characters.length){
				
				if(onTouchingLetterChangedListener != null){
					onTouchingLetterChangedListener.onTouchingLetterChanged(characters[pos]);
				}
				if(mTextDialog != null){
					mTextDialog.setText(characters[pos]);
					mTextDialog.setVisibility(View.GONE);
				}
			}
			break;
		}
		return true;
		
	}
	/**
	 * ���⹫���ķ���
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	/**
	 * �ӿ�
	 * @author coder 
	 */
	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}
	
}
