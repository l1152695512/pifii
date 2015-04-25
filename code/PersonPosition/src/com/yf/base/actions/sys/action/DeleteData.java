package com.yf.base.actions.sys.action;


import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.service.SysActionService;

public class DeleteData extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SysActionService sysActionService;


	public SysActionService getSysActionService() {
		return sysActionService;
	}

	public void setSysActionService(SysActionService sysActionService) {
		this.sysActionService = sysActionService;
	}

	private String[] ids;
	
	


	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	@Override
	public String execute() throws Exception {


		this.sysActionService.deleteByIdArray(ids);
		return super.execute();
	}
	
	
}
