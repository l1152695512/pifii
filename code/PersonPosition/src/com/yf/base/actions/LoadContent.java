package com.yf.base.actions;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.db.vo.SysMenu;
import com.yf.base.service.SysMenuService;
import com.yf.data.base.TransactionalCallBack;
import com.yf.service.IService;
import com.yf.util.Debug;

public class LoadContent extends ActionSupport {
	
	private SysMenuService systemMenuService;
	

	public SysMenuService getSystemMenuService() {
		return systemMenuService;
	}

	public void setSystemMenuService(SysMenuService systemMenuService) {
		this.systemMenuService = systemMenuService;
	}

	private String cid;

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	@Override
	public String execute() throws Exception {
//		SystemMenu sysm = new SystemMenu();
//		sysm.setMenuText("menu1");
//		sysm.setActionPath("path1");
//		sysm.setExpanded(true);
//		sysm.setIconClass("icon1");
//		sysm.setLeaf(false);
//		sysm.setExtId("ext-menu1");
//		this.systemMenuService.add(sysm);
//		SystemMenu sysm2 = new SystemMenu();
//		sysm2.setMenuText("menu2");
//		sysm2.setActionPath("path2");
//		sysm2.setExpanded(true);
//		sysm2.setIconClass("icon2");
//		sysm2.setLeaf(false);
//		sysm2.setExtId("ext-menu2");
//		this.systemMenuService.add(sysm2);
//		SystemMenu sysm3 = new SystemMenu();
//		sysm3.setMenuText("menu3");
//		sysm3.setActionPath("path3");
//		sysm3.setExpanded(true);
//		sysm3.setIconClass("icon3");
//		sysm3.setLeaf(false);
//		sysm3.setExtId("ext-menu3");
//		this.systemMenuService.add(sysm3);
//		
//		SystemMenu sysm11 = new SystemMenu();
//		sysm11.setMenuText("menu11");
//		sysm11.setActionPath("path11");
//		sysm11.setExpanded(false);
//		sysm11.setIconClass("icon11");
//		sysm11.setLeaf(true);
//		sysm11.setExtId("ext-menu11");
//		sysm11.setSystemMenu(sysm);
//		this.systemMenuService.add(sysm11);
//		
//		SystemMenu sysm12 = new SystemMenu();
//		sysm12.setMenuText("menu12");
//		sysm12.setActionPath("path12");
//		sysm12.setExpanded(false);
//		sysm12.setIconClass("icon12");
//		sysm12.setLeaf(false);
//		sysm12.setExtId("ext-menu12");
//		sysm12.setSystemMenu(sysm);
//		this.systemMenuService.add(sysm12);
//		
//		SystemMenu sysm121 = new SystemMenu();
//		sysm121.setMenuText("menu121");
//		sysm121.setActionPath("path121");
//		sysm121.setExpanded(false);
//		sysm121.setIconClass("icon121");
//		sysm121.setLeaf(true);
//		sysm121.setExtId("ext-menu121");
//		sysm121.setSystemMenu(sysm12);
//		this.systemMenuService.add(sysm121);
//		
//		SystemMenu sysm21 = new SystemMenu();
//		sysm21.setMenuText("menu21");
//		sysm21.setActionPath("path21");
//		sysm21.setExpanded(false);
//		sysm21.setIconClass("icon21");
//		sysm21.setLeaf(true);
//		sysm21.setExtId("ext-menu21");
//		sysm21.setSystemMenu(sysm2);
//		this.systemMenuService.add(sysm21);
//		
//		SystemMenu sysm22 = new SystemMenu();
//		sysm22.setMenuText("menu22");
//		sysm22.setActionPath("path22");
//		sysm22.setExpanded(false);
//		sysm22.setIconClass("icon22");
//		sysm22.setLeaf(true);
//		sysm22.setExtId("ext-menu22");
//		sysm22.setSystemMenu(sysm2);
//		this.systemMenuService.add(sysm22);
//		
//		SystemMenu sysm31 = new SystemMenu();
//		sysm31.setMenuText("menu31");
//		sysm31.setActionPath("path31");
//		sysm31.setExpanded(false);
//		sysm31.setIconClass("icon31");
//		sysm31.setLeaf(false);
//		sysm31.setExtId("ext-menu31");
//		sysm31.setSystemMenu(sysm3);
//		this.systemMenuService.add(sysm31);
//		
//		SystemMenu sysm311 = new SystemMenu();
//		sysm311.setMenuText("menu311");
//		sysm311.setActionPath("path311");
//		sysm311.setExpanded(false);
//		sysm311.setIconClass("icon311");
//		sysm311.setLeaf(false);
//		sysm311.setExtId("ext-menu311");
//		sysm311.setSystemMenu(sysm31);
//		this.systemMenuService.add(sysm311);
//		
//		SystemMenu sysm3111 = new SystemMenu();
//		sysm3111.setMenuText("menu3111");
//		sysm3111.setActionPath("path3111");
//		sysm3111.setExpanded(false);
//		sysm3111.setIconClass("icon3111");
//		sysm3111.setLeaf(true);
//		sysm3111.setExtId("ext-menu3111");
//		sysm3111.setSystemMenu(sysm311);
//		this.systemMenuService.add(sysm3111);
		this.systemMenuService.executeTransactional(new TransactionalCallBack() {
			
			@Override
			public Object execute(IService service) {
				List l = systemMenuService.findByProperty("leaf", true);
				Set set = new HashSet();
				for (Iterator iterator = l.iterator(); iterator.hasNext();) {
					SysMenu o = (SysMenu) iterator.next();
					set.add(o);
					SysMenu p = o.getSysMenu();
					while(p != null){
						set.add(p);
						p = p.getSysMenu();
					}
				}
				Debug.println(l.size());
				Debug.println(set.size());
				for (Iterator iterator = set.iterator(); iterator.hasNext();) {
					SysMenu object = (SysMenu) iterator.next();
					Debug.println(object.getMenuText());
				}
				
				return null;
			}
		});
		
		return SUCCESS;
	}

}
