package com.pifii.test;

import java.util.Calendar;

public class Test {
public static void main(String[] args) {
	Calendar c=Calendar.getInstance();
	c.get(Calendar.MONTH);
	c.get(Calendar.DAY_OF_MONTH);
	System.out.println(c.get(Calendar.MONDAY)+1);
	System.out.println(c.get(Calendar.DAY_OF_MONTH));
}
}
