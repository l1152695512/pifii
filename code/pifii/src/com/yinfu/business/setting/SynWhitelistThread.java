package com.yinfu.business.setting;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class SynWhitelistThread extends Thread{
	private List<Record> devices;
	private boolean clear;
	
	public SynWhitelistThread(List<Record> devices,boolean clear) {
		this.devices = devices;
		this.clear = clear;
	}
	
	@Override
	public void run() {
		new SynWhitelist().synHost(devices,clear);
	}
}
