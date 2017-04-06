package com.vidmt.lmei.util.think;
import com.john.testlog.MyLogger;
import com.ta.util.TALogger;

public class LogUtil {
	/**
	 * 程序日志打印
	 * @param tag  标识
	 * @param info  日志信息
	 */
  public static void printLog(String tag,String info)
  {
	  TALogger.v(tag,info);
  }
   /**
    * 程序日志打印
    * @param info 日志信息
    */
  public static void printLog(String info)
  {
		MyLogger.showLogWithLineNum(4, info);
  }
}
