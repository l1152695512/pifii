package com.yf.base.actions.sys.usergroup.menus;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.yf.base.db.vo.SysMenu;
import com.yf.base.db.vo.SysUsergroup;
import com.yf.base.db.vo.SysUsergroupMenus;
import com.yf.base.service.SysMenuService;
import com.yf.base.service.SysUsergroupService;
import com.yf.constants.SystemConstants;
import com.yf.data.base.PageList;
import com.yf.ext.base.BaseGridDataResultAction;
import com.yf.service.IService;
import com.yf.util.JsonUtils;
import com.yf.util.JsonUtils.JsonHelper;

public class SelectData extends BaseGridDataResultAction<SysMenu, String> {


	private SysMenuService sysMenuService;
	private SysUsergroupService sysUsergroupService;
	private String sugid;
	private String name;
	public String getSugid() {
		return sugid;
	}
	public void setSugid(String sugid) {
		this.sugid = sugid;
	}
	@Override
	protected IService<SysMenu, String> _getService() {
		return this.sysMenuService;
	}

	@Override
	protected JsonHelper _getJsonHelper() {
		return JsonUtils.getJsonHelper().setDateFormat(SystemConstants.STANDARD_DATE_FORMAT)
				.excludeForeignObject(SysMenu.class);
	}

	@Override
	public  PageList getPageList(IService<SysMenu, String> service){
		int currentPage = start/limit + 1;
		DetachedCriteria  dc=service.createCriteria();
		if(sort != null && dir != null){
			dc.addOrder(SystemConstants.SORT_ASC.equals(dir)?Order.asc(sort):Order.desc(sort));		
		}		
		SysUsergroup sysUsergroup=this.sysUsergroupService.findById(sugid);
		List <SysUsergroupMenus> tempList=sysUsergroup.getSysUsergroupMenuss();
		if(tempList!=null&&tempList.size()>0){
			List sysMenusList=new ArrayList();
			for (SysUsergroupMenus temp : tempList) {
				sysMenusList.add(temp.getSysMenu().getSystemMenuId());	
			}
			dc.add(Restrictions.not(Restrictions.in("systemMenuId", sysMenusList)));
		}
		if(!StringUtils.isBlank(this.name)){
			dc.add(Restrictions.like("menuText", this.name,MatchMode.ANYWHERE));	
		}
		return service.findByDetachedCriteriaByPage(dc, limit, currentPage);
				
	}
	
	@Override
	protected void processRowJson(JSONObject json, Object rowObject) {
		SysMenu c = (SysMenu) rowObject;
   
	    json.put("parentMenuText",c.getSysMenu()==null?"无上级菜单":c.getSysMenu().getMenuText());	    
	    
	}
	public SysUsergroupService getSysUsergroupService() {
		return sysUsergroupService;
	}
	public void setSysUsergroupService(SysUsergroupService sysUsergroupService) {
		this.sysUsergroupService = sysUsergroupService;
	}
	public SysMenuService getSysMenuService() {
		return sysMenuService;
	}

	public void setSysMenuService(SysMenuService sysMenuService) {
		this.sysMenuService = sysMenuService;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
