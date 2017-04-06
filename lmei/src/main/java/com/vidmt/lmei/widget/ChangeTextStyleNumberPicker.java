package com.vidmt.lmei.widget;

import com.vidmt.lmei.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

public class ChangeTextStyleNumberPicker extends NumberPicker{

	  public ChangeTextStyleNumberPicker(Context context) {
	        super(context);
	    }

	    public ChangeTextStyleNumberPicker(Context context, AttributeSet attrs) {
	        super(context, attrs);
	    }

	    public ChangeTextStyleNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
	        super(context, attrs, defStyleAttr);
	    }

	    @Override
	    public void addView(View child) {
	        super.addView(child);
	        updateView(child);
	    }

	    @Override
	    public void addView(View child, ViewGroup.LayoutParams params) {
	        super.addView(child, params);
	        updateView(child);
	    }

	    @Override
	    public void addView(View child, int index, ViewGroup.LayoutParams params) {
	        super.addView(child, index, params);
	        updateView(child);
	    }

	    private void updateView(View view) {
	        if (view instanceof EditText) {
	            ((EditText) view).setTextColor(getResources().getColor(R.color.black));
	            ((EditText) view).setTextSize(16);
	        }
	    }
}
