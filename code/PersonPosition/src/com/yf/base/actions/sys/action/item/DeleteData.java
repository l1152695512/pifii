package com.yf.base.actions.sys.action.item;


import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.service.SysActionItemService;

public class DeleteData extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SysActionItemService sysActionItemService;

	
	private String[] ids;
	
	public SysActionItemService getSysActionItemService() {
		return sysActionItemService;
	}

	public void setSysActionItemService(SysActionItemService sysActionItemService) {
		this.sysActionItemService = sysActionItemService;
	}



	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	@Override
	public String execute() throws Exception {

		this.sysActionItemService.deleteByIdArray(ids);
		return super.execute();
	}
	
	
}
