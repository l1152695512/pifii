package com.yf.base.actions.mapposition.areamanage.warnarea;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.actions.mapposition.areamanage.AreaUtilService;

public class DeleteArea extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
//	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String areaId;
	
	@Override
	public String execute() throws Exception {
//		List<String> sqls = new ArrayList<String>();
//		sqls.add("delete from bp_coarse_area_tbl where id='"+areaId+"' ");
//		sqls.add("delete from bp_coarse_area_point_tbl where area_id='"+areaId+"' ");
//		if(dbhelper.executeFor(sqls)){
//			return super.execute();
//		}
		if(AreaUtilService.removeArea(areaId,"bp_coarse_area_tbl","bp_coarse_area_point_tbl","bp_coarse_area_time_tbl","bp_coarse_area_time_assign_tbl")){
			return super.execute();
		}
		return "failure";
	}

	public String getAreaId() {
		return areaId;
	}
	
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	
}
