package com.yf.base.actions.sys.datadelete;

import java.util.HashMap;

public class Constants {
	private static  HashMap<Integer,String> hasp ;

	public static HashMap <Integer,String> getHashMap(){
		if(hasp==null){
			hasp=new HashMap<Integer,String>();
			hasp.put(0, "区域表");
			hasp.put(1, "机楼类型表");
			hasp.put(2, "公司表");
			hasp.put(3, "机楼表");
			hasp.put(4, "接入间");
			hasp.put(5, "基站");
			hasp.put(6, "楼层");
			hasp.put(7, "机房");
			hasp.put(8, "用户组");
		}
		return hasp;
	}

}