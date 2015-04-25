package com.yf.base.actions.sys.user;


import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.service.SysUserService;
import com.yf.util.Debug;

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
		Debug.println(".............delete..........");
		if(ids != null && this.sysUserService.getRowCount() <= ids.length){
			return "failure";
		}
		this.sysUserService.deleteByIdArray(ids);
		return super.execute();
	}
	
	
}
