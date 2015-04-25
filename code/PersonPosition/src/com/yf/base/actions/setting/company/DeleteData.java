package com.yf.base.actions.setting.company;


import java.util.Date;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.db.vo.YwCompany;
import com.yf.base.service.YwCompanyService;

public class DeleteData extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private YwCompanyService ywCompanyService;

	private String[] ids;
	
	public YwCompanyService getYwCompanyService() {
		return ywCompanyService;
	}

	public void setYwCompanyService(YwCompanyService ywCompanyService) {
		this.ywCompanyService = ywCompanyService;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	@Override
	public String execute() throws Exception {
		List <YwCompany>list=this.ywCompanyService.findByIds(ids);
		for (YwCompany bean : list) {
			bean.setDeleted(true);
			bean.setDeletedDate(new Date());
			this.ywCompanyService.update(bean);////软删除
		}
		//ywCompanyService.deleteByIdArray(ids);
		return super.execute();
	}


	
}
