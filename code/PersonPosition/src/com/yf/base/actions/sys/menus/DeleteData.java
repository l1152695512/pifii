package com.yf.base.actions.sys.menus;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.db.vo.SysMenu;
import com.yf.base.service.SysMenuService;
import com.yf.data.base.TransactionalCallBack;
import com.yf.service.IService;

public class DeleteData extends ActionSupport {


	private SysMenuService sysMenuService;


	public SysMenuService getSysMenuService() {
		return sysMenuService;
	}

	public void setSysMenuService(SysMenuService sysMenuService) {
		this.sysMenuService = sysMenuService;
	}

	private String id;
	
	private String msg;
	
	
	public String getMsg() {
		return msg;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Comparator<SysMenu> getComp() {
		return comp;
	}

	public void setComp(Comparator<SysMenu> comp) {
		this.comp = comp;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}



	private Comparator<SysMenu> comp =  new Comparator<SysMenu>(){

		@Override
		public int compare(SysMenu o1, SysMenu o2) {
			int com1 = 0;
			int com2 = 0;
			if(o1.getIndexValue()!=null){
				com1 = o1.getIndexValue();
			}
			if(o2.getIndexValue()!=null){
				com2 = o2.getIndexValue();
			}
			return com1-com2;
		}
	};
	
	private void reOrder(List oldList) {
		short index = 1;
		for (Iterator iterator = oldList.iterator(); iterator
				.hasNext();) {
			SysMenu menu = (SysMenu) iterator.next();
			menu.setIndexValue(index++);
			this.sysMenuService.update(menu);
		}
	}
	
	@Override
	public String execute() throws Exception {
		try {
			this.sysMenuService.executeTransactional(new TransactionalCallBack() {
				@Override
				public Object execute(IService service) {
					return deleteDoing();	
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		return super.execute();
	}
	private String deleteDoing(){

		final SysMenu menu = sysMenuService.findById(id);
		if(menu == null){
			msg = "找不到id 为" + id +"的菜单项";
			return "failure";
		}
		List siblings  = getSiblingList(menu);
		reOrder(siblings);
		sysMenuService.delete(menu);
		sysMenuService.execute(new HibernateCallback() {	
			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				SysMenu m = menu.getSysMenu();
				if(m!=null)session.getSessionFactory().getCache().evictCollection(SysMenu.class.getName()+".sysMenus", m.getSystemMenuId());
				return null;
			}
		});
		return "success";
	
		
	}
	private List getSiblingList(SysMenu menu){
		List list = null;
		SysMenu parent = menu.getSysMenu();
		if(parent != null){
			list =  parent.getSysMenus();
		}else{
			list = this.sysMenuService.findByDetachedCriteria(sysMenuService.createCriteria().add(Restrictions.isNull("sysMenus")));
		}
		List result = new ArrayList();
		result.addAll(list);
		result.remove(menu);
		Collections.sort(result,comp);
		return result;
	}
	
}
