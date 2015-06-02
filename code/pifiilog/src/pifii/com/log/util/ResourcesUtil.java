package pifii.com.log.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourcesUtil {
	/**
	 * 
	 * 资源读取文件工具类
	 * 
	 */
	public static String RESOURCE_URL="/config.properties";
	private static Properties p=null;
	public synchronized static void init(){
		
		try {
			p=new Properties();
			p.load(ResourcesUtil.class.getResourceAsStream(RESOURCE_URL));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 	根据资源文件参数取得资源路径
	 * new FileInputStream(new File(RESOURCE_URL)) 以这样的相对路径的方式 路径是conf/file.properties
	 * @param key 参数名
	 * @return 取得的资源
	 */
	public static String getVbyKey(String key){
		init();
		return 	p.getProperty(key);
	}
	/**
	 * 遇到问题:如果项目被打成jar包 之前的项目是文件夹(jar包是文件)造成以相对路径的资源文件无法读取要采用这种方式
	 * @param key 资源文件的key
	 * @param file 文件(*.properties)的绝对路径比如:c:/conf/file.properties
	 * @return 资源文件中的value
	 */
	public static String getFileKey(String key,String file){
		 Properties p=new Properties();
		 try {
			p.load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 	p.getProperty(key);
	}
	
	
	
}
