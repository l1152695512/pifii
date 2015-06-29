package com.yf.base.actions.sys.menus;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.yf.base.db.vo.SysMenu;
import com.yf.base.service.SysMenuService;
import com.yf.constants.SystemConstants;
import com.yf.data.base.PageList;
import com.yf.ext.base.BaseGridDataResultAction;
import com.yf.service.IService;
import com.yf.util.JsonUtils;
import com.yf.util.JsonUtils.JsonHelper;

public class GridData extends BaseGridDataResultAction<SysMenu, String> {
	
	private SysMenuService sysMenuService;

	private String menuId;
	private String menuText;
	
	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuText() {
		return menuText;
	}

	public void setMenuText(String menuText) {
		this.menuText = menuText;
	}


	@Override
	protected IService<SysMenu, String> _getService() {
		return this.sysMenuService;
	}

	@Override
	protected JsonHelper _getJsonHelper() {
		return JsonUtils.getJsonHelper().setDateFormat(dateFormat)
				.excludeForeignObject(SysMenu.class);
	}

	@Override
	public  PageList getPageList(IService<SysMenu, String> service){
		int currentPage = start/limit + 1;
		DetachedCriteria  dc=service.createCriteria();
		if(!StringUtils.isBlank(this.menuId)){
			dc.add(Restrictions.like("extId", this.menuId,MatchMode.ANYWHERE));	
		}
		if(!StringUtils.isBlank(this.menuText)){
			dc.add(Restrictions.like("menuText", this.menuText,MatchMode.ANYWHERE));
			
		}
		if(sort != null && dir != null){
			dc.addOrder(SystemConstants.SORT_ASC.equals(dir)?Order.asc(sort):Order.desc(sort));	
		}
		return service.findByDetachedCriteriaByPage(dc, limit, currentPage);
				
	}

	public SysMenuService getSysMenuService() {
		return sysMenuService;
	}

	public void setSysMenuService(SysMenuService sysMenuService) {
		this.sysMenuService = sysMenuService;
	}

}
