package com.yf.base.actions.sys.usergroup.menus;


import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.service.SysUsergroupMenusService;

public class DeleteData extends ActionSupport {

	/**
	 */
	private static final long serialVersionUID = 1L;
	private SysUsergroupMenusService sysUsergroupMenusService;
	private String[] ids;

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	@Override
	public String execute() throws Exception {
		this.sysUsergroupMenusService.deleteByIdArray(ids);
		return super.execute();
	}

	public SysUsergroupMenusService getSysUsergroupMenusService() {
		return sysUsergroupMenusService;
	}

	public void setSysUsergroupMenusService(
			SysUsergroupMenusService sysUsergroupMenusService) {
		this.sysUsergroupMenusService = sysUsergroupMenusService;
	}


	
	
}
