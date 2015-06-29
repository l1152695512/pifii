package pifii.com.log.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TableNameFort {
	static SimpleDateFormat sdf=new SimpleDateFormat("_yyyy_MM_dd");
	public static String  nowTable(){
		
		return "bpbaselogtbl"+sdf.format(new Date());
	}

}
