package com.yf.base.actions.mapfloor;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class Index extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String id;
	private String jsonData;
	private String msg;
	private String title;
	
	@Override
	public String execute() throws Exception {
		List<?> dataList = dbhelper.getMapListBySql(
				"select id,trim(TRAILING ')' FROM right(name,LOCATE('(',REVERSE(name))-1)) name,map from bp_community_tbl where dependent_community =? or id=? order by createDate ",
				new Object[]{id,id});
		if(dataList.size()>0){
			jsonData = JsonUtils.object2json(dataList);
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
	
	public String getJsonData() {
		return jsonData;
	}
	
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
}
