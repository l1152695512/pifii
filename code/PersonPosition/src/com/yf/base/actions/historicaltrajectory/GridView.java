package com.yf.base.actions.historicaltrajectory;


import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class GridView extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String defaultMapPath;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		String sql = "select path from map where isUsed = 1 limit 1 ";
		List<?> dataList = dbhelper.getMapListBySql(sql);
		defaultMapPath = ((Map<String,String>)dataList.get(0)).get("path");
		return super.execute();
	}
	
	public String getDefaultMapPath() {
		return defaultMapPath;
	}
	
	public void setDefaultMapPath(String defaultMapPath) {
		this.defaultMapPath = defaultMapPath;
	}
}
