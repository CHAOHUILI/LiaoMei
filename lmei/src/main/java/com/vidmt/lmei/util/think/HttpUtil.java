package com.vidmt.lmei.util.think;


import com.ta.TASyncHttpClient;
import com.ta.annotation.TAInject;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;

public class HttpUtil {
	@TAInject
	// ͬ请求同步类
	private static TASyncHttpClient syncHttpClient = new TASyncHttpClient();;
	@TAInject
	// 异步类
	private static AsyncHttpClient asyncHttpClient  = new AsyncHttpClient();;

	public HttpUtil() {
		if(syncHttpClient==null)
	     	syncHttpClient = new TASyncHttpClient();
		if(syncHttpClient==null)
		   asyncHttpClient = new AsyncHttpClient();
	}

	/**
	 * get请求json对象
	 * 
	 * @param url
	 *            输入请求的网址
	 * @return 返回json字符串
	 */
	public static String doGet(String url) {
		return syncHttpClient.get(url);
	}

	/**
	 * post请求json对象
	 * 
	 * @param url
	 *            输入请求的网址
	 * @param params
	 *            post请求的参数列表 格式如下： RequestParams params = new RequestParams();
	 *            params.put("username", "white_cat"); params.put("password",
	 *            "123456"); params.put("email", "2640017581@qq.com");
	 * @return 返回json字符串
	 */
	public static String doPost(String url, RequestParams params) {
		if (params != null)
			return syncHttpClient.post(url, params) ;
		return syncHttpClient.post(url);
		
		
		
	}
	/*
	 * 适合页面中直接调用和插入数据到服务器
	 */
	public static void asynDoGet(String url) {
		asyncHttpClient.get(url, new AsyncHttpResponseHandler()
		{
			@Override //成功的信息
			public void onSuccess(String content)
			{
				// TODO Auto-generated method stub
				super.onSuccess(content);
				//showWebView(content);
				String str = content;
			}
			@Override //错误信息
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				super.onFailure(error);
				try {
					throw error;
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
	}

	
	
}
