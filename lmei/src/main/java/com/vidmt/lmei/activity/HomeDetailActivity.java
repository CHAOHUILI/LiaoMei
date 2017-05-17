package com.vidmt.lmei.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.ta.TAApplication;
import com.ta.annotation.TAInjectView;
import com.vidmt.lmei.R;
import com.vidmt.lmei.adapter.AdapterHobby;
import com.vidmt.lmei.adapter.AdapterUserPhoto;
import com.vidmt.lmei.adapter.GridViewAdapter;
import com.vidmt.lmei.adapter.ViewPagerAdapter;
import com.vidmt.lmei.constant.Constant;
import com.vidmt.lmei.controller.Chat_Service;
import com.vidmt.lmei.controller.Person_Service;
import com.vidmt.lmei.dialog.ConnectionUtil;
import com.vidmt.lmei.dialog.LoadingDialog;
import com.vidmt.lmei.entity.Image;
import com.vidmt.lmei.entity.Model;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.entity.Present;
import com.vidmt.lmei.util.rule.Bimp;
import com.vidmt.lmei.util.rule.BitmapBlurUtil;
import com.vidmt.lmei.util.rule.ManageDataBase;
import com.vidmt.lmei.util.rule.SDcardTools;
import com.vidmt.lmei.util.rule.ScreenUtils;
import com.vidmt.lmei.util.rule.SharedPreferencesUtil;
import com.vidmt.lmei.util.think.DbUtil;
import com.vidmt.lmei.util.think.JsonUtil;
import com.vidmt.lmei.widget.CircleImageView;
import com.vidmt.lmei.widget.HorizontalListView;
import com.vidmt.lmei.widget.MyGridView;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallSession;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;

import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.view.ViewPager;
import android.util.Log;

/**
 * 别人信息详情页
 */
public class HomeDetailActivity extends BaseActivity {
    private  ImageView mReportOrBlack;
    @TAInjectView(id = R.id.linheader)
    LinearLayout linheader;
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
    @TAInjectView(id = R.id.userdetailico)
    ImageView userdetailico;
    @TAInjectView(id = R.id.rela_error)
    RelativeLayout rela_error;
    @TAInjectView(id = R.id.rela_broken)
    RelativeLayout rela_broken;
    @TAInjectView(id = R.id.scroll_my)
    ScrollView scroll_my;
    @TAInjectView(id = R.id.userdetailsex)
    ImageView userdetailsex;
    @TAInjectView(id = R.id.userdetailage)
    TextView userdetailage;
    @TAInjectView(id = R.id.userdetaillive)
    TextView userdetaillive;
    @TAInjectView(id = R.id.userdetailaddres)
    TextView userdetailaddres;
    @TAInjectView(id = R.id.userdetailnum)
    TextView userdetailnum;
    @TAInjectView(id = R.id.userdetailrz)
    ImageView userdetailrz;
    @TAInjectView(id = R.id.userdetailstar1)
    ImageView userdetailstar1;
    @TAInjectView(id = R.id.userdetailstar2)
    ImageView userdetailstar2;
    @TAInjectView(id = R.id.userdetailstar3)
    ImageView userdetailstar3;
    @TAInjectView(id = R.id.userdetailstar4)
    ImageView userdetailstar4;
    @TAInjectView(id = R.id.userdetailstar5)
    ImageView userdetailstar5;
    @TAInjectView(id = R.id.userdetailcivico)
    CircleImageView userdetailcivico;
    @TAInjectView(id = R.id.userdetailgiftlin)
    LinearLayout userdetailgiftlin;
    @TAInjectView(id = R.id.userdetaichatattentionlin)
    LinearLayout userdetaichatattentionlin;
    @TAInjectView(id = R.id.userdetailstarlin)
    LinearLayout userdetailstarlin;
    @TAInjectView(id = R.id.userdetailchatvideo)
    TextView userdetailchatvideo;
    @TAInjectView(id = R.id.userdetailchatvoice)
    TextView userdetailchatvoice;
    @TAInjectView(id = R.id.userdetailphotonum)
    TextView userdetailphotonum;
    @TAInjectView(id = R.id.hobbysex)
    TextView hobbysex;
    @TAInjectView(id = R.id.userdetailhlist)
    HorizontalListView userdetailhlist;
    @TAInjectView(id = R.id.rela_userphoterror)
    RelativeLayout rela_userphoterror;
    @TAInjectView(id = R.id.userdetailsign)
    TextView userdetailsign;
    @TAInjectView(id = R.id.userdetailbirthday)
    TextView userdetailbirthday;
    @TAInjectView(id = R.id.userdetailarea)
    TextView userdetailarea;
    @TAInjectView(id = R.id.userdetaivideoimg)
    ImageView userdetaivideoimg;
    @TAInjectView(id = R.id.rela_uservideoshow)
    RelativeLayout rela_uservideoshow;
    @TAInjectView(id = R.id.rela_uservideoerror)
    RelativeLayout rela_uservideoerror;
    int userid = 0;
    @TAInjectView(id = R.id.userdetailvideonum)
    TextView userdetailvideonum;
    @TAInjectView(id = R.id.rela_uservoicecontent)
    RelativeLayout rela_uservoicecontent;
    @TAInjectView(id = R.id.rela_uservoiceerror)
    RelativeLayout rela_uservoiceerror;
    @TAInjectView(id = R.id.userdetailhobygridview)
    MyGridView userdetailhobygridview;
    @TAInjectView(id = R.id.rela_userhpbyerror)
    RelativeLayout rela_userhpbyerror;
    @TAInjectView(id = R.id.userdetailhobbyonum)
    TextView userdetailhobbyonum;
    @TAInjectView(id = R.id.userdetailvoiceonum)
    TextView userdetailvoiceonum;
    @TAInjectView(id = R.id.userdetailvoicetime)
    TextView userdetailvoicetime;
    @TAInjectView(id = R.id.uservoice)
    LinearLayout uservoice;
    AdapterHobby adapterhobby;
    List<String> userhobbylist;
    @TAInjectView(id = R.id.uservoiceimg)
    ImageView uservoiceimg;
    @TAInjectView(id = R.id.videoout)
    LinearLayout videoout;// 视频
    @TAInjectView(id = R.id.vioceout)
    LinearLayout vioceout;// 语音
    @TAInjectView(id = R.id.chatout)
    LinearLayout chatout;// 私信
    @TAInjectView(id = R.id.chatgift)
    ImageView chatgift;// 礼物
    @TAInjectView(id = R.id.chatattentionimg)
    ImageView chatattentionimg;// 礼物
    @TAInjectView(id = R.id.chatattentiontxt)
    TextView chatattentiontxt;
    AdapterUserPhoto adapteruserphotot;
    List<String> userphotolist;
    int islike = 0;
    int isblack = 0;
    int myisblack = 0;
    @TAInjectView(id = R.id.ll)
    LinearLayout ll;
    private float scaleFactor = 2.5f;// 1以上，越大越模糊
    public static Bitmap bmp;
    boolean isprepare;// 缓存状态
    String videopath = "";
    String voicespath = "";
    int num = 0;
    int maxnum = 0;
    Timer T = new Timer();
    static MediaPlayer mPlayer = new MediaPlayer();
    List<Image> list;
    private Persion p;
    private int token;
    private int catetoken;// 礼物的金币数
    File photofile;

    private ArrayList<Present> presents;
    private Present cates;
    private String[] titles = {"棒棒糖", "去污皂", "喜欢你", "萌猫耳", "金话筒", "皇冠", "跑车", "掌声", "信用卡", "桃心", "玫瑰", "跑车", "玩偶熊",
            "蛋糕", "发夹", "化妆品", "别墅", "马卡龙", "金卡", "冰激凌", "爱心", "包包"};
    private ViewPager mPager;
    private List<View> mPagerList;
    private List<Model> mDatas;
    private LinearLayout mLlDot;
    private LayoutInflater inflater;

    @TAInjectView(id = R.id.linjobdetailbootm)
    private LinearLayout linjobdetailbootm;
    int sate = 0;

    String mp4url = null;
    int pdclose = 0;
    /**
     * 总的页数
     */
    private int pageCount;
    /**
     * 每一页显示的个数
     */
    private int pageSize = 8;
    /**
     * 当前显示的是第几页
     */
    private int curIndex = 0;

    int pdshowdate = 0;
    static LoadingDialog dialog = null;
    String jhuser;

    private int chattype;// 1为从 会话界面
    int sxdate = 0;
    private LoadingDialog mDialog;
    private int pos;
    private Drawable black_report2;
    private Drawable black_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_detail);
        themes();
        InitView();
        loadingDialog.onWindowFocusChanged(true);
        loaduserinfo();
        genpresent();

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    public void InitView() {
        // TODO Auto-generated method stub
        super.InitView();
        /*
		 * linheader.setBackgroundColor(context.getResources().getColor(R.color.
		 * white));
		 */
        SharedPreferencesUtil.putInt(HomeDetailActivity.this, "sxdate", 0);
        dialog = new LoadingDialog(HomeDetailActivity.this);
        headerthemeleft.setVisibility(View.VISIBLE);
        headerright.setVisibility(View.VISIBLE);
        headconrel1.setVisibility(View.GONE);
        headercontentv.setVisibility(View.GONE);
        userid = getIntent().getIntExtra("userid", 0);
        if(userid!=0){
            SharedPreferencesUtil.putString(getApplicationContext(),"she_id",userid+"");
        }
        chattype = getIntent().getIntExtra("chattype", 0);
        pdshowdate = getIntent().getIntExtra("pdshowdate", 0);
        Drawable drawable = context.getResources().getDrawable(R.drawable.header_left);
        black_report = context.getResources().getDrawable(R.drawable.black_report);
        black_report2 = context.getResources().getDrawable(R.drawable.black_report2);
        user.setBackgroundDrawable(drawable);
        Drawable drawablemore = context.getResources().getDrawable(R.drawable.chatmore);
        typelog.setBackgroundDrawable(drawablemore);
        list = new ArrayList<Image>();
        userphotolist = new ArrayList<String>();
        adapteruserphotot = new AdapterUserPhoto(context, userphotolist, imageLoader, options);
        userdetailhlist.setAdapter(adapteruserphotot);
        userdetailhlist.setFocusable(false);
        userdetailhlist.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent in = new Intent(HomeDetailActivity.this, AmplificationActivity.class);
                in.putExtra("index", position);
                startActivity(in);
                AmplificationActivity.goodsimglists = list;
            }
        });
        userhobbylist = new ArrayList<String>();
        if (userid == b_person.getId()) {
            linjobdetailbootm.setVisibility(View.GONE);
        }

        adapterhobby = new AdapterHobby(context, userhobbylist, imageLoader, options);
        userdetailhobygridview.setAdapter(adapterhobby);
        userdetailhobygridview.setFocusable(false);
        userdetailhobygridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        presents = new ArrayList<Present>();


        if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
            if (mDialog != null && !mDialog.isShowing()) {
                mDialog.show();
            }
            RongIM.init(this);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    enterActivity();
                }
            }, 300);
        } else {

        }

    }

    private void enterActivity() {

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
            //			startActivity(new Intent(ConversationActivity.this, LoginActivity.class));
            //			finish();
            reconnect(tokens);
        } else {
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

    public void loaduserinfo() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                jhuser = Person_Service.loaduserinfo(b_person.getId(), userid);
                Message msg = mUIHandler.obtainMessage(1);
                msg.obj = jhuser;
                msg.sendToTarget();
            }
        }).start();
    }

    /**
     * 获取礼物列表
     */
    private void genpresent() {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                String json = Chat_Service.getpresent();
                Message msg = mUIHandler.obtainMessage(3);
                msg.obj = json;
                msg.sendToTarget();
            }
        }).start();

    }

    /**
     * 发送礼物
     *
     * @param buy_id
     * @param sell_id
     * @param id
     * @param_token
     */
    private void sendpresent(final String buy_id, final String sell_id, final int id) {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = Chat_Service.sendpresent(buy_id, sell_id, id);
                Message msg = mUIHandler.obtainMessage(4);
                msg.obj = JsonUtil.JsonToObj(json, String.class);
                msg.sendToTarget();
            }
        }).start();
    }

    private Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    showInfo(msg);
                    break;

                case 3:
                    if (msg.obj != null) {
                        String str = (String) msg.obj;
                        if (str.equals(JsonUtil.ObjToJson(Constant.ERROR)) || str.equals("")) {

                            Toast.makeText(HomeDetailActivity.this, "网络连接失败，请检查您的网络连接", Toast.LENGTH_SHORT).show();
                        } else {
                            //
                            try {
                                List<Present> presentslist = JsonUtil.JsonToObj(str, new TypeToken<List<Present>>() {
                                }.getType());
                                presents.addAll(presentslist);
                            } catch (Exception e) {
                                // TODO: handle exception
                                // new
                                // ShowContentView(HomeDetailActivity.this,ll,str,2);
                                genpresent();
                            }
                            // ToastShow(presents.size()+"");
                            // persentAdapter = new
                            // PersentAdapter(HomeDetailActivity.this, presents, 1);

                            // forumAllCateAdapter = new
                            // ForumAllCateAdapter(ForumActivity.this,
                            // fcategorys,1);

                        }
                        // loadingDialog.dismiss();
                    }

                    break;
                case 4:
                    // 异常 = error
                    // 对方被禁用 = sell_error
                    // 自己被禁用 = buy_error
                    // 自己金币不足 = buy_nomoney
                    // 成功 = 返回自己的剩余金币 int 类型
                    if (msg.obj != null) {
                        String mes = (String) msg.obj;
                        if ("".equals(mes)) {
                            // ToastShow("网络异常，请检查网络连接");
                        } else if (mes.contains("sell_error")) {

                        } else if (mes.contains("buy_error")) {

                        } else if (mes.contains("buy_nomoney")) {

                        } else {
                            token = Integer.valueOf(mes).intValue();
                            sendTextBlackMessage("消费了" + catetoken + "金币", 2);
                            Toast.makeText(HomeDetailActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                            // ToastShow(mes+ip);
                        }
                    }
                    break;
                case 5:
                    num++;
                    userdetailvoicetime.setText(secToTime(num) + "“");
                    if (num >= maxnum) {
                        num = 0;
                    }
                    break;
                case 6:
                   audioChart();
                    break;
                case 7:

                    videoChart();
                    break;
                case 1111:
                    if (msg.obj != null) {
                        String mes = (String) msg.obj;

                        VideoPlayActivity.mp4url = mp4url;
                        // 下载好视频后 记录这个用户的id,方便下次直接调用
                        if (mp4url != null) {
                            SharedPreferencesUtil.putInt(HomeDetailActivity.this, "Cachemp4", p.getId());
                            // SharedPreferencesUtil.putString(HomeDetailActivity.this,
                            // "Cachemp4path", mp4url);
                            StartActivity(VideoPlayActivity.class);
                        }
                    }
                    loadingDialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    private void showInfo(Message msg) {
        if (msg.obj != null) {
            String mes = (String) msg.obj;

            if (!mes.equals("") && !mes.equals(JsonUtil.ObjToJson(Constant.FAIL))) {

                try {
                    //
                    p = JsonUtil.JsonToObj(mes, Persion.class);
                    ImageLoader.getInstance().loadImage(p.getPhoto(), new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
                            if (loadedImage != null) {

                                BitmapBlurUtil.addTask(loadedImage, new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        super.handleMessage(msg);
                                        Drawable drawablexh = (Drawable) msg.obj;
                                        userdetailico.setImageDrawable(drawablexh);
                                        loadedImage.recycle();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });

                    userdetaillive.setText(p.getLevel() + "级");
                    headercontent.setText(p.getNick_name());
                    if (p.getIsLike() != null) {
                        islike = p.getIsLike();
                        chatattentiontxt.setText("已关注");
                        Drawable drawable = context.getResources().getDrawable(R.drawable.chatattention);
                        chatattentionimg.setBackgroundDrawable(drawable);
                    } else {
                        chatattentiontxt.setText("关注");
                        Drawable drawable = context.getResources().getDrawable(R.drawable.chatqattention);
                        chatattentionimg.setBackgroundDrawable(drawable);
                    }
                    if (p.getIsBlack() != null) {
                        isblack = p.getIsBlack();

                        SharedPreferencesUtil.putInt(HomeDetailActivity.this, "lblack", 1);

                    } else {
                        isblack = 0;

                        SharedPreferencesUtil.putInt(HomeDetailActivity.this, "lblack", 0);
                    }
                    if (p.getSex() != null && p.getSex() == 1) {
                        hobbysex.setText("他的爱好");
                        Drawable drawable = context.getResources().getDrawable(R.drawable.detailboy);
                        userdetailsex.setBackgroundDrawable(drawable);
                    } else {
                        hobbysex.setText("她的爱好");
                        Drawable drawable = context.getResources().getDrawable(R.drawable.detailgirl);
                        userdetailsex.setBackgroundDrawable(drawable);
                    }

                    if (p.getVideo_ident() != null || p.getIdent_state() != null) {
                        if (p.getVideo_ident() == 3) {
                            userdetailrz.setVisibility(View.VISIBLE);
                            Drawable drawable = context.getResources().getDrawable(R.drawable.uservideorz);
                            userdetailrz.setBackgroundDrawable(drawable);
                        } else if (p.getIdent_state() == 5) {
                            userdetailrz.setVisibility(View.VISIBLE);
                            Drawable drawable = context.getResources().getDrawable(R.drawable.userphotorz);
                            userdetailrz.setBackgroundDrawable(drawable);
                        } else {
                            // Drawable drawable =
                            // context.getResources().getDrawable(R.drawable.userwrz);
                            userdetailrz.setVisibility(View.GONE);
                        }
                    } else {
                        // Drawable drawable =
                        // context.getResources().getDrawable(R.drawable.userwrz);
                        userdetailrz.setVisibility(View.GONE);
                    }

                    if (p.getStar() == null) {
                        userdetailstarlin.setVisibility(View.GONE);
                    } else {

                        if (p.getStar() == 1) {
                            userdetailstar1.setVisibility(View.VISIBLE);
                            userdetailstar2.setVisibility(View.GONE);
                            userdetailstar3.setVisibility(View.GONE);
                            userdetailstar4.setVisibility(View.GONE);
                            userdetailstar5.setVisibility(View.GONE);
                        } else if (p.getStar() == 2) {
                            userdetailstar1.setVisibility(View.VISIBLE);
                            userdetailstar2.setVisibility(View.VISIBLE);
                            userdetailstar3.setVisibility(View.GONE);
                            userdetailstar4.setVisibility(View.GONE);
                            userdetailstar5.setVisibility(View.GONE);
                        } else if (p.getStar() == 3) {
                            userdetailstar1.setVisibility(View.VISIBLE);
                            userdetailstar2.setVisibility(View.VISIBLE);
                            userdetailstar3.setVisibility(View.VISIBLE);
                            userdetailstar4.setVisibility(View.GONE);
                            userdetailstar5.setVisibility(View.GONE);
                        } else if (p.getStar() == 4) {
                            userdetailstar1.setVisibility(View.VISIBLE);
                            userdetailstar2.setVisibility(View.VISIBLE);
                            userdetailstar3.setVisibility(View.VISIBLE);
                            userdetailstar4.setVisibility(View.VISIBLE);
                            userdetailstar5.setVisibility(View.GONE);
                        } else if (p.getStar() == 5) {
                            userdetailstar1.setVisibility(View.VISIBLE);
                            userdetailstar2.setVisibility(View.VISIBLE);
                            userdetailstar3.setVisibility(View.VISIBLE);
                            userdetailstar4.setVisibility(View.VISIBLE);
                            userdetailstar5.setVisibility(View.VISIBLE);
                        } else {
                            userdetailstarlin.setVisibility(View.GONE);
                        }
                    }
                    if (p.getAlbum() != null) {
                        userphotolist.clear();
                        list.clear();
                        String[] photo = p.getAlbum().split("_");
                        for (int i = 0; i < photo.length; i++) {
                            userphotolist.add(photo[i]);
                            Image image = new Image();
                            image.setPath(photo[i]);
                            image.setStatus(2);
                            list.add(image);
                        }
                        userdetailphotonum.setText("" + photo.length);
                        userdetailhlist.setVisibility(View.VISIBLE);
                        rela_userphoterror.setVisibility(View.GONE);

                    } else {
                        userdetailphotonum.setText("0");
                        userdetailhlist.setVisibility(View.GONE);
                        rela_userphoterror.setVisibility(View.VISIBLE);
                    }

                    imageLoader.displayImage(p.getPhoto(), userdetailcivico, options);

                    if (p.getArea() == null) {

                    } else {
                        if (p.getArea().split("-").length > 1) {
                            userdetailaddres.setText(p.getArea().split("-")[1]);
                        } else {
                            userdetailaddres.setText(p.getArea());
                        }
                        userdetailarea.setText(p.getArea());
                    }
                    if (p.getBirthday() == null) {
                        userdetailbirthday.setText("1999-01-01");
                    } else {
                        userdetailbirthday.setText(p.getBirthday());
                    }

                    userdetailsign.setText(p.getSign());

                    userdetailchatvideo.setText(p.getVideo_money() + "金币/分钟");
                    userdetailchatvoice.setText(p.getVoice_money() + "金币/分钟");

                    if (p.getVideo() != null && p.getVideo_ident() == 3) {
                        Bitmap videoThumbnail = createVideoThumbnail(p.getVideo(), 100,100);

                        userdetaivideoimg.setImageBitmap(videoThumbnail);

//                        ImageLoader.getInstance().loadImage(p.getPhoto(), new ImageLoadingListener() {
//                            @Override
//                            public void onLoadingStarted(String imageUri, View view) {
//                            }
//
//                            @Override
//                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                            }
//
//                            @Override
//                            public void onLoadingComplete(String imageUri, View view,
//                                                          final Bitmap loadedImage) {
//                                if (loadedImage != null) {
//
//                                    BitmapBlurUtil.addTask(loadedImage, new Handler() {
//                                        @Override
//                                        public void handleMessage(Message msg) {
//                                            super.handleMessage(msg);
//                                            Drawable drawablexh = (Drawable) msg.obj;
//                                            userdetaivideoimg.setImageDrawable(drawablexh);
//                                            loadedImage.recycle();
//                                        }
//                                    });
//                                }
//                            }
//
//                            @Override
//                            public void onLoadingCancelled(String imageUri, View view) {
//
//                            }
//                        });
                        // userdetaivideoimg.setImageDrawable(drawablexh);
                        videopath = p.getVideo();
                        rela_uservideoshow.setVisibility(View.VISIBLE);
                        rela_uservideoerror.setVisibility(View.GONE);
                        userdetailvideonum.setText("1");
                    } else {
                        pdclose = 1;
                        rela_uservideoshow.setVisibility(View.GONE);
                        rela_uservideoerror.setVisibility(View.VISIBLE);
                        userdetailvideonum.setText("0");
                    }

                    if (p.getSup_ability() != null) {
                        userhobbylist.clear();
                        String[] photo = p.getSup_ability().split("_");
                        userdetailhobbyonum.setText(photo.length+"");

                        for (int i = 0; i < photo.length; i++) {
                            if("".equals(photo[i])&&photo.length==1){
                                userdetailhobbyonum.setText("0");
                            }else{
                                userhobbylist.add(photo[i]);
                            }
                        }
                        userdetailhobygridview.setVisibility(View.VISIBLE);
                        rela_userhpbyerror.setVisibility(View.GONE);

                    } else {
                        userdetailhobbyonum.setText("0");
                        userdetailhobygridview.setVisibility(View.GONE);
                        rela_userhpbyerror.setVisibility(View.VISIBLE);
                    }
                    voicespath = p.getVoice_ident();
                    if (voicespath != null) {
                        userdetailvoiceonum.setText("1");
                        maxnum = p.getVoice_time_length();
                        userdetailvoicetime.setText(secToTime(p.getVoice_time_length()) + "“");
                        rela_uservoicecontent.setVisibility(View.VISIBLE);
                        rela_uservoiceerror.setVisibility(View.GONE);
                        PrepareM();
                    } else {
                        userdetailvoiceonum.setText("0");
                        rela_uservoicecontent.setVisibility(View.GONE);
                        rela_uservoiceerror.setVisibility(View.VISIBLE);
                    }
                    if (p.getAge() != null) {
                        userdetailage.setText(p.getAge() + "");
                    } else {
                        userdetailage.setText(18 + "");
                    }
                    userdetailnum.setText("ID:" + p.getOtherkey());
                    scroll_my.setVisibility(View.VISIBLE);
                    rela_error.setVisibility(View.GONE);
                    rela_broken.setVisibility(View.GONE);
                } catch (Exception e) {
                   Log.e("homeactivity",e.toString());
                    loaduserinfo();
                }
            } else {
                if (ConnectionUtil.isConn(activity) == false) {
                    // ConnectionUtil.setNetworkMethod(activity);
                    scroll_my.setVisibility(View.GONE);
                    rela_error.setVisibility(View.GONE);
                    rela_broken.setVisibility(View.VISIBLE);
                } else {
                    scroll_my.setVisibility(View.GONE);
                    rela_error.setVisibility(View.VISIBLE);
                    rela_broken.setVisibility(View.GONE);
                }
            }

        } else {
            scroll_my.setVisibility(View.GONE);
            rela_error.setVisibility(View.VISIBLE);
            rela_broken.setVisibility(View.GONE);
            loadingDialog.dismiss();
        }
        adapteruserphotot.notifyDataSetChanged();
        adapterhobby.notifyDataSetChanged();
        loadingDialog.dismiss();
    }
    private Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MICRO_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }
    private void videoChart() {
        SharedPreferencesUtil.putInt(HomeDetailActivity.this, "sxdate", 1);
        try {
            if (RongIM.getInstance() == null && RongIM.getInstance().getRongIMClient() == null) {

                ToastShow("网络出现问题  ，请检查 网络设置。");
            } else {

                if (p.getVideo_state() != 1) {

                    ToastShow("对方未开启视频聊天");

                } else {

                    List<Persion> list = ManageDataBase.SelectList(dbutil, Persion.class, null);
                    if (list.size() > 0) {
                        Persion bn = list.get(0);
                        if (p.getToken() != bn.getToken() && bn.getId() == p.getId()) {
                            b_person = bn;
                        }
                    }
                    if (b_person.getUse_state() != null && b_person.getUse_state() == 2) {

                        ToastShow("您的账户被禁用，不能进行此操作");
                    } else {

                        if (isblack == 0) {
                            if (myisblack == 1) {
                                ToastShow("您被对方拉黑，不能进行此操作");
                            } else {

                                if (b_person.getToken() != null   && b_person.getToken() >= p.getVideo_money()) {

                                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(p.getId() + "",
                                            p.getNick_name(), Uri.parse(p.getPhoto())));
                                    RongIM.getInstance()
                                            .refreshUserInfoCache(new UserInfo(b_person.getId() + "",
                                                    b_person.getNick_name(),
                                                    Uri.parse(b_person.getPhoto())));
                                    RongIM.getInstance().setMessageAttachedUserInfo(true);
                                    int ltype = SharedPreferencesUtil.getInt(HomeDetailActivity.this,
                                            "ltype", 0);
                                    if (p.getIsLike() != null) {

                                        if (ltype == 1) {

                                        } else {
                                            SharedPreferencesUtil.putInt(HomeDetailActivity.this, "ltype",
                                                    1);
                                        }
                                    } else {

                                        if (ltype == 1) {
                                            SharedPreferencesUtil.putInt(HomeDetailActivity.this, "ltype",
                                                    0);
                                        } else {

                                        }

                                    }
                                    RongCallSession profile = RongCallClient.getInstance().getCallSession();
                                    if (profile != null && profile.getActiveTime() > 0) {
                                        Toast.makeText(HomeDetailActivity.this,
                                                getString(io.rong.imkit.R.string.rc_voip_call_start_fail),
                                                Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    // RongIM.getInstance().startPrivateChat(UserActivity.this,
                                    // persion.getId()+"",
                                    // persion.getNick_name());
                                    ConnectivityManager cm = (ConnectivityManager) getSystemService(
                                            Context.CONNECTIVITY_SERVICE);
                                    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                                    if (networkInfo == null || !networkInfo.isConnected()
                                            || !networkInfo.isAvailable()) {
                                        Toast.makeText(HomeDetailActivity.this,
                                                getString(
                                                        io.rong.imkit.R.string.rc_voip_call_network_error),
                                                Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    Intent intent = new Intent(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEVIDEO);
                                    intent.putExtra("conversationType",
                                            Conversation.ConversationType.PRIVATE.getName().toLowerCase());
                                    intent.putExtra("targetId", p.getId() + "");
                                    intent.putExtra("mytargetId", b_person.getId() + "");
                                    intent.putExtra("callAction",
                                            RongCallAction.ACTION_OUTGOING_CALL.getName());
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setPackage(getPackageName());
                                    getApplicationContext().startActivity(intent);
                                } else {
                                    ToastShow("您的金币不足，请充值后再发送吧！");
                                    LoadDataUpdate();
                                    SplashActivity.mainactivity.Tankuang(4);
                                }
                            }
                        } else {

                            ToastShow("您已拉黑对方 ，不能进行此操作");
                        }

                    }
                }
            }
        } catch (Exception e) {
            ToastShow("网络出现问题  ，请检查 网络设置。");
        }

    }

    private void audioChart() {
        SharedPreferencesUtil.putInt(HomeDetailActivity.this, "sxdate", 1);
        try {

            /**
             * 发起视频通话。
             *
             * @param context
             *            上下文
             * @param targetId
             *            会话 id
             * @param mediaType
             *            会话媒体类型
             */
            if (RongIM.getInstance() == null && RongIM.getInstance().getRongIMClient() == null) {
                ToastShow("网络出现问题  ，请检查 网络设置。");
            } else {
                if (p.getVoice_state() != 1) {
                    ToastShow("对方未开启语音聊天");
                } else {
                    List<Persion> list = ManageDataBase.SelectList(dbutil, Persion.class, null);
                    if (list.size() > 0) {
                        Persion bn = list.get(0);
                        if (p.getToken() != bn.getToken() && bn.getId() == p.getId()) {
                            b_person = bn;
                        }
                    }
                    if (b_person.getUse_state() != null && b_person.getUse_state() == 2) {
                        ToastShow("您的账户被禁用，不能进行此操作");
                    } else {
                        if (isblack == 0) {

                            if (myisblack == 1) {
                                ToastShow("您被对方拉黑，不能进行此操作");
                            } else {

                                if (b_person.getToken() != null  && b_person.getToken() >= p.getVoice_money()) {

                                    try {
                                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(
                                                p.getId() + "", p.getNick_name(), Uri.parse(p.getPhoto())));
                                        RongIM.getInstance()
                                                .refreshUserInfoCache(new UserInfo(b_person.getId() + "",
                                                        b_person.getNick_name(),
                                                        Uri.parse(b_person.getPhoto())));
                                        RongIM.getInstance().setMessageAttachedUserInfo(true);
                                        int ltype = SharedPreferencesUtil.getInt(HomeDetailActivity.this,
                                                "ltype", 0);
                                        if (p.getIsLike() != null) {

                                            if (ltype == 1) {

                                            } else {
                                                SharedPreferencesUtil.putInt(HomeDetailActivity.this,
                                                        "ltype", 1);
                                            }
                                        } else {

                                            if (ltype == 1) {
                                                SharedPreferencesUtil.putInt(HomeDetailActivity.this,
                                                        "ltype", 0);
                                            } else {

                                            }

                                        }

                                        RongCallSession profile = RongCallClient.getInstance()
                                                .getCallSession();
                                        if (profile != null && profile.getActiveTime() > 0) {
                                            Toast.makeText(HomeDetailActivity.this,
                                                    getString(
                                                            io.rong.imkit.R.string.rc_voip_call_start_fail),
                                                    Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        // RongIM.getInstance().startPrivateChat(UserActivity.this,
                                        // persion.getId()+"",
                                        // persion.getNick_name());

                                        ConnectivityManager cm = (ConnectivityManager) getSystemService(
                                                Context.CONNECTIVITY_SERVICE);
                                        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                                        if (networkInfo == null || !networkInfo.isConnected()
                                                || !networkInfo.isAvailable()) {
                                            Toast.makeText(HomeDetailActivity.this,
                                                    getString(
                                                            io.rong.imkit.R.string.rc_voip_call_network_error),
                                                    Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        Intent intent = new Intent(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEAUDIO);
                                        intent.putExtra("conversationType",
                                                Conversation.ConversationType.PRIVATE.getName()
                                                        .toLowerCase());
                                        intent.putExtra("targetId", p.getId() + "");
                                        intent.putExtra("mytargetId", b_person.getId() + "");
                                        intent.putExtra("callAction",
                                                RongCallAction.ACTION_OUTGOING_CALL.getName());
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.setPackage(getPackageName());
                                        getApplicationContext().startActivity(intent);
                                        // RongCallKit.startSingleCall(useractivity,
                                        // "123",
                                        // RongCallKit.CallMediaType.CALL_MEDIA_TYPE_AUDIO);

                                    } catch (Exception e) {
                                        // TODO: handle exception
                                        e.printStackTrace();
                                    }
                                } else {
                                    ToastShow("您的金币不足，请充值后发送吧！");
                                    LoadDataUpdate();
                                    SplashActivity.mainactivity.Tankuang(4);
                                }

                            }
                        } else {
                            RongIM.getInstance().getRongIMClient()
                                    .removeConversation(ConversationType.PRIVATE, p.getId() + "");
                            ToastShow("您已拉黑对方，不能进行此操作");
                        }

                    }
                }
            }
        } catch (Exception e) {
            ToastShow("网络出现问题  ，请检查 网络设置。");
        }

    }

    public class ShowContentView extends PopupWindow {

        public ShowContentView(Context mContext, View parent, String type, int lei) {

            View view = View.inflate(mContext, R.layout.attention_loading, null);
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
            TextView attentionts = (TextView) view.findViewById(R.id.attentionts);
            TextView btn_dialog_confirm = (TextView) view.findViewById(R.id.btn_dialog_confirm);
            TextView ceshileixing = (TextView) view.findViewById(R.id.ceshileixing);
            attentionts.setText(lei + ":" + type);
            if (lei == 1) {
                ceshileixing.setText("用户信息");
            } else if (lei == 2) {
                ceshileixing.setText("礼物列表");
            } else if (lei == 3) {
                ceshileixing.setText("自己信息");
            }
            btn_dialog_confirm.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dismiss();
                }
            });
        }
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;

        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        // setListViewHeight(adverlist);
        ViewGroup.LayoutParams params = userdetaivideoimg.getLayoutParams();
        // advertising.setImageResources(gdbilllist, mAdCycleViewListener,2);
        params.height = (int) (ScreenUtils.getScreenWidth(HomeDetailActivity.this) / 5 * 3);
        userdetaivideoimg.setLayoutParams(params);
        super.onWindowFocusChanged(hasFocus);
    }

    /**
     * 缓存MP4
     */
    public void inputMp4() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                mp4url = inputMp4(p.getVideo());
                VideoPlayActivity.mp4url = mp4url;
                // 下载好视频后 记录这个用户的id,方便下次直接调用
                if (!HomeDetailActivity.this.isFinishing()) {
                    SharedPreferencesUtil.putInt(HomeDetailActivity.this, "Cachemp4", p.getId());
                    StartActivity(VideoPlayActivity.class);
                }

            }
        }).start();

    }

    public static String inputMp4(String path) {
        String newFilename = SDcardTools.getSDPath() + "/mp4.mp4";
        File file = new File(newFilename);
        // 如果目标文件已经存在，则删除。产生覆盖旧文件的效果
        if (file.exists()) {
            file.delete();
        }
        try {
            // 构造URL
            URL url = new URL(path);
            // 打开连接
            URLConnection con = url.openConnection();
            // 获得文件的长度
            int contentLength = con.getContentLength();
            System.out.println("长度 :" + contentLength);
            // 输入流
            InputStream is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            OutputStream os = new FileOutputStream(newFilename);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.dismiss();
        return newFilename;
    }

    public Bitmap createVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return Bimp.comp(bitmap);
    }
	/*
	 * public static Bitmap createVideoThumbnail(String filePath) { //
	 * MediaMetadataRetriever is available on API Level 8 // but is hidden until
	 * API Level 10 Class<?> clazz = null; Object instance = null; try { clazz =
	 * Class.forName("android.media.MediaMetadataRetriever"); instance =
	 * clazz.newInstance();
	 * 
	 * Method method = clazz.getMethod("setDataSource", String.class);
	 * method.invoke(instance, filePath);
	 * 
	 * // The method name changes between API Level 9 and 10. if
	 * (Build.VERSION.SDK_INT <= 9) { return (Bitmap)
	 * clazz.getMethod("captureFrame").invoke(instance); } else { byte[] data =
	 * (byte[]) clazz.getMethod("getEmbeddedPicture").invoke(instance); if (data
	 * != null) { Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
	 * data.length); if (bitmap != null) return bitmap; } return (Bitmap)
	 * clazz.getMethod("getFrameAtTime").invoke(instance); } } catch
	 * (IllegalArgumentException ex) { // Assume this is a corrupt video file }
	 * catch (RuntimeException ex) { // Assume this is a corrupt video file. }
	 * catch (InstantiationException e) { Log.e("1", "createVideoThumbnail", e);
	 * } catch (InvocationTargetException e) { Log.e("2",
	 * "createVideoThumbnail", e); } catch (ClassNotFoundException e) {
	 * Log.e("3", "createVideoThumbnail", e); } catch (NoSuchMethodException e)
	 * { Log.e("4", "createVideoThumbnail", e); } catch (IllegalAccessException
	 * e) { Log.e("5", "createVideoThumbnail", e); } finally { try { if
	 * (instance != null) { clazz.getMethod("release").invoke(instance); } }
	 * catch (Exception ignored) { } } return null; }
	 */

    FeaturesView featuresview;

    @Override
    protected void onAfterSetContentView() {
        // TODO Auto-generated method stub
        super.onAfterSetContentView();

        OnClickListener onClickListener = new OnClickListener() {
            public boolean playVideo;
            public static final int MIN_CLICK_DELAY_TIME = 1000;
            private long lastClickTime = 0;

            @Override
            public void onClick(View v) {
                Intent intent = null;
                // TODO Auto-generated method stub
                switch (v.getId()) {

                    case R.id.headerthemeleft:
                        if (mPlayer.isPlaying() == true) {
                            mPlayer.stop();
                            mPlayer.release();

                            T.cancel();
                            T = null;
                        }
                        SharedPreferencesUtil.putInt(HomeDetailActivity.this, "sxdate", 0);
                        finish();
                        if (pdshowdate == 1) {
                            Intent intents = new Intent("friendschild");
                            sendBroadcast(intents);
                        }
                        break;
                    case R.id.headerright:
                        featuresview = new FeaturesView(HomeDetailActivity.this, linheader);
                        break;
                    case R.id.userdetaichatattentionlin:
                        if (islike == 0) {
                            LoadStrangerData();
                        } else {
                            unsubscribe();
                        }
                        break;
                    case R.id.chatgift:


                        if (b_person.getUse_state() != null && b_person.getUse_state() == 2) {
                            ToastShow("您的账户被禁用，不能进行此操作");

                            // intent = new Intent(HomeDetailActivity.this,
                            // DemoActivity.class);
                            // startActivity(intent);
                        } else {

                            if (myisblack == 1) {
                                ToastShow("您被对方拉黑，不能进行此操作");
                            } else {
                                curIndex = 0;
                                new PopupWindows(HomeDetailActivity.this, chatgift, 1);
                            }

                        }

                        break;
                    case R.id.rela_uservideoshow:
                        num=0;
                        userdetailvoicetime.setText(secToTime(maxnum) + "“");

                        if (mPlayer.isPlaying()) {
                            mPlayer.pause();
                            if(T!=null){
                                T.cancel();
                            }
                        }
                        dialog.show();
                        int Cache1 = SharedPreferencesUtil.getInt(HomeDetailActivity.this, "Cachemp4", 0);
                        if (Cache1 == p.getId()) {
                            dialog.dismiss();
                            VideoPlayActivity.mp4url = p.getVideo();

                            StartActivity(VideoPlayActivity.class);
                        } else {
                            inputMp4();
                        }
                        playVideo=true;
                        break;
                    case R.id.uservoiceimg:
                        long currentTime = Calendar.getInstance().getTimeInMillis();
                        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                            lastClickTime = currentTime;
                            if (!mPlayer.isPlaying()) {
                                if (isprepare) {
                                    if(num==0){
                                        mPlayer.stop();
                                        try {
                                            mPlayer.prepare();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    mPlayer.start();// 播放
                                    T = new Timer();
                                    T.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            Message msg = new Message();
                                            msg.what = 5;

                                            mUIHandler.sendMessage(msg);
                                        }
                                    }, 1000, 1000);
                                } else {
                                    ToastShow("音频还在缓存中...");
                                }
                            } else {
                                mPlayer.pause();
                                if (T != null) {
                                    T.cancel();
                                }
                                T = null;
                            }
                        }
                        break;
                    case R.id.chatout:
                        SharedPreferencesUtil.putInt(HomeDetailActivity.this, "sxdate", 1);
                        try {
                            if (RongIM.getInstance() == null && RongIM.getInstance().getRongIMClient() == null) {
                                ToastShow("网络出现问题  ，请检查 网络设置。");
                            } else {
                                try {
                                    if (chattype == 1) {
                                        if (mPlayer.isPlaying()) {
                                            mPlayer.stop();
                                            try {
                                                mPlayer.prepare();
                                            } catch (IllegalStateException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            T.cancel();
                                            T = null;
                                        }
                                        finish();
                                        if (pdshowdate == 1) {
                                            Intent intents = new Intent("friendschild");
                                            sendBroadcast(intents);
                                        }
                                    } else {

                                        if (b_person.getUse_state() != null && b_person.getUse_state() == 2) {

                                            ToastShow("您的账户被禁用，不能进行此操作");
                                        } else {
                                            if (isblack == 0) {

                                                if (myisblack == 1) {
                                                    ToastShow("您被对方拉黑，不能进行此操作");
                                                } else {
                                                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(p.getId() + "",
                                                            p.getNick_name(), Uri.parse(p.getPhoto())));

                                                    RongIM.getInstance()
                                                            .refreshUserInfoCache(new UserInfo(b_person.getId() + "",
                                                                    b_person.getNick_name(),
                                                                    Uri.parse(b_person.getPhoto())));

                                                    RongIM.getInstance().setMessageAttachedUserInfo(true);

                                                    RongIM.getInstance().startPrivateChat(HomeDetailActivity.this,
                                                            p.getId() + "", p.getNick_name() + "/" + b_person.getId() + "");
                                                }

                                            } else {
                                                RongIM.getInstance().getRongIMClient()
                                                        .removeConversation(ConversationType.PRIVATE, p.getId() + "");
                                                ToastShow("您已拉黑对方，不能进行此操作");
                                            }

                                        }
                                    }
                                } catch (Exception e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            ToastShow("网络出现问题  ，请检查 网络设置。");
                        }
                        break;
                    case R.id.vioceout:
                        loadChartStatus(1);

                        break;
                    case R.id.videoout:
                        loadChartStatus(2);

                        break;
                    default:
                        break;
                }
            }
        };
        headerthemeleft.setOnClickListener(onClickListener);
        headerright.setOnClickListener(onClickListener);
        rela_uservideoshow.setOnClickListener(onClickListener);
        chatout.setOnClickListener(onClickListener);
        vioceout.setOnClickListener(onClickListener);
        videoout.setOnClickListener(onClickListener);
        chatgift.setOnClickListener(onClickListener);
        uservoiceimg.setOnClickListener(onClickListener);
        userdetaichatattentionlin.setOnClickListener(onClickListener);
        // 播放完的监听
        mPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                userdetailvoicetime.setText(secToTime(maxnum) + "“");
                if (T != null) {
                    T.cancel();
                }
                num=0;
            }
        });
        // 缓存完的监听
        mPlayer.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isprepare = true;
//                try {
//                    mPlayer.prepare();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                // ToastShow("音频已缓存好");
            }
        });
    }

    private void loadChartStatus(final int i) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                jhuser = Person_Service.loaduserinfo(b_person.getId(), userid);
                if (jhuser != null) {
                    p = JsonUtil.JsonToObj(jhuser, Persion.class);
                }
                Message message;
                if (i == 1) {
                    message = mUIHandler.obtainMessage(6);

                } else {
                    message = mUIHandler.obtainMessage(7);
                }
                message.sendToTarget();
            }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
      if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (pdshowdate == 1) {
                Intent intents = new Intent("friendschild");
                sendBroadcast(intents);
            }

            if (mPlayer.isPlaying()) {
                mPlayer.stop();
                mPlayer.release();
                T.cancel();
                T = null;
            }
            finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void LoadStrangerData() {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                // dialog.show();
                Message msg = mUHandler.obtainMessage(1);
                try {
                    msg.obj = Person_Service.addpaste(b_person.getId(), p.getId());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    msg.obj = null;
                }
                msg.sendToTarget();
            }
        }).start();
    }

    public void unsubscribe() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                // dialog.show();
                Message msg = mUHandler.obtainMessage(2);
                try {
                    msg.obj = Person_Service.unsubscribe(islike);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    msg.obj = null;
                }
                msg.sendToTarget();
            }
        }).start();
    }

    public void featuresblack() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                // dialog.show();
                Message msg = mUHandler.obtainMessage(3);
                try {
                    msg.obj = Person_Service.featuresblack(b_person.getId(), p.getId());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    msg.obj = null;
                }
                msg.sendToTarget();
            }
        }).start();
    }

    public void cancelPullBlack() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                // dialog.show();
                Message msg = mUHandler.obtainMessage(4);
                try {
                    msg.obj = Person_Service.cancelPullBlack(isblack);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    msg.obj = null;
                }
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
                        String json = (String) msg.obj;
                        if (json.equals(JsonUtil.ObjToJson(Constant.FAIL))) {
                            new AttentionView(HomeDetailActivity.this, userdetaichatattentionlin, 3);
                        } else {

                            try {
                                JSONObject jsonint = new JSONObject(json);
                                islike = jsonint.getInt("isLike");
                                chatattentiontxt.setText("已关注");
                                isblack = 0;
                                Drawable drawable = context.getResources().getDrawable(R.drawable.chatattention);
                                chatattentionimg.setBackgroundDrawable(drawable);
                                SharedPreferencesUtil.putInt(HomeDetailActivity.this, "ltype", 1);//是否关注，1 已关注
                                new AttentionView(HomeDetailActivity.this, ll, 1);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                new AttentionView(HomeDetailActivity.this, ll, 3);
                            }

                        }
                    } else {
                        new AttentionView(HomeDetailActivity.this, ll, 3);
                    }
                    break;

                case 2:
                    if (msg.obj != null) {
                        String json = (String) msg.obj;
                        if (json.equals(JsonUtil.ObjToJson(Constant.SUCCESS))) {
                            islike = 0;
                            chatattentiontxt.setText("关注");
                            Drawable drawable = context.getResources().getDrawable(R.drawable.chatqattention);
                            chatattentionimg.setBackgroundDrawable(drawable);
                            SharedPreferencesUtil.putInt(HomeDetailActivity.this, "ltype", 0);
                            new AttentionView(HomeDetailActivity.this, ll, 2);
                        } else {
                            new AttentionView(HomeDetailActivity.this, ll, 3);
                        }
                    } else {
                        new AttentionView(HomeDetailActivity.this, ll, 3);
                    }
                    break;
                case 3:
                    if (msg.obj != null) {
                        String json = (String) msg.obj;
                        if (json.equals(JsonUtil.ObjToJson(Constant.FAIL))) {
                            new AttentionView(HomeDetailActivity.this, ll, 3);
                        } else {
                            try {
                                JSONObject jsonint = new JSONObject(json);
                                isblack = jsonint.getInt("isBlack");
                                islike = 0;
                                chatattentiontxt.setText("关注");
                                Drawable drawable = context.getResources().getDrawable(R.drawable.chatqattention);
                                chatattentionimg.setBackgroundDrawable(drawable);
                                SharedPreferencesUtil.putInt(HomeDetailActivity.this, "ltype", 0);
                                new BlackView(HomeDetailActivity.this, ll, 1);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                new BlackView(HomeDetailActivity.this, ll, 3);
                            }
                        }
                    } else {
                        new BlackView(HomeDetailActivity.this, ll, 3);
                    }
                    break;
                case 4:
                    if (msg.obj != null) {
                        String json = (String) msg.obj;
                        if (json.equals(JsonUtil.ObjToJson(Constant.SUCCESS))) {
                            isblack = 0;
                            SharedPreferencesUtil.putInt(HomeDetailActivity.this, "lblack", 1);
                            new BlackView(HomeDetailActivity.this, ll, 2);
                        } else {
                            new BlackView(HomeDetailActivity.this, ll, 3);
                        }
                    } else {
                        new BlackView(HomeDetailActivity.this, ll, 3);
                    }
                    break;
                default:
                    break;
            }
        }

    };

    public class AttentionView extends PopupWindow {

        public AttentionView(Context mContext, View parent, int type) {

            View view = View.inflate(mContext, R.layout.attention_loading, null);
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
            TextView attentionts = (TextView) view.findViewById(R.id.attentionts);
            TextView btn_dialog_confirm = (TextView) view.findViewById(R.id.btn_dialog_confirm);
            if (type == 1) {
                attentionts.setText("已成功关注");
            } else if (type == 2) {
                attentionts.setText("已取消关注");
            } else {
                attentionts.setText("关注失败");
            }
            btn_dialog_confirm.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dismiss();
                }
            });
        }
    }

    public class FeaturesView extends PopupWindow {//拉黑或者举报选择

        public FeaturesView() {
            // TODO Auto-generated method stub

            dismiss();

        }

        public FeaturesView(Context mContext, final View parent) {

            View view = View.inflate(mContext, R.layout.features_loading, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));
            // LinearLayout ll_popup = (LinearLayout) view
            // .findViewById(R.id.ll_popup);
            // ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
            // R.anim.push_bottom_in_1));

            setWidth(LayoutParams.MATCH_PARENT);
            setHeight(LayoutParams.MATCH_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            setContentView(view);
            showAsDropDown(parent);

            FrameLayout featuresreportlin = (FrameLayout) view.findViewById(R.id.featuresreportlin);
            FrameLayout featuresblacklin = (FrameLayout) view.findViewById(R.id.featuresblacklin);
            RelativeLayout featuresrel = (RelativeLayout) view.findViewById(R.id.featuresrel);
            mReportOrBlack = (ImageView) view.findViewById(R.id.black_report);
            if (isblack != 0) {
                mReportOrBlack.setBackgroundResource(R.drawable.black_report2);
            } else {
                mReportOrBlack.setBackgroundResource(R.drawable.black_report);
            }
            featuresblacklin.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                    Intent intent = new Intent(HomeDetailActivity.this, ReportActivity.class);
                    intent.putExtra("otherid", p.getId());
                    startActivity(intent);
                }
            });
            featuresreportlin.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (isblack != 0) {
                        cancelPullBlack();
                    } else {
                        new BlackPromptView(HomeDetailActivity.this, parent);
                    }
                    dismiss();
                }
            });
            featuresrel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    public class BlackPromptView extends PopupWindow {//拉黑确定提现

        public BlackPromptView(Context mContext, View parent) {

            View view = View.inflate(mContext, R.layout.black_prompt_loading, null);
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
            TextView btn_dialog_cancel = (TextView) view.findViewById(R.id.btn_dialog_cancel);
            TextView btn_dialog_confirm = (TextView) view.findViewById(R.id.btn_dialog_confirm);
            btn_dialog_cancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dismiss();
                }
            });
            btn_dialog_confirm.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    featuresblack();
                    dismiss();
                }
            });
        }
    }

    public class BlackView extends PopupWindow {//拉黑成功提醒

        public BlackView(Context mContext, View parent, int type) {

            View view = View.inflate(mContext, R.layout.black_loading, null);
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
            TextView attentionts = (TextView) view.findViewById(R.id.attentionts);
            TextView btn_dialog_confirm = (TextView) view.findViewById(R.id.btn_dialog_confirm);
            if (type == 1) {
                attentionts.setText("已成功拉黑");
                mReportOrBlack.setBackgroundResource(R.drawable.black_report2);

            } else if (type == 2) {
                attentionts.setText("已取消拉黑");
                mReportOrBlack.setBackgroundResource(R.drawable.black_report);

            } else {
                attentionts.setText("拉黑失败");
            }
            btn_dialog_confirm.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dismiss();
                }
            });
        }
    }

    /**
     * 缓存录音文件
     */
    public void PrepareM() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                inputMp3(voicespath);// 缓存音频
            }
        }).start();
    }

    public static void PrepareM2() {
        if(mPlayer!=null){
            mPlayer.reset();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mPlayer.setDataSource(SDcardTools.getSDPath() + "/mp3.mp3");
                    mPlayer.prepareAsync();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 缓存MP3
     *
     * @param path
     * @return
     */

    public static String inputMp3(String path) {
        String newFilename = SDcardTools.getSDPath() + "/mp3.mp3";
        File file = new File(newFilename);
        // 如果目标文件已经存在，则删除。产生覆盖旧文件的效果
        if (file.exists()) {
            file.delete();
        }
        try {
            // 构造URL
            URL url = new URL(path);
            // 打开连接
            URLConnection con = url.openConnection();
            // 获得文件的长度
            int contentLength = con.getContentLength();
            System.out.println("长度 :" + contentLength);
            // 输入流
            InputStream is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            OutputStream os = new FileOutputStream(newFilename);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        PrepareM2();
        return newFilename;
    }

    @Override
    public void onResume() {
        sxdate = SharedPreferencesUtil.getInt(HomeDetailActivity.this, "sxdate", 0);
        if (sxdate == 1) {
            selectisblack();
            LoadDataUpdate();
        }
        super.onResume();
    }

    public void LoadDataUpdate() {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                List<Persion> list = ManageDataBase.SelectList(dbutil, Persion.class, null);
                if (list.size() > 0) {
                    Persion bn = list.get(0);
                    String str = Person_Service.loginupdage(bn.getId());
                    Message msg = mUIHandlerres.obtainMessage(1);
                    msg.obj = str;
                    msg.sendToTarget();
                }
            }
        }).start();
    }

    public void selectisblack() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                // TODO Auto-generated method stub
                String jhuser = Person_Service.selectisblack(b_person.getId()+"", userid+"");
                Message msg = mUIHandlerres.obtainMessage(2);
                msg.obj = jhuser;
                msg.sendToTarget();
            }
        }).start();
    }

    private Handler mUIHandlerres = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {

                case 2:

                    if (msg.obj != null) {
                        String mes = (String) msg.obj;
                        if ("".equals(mes)) {
                            // ToastShow("网络异常，请检查网络连接");
                        } else {

                            JSONObject jsStr;
                            String province = null;
                            int carrier = 0;

                            try {
                                jsStr = new JSONObject(mes);

                                myisblack = jsStr.getInt("myisblack");

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                selectisblack();
                            }
                        }
                    }
                    break;
                case 1:
                    if (msg.obj != null && !msg.obj.equals("")) {
                        String str = (String) msg.obj;
                        if (!str.equals("") && !str.equals(JsonUtil.ObjToJson(Constant.ERROR))
                                && !str.equals(JsonUtil.ObjToJson("param_error"))) {
                            try {
                                // new
                                // ShowContentView(HomeDetailActivity.this,ll,str,3);
                                Persion p1 = JsonUtil.JsonToObj(str, Persion.class);

                                if (p1.getToken() != b_person.getToken() && b_person.getId().intValue() == p1.getId().intValue()) {

                                    ManageDataBase.Delete(dbutil, Persion.class, null);
                                    ManageDataBase.Insert(dbutil, Persion.class, p1);
                                    b_person = p1;
                                }
                            } catch (Exception e) {
                                LoadDataUpdate();
                                //new ShowContentView(HomeDetailActivity.this, ll, str, 3);
                                // TODO: handle exception
                                // SharedPreferencesUtil.putInt(HomeDetailActivity.this,
                                // "sxdate", 0);
                            }

                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    // 礼物点击界面

    /**
     * 礼物选取界面
     **/
    public class PopupWindows extends PopupWindow {
        public PopupWindows() {
            // TODO Auto-generated method stub

            dismiss();

        }

        public PopupWindows(Context mContext, View parent, int type) {

            if (type == 1) {

                View view = null;
                view = View.inflate(mContext, R.layout.activity_demo, null);
                view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));
                setWidth(LayoutParams.FILL_PARENT);
                setHeight(LayoutParams.FILL_PARENT);
                setFocusable(true);
                setBackgroundDrawable(new BitmapDrawable());
                setOutsideTouchable(true);
                setContentView(view);
                showAtLocation(parent, Gravity.BOTTOM, 0, 0);
                showAsDropDown(parent); // 显示到那个视图之下
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                ViewPager mPager = (ViewPager) view.findViewById(R.id.viewpager);
                LinearLayout mLlDot = (LinearLayout) view.findViewById(R.id.ll_dot);
                RelativeLayout giftout = (RelativeLayout) view.findViewById(R.id.giftout);
                TextView chunzhi = (TextView) view.findViewById(R.id.chunzhi);
                ImageView giftguan = (ImageView) view.findViewById(R.id.giftguan);
                // mPager.setFocusable(false);
                // mPager.setFocusableInTouchMode(false);
                // mPager.requestFocus();
                initDatas();
                inflater = LayoutInflater.from(HomeDetailActivity.this);
                // 总的页数=总数/每页数量，并取整

                pageCount = (int) Math.ceil(presents.size() * 1.0 / pageSize);
                mPagerList = new ArrayList<View>();
                for (int i = 0; i < pageCount; i++) {
                    // 每个页面都是inflate出一个新实例
                    GridView gridView = (GridView) inflater.inflate(R.layout.gridview, mPager, false);
                    gridView.setAdapter(new GridViewAdapter(HomeDetailActivity.this, i, pageSize, presents));
                    gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
                    mPagerList.add(gridView);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            pos = position + curIndex * pageSize;

                            // TODO Auto-generated method stub
                            if (checkNetworkState() == false) {

                                Toast.makeText(HomeDetailActivity.this, "当前网络已断开，请网络连接后再送礼物吧。", Toast.LENGTH_SHORT)
                                        .show();

                            } else {

                                // if (psentext.getText().length()==0) {
                                // Toast.makeText(ECChattingActivity.this,
                                // "您还没有输入想说的话", Toast.LENGTH_SHORT).show();
                                //
                                // }else {
                                if (p.getRongyuntoken() == null) {
                                    Toast.makeText(HomeDetailActivity.this, "对方还没有融云账号，不能送礼给她", Toast.LENGTH_SHORT)
                                            .show();
                                    // ToastShow("对方还没有融云账号，不能送礼给她");
                                } else {

                                    cates = presents.get(pos);
                                    catetoken = cates.getPrice();

                                    if (token != 0) {

                                        if (token < catetoken) {
                                            Toast.makeText(HomeDetailActivity.this, "您的金币值不够，请充值或者选其他的礼物吧。",
                                                    Toast.LENGTH_SHORT).show();
                                            SplashActivity.mainactivity.Tankuang(4);

                                        } else {

                                            // if
                                            // (psentext.getText().length()==0)
                                            // {
                                            // Toast.makeText(ECChattingActivity.this,
                                            // "您还没有输入想说的话",
                                            // Toast.LENGTH_SHORT).show();
                                            //
                                            // }else {
                                            // if
                                            // (persion_main.getToken()<catetoken)
                                            // {
                                            //
                                            // Toast.makeText(UserActivity.this,
                                            // Toast.LENGTH_SHORT).show();
                                            //
                                            // }else {

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //
                                                    //
                                                    // if
                                                    // (cates.getResult_img()==null)
                                                    // {
                                                    photofile = getphpto(cates.getResult_img());
                                                    // }else {
                                                    //
                                                    // photofile=
                                                    // getphpto(cates.getResult_img());

                                                    if (photofile == null) {

                                                    } else {
                                                        sendimg(photofile);
                                                    }

                                                }
                                            }).start();

                                            // Toast.makeText(ECChattingActivity.this,
                                            // "选择礼物"+cates.getPresent_name(),Toast.LENGTH_SHORT).show();
                                        }
                                        dismiss();

                                    } else {
                                        if (b_person.getToken() < catetoken) {
                                            Toast.makeText(HomeDetailActivity.this, "您的金币值不够，请充值或者选其他的礼物吧。",
                                                    Toast.LENGTH_SHORT).show();
                                            SplashActivity.mainactivity.Tankuang(4);

                                        } else {

                                            // if
                                            // (psentext.getText().length()==0)
                                            // {
                                            // Toast.makeText(ECChattingActivity.this,
                                            // "您还没有输入想说的话",
                                            // Toast.LENGTH_SHORT).show();
                                            //
                                            // }else {
                                            // if
                                            // (persion_main.getToken()<catetoken)
                                            // {
                                            //
                                            // Toast.makeText(UserActivity.this,
                                            // Toast.LENGTH_SHORT).show();
                                            //
                                            // }else {

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //
                                                    //
                                                    // if
                                                    // (cates.getResult_img()==null)
                                                    // {
                                                    photofile = getphpto(cates.getResult_img());
                                                    // }else {
                                                    //
                                                    // photofile=
                                                    // getphpto(cates.getResult_img());

                                                    if (photofile == null) {

                                                    } else {
                                                        sendimg(photofile);
                                                    }

                                                }
                                            }).start();

                                            // Toast.makeText(ECChattingActivity.this,
                                            // "选择礼物"+cates.getPresent_name(),Toast.LENGTH_SHORT).show();
                                        }
                                        dismiss();

                                    }

                                }
                            }

                            // }

//							
//							Toast.makeText(HomeDetailActivity.this, presents.get(pos).getPresent_name(),
//									Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                // 设置适配器
                mPager.setAdapter(new ViewPagerAdapter(mPagerList));
                // 设置圆点
                setOvalLayout(mLlDot, mPager);
                // GridView f_classgridview =(GridView)
                // view.findViewById(R.id.f_classgridview);
                // // ScrollView presentscroll = (ScrollView)
                // view.findViewById(R.id.presentscroll);
                // TextView mybalance = (TextView)
                // view.findViewById(R.id.mybalance);
                // // final EditText psentext = (EditText)
                // view.findViewById(R.id.psentext);
                // // final TextView sentnums = (TextView)
                // view.findViewById(R.id.sentnums);
                // ImageView sendpersents = (ImageView)
                // view.findViewById(R.id.sendpersents);
                // ImageView liguan = (ImageView)
                // view.findViewById(R.id.liguan);

                // mybalance.setText("我的余额："+token+""+"金币");

                // presents.add(new Present("猪头", 0));
                // presents.add(new Present("吻", 10));
                // presents.add(new Present("吻", 10));
                // presents.add(new Present("跑车", 20));
                // presents.add(new Present("飞机", 30));
                // presents.add(new Present("蛋糕", 6));
                // presents.add(new Present("抱抱", 10));
                // presents.add(new Present("亲亲", 10));
                // presents.add(new Present("游艇", 20));

                giftguan.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dismiss();
                    }
                });
                giftout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dismiss();
                    }
                });
                chunzhi.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        SharedPreferencesUtil.putInt(HomeDetailActivity.this, "sxdate", 1);
                        Intent intent = new Intent(HomeDetailActivity.this, UserRechargeActivity.class);
                        startActivity(intent);
                        dismiss();
                        //Intent intents = new Intent(HomeDetailActivity.this, UserRechargeActivity.class);
                        //startActivity(intents);

                    }
                });

                // f_classgridview.setOnItemClickListener(new
                // OnItemClickListener() {
                //
                // @Override
                // public void onItemClick(AdapterView<?> parent, View view,
                // int position, long id) {
                // // TODO Auto-generated method stub
                //
                // cates = presents.get(position);
                // cates.setSelected(1);
                // for (int i = 0; i < presents.size(); i++) {
                // Present allcates = new Present();
                // allcates = presents.get(i);
                // if (i==position) {
                // allcates.setSelected(1);
                // }else {
                // allcates.setSelected(0);
                // }
                // presents.set( i, allcates);
                // }
                // persentAdapter.notifyDataSetChanged();
                // }
                //
                // });
                // sendpersents.setOnClickListener(new OnClickListener() {
                //
                // @Override
                // public void onClick(View v) {
                // if (checkNetworkState()==false) {
                //
                // Toast.makeText(ConversationActivity.this,
                // "当前网络已断开，请网络连接后再送礼物吧。", Toast.LENGTH_SHORT).show();
                //
                // }else {
                // if (cates==null) {
                // Toast.makeText(ConversationActivity.this, "您还没有选择礼物",
                // Toast.LENGTH_SHORT).show();
                //
                //
                // }else {
                // // if (psentext.getText().length()==0) {
                // // Toast.makeText(ECChattingActivity.this, "您还没有输入想说的话",
                // Toast.LENGTH_SHORT).show();
                // //
                // // }else {
                // if (persion.getRongyuntoken()==null) {
                // Toast.makeText(ConversationActivity.this, "对方还没有融云账号，不能送礼给她",
                // Toast.LENGTH_SHORT).show();
                // //ToastShow("对方还没有融云账号，不能送礼给她");
                // }else {
                //
                // catetoken=cates.getPrice();
                //
                // if (persion.getToken()<catetoken) {
                // Toast.makeText(ConversationActivity.this,
                //
                // }else {
                //
                //
                // // if (psentext.getText().length()==0) {
                // // Toast.makeText(ECChattingActivity.this, "您还没有输入想说的话",
                // Toast.LENGTH_SHORT).show();
                // //
                // // }else {
                // // if (persion_main.getToken()<catetoken) {
                // //
                // Toast.LENGTH_SHORT).show();
                // //
                // // }else {
                //
                //
                // new Thread(new Runnable() {
                // @Override
                // public void run() {
                // //
                // //
                // // if (cates.getResult_img()==null) {
                // photofile= getphpto(cates.getResult_img());
                // // }else {
                // //
                // // photofile= getphpto(cates.getResult_img());
                //
                // if (photofile==null) {
                //
                //
                // }else {
                // sendimg(photofile);
                // }
                //
                //
                // }
                // }).start();
                //
                //
                // //Toast.makeText(ECChattingActivity.this,
                // "选择礼物"+cates.getPresent_name(),Toast.LENGTH_SHORT).show();
                // }
                // dismiss();
                //
                // }
                // }
                // }
                // // }
                // }
                // });
            }

        }

    }

    /**
     * 初始化数据源
     */
    private void initDatas() {
        mDatas = new ArrayList<Model>();
        for (int i = 0; i < titles.length; i++) {
            // 动态获取资源ID，第一个参数是资源名，第二个参数是资源类型例如drawable，string等，第三个参数包名
            int imageId = getResources().getIdentifier("ic_category_" + i, "mipmap", getPackageName());
            mDatas.add(new Model(titles[i], imageId));
        }
    }

    /**
     * 设置圆点
     */
    public void setOvalLayout(final LinearLayout mLlDot, ViewPager mPager) {
        for (int i = 0; i < pageCount; i++) {
            mLlDot.addView(inflater.inflate(R.layout.dot, null));
        }
        // 默认显示第一页
        mLlDot.getChildAt(0).findViewById(R.id.v_dot).setBackgroundResource(R.drawable.dot_selected);

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                // 取消圆点选中
                mLlDot.getChildAt(curIndex).findViewById(R.id.v_dot).setBackgroundResource(R.drawable.dot_normal);
                // 圆点选中
                mLlDot.getChildAt(position).findViewById(R.id.v_dot).setBackgroundResource(R.drawable.dot_selected);
                curIndex = position;
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /**
     * 检测网络是否连接
     *
     * @return
     */
    private boolean checkNetworkState() {
        boolean flag = false;
        // 得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // 去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        // if (!flag) {
        // setNetwork();
        // } else {
        // isNetworkAvailable();
        // }

        return flag;
    }

    /**
     * 获取图片
     *
     * @param url
     */
    private File getphpto(String url) {
        // TODO Auto-generated method stub
        File file = null;
        byte[] responseBody = getImageFromURL(url);

        // String str=url.substring(url.lastIndexOf("schoolphoto/"));
        try {
            String path = Environment.getExternalStorageDirectory() + "/aa.png";
            file = new File(path);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(responseBody);
            fos.flush();
            fos.close();

        } catch (Exception e) {

        }
        return file;
    }

    // Java代码
    public static byte[] getImageFromURL(String urlPath) {
        byte[] data = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            // conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(6000);
            is = conn.getInputStream();
            if (conn.getResponseCode() == 200) {
                data = readInputStream(is);
            } else {
                data = null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.disconnect();
        }
        return data;
    }

    /**
     * 读取InputStream数据，转为byte[]数据类型
     *
     * @param is InputStream数据
     * @return 返回byte[]数据
     */

    // Java代码
    public static byte[] readInputStream(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = -1;
        try {
            while ((length = is.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = baos.toByteArray();
        try {
            is.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void sendimg(File imageFileThumbs) {
        // TODO Auto-generated method stub
        // 发送图片消息
        File imageFileSource = new File(getCacheDir(), "source.png");
        File imageFileThumb = new File(getCacheDir(), "thumb.png");

        // File imageFileSource =
        // getphpto("http://192.168.1.127:8080/loveInterest/upload/present/1f43b4c0-e910-4997-9bb2-666909a39501.png");
        try {
            // 读取图片。
            // InputStream is = getAssets().open(imageFileThumbs.getPath());

            // Bitmap bmpSource = BitmapFactory.decodeStream(is);
            Bitmap bmpSource = BitmapFactory.decodeFile(imageFileThumbs.getPath());
            imageFileSource.createNewFile();
            FileOutputStream fosSource = new FileOutputStream(imageFileSource);

            // 保存原图。
            bmpSource.compress(Bitmap.CompressFormat.PNG, 100, fosSource);

            // 创建缩略图变换矩阵。
            Matrix m = new Matrix();
            m.setRectToRect(new RectF(0, 0, bmpSource.getWidth(), bmpSource.getHeight()), new RectF(0, 0, 160, 160),
                    Matrix.ScaleToFit.CENTER);

            // 生成缩略图。
            Bitmap bmpThumb = Bitmap.createBitmap(bmpSource, 0, 0, bmpSource.getWidth(), bmpSource.getHeight(), m,
                    true);

            imageFileThumb.createNewFile();

            FileOutputStream fosThumb = new FileOutputStream(imageFileThumb);

            // 保存缩略图。
            bmpThumb.compress(Bitmap.CompressFormat.PNG, 60, fosThumb);

        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageMessage imgMsg = ImageMessage.obtain(Uri.fromFile(imageFileThumb), Uri.fromFile(imageFileSource));

        /**
         * 发送图片消息。
         *
         * @param conversationType
         *            会话类型。
         * @param targetId
         *            会话目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室
         *            Id。
         * @param imgMsg
         *            消息内容。
         * @param pushContent
         *            接收方离线时需要显示的push消息内容。
         * @param pushData
         *            接收方离线时需要在push消息中携带的非显示内容。
         * @param SendImageMessageCallback
         *            发送消息的回调。
         */
        RongIM.getInstance().getRongIMClient().sendImageMessage(ConversationType.PRIVATE, p.getId() + "", imgMsg, null,
                null, new RongIMClient.SendImageMessageCallback() {

                    @Override
                    public void onSuccess(io.rong.imlib.model.Message arg0) {
                        // TODO Auto-generated method stub
                        arg0.describeContents();

                        sendpresent(b_person.getId() + "", p.getId() + "", cates.getId());
                        Toast.makeText(HomeDetailActivity.this, presents.get(pos).getPresent_name(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onProgress(io.rong.imlib.model.Message arg0, int arg1) {
                        // TODO Auto-generated method stub
                        arg0.describeContents();
                    }

                    @Override
                    public void onError(io.rong.imlib.model.Message arg0, ErrorCode arg1) {
                        // TODO Auto-generated method stub
                        arg0.describeContents();
                    }

                    @Override
                    public void onAttached(io.rong.imlib.model.Message arg0) {
                        // TODO Auto-generated method stub

                    }
                });

    }

    // 发送小灰条消息。
    @SuppressWarnings("deprecation")
    public void sendTextBlackMessage(String text, int type) {
        InformationNotificationMessage informationNotificationMessage;
        if (type == 1) {
            informationNotificationMessage = InformationNotificationMessage
                    .obtain("聊天时请保持社交礼仪，如果出现谩骂、\n色情及骚扰信息，请及时举报，一旦核实\n立即封号。");

        } else {
            informationNotificationMessage = InformationNotificationMessage.obtain(text);

        }
        // RongIM.getInstance().insertMessage(ConversationType.PRIVATE,
        // targetId, targetId, informationNotificationMessage );
        RongIM.getInstance().insertMessage(ConversationType.PRIVATE, p.getId().toString(), b_person.getId().toString(),
                informationNotificationMessage);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_detail, menu);
        return true;
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
