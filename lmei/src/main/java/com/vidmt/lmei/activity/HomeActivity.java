package com.vidmt.lmei.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.gson.reflect.TypeToken;
import com.ta.TAApplication;
import com.ta.annotation.TAInjectView;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.vidmt.lmei.Manifest;
import com.vidmt.lmei.R;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.dialog.ConnectionUtil;
import com.vidmt.lmei.dialog.HomeFilterDialog;
import com.vidmt.lmei.dialog.LoadingDialog;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.recording.RecordDialog;
import com.vidmt.lmei.util.rule.FileUtils;
import com.vidmt.lmei.util.rule.ManageDataBase;
import com.vidmt.lmei.util.rule.SharedPreferencesUtil;
import com.vidmt.lmei.util.think.DbUtil;
import com.vidmt.lmei.util.think.JsonUtil;

import android.app.Application;
import android.app.LocalActivityManager;
import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 广场activity
 */
public class HomeActivity extends BaseActivity {
    private long exitTime = 0;
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
    @TAInjectView(id = R.id.headconrel)
    RelativeLayout headconrel;
    @TAInjectView(id = R.id.userbannerdelete)
    ImageView userbannerdelete;
    @TAInjectView(id = R.id.userbanner)
    ImageView userbanner;
    @TAInjectView(id = R.id.rel_authentification)
    RelativeLayout rel_authentification;
    @TAInjectView(id = R.id.ll)
    LinearLayout ll;

    LocalActivityManager manager = null;
    @TAInjectView(id = R.id.home_viewpager)
    ViewPager viewPager;
    private int code = 1;
    MyPagerAdapter mypageradapter;
    String s_position_x;//       经度
    String s_position_y;//       维度
    int ident_state = 0; //0,全部，1，认证，2，未认证
    int sex = 0;  //0，全部，1，选择显示男，2，选择显示女
    private LoadingDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (b_person.getSex() == 1) {
            sex=2;
            SharedPreferencesUtil.putInt(getApplicationContext(),"filter_sex",2);
        }
        themes();
        InitView();
        login_again();
        LoadDataUpdate();//更新信息，是否禁用
        if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
            RongIM.init(this);
            //Toast.makeText(ConversationListActivity.this, "rong", Toast.LENGTH_SHORT).show();
            if (mDialog != null && !mDialog.isShowing()) {
                mDialog.show();

            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    enterActivity();
                }
            }, 300);
        }




    }

    private void enterActivity() {//連接融云

        String tokens = null;
        if (b_person == null) {
            dbutil = new DbUtil((TAApplication) this.getApplication());
            List<Persion> users = dbutil.selectData(Persion.class, null);
            if (users != null) {
                if (users.size() > 0) {
                    try {
                        b_person = users.get(0);

                        tokens = b_person.getRongyuntoken();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        } else {
            tokens = b_person.getRongyuntoken();
        }


        if ("default".equals(tokens)) {
            Log.e("ConversationActivity", "push2");
            //			startActivity(new Intent(ConversationActivity.this, LoginActivity.class));
            //			finish();
            reconnect(tokens);
        } else {
            Log.e("ConversationActivity", "push3");
            reconnect(tokens);
        }
    }

    private void reconnect(String token) {

        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {


            }

            @Override
            public void onSuccess(String s) {

                Log.e("ConversationActivity", "push4");

                if (mDialog != null)
                    mDialog.dismiss();

                //
                //                Intent intent = new Intent();
                //                intent.setClass(ConversationActivity.this, SplashActivity.class);
                //                intent.putExtra("PUSH_CONVERSATIONTYPE", mConversationType.toString());
                //                intent.putExtra("PUSH_TARGETID", mTargetId);
                //                startActivity(intent);
                //                finish();


            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {

                if (mDialog != null)
                    mDialog.dismiss();


            }
        });

    }

    @Override
    public void InitView() {
        // TODO Auto-generated method stub
        super.InitView();
        headerthemeleft.setVisibility(View.GONE);
        headerright.setVisibility(View.VISIBLE);
        headconrel1.setVisibility(View.VISIBLE);
        headercontent1.setVisibility(View.VISIBLE);
        headercontentv1.setVisibility(View.GONE);
        headercontentv.setVisibility(View.VISIBLE);
        headercontent.setText("热门");
        headercontent1.setText("最新");
        Drawable drawablemore = context.getResources().getDrawable(R.drawable.filterhome);
        typelog.setBackgroundDrawable(drawablemore);
        Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
        user.setBackgroundDrawable(drawable);

        manager = new LocalActivityManager(this, true);
        manager.dispatchResume();
        LatestChatActivity.dialog = loadingDialog;
        HotChatActivity.dialog = loadingDialog;
        initTextView();
        initPagerViewer();

        //廣告
        if ((b_person.getVideo_ident() != null && b_person.getVideo_ident() == 3) ||
                (b_person.getIdent_state() != null && b_person.getIdent_state() == 5)) {
            rel_authentification.setVisibility(View.GONE);
        } else {
            rel_authentification.setVisibility(View.VISIBLE);
        }
    }

    private void login_again() {//刷新用戶時間，
        // TODO Auto-generated method stub
        //刷新登录时间
        new Thread(new Runnable() {
            @Override
            public void run() {
//				s_position_x = SharedPreferencesUtil.getString(HomeActivity.this, "x", "");
//				s_position_y = SharedPreferencesUtil.getString(HomeActivity.this, "y", "");
                String all = Person_Service.login_again(b_person.getId());
                Message msg = mUHandler.obtainMessage(1);
                msg.obj = all;
                msg.sendToTarget();
            }
        }).start();
    }

    public void LoadDataUpdate() {//查看用戶狀態，是否禁用
        // TODO Auto-generated method stub
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                String str = Person_Service.loginupdage(b_person.getId());
                Message msg = mUHandler.obtainMessage(4);
                msg.obj = str;
                msg.sendToTarget();
            }
        }).start();
    }

    private Handler mUHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (msg.obj != null) {
                        //					Bundle bundle = (Bundle)msg.obj;
                        //					orderCondition=	bundle.getInt("orderCondition");
                        //					sex = bundle.getString("sex");
                        //					ident_state = bundle.getString("ident_state");
                        //					sup_ability = bundle.getString("sup_ability");
                        //					screen();
                        //					viewPager.getAdapter().notifyDataSetChanged();
                    }
                    break;
                case 4:
                    if (msg.obj != null && !msg.obj.equals("")) {
                        String str = (String) msg.obj;
                        if (!str.equals("") && !str.equals(JsonUtil.ObjToJson(Constant.ERROR))) {
                            Persion p = JsonUtil.JsonToObj(str, Persion.class);
                            if (p.getUse_state() != null && p.getUse_state() == 2) {
                                new DisableView(HomeActivity.this, ll);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }

    };

    public class DisableView extends PopupWindow {

        public DisableView(Context mContext, View parent) {

            View view = View.inflate(mContext, R.layout.activity_close_account, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));
            // LinearLayout ll_popup = (LinearLayout) view
            // .findViewById(R.id.ll_popup);
            // ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
            // R.anim.push_bottom_in_1));

            setWidth(LayoutParams.FILL_PARENT);
            setHeight(LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            setContentView(view);
            showAtLocation(parent, Gravity.CENTER, 0, 0);
            TextView button1 = (TextView) view.findViewById(R.id.dialogleft);
            TextView dialogcontext = (TextView) view.findViewById(R.id.dialogcontext);
            dialogcontext.setText("您的账号被管理员禁用");
            button1.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    RongIM.getInstance().logout();
                    ManageDataBase.Delete(dbutil, Persion.class, null);
                    StartActivity(LoginActivity.class);
                }
            });
        }
    }

    /**
     * 初始化标题
     */
    private void initTextView() {
        headconrel.setOnClickListener(new MyOnClickListener(1));
        headconrel1.setOnClickListener(new MyOnClickListener(2));
    }

    /**
     * 初始化PageViewer
     */
    private void initPagerViewer() {
        final ArrayList<View> list = new ArrayList<View>();
        View view = null;
        for (int i = 0; i < 2; i++) {
            list.add(view);
        }
        mypageradapter = new MyPagerAdapter(list);
        viewPager.setAdapter(mypageradapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            Intent intents;
            switch (arg0) {
                case 0:
                    headercontentv.setVisibility(View.VISIBLE);
                    headercontentv1.setVisibility(View.GONE);
                    headercontent.setTextSize(22);
                    headercontent1.setTextSize(18);
                    intents = new Intent("home");
                    sendBroadcast(intents);
                    break;
                case 1:
                    headercontentv.setVisibility(View.GONE);
                    headercontentv1.setVisibility(View.VISIBLE);
                    headercontent1.setTextSize(22);
                    headercontent.setTextSize(18);
                    intents = new Intent("home");
                    sendBroadcast(intents);
                    break;
            }
            code = arg0 + 1;
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }

    /**
     * 头标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            if (index == 1) {
                headercontentv.setVisibility(View.VISIBLE);
                headercontentv1.setVisibility(View.GONE);

            } else if (index == 2) {
                headercontentv.setVisibility(View.GONE);
                headercontentv1.setVisibility(View.VISIBLE);
            }
            code = index;
            viewPager.setCurrentItem(index - 1);
        }
    }

    ;

    /**
     * Pager适配器
     */

    public class MyPagerAdapter extends PagerAdapter {
        List<View> list = new ArrayList<View>();

        public MyPagerAdapter(ArrayList<View> list) {
            this.list = list;
        }

        // 只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ViewPager pViewPager = ((ViewPager) container);
            pViewPager.removeView(list.get(position));
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        private int mChildCount = 0;

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {

            ViewPager pViewPager = ((ViewPager) arg0);
            View view = list.get(arg1);
            Intent intent = null;
            if (arg1 == 0 && view == null) {
                intent = new Intent(HomeActivity.this, LatestChatActivity.class);
                view = getView("LatestChatActivity", intent);
                list.remove(0);
                list.add(0, view);

            } else if (arg1 == 1 && view == null) {
                intent = new Intent(HomeActivity.this, HotChatActivity.class);
                view = getView("HotChatActivity", intent);
                list.remove(1);
                list.add(1, view);
            }
            pViewPager.addView(view);
            return view;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

    }

    public class FilterView extends PopupWindow {

        public FilterView(Context mContext, View parent) {

            View view = View.inflate(mContext, R.layout.home_filter_loading, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));
            // LinearLayout ll_popup = (LinearLayout) view
            // .findViewById(R.id.ll_popup);
            // ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
            // R.anim.push_bottom_in_1));

            setWidth(LayoutParams.FILL_PARENT);
            setHeight(LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            setContentView(view);
            showAtLocation(parent, Gravity.CENTER, 0, 0);
            final TextView filterboy = (TextView) view.findViewById(R.id.filterboy);
            final TextView filtergiles = (TextView) view.findViewById(R.id.filtergiles);
            final TextView filtersex = (TextView) view.findViewById(R.id.filtersex);
            final TextView certificationyes = (TextView) view.findViewById(R.id.certificationyes);
            final TextView certificationno = (TextView) view.findViewById(R.id.certificationno);
            final TextView certification = (TextView) view.findViewById(R.id.certification);
            TextView btn_dialog_confirm = (TextView) view.findViewById(R.id.btn_dialog_confirm);
            TextView btn_dialog_cancel = (TextView) view.findViewById(R.id.btn_dialog_cancel);
            if (sex == 1) {
                filterboy.setTextColor(getResources().getColor(R.color.authentication));
                filtergiles.setTextColor(getResources().getColor(R.color.black));
                filtersex.setTextColor(getResources().getColor(R.color.black));
            } else if (sex == 2) {
                filterboy.setTextColor(getResources().getColor(R.color.black));
                filtergiles.setTextColor(getResources().getColor(R.color.authentication));
                filtersex.setTextColor(getResources().getColor(R.color.black));
            } else if (sex == 0) {
                filterboy.setTextColor(getResources().getColor(R.color.black));
                filtergiles.setTextColor(getResources().getColor(R.color.black));
                filtersex.setTextColor(getResources().getColor(R.color.authentication));
            } else {
                filterboy.setTextColor(getResources().getColor(R.color.black));
                filtergiles.setTextColor(getResources().getColor(R.color.black));
                filtersex.setTextColor(getResources().getColor(R.color.black));
            }
            if (ident_state == 1) {
                certificationyes.setTextColor(getResources().getColor(R.color.authentication));
                certificationno.setTextColor(getResources().getColor(R.color.black));
                certification.setTextColor(getResources().getColor(R.color.black));
            } else if (ident_state == 2) {
                certificationyes.setTextColor(getResources().getColor(R.color.black));
                certificationno.setTextColor(getResources().getColor(R.color.authentication));
                certification.setTextColor(getResources().getColor(R.color.black));
            } else if (ident_state == 0) {
                certificationyes.setTextColor(getResources().getColor(R.color.black));
                certificationno.setTextColor(getResources().getColor(R.color.black));
                certification.setTextColor(getResources().getColor(R.color.authentication));
            } else {
                certificationyes.setTextColor(getResources().getColor(R.color.black));
                certificationno.setTextColor(getResources().getColor(R.color.black));
                certification.setTextColor(getResources().getColor(R.color.black));
            }
            filterboy.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    sex = 1;
                    filterboy.setTextColor(getResources().getColor(R.color.authentication));
                    filtergiles.setTextColor(getResources().getColor(R.color.black));
                    filtersex.setTextColor(getResources().getColor(R.color.black));
                }
            });
            filtergiles.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    sex = 2;
                    filterboy.setTextColor(getResources().getColor(R.color.black));
                    filtergiles.setTextColor(getResources().getColor(R.color.authentication));
                    filtersex.setTextColor(getResources().getColor(R.color.black));
                }
            });

            filtersex.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    sex = 0;
                    filterboy.setTextColor(getResources().getColor(R.color.black));
                    filtergiles.setTextColor(getResources().getColor(R.color.black));
                    filtersex.setTextColor(getResources().getColor(R.color.authentication));
                }
            });
            certificationyes.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    ident_state = 1;
                    certificationyes.setTextColor(getResources().getColor(R.color.authentication));
                    certificationno.setTextColor(getResources().getColor(R.color.black));
                    certification.setTextColor(getResources().getColor(R.color.black));
                }
            });
            certificationno.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    ident_state = 2;
                    certificationyes.setTextColor(getResources().getColor(R.color.black));
                    certificationno.setTextColor(getResources().getColor(R.color.authentication));
                    certification.setTextColor(getResources().getColor(R.color.black));
                }
            });
            certification.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    ident_state = 0;
                    certificationyes.setTextColor(getResources().getColor(R.color.black));
                    certificationno.setTextColor(getResources().getColor(R.color.black));
                    certification.setTextColor(getResources().getColor(R.color.authentication));
                }
            });
            btn_dialog_confirm.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intents = new Intent("home");
                    SharedPreferencesUtil.putInt(getApplicationContext(), "filter_ident_state", ident_state);
                    SharedPreferencesUtil.putInt(getApplicationContext(), "filter_sex", sex);
                    sendBroadcast(intents);
                    dismiss();
                }
            });
            btn_dialog_cancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dismiss();
                }
            });
        }
    }

    /**
     * 通过activity获取视图
     *
     * @param id
     * @param intent
     * @return
     */
    private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
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
                    case R.id.userbannerdelete:
                        rel_authentification.setVisibility(View.GONE);
                        break;
                    case R.id.headerright:
                    /*
					 * HomeFilterDialog registerenterlog = new
					 * HomeFilterDialog(HomeActivity.this,
					 * R.style.mobile_dialog_full_window_dialog,"" , 0);
					 * registerenterlog.show();
					 */
                        new FilterView(HomeActivity.this, ll);
                        break;
                    case R.id.userbanner:
                        StartActivity(UserAuthenticationActivity.class);
                        break;
                    default:
                        break;
                }
            }
        };
        userbanner.setOnClickListener(onClickListener);//广告
        userbannerdelete.setOnClickListener(onClickListener);
        headerright.setOnClickListener(onClickListener);
        userbanner.setOnClickListener(onClickListener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.exit), Toast.LENGTH_SHORT)
                    .show();
            exitTime = System.currentTimeMillis();
        } else {
            exitApp();
        }
    }

}
