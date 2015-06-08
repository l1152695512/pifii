package com.pifii.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 字符串处理工具类
 * @author 李听
 *
 */
public class StringUtil {
	public StringUtil() {
	}
	/**
	 * 得到字符串中某个字符的个数
	 * @param str  字符串
	 * @param c 字符
	 * @return
	 */
	public static final int getCharnum(String str,char c){
		int num = 0;
		char[] chars = str.toCharArray();
		for(int i = 0; i < chars.length; i++)
		{
		    if(c == chars[i])
		    {
		       num++;
		    }
		} 
		
		return num;
	}
	
	/**
	 * @author 李听
	
	 * @Title: replaceNumber
	
	 * @Description: 去掉String字符串中的数字
	
	 * @param 有数字的字符串
	 * @return没有数字的字符串
	
	 * @return: String
	 */
	public static String replaceNumber(String s){
		String regP="[0-9]";//去掉string中的数字的正则规则
		Pattern p=Pattern.compile(regP);
		Matcher m=p.matcher(s);
		return m.replaceAll("").trim();
	}
	/**
	 * 返回yyyymm
	 * @param aDate
	 * @return
	 */
	  public static final String getYear_Month(Date aDate) {
		     SimpleDateFormat df = null;
		     String returnValue = "";

		     if (aDate != null) {
		         df = new SimpleDateFormat("yyyyMM");
		         returnValue = df.format(aDate);
		     }

		     return (returnValue);
		  }
	/**
	 * hxw 返回当前年
	 * 
	 * @return
	 */
	public static String getYear() {
		Calendar calendar = Calendar.getInstance();
		return String.valueOf(calendar.get(1));
	}

	/**
	 * hxw 返回当前月
	 * 
	 * @return
	 */
	public static String getMonth() {
		Calendar calendar = Calendar.getInstance();
		String temp = String.valueOf(calendar.get(2) + 1);
		if (temp.length() < 2)
			temp = "0" + temp;
		return temp;
	}
	/**
	 * 按长度分割字符串
	 * 
	 * @param content
	 * @param len
	 * @return
	 */
	public static String[] split(String content, int len) {
		if (content == null || content.equals("")) {
			return null;
		}
		int len2 = content.length();
		if (len2 <= len) {
			return new String[] { content };
		} else {
			int i = len2 / len + 1;
			System.out.println("i:" + i);
			String[] strA = new String[i];
			int j = 0;
			int begin = 0;
			int end = 0;
			while (j < i) {
				begin = j * len;
				end = (j + 1) * len;
				if (end > len2)
					end = len2;
				strA[j] = content.substring(begin, end);
				// System.out.println(strA[j]+"<br/>");
				j = j + 1;
			}
			return strA;
		}
	}
	/**
	 * 
	 * @Title: emailFormat
	 * @Description: TODO
	 * @param email
	 * @return boolean 判断是否是真确的邮件地址
	 * @return: boolean
	 */
	public static boolean emailFormat(String email)
    {
        boolean tag = true;
        final String pattern1 = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        final Pattern pattern = Pattern.compile(pattern1);
        final Matcher mat = pattern.matcher(email);
        if (!mat.find()) {
            tag = false;
        }
        return tag;
    }


    /**
     * 验证是不是EMAIL
     * @param email
     * @return
     */
	public static boolean isEmail(String email) {
		boolean retval = false;
		String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";   
	    Pattern regex = Pattern.compile(check);   
	    Matcher matcher = regex.matcher(email);   
	    retval = matcher.matches();  
		return retval;
	}
	
	/**
	 * 验证汉字为true 
	 * @param s
	 * @return
	 */
	public static boolean isLetterorDigit(String s) {
		if (s.equals("") || s == null) {
			return false;
		}
		for (int i = 0; i < s.length(); i++) {
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				// if (!Character.isLetter(s.charAt(i))){
				return false;
			}
		}
		// Character.isJavaLetter()
		return true;
	}
	/**
	 * 判断某字符串是否为null，如果长度大于256，则返回256长度的子字符串，反之返回原串
	 * @param str
	 * @return
	 */
	public static String checkStr(String str){
		if(str==null){
			return "";
		}else if(str.length()>256){
			return str.substring(256);
		}else{
			return str;
		}
	}

	/**
	 * 验证是不是Int
	 * validate a int string   
	 * @param str
	 * the Integer string.
	 * @return true if the str is invalid otherwise false.
	 */
	public static boolean validateInt(String str) {
		if (str == null || str.trim().equals(""))
			return false;

		char c;
		for (int i = 0, l = str.length(); i < l; i++) {
			c = str.charAt(i);
			if (!((c >= '0') && (c <= '9')))
				return false;
		}

		return true;
	}

	/**
	 * 字节码转换成自定义字符串 内部调试使用
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2string(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			// if (n<b.length-1) hs=hs+":";
		}
		return hs.toUpperCase();
	}

	public static byte[] string2byte(String hs) {
		byte[] b = new byte[hs.length() / 2];
		for (int i = 0; i < hs.length(); i = i + 2) {
			String sub = hs.substring(i, i + 2);
			byte bb = new Integer(Integer.parseInt(sub, 16)).byteValue();
			b[i / 2] = bb;
		}
		return b;
	}

	/**
	 * 验证字符串是否为空
	 * @param param
	 * @return
	 */
	public static boolean empty(String param) {
		return param == null || param.trim().length() < 1;
	}

	// 验证英文字母或数据
	public static boolean isLetterOrDigit(String str) {
		java.util.regex.Pattern p = null; // 正则表达式
		java.util.regex.Matcher m = null; // 操作的字符串
		boolean value = true;
		try {
			p = java.util.regex.Pattern.compile("[^0-9A-Za-z]");
			m = p.matcher(str);
			if (m.find()) {

				value = false;
			}
		} catch (Exception e) {
		}
		return value;
	}

	/**
     * 验证是否是小写字符串
     */
	@SuppressWarnings("unused")
	private static boolean isLowerLetter(String str) {
		java.util.regex.Pattern p = null; // 正则表达式
		java.util.regex.Matcher m = null; // 操作的字符串
		boolean value = true;
		try {
			p = java.util.regex.Pattern.compile("[^a-z]");
			m = p.matcher(str);
			if (m.find()) {
				value = false;
			}
		} catch (Exception e) {
		}
		return value;
	}
	
	
	/**
	 * 判断一个字符串是否都为数字
	 * @param strNum
	 * @return
	 */
	public static boolean isDigit(String strNum) {
		return strNum.matches("[0-9]{1,}");
	}

	/**
	 * 判断一个字符串是否都为数字
	 * @param strNum
	 * @return
	 */
	public static boolean isDigit2(String strNum) {
		Pattern pattern = Pattern.compile("[0-9]{1,}");
		Matcher matcher = pattern.matcher((CharSequence) strNum);
		return matcher.matches();
	}

    /**
     * 截取数字
     * @param content
     * @return
     */
    public static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

	/**
	 * 截取非数字
	 * @param content
	 * @return
	 */
	public static String splitNotNumber(String content) {
		Pattern pattern = Pattern.compile("\\D+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}
	

	public static String encode(String str, String code) {
		try {
			return URLEncoder.encode(str, code);
		} catch (Exception ex) {
			ex.fillInStackTrace();
			return "";
		}
	}

	public static String decode(String str, String code) {
		try {
			return URLDecoder.decode(str, code);
		} catch (Exception ex) {
			ex.fillInStackTrace();
			return "";
		}
	}

	public static String nvl(String param) {
		return param != null ? param.trim() : "";
	}

	public static int parseInt(String param, int d) {
		int i = d;
		try {
			i = Integer.parseInt(param);
		} catch (Exception e) {
		}
		return i;
	}

	public static int parseInt(String param) {
		return parseInt(param, 0);
	}

	public static long parseLong(String param) {
		long l = 0L;
		try {
			l = Long.parseLong(param);
		} catch (Exception e) {
		}
		return l;
	}

	public static double parseDouble(String param) {
		return parseDouble(param, 1.0);
	}

	public static double parseDouble(String param, double t) {
		double tmp = 0.0;
		try {
			tmp = Double.parseDouble(param.trim());
		} catch (Exception e) {
			tmp = t;
		}
		return tmp;
	}

	public static boolean parseBoolean(String param) {
		if (empty(param))
			return false;
		switch (param.charAt(0)) {
		case 49: // '1'
		case 84: // 'T'
		case 89: // 'Y'
		case 116: // 't'
		case 121: // 'y'
			return true;

		}
		return false;
	}

	/**
	 * public static String replace(String mainString, String oldString, String
	 * newString) { if(mainString == null) return null; int i =
	 * mainString.lastIndexOf(oldString); if(i < 0) return mainString;
	 * StringBuffer mainSb = new StringBuffer(mainString); for(; i >= 0; i =
	 * mainString.lastIndexOf(oldString, i - 1)) mainSb.replace(i, i +
	 * oldString.length(), newString);
	 * 
	 * return mainSb.toString(); }
	 * 
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final String[] split(String str, String delims) {
		StringTokenizer st = new StringTokenizer(str, delims);
		ArrayList list = new ArrayList();
		for (; st.hasMoreTokens(); list.add(st.nextToken()))
			;
		return (String[]) list.toArray(new String[list.size()]);
	}

	
	

	public static String substring(String str, int length) {
		if (str == null)
			return null;

		if (str.length() > length)
			return (str.substring(0, length - 2) + "...");
		else
			return str;
	}

	public static boolean validateDouble(String str) throws RuntimeException {
		if (str == null)
			// throw new RuntimeException("null input");
			return false;
		char c;
		int k = 0;
		for (int i = 0, l = str.length(); i < l; i++) {
			c = str.charAt(i);
			if (!((c >= '0') && (c <= '9')))
				if (!(i == 0 && (c == '-' || c == '+')))
					if (!(c == '.' && i < l - 1 && k < 1))
						// throw new RuntimeException("invalid number");
						return false;
					else
						k++;
		}
		return true;
	}

	public static boolean validateMobile(String str, boolean includeUnicom) {
		if (str == null || str.trim().equals(""))
			return false;
		str = str.trim();

		if (str.length() != 11 || !validateInt(str))
			return false;

		if (includeUnicom
				&& (str.startsWith("130") || str.startsWith("133") || str.startsWith("131")))
			return true;

		if (!(str.startsWith("139") || str.startsWith("138") || str.startsWith("137")
				|| str.startsWith("136") || str.startsWith("135")))
			return false;
		return true;
	}

	public static boolean validateMobile(String str) {
		return validateMobile(str, false);
	}

	/**
	 * delete file
	 * 
	 * @param fileName
	 * @return -1 exception,1 success,0 false,2 there is no one directory of the
	 *         same name in system
	 */
	public static int deleteFile(String fileName) {
		File file = null;
		int returnValue = -1;
		try {
			file = new File(fileName);
			if (file.exists())
				if (file.delete())
					returnValue = 1;
				else
					returnValue = 0;
			else
				returnValue = 2;

		} catch (Exception e) {
			System.out.println("Exception:e=" + e.getMessage());
		} finally {
			file = null;
			// return returnValue;
		}
		return returnValue;
	}

	public static String gbToIso(String s) throws UnsupportedEncodingException {
		return covertCode(s, "GB2312", "ISO8859-1");
	}

	public static String covertCode(String s, String code1, String code2)
			throws UnsupportedEncodingException {
		if (s == null)
			return null;
		else if (s.trim().equals(""))
			return "";
		else
			return new String(s.getBytes(code1), code2);
	}

	public static String transCode(String s0) throws UnsupportedEncodingException {
		if (s0 == null || s0.trim().equals(""))
			return null;
		else {
			s0 = s0.trim();
			return new String(s0.getBytes("GBK"), "ISO8859-1");
		}
	}
	/**
	 * 编码转换
	 * @param s
	 * @return
	 */
	public static String GBToUTF8(String s) {
		try {
			StringBuffer out = new StringBuffer("");
			byte[] bytes = s.getBytes("unicode");
			for (int i = 2; i < bytes.length - 1; i += 2) {
				out.append("\\u");
				String str = Integer.toHexString(bytes[i + 1] & 0xff);
				for (int j = str.length(); j < 2; j++) {
					out.append("0");
				}
				out.append(str);
				String str1 = Integer.toHexString(bytes[i] & 0xff);
				for (int j = str1.length(); j < 2; j++) {
					out.append("0");
				}

				out.append(str1);
			}
			return out.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unused")
	public static final String[] replaceAll(String[] obj, String oldString, String newString) {
		if (obj == null) {
			return null;
		}
		int length = obj.length;
		String[] returnStr = new String[length];
		String str = null;
		for (int i = 0; i < length; i++) {
			returnStr[i] = replaceAll(obj[i], oldString, newString);
		}
		return returnStr;
	}
	/**
	 * 字符串全文替换
	 * @param s0
	 * @param oldstr
	 * @param newstr
	 * @return
	 */
	public static String replaceAll(String s0, String oldstr, String newstr) {
		if (s0 == null || s0.trim().equals(""))
			return null;
		StringBuffer sb = new StringBuffer(s0);
		int begin = 0;
		// int from = 0;
		begin = s0.indexOf(oldstr);
		while (begin > -1) {
			sb = sb.replace(begin, begin + oldstr.length(), newstr);
			s0 = sb.toString();
			begin = s0.indexOf(oldstr, begin + newstr.length());
		}
		return s0;
	}

	public static String toHtml(String str) {
		String html = str;
		if (str == null || str.length() == 0) {
			return str;
		}
		html = replaceAll(html, "&", "&amp;");
		html = replaceAll(html, "<", "&lt;");
		html = replaceAll(html, ">", "&gt;");
		html = replaceAll(html, "\r\n", "\n");
		html = replaceAll(html, "\n", "<br>\n");
		html = replaceAll(html, "\t", "         ");
		html = replaceAll(html, " ", "&nbsp;");
		return html;
	}

	/**
	 * 字符串替换
	 * @param line
	 * @param oldString
	 * @param newString
	 * @return
	 */
	public static final String replace(String line, String oldString, String newString) {
		if (line == null) {
			return null;
		}
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = line.indexOf(oldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	public static final String replaceIgnoreCase(String line, String oldString, String newString) {
		if (line == null) {
			return null;
		}
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	/**
	 * 标签转义
	 * @param input
	 * @return
	 */
	public static final String escapeHTMLTags(String input) {
		// Check if the string is null or zero length -- if so, return
		// what was sent in.
		if (input == null || input.length() == 0) {
			return input;
		}
		// Use a StringBuffer in lieu of String concatenation -- it is
		// much more efficient this way.
		StringBuffer buf = new StringBuffer(input.length());
		char ch = ' ';
		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			if (ch == '<') {
				buf.append("&lt;");
			} else if (ch == '>') {
				buf.append("&gt;");
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}

	/**
	 * Returns a random String of numbers and letters of the specified length.
	 * The method uses the Random class that is built-in to Java which is
	 * suitable for low to medium grade security uses. This means that the
	 * output is only pseudo random, i.e., each number is mathematically
	 * generated so is not truly random.
	 * <p>
	 * 
	 * For every character in the returned String, there is an equal chance that
	 * it will be a letter or number. If a letter, there is an equal chance that
	 * it will be lower or upper case.
	 * <p>
	 * 
	 * The specified length must be at least one. If not, the method will return
	 * null.
	 * 
	 * @param length
	 *            the desired length of the random String to return.
	 * @return a random String of numbers and letters of the specified length.
	 */

	private static Random randGen = null;
	private static Object initLock = new Object();
	private static char[] numbersAndLetters = null;

	public static final String randomString(int length) {
		if (length < 1) {
			return null;
		}
		// Init of pseudo random number generator.
		if (randGen == null) {
			synchronized (initLock) {
				if (randGen == null) {
					randGen = new Random();
					// Also initialize the numbersAndLetters array
					numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
							+ "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
				}
			}
		}
		// Create a char buffer to put random letters and numbers in.
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	/**
	 * 固定长度字符串截取
	 * @param content
	 * @param i
	 * @return
	 */
	public static String getSubstring(String content, int i) {
		int varsize = 10;
		String strContent = content;
		if (strContent.length() < varsize + 1) {
			return strContent;
		} else {
			int max = (int) Math.ceil((double) strContent.length() / varsize);
			if (i < max - 1) {
				return strContent.substring(i * varsize, (i + 1) * varsize);
			} else {
				return strContent.substring(i * varsize);
			}
		}
	}

	/**
	 * 日期转String
	 * @param pattern
	 * @return
	 */
	public static String now(String pattern) {
		return dateToString(new Date(), pattern);
	}

	public static String dateToString(Date date, String pattern) {
		if (date == null) {
			return "";
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		}
	}

	public static synchronized String getNow() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(new Date());
	}

	/**
	 * String转Date
	 * @param strDate
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static java.sql.Date stringToDate(String strDate, String pattern) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Date date = simpleDateFormat.parse(strDate);
		long lngTime = date.getTime();
		return new java.sql.Date(lngTime);
	}

	public static java.util.Date stringToUtilDate(String strDate, String pattern)
			throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.parse(strDate);
	}

	public static String formatHTMLOutput(String s) {
		if (s == null)
			return null;

		if (s.trim().equals(""))
			return "";

		String formatStr;
		formatStr = replaceAll(s, " ", "&nbsp;");
		formatStr = replaceAll(formatStr, "\n", "<br />");

		return formatStr;
	}

	/*
	 * 4舍5入 @param num 要调整的数 @param x 要保留的小数位
	 */
	public static double myround(double num, int x) {
		BigDecimal b = new BigDecimal(num);
		return b.setScale(x, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/*
	 * public static String getSubstring(String content,int i){ int varsize=10;
	 * String strContent=content; if(strContent.length()<varsize+1){ return
	 * strContent; }else{ int
	 * max=(int)Math.ceil((double)strContent.length()/varsize); if(i<max-1){
	 * return strContent.substring(i*varsize,(i+1)*varsize); }else{ return
	 * strContent.substring(i*varsize); } } }
	 */


    
	/**
	 * liuqs
	 * 
	 * @param param
	 * @param d
	 * @return
	 */
	public static int parseLongInt(Long param, int d) {
		int i = d;
		try {
			i = Integer.parseInt(String.valueOf(param));
		} catch (Exception e) {
		}
		return i;
	}

	public static int parseLongInt(Long param) {
		return parseLongInt(param, 0);
	}

	

	public static String returnString(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}


	
	
	/**
     * 修改敏感字符编码
     * @param value
     * @return
     */
	public static String htmlEncode(String value){
	    String re[][] = {{"<","&lt;"},
	                     {">","&gt;"},
	                     {"\"","&quot;"},
	                     {"\\′","&acute;"},
	                     {"&","&amp;"}
	                    };
	   
	    for(int i=0; i<4; i++){
	        value = value.replaceAll(re[i][0], re[i][1]);
	    }
	    return value;
	}
	/**
     * 防SQL注入
     * 
     * @param str
     * @return
     */
	public static boolean sql_inj(String str) {
		 String inj_str = "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+|,";
		 String inj_stra[] = inj_str.split("|");
		 for (int i=0 ; i < inj_stra.length ; i++ )
		 {
			 if (str.indexOf(inj_stra[i])>=0)
			 {
			 	return true;
			 }
		 }
		 return false;
	 }

	// 存放特殊的数组
	private static final char[] chArr = new char[]{'$','|','[',']','(',')','*','+','?','.'};
	
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
	/**
	 * 接收从资源文件中读取的键的值
	 */
	public static String propreties_key_value = "";
	
	
	/**
	 * 将字符串数组用“，”连接成字符串
	 * @param list	List 对象
	 * @return	连接后的字符串
	 */
	public static String join(List<Object> list){
		if(null == list  || list.size() == 0) return "";
		Object[] arr = new Object[1];
		arr = list.toArray(arr);
		StringBuffer sb = new StringBuffer();
		for(Object str : arr){
			if(null == str || str.equals("")) continue;
			sb.append(str+",");
		}
		return RemoveStr(sb);
	}
	
	
	/**
	 * 将字符串数组用“，”连接成字符串
	 * @param arr	Object 数组
	 * @return	连接后的字符串
	 */
	public static String join(Object[] arr){
		StringBuffer sb = new StringBuffer();
		for(Object str : arr){
			if(null == str || str.equals("")) continue;
			sb.append(str+",");
		}
		return RemoveStr(sb);
	}
	
	/**
	 * 将字符串数组用“，”连接成字符串
	 * @param arr	字符串数组
	 * @return	连接后的字符串
	 */
	public static String join(String[] arr){
		StringBuffer sb = new StringBuffer();
		for(String str : arr){
			if(null == str || str.equals("")) continue;
			sb.append(str+",");
		}
		return RemoveStr(sb);
	}
	/**
     * 字符串按字节截取
     * @param str 原字符
     * @param len 截取长度
     * @since 2006.07.20
     */
	public static String SplitString(String str, int len) {
		return SplitString(str, len, "...");
	}
     
    /**
     * 字符串按字节截取
     * @param str 原字符
     * @param len 截取长度
     * @param elide 省略符
     * @since 2006.07.20
     */
	public static String SplitString(String str,int len,String elide) {
		if (str == null) {
			return "";
		}
		byte[] strByte = str.getBytes();
		int strLen = strByte.length;
		int elideLen = (elide.trim().length() == 0) ? 0 : elide.getBytes().length;
		if(len>=strLen || len<1) {
			return str;
		}
		if(len-elideLen > 0) {
			len = len-elideLen;
		}
		int count = 0;
		for(int i=0; i<len; i++) {
			int value = (int)strByte[i];
			if(value < 0){
				count++;
			}
		}
		if(count % 2 != 0) {
			len = (len == 1) ? len + 1 : len - 1;
		}
		return new String(strByte, 0, len) + elide.trim();
	}
	
	/**
	 * 打印输出方法
	 * @param msg
	 */
	public void P(Object msg)
	{
		System.out.println(msg);
	}
	
	/**
	 * 分割字符串为字符串数组
	 * @param strs
	 * @param op
	 * @return
	 */
	public static String[] splitStr(String strs,String op)
	{
		if(isValidStr(strs) && isValidStr(op))
		{
			return strs.split(op);
		}
		return null;
	}
	
	/**
	 * 验证字符串是否为 null 或 "" 
	 * 不为 null 或 "" 返回 True 否则返回 false
	 * @param str
	 * @return
	 */
	public static boolean isValidStr(String str)
	{
		return (null != str && !"".equals(str) && !"null".equals(str)) ? true : false;
	}
	
	/**
	 * 验证字符串是否为 null 或 "" 
	 * 不为 null 或 "" 返回 True 否则返回 false
	 * @param obj
	 * @return
	 */
	public static boolean isValidObj(Object obj)
	{
		return (null != obj && !"".equals(obj.toString())) ? true : false;
	}
	
	/**
	 * 验证整型对象是否为空
	 * @param <T>
	 * @param obj
	 * @return 如果为空就返回 false 否则为 true
	 */
	public static <T> boolean isValidIntegerNull(Integer obj){
		if(null != obj && 0 !=obj) return true;
		return false;
	}
	/**
	 * 将字符串转换为整型，如果字符串为 空 或 “” 就为0
	 * @param val
	 * @return
	 */
	public static Integer convertInt(String val){
		return (null == val || "".equals(val)) ? 0 : Integer.parseInt(val);
	}
	/**
	 * 将字符串转换为日期格式
	 * 举例: Date date = StringHandler.dateFormat("yyyy-mm-dd","2008-1-22");
	 * @param pattern	日期格式："yyyy-mm-dd"
	 * @param strDate
	 * @return 返回一个 Date对象
	 */
	public static Date dateFormat(String pattern,String strDate)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 将日期格式化为字符串
	 * @param pattern	日期格式："yyyy-MM-dd"等
	 * @param date	日期对象
	 * @return
	 */
	public static String dateFormatToStr(String pattern,Date date){
		if(null==pattern || "".equals(pattern)) pattern="yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		if(null == date) return "";
		return sdf.format(date);
	}
	/**
	 *	将字符串以特定的符号转换为数组 (如果未提供就将以“,”进行分割)
	 * @param strs 一个字符串类型的变长参数。<br/>\n
	 *            如果提供分割自己的分割符号，请将它作为第一个参数传入,
	 *            第二个就是要分割成数组的字符串
	 *            例如：name="peng,deng,hao";
	 *            		StringHandler.convertStrToArr(",",name);
	 * @return
	 */
	public static String[] convertStrToArr(String... strs){
		String[] arrStr = null;
		String  sigin = ",";
		if(strs==null) return null;
		if(strs.length==1) return strs[0].split(sigin);
		if(strs.length >= 2){
			if(isValidSigin(strs[0])){
				strs[1] = replaceSigins(strs[1],strs[0], sigin);
			}else{
				sigin = strs[0]; //过滤不能作为字符串分割的符号
			}
			return strs[1].split(sigin);
		}
		return arrStr;
	}
	
	/**
	 * 替换特殊字符以便分割成字符串数组
	 * @param strs
	 * @param sourceSigin：要被替换的符号字符
	 * @param targetSigin：准备用符号替换字符
	 * @return
	 */
	public static String replaceSigins(String strs,String sourceSigin,String targetSigin){
		if(strs==null) return "";
		StringBuffer sb = new StringBuffer(strs);
		int sindex = sb.indexOf(sourceSigin);
		if(sindex<0) return sb.toString();
		int start = 0,len = sb.length();
		while(start != len){
			int index = sb.indexOf(sourceSigin,start);
			if(index != -1){
				start = index+1;
				sb.deleteCharAt(index).insert(index,targetSigin);
			}else{
				break;
			}
		}
		return sb.toString();
	}
	/**
	 * 验证指定的符号是否能作为分割符进行字符串转换为字符串数组的分割
	 * @param sigin	要验证的符号
	 * @return 返回 true 则为非法字符 false 合法 
	 */
	public static boolean isValidSigin(String sigin){
		char sourceCh = sigin.charAt(0);
		for(char ch : chArr){
			if(ch == sourceCh) return true;
		}
		return false;
	}
	
	/***
	 * 获取真实数据，回车字段保留
	 * @param str
	 * @return
	 */
	public static String GetTrueStr(String str){
		String tempstr = "";
		if(str==null) return tempstr;
		for(int j=0;j<str.length();j++){ 
			 char ch = str.charAt(j); 
			 if(ch=='\n')
				 tempstr += "\\n";
			 else if(ch == '\r' || ch == '\t') 
				 tempstr += "";
			 else 
				 tempstr += String.valueOf(ch);
			// System.out.println(tempstr+"<<"+j);
		}
		return tempstr;
	}
	/**
	 * 中文乱码问题 
	 * @param str
	 * @return
	 */
	public static String getStr(String str){
		try
	    {
			String temp_p = str;
			byte[] temp_t = temp_p.getBytes("ISO-8859-1");
			String temp = new String(temp_t);
			return temp;
	    }
	    catch(Exception e)
	    {}
	    return null;
	}
	/**
	 * 中文乱码问题  ISO-8859-1 转 UTF-8
	 * @param str
	 * @return
	 */
	public static String GetUTF_8(String str){
		try {
			String temp_p = str;
			byte[] temp_t = temp_p.getBytes("ISO-8859-1");
			String temp = new String(temp_t,"UTF-8");
			return temp;
	    }catch(Exception e) {}
		return null;
	}
	/**
	 * 中文乱码问题  ISO-8859-1 转 GBK
	 * @param str
	 * @return
	 */
	public static String GetGBK(String str){
		try {
			String temp_p = str;
			byte[] temp_t = temp_p.getBytes("ISO-8859-1");
			String temp = new String(temp_t,"GBK");
			return temp;
	    }catch(Exception e) {}
		return null;
	}
	/*****
	 * 打印
	 * @param str：打印值
	 * @param msg：打印说明
	 */
	public static void P(String str,String msg)
	{
		System.out.println(msg+" 值："+str);
	}
	
	/*****
	 * 打印
	 * @param str：打印值
	 */
	public static void P(String str)
	{
		System.out.println(str);
	}
	
	/***
	 * 校验多值
	 * @param str：待校验的字符串
	 * @param iscase：是否检验大小写：true:大小写敏感
	 * @param values：比较的值
	 * @return
	 */
	public static boolean Equals(String str,boolean iscase,String... values){
		for(int i=0;i<values.length;i++){
			if(!iscase){
				str = str.toLowerCase();
				values[i] = values[i].toLowerCase();
			}
			if(str.equals(values[i]))
				return true;
		}
		return false;
	}
	
	/***
	 * 校验多值，不区分大小写
	 * @param str
	 * @param values
	 * @return
	 */
	public static boolean Equals(String str,String... values){
		return Equals(str,false,values);
	}

    /*********
  	 * 空处理字段
  	 * @param valueStr：接收值
  	 * @param nullvalue：为空时默认的值
  	 * @return：为不空的值
  	 */
  	public static String GetValue(String valueStr,String nullvalue)
  	{
  		return (valueStr==null || "".equals(valueStr))?nullvalue:valueStr;
  	}
  	
  	/**
  	 * 将指定的值转换成""空符串
  	 * @param valueStr 要转换的字符
  	 * @param nullStr 用来比较的值
  	 * @param nullvalue
  	 * @return
   	*/
  	public static String GetValue(String valueStr,String eqStr,String nullvalue)
  	{
  		return (valueStr.equals(eqStr))?nullvalue:valueStr;
  	}
  	
  	/*********
  	 * 空处理字段,默认为""
  	 * @param valueStr：接收值
  	 * @return：为不空的值
  	 */
  	public static String GetValue(String valueStr)
  	{
  		return (null==valueStr)?"":valueStr;
  	}

  	/**
     * 判断是否为数字型:
     * "33" "+33" "033.30" "-.33" ".33" " 33." " 000.000 "
     * @param str：接收值
     * @return boolean
    */
    public static boolean IsNumeric(String str) {
    	int begin = 0;
        boolean once = true;
        if (str == null || str.trim().equals("")) {
        	return false;
        }
        str = str.trim();
        if (str.startsWith("+") || str.startsWith("-")) {
        	if (str.length() == 1) {
        		return false;
            }
            begin = 1;
        }
        for (int i = begin; i < str.length(); i++) {
        	if (!Character.isDigit(str.charAt(i))) {
        		if (str.charAt(i) == '.' && once) {
        			// '.' can only once
                    once = false;
                }else {
                	return false;
                }
            }
        }
        if (str.length() == (begin + 1) && !once) {
        	// "." "+." "-."
            return false;
        }
        return true;
    }

    /**
     * 判断是否为整形:
     * "33" "003300" "+33" " -0000 "
     * @param str：接收值
     * @return boolean
     */
    public static boolean IsInteger(String str) {
    	int begin = 0;
    	if (str == null || str.trim().equals("") || str.length()>7) {
    		return false;
    	}
    	str = str.trim();
    	if (str.startsWith("+") || str.startsWith("-")) {
    		if (str.length() == 1) {
    			// "+" "-"
    			return false;
    		}
    		begin = 1;
    	}
    	for (int i = begin; i < str.length(); i++) {
    		if (!Character.isDigit(str.charAt(i))) {
    			return false;
    		}
    	}
    	return true;
   }
   
	/**
  	 * 字符串分割函数
  	 * @param sign  分割符号
  	 * @param splids 要进行分割的字符串
  	 * @return  返回分割好的字符串数组
  	 * @throws Exception
  	 */
  	public static String[] Split(String sign,String splids)
	{
		if (null != splids && !"".equals(splids)) {
			String[] ids = splids.split(sign);
		return ids;
		}
		return null;
		}
  	
  	/**
  	 * 将字符串分割成字符串数组，在分割时根据参数
  	 * removeStr从分割后的字符数组元素中移出指定字符
  	 * 示例：  StringHandler.Split(",",vmodelid,"B,S,M");
  	 * @param sign
  	 * @param splids
  	 * @param removeStr
  	 * @return
  	 * @throws Exception
  	 */
  	public static String[] Split(String sign,String splids,String removeStr) {
  		String[] ids = Split(sign,splids);
  		String[] rChars = removeStr.split(sign);
  		int i=0;
  		for(String id : ids){
  			int offset = id.indexOf(rChars[i]);
  			if(offset != -1){
  				ids[i] = id.substring(offset+1,id.length());
  			}
  			i++;
  		}
  		return ids;
  	}
  	
  	/**
  	 * 将一个字符串加入到剪贴板
  	 * @param str
  	 */
  	public static void Paste(String str){
  		Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
  		Transferable text = new StringSelection(str); 
  		sysClip.setContents(text, null); 
  	}

	
	/**  
	* 提供小数位四舍五入处理
	* @param v 需要四舍五入的数字
	* @param scale 小数点后保留几位
	* @return 四舍五入后的结果
	*/
	public static String Round(String v,int scale){
		boolean isNum = IsNumeric(v);
		if(!isNum) return v;
		if(v.indexOf(".") != -1){
			String xiaoshu = v.split("\\.")[1];
			int unints = scale-xiaoshu.length();
			if(unints>0){
				xiaoshu = "";
				for(int i=0;i<unints;i++){
					xiaoshu+="0";
				}
				v+=xiaoshu;
			}
		}else{
			return v;
		}
		String temp="###0.";
		for (int i=0;i<scale;i++ ){
			temp+="0";
		}
		return String.valueOf(new java.text.DecimalFormat(temp).format(Double.parseDouble(v)));  
	}
	
	/**  
	* 提供小数位四舍五入处理(保留两位小数)
	* @param v 需要四舍五入的数字
	* @return 四舍五入后的结果
	*/
	public static Double Round(Double v){
		return Round(v,2);  
	}
	
	/**  
	* 提供小数位四舍五入处理
	* @param v 需要四舍五入的数字
	* @param scale 小数点后保留几位
	* @return 四舍五入后的结果
	*/
	public static Double Round(Double v,int scale){
		if(null==v) return 0d;
		String temp="###0.";
		for (int i=0;i<scale;i++ ){
			temp+="0";
		}
		return Double.parseDouble(new java.text.DecimalFormat(temp).format(v));  
	}
	
	
	/**  
	* 提供小数位四舍五入处理并以字符串返回
	* @param v 需要四舍五入的数字
	* @param scale 小数点后保留几位
	* @return 四舍五入后的结果
	*/
	public static String Round2Str(Double v,int scale){
		if(null==v) return "0.00";
		String temp="###0.";
		for (int i=0;i<scale;i++ ){
			temp+="0";
		}
		v = Double.parseDouble(new java.text.DecimalFormat(temp).format(v));
		return String.valueOf(v);  
	}
	
	/**  
	* 提供小数位四舍五入处理并以 BigDecimal返 回(保留两位小数)
	* @param v 需要四舍五入的数字
	* @return 四舍五入后的 BigDecimal
	*/
	public static BigDecimal Round2BigDecimal(Double v){
		if(null==v) return new BigDecimal(0);
		String temp="###0.";
		for (int i=0;i<2;i++ ){
			temp+="0";
		}
		v = Double.parseDouble(new java.text.DecimalFormat(temp).format(v));
		return new BigDecimal(String.valueOf(v));  
	}
	
	/**  
	* 提供小数位四舍五入处理并以 BigDecimal返 回
	* @param v 需要四舍五入的数字
	* @param scale 小数点后保留几位
	* @return 四舍五入后的 BigDecimal
	*/
	public static BigDecimal Round2BigDecimal(Double v,int scale){
		if(null==v) return new BigDecimal(0);
		String temp="###0.";
		for (int i=0;i<scale;i++ ){
			temp+="0";
		}
		v = Double.parseDouble(new java.text.DecimalFormat(temp).format(v));
		return new BigDecimal(String.valueOf(v));  
	}
	
	/** 
	 * 格式化数字为千分位显示； 
	 * @param 要格式化的数字； 
	 * @return 
	 */  
	public static String fmtMicrometer(Object text)  
	{
		if(isValidObj(text)){
			return fmtMicrometer(text.toString());
		}
		return "";
	}
	/** 
	 * 格式化数字为千分位显示； 
	 * @param 要格式化的数字； 
	 * @return 
	 */  
	public static String fmtMicrometer(String text)  
	{  
	    DecimalFormat df = null;  
	    if(text.indexOf(".") > 0)  
	    {  
	        if(text.length() - text.indexOf(".")-1 == 0)  
	        {  
	            df = new DecimalFormat("###,##0.");  
	        }else if(text.length() - text.indexOf(".")-1 == 1)  
	        {  
	            df = new DecimalFormat("###,##0.0");  
	        }else  
	        {  
	            df = new DecimalFormat("###,##0.00");  
	        }  
	    }else   
	    {  
	        df = new DecimalFormat("###,##0");  
	    }  
	    double number = 0.0;  
	    try {  
	         number = Double.parseDouble(text);  
	    } catch (Exception e) {  
	        number = 0.0;  
	    }  
	    return df.format(number);  
	}  
	
	/**  
	* 提供小数位四舍五入处理
	*  返回最小的（最接近负无穷大）double 值，该值大于或等于参数，并且等于某个整数。
	* @param v 需要四舍五入的数字
	* @param scale 小数点后保留几位
	* @return 四舍五入后的结果
	*/
	public static Double ceil(Double v,int scale){
		if(null==v) return 0d;
		String temp="###0.";
		for (int i=0;i<scale;i++ ){
			temp+="0";
		}
		return Double.parseDouble(new java.text.DecimalFormat(temp).format(v));  
	}
	
	/**
	 * 根据传入的字符串，返回一个整型值
	 * @param val 字符串值
	 * @return 返回一个整型值
	 */
	public static Integer getIntegerVal(String val){
		if(!isValidStr(val)) val = "0";
		Integer intVal = Integer.parseInt(val);
		return intVal;
	}
	
	/**
	 * 根据传入的字符串，返回一个长整型值
	 * @param val 字符串值
	 * @return 返回一个长整型值
	 */
	public static Long getLongVal(String val){
		if(!isValidStr(val)) val = "0";
		Long intVal = Long.parseLong(val);
		return intVal;
	}
	
	/**
	 * 根据传入的字符串，返回一个双精度 Double 值
	 * @param val 字符串值
	 * @return 返回一个Double 值
	 */
	public static Double getDoubleVal(String val){
		if(!isValidStr(val)) val = "0.0";
		Double dVal = Double.parseDouble(val);
		return dVal;
	}
	
	/**
	 * 根据传入的字符串，返回一个 Float 值
	 * @param val 字符串值
	 * @return 返回一个Float 值
	 */
	public static Float getFloateVal(String val){
		if(!isValidStr(val)) val = "0.0";
		Float fVal = Float.parseFloat(val);
		return fVal;
	}
	
	  /**
     * 当Double 类型数据为科学计算法显示时，将其转换成正常的形式显示
     * 例如： 1.0E7 ---> 10000000
     * @param val	要转换的科学计数法数据
     * @return 返回正常的数据显示
     */
    public static String getScienceVal(double val){
    	NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        
       String str = numberFormat.format(val);
       String[] strArray = str.split(",");
       StringBuffer buffer = new StringBuffer();
       for(int i=0;i<strArray.length;i++){
        buffer.append(strArray[i]);
       }
       return buffer.toString();
    }
    
	/**
	 * 判断是否手机号码 
	 * @param mobilePhone
	 * @return
	 */
	public static boolean IsMobile(String mobilePhone){
		if(mobilePhone.length()==11 && (mobilePhone.startsWith("13") || mobilePhone.startsWith("15") || mobilePhone.startsWith("18")))   
	    	return true;
		else 
	    	return false;
	}
  	
	/**
	 * 移除指定的字符
	 * @param sb  StringBuffer对象
	 * @param remove_str	要移除的对象
	 * 使用示例:
	 * 		StringBuffer sb = new StringBuffer("ab,cd,ef,");
	 *  	String str = StringHandler.RemoveStr(sb,",");
	 *  	str 结果就是 "ab,cd,ef"
	 * @return
	 */
	public static String RemoveStr(StringBuffer sb,String remove_str)
	{
		return (sb != null && sb.length() > 0 && remove_str != null && sb.indexOf(remove_str) != -1) ? sb.deleteCharAt(sb.lastIndexOf(remove_str)).toString() : "";
	}
	
	/**
	 * 从源数组中移除指定的多个数据
	 * @param sourceArr  String[] 对象
	 * @param removeArr	String[] 要移除的对象
	 * 使用示例:
	 * 		String sourceArr = new String[]{"ab","cd","ef,"};
	 *  	String removeArr = new String[]{"ab"};
	 *  	str 结果就是 "cd","ef"
	 * @return
	 */
	public static String[] Removes(String[] sourceArr, String[] removeArr){
		List<String> list = Arrays.asList(sourceArr);
		if(null == list || list.size() == 0) return null;
		if(null == removeArr || removeArr.length == 0) return sourceArr;
		List<String> newList = new ArrayList<String>(list);
//		List<Integer> delIndexList = new ArrayList<Integer>();
		for(int i=0,count=list.size(); i<count; i++){
//			boolean isDel = false;
			String sourceStr = list.get(i);
			for(String removeStr : removeArr){
				if(sourceStr.equals(removeStr)){
					newList.remove(removeStr);
//					isDel = true;
					break;
				}
			}
//			if(isDel) delIndexList.add(i);
		}
//		if(null == delIndexList || delIndexList.size() == 0) return sourceArr;
//		for(int i=0,count = delIndexList.size(); i<count; i++){
//			int delIndex = delIndexList.get(i);
//			newList.remove(delIndex);
//		}
		String[] newArr = new String[1];
		newArr = newList.toArray(newArr);
		return newArr;
	}
	/**
	 * 移除最后一个逗号
	 * @param sb  StringBuffer对象
	 * 使用示例:
	 * 		StringBuffer sb = new StringBuffer("ab,cd,ef,");
	 *  	String str = StringHandler.RemoveStr(sb);
	 *  	str 结果就是 "ab,cd,ef"
	 * @return
	 */
	public static String RemoveStr(StringBuffer sb)
	{
		return (sb != null && sb.length()>0 && sb.indexOf(",") != -1) ? sb.deleteCharAt(sb.lastIndexOf(",")).toString() : "";
	}
	/**
	 * 移除最后一个逗号
	 * @param sb  StringBuilder对象
	 * 使用示例:
	 * 		StringBuffer sb = new StringBuffer("ab,cd,ef,");
	 *  	String str = StringHandler.RemoveStr(sb);
	 *  	str 结果就是 "ab,cd,ef"
	 * @return
	 */
	public static String RemoveStr(StringBuilder sb)
	{
		return (sb != null && sb.length()>0 && sb.indexOf(",") != -1)  ? sb.deleteCharAt(sb.lastIndexOf(",")).toString() : "";
	}
	/**
	 * 移除指定的字符
	 * @param sb  StringBuffer对象
	 * @param remove_str	要移除的对象
	 * 使用示例:
	 * 		String strids ="ab,cd,ef,";
	 *  	String str = StringHandler.RemoveStr(strids,",");
	 *  	str 结果就是 "ab,cd,ef"
	 * @return
	 */
	public static String RemoveStr(String str,String remove_str)
	{
		StringBuffer sb = new StringBuffer(str);
		return RemoveStr(sb,remove_str);
	}
	/**
	 * 替换回车等字符为空字符
	 * @param content
	 * @return
	 */
	public static String replaceEnter(String content)
	{
		//去掉回车和换行符，否则页面无法显示
		return (null != content) ? content.replaceAll("[\\n\\r]*","") : "";
	}
	/**
	 * 替换双引号和单引号为HTML 标记的双引号和单引号
	 * @param content
	 * @return
	 */
	public static String replaceBothSign(String content)
	{
		return (null != content) ? content = content.replaceAll("\\\"",   "&quot").replace("\\\'", "&apos;"): "";
	}
	/**
	 * 替换 SQL 查询语句中的 一个单引号为两个单引号，以防止非法注入
	 * 例如：StringHandler.replaceSqlSign("'王兰'") ----> ''王兰''
	 * @param content
	 * @return
	 */
	public static String replaceSqlSign(String content){
		return (null != content) ? content = content.replaceAll("'",   "''"): "";
	}
	/**
	 * 用 indexOf 和 subString 方法将一个字符串以指定的分隔符
	 * 拆分为有两个长度的字符串数组
	 * @param val 要进行拆分的字符串
	 * @param sigin 分隔符
	 * @return 返回两个长度的字符串数组
	 */
	public static String[] splitStrToArr(String val,String sigin){
		int offset = val.indexOf(sigin);
		if(-1 == offset) return new String[]{val};
		String[] arr = {val.substring(0, offset),val.substring(offset+sigin.length(),val.length())};
		return arr;
	}
	/**
	 * 将 arr 转化为 JavaScript 数组格式的字符串
	 * 例如："["btn_1_add","btn_1_del"]"
	 * @param <T>
	 * @param arr 要转换的集合类
	 * @return 返回转化后的 JavaScript 数组格式的字符串
	 */
	public static <T> String getJsArrStr(List<T> arr){
		StringBuffer sb = new StringBuffer("[");
		for(T item : arr){
			sb.append("\""+item+"\",");
		}
		String arrStr = RemoveStr(sb, ",");
		arrStr += "]";
		return arrStr;
	}
	/**
	 * 将指定的字符串值以指定的类型转型后并返回
	 * @param val	要转型的字符串值
	 * @param type	数据类型的简写，相对应数据类型如下
	 * * 则依次是以下意思：
	 * 		I : Integer	,	L : Long
	 * 		F : Float	,	O : Double
	 * 		D : Date	,	B : Boolean
	 * 	   BG : BigDecimal
	 * @return	返回一个转型后的对象值
	 */
	public static Object getValByType(String val,String type){
		type = type.toUpperCase();
		Object objVal = null;
		if("I".equals(type)){
			objVal = getIntegerVal(val);
		}else if("L".equals(type)){
			objVal = getLongVal(val);
		}else if("F".equals(type)){
			objVal = getFloateVal(val);
		}else if("O".equals(type)){
			objVal = getDoubleVal(val);
		}else if("D".equals(type)){
			objVal = dateFormat(DATE_FORMAT,val);
		}else if("B".equals(type)){
			objVal = new Boolean(val.toLowerCase());
		}else if("BG".equals(type)){
			objVal = new BigDecimal(val);
		}
		return objVal;
	}
	/**
	 * 获取 classes 所在的真实物理路径
	 * @return
	 */
	public static String getClassPath(){
		String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		int index = classPath.indexOf("/");
		if(index == 0) classPath = classPath.substring(1);
		return classPath;
	}
	/**
	 * 将 Windows 中文件路径分隔符替换成正斜杠分隔符。
	 * 例如： E:\\j2ee_proj\\test  ---->  E:/j2ee_proj/test
	 * @param path	要替换的Window 分隔符
	 * @return
	 */
	public static String getSpeatorPath(String path){
		return path.replaceAll("[\\\\]", "/");
	}
	/**
	 * 格式化字符串
	 * 例如：
	 *   String val = "\"{0}\" 是我的弟的女朋友，她现在在{1}工作!";
	 *	val = format(val, "程明强","IBM");
	 *	输出：val = ""程明强" 是我的弟的女朋友，她现在在IBM工作";
	 * @param source	要格式化的源字符串
	 * @param args	字符串中填充的参数值
	 * @return 返回格式化后的字符串
	 */
	public static String format(String source , Object... args){
		if(!isValidStr(source)) return null;
		for(int i=0,count=args.length; i<count; i++){
			Object argVal = args[i];
			if(!isValidObj(argVal)) argVal = "";
			source = source.replace("{"+i+"}", argVal.toString());
		}
		return source;
	}
	
	public static String getClassesPath(String fileName){
		String resFilePath = isValidStr(fileName) ? fileName : "";
		return Thread.currentThread().getContextClassLoader().getResource(resFilePath).getPath();
	}
	
	static final String SIGINS_JINJIN = "##";
	/**
	 * 获取组合值的ID部份内容。
	 * 例如：12##张三 --> 返回 12 
	 * @param strs 要拆份的字符串数组
	 * @param sigins	分割标识符。如不提供，则默认为“##”
	 * @return	返回 ID部份内容
	 */
	public static String getIdsBySigins(String strs,String sigins){
		if(!isValidStr(strs)) return "";
		sigins = (isValidStr(sigins)) ? sigins : SIGINS_JINJIN;
		String[] arr = strs.split(SIGINS_JINJIN);
		if(null == arr || arr.length == 0) return "";
		return arr[0];
	}

    private static Pattern numericPattern = Pattern.compile("^[0-9\\-]+$");
    private static Pattern integerPattern = Pattern.compile("^[0-9]+$");
    private static Pattern numericStringPattern = Pattern.compile("^[0-9\\-\\-]+$");
    private static Pattern floatNumericPattern = Pattern.compile("^[0-9\\.]+$");
    private static Pattern abcPattern = Pattern.compile("^[a-z|A-Z]+$");
    public static final String splitStrPattern = ",|，|;|；|、|\\.|。|-|_|\\(|\\)|\\[|\\]|\\{|\\}|\\\\|/| |　|\"";
    
    /**
     * 判断是否数字表示
     * 
     * @param src
     *            源字符串
     * @return 是否数字的标志
     */
    public static boolean isNumeric(String src) {
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = numericPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }
    
    /**
     * 
     * 是否是数字。整数，没有任何符号
     *
     * @param src
     * @return
     */
    public static boolean isInteger(String src){
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = integerPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }
    
    /**
     * 判断是否数字
     * 
     * @param src
     *            源字符串
     * @return 是否数字的标志
     */
    public static boolean isNumericString(String src) {
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = numericStringPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }
    
    /**
     * 判断是否纯字母组合
     * 
     * @param src
     *            源字符串
     * @return 是否纯字母组合的标志
     */
    public static boolean isABC(String src) {
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = abcPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }
    
    /**
     * 判断是否浮点数字表示
     * 
     * @param src
     *            源字符串
     * @return 是否数字的标志
     */
    public static boolean isFloatNumeric(String src) {
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = floatNumericPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }
    
    /**
     * 把string array or list用给定的符号symbol连接成一个字符串
     * 
     * @param array
     * @param symbol
     * @return
     */
    public static String joinString(List array, String symbol) {
        String result = "";
        if (array != null) {
            for (int i = 0; i < array.size(); i++) {
                String temp = array.get(i).toString();
                if (temp != null && temp.trim().length() > 0)
                    result += (temp + symbol);
            }
            if (result.length() > 1)
                result = result.substring(0, result.length() - 1);
        }
        return result;
    }
    
    /**
     * 截取字符串
     * 
     * @param subject
     * @param size
     * @return
     */
    public static String subStringNotEncode(String subject, int size) {
        if (subject != null && subject.length() > size) {
            subject = subject.substring(0, size) + "...";
        }
        return subject;
    }
    
    
    /**
     * 截取字符串　超出的字符用symbol代替 　　
     * 
     * @param len
     *            　字符串长度　长度计量单位为一个GBK汉字　　两个英文字母计算为一个单位长度
     * @param str
     * @param symbol
     * @return
     */
    public static String getLimitLengthString(String str, int len, String symbol) {
        int iLen = len * 2;
        int counterOfDoubleByte = 0;
        String strRet = "";
        try {
            if (str != null) {
                byte[] b = str.getBytes("GBK");
                if (b.length <= iLen) {
                    return str;
                }
                for (int i = 0; i < iLen; i++) {
                    if (b[i] < 0) {
                        counterOfDoubleByte++;
                    }
                }
                if (counterOfDoubleByte % 2 == 0) {
                    strRet = new String(b, 0, iLen, "GBK") + symbol;
                    return strRet;
                } else {
                    strRet = new String(b, 0, iLen - 1, "GBK") + symbol;
                    return strRet;
                }
            } else {
                return "";
            }
        } catch (Exception ex) {
            return str.substring(0, len);
        } finally {
            strRet = null;
        }
    }
    
    /**
     * 截取字符串　超出的字符用symbol代替 　　
     * 
     * @param len
     *            　字符串长度　长度计量单位为一个GBK汉字　　两个英文字母计算为一个单位长度
     * @param str
     * @param symbol
     * @return12
     */
    public static String getLimitLengthString(String str, int len) {
        return getLimitLengthString(str, len, "...");
    }
    
    
    /**
     * 截取字符，不转码
     * 
     * @param subject
     * @param size
     * @return
     */
    public static String subStrNotEncode(String subject, int size) {
        if (subject.length() > size) {
            subject = subject.substring(0, size);
        }
        return subject;
    }
    
    /**
     * 把string array or list用给定的符号symbol连接成一个字符串
     * 
     * @param array
     * @param symbol
     * @return
     */
    public static String joinString(String[] array, String symbol) {
        String result = "";
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                String temp = array[i];
                if (temp != null && temp.trim().length() > 0)
                    result += (temp + symbol);
            }
            if (result.length() > 1)
                result = result.substring(0, result.length() - 1);
        }
        return result;
    }
    
    /**
     * 取得字符串的实际长度（考虑了汉字的情况）
     * 
     * @param SrcStr
     *            源字符串
     * @return 字符串的实际长度
     */
    public static int getStringLen(String SrcStr) {
        int return_value = 0;
        if (SrcStr != null) {
            char[] theChars = SrcStr.toCharArray();
            for (int i = 0; i < theChars.length; i++) {
                return_value += (theChars[i] <= 255) ? 1 : 2;
            }
        }
        return return_value;
    }
    
    /**
     * 检查联系人信息是否填写，电话，手机，email必须填至少一个，email填了的话检查格式
     * 
     * @param phoneCity
     * @param phoneNumber
     * @param phoneExt
     * @param mobileNumber
     * @param email
     * @return
     */
    public static boolean checkContactInfo(String phoneCity, String phoneNumber, String phoneExt,
            String mobileNumber, String email) {
        String result = (phoneCity == null ? "" : phoneCity.trim())
                + (phoneNumber == null ? "" : phoneNumber.trim())
                + (phoneExt == null ? "" : phoneExt.trim())
                + (mobileNumber == null ? "" : mobileNumber.trim())
                + (email == null ? "" : email.trim());
        if (result.length() < 1)
            return false;
        if (!isEmail(email))
            return false;
        
        return true;
    }
    
    /**
     * 检查数据串中是否包含非法字符集
     * 
     * @param str
     * @return [true]|[false] 包含|不包含
     */
    public static boolean check(String str) {
        String sIllegal = "'\"";
        int len = sIllegal.length();
        if (null == str)
            return false;
        for (int i = 0; i < len; i++) {
            if (str.indexOf(sIllegal.charAt(i)) != -1)
                return true;
        }
        
        return false;
    }
    
    /**
     * getHideEmailPrefix - 隐藏邮件地址前缀。
     * 
     * @param email
     *            - EMail邮箱地址 例如: linwenguo@koubei.com 等等...
     * @return 返回已隐藏前缀邮件地址, 如 *********@koubei.com.
     * @version 1.0 (2006.11.27) Wilson Lin
     */
    public static String getHideEmailPrefix(String email) {
        if (null != email) {
            int index = email.lastIndexOf('@');
            if (index > 0) {
                email = repeat("*", index).concat(email.substring(index));
            }
        }
        return email;
    }
    
    /**
     * repeat - 通过源字符串重复生成N次组成新的字符串。
     * 
     * @param src
     *            - 源字符串 例如: 空格(" "), 星号("*"), "浙江" 等等...
     * @param num
     *            - 重复生成次数
     * @return 返回已生成的重复字符串
     * @version 1.0 (2006.10.10) Wilson Lin
     */
    public static String repeat(String src, int num) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < num; i++)
            s.append(src);
        return s.toString();
    }
    
    /**
     * 根据指定的字符把源字符串分割成一个数组
     * 
     * @param src
     * @return
     */
    public static List<String> parseString2ListByCustomerPattern(String pattern, String src) {
        
        if (src == null)
            return null;
        List<String> list = new ArrayList<String>();
        String[] result = src.split(pattern);
        for (int i = 0; i < result.length; i++) {
            list.add(result[i]);
        }
        return list;
    }
    
    /**
     * 根据指定的字符把源字符串分割成一个数组
     * 
     * @param src
     * @return
     */
    public static List<String> parseString2ListByPattern(String src) {
        String pattern = "，|,|、|。";
        return parseString2ListByCustomerPattern(pattern, src);
    }
    
    /**
     * 格式化一个float
     * 
     * @param format
     *            要格式化成的格式 such as #.00, #.#
     */
    
    public static String formatFloat(float f, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(f);
    }
    
    /**
     * 判断是否是空字符串 null和"" 都返回 true
     * 
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        if (s != null && !s.equals("")) {
            return false;
        }
        return true;
    }
    
    /**
     * 自定义的分隔字符串函数 例如: 1,2,3 =>[1,2,3] 3个元素 ,2,3=>[,2,3] 3个元素 ,2,3,=>[,2,3,]
     * 4个元素 ,,,=>[,,,] 4个元素
     * 5.22算法修改，为提高速度不用正则表达式 两个间隔符,,返回""元素
     * 
     * @param split
     *            分割字符 默认,
     * @param src
     *            输入字符串
     * @return 分隔后的list
     */
    public static List<String> splitToList(String split, String src) {
        // 默认,
        String sp = ",";
        if (split != null && split.length() == 1) {
            sp = split;
        }
        List<String> r = new ArrayList<String>();
        int lastIndex = -1;
        int index = src.indexOf(sp);
        if (-1 == index && src != null) {
            r.add(src);
            return r;
        }
        while (index >= 0) {
            if (index > lastIndex) {
                r.add(src.substring(lastIndex + 1, index));
            } else {
                r.add("");
            }
            
            lastIndex = index;
            index = src.indexOf(sp, index + 1);
            if (index == -1) {
                r.add(src.substring(lastIndex + 1, src.length()));
            }
        }
        return r;
    }
    
    /**
     * 把 名=值 参数表转换成字符串 (a=1,b=2 =>a=1&b=2)
     * 
     * @param map
     * @return
     */
    public static String linkedHashMapToString(LinkedHashMap<String, String> map) {
        if (map != null && map.size() > 0) {
            String result = "";
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                String name = (String) it.next();
                String value = (String) map.get(name);
                result += (result.equals("")) ? "" : "&";
                result += String.format("%s=%s", name, value);
            }
            return result;
        }
        return null;
    }
    
    /**
     * 解析字符串返回 名称=值的参数表 (a=1&b=2 => a=1,b=2)
     * 
     * @see test.koubei.util.StringUtilTest#testParseStr()
     * @param str
     * @return
     */
    @SuppressWarnings("unchecked")
    public static LinkedHashMap<String, String> toLinkedHashMap(String str) {
        if (str != null && !str.equals("") && str.indexOf("=") > 0) {
            LinkedHashMap result = new LinkedHashMap();
            
            String name = null;
            String value = null;
            int i = 0;
            while (i < str.length()) {
                char c = str.charAt(i);
                switch (c) {
                    case 61: // =
                        value = "";
                        break;
                    case 38: // &
                        if (name != null && value != null && !name.equals("")) {
                            result.put(name, value);
                        }
                        name = null;
                        value = null;
                        break;
                    default:
                        if (value != null) {
                            value = (value != null) ? (value + c) : "" + c;
                        } else {
                            name = (name != null) ? (name + c) : "" + c;
                        }
                }
                i++;
                
            }
            
            if (name != null && value != null && !name.equals("")) {
                result.put(name, value);
            }
            
            return result;
            
        }
        return null;
    }
    
    /**
     * 根据输入的多个解释和下标返回一个值
     * 
     * @param captions
     *            例如:"无,爱干净,一般,比较乱"
     * @param index
     *            1
     * @return 一般
     */
    public static String getCaption(String captions, int index) {
        if (index > 0 && captions != null && !captions.equals("")) {
            String[] ss = captions.split(",");
            if (ss != null && ss.length > 0 && index < ss.length) {
                return ss[index];
            }
        }
        return null;
    }
    
    /**
     * 数字转字符串,如果num<=0 则输出"";
     * 
     * @param num
     * @return
     */
    public static String numberToString(Object num) {
        if (num == null) {
            return null;
        } else if (num instanceof Integer && (Integer) num > 0) {
            return Integer.toString((Integer) num);
        } else if (num instanceof Long && (Long) num > 0) {
            return Long.toString((Long) num);
        } else if (num instanceof Float && (Float) num > 0) {
            return Float.toString((Float) num);
        } else if (num instanceof Double && (Double) num > 0) {
            return Double.toString((Double) num);
        } else {
            return "";
        }
    }
    
    /**
     * 货币转字符串
     * 
     * @param money
     * @param style
     *            样式 [default]要格式化成的格式 such as #.00, #.#
     * @return
     */
    
    public static String moneyToString(Object money, String style) {
        if (money != null && style != null && (money instanceof Double || money instanceof Float)) {
            Double num = (Double) money;
            
            if (style.equalsIgnoreCase("default")) {
                // 缺省样式 0 不输出 ,如果没有输出小数位则不输出.0
                if (num == 0) {
                    // 不输出0
                    return "";
                } else if ((num * 10 % 10) == 0) {
                    // 没有小数
                    return Integer.toString((int) num.intValue());
                } else {
                    // 有小数
                    return num.toString();
                }
                
            } else {
                DecimalFormat df = new DecimalFormat(style);
                return df.format(num);
            }
        }
        return null;
    }
    
    /**
     * 在sou中是否存在finds 如果指定的finds字符串有一个在sou中找到,返回true;
     * 
     * @param sou
     * @param find
     * @return
     */
    public static boolean strPos(String sou, String... finds) {
        if (sou != null && finds != null && finds.length > 0) {
            for (int i = 0; i < finds.length; i++) {
                if (sou.indexOf(finds[i]) > -1)
                    return true;
            }
        }
        return false;
    }
    
    public static boolean strPos(String sou, List<String> finds) {
        if (sou != null && finds != null && finds.size() > 0) {
            for (String s : finds) {
                if (sou.indexOf(s) > -1)
                    return true;
            }
        }
        return false;
    }
    
    public static boolean strPos(String sou, String finds) {
        List<String> t = splitToList(",", finds);
        return strPos(sou, t);
    }
    
    /**
     * 判断两个字符串是否相等 如果都为null则判断为相等,一个为null另一个not null则判断不相等 否则如果s1=s2则相等
     * 
     * @param s1
     * @param s2
     * @return
     */
    public static boolean equals(String s1, String s2) {
        if (StringUtil.isEmpty(s1) && StringUtil.isEmpty(s2)) {
            return true;
        } else if (!StringUtil.isEmpty(s1) && !StringUtil.isEmpty(s2)) {
            return s1.equals(s2);
        }
        return false;
    }
    
    public static int toInt(String s) {
        if (s != null && !"".equals(s.trim())) {
            try {
                return Integer.parseInt(s);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }
    
    public static double toDouble(String s) {
        if (s != null && !"".equals(s.trim())) {
            return Double.parseDouble(s);
        }
        return 0;
    }
    
    public static boolean isPhone(String phone) {
        if (phone == null && "".equals(phone)) {
            return false;
        }
        String[] strPhone = phone.split("-");
        try {
            for (int i = 0; i < strPhone.length; i++) {
                Long.parseLong(strPhone[i]);
            }
            
        } catch (Exception e) {
            return false;
        }
        return true;
        
    }
    
    /**
     * 把xml 转为object
     * 
     * @param xml
     * @return
     */
    public static Object xmlToObject(String xml) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes("UTF8"));
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(in));
            return decoder.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    public static long toLong(String s) {
        try {
            if (s != null && !"".equals(s.trim()))
                return Long.parseLong(s);
        } catch (Exception exception) {
        }
        return 0L;
    }
    
    public static String simpleEncrypt(String str) {
        if (str != null && str.length() > 0) {
            // str = str.replaceAll("0","a");
            str = str.replaceAll("1", "b");
            // str = str.replaceAll("2","c");
            str = str.replaceAll("3", "d");
            // str = str.replaceAll("4","e");
            str = str.replaceAll("5", "f");
            str = str.replaceAll("6", "g");
            str = str.replaceAll("7", "h");
            str = str.replaceAll("8", "i");
            str = str.replaceAll("9", "j");
        }
        return str;
        
    }
    
    /**
     * 过滤用户输入的URL地址（防治用户广告） 目前只针对以http或www开头的URL地址
     * 本方法调用的正则表达式，不建议用在对性能严格的地方例如:循环及list页面等
     * 
     * @param str
     *            需要处理的字符串
     * @return 返回处理后的字符串
     */
    public static String removeURL(String str) {
        if (str != null)
            str = str.toLowerCase().replaceAll("(http|www|com|cn|org|\\.)+", "");
        return str;
    }
    
    
    /**
     * 检查字符串是否属于手机号码
     * 
     * @param str
     * @return boolean
     */
    public static boolean isMobile(String str) {
        if (str == null || str.equals(""))
            return false;
        if (str.length() != 11 || !isNumeric(str))
            return false;
        if (!str.substring(0, 2).equals("13") && !str.substring(0, 2).equals("15")
                && !str.substring(0, 2).equals("18"))
            return false;
        return true;
    }
    
    /**
     * Wap页面的非法字符检查
     * 
     * @param str
     * @return
     */
    public static String replaceWapStr(String str) {
        if (str != null) {
            str = str.replaceAll("<span class=\"keyword\">", "");
            str = str.replaceAll("</span>", "");
            str = str.replaceAll("<strong class=\"keyword\">", "");
            str = str.replaceAll("<strong>", "");
            str = str.replaceAll("</strong>", "");
            
            str = str.replace('$', '＄');
            
            str = str.replaceAll("&", "＆");
            str = str.replace('&', '＆');
            
            str = str.replace('<', '＜');
            
            str = str.replace('>', '＞');
            
        }
        return str;
    }
    
    /**
     * 字符串转float 如果异常返回0.00
     * 
     * @param s
     *            输入的字符串
     * @return 转换后的float
     */
    public static Float toFloat(String s) {
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            return new Float(0);
        }
    }
    
    /**
     * 页面中去除字符串中的空格、回车、换行符、制表符
     * 
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        }
        return str;
    }
    
    /**
     * 全角生成半角
     * 
     * @param str
     * @return
     */
    public static String Q2B(String QJstr) {
        String outStr = "";
        String Tstr = "";
        byte[] b = null;
        for (int i = 0; i < QJstr.length(); i++) {
            try {
                Tstr = QJstr.substring(i, i + 1);
                b = Tstr.getBytes("unicode");
            } catch (java.io.UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (b[3] == -1) {
                b[2] = (byte) (b[2] + 32);
                b[3] = 0;
                try {
                    outStr = outStr + new String(b, "unicode");
                } catch (java.io.UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
            } else {
                outStr = outStr + Tstr;
            }
        }
        return outStr;
    }
    
    /**
     * 转换编码
     * 
     * @param s
     *            源字符串
     * @param fencode
     *            源编码格式
     * @param bencode
     *            目标编码格式
     * @return 目标编码
     */
    public static String changCoding(String s, String fencode, String bencode) {
        try {
            String str = new String(s.getBytes(fencode), bencode);
            return str;
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }
    
    /**
     * 除去html标签
     * 
     * @param str
     * @return
     */
    public static String removeHTMLLableExe(String str) {
        str = stringReplace(str, ">\\s*<", "><");
        str = stringReplace(str, " ", " ");// 替换空格
        str = stringReplace(str, "<br ?/?>", "\n");// 去<br><br />
        str = stringReplace(str, "<([^<>]+)>", "");// 去掉<>内的字符
        str = stringReplace(str, "\\s\\s\\s*", " ");// 将多个空白变成一个空格
        str = stringReplace(str, "^\\s*", "");// 去掉头的空白
        str = stringReplace(str, "\\s*$", "");// 去掉尾的空白
        str = stringReplace(str, " +", " ");
        return str;
    }
    
    /**
     * 除去html标签
     * 
     * @param str
     *            源字符串
     * @return 目标字符串
     */
    public static String removeHTMLLable(String str) {
        str = stringReplace(str, "\\s", "");// 去掉页面上看不到的字符
        str = stringReplace(str, "<br ?/?>", "\n");// 去<br><br />
        str = stringReplace(str, "<([^<>]+)>", "");// 去掉<>内的字符
        str = stringReplace(str, " ", " ");// 替换空格
        str = stringReplace(str, "&(\\S)(\\S?)(\\S?)(\\S?);", "");// 去<br><br />
        return str;
    }
    
    /**
     * 去掉HTML标签之外的字符串
     * 
     * @param str
     *            源字符串
     * @return 目标字符串
     */
    public static String removeOutHTMLLable(String str) {
        str = stringReplace(str, ">([^<>]+)<", "><");
        str = stringReplace(str, "^([^<>]+)<", "<");
        str = stringReplace(str, ">([^<>]+)$", ">");
        return str;
    }
    
    /**
     * 字符串替换
     * 
     * @param str
     *            源字符串
     * @param sr
     *            正则表达式样式
     * @param sd
     *            替换文本
     * @return 结果串
     */
    public static String stringReplace(String str, String sr, String sd) {
        String regEx = sr;
        Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        str = m.replaceAll(sd);
        return str;
    }
    
    /**
     * 将html的省略写法替换成非省略写法
     * 
     * @param str
     *            html字符串
     * @param pt
     *            标签如table
     * @return 结果串
     */
    public static String fomateToFullForm(String str, String pt) {
        String regEx = "<" + pt + "\\s+([\\S&&[^<>]]*)/>";
        Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        String[] sa = null;
        String sf = "";
        String sf2 = "";
        String sf3 = "";
        for (; m.find();) {
            sa = p.split(str);
            if (sa == null) {
                break;
            }
            sf = str.substring(sa[0].length(), str.indexOf("/>", sa[0].length()));
            sf2 = sf + "></" + pt + ">";
            sf3 = str.substring(sa[0].length() + sf.length() + 2);
            str = sa[0] + sf2 + sf3;
            sa = null;
        }
        return str;
    }
    
    /**
     * 得到字符串的子串位置序列
     * 
     * @param str
     *            字符串
     * @param sub
     *            子串
     * @param b
     *            true子串前端,false子串后端
     * @return 字符串的子串位置序列
     */
    public static int[] getSubStringPos(String str, String sub, boolean b) {
        // int[] i = new int[(new Integer((str.length()-stringReplace( str , sub
        // , "" ).length())/sub.length())).intValue()] ;
        String[] sp = null;
        int l = sub.length();
        sp = splitString(str, sub);
        if (sp == null) {
            return null;
        }
        int[] ip = new int[sp.length - 1];
        for (int i = 0; i < sp.length - 1; i++) {
            ip[i] = sp[i].length() + l;
            if (i != 0) {
                ip[i] += ip[i - 1];
            }
        }
        if (b) {
            for (int j = 0; j < ip.length; j++) {
                ip[j] = ip[j] - l;
            }
        }
        return ip;
    }
    
    /**
     * 根据正则表达式分割字符串
     * 
     * @param str
     *            源字符串
     * @param ms
     *            正则表达式
     * @return 目标字符串组
     */
    public static String[] splitString(String str, String ms) {
        String regEx = ms;
        Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        String[] sp = p.split(str);
        return sp;
    }
    
    /**
     * *************************************************************************
     * 根据正则表达式提取字符串,相同的字符串只返回一个
     * 
     * @param str
     *            源字符串
     * @param pattern
     *            正则表达式
     * @return 目标字符串数据组
     */
    
    // ★传入一个字符串，把符合pattern格式的字符串放入字符串数组
    // java.util.regex是一个用正则表达式所订制的模式来对字符串进行匹配工作的类库包
    public static String[] getStringArrayByPattern(String str, String pattern) {
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(str);
        // 范型
        Set<String> result = new HashSet<String>();// 目的是：相同的字符串只返回一个。。。 不重复元素
        // boolean find() 尝试在目标字符串里查找下一个匹配子串。
        while (matcher.find()) {
            for (int i = 0; i < matcher.groupCount(); i++) { // int groupCount()
                                                             // 返回当前查找所获得的匹配组的数量。
                // System.out.println(matcher.group(i));
                result.add(matcher.group(i));
                
            }
        }
        String[] resultStr = null;
        if (result.size() > 0) {
            resultStr = new String[result.size()];
            return result.toArray(resultStr);// 将Set result转化为String[] resultStr
        }
        return resultStr;
        
    }
    
    /**
     * 得到第一个b,e之间的字符串,并返回e后的子串
     * 
     * @param s
     *            源字符串
     * @param b
     *            标志开始
     * @param e
     *            标志结束
     * @return b,e之间的字符串
     */
    
    /*
     * String aaa="abcdefghijklmn"; String[] bbb=StringProcessor.midString(aaa,
     * "b","l"); System.out.println("bbb[0]:"+bbb[0]);//cdefghijk
     * System.out.println("bbb[1]:"+bbb[1]);//lmn
     * ★这个方法是得到第二个参数和第三个参数之间的字符串,赋给元素0;然后把元素0代表的字符串之后的,赋给元素1
     */
    
    /*
     * String aaa="abcdefgllhijklmn5465"; String[]
     * bbb=StringProcessor.midString(aaa, "b","l"); //ab cdefg llhijklmn5465 //
     * 元素0 元素1
     */
    public static String[] midString(String s, String b, String e) {
        int i = s.indexOf(b) + b.length();
        int j = s.indexOf(e, i);
        String[] sa = new String[2];
        if (i < b.length() || j < i + 1 || i > j) {
            sa[1] = s;
            sa[0] = null;
            return sa;
        } else {
            sa[0] = s.substring(i, j);
            sa[1] = s.substring(j);
            return sa;
        }
    }
    
    /**
     * 带有前一次替代序列的正则表达式替代
     * 
     * @param s
     * @param pf
     * @param pb
     * @param start
     * @return
     */
    public static String stringReplace(String s, String pf, String pb, int start) {
        Pattern pattern_hand = Pattern.compile(pf);
        Matcher matcher_hand = pattern_hand.matcher(s);
        int gc = matcher_hand.groupCount();
        int pos = start;
        String sf1 = "";
        String sf2 = "";
        String sf3 = "";
        int if1 = 0;
        String strr = "";
        while (matcher_hand.find(pos)) {
            sf1 = matcher_hand.group();
            if1 = s.indexOf(sf1, pos);
            if (if1 >= pos) {
                strr += s.substring(pos, if1);
                pos = if1 + sf1.length();
                sf2 = pb;
                for (int i = 1; i <= gc; i++) {
                    sf3 = "\\" + i;
                    sf2 = replaceAll(sf2, sf3, matcher_hand.group(i));
                }
                strr += sf2;
            } else {
                return s;
            }
        }
        strr = s.substring(0, start) + strr;
        return strr;
    }
    
    /**
     * 判断是否与给定字符串样式匹配
     * 
     * @param str
     *            字符串
     * @param pattern
     *            正则表达式样式
     * @return 是否匹配是true,否false
     */
    public static boolean isMatch(String str, String pattern) {
        Pattern pattern_hand = Pattern.compile(pattern);
        Matcher matcher_hand = pattern_hand.matcher(str);
        boolean b = matcher_hand.matches();
        return b;
    }
    
    /**
     * 截取字符串
     * 
     * @param s
     *            源字符串
     * @param jmp
     *            跳过jmp
     * @param sb
     *            取在sb
     * @param se
     *            于se
     * @return 之间的字符串
     */
    public static String subStringExe(String s, String jmp, String sb, String se) {
        if (isEmpty(s)) {
            return "";
        }
        int i = s.indexOf(jmp);
        if (i >= 0 && i < s.length()) {
            s = s.substring(i + 1);
        }
        i = s.indexOf(sb);
        if (i >= 0 && i < s.length()) {
            s = s.substring(i + 1);
        }
        if (se == "") {
            return s;
        } else {
            i = s.indexOf(se);
            if (i >= 0 && i < s.length()) {
                s = s.substring(i + 1);
            }
            return s;
        }
    }
    
    /**
     * 用要通过URL传输的内容进行编码
     * 
     * @param 源字符串
     * @return 经过编码的内容
     */
    public static String URLEncode(String src) {
        String return_value = "";
        try {
            if (src != null) {
                return_value = URLEncoder.encode(src, "GBK");
                
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return_value = src;
        }
        
        return return_value;
    }
    
    /**
     * 换成GBK的字符串
     * 
     * @param str
     * @return
     */
    public static String getGBK(String str) {
        
        return transfer(str);
    }
    
    public static String transfer(String str) {
        Pattern p = Pattern.compile("&#\\d+;");
        Matcher m = p.matcher(str);
        while (m.find()) {
            String old = m.group();
            str = str.replaceAll(old, getChar(old));
        }
        return str;
    }
    
    public static String getChar(String str) {
        String dest = str.substring(2, str.length() - 1);
        char ch = (char) Integer.parseInt(dest);
        return "" + ch;
    }
    
    /**
     * 首页中切割字符串.
     * 
     * @date 2007-09-17
     * @param str
     * @return
     */
    public static String subYhooString(String subject, int size) {
        subject = subject.substring(1, size);
        return subject;
    }
    
    public static String subYhooStringDot(String subject, int size) {
        subject = subject.substring(1, size) + "...";
        return subject;
    }
    
    /**
     * 泛型方法(通用)，把list转换成以“,”相隔的字符串 调用时注意类型初始化（申明类型） 如：List<Integer> intList =
     * new ArrayList<Integer>(); 调用方法：StringUtil.listTtoString(intList);
     * 效率：list中4条信息，1000000次调用时间为850ms左右
     * 
     * @param <T>
     *            泛型
     * @param list
     *            list列表
     * @return 以“,”相隔的字符串
     */
    public static <T> String listTtoString(List<T> list) {
        if (list == null || list.size() < 1)
            return "";
        Iterator<T> i = list.iterator();
        if (!i.hasNext())
            return "";
        StringBuilder sb = new StringBuilder();
        for (;;) {
            T e = i.next();
            sb.append(e);
            if (!i.hasNext())
                return sb.toString();
            sb.append(",");
        }
    }
    
    /**
     * 把整形数组转换成以“,”相隔的字符串
     * 
     * @param a
     *            数组a
     * @return 以“,”相隔的字符串
     */
    public static String intArraytoString(int[] a) {
        if (a == null)
            return "";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "";
        StringBuilder b = new StringBuilder();
        for (int i = 0;; i++) {
            b.append(a[i]);
            if (i == iMax)
                return b.toString();
            b.append(",");
        }
    }
    
    /**
     * 判断文字内容重复
     * 
     * @Date 2008-04-17
     */
    public static boolean isContentRepeat(String content) {
        int similarNum = 0;
        int forNum = 0;
        int subNum = 0;
        int thousandNum = 0;
        String startStr = "";
        String nextStr = "";
        boolean result = false;
        float endNum = (float) 0.0;
        if (content != null && content.length() > 0) {
            if (content.length() % 1000 > 0)
                thousandNum = (int) Math.floor(content.length() / 1000) + 1;
            else
                thousandNum = (int) Math.floor(content.length() / 1000);
            if (thousandNum < 3)
                subNum = 100 * thousandNum;
            else if (thousandNum < 6)
                subNum = 200 * thousandNum;
            else if (thousandNum < 9)
                subNum = 300 * thousandNum;
            else
                subNum = 3000;
            for (int j = 1; j < subNum; j++) {
                if (content.length() % j > 0)
                    forNum = (int) Math.floor(content.length() / j) + 1;
                else
                    forNum = (int) Math.floor(content.length() / j);
                if (result || j >= content.length())
                    break;
                else {
                    for (int m = 0; m < forNum; m++) {
                        if (m * j > content.length() || (m + 1) * j > content.length()
                                || (m + 2) * j > content.length())
                            break;
                        startStr = content.substring(m * j, (m + 1) * j);
                        nextStr = content.substring((m + 1) * j, (m + 2) * j);
                        if (startStr.equals(nextStr)) {
                            similarNum = similarNum + 1;
                            endNum = (float) similarNum / forNum;
                            if (endNum > 0.4) {
                                result = true;
                                break;
                            }
                        } else
                            similarNum = 0;
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * Ascii转为Char
     * 
     * @param asc
     * @return
     */
    public static String AsciiToChar(int asc) {
        String TempStr = "A";
        char tempchar = (char) asc;
        TempStr = String.valueOf(tempchar);
        return TempStr;
    }
    
    /**
     * 判断是否是空字符串 null和"" null返回result,否则返回字符串
     * 
     * @param s
     * @return
     */
    public static String isEmpty(String s, String result) {
        if (s != null && !s.equals("")) {
            return s;
        }
        return result;
    }
    /**
     * 移除html标签
     * 
     * @param htmlstr
     * @return
     */
    public static String removeHtmlTag(String htmlstr) {
        Pattern pat = Pattern.compile("\\s*<.*?>\\s*", Pattern.DOTALL | Pattern.MULTILINE
                | Pattern.CASE_INSENSITIVE); // \\s?[s|Sc|Cr|Ri|Ip|Pt|T]
        Matcher m = pat.matcher(htmlstr);
        String rs = m.replaceAll("");
        rs = rs.replaceAll(" ", " ");
        rs = rs.replaceAll("<", "<");
        rs = rs.replaceAll(">", ">");
        return rs;
    }
    
    /**
     * 取从指定搜索项开始的字符串，返回的值不包含搜索项
     * 
     * @param captions
     *            例如:"www.koubei.com"
     * @param regex
     *            分隔符，例如"."
     * @return 结果字符串，如：koubei.com，如未找到返回空串
     */
    public static String getStrAfterRegex(String captions, String regex) {
        if (!isEmpty(captions) && !isEmpty(regex)) {
            int pos = captions.indexOf(regex);
            if (pos != -1 && pos < captions.length() - 1) {
                return captions.substring(pos + 1);
            }
        }
        return "";
    }
    
    /**
     * 把字节码转换成16进制
     */
    public static String byte2hex(byte bytes[]) {
        StringBuffer retString = new StringBuffer();
        for (int i = 0; i < bytes.length; ++i) {
            retString.append(Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1).toUpperCase());
        }
        return retString.toString();
    }
    
    /**
     * 把16进制转换成字节码
     * 
     * @param hex
     * @return
     */
    public static byte[] hex2byte(String hex) {
        byte[] bts = new byte[hex.length() / 2];
        for (int i = 0; i < bts.length; i++) {
            bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bts;
    }
    
    /**
     * 转换数字为固定长度的字符串
     * 
     * @param length
     *            希望返回的字符串长度
     * @param data
     *            传入的数值
     * @return
     */
    public static String getStringByInt(int length, String data) {
        String s_data = "";
        int datalength = data.length();
        if (length > 0 && length >= datalength) {
            for (int i = 0; i < length - datalength; i++) {
                s_data += "0";
            }
            s_data += data;
        }
        
        return s_data;
    }
    
    /**
     * 判断是否位数字,并可为空
     * 
     * @param src
     * @return
     */
    public static boolean isNumericAndCanNull(String src) {
        Pattern numericPattern = Pattern.compile("^[0-9]+$");
        if (src == null || src.equals(""))
            return true;
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = numericPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }
    
    /**
     * @param src
     * @return
     */
    public static boolean isFloatAndCanNull(String src) {
        Pattern numericPattern = Pattern.compile("^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$");
        if (src == null || src.equals(""))
            return true;
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = numericPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }
    
    public static boolean isNotEmpty(String str) {
        if (str != null && !str.equals(""))
            return true;
        else
            return false;
    }
    
    public static boolean isDate(String date) {
        String regEx = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(date);
        boolean result = m.find();
        return result;
    }
    
    public static boolean isFormatDate(String date, String regEx) {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(date);
        boolean result = m.find();
        return result;
    }
    
    /**
     * 根据指定整型list 组装成为一个字符串
     * 
     * @param src
     * @return
     */
    public static String listToString(List<Integer> list) {
        String str = "";
        if (list != null && list.size() > 0) {
            for (int id : list) {
                str = str + id + ",";
            }
            if (!"".equals(str) && str.length() > 0)
                str = str.substring(0, str.length() - 1);
        }
        return str;
    }
    
    /**
     * 页面的非法字符检查
     * 
     * @param str
     * @return
     */
    public static String replaceStr(String str) {
        if (str != null && str.length() > 0) {
            str = str.replaceAll("~", ",");
            str = str.replaceAll(" ", ",");
            str = str.replaceAll("　", ",");
            str = str.replaceAll(" ", ",");
            str = str.replaceAll("`", ",");
            str = str.replaceAll("!", ",");
            str = str.replaceAll("@", ",");
            str = str.replaceAll("#", ",");
            str = str.replaceAll("\\$", ",");
            str = str.replaceAll("%", ",");
            str = str.replaceAll("\\^", ",");
            str = str.replaceAll("&", ",");
            str = str.replaceAll("\\*", ",");
            str = str.replaceAll("\\(", ",");
            str = str.replaceAll("\\)", ",");
            str = str.replaceAll("-", ",");
            str = str.replaceAll("_", ",");
            str = str.replaceAll("=", ",");
            str = str.replaceAll("\\+", ",");
            str = str.replaceAll("\\{", ",");
            str = str.replaceAll("\\[", ",");
            str = str.replaceAll("\\}", ",");
            str = str.replaceAll("\\]", ",");
            str = str.replaceAll("\\|", ",");
            str = str.replaceAll("\\\\", ",");
            str = str.replaceAll(";", ",");
            str = str.replaceAll(":", ",");
            str = str.replaceAll("'", ",");
            str = str.replaceAll("\\\"", ",");
            str = str.replaceAll("<", ",");
            str = str.replaceAll(">", ",");
            str = str.replaceAll("\\.", ",");
            str = str.replaceAll("\\?", ",");
            str = str.replaceAll("/", ",");
            str = str.replaceAll("～", ",");
            str = str.replaceAll("`", ",");
            str = str.replaceAll("！", ",");
            str = str.replaceAll("＠", ",");
            str = str.replaceAll("＃", ",");
            str = str.replaceAll("＄", ",");
            str = str.replaceAll("％", ",");
            str = str.replaceAll("︿", ",");
            str = str.replaceAll("＆", ",");
            str = str.replaceAll("×", ",");
            str = str.replaceAll("（", ",");
            str = str.replaceAll("）", ",");
            str = str.replaceAll("－", ",");
            str = str.replaceAll("＿", ",");
            str = str.replaceAll("＋", ",");
            str = str.replaceAll("＝", ",");
            str = str.replaceAll("｛", ",");
            str = str.replaceAll("［", ",");
            str = str.replaceAll("｝", ",");
            str = str.replaceAll("］", ",");
            str = str.replaceAll("｜", ",");
            str = str.replaceAll("＼", ",");
            str = str.replaceAll("：", ",");
            str = str.replaceAll("；", ",");
            str = str.replaceAll("＂", ",");
            str = str.replaceAll("＇", ",");
            str = str.replaceAll("＜", ",");
            str = str.replaceAll("，", ",");
            str = str.replaceAll("＞", ",");
            str = str.replaceAll("．", ",");
            str = str.replaceAll("？", ",");
            str = str.replaceAll("／", ",");
            str = str.replaceAll("·", ",");
            str = str.replaceAll("￥", ",");
            str = str.replaceAll("……", ",");
            str = str.replaceAll("（", ",");
            str = str.replaceAll("）", ",");
            str = str.replaceAll("——", ",");
            str = str.replaceAll("-", ",");
            str = str.replaceAll("【", ",");
            str = str.replaceAll("】", ",");
            str = str.replaceAll("、", ",");
            str = str.replaceAll("”", ",");
            str = str.replaceAll("’", ",");
            str = str.replaceAll("《", ",");
            str = str.replaceAll("》", ",");
            str = str.replaceAll("“", ",");
            str = str.replaceAll("。", ",");
        }
        return str;
    }
    
    /**
     * 全角字符变半角字符
     * 
     * @param str
     * @return
     */
    public static String full2Half(String str) {
        if (str == null || "".equals(str))
            return "";
        StringBuffer sb = new StringBuffer();
        
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            
            if (c >= 65281 && c < 65373)
                sb.append((char) (c - 65248));
            else
                sb.append(str.charAt(i));
        }
        
        return sb.toString();
        
    }
    
    /**
     * 全角括号转为半角
     * 
     * @param str
     * @return
     */
    public static String replaceBracketStr(String str) {
        if (str != null && str.length() > 0) {
            str = str.replaceAll("（", "(");
            str = str.replaceAll("）", ")");
        }
        return str;
    }
    
    /**
     * 分割字符，从开始到第一个split字符串为止
     * 
     * @param src
     *            源字符串
     * @param split
     *            截止字符串
     * @return
     */
    public static String subStr(String src, String split) {
        if (!isEmpty(src)) {
            int index = src.indexOf(split);
            if (index >= 0) {
                return src.substring(0, index);
            }
        }
        return src;
    }
    
    /**
     * 取url里的keyword（可选择参数）参数，用于整站搜索整合
     * 
     * @param params
     * @param qString
     * @return
     */
    public static String getKeyWord(String params, String qString) {
        String keyWord = "";
        if (qString != null) {
            String param = params + "=";
            int i = qString.indexOf(param);
            if (i != -1) {
                int j = qString.indexOf("&", i + param.length());
                if (j > 0) {
                    keyWord = qString.substring(i + param.length(), j);
                }
            }
        }
        return keyWord;
    }
    
    /**
     * 解析字符串返回map键值对(例：a=1&b=2 => a=1,b=2)
     * 
     * @param query
     *            源参数字符串
     * @param split1
     *            键值对之间的分隔符（例：&）
     * @param split2
     *            key与value之间的分隔符（例：=）
     * @param dupLink
     *            重复参数名的参数值之间的连接符，连接后的字符串作为该参数的参数值，可为null
     *            null：不允许重复参数名出现，则靠后的参数值会覆盖掉靠前的参数值。
     * @return map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseQuery(String query, char split1, char split2,
            String dupLink) {
        if (!isEmpty(query) && query.indexOf(split2) > 0) {
            Map<String, String> result = new HashMap();
            
            String name = null;
            String value = null;
            String tempValue = "";
            int len = query.length();
            for (int i = 0; i < len; i++) {
                char c = query.charAt(i);
                if (c == split2) {
                    value = "";
                } else if (c == split1) {
                    if (!isEmpty(name) && value != null) {
                        if (dupLink != null) {
                            tempValue = result.get(name);
                            if (tempValue != null) {
                                value += dupLink + tempValue;
                            }
                        }
                        result.put(name, value);
                    }
                    name = null;
                    value = null;
                } else if (value != null) {
                    value += c;
                } else {
                    name = (name != null) ? (name + c) : "" + c;
                }
            }
            
            if (!isEmpty(name) && value != null) {
                if (dupLink != null) {
                    tempValue = result.get(name);
                    if (tempValue != null) {
                        value += dupLink + tempValue;
                    }
                }
                result.put(name, value);
            }
            
            return result;
        }
        return null;
    }
    
    /**
     * 将list 用传入的分隔符组装为String
     * 
     * @param list
     * @param slipStr
     * @return String
     */
    @SuppressWarnings("unchecked")
    public static String listToStringSlipStr(List list, String slipStr) {
        StringBuffer returnStr = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                returnStr.append(list.get(i)).append(slipStr);
            }
        }
        if (returnStr.toString().length() > 0)
            return returnStr.toString().substring(0, returnStr.toString().lastIndexOf(slipStr));
        else
            return "";
    }
    
    /**
     * 获取从start开始用*替换len个长度后的字符串
     * 
     * @param str
     *            要替换的字符串
     * @param start
     *            开始位置
     * @param len
     *            长度
     * @return 替换后的字符串
     */
    public static String getMaskStr(String str, int start, int len) {
        if (StringUtil.isEmpty(str)) {
            return str;
        }
        if (str.length() < start) {
            return str;
        }
        
        // 获取*之前的字符串
        String ret = str.substring(0, start);
        
        // 获取最多能打的*个数
        int strLen = str.length();
        if (strLen < start + len) {
            len = strLen - start;
        }
        
        // 替换成*
        for (int i = 0; i < len; i++) {
            ret += "*";
        }
        
        // 加上*之后的字符串
        if (strLen > start + len) {
            ret += str.substring(start + len);
        }
        
        return ret;
    }
    
    /**
     * 根据传入的分割符号,把传入的字符串分割为List字符串
     * 
     * @param slipStr
     *            分隔的字符串
     * @param src
     *            字符串
     * @return 列表
     */
    public static List<String> stringToStringListBySlipStr(String slipStr, String src) {
        
        if (src == null)
            return null;
        List<String> list = new ArrayList<String>();
        String[] result = src.split(slipStr);
        for (int i = 0; i < result.length; i++) {
            list.add(result[i]);
        }
        return list;
    }
    
    /**
     * 截取字符串
     * 
     * @param str
     *            原始字符串
     * @param len
     *            要截取的长度
     * @param tail
     *            结束加上的后缀
     * @return 截取后的字符串
     */
    public static String getHtmlSubString(String str, int len, String tail) {
        if (str == null || str.length() <= len) {
            return str;
        }
        int length = str.length();
        char c = ' ';
        String tag = null;
        String name = null;
        int size = 0;
        String result = "";
        boolean isTag = false;
        List<String> tags = new ArrayList<String>();
        int i = 0;
        for (int end = 0, spanEnd = 0; i < length && len > 0; i++) {
            c = str.charAt(i);
            if (c == '<') {
                end = str.indexOf('>', i);
            }
            
            if (end > 0) {
                // 截取标签
                tag = str.substring(i, end + 1);
                int n = tag.length();
                if (tag.endsWith("/>")) {
                    isTag = true;
                } else if (tag.startsWith("</")) { // 结束符
                    name = tag.substring(2, end - i);
                    size = tags.size() - 1;
                    // 堆栈取出html开始标签
                    if (size >= 0 && name.equals(tags.get(size))) {
                        isTag = true;
                        tags.remove(size);
                    }
                } else { // 开始符
                    spanEnd = tag.indexOf(' ', 0);
                    spanEnd = spanEnd > 0 ? spanEnd : n;
                    name = tag.substring(1, spanEnd);
                    if (name.trim().length() > 0) {
                        // 如果有结束符则为html标签
                        spanEnd = str.indexOf("</" + name + ">", end);
                        if (spanEnd > 0) {
                            isTag = true;
                            tags.add(name);
                        }
                    }
                }
                // 非html标签字符
                if (!isTag) {
                    if (n >= len) {
                        result += tag.substring(0, len);
                        break;
                    } else {
                        len -= n;
                    }
                }
                
                result += tag;
                isTag = false;
                i = end;
                end = 0;
            } else { // 非html标签字符
                len--;
                result += c;
            }
        }
        // 添加未结束的html标签
        for (String endTag : tags) {
            result += "</" + endTag + ">";
        }
        if (i < length) {
            result += tail;
        }
        return result;
    }
    /**  
     *		 判断是否乱码 :乱码之后就转换  返回不乱码的字符串
     *  用getBytes(encoding)：返回字符串的一个byte数组  
     *  当b[0]为  63时，应该是转码错误  
     *  A、不乱码的汉字字符串：  
     *  1、encoding用GB2312时，每byte是负数；  
     *  2、encoding用ISO8859_1时，b[i]全是63。  
     *  B、乱码的汉字字符串：  
     *  1、encoding用ISO8859_1时，每byte也是负数；  
     *  2、encoding用GB2312时，b[i]大部分是63。  
     *  C、英文字符串  
     *  1、encoding用ISO8859_1和GB2312时，每byte都大于0；  
     *  总结：给定一个字符串，用getBytes("iso8859_1")  
     *  1、如果b[i]有63，不用转码；  A-2  
     *  2、如果b[i]全大于0，那么为英文字符串，不用转码；  B-1  
     *  3、如果b[i]有小于0的，那么已经乱码，要转码。  C-1  
     */ 
    public  static  String  toGb2312(String  str)  {  
        if  (str  ==  null)  return  null;  
        String  retStr  =  str;  
        byte  b[];  
        try  {  
                b  =  str.getBytes("ISO8859_1"); 
                for  (int  i  =  0;  i  <  b.length;  i++)  {  
                        byte  b1  =  b[i];  
                        if  (b1  ==  63)  
                                break;    //1  
                        else  if  (b1  >  0)  
                                continue;//2  
                        else  if  (b1  <  0)  {        //不可能为0，0为字符串结束符 
//小于0乱码
                                retStr  =  new  String(b,  "GB2312");  
                                break;  
                        }  
                }  
        }  catch  (UnsupportedEncodingException  e)  {  
                  e.printStackTrace();   
        }  
        return  retStr;  
    } 
    /**
     * 判断字符是否是某一个编码 (如果一个字符串和传入的编码是同一个或者说是兼容的那么就不会造成乱码)
     * 	可以用来处理乱码之前的判断 
     * @param s 传入的可能乱码的字符串
     * @param u  用来比较的编码类型 如:UTF-8,ISO-8859-1,GBK等 
     * 
     * @return
     */
    public static boolean isUtf8(String s,String u){
    	
    	return java.nio.charset.Charset.forName(u).newEncoder().canEncode(s);
    }
    /**
     * 处理中文乱码
     * @param s 有可能乱码的字符串
     * @return 处理过后的字符串
     */
    public static String getUtf8Str(String s){
    	
    	if(!isUtf8(s, "GBK")){
    		try {
				s=new String(s.getBytes("ISO-8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	return s;
    }
    
    
   public static void main(String[] args) {
	try {
		System.out.println(isUtf8(new String("ss".getBytes("GBK")), "ISO-8859-1"));
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	};
	
   } 
   
}
