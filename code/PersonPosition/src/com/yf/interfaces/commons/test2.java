package com.yf.interfaces.commons;

import java.io.IOException;
import java.util.Calendar;

import org.jdom.Document;
import org.jdom.JDOMException;

import com.yf.tradecontrol.JDomHandler;
import com.yf.tradecontrol.JDomHandlerException;







public class test2 {
	
	 private static int getMonthDays(int year, int month) {
			Calendar cal = Calendar.getInstance();

			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month - 1);

			return cal.getActualMaximum(Calendar.DATE);
		}
	
	 public static void main(String[] args) { 
		 
		 SinaWeather aa = SinaWeather.getInstance();
		 String tt = aa.getAndUpdWeather("北京");
		 System.out.println(tt);
//		 String urlString = "http://php.weather.sina.com.cn/xml.php?city=北京&password=DJOYnieT8234jlsK&day=0";
//		 HttpRequester http = new HttpRequester();
//		 try {
//			 HttpRespons hr = http.sendGet(urlString);
//			 System.out.println(hr.getContent());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		JDomHandler domHandler = new JDomHandler();
//		Document doc = domHandler.loadXmlByUrl(urlString);
//		domHandler.setDoc(doc);
//		try {
//			System.out.println(domHandler.getNodeValue("/Profiles/Weather/city"));
//		} catch (JDOMException e) {
//			e.printStackTrace();
//		} catch (JDomHandlerException e) {
//			e.printStackTrace();
//		}

	}

}
