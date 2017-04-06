package com.vidmt.lmei.dialog;






import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 判读是否有网络
 * @author Administrator
 *
 */
public class ConnectionUtil {

	/**
	 * 判断网络连接是否已开 true 已打�?false 未打�?
	 */
	public static boolean isConn(Context context) {
		boolean bisConnFlag = false;
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = conManager.getActiveNetworkInfo();
		if (network != null) {
			bisConnFlag = conManager.getActiveNetworkInfo().isAvailable();
		}
		return bisConnFlag;
	}

//	/**
//	 * 打开设置网络界面
//	 */
//	public static void setNetworkMethod(final Context context) {
//
//		Dialog_net_set.show(context, R.style.mobile_dialog_full_window_dialog)
//				.show();
//	}
}
