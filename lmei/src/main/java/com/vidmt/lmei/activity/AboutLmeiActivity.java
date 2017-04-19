package com.vidmt.lmei.activity;

import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.R.drawable;
import com.vidmt.lmei.R.id;
import com.vidmt.lmei.R.layout;
import com.vidmt.lmei.constant.Constant;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 关于聊妹
 */
public class AboutLmeiActivity extends BaseActivity {

    @TAInjectView(id = R.id.headerthemeleft)
    RelativeLayout headerthemeleft;
    @TAInjectView(id = R.id.user)
    ImageView user;
    @TAInjectView(id = R.id.headerright)
    LinearLayout headerright;
    @TAInjectView(id = R.id.typelog)
    ImageView typelog;
    @TAInjectView(id = R.id.headercontent)
    TextView headercontent;
    @TAInjectView(id = R.id.headercontentv)
    View headercontentv;
    @TAInjectView(id = R.id.headercontent1)
    TextView headercontent1;
    @TAInjectView(id = R.id.headercontentv1)
    View headercontentv1;
    @TAInjectView(id = R.id.headconrel1)
    RelativeLayout headconrel1;

    @TAInjectView(id = R.id.vertical)
    TextView vertical;
    @TAInjectView(id = R.id.resumeweb)
    WebView resumeweb;
    @TAInjectView(id = R.id.linjobdetailbootm)
    LinearLayout linjobdetailbootm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_system);
        themes();
        InitView();
    }

    @Override
    public void InitView() {
        // TODO Auto-generated method stub
        super.InitView();
        headerthemeleft.setVisibility(View.VISIBLE);
        headerright.setVisibility(View.VISIBLE);
        headconrel1.setVisibility(View.GONE);
        headercontentv.setVisibility(View.GONE);
        headercontent.setText("关于聊妹");
        Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
        user.setBackgroundDrawable(drawable);
        vertical.setText("版本V" + getVersion());
        WebSettings ws = resumeweb.getSettings();
        //ws.setBuiltInZoomControls(true);//  隐藏缩放按钮
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//  排版适应屏幕
        //ws.setUseWideViewPort(false);// 可任意比例缩放
        ws.setLoadWithOverviewMode(true);//  setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        ws.setSavePassword(true);
        ws.setSaveFormData(true);//  保存表单数据
        ws.setJavaScriptEnabled(true);
        ws.setGeolocationEnabled(true);//  启用地理定位
        ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");//  设置定位的数据库路径
        ws.setDomStorageEnabled(true);
        resumeweb.loadUrl(Constant.HOST+"upload/resume.htm");
    }

    @Override
    protected void onAfterSetContentView() {
        // TODO Auto-generated method stub
        super.onAfterSetContentView();
        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switch (v.getId()) {

                    case R.id.headerthemeleft:
                        finish();
                        break;
                    case R.id.linjobdetailbootm:
                        StartActivity(UserAgreementActivity.class);
                        break;
                    default:
                        break;
                }
            }
        };
        headerthemeleft.setOnClickListener(onClickListener);
        linjobdetailbootm.setOnClickListener(onClickListener);
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "找不到当前版本";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
