package com.pifii.timer;

import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pifii.test.Table;
import com.pifii.util.DateUtil;
import com.pifii.util.ResourcesUtil;

public class TimerCutTab {
	Logger log=LoggerFactory.getLogger(TimerCutTab.class);
	Timer time=new Timer();
	public TimerCutTab(){
		log.info("分表服务启动");
//		time.schedule(new Table(), DateUtil.getTimerRual(11, 52,1));//定时执行
		int shi=Integer.parseInt(ResourcesUtil.getVbyKey("shi"));
		int fen=Integer.parseInt(ResourcesUtil.getVbyKey("fen"));
//		int miao=Integer.parseInt(ResourcesUtil.getVbyKey("miao"));
		int foreach=1000*60*60;//默认一天
		time.schedule(new Table(), DateUtil.getTimerRual(shi, fen),foreach);//每天执行
		log.info("分表时间"+DateUtil.getTimerRual(shi, fen));
	}
	public static void main(String[] args) {
		//	 new TimerCutTab();
	}
}

