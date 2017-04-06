package com.vidmt.lmei.dialog;


import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.widget.GifMovieView;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



public class LoadingDialog extends ProgressDialog {

	AnimationDrawable  mAnimation;
	ImageView gif;
	public LoadingDialog(Context context) {
		super(context);
	    setCanceledOnTouchOutside(false);
        //点击返回键是否取消提示框
        setCancelable(false);
        setIndeterminate(true); 
	}
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        

        gif = (ImageView) findViewById(R.id.load_gif);
        
        gif.setBackgroundResource(R.anim.progress_round);
        // 通过ImageView对象拿到背景显示的AnimationDrawable
        mAnimation = (AnimationDrawable) gif.getBackground();
        
        gif.post(new Runnable() {
            @Override
            public void run() {
                mAnimation.start();
            }
        });
    }
	@Override
	public void show() {
		super.show();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		// if(mAnimationDrawable!=null)
		// mAnimationDrawable.stop();
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			dismiss();
			return false;
		}
		return super.onTouchEvent(event);
	}


}
