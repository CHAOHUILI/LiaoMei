package com.vidmt.lmei.util.rule;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {
	/** 时间日期格式化到年月日时分秒. */
	public static String dateFormatYMDHMS = "yyyy-MM-dd HH:mm:ss";

	/** 时间日期格式化到年月日时分秒. */
	public static String dateFormatYMDHMS_Str = "yyyy年MM月dd日 HH:mm:ss";
	/** 时间日期格式化到年月日时分. */
	public static String dateFormatYMDHM_Str = "yyyy年MM月dd日 HH:mm";
	/** 时间日期格式化到年月日. */
	public static String dateFormatYMD_Str = "yyyy年MM月dd日";

	/** 时间日期格式化到年月日. */
	public static String dateFormatYMD = "yyyy-MM-dd";
	/** 时间日期格式化到年月日. */
	public static String datePointFormatYMD = "yyyy.MM.dd";
	/** 时间日期格式化到年月. */
	public static String dateFormatYM = "yyyy-MM";

	/** 时间日期格式化到年月日时分. */
	public static String dateFormatYMDHM = "yyyy-MM-dd HH:mm";
	/** 时分. */
	public static String dateFormatHM = "HH:mm";
	/**
	 * 计算俩个时间差
	* @Title dateDiff
	* @Description TODO(这里用一句话描述这个方法的作用)
	* @param @param startTime
	* @param @param endTime
	* @param @param format
	* @param @param str
	* @param @return    参数
	* @return String    返回类型
	 */
    public static String dateDiff(String startTime, String endTime,  String format, String str) {   
        // 按照传入的格式生成一个simpledateformate对象    
        SimpleDateFormat sd = new SimpleDateFormat(format);    
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数    
        long nh = 1000 * 60 * 60;// 一小时的毫秒数    
        long nm = 1000 * 60;// 一分钟的毫秒数    
        long ns = 1000;// 一秒钟的毫秒数    
        long diff;    
        long day = 0;    
        long hour = 0;    
        long min = 0;    
        long sec = 0;    
        
        String timer ="";
        // 获得两个时间的毫秒时间差异    
        try {    
            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();    
            day = diff / nd;// 计算差多少天    
            hour = diff % nd / nh + day * 24;// 计算差多少小时    
            min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟    
            sec = diff % nd % nh % nm / ns;// 计算差多少秒    
            // 输出结果    
            System.out.println("时间相差：" + day + "天" + (hour - day * 24) + "小时"   
                    + (min - day * 24 * 60) + "分钟" + sec + "秒。");   
            timer = "仅剩：" + day + "天" + (hour - day * 24) + "小时"   
                    + (min - day * 24 * 60) + "分钟" + sec + "秒";
            System.out.println("hour=" + hour + ",min=" + min);    
            timer = day+"_"+(hour-day*24)+"_"+(min - day * 24 * 60)+"_"+sec;
//            if (str.equalsIgnoreCase("h")) {    
//                return hour;    
//            } else {    
//                return min;    
//            }    
           
        } catch (ParseException e) {    
            // TODO Auto-generated catch block    
            e.printStackTrace();    
        }    
        return timer;
    }  
    /**
	 * 
	 * @Title: simpleDateForString
	 * @Description: 时间带有年月日，转换成long
	 * @param obj
	 * @return
	 * @author : ccy   
	 * @return: Long
	 * @createTime:2014-8-29 上午10:28:09
	 * @Copyright: 壹健康
	 * @throws
	 */
	public static Long simpleDateForString(String obj){
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormatYMDHM_Str);
		Date startDate = null;
		try {
			startDate = sdf.parse(obj);
			long date=startDate.getTime();
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @Title: simpleDateForString
	 * @Description: 时间带有年月日，转换成long
	 * @param obj
	 * @return
	 * @author : ccy   
	 * @return: Long
	 * @createTime:2014-8-29 上午10:28:09
	 * @Copyright: 深圳市驱动人生软件技术有限公司
	 * @throws
	 */
	
	public static Long simpleDateForString(String obj,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date startDate = null;
		try {
			startDate = sdf.parse(obj);
			long date=startDate.getTime();
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatYMD);

	/**
	 * @Description 根据int形 yyyyMMdd，得到字符串形yyyy-MM-dd
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String dateYYYY_MM_DD(int date) throws ParseException{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(dateFormatYMD);
		return simpleDateFormat2.format(simpleDateFormat.parse("" + date));
	}

	// 将字符串转为时间戳
	public static String getTime(String user_time,String pattern) {
		String re_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date d;
		try {

			d = sdf.parse(user_time);
			long l = d.getTime();
			String str = String.valueOf(l);
			re_time = str.substring(0, 10);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return re_time;
	}

	/**
	 * 
	 * @Title: date2StringByFormat 
	 * @Description: TODO 根据格式格式化时间
	 * @param @param date
	 * @param @param pattern
	 * @param @return 
	 * @return String
	 * @throws 
	 * @author : ccy
	 */
	public static String getDateByFormat(Date date,String pattern){
		String mDateTime = null;
		if(null!=date){
			dateFormat = new SimpleDateFormat(pattern);
			mDateTime = dateFormat.format(date);
		}
		return mDateTime;
	}

	/**
	 * 描述：String类型的日期时间转化为Date类型.
	 *
	 * @param strDate String形式的日期时间
	 * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return Date Date类型日期时间
	 */
	public static Date getDateByFormat(String strDate, String format) {
		dateFormat = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = dateFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 描述：获取milliseconds表示的日期时间的字符串.
	 *
	 * @param milliseconds the milliseconds
	 * @param format  格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return String 日期时间字符串
	 */
	public static String getStringByFormat(long milliseconds,String format) {
		String thisDateTime = null;
		try {
			dateFormat = new SimpleDateFormat(format);
			thisDateTime = dateFormat.format(milliseconds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return thisDateTime;
	}

	/**
	 * 自定义格式时间戳转换
	 * 
	 * @param beginDate
	 * @return
	 */
	public static String timestampToDate(String beginDate,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String sd = sdf.format(new Date(Long.parseLong(beginDate)));
		return sd;
	}
	/**
	 * 描述：获取指定日期时间的字符串,用于导出想要的格式.
	 *
	 * @param strDate String形式的日期时间，必须为yyyy-MM-dd HH:mm:ss格式
	 * @param format 输出格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return String 转换后的String类型的日期时间
	 */
	public static String getStringByFormat(String strDate, String format) {
		String mDateTime = null;
		try {
			Calendar c = new GregorianCalendar();
			dateFormat = new SimpleDateFormat(dateFormatYMDHMS);
			c.setTime(dateFormat.parse(strDate));
			DateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
			mDateTime = mSimpleDateFormat2.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mDateTime;
	}

	/**
	 * 
	 * @Title: unixTimestamp2DateStr 
	 * @Description: TODO unix时间戳转换成时间
	 * @param @param dateparm
	 * @param @param pattern
	 * @param @return 
	 * @return String
	 * @throws 
	 * @author : ccy
	 */
	public static String unixTimestamp2DateStr(long dateparam,String pattern){
		Date date = new Date(dateparam*1000L);
		dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}
	/**
	 * 
	 * @Title: getAge 
	 * @Description: 根据手机当前时间，和传入的生日，计算年龄
	 * @param @param birthday
	 * @param @return 
	 * @return int
	 * @throws 
	 * @author : ccy
	 */
	public static int getAge(Date birthday) {
		Calendar cal = Calendar.getInstance();

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthday);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				// monthNow==monthBirth 
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				// monthNow>monthBirth 
				age--;
			}
		}
		return age+1;
	}
	/**
	 * 描述：获取偏移之后的Date.
	 * @param date 日期时间
	 * @param calendarField Calendar属性，对应offset的值， 如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
	 * @param offset 偏移(值大于0,表示+,值小于0,表示－)
	 * @return Date 偏移之后的日期时间
	 */
	public static Date getDateByOffset(Date date,int calendarField,int offset) {
		Calendar c = new GregorianCalendar();
		try {
			c.setTime(date);
			c.add(calendarField, offset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c.getTime();
	}

	/**
	 * 描述：获取指定日期时间的字符串(可偏移).
	 *
	 * @param strDate String形式的日期时间
	 * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @param calendarField Calendar属性，对应offset的值， 如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
	 * @param offset 偏移(值大于0,表示+,值小于0,表示－)
	 * @return String String类型的日期时间
	 */
	public static String getStringByOffset(String strDate, String format,int calendarField,int offset) {
		String mDateTime = null;
		try {
			Calendar c = new GregorianCalendar();
			dateFormat = new SimpleDateFormat(format);
			c.setTime(dateFormat.parse(strDate));
			c.add(calendarField, offset);
			mDateTime = dateFormat.format(c.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return mDateTime;
	}
	/**
	 * 描述：Date类型转化为String类型(可偏移).
	 *
	 * @param date the date
	 * @param format the format
	 * @param calendarField the calendar field
	 * @param offset the offset
	 * @return String String类型日期时间
	 */
	public static String getStringByOffset(Date date, String format,int calendarField,int offset) {
		String strDate = null;
		try {
			Calendar c = new GregorianCalendar();
			dateFormat = new SimpleDateFormat(format);
			c.setTime(date);
			c.add(calendarField, offset);
			strDate = dateFormat.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * 描述：获取表示当前日期时间的字符串.
	 *
	 * @param format  格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return String String类型的当前日期时间
	 */
	public static String getCurrentDate(String format) {
		String curDateTime = null;
		try {
			dateFormat = new SimpleDateFormat(format);
			Calendar c = new GregorianCalendar();
			curDateTime = dateFormat.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return curDateTime;
	}
	/**
	 * 描述：获取表示当前日期时间的字符串(可偏移).
	 *
	 * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @param calendarField Calendar属性，对应offset的值， 如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
	 * @param offset 偏移(值大于0,表示+,值小于0,表示－)
	 * @return String String类型的日期时间
	 */
	public static String getCurrentDateByOffset(String format,int calendarField,int offset) {
		String mDateTime = null;
		try {
			dateFormat = new SimpleDateFormat(format);
			Calendar c = new GregorianCalendar();
			c.add(calendarField, offset);
			mDateTime = dateFormat.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mDateTime;
	}
	/**
	 * 描述：计算两个日期所差的天数.
	 *
	 * @param date1 第一个时间的毫秒表示
	 * @param date2 第二个时间的毫秒表示
	 * @return int 所差的天数
	 */
	public static int getOffectDay(long smdate,long bdate) 
	{  
		long between_days=Math.abs((smdate-bdate))/(1000*3600*24);
		return Integer.parseInt(String.valueOf(between_days));         
	}
	/**
	 * 
	 * <br/>
	 * <b>@param smdate
	 * <b>@param bdate
	 * <b>@return<b>int<br/>
	 * <b>@Author:<b>ccy<br/>
	 * <b>@Since:<b>2014-8-27-上午10:19:07<br/>
	 */
	public static int getOffectDay(Date smdate,Date bdate){
		try {
			SimpleDateFormat sdf=new SimpleDateFormat(dateFormatYMD);
			smdate=sdf.parse(sdf.format(smdate));
			bdate=sdf.parse(sdf.format(bdate));
			Calendar cal = Calendar.getInstance();  
			cal.setTime(smdate);  
			long time1 = cal.getTimeInMillis();               
			cal.setTime(bdate);  
			long time2 = cal.getTimeInMillis();       
			long between_days=(time2-time1)/(1000*3600*24);  
			return Integer.parseInt(String.valueOf(between_days)); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 描述：计算两个日期所差的小时数.
	 *
	 * @param date1 第一个时间的毫秒表示
	 * @param date2 第二个时间的毫秒表示
	 * @return int 所差的小时数
	 */
	public static int getOffectHour(long date1, long date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(date2);
		int h1 = calendar1.get(Calendar.HOUR_OF_DAY);
		int h2 = calendar2.get(Calendar.HOUR_OF_DAY);
		int h = 0;
		int day = getOffectDay(date1, date2);
		h = h1-h2+day*24;
		return h;
	}

	/**
	 * 描述：计算两个日期所差的分钟数.
	 *
	 * @param date1 第一个时间的毫秒表示
	 * @param date2 第二个时间的毫秒表示
	 * @return int 所差的分钟数
	 */
	public static int getOffectMinutes(long date1, long date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(date2);
		int m1 = calendar1.get(Calendar.MINUTE);
		int m2 = calendar2.get(Calendar.MINUTE);
		int h = getOffectHour(date1, date2);
		int m = 0;
		m = m1-m2+h*60;
		return m;
	}
	/**
	 * 描述：获取本周一.
	 *
	 * @param format the format
	 * @return String String类型日期时间
	 */
	public static String getFirstDayOfWeek(String format) {
		return getDayOfWeek(format,Calendar.MONDAY);
	}
	/**
	 * 描述：获取本周日.
	 *
	 * @param format the format
	 * @return String String类型日期时间
	 */
	public static String getLastDayOfWeek(String format) {
		return getDayOfWeek(format,Calendar.SUNDAY);
	}
	/**
	 * 描述：获取本周的某一天.
	 *
	 * @param format the format
	 * @param calendarField the calendar field
	 * @return String String类型日期时间
	 */
	private static String getDayOfWeek(String format,int calendarField) {
		String strDate = null;
		try {
			Calendar c = new GregorianCalendar();
			dateFormat = new SimpleDateFormat(format);
			int week = c.get(Calendar.DAY_OF_WEEK);
			if (week == calendarField){
				strDate = dateFormat.format(c.getTime());
			}else{
				int offectDay = calendarField - week;
				if (calendarField == Calendar.SUNDAY) {
					offectDay = 7-Math.abs(offectDay);
				} 
				c.add(Calendar.DATE, offectDay);
				strDate = dateFormat.format(c.getTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}
	/**
	 * 描述：获取本月第一天.
	 *
	 * @param format the format
	 * @return String String类型日期时间
	 */
	public static String getFirstDayOfMonth(String format) {
		String strDate = null;
		try {
			Calendar c = new GregorianCalendar();
			dateFormat = new SimpleDateFormat(format);
			//当前月的第一天
			c.set(GregorianCalendar.DAY_OF_MONTH, 1);
			strDate = dateFormat.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;

	}
	/**
	 * 描述：获取本月最后一天.
	 *
	 * @param format the format
	 * @return String String类型日期时间
	 */
	public static String getLastDayOfMonth(String format) {
		String strDate = null;
		try {
			Calendar c = new GregorianCalendar();
			dateFormat = new SimpleDateFormat(format);
			// 当前月的最后一天
			c.set(Calendar.DATE, 1);
			c.roll(Calendar.DATE, -1);
			strDate = dateFormat.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * 描述：获取表示当前日期的0点时间毫秒数.
	 *
	 * @return the first time of day
	 */
	public static long getFirstTimeOfDay() {
		Date date = null;
		try {
			String currentDate = getCurrentDate(dateFormatYMD);
			date = getDateByFormat(currentDate+" 00:00:00",dateFormatYMDHMS);
			return date.getTime();
		} catch (Exception e) {
		}
		return -1;
	}

	/**
	 * 描述：获取表示当前日期24点时间毫秒数.
	 *
	 * @return the last time of day
	 */
	public static long getLastTimeOfDay() {
		Date date = null;
		try {
			String currentDate = getCurrentDate(dateFormatYMD);
			date = getDateByFormat(currentDate+" 24:00:00",dateFormatYMDHMS);
			return date.getTime();
		} catch (Exception e) {
		}
		return -1;
	}

	/**
	 * 描述：判断是否是闰年()
	 * <p>(year能被4整除 并且 不能被100整除) 或者 year能被400整除,则该年为闰年.
	 *
	 * @param year 年代（如2012）
	 * @return boolean 是否为闰年
	 */
	public static boolean isLeapYear(int year) {
		if ((year % 4 == 0 && year % 400 != 0) || year % 400 == 0) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 描述：根据时间返回格式化后的时间的描述.
	 * 小于1小时显示多少分钟前  大于1小时显示今天＋实际日期，大于今天全部显示实际时间
	 *
	 * @param strDate the str date
	 * @param outFormat the out format
	 * @return the string
	 */
	public static String formatDateStr2Desc(String strDate,String outFormat) {

		dateFormat  = new SimpleDateFormat(dateFormatYMDHMS);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c2.setTime(dateFormat.parse(strDate));
			c1.setTime(new Date());
			int d = getOffectDay(c1.getTimeInMillis(), c2.getTimeInMillis());
			if(d==0){
				int h = getOffectHour(c1.getTimeInMillis(), c2.getTimeInMillis());
				if(h>0){
					return "今天"+getStringByFormat(strDate,dateFormatHM);
					//return h + "小时前";
				}else if(h<0){
					//return Math.abs(h) + "小时后";
				}else if(h==0){
					int m = getOffectMinutes(c1.getTimeInMillis(), c2.getTimeInMillis());
					if(m>0){
						return m + "分钟前";
					}else if(m<0){
						//return Math.abs(m) + "分钟后";
					}else{
						return "刚刚";
					}
				}

			}else if(d>0){
				if(d == 1){
					return "昨天"+getStringByFormat(strDate,outFormat);
				}else if(d==2){
					return "前天"+getStringByFormat(strDate,outFormat);
				}
			}else if(d<0){
				if(d == -1){
					return "明天"+getStringByFormat(strDate,outFormat);
				}else if(d== -2){
					return "后天"+getStringByFormat(strDate,outFormat);
				}else{
					return Math.abs(d) + "天后"+getStringByFormat(strDate,outFormat);
				}
			}

			String out = getStringByFormat(strDate,outFormat);
			return out;

		} catch (Exception e) {
		}
		return strDate;
	}
	/**
	 * 取指定日期为星期几.
	 *
	 * @param strDate 指定日期
	 * @param inFormat 指定日期格式
	 * @return String   星期几
	 */
	public static String getWeekNumber(String strDate,String inFormat) {
		String week = "星期日";
		Calendar calendar = new GregorianCalendar();
		dateFormat = new SimpleDateFormat(inFormat);
		try {
			calendar.setTime(dateFormat.parse(strDate));
		} catch (Exception e) {
			return "错误";
		}
		int intTemp = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		switch (intTemp){
		case 0:
			week = "星期日";
			break;
		case 1:
			week = "星期一";
			break;
		case 2:
			week = "星期二";
			break;
		case 3:
			week = "星期三";
			break;
		case 4:
			week = "星期四";
			break;
		case 5:
			week = "星期五";
			break;
		case 6:
			week = "星期六";
			break;
		}
		return week;
	}

	/**
	 * 
	 * <b>@Description:<b>格式化时间 dateStr格式为:20100108<br/>
	 * <b>@param dateStr
	 * <b>@return<b>long<br/>
	 * <b>@Author:<b>ccy<br/>
	 * <b>@Since:<b>2014-8-26-下午12:51:36<br/>
	 */
	public static long dateYYYYMMDD(String dateStr){
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
			return simpleDateFormat.parse(dateStr).getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0L;
	}

	/**
	 * 
	 * <b>@Description:<b>距离下一次公历生日还有多少天<br/>
	 * <b>@param year
	 * <b>@param month
	 * <b>@param date
	 * <b>@return<b>long<br/>
	 * <b>@Author:<b>ccy<br/>
	 * <b>@Since:<b>2014-8-27-上午10:46:50<br/>
	 */
	public static long getFromBirthday(int year, int month, int date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		long now = calendar.getTimeInMillis();
		if (calendar.get(Calendar.MONTH) + 1 > month) {
			calendar.add(Calendar.YEAR, 1);
		}
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DATE, date);
		long nextBirthMillionSeconds = calendar.getTimeInMillis();
		long diffSeconds = now + nextBirthMillionSeconds;
		Date date2 = new Date();
		date2.setTime(diffSeconds);

		long diffDay = (nextBirthMillionSeconds - now)/ (1000 * 60 * 60 * 24);
		if (diffDay < 0) {
			if (isLeapYear(year)) {
				return diffDay + 366;
			} else {
				return diffDay + 365;
			}
		}
		return diffDay;
	}

	/**
	 * 
	 * <b>@Description:<b>计算星座<br/>
	 * <b>@param mouth 5月用5表示
	 * <b>@param date
	 * <b>@return<b>String<br/>
	 * <b>@Author:<b>ccy<br/>
	 * <b>@Since:<b>2014-8-28-下午1:56:19<br/>
	 */
	public static String getConstellation(int month, int date) {
		String constellation = "摩羯水瓶双鱼白羊金牛双子巨蟹狮子处女天秤天蝎射手摩羯";
		int arr[] = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
		if (month <= 0 || date <= 0) {
			return "";
		}
		int index = 2 * month - (date < arr[month - 1] ? 2 : 0);
		String str = constellation.substring(index, 2 + index);
		return str + "座";
	}


	/**
	 * 
	 * <b>@Description:<b>得到生日的年 月  日 以及日期对象<br/>
	 * <b>@param birthdayStr 20121212  12月用12
	 * <b>@return<b>Map<br/>
	 * <b>@Author:<b>ccy<br/>
	 * <b>@Since:<b>2014-9-18-下午4:12:57<br/>
	 */
	public static Map<String, Object> getBirthdayMsg(String birthdayStr){
		if(StringUtils.isEmpty(birthdayStr)){
			return null;
		}
		Map<String, Object> msgMap = new HashMap<String, Object>();
		Date birthdayDate = DateUtil.getDateByFormat(birthdayStr, "yyyyMMdd");
		int year = 1900+birthdayDate.getYear();
		int month = birthdayDate.getMonth()+1;
		int date = birthdayDate.getDate();
		int[] resultArr = {year,month,date};
		msgMap.put("birthdayDate_Arr", resultArr);
		msgMap.put("birthdayDate", birthdayDate);
		return msgMap;
	}
	
	/**
	 * 
	 * @Title: getStringByFormat 
	 * @Description: 获得想要的时间格式
	 * @param @param strDate 时间字符串
	 * @param @param format 字符串时间格式
	 * @param @param wantFormat 想要的时间格式
	 * @param @return    设定文件 
	 * @author ccy
	 * @return String    返回类型 
	 * @throws
	 */
	public static String getStringByWantFormat(String strDate, String format , String wantFormat) {
		String mDateTime = null;
		try {
			Calendar c = new GregorianCalendar();
			dateFormat = new SimpleDateFormat(format);
			c.setTime(dateFormat.parse(strDate));
			DateFormat mSimpleDateFormat2 = new SimpleDateFormat(wantFormat);
			mDateTime = mSimpleDateFormat2.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mDateTime;
	}
}
