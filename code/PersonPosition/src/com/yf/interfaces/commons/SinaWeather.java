package com.yf.interfaces.commons;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.util.StringUtils;


/**
 * 从新浪网站获取的天气实体类
 * 
 * @author dxm
 * 
 */
public class SinaWeather {

	// 单例
	private static SinaWeather inst = null;

	/**
	 * 构造器
	 */
	private SinaWeather() {
	}

	/**
	 * 获取单例
	 * 
	 * @return
	 */
	public static SinaWeather getInstance() {
		if (null == inst) {
			inst = new SinaWeather();
		}
		return inst;
	}

	public String getAndUpdWeather(String city) {

		// 将城市转码

		// 获取天气的URL
		String urlPath = "http://php.weather.sina.com.cn/xml.php?city=" + city + "&password=DJOYnieT8234jlsK&day=0";

		// 从URL获取的内容
		StringBuffer weather = new StringBuffer();

		try {

			// 链接指定URL
			URL url = new URL(urlPath);
			URLConnection conn = url.openConnection();

			// 设置包头
			conn.setRequestProperty("User-agent", "Mozilla/4.0");
			conn.connect();

			// 获取输入流
			InputStream input = conn.getInputStream();

			int all = input.available();

			// 读取数据
			while (all > 0) {

				byte[] b = new byte[all];
				input.read(b);
				weather.append(new String(b, "UTF-8"));

				all = input.available();
			}

			// 关闭输入流
			input.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return weather.toString();
	}

}
