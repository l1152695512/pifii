package com.yf.base.actions.warnmanage.warnarea;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.actions.mapposition.areamanage.AreaUtilService;

public class DeleteWarnArea extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
//	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String warnAreaId;
	
	@Override
	public String execute() throws Exception {
//		List<String> sqls = new ArrayList<String>();
//		sqls.add("delete from bp_fine_area_tbl where id='"+warnAreaId+"' ");
//		sqls.add("delete from bp_fine_area_point_tbl where area_id='"+warnAreaId+"' ");
//		if(dbhelper.executeFor(sqls)){
//			return super.execute();
//		}
		if(AreaUtilService.removeArea(warnAreaId,"bp_fine_area_tbl","bp_fine_area_point_tbl","bp_fine_area_time_tbl","bp_fine_area_time_assign_tbl")){
			return super.execute();
		}
		return "failure";
	}

	public String getWarnAreaId() {
		return warnAreaId;
	}
	
	public void setWarnAreaId(String warnAreaId) {
		this.warnAreaId = warnAreaId;
	}
	
}
