package com.yf.base.actions.sys.usergroup.menus;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.yf.base.db.vo.SysMenu;
import com.yf.base.db.vo.SysUsergroupMenus;
import com.yf.base.service.SysUsergroupMenusService;
import com.yf.constants.SystemConstants;
import com.yf.data.base.PageList;
import com.yf.ext.base.BaseGridDataResultAction;
import com.yf.service.IService;
import com.yf.util.JsonUtils;
import com.yf.util.JsonUtils.JsonHelper;

public class GridData extends BaseGridDataResultAction<SysUsergroupMenus, String> {

	private SysUsergroupMenusService sysUsergroupMenusService;
	private String sugid;
	private String name;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public SysUsergroupMenusService getSysUsergroupMenusService() {
		return sysUsergroupMenusService;
	}

	public void setSysUsergroupMenusService(
			SysUsergroupMenusService sysUsergroupMenusService) {
		this.sysUsergroupMenusService = sysUsergroupMenusService;
	}

	@Override
	protected IService<SysUsergroupMenus, String> _getService() {
		return this.sysUsergroupMenusService;
	}

	@Override
	protected JsonHelper _getJsonHelper() {
		return JsonUtils.getJsonHelper().setDateFormat(SystemConstants.STANDARD_DATE_FORMAT)
				.excludeForeignObject(SysUsergroupMenus.class);
	}

	@Override
	public  PageList getPageList(IService<SysUsergroupMenus, String> service){
		int currentPage = start/limit + 1;
		DetachedCriteria  dc=service.createCriteria();
		if(sort != null && dir != null){
			dc.addOrder(SystemConstants.SORT_ASC.equals(dir)?Order.asc(sort):Order.desc(sort));		
		}
		dc.createAlias("sysUsergroup", "sysUsergroup");
		dc.add(Restrictions.eq("sysUsergroup.sugid", this.sugid));	
		if(!StringUtils.isBlank(this.name)){
			dc.createAlias("sysMenu", "sysMenu");
			//dc.add(Restrictions.like("sysMenu.menuText", this.name,MatchMode.ANYWHERE));	
			dc.add(Restrictions.like("sysMenu.menuText", this.name,MatchMode.ANYWHERE));	
		}
		return service.findByDetachedCriteriaByPage(dc, limit, currentPage);
				
	}
	
	public String getSugid() {
		return sugid;
	}

	public void setSugid(String sugid) {
		this.sugid = sugid;
	}

	@Override
	protected void processRowJson(JSONObject json, Object rowObject) {
		SysUsergroupMenus c = (SysUsergroupMenus) rowObject;
		SysMenu sysMenu=c.getSysMenu();
		json.put("menuText",sysMenu.getMenuText());	
	    json.put("parentMenuText",sysMenu.getSysMenu()==null?"无上级菜单":sysMenu.getSysMenu().getMenuText());	    
	    
	}


}
