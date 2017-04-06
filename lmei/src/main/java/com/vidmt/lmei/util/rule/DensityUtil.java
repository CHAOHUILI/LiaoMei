package com.vidmt.lmei.util.rule;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class DensityUtil {
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				res.getDisplayMetrics());
	}
	/** 
	 * sp转px 
	 *  
	 * @param context 
	 * @param val 
	 * @return 
	 */  
	public static int sp2px(Context context, float spVal)  
	{  
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,  
				spVal, context.getResources().getDisplayMetrics());  
	}  
	/** 
	 * px转sp 
	 *  
	 * @param fontScale 
	 * @param pxVal 
	 * @return 
	 */  
	public static float px2sp(Context context, float pxVal)  
	{  
		return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);  
	}  
	/**
	 * 屏幕的宽度
	* @Title mGetScreenWidth
	* @Description TODO(这里用一句话描述这个方法的作用)
	* @param @param context
	* @param @return    参数
	* @return int    返回类型
	 */
	public static int mGetScreenWidth(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
/**
 * 屏幕的高度
* @Title mGetScreenHeight
* @Description TODO(这里用一句话描述这个方法的作用)
* @param @param context
* @param @return    参数
* @return int    返回类型
 */
	public static int mGetScreenHeight(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
}
