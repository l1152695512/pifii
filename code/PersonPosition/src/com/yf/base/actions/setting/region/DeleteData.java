package com.yf.base.actions.setting.region;


import java.util.Date;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.db.vo.YwRegion;
import com.yf.base.service.YwRegionService;

public class DeleteData extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private YwRegionService ywRegionService;

	private String[] ids;
	
	
	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	@Override
	public String execute() throws Exception {
		List <YwRegion>list=this.ywRegionService.findByIds(ids);
		for (YwRegion bean : list) {
			bean.setDeleted(true);
			bean.setDeletedDate(new Date());
			this.ywRegionService.update(bean);////软删除
		}
		return super.execute();
	}

	public YwRegionService getYwRegionService() {
		return ywRegionService;
	}

	public void setYwRegionService(YwRegionService ywRegionService) {
		this.ywRegionService = ywRegionService;
	}



	
}
