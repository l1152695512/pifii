package com.yf.base.actions.setting.usergroup.user;


import com.yf.base.service.SysUserService;
import com.opensymphony.xwork2.ActionSupport;

public class DeleteData extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SysUserService sysUserService;



	public SysUserService getSysUserService() {
		return sysUserService;
	}

	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
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
		this.sysUserService.deleteByIdArray(ids);
		return super.execute();
	}
	
	
}
