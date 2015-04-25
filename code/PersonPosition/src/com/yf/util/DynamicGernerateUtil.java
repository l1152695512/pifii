package com.yf.util;

public class DynamicGernerateUtil {

	public static String getDynamic(){
		int dynamicPwd=(int)(Math.random()*900000)+100000; 

			return String.valueOf(dynamicPwd);
		}
	}
