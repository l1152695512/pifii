package com.yf.base.actions.sys.usergroup;


import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.service.SysUsergroupService;
import com.yf.util.Debug;

public class DeleteData extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SysUsergroupService sysUsergroupService;
	private String[] ids;
	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	@Override
	public String execute() throws Exception {
		Debug.println("aaaaa");
		for(int i=0;i<ids.length;i++)Debug.println(i+"  =="+ids[i]);
		if(ids != null && this.sysUsergroupService.getRowCount() <= ids.length){
			return "failure";
		}
		this.sysUsergroupService.deleteByIdArray(ids);
		return super.execute();
	}

	public SysUsergroupService getSysUsergroupService() {
		return sysUsergroupService;
	}

	public void setSysUsergroupService(SysUsergroupService sysUsergroupService) {
		this.sysUsergroupService = sysUsergroupService;
	}
	
	
}
