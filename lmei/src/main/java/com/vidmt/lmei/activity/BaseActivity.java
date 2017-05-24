package com.vidmt.lmei.activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.ConversationListBehaviorListener;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.model.Conversation.ConversationType;

import com.mrwujay.cascade.model.CityModel;
import com.mrwujay.cascade.model.DistrictModel;
import com.mrwujay.cascade.model.ProvinceModel;
import com.mrwujay.cascade.service.XmlParserHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.ta.util.TALogger;
import com.umeng.analytics.MobclickAgent;
import com.vidmt.lmei.CloseAccountActivity;
import com.vidmt.lmei.R;
import com.vidmt.lmei.dialog.ConnectionUtil;
import com.vidmt.lmei.dialog.LoadingDialog;
import com.vidmt.lmei.entity.Persion;
import com.vidmt.lmei.util.rule.ManageDataBase;
import com.vidmt.lmei.util.rule.ScreenUtils;
import com.vidmt.lmei.util.rule.SharedPreferencesUtil;
import com.vidmt.lmei.util.think.DbUtil;

/**
 * 基础activity
 */
public class BaseActivity extends ThinkAndroidBaseActivity implements ConversationListBehaviorListener{

	// test
	private static final String TAG = "BaseActivity";
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options;
	protected DisplayImageOptions filletoptions;
	protected DisplayImageOptions roundptions;
	protected Context context;
	public static Activity activity;
	protected DbUtil dbutil;
	protected LoadingDialog loadingDialog;
	private static long lastClickTime;
	protected Persion b_person = null;
	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * key - 区 values - 邮编
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName = "";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
		context = this.getApplicationContext();
		activity = this;
		setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音
		if (ConnectionUtil.isConn(activity) == false) {
			// ConnectionUtil.setNetworkMethod(activity);
			Toast.makeText(context, "网络未连接", Toast.LENGTH_SHORT).show();
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.sisterloadlog)
				.showImageForEmptyUri(R.drawable.sisterloadlog).showImageOnFail( R.drawable.sisterloadlog).cacheInMemory()
				.imageScaleType(ImageScaleType.NONE).cacheOnDisc().displayer(new RoundedBitmapDisplayer(0)).build();
		filletoptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.sisterloadlog)
				.showImageForEmptyUri(R.drawable.sisterloadlog).showImageOnFail(R.drawable.sisterloadlog).cacheInMemory()
				.cacheOnDisc().displayer(new RoundedBitmapDisplayer(45)).build();

		roundptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.sisterloadlog) //设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.sisterloadlog) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.sisterloadlog) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory() // 设置下载的图片是否缓存在内存中
				.cacheOnDisc() // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(360)) // 设置成圆角、圆形图片,我这里将new RoundedBitmapDisplayer的参数设置为90,就是圆形图片，其他角度可以根据需求自行修改
				.build();
		dbutil = new DbUtil(this.getTAApplication());
		loadingDialog = new LoadingDialog(this);
		List<Persion> list = ManageDataBase.SelectList(dbutil, Persion.class, null);
		if (list.size() > 0) {
			b_person = list.get(0);
		}
		TALogger.isTrue = false; // 设置false为不打印日志
	}

	public void themes() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			Window win = getWindow();
			// win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			final int bits = ScreenUtils.getStatusHeight(this);
			View layoutAll = findViewById(R.id.linheader);
			// 设置系统栏需要的内偏移
			layoutAll.setPadding(0, ScreenUtils.getStatusHeight(this), 0, 0);
			LinearLayout linheader = (LinearLayout) findViewById(R.id.linheader);
			if (linheader != null) {
				int height = linheader.getLayoutParams().height;
				linheader.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height + bits));
			}
		}
	}

	// 统一子类方法名
	public void InitView() {

	}

	// 统一子类方法吗
	public void LoadData() {

	}



	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	public void onResume() {
		super.onResume();
		//InitView();
		MobclickAgent.onResume(this);
		JPushInterface.onResume(this);

		Integer editor = SharedPreferencesUtil.getInt(getApplicationContext(), "editor", 0);
		if(editor==0){
			JPushInterface.resumePush(getApplicationContext());
		} else {
			JPushInterface.stopPush(getApplicationContext());
		}
	}
	/**
	 * 打开一个activity
	 * 
	 * @param aclass
	 *            需要打开的activity的类
	 */
	public void StartActivity(Class aclass) {
		Intent intent = new Intent();
		intent.setClass(context, aclass);
//		intent.putExtra("default_login","default_login");
		startActivity(intent);
	}
	public void StartActivity2(Class aclass) {
		Intent intent = new Intent();
		intent.setClass(context, aclass);
		intent.putExtra("default_login","default_login");
		startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			this.finish();

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 封装toast显示
	 *
	 * @param mes
	 */
	public void ToastShow(String mes) {
		Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_SHORT).show();
		// 自定义的toast
		// WinToast.toast(this, mes);
	}
	public void ToastShow(int mes) {
		Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_SHORT).show();
		// 自定义的toast
		// WinToast.toast(this, mes);
	}

	/**
	 * 通过流转换成bitmap
	 * 无用
	 */
	public InputStream GetIma(String path) {
		URL url;
		HttpURLConnection conn;
		InputStream is = null;
		try {

			url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(0);
			conn.setDoInput(true);
			conn.connect();
			is = conn.getInputStream();
			// imageView.setImageBitmap(BitmapFactory.decodeStream(is));
			// is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
		return is;
	}

	// 取到绝对路径
	protected String getAbsoluteImagePath(Uri uri) {
		// can post image
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, proj, // Which
				// columns
				// to
				// return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)

		int column_index = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
		// .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String image_path = cursor.getString(column_index);
		cursor.close();
		return image_path;
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
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:// 音量增大
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume+1, 1);
			break;
		case KeyEvent.KEYCODE_VOLUME_DOWN:// 音量减小
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume-1, 1);
			break;

		default:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}
	/**
	 * 解析省市区的XML数据
	 */

	protected void initProvinceDatas()
	{
		List<ProvinceModel> provinceList = null;
		AssetManager asset = getAssets();
		try {
			InputStream input = asset.open("province_data.xml");
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			provinceList = handler.getDataList();
			//*/ 初始化默认选中的省、市、区
			if (provinceList!= null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList!= null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0).getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();

				}
			}
			//*/
			mProvinceDatas = new String[provinceList.size()];
			for (int i=0; i< provinceList.size(); i++) {
				// 遍历所有省的数据
				mProvinceDatas[i] = provinceList.get(i).getName();
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j=0; j< cityList.size(); j++) {
					// 遍历省下面的所有市的数据
					cityNames[j] = cityList.get(j).getName();
					List<DistrictModel> districtList = cityList.get(j).getDistrictList();
					String[] distrinctNameArray = new String[districtList.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
					for (int k=0; k<districtList.size(); k++) {
						// 遍历市下面所有区/县的数据
						DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
						// 区/县对于的邮编，保存到mZipcodeDatasMap
						mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					// 市-区/县的数据，保存到mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				// 省-市的数据，保存到mCitisDatasMap
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
			}
		} catch (Throwable e) {  
			e.printStackTrace();  
		} finally {

		} 

	}

	@Override
	public boolean onConversationClick(Context arg0, View arg1, UIConversation arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onConversationLongClick(Context arg0, View arg1, UIConversation arg2) {
		// TODO Auto-generated method stub
		Log.e("rongyun", "---------------sdf---------------------------------");

		return false;
	}

	@Override
	public boolean onConversationPortraitClick(Context arg0, ConversationType arg1, String arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onConversationPortraitLongClick(Context arg0, ConversationType arg1, String arg2) {
		// TODO Auto-generated method stub
		return false;
	}
	public void Tankuang(int type){
		//type=1禁用 2异地登录
		Intent in = new Intent(activity,CloseAccountActivity.class);
		if (type==1) {
			in.putExtra("state", 1);//1自己的号被禁用了，2别人号被禁用了	
		}else {
			in.putExtra("state", 3);//1自己的号被禁用了，2别人号被禁用了
		}

		in.putExtra("type", type);
		startActivity(in);
		finish();
	}
	public void gobreak(){
		//dbutil.dropTable(Persion.class);
		ManageDataBase.Delete(dbutil, Persion.class, null);
		RongIM.getInstance().logout();
		Intent intents = new Intent("closeapp");
		sendBroadcast(intents);	
		//JPushInterface.stopPush(this);
		finish(); 

	}
}
