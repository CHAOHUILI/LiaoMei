package com.vidmt.lmei.util.think;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
public class SmsUtil {
	/**
	 * 发送短信息
	 * @param tel 发送的手机号
	 * @param mes 发送的内容
	 */
  public static String SendSms(String tel,String mes) 
  {
	    HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://gbk.sms.webchinese.cn");
		post.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=gbk");// 在头文件中设置转码
		NameValuePair[] data = { new NameValuePair("Uid", "仁德智信"),//本站用户名dingjianjun
				new NameValuePair("Key", "c2ca061b6e0f99e153b6"),//接口安全密码8cec3aa37f901165d227
				new NameValuePair("smsMob", tel),
				new NameValuePair("smsText", mes) };
		post.setRequestBody(data);

		try {
			client.executeMethod(post);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Header[] headers = post.getResponseHeaders();
		int statusCode = post.getStatusCode();
		System.out.println("statusCode:" + statusCode);
		for (Header h : headers) {
			System.out.println(h.toString());
		}
		String result = null;
		try {
			result = new String(post.getResponseBodyAsString().getBytes(
					"gbk"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(result); // 打印返回消息状态

		post.releaseConnection();
		return result;
  }
}
