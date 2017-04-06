package com.vidmt.lmei.util.rule;
import android.content.Context;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

public class ScreenUtil {
	private static Display defaultDisplay;
	
	public static Display getDefaultDisplay(Context context) {
		if (null == defaultDisplay) {
			WindowManager systemService = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			defaultDisplay = systemService.getDefaultDisplay();
		}
		return defaultDisplay;
	}
	/**
	 * 
	* @Title: getScreenWidth 
	* @Description: 获得屏幕宽度 
	* @param @param context
	* @param @return    
	* @return int    
	* @author ccy
	* @throws
	 */
	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 
	* @Title: getScreenHeight 
	* @Description: 获得屏幕高度 
	* @param @param context
	* @param @return    
	* @return int    
	* @author ccy
	* @throws
	 */
	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	public static float getScreenDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	/**
	 * 
	* @Title: dip2px 
	* @Description: 将dip转换为px 
	* @param @param context
	* @param @param px
	* @param @return    
	* @return int    
	* @author ccy
	* @throws
	 */
	public static int dip2px(Context context, float px) {
		final float scale = getScreenDensity(context);
		return (int) (px * scale + 0.5);
	}
	
	 /**
     * 
     * <b>@Description:<b>dp2px<br/>
     * <b>@param context
     * <b>@param dp
     * <b>@return<b>int<br/>
     * <b>@Author:<b>ccy<br/>
     * <b>@Since:<b>2014-9-4-上午10:04:48<br/>
     */
    public static int dp2px(Context context,int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}
    
    public static int getWidth(Context context) {
		return getDefaultDisplay(context).getWidth();
	}
}
