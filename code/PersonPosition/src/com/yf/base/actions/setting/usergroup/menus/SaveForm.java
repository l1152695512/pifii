package com.yf.base.actions.setting.usergroup.menus;

import java.util.List;

import com.yf.base.db.vo.SysMenu;
import com.yf.base.db.vo.SysUsergroup;
import com.yf.base.db.vo.SysUsergroupMenus;
import com.yf.base.service.SysMenuService;
import com.yf.base.service.SysUsergroupMenusService;
import com.yf.base.service.SysUsergroupService;
import com.yf.data.base.TransactionalCallBack;
import com.yf.ext.base.BaseAction;
import com.yf.service.IService;

public class SaveForm extends   BaseAction {

	private SysUsergroupService sysUsergroupService;
	private String sugid;//用户组主键
	private String []systemMenuIds;
	private SysUsergroupMenusService sysUsergroupMenusService;
	private SysMenuService sysMenuService;
	
	@Override
	public String execute() throws Exception {
		try {
			this.sysUsergroupService
					.executeTransactional(new TransactionalCallBack() {
						@Override
						public Object execute(IService arg0) {
							try{
								executeDoing();
								return null;
							}catch(Exception e){
								e.printStackTrace();
							}
							return null;
						}
					});

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "failure";
		}
		return "success";
	}

	public String executeDoing() throws Exception {
		SysUsergroup sysUsergroup=sysUsergroupService.findById(sugid);
		sysUsergroupMenusService.deleteList(sysUsergroup.getSysUsergroupMenuss());
		List <SysMenu>sysMenuList=this.sysMenuService.findByIds(this.systemMenuIds);
		SysUsergroupMenus sysUsergroupMenus=null;
		for (SysMenu sysMenu : sysMenuList) {
			sysUsergroupMenus=new SysUsergroupMenus();
			sysUsergroupMenus.setSysMenu(sysMenu);
			sysUsergroupMenus.setSysUsergroup(sysUsergroup);
			this.sysUsergroupMenusService.add(sysUsergroupMenus);
		}
		return super.execute();
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

	public String[] getSystemMenuIds() {
		return systemMenuIds;
	}

	public void setSystemMenuIds(String[] systemMenuIds) {
		this.systemMenuIds = systemMenuIds;
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



}
