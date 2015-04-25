package com.yf.base.actions.mapposition.areamanage.areaassign;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.actions.mapposition.areamanage.AreaUtilService;

public class DeleteAreaTime extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
//	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String[] ids;

	@Override
	public String execute() throws Exception {
//		List<String> sqlList = new ArrayList<String>();
//		for(int i=0;i<ids.length;i++){
//			if(!"".equals(ids[i])){
//				sqlList.add("delete from bp_coarse_area_time_tbl where id='"+ids[i]+"'");
//			}
//		}
//		if(dbhelper.executeFor(sqlList)){
//			return super.execute();
//		}
		if(AreaUtilService.removeAreaTimes(ids,"bp_coarse_area_time_tbl","bp_coarse_area_time_assign_tbl")){
			return super.execute();
		}
		return "failure";
	}

	public String[] getIds() {
		return ids;
	}
	
	public void setIds(String[] ids) {
		this.ids = ids;
	}
}
