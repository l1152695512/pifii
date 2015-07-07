package com.cnvp.paladin.pifii.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateU {
	private DateU(){
	}
	public static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
	public static SimpleDateFormat cut_tab_sdf=new SimpleDateFormat("yyyy_MM_dd");
	public static String getNow(){
		return sdf.format(System.currentTimeMillis());
	}
	
	public static void main(String[] args) {
		System.out.println(getNow());
	}
	
}
