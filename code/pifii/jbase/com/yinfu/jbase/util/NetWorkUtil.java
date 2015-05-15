package com.yinfu.jbase.util;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 网络相关工具类
 * @author JiaYongChao
 *
 */
public class NetWorkUtil {
	//@formatter:off 
	/**
	 * Title: byPhoneGetInfo
	 * Description:通过手机号码获得归属地
	 * Created On: 2014年9月15日 上午9:41:02
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public static String byPhoneGetAddress(String mobile){
		String address = "";
		try {
			mobile = mobile.trim();
			if (mobile.matches("^(13|15|18)\\d{9}$") || mobile.matches("^(013|015|018)\\d{9}$")) // 以13，15，18开头，后面九位全为数字
			{
				String url = "http://www.ip138.com:8080/search.asp?action=mobile&mobile=" + mobile;
				URLConnection connection = (URLConnection) new URL(url).openConnection();
				connection.setDoOutput(true);
				InputStream os = connection.getInputStream();
				Thread.sleep(100);
				int length = os.available();
				byte[] buff = new byte[length];
				os.read(buff);
				String s = new String(buff, "gbk");
				Document doc = Jsoup.parse(s);
				Element info = doc.select("table").last();
				Elements addressInfo = info.select("tr").eq(2);
				address = addressInfo.select("td").last().text();
				s = null;
				buff = null;
				os.close();
				connection = null;
			}
		} catch (Exception e) {
			address = "未知";
		}
		return address;
	}
	//@formatter:off 
	/**
	 * Title: byMacGetInfo
	 * Description:  通过手机号码获得卡类型  
	 * Created On: 2014年9月15日 上午9:41:35
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public static String byPhoneGetCardType (String mobile){
		String cardType="";//卡类型
		try {
			mobile = mobile.trim();
			if (mobile.matches("^(13|15|18)\\d{9}$") || mobile.matches("^(013|015|018)\\d{9}$")) // 以13，15，18开头，后面九位全为数字
			{
				String url = "http://www.ip138.com:8080/search.asp?action=mobile&mobile=" + mobile;
				URLConnection connection = (URLConnection) new URL(url).openConnection();
				connection.setDoOutput(true);
				InputStream os = connection.getInputStream();
				Thread.sleep(100);
				int length = os.available();
				byte[] buff = new byte[length];
				os.read(buff);
				String s = new String(buff, "gbk");
				Document doc = Jsoup.parse(s);
				Element info = doc.select("table").last();
				Elements cardTypeInfo = info.select("tr").eq(3);
				cardType = cardTypeInfo.select("td").last().text();
				s = null;
				buff = null;
				os.close();
				connection = null;
			}
		} catch (Exception e) {
			cardType = "未知";
		}
		return cardType;
	}
	//@formatter:off 
	/**
	 * Title: byMacGetInfo
	 * Description:通过mac地址获得信息
	 * Created On: 2014年9月15日 上午10:58:43
	 * @author JiaYongChao
	 * <p>
	 * @param mac 
	 */
	//@formatter:on
	public static String byMacGetInfo(String mac){
		String ftutype= "";//终端信息
		try {
				mac = mac.trim();
				String url = "http://mac.51240.com/"+mac+"__mac/";
				URLConnection connection = (URLConnection) new URL(url).openConnection();
				connection.setDoOutput(true);
				InputStream os = connection.getInputStream();
				Thread.sleep(100);
				int length = os.available();
				byte[] buff = new byte[length];
				os.read(buff);
				String s = new String(buff, "utf-8");
				Document doc = Jsoup.parse(s);
				Element info = doc.select("table").first();
				Elements cardTypeInfo = info.select("tr").eq(2);
				ftutype = cardTypeInfo.select("td").last().text();
				if(StringUtils.contains(ftutype," ")){
					String infos = ftutype.split(" ")[0].toString();
					ftutype = infos;
				}
				s = null;
				buff = null;
				os.close();
				connection = null;
		} catch (Exception e) {
			ftutype = "未知";
		}
		return ftutype;
	}
	
	public static void main(String[] args) {
		byMacGetInfo("mm-ab-27-7c-4c-35");
	}
}
