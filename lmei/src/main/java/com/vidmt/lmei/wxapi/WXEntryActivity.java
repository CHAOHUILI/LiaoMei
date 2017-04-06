package com.vidmt.lmei.wxapi;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import android.widget.Toast;

public class WXEntryActivity extends WXCallbackActivity {

	@Override  
	public void onReq(BaseReq req) {  
		// TODO Auto-generated method stub  
		super.onReq(req);  
	}  
	@Override  
	public void onResp(BaseResp resp) {  
		// TODO Auto-generated method stub  
		super.onResp(resp);  
		//  Toast.makeText(WXEntryActivity.this, resp.errCode+""+resp.errStr, 0).show();
		if (resp.errCode == BaseResp.ErrCode.ERR_OK) {  
			//Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();  
		} else {
			//Toast.makeText(this, "分享失败", Toast.LENGTH_LONG).show();
		} 
	}  
}
