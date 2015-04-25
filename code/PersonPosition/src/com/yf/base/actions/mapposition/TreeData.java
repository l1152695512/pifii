package com.yf.base.actions.mapposition;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class TreeData extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String node;
	private String type;
	private String checked;

	@Override
	public String execute() throws Exception {
		JSONArray array = new JSONArray();
		String userId = (String) ActionContext.getContext().getSession().get("loginUserId");
		StringBuffer sql = new StringBuffer();
		if("2".equals(type)){//加载小区
			sql.append("SELECT id AS RID,districtId AS PID ,NAME,LOCATIONX,LOCATIONY,MAP,3 AS TYPE ");
			sql.append("FROM bp_community_tbl WHERE districtId = ? ");
			if(!(Boolean) ActionContext.getContext().getSession().get("isAdmin")){
				sql.append("and user_id = '"+userId+"' ");
			}
		}else if("3".equals(type)){//加载人员
			sql.append("SELECT phone AS RID,communityId AS PID ,NAME,4 AS TYPE ");
			sql.append("FROM bp_person_tbl WHERE communityId = ? ");
		}else{
			if("402881fe3127d5510131283920d30001".equals(node)){//加载市
				sql.append("select DISTINCT r.RID,r.PID,r.NAME,r.LOCATIONX,r.LOCATIONY,'' AS MAP,1 AS TYPE ");
				sql.append("FROM bp_community_tbl c ");
				sql.append("join yw_region_tbl r1 on (r1.rid = c.districtId) ");
				sql.append("join yw_region_tbl r on (r.rid = r1.pid) ");
				sql.append("where r.pid = ? ");
				if(!(Boolean) ActionContext.getContext().getSession().get("isAdmin")){
					sql.append("and c.user_id = '"+userId+"' ");
				}
			}else{//加载区
				sql.append("select DISTINCT r.RID,r.PID,r.NAME,r.LOCATIONX,r.LOCATIONY,'' AS MAP,2 AS TYPE ");
				sql.append("FROM bp_community_tbl c ");
				sql.append("join yw_region_tbl r on (r.rid = c.districtId) ");
				sql.append("where r.pid = ? ");
				if(!(Boolean) ActionContext.getContext().getSession().get("isAdmin")){
					sql.append("and c.user_id = '"+userId+"' ");
				}
			}
		}
		List<?> list = dbhelper.getMapListBySql(sql.toString(),new Object[]{node});
		processTreeNode(array,list);
		jsonString = array.toString();
		return "data";
	}
	
	@SuppressWarnings("unchecked")
	private void processTreeNode(JSONArray array , List<?> depts){
		for(int i=0;i<depts.size();i++){
			Map<String,Object> map = (Map<String,Object>)depts.get(i);
			JSONObject json = new JSONObject();
			json.put("id", map.get("RID"));
			json.put("text", map.get("NAME"));
			json.put("longitude", map.get("LOCATIONX"));
			json.put("latitude", map.get("LOCATIONY"));
			json.put("map", map.get("MAP"));
			json.put("type", map.get("TYPE"));
			if("1".equals(checked)){
				json.put("checked", true);
			}else{
				json.put("checked", false);
			}
			if(map.get("TYPE").toString().equals("4")){
				json.put("icon", "hr/img/person_photo.png");
				json.put("leaf", true);
			}else if(map.get("TYPE").toString().equals("3")){
				json.put("icon", "hr/img/net_16.png");
				json.put("leaf", false);
			}else{
				json.put("icon", "hr/img/e_16.png");
				json.put("leaf", false);
			}
			array.add(json);
		}
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	
	public String getNode() {
		return node;
	}
	
	public void setNode(String node) {
		this.node = node;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getChecked() {
		return checked;
	}
	
	public void setChecked(String checked) {
		this.checked = checked;
	}
}
