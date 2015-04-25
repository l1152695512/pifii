package com.yf.base.actions.devicemanage;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class DeleteDevice extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String deviceId;
	
	@Override
	public String execute() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("delete from bp_card_tbl where id='");
		sql.append(deviceId);
		sql.append("'");
		boolean isSuccess = dbhelper.delete(sql.toString());
		if(isSuccess){
			return super.execute();
		}
		return "failure";
	}

	public String getDeviceId() {
		return deviceId;
	}
	
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
}
