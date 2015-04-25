package com.yf.base.actions.sys.user.usergroup;


import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.service.SysUsergroupUserService;

public class DeleteData extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SysUsergroupUserService sysUsergroupUserService;
	private String[] ids;


	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	@Override
	public String execute() throws Exception {
		
		if(ids != null && this.sysUsergroupUserService.getRowCount() <= ids.length){
			return "failure";
		}
		this.sysUsergroupUserService.deleteByIdArray(ids);
		return super.execute();
	}

	public SysUsergroupUserService getSysUsergroupUserService() {
		return sysUsergroupUserService;
	}

	public void setSysUsergroupUserService(
			SysUsergroupUserService sysUsergroupUserService) {
		this.sysUsergroupUserService = sysUsergroupUserService;
	}


	
}
