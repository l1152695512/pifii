package com.yf.base.actions.sys.usergroup.action;


import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.service.SysActionItemService;
import com.yf.base.service.SysUsergroupActionService;

public class DeleteData extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SysUsergroupActionService sysUsergroupActionService;
	private String[] ids;

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	@Override
	public String execute() throws Exception {

		this.sysUsergroupActionService.deleteByIdArray(ids);
		return super.execute();
	}

	public SysUsergroupActionService getSysUsergroupActionService() {
		return sysUsergroupActionService;
	}

	public void setSysUsergroupActionService(
			SysUsergroupActionService sysUsergroupActionService) {
		this.sysUsergroupActionService = sysUsergroupActionService;
	}
	
	
}
