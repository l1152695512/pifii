package com.yf.base.actions.setting.usergroup;


import java.util.Date;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.db.vo.SysUsergroup;
import com.yf.base.db.vo.YwRegion;
import com.yf.base.service.SysUsergroupService;

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
		List <SysUsergroup>list=this.sysUsergroupService.findByIds(ids);
		for (SysUsergroup bean : list) {
			bean.setDeleted(true);
			bean.setDeletedDate(new Date());
			this.sysUsergroupService.update(bean);////软删除
		}
		return super.execute();
	}

	public SysUsergroupService getSysUsergroupService() {
		return sysUsergroupService;
	}

	public void setSysUsergroupService(SysUsergroupService sysUsergroupService) {
		this.sysUsergroupService = sysUsergroupService;
	}
	
	
}
