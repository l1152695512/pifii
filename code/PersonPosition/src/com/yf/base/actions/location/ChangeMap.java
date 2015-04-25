package com.yf.base.actions.location;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class ChangeMap extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String mapId;
	
	@Override
	public String execute() throws Exception {
		String sql1 = "update map set isUsed = 0 ";
		String sql2 = "update map set isUsed = 1 where id = '"+mapId+"'";
		List<String> sqlList = new ArrayList<String>();
		sqlList.add(sql1);
		sqlList.add(sql2);
		boolean isSuccess = dbhelper.executeFor(sqlList);
		if(isSuccess){
			return super.execute();
		}
		return "failure";
	}

	public String getMapId() {
		return mapId;
	}
	
	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
	
}
