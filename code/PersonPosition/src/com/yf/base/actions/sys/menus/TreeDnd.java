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
import com.yf.util.Debug;

public class TreeDnd extends ActionSupport {


	private SysMenuService sysMenuService;
	
	private String dragId;
	private String dropId;
	
	private String point;
	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}


	private List getTargetSiblingList(){
		List list = null;
		if(targetParent != null){
			list =  targetParent.getSysMenus();
		}else{
			list = this.sysMenuService.findByDetachedCriteria(sysMenuService.createCriteria().add(Restrictions.isNull("sysMenu")));
		}
		List result = new ArrayList();
		result.addAll(list);
		Collections.sort(result,comp);
		return result;
	}
	
	private List getSrcSiblingList(){
		List list = null;
		if(srcParent != null){
			list =  srcParent.getSysMenus();
		}else{
			list = this.sysMenuService.findByDetachedCriteria(sysMenuService.createCriteria().add(Restrictions.isNull("sysMenu")));
		}
		List result = new ArrayList();
		result.addAll(list);
		Collections.sort(result,comp);
		return result;
	}
	
	private SysMenu src;
	private SysMenu target;
	private SysMenu srcParent;
	private SysMenu targetParent;
	
	
	private boolean isMoveInSameParent(){
		if("append".equals(point)){
			return false;
		}else if("above".equals(point) || "below".equals(point)){
			if(target == null){//target == root
				if(srcParent == null){
					return true;
				}else{
					return false;
				}
			}else{
				if(srcParent == null && targetParent == null){
					return true;
				}else if(srcParent != null && targetParent !=null && srcParent.getSystemMenuId().equals(targetParent.getSystemMenuId())){
					return true;
				}else{
					return false;
				}
			}
		}
		throw new RuntimeException();
	}
	
	private boolean isNotNeedMove(){
		
		if("append".equals(point)){
			if(target == null){//target == root
				if(srcParent == null){
					return true;
				}else{
					return false;
				}
			}else{
				if(srcParent == null){
					return false;
				}else if(srcParent.getSystemMenuId().equals(target.getSystemMenuId())){
					return true;
				}else{
					return false;
				}
			}
		}else if("above".equals(point) || "below".equals(point)){
			if(target == null){//target == root
				throw new RuntimeException();
			}else{
				if(srcParent == null){
					if(targetParent == null){
						if("above".equals(point) ){
							if(src.getIndexValue()+1 == target.getIndexValue()){
								return true;
							}else{
								return false;
							}
						}else{
							if(src.getIndexValue()-1 == target.getIndexValue()){
								return true;
							}else{
								return false;
							}
						}
					}else{
						return false;
					}
				}else if(srcParent != null && targetParent != null 
						&& srcParent.getSystemMenuId().equals(targetParent.getSystemMenuId())){
					if("above".equals(point) ){
						if(src.getIndexValue()+1 == target.getIndexValue()){
							return true;
						}else{
							return false;
						}
					}else{
						if(src.getIndexValue()-1 == target.getIndexValue()){
							return true;
						}else{
							return false;
						}
					}
				}else{
					return false;
				}
			}
		}
		throw new RuntimeException();
	}
	
	
	private String msg;

	public String getMsg() {
		return msg;
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
			Debug.println(menu.getMenuText());
			Debug.println(" old index:"+menu.getIndexValue());
			menu.setIndexValue(index);
			Debug.println(" new index:"+menu.getIndexValue());
			index++;
		}
	}
	public String executeDoing(){
		
		if(dragId != dropId){
			src = sysMenuService.findById(dragId);
			if(src == null){
				msg = "拖动的对象不存在于数据库";
				return "failure";
			}
			srcParent = src.getSysMenu();
			target = sysMenuService.findById(dropId);
			if(target != null) targetParent = target.getSysMenu();
//			Debug.println("a");
			if(isNotNeedMove())return "success";
//			Debug.println("b");
			if(isMoveInSameParent()){
//				Debug.println("c");
				List list = getSrcSiblingList();
				list.remove(src);
//				Debug.println(list.indexOf(target));
				if("above".equals(point)){
					list.add(list.indexOf(target),src);
				}else{
					list.add(list.indexOf(target)+1,src);
				}
				Debug.println("reOrder list");
				reOrder(list);
			}else{
//				Debug.println("d");
				List srcList = getSrcSiblingList();
				srcList.remove(src);
				List targetList = getTargetSiblingList();
				int index = targetList.indexOf(target);
				if("above".equals(point)){
					targetList.add(index,src);
					src.setSysMenu(targetParent);
				}else if("below".equals(point)){
					targetList.add(index+1,src);
					src.setSysMenu(targetParent);
				}else{
					if(target!=null){
						targetList = new ArrayList();
						targetList.addAll(target.getSysMenus());
						Collections.sort(targetList,comp);
					}
					targetList.add(src);
					src.setSysMenu(target);
				}
				Debug.println("reOrder src list");
				reOrder(srcList);
				Debug.println("reOrder target list");
				reOrder(targetList);
			}
			sysMenuService.execute(new HibernateCallback() {
				
				@Override
				public Object doInHibernate(Session session) throws HibernateException,
						SQLException {
					if(srcParent != null)session.getSessionFactory().getCache().evictCollection(SysMenu.class.getName()+".sysMenus",srcParent.getSystemMenuId() );
					if(src.getSysMenu() != null)session.getSessionFactory().getCache().evictCollection(SysMenu.class.getName()+".sysMenus",src.getSysMenu().getSystemMenuId() );
					return null;
				}
			});
		}
		return "success";
	}
	
	@Override
	public String execute() throws Exception {
			return (String) this.sysMenuService.executeTransactional(new TransactionalCallBack() {
				
				@Override
				public Object execute(IService service) {
					return executeDoing();		
				}
			});
	}

	public SysMenuService getSysMenuService() {
		return sysMenuService;
	}

	public void setSysMenuService(SysMenuService sysMenuService) {
		this.sysMenuService = sysMenuService;
	}

	public String getDragId() {
		return dragId;
	}

	public void setDragId(String dragId) {
		this.dragId = dragId;
	}

	public String getDropId() {
		return dropId;
	}

	public void setDropId(String dropId) {
		this.dropId = dropId;
	}

	public SysMenu getSrc() {
		return src;
	}

	public void setSrc(SysMenu src) {
		this.src = src;
	}

	public SysMenu getTarget() {
		return target;
	}

	public void setTarget(SysMenu target) {
		this.target = target;
	}

	public SysMenu getSrcParent() {
		return srcParent;
	}

	public void setSrcParent(SysMenu srcParent) {
		this.srcParent = srcParent;
	}

	public SysMenu getTargetParent() {
		return targetParent;
	}

	public void setTargetParent(SysMenu targetParent) {
		this.targetParent = targetParent;
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

}
