package com.yf.base.actions.location;

import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class AddDispatchTask extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String mapPath;
	private String personId;
	private String name;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		String sql = "select name from bp_person_tbl where id='"+personId+"' ";
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
		if(dataList.size()>0){
			Map<String,Object> data = dataList.get(0);
			name = null==data.get("name")?"":data.get("name").toString();
			return super.execute();
		}else{
			return "failure";
		}
	}
	
	public String getMapPath() {
		return mapPath;
	}
	
	public void setMapPath(String mapPath) {
		this.mapPath = mapPath;
	}
	
	public String getPersonId() {
		return personId;
	}
	
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
