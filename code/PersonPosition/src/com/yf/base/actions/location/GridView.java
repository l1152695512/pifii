package com.yf.base.actions.location;


import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class GridView extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String id;//f05e7f0794ca422189f922e549f9edc4
	private String map;
	private String name;
	private String createDate;
	private String msg;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		String sql = "select name,map,DATE_FORMAT(createDate,'%Y-%m-%d %H:%i:%s') createDate from community where id = '"+id+"' ";
		List<?> dataList = dbhelper.getMapListBySql(sql);
		if(dataList.size()>0){
			Map<String,String> data = (Map<String,String>)dataList.get(0);
			map = data.get("name");
			name = data.get("map");
			createDate = data.get("createDate");
			return super.execute();
		}else{
			this.msg = "该小区不存在！";
		}
		return "failure";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
