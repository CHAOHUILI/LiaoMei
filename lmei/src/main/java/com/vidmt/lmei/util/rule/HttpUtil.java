package com.vidmt.lmei.util.rule;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class HttpUtil {

	private static final String TAG = "HttpUtil";

	/**
	 * HttpClient请求服务器
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String HttpClientDoPost(String url, List<NameValuePair> params)
			throws IOException {

		HttpClient client = new DefaultHttpClient();
		HttpParams parms = client.getParams();
		HttpConnectionParams.setConnectionTimeout(parms, 10000);// 设置网络超时
		HttpConnectionParams.setSoTimeout(parms, 10000);// 设置网络超时
		HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
		HttpPost post = new HttpPost(url);
		post.setHeader("flag",Calendar.getInstance().getTime().getDay()*9+1+"");
		post.setEntity(entity);
		HttpResponse response = client.execute(post);
		int code = response.getStatusLine().getStatusCode();
		Log.v(TAG, "StatusCode=====" + code);
		if (code == HttpStatus.SC_OK) {
			Log.v(TAG, "响应成功！");
			InputStream inputStream = response.getEntity().getContent();
			StringBuffer stringBuffer = new StringBuffer();
			InputStreamReader reader = new InputStreamReader(inputStream,
					"UTF_8");
			char[] buffer = new char[1024 * 4];
			int count = 0;
			while ((count = reader.read(buffer, 0, buffer.length)) != -1) {
				stringBuffer.append(buffer, 0, count);
			}
			return new String(stringBuffer);
		} else {
			post.abort();
			Log.v(TAG, "响应失败,请求终止.");
			return null;
		}
	}

	/**
	 * HttpClient请求服务器
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String HttpClientdoPost(String urlStr,
			List<NameValuePair> params) {
		HttpEntity entity1 = null;
		String result = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpParams parms = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(parms, 10000);// 设置网络超时
			HttpConnectionParams.setSoTimeout(parms, 10000);// 设置网络超时
			entity1 = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			HttpPost httpPost = new HttpPost(urlStr);
			httpPost.setEntity(entity1);
			httpPost.setHeader("flag",Calendar.getInstance().getTime().getDay()*9+1+"");
			HttpResponse response = httpclient.execute(httpPost);
			int code = response.getStatusLine().getStatusCode();
			Log.v(TAG, "StatusCode=====" + code);
			if (code == HttpStatus.SC_OK) {
				Log.v(TAG, "响应成功！");
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity);
			}
		} catch (MalformedURLException e) {
			result = "网络错误";
		} catch (IOException e) {
			result = "网络错误";
		}
		return result;
	}

	public static String HttpClientDoGet(String urlStr) {
		String result = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpParams parms = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(parms, 10000);// 设置网络超时
			HttpConnectionParams.setSoTimeout(parms, 10000);// 设置网络超时
			HttpGet httpget = new HttpGet(urlStr);
			httpget.setHeader("flag",Calendar.getInstance().getTime().getDay()*9+1+"");
			HttpResponse response = httpclient.execute(httpget);
			int cade = response.getStatusLine().getStatusCode();
			Log.v(TAG, "StatusCode=====" + cade);
			if (cade == HttpStatus.SC_OK) {
				Log.v(TAG, "响应成功！");
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					result = EntityUtils.toString(entity);
				}
			} else {
				result = "网络错误";
			}
		} catch (MalformedURLException e) {
			result = "网络错误";
		} catch (IOException e) {
			result = "网络错误";
		}
		return result;
	}

	/**
	 * 获取 Drawable 对象
	 * 
	 * @param path
	 *            图片的绝对路径
	 * @param srcName
	 *            资源名（如：image.png）
	 * @return
	 * @throws IOException
	 */
	public static Drawable getDrawable(String path, String srcName)
			throws IOException {
		return Drawable.createFromStream(new URL(path).openStream(), srcName);
	}

	/**
	 * 获取 Bitmap 对象
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static Bitmap getBitmap(String path) throws Exception {
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		URL url = new URL(path);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("flag",Calendar.getInstance().getTime().getDay()*9+1+"");
		connection.connect();
		int code = connection.getResponseCode();
		if (code == HttpURLConnection.HTTP_OK) {
			is = connection.getInputStream();
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			byte[] data = baos.toByteArray();
			return BitmapFactory.decodeByteArray(data, 0, data.length);
		}

		return null;

	}

	/**
	 * 获取 Bitmap 对象
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static Bitmap getBitmap1(String path) throws IOException {
		return BitmapFactory.decodeStream(new URL(path).openStream());
	}

	/**
	 * 获取 BitmapStream对象
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static InputStream getBitmapStream(String path) throws Exception {
		InputStream is = null;
		URL url = new URL(path);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("flag",Calendar.getInstance().getTime().getDay()*9+1+"");
		connection.connect();
		int code = connection.getResponseCode();
		if (code == HttpURLConnection.HTTP_OK) {
			is = connection.getInputStream();
			return is;
		}
		return null;

	}

	public static String getString(String params) throws IOException {
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		URL url = new URL(params);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(6000);
		conn.setReadTimeout(6000);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("flag",Calendar.getInstance().getTime().getDay()*9+1+"");
		conn.getConnectTimeout();
		conn.connect();
		int cod = conn.getResponseCode();
		Log.d(TAG, "cod= " + cod);

		if (cod == HttpURLConnection.HTTP_OK) {
			is = conn.getInputStream();
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			while (is.read(buffer) != -1) {
				baos.write(buffer, 0, buffer.length);
			}
			byte[] data = baos.toByteArray();

			return new String(data);
		}
		return null;
	}

	public static String postString(String path) throws IOException {
		InputStream is = null;
		String params = "par=" + URLEncoder.encode("1", "UTF-8");
		byte[] postdata = params.getBytes();
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(6000);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("flag",Calendar.getInstance().getTime().getDay()*9+1+"");
		conn.connect();
		DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
		dos.write(postdata);
		dos.flush();
		dos.close();
		int cod = conn.getResponseCode();
		Log.d(TAG, "cod= " + cod);

		if (cod == HttpURLConnection.HTTP_OK) {
			is = conn.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader buffer = new BufferedReader(isr);
			StringBuilder builder = new StringBuilder();
			String line = "";
			while ((line = buffer.readLine()) != null) {
				builder.append(line);
			}
			return new String(builder.toString());

		}
		return null;
	}
	
	/*
	 * 获取html内容
	 */
	public String getHtmlString(String urlString) {  
	    try {  
	        URL url = new URL(urlString);  
	        URLConnection ucon = url.openConnection();  
	        ucon.setRequestProperty("flag",Calendar.getInstance().getTime().getDay()*9+1+"");
	        InputStream instr = ucon.getInputStream();  
	        BufferedInputStream bis = new BufferedInputStream(instr);  
	        ByteArrayBuffer baf = new ByteArrayBuffer(500);  
	        int current = 0;  
	        while ((current = bis.read()) != -1) {  
	            baf.append((byte) current);  
	        }  
	        return EncodingUtils.getString(baf.toByteArray(), "UTF-8");  
	    } catch (Exception e) {  
	        return "";  
	    }  
	}  

}
