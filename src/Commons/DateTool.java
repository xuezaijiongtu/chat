package Commons;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 关于时间的工具类
 * @author liuxing
 * @date 2015-01-07
 * */
public class DateTool {
	/**
	 * 获取当天时间 格式如:2014/12/05 12:12:33
	 * */
	public static String getDateTime(){
		SimpleDateFormat sDateFormat   =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );    
	    String datetime                =   sDateFormat.format(new java.util.Date());
	    return datetime;
	}
	
	public static String getDateTimeWithParam(Date date) {
		SimpleDateFormat sDateFormat   =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );    
	    String datetime                =   sDateFormat.format(date);
	    return datetime;
	}
	
	/**
	 * 获取当天时间 格式如:2014/12/05
	 * */
	public static String getDate(){
		SimpleDateFormat sDateFormat   =   new SimpleDateFormat("yyyy-MM-dd" );    
	    String datetime                =   sDateFormat.format(new java.util.Date());
	    return datetime;
	}
	
	/**
	 * 获取时间戳
	 * */
	public static String getTimeStamp(){
		 return String.valueOf(System.currentTimeMillis());
	}
}
