package com.pifii.test;

import java.util.Calendar;
import java.util.List;

import com.pifii.dao.CutTableJdbc;

public class Test {
public static void main(String[] args) {
//	Calendar c=Calendar.getInstance();
//	c.get(Calendar.MONTH);
//	c.get(Calendar.DAY_OF_MONTH);
//	System.out.println(c.get(Calendar.MONDAY)+1);
//	System.out.println(c.get(Calendar.DAY_OF_MONTH));
	
}
//1.表中有其他日期
@org.junit.Test
public void testSeleTab(){
List<String> list=CutTableJdbc.getReplace("bpbaselogtbl");
for (String string : list) {
	System.out.println(string);
}


}
}
