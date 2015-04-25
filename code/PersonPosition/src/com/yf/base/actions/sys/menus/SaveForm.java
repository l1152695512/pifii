package com.yf.base.actions.sys.menus;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.yf.base.db.vo.SysMenu;
import com.yf.base.service.SysMenuService;
import com.yf.data.base.TransactionalCallBack;
import com.yf.service.IService;

public class SaveForm extends ActionSupport implements ModelDriven<SysMenu> {
	
	private SysMenu menu = new SysMenu();
	
	private SysMenuService sysMenuService;
	

	public SysMenuService getSysMenuService() {
		return sysMenuService;
	}
	public void setSysMenuService(SysMenuService sysMenuService) {
		this.sysMenuService = sysMenuService;
	}


	
	private String parentId;


	
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}



	private String errorMsg;
	
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	@Override
	public String execute() throws Exception {
		return (String)sysMenuService.executeTransactional(new TransactionalCallBack() {
			
			@Override
			public Object execute(IService service) {
				if(!StringUtils.isBlank(menu.getSystemMenuId())){
					//this.closeId = "editSystemMenuFormTab" + menu.getSystemMenuId();
					SysMenu temp = sysMenuService.findById(menu.getSystemMenuId());
					if(temp == null){
						errorMsg="保存失败,ID为" + menu.getSystemMenuId() + "的菜单项不存在";
						return "failure";
					}
					List l  = sysMenuService.findByProperty("systemMenuId", menu.getSystemMenuId());
					if(l.size() > 0){
						SysMenu duplicate = (SysMenu)l.get(0);
						if(!duplicate.getSystemMenuId().equals(temp.getSystemMenuId())){
							errorMsg="保存失败,MENU ID" + menu.getExtId() + " 与[ " + duplicate.getMenuText() +"]冲突";
							return "failure";
						}
					}
					//不在提交的form里面包含的参数，要自己设置回去。因为update的对象是新的对象，而不是从数据库里面加载出来的。
					//if(menu.getSystemMenuId() != parentId)menu.setSystemMenu(systemMenuService.findById(parentId));
					//menu.setIndexValue(temp.getIndexValue());
//					if(temp.getSystemMenus().size()>0){//如果有值列表，那就不是叶节点
//						menu.setLeaf(false);
//					}
					
					temp.setActionPath(menu.getActionPath());
					temp.setEnabled(menu.getEnabled());
					temp.setExpanded(menu.getExpanded());
					//temp.setExtId(menu.getExtId());
					temp.setIconClass(menu.getIconClass());
					//temp.setLayoutAttr(menu.getLayoutAttr());
					temp.setLeaf(menu.getLeaf());
					temp.setMenuText(menu.getMenuText());
					temp.setIsGrey(menu.getIsGrey());
					temp.setShortcuts(menu.getShortcuts());
					sysMenuService.update(temp);
				}else {
					//this.closeId = "newSystemMenuFormTab";
					List list  = sysMenuService.findByProperty("systemMenuId", menu.getSystemMenuId());
					if(list.size() > 0){
						SysMenu duplicate = (SysMenu)list.get(0);
						errorMsg="保存失败,MENU ID" + menu.getExtId() + " 与 [" + duplicate.getMenuText() +"]冲突";
						return "failure";
					}
					if(StringUtils.isBlank(parentId)||parentId.trim().equals("0")){
						List l =  sysMenuService.findByDetachedCriteria(sysMenuService.createCriteria().add(Restrictions.isNull("sysMenu")));
						menu.setIndexValue((short)(l.size()+1));
						
					}else{

						SysMenu parent= sysMenuService.findById(parentId);
						List l = parent.getSysMenus();
						menu.setIndexValue((short)(l.size()+1));
						menu.setSysMenu(parent);
						parent.setLeaf(false);
						sysMenuService.update(parent);
					}
					sysMenuService.add(menu);
				}
				sysMenuService.execute(new HibernateCallback() {
					
					@Override
					public Object doInHibernate(Session session) throws HibernateException,
							SQLException {
						//Debug.println(parentId);
						session.getSessionFactory().getCache().evictCollection(SysMenu.class.getName()+".sysMenus", parentId);
						return null;
					}
				});
				return "success";
			}
		});
		
	}
	@Override
	public SysMenu getModel() {
		return menu;
	}
	public SysMenu getMenu() {
		return menu;
	}
	public void setMenu(SysMenu menu) {
		this.menu = menu;
	}


}
