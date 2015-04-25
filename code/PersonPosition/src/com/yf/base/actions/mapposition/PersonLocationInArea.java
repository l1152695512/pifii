package com.yf.base.actions.mapposition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class PersonLocationInArea extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String minLocationX;
	private String maxLocationX;
	private String minLocationY;
	private String maxLocationY;
	
	@Override
	public String execute() throws Exception {
		StringBuffer getListSql = new StringBuffer();
		getListSql.append("select * from ");
		getListSql.append("(select p.phone,p.name,l.location_x,l.location_y,l.upload_date,1 info ");
		getListSql.append("from bp_community_tbl c join bp_person_tbl p on (p.communityId=c.id) join bp_phone_location_tbl l on (");
		if(!"".equals(PersonLocation.minSearchDateStr)){
			getListSql.append("l.upload_date > '"+PersonLocation.minSearchDateStr+"' and ");
		}
		getListSql.append("p.phone = l.phone_imsi) ");
		String userId = (String) ActionContext.getContext().getSession().get("loginUserId");
		if(!(Boolean) ActionContext.getContext().getSession().get("isAdmin")){
			getListSql.append("where c.user_id = '"+userId+"' ");
		}
		getListSql.append("order by l.upload_date desc) a ");
		getListSql.append("group by phone having (");
		getListSql.append("location_x > "+minLocationX+" and ");
		getListSql.append("location_x < "+maxLocationX+" and ");
		getListSql.append("location_y > "+minLocationY+" and ");
		getListSql.append("location_y < "+maxLocationY+") ");
		List<?> dataList = dbhelper.getMapListBySql(getListSql.toString());
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("list", dataList.toArray());
		String result=JsonUtils.map2json(resultMap);
		this.jsonString = result;
		return "data";
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public String getMinLocationX() {
		return minLocationX;
	}

	public void setMinLocationX(String minLocationX) {
		this.minLocationX = minLocationX;
	}

	public String getMaxLocationX() {
		return maxLocationX;
	}

	public void setMaxLocationX(String maxLocationX) {
		this.maxLocationX = maxLocationX;
	}

	public String getMinLocationY() {
		return minLocationY;
	}

	public void setMinLocationY(String minLocationY) {
		this.minLocationY = minLocationY;
	}

	public String getMaxLocationY() {
		return maxLocationY;
	}

	public void setMaxLocationY(String maxLocationY) {
		this.maxLocationY = maxLocationY;
	}
	
}
