package com.vidmt.lmei.util.rule;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class ProgressUtil {

	public ProgressDialog dialog = null;
	
	

	public void ShowProgress(Context context, boolean b) {
		if (b) {
			dialog = new ProgressDialog(context);
			dialog.setTitle("提示：");
			dialog.setMessage("数据加载中......");
			dialog.setIndeterminate(b);
			dialog.show();

		} else {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}

	public void ShowLoginProgress(Context context, boolean b) {
		if (b) {
			dialog = new ProgressDialog(context);
			dialog.setTitle("提示：");
			//dialog.setMessage("正在登陆......");
			dialog.setIndeterminate(b);
			dialog.show();
		} else {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}

	public void showProgressDialog(Context context, boolean b) {
		if (b) {
			dialog = new ProgressDialog(context);
			dialog.setMessage("数据请求中...");
			Window window = dialog.getWindow();
			  WindowManager.LayoutParams lp = window.getAttributes();
			  lp.alpha = 0.7f;// 透明度
			  lp.dimAmount = 0.8f;// 黑暗度
			  window.setAttributes(lp);
			dialog.setCancelable(false);
			dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			        
			        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			            // TODO Auto-generated method stub
			            // Cancel task.
			            if (keyCode == KeyEvent.KEYCODE_BACK) {
			            	dialog.dismiss();
			            }
			            return false;
			        }
			    });
			dialog.show();
		} else {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}
	
	public void showProgressDialog(ProgressDialog dialog,String mes) {
			dialog.setMessage("数据请求中...");
			dialog.setCancelable(false);
	}

}
