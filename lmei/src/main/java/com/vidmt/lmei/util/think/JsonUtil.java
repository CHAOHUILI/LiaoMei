package com.vidmt.lmei.util.think;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * json转换类
 * @author Administrator ding
 *
 */
public class JsonUtil {
	/**
	 * jsonStr转换成对象  只能转单个对象list集合不可以
	 * @param jsonStr 传入服务器得到的jsonStr
	 * @param claz  需要转换的对象  比如user.class
	 * @return
	 */
  public static <T> T JsonToObj(String jsonStr,Class<T> claz)
  {
	  //Gson gson = new Gson();
	  Gson gson = new GsonBuilder().
	  enableComplexMapKeySerialization().setDateFormat("yyyy-MM-dd") //支持复杂读写map
		.create();
	  return  gson.fromJson(jsonStr, claz);
  } 
  /**
   * jsonStr转换成对象  list可以转
   * @param jsonStr 传入服务器得到的jsonStr
   * @param type   比如： new TypeToken<List<User>>() {
						}.getType())
   * @return  返回转换以后的对象  比如List<user>
   */
  public static <T> T JsonToObj(String jsonStr,Type type)
  {
	  Gson gson = new GsonBuilder().enableComplexMapKeySerialization().
      setDateFormat("yyyy-MM-dd")//支持复杂读写map
			.create();
	  return  gson.fromJson(jsonStr, type);
  }
  /**
   * 对象转换成jsonStr
   * @param obj 需要转换的对象
   * @return  返回jsonStr
   */
  public static String ObjToJson(Object obj)
  {
	 Gson gson = new GsonBuilder().enableComplexMapKeySerialization().
	      setDateFormat("yyyy-MM-dd")//支持复杂读写map
				.create();
	  return gson.toJson(obj);
  }
  public static void main(String[] args) {
  }
}
