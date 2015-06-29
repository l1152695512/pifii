package com.yf.base.actions.warnmanageold.warnarea;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class DeleteWarnAreaPoint extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String id;
	
	@Override
	public String execute() throws Exception {
		String[] pointData = getPointData();
		List<String> sqls = new ArrayList<String>();
		sqls.add("delete from bp_fine_area_point_tbl where id = '"+id+"' ");
		if(null != pointData){
			if(StringUtils.isNotBlank(pointData[0])){
				sqls.add("update bp_fine_area_point_tbl set next_point_id = '"+pointData[1]+"' where id = '"+pointData[0]+"' ");
			}
			if(StringUtils.isNotBlank(pointData[1])){
				sqls.add("update bp_fine_area_point_tbl set previous_point_id = '"+pointData[0]+"' where id = '"+pointData[1]+"' ");
			}
		}
		if(dbhelper.executeFor(sqls)){
			return super.execute();
		}
		return "failure";
	}

	@SuppressWarnings("unchecked")
	private String[] getPointData(){
		StringBuffer sql = new StringBuffer();
		sql.append("select previous_point_id,next_point_id from bp_fine_area_point_tbl where id = '");
		sql.append(id);
		sql.append("' ");
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql.toString());
		if(dataList.size()>0){
			Map<String,Object> data = dataList.get(0);
			String previousPoint = null==data.get("previous_point_id")?null:data.get("previous_point_id").toString();
			String nextPoint = null==data.get("next_point_id")?null:data.get("next_point_id").toString();
			String[] routePoints = new String[2];
			routePoints[0] = previousPoint;
			routePoints[1] = nextPoint;
			return routePoints;
		}
		return null;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
}
