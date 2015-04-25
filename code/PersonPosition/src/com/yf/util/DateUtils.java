package com.yf.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class DateUtils {

	/** 日期 */
	public final static String DEFAILT_DATE_PATTERN = "yyyy-MM-dd";
	/** 日期时间 */
	public final static String DEFAILT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/** 时间 */
	public final static String DEFAULT_TIME_PATTERN = "HH:mm:ss";
	/**
	 * 每天的毫秒数
	 */
	public final static long MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
  
    public static Integer getCurrentMonth() {   
        Calendar cal = Calendar.getInstance();   
        cal.setTime(new Date());   
        return cal.get(Calendar.MONTH) + 1;   
  
    }   
  
    public static Date getTimeByMonth(Integer month) {   
        Calendar cal = Calendar.getInstance();   
        cal.setTime(new Date());   
        cal.set(Calendar.MONTH, month - 1);   
        return cal.getTime();   
    }   
  
    public static Date getMonthBegin(Date date) {   
        Calendar cal = Calendar.getInstance();   
        cal.setTime(date);   
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0);   
        return cal.getTime();   
    }   
  
    public static Date getMonthEnd(Date date) {   
        Calendar cal = Calendar.getInstance();   
        cal.setTime(date);   
        cal   
                .set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0,   
                        0, 0);   
        //cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) - 1);   
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();   
    }   
    public static Date getWeekBegin(Date date) {   
        Calendar cal = Calendar.getInstance();   
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //Date mm=nDaysAgo(cal.get(Calendar.DAY_OF_WEEK)-2,date);   
        return getDayBegin(cal.getTime());   
    }   
  
    public static Date getWeekEnd(Date date) {   
        Calendar cal = Calendar.getInstance();   
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        //Date mm=nDaysAfter(cal.get(8-Calendar.DAY_OF_WEEK),date);   
        return getDayEnd(cal.getTime());   
  
    }   
    public static Date nDaysAfter(int n,Date date) {   
        Calendar cal = Calendar.getInstance();   
        cal.setTime(date);   
        cal.add(Calendar.DATE,n);   
        return cal.getTime();   
    }   
  
    public static Date getDayBegin(Date date) {   
        Calendar cal = Calendar.getInstance();   
        cal.setTime(date);   
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);   
        return cal.getTime();   
    }   
  
    public static Date getDayEnd(Date date) {   
        Calendar cal = Calendar.getInstance();   
        cal.setTime(date);   
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.add(Calendar.DATE, 1);
        //cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) - 1);   
  
        return cal.getTime();   
    }   
      
    public static Date nMonthsAgo(Integer n,Date date) {   
        Calendar cal = Calendar.getInstance();   
        cal.setTime(date);   
        cal.add(Calendar.MONTH,  - n);   
        return cal.getTime();   
    }   
    public static Date nDaysAgo(Integer n,Date date) {   
        Calendar cal = Calendar.getInstance();   
        cal.setTime(date);   
        cal.add(Calendar.DATE, -n);   
        return cal.getTime();   
    }   

	/**
	 * 转换日期字符串得到指定格式的日期类型
	 * 
	 * @param formatString
	 *            需要转换的格式字符串
	 * @param targetDate
	 *            需要转换的时间
	 * @return
	 * @throws ParseException
	 */
	public static final Date convertString2Date(String formatString,
			String targetDate) throws ParseException {
		if (StringUtils.isBlank(targetDate) || StringUtils.isEmpty(targetDate))
			return null;
		SimpleDateFormat format = null;
		Date result = null;
		format = new SimpleDateFormat(formatString);
		try {
			result = format.parse(targetDate);
		} catch (ParseException pe) {
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		return result;
	}

	/**
	 * 转换字符串得到默认格式的日期类型
	 * 
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date convertString2Date(String strDate) throws ParseException {
		Date result = null;
		try {
			result = convertString2Date(DEFAILT_DATE_PATTERN, strDate);
		} catch (ParseException pe) {
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		return result;
	}

	/**
	 * 转换日期得到指定格式的日期字符串
	 * 
	 * @param formatString
	 *            需要把目标日期格式化什么样子的格式。例如,yyyy-MM-dd HH:mm:ss
	 * @param targetDate
	 *            目标日期
	 * @return
	 */
	public static String convertDate2String(String formatString, Date targetDate) {
		SimpleDateFormat format = null;
		String result = null;
		if (targetDate != null) {
			format = new SimpleDateFormat(formatString);
			result = format.format(targetDate);
		} else {
			return null;
		}
		return result;
	}

	/**
	 * 转换日期,得到默认日期格式字符串
	 * 
	 * @param targetDate
	 * @return
	 */
	public static String convertDate2String(Date targetDate) {
		return convertDate2String(DEFAILT_DATE_PATTERN, targetDate);
	}
	
	/**
	 * 得到某年某月的第一天
	 * @param args
	 */
	public static String getFirstDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}
	
	/**
	 * 得到某年某月的最后一天
	 * @param args
	 */
	public static String getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, value);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}
	
	public static void main(String args[]){
		System.out.println("DateUtils.convertDate2String(yyyy, new Date());="+DateUtils.convertDate2String("yyyy", new Date()));
	}
	
}
