package com.yf.base.actions.sys.usergroup.menus;

import com.yf.base.db.vo.SysMenu;
import com.yf.base.db.vo.SysUsergroup;
import com.yf.base.db.vo.SysUsergroupMenus;
import com.yf.base.service.SysMenuService;
import com.yf.base.service.SysUsergroupMenusService;
import com.yf.base.service.SysUsergroupService;
import com.yf.ext.base.BaseAction;

public class SelectSave extends  BaseAction {
	
	private SysUsergroupMenusService sysUsergroupMenusService;
	private SysUsergroupService sysUsergroupService;

	private SysMenuService sysMenuService;
	private String sugid;//用户组主键
	private String []ids;//主键
	@Override
	public String execute() throws Exception {
		try{
			SysUsergroup  sysUsergroup=this.sysUsergroupService.findById(sugid);
			SysUsergroupMenus sysUsergroupMenus=null;
			for(int i=0;i<ids.length;i++){
				 SysMenu   sysMenu=this.sysMenuService.findById(ids[i]);
				if(sysMenu!=null){
					sysUsergroupMenus=new SysUsergroupMenus ();
					sysUsergroupMenus.setSysMenu(sysMenu);
					sysUsergroupMenus.setSysUsergroup(sysUsergroup);
					this.sysUsergroupMenusService.add(sysUsergroupMenus);
				}	
			}
		}catch(Exception e){
			e.printStackTrace();
			this.msg="保存失败,请稍后再试!";
			return "failure";
		}
		
		return super.execute();
	}

	public SysUsergroupMenusService getSysUsergroupMenusService() {
		return sysUsergroupMenusService;
	}

	public void setSysUsergroupMenusService(
			SysUsergroupMenusService sysUsergroupMenusService) {
		this.sysUsergroupMenusService = sysUsergroupMenusService;
	}

	public SysMenuService getSysMenuService() {
		return sysMenuService;
	}

	public void setSysMenuService(SysMenuService sysMenuService) {
		this.sysMenuService = sysMenuService;
	}

	public SysUsergroupService getSysUsergroupService() {
		return sysUsergroupService;
	}
	public void setSysUsergroupService(SysUsergroupService sysUsergroupService) {
		this.sysUsergroupService = sysUsergroupService;
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
