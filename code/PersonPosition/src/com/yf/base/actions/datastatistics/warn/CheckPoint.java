package com.yf.base.actions.datastatistics.warn;

import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;
//测试点在警告区域的位置（内，外，线上）
public class CheckPoint extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String msg;
	private String warnAreaId;
	private String locationX;
	private String locationY;

	@Override
	public String execute() throws Exception {
		Map<String,List<Map<String,Object>>> warnAreas = Commons.getWarnAreas(dbhelper);
		if(null != warnAreas){
			int pointPosition = Commons.checkPointInWarnArea(warnAreas.get(warnAreaId),Double.parseDouble(locationX),Double.parseDouble(locationY));
			if(-1 == pointPosition){
				this.msg = "在区域外！";
			}else if(0 == pointPosition){
				this.msg = "在线上！";
			}else if(1 == pointPosition){
				this.msg = "在区域内！";
			}else{
				this.msg = "pointPosition="+pointPosition;
			}
			return super.execute();
		}else{
			this.msg = "获取警告区域失败！";
		}
		return "failure";
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getWarnAreaId() {
		return warnAreaId;
	}
	
	public void setWarnAreaId(String warnAreaId) {
		this.warnAreaId = warnAreaId;
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
	
}
