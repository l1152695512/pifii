package com.pifii.test;

import java.util.Calendar;
import java.util.List;

import com.pifii.dao.CutTableJdbc;

public class Test {
public static void main(String[] args) {
	Calendar c=Calendar.getInstance();
	c.get(Calendar.MONTH);
	c.get(Calendar.DAY_OF_MONTH);
	System.out.println(c.get(Calendar.MONDAY)+1);
	System.out.println(c.get(Calendar.DAY_OF_MONTH));
}
@org.junit.Test
public void testSele(){
	List<String> li=CutTableJdbc.getReplace("bpbaselogtbl_2015_06_06");
	for (String string : li) {
		System.out.println(string);
		if(!"2015-06-06".equals(string)){
			//这里是要转移的数据
			//1.查一下有多少条
			Integer num=CutTableJdbc.selectDateTab("bpbaselogtbl_2015_06_06", string);
			System.out.println(num);
			for(int s=0;s<=num;s++){
				CutTableJdbc.insertTabLimit("bpbaselogtbl_2015_06_07", "bpbaselogtbl_2015_06_06", string);
				CutTableJdbc.deleteTabLimit("bpbaselogtbl_2015_06_06", string);
			}
			
		}
	}
}
}
