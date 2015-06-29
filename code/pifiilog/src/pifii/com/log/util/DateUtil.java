package pifii.com.log.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	public static String getNow(){
		return sdf.format(new Date());
	}

	public static void main(String[] args) {
//		System.out.println(new Date().toLocaleString());
//		System.out.println(timeStame2Date("1252639886", "yyyy-MM-dd HH:mm:ss "));
		System.out.println(getNow());
	}
	
	/**
	* @Title: timeStame2Date 
	* @Description: TODO(unix时间戳 转date时间类型) 
	* @param @param unix 时间戳值
 	* @param @param fors 格式 yyyy-MM-dd HH:mm:ss 
 	* 毫秒级别是三位数 用SSS表示
	* @param @return    设定文件  2015-06-16 15:28:54
	* @return String    返回类型  2015-06-16 15:28:54
	* @throws
	 */
	public static String timeStame2Date(String unix,String fors){
		Long time=Long.parseLong(unix)*1000;
		String date=new SimpleDateFormat(fors).format(new Date(time));
		return date;
	}
	
}
