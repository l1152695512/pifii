package com.yf.base.actions.devicemanage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class AddDevice extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String locationX;
	private String locationY;
	
	private String jsonString;
	
	@Override
	public String execute() throws Exception {
//		String groupId = ActionContext.getContext().getSession().get("userGroupId").toString();
		String deviceId = UUID.randomUUID().toString().replaceAll("-", "");
		StringBuffer sql = new StringBuffer();
		sql.append("insert into bp_card_tbl(id,locationX,locationY,addDate) values('");
		sql.append(deviceId);
		sql.append("',");
		sql.append(locationX);
		sql.append(",");
		sql.append(locationY);
		sql.append(",now())");
		boolean isSuccess = dbhelper.insert(sql.toString());
		if(isSuccess){
			Map<String,String> params = new HashMap<String,String>();
			params.put("id", deviceId);
			params.put("locationX", locationX);
			params.put("locationY", locationY);
			params.put("addDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			this.jsonString = JsonUtils.map2json(params);;
		}
		return "data";
	}
	
	public String getLocationX() {
		return locationX;
	}

	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}

	public String getLocationY() {
		return locationY;
	}

	public void setLocationY(String locationY) {
		this.locationY = locationY;
	}
	
	public String getJsonString() {
		return jsonString;
	}
	
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	
}
