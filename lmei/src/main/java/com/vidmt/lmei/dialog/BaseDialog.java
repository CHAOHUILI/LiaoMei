package com.vidmt.lmei.dialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

public class BaseDialog extends Dialog {

	protected Typeface tf;// 字体
	protected Context context = getContext();

	public BaseDialog(Context context) {
		super(context);
	}

	public BaseDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * 自定义字体设置
		 */
		/*tf = Typeface
				.createFromAsset(context.getAssets(), "fonts/FZMWFont.ttf");*/
	}
}
