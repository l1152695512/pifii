package com.yf.base.actions.sys.dictionary;


import com.yf.base.service.SysDictionaryService;
import com.opensymphony.xwork2.ActionSupport;

public class DeleteData extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SysDictionaryService sysDictionaryService;


	private String [] ids;
	

	@Override
	public String execute() throws Exception {
		this.sysDictionaryService.deleteByIdArray(ids);
		return super.execute();
		
	}

	public SysDictionaryService getSysDictionaryService() {
		return sysDictionaryService;
	}

	public void setSysDictionaryService(SysDictionaryService sysDictionaryService) {
		this.sysDictionaryService = sysDictionaryService;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}


	
	
}
