package com.yf.base.actions.sys.usergroup.action;

import com.yf.base.db.vo.SysAction;
import com.yf.base.db.vo.SysUsergroup;
import com.yf.base.db.vo.SysUsergroupAction;
import com.yf.base.service.SysActionService;
import com.yf.base.service.SysUsergroupActionService;
import com.yf.base.service.SysUsergroupService;
import com.yf.ext.base.BaseAction;

public class SelectSave extends  BaseAction {
	
	private SysUsergroupActionService sysUsergroupActionService;
	private SysUsergroupService sysUsergroupService;
	private SysActionService sysActionService;
	private String sugid;//用户组主键
	private String []ids;//主键
	@Override
	public String execute() throws Exception {
		try{
			SysUsergroup  sysUsergroup=this.sysUsergroupService.findById(sugid);
			SysUsergroupAction sysUsergroupAction=null;
			for(int i=0;i<ids.length;i++){
				SysAction  sysAction=this.sysActionService.findById(ids[i]);
				if(sysAction!=null){
					sysUsergroupAction=new SysUsergroupAction ();
					sysUsergroupAction.setSysAction(sysAction);
					sysUsergroupAction.setSysUsergroup(sysUsergroup);
					this.sysUsergroupActionService.add(sysUsergroupAction);
				}	
			}
		}catch(Exception e){
			e.printStackTrace();
			this.msg="保存失败,请稍后再试!";
			return "failure";
		}
		
		return super.execute();
	}
	public SysUsergroupActionService getSysUsergroupActionService() {
		return sysUsergroupActionService;
	}
	public void setSysUsergroupActionService(
			SysUsergroupActionService sysUsergroupActionService) {
		this.sysUsergroupActionService = sysUsergroupActionService;
	}
	public SysUsergroupService getSysUsergroupService() {
		return sysUsergroupService;
	}
	public void setSysUsergroupService(SysUsergroupService sysUsergroupService) {
		this.sysUsergroupService = sysUsergroupService;
	}
	public SysActionService getSysActionService() {
		return sysActionService;
	}
	public void setSysActionService(SysActionService sysActionService) {
		this.sysActionService = sysActionService;
	}
	public String getSugid() {
		return sugid;
	}
	public void setSugid(String sugid) {
		this.sugid = sugid;
	}
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}

	
	

}
