package com.yf.base.actions.setting.usergroup.menus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.criterion.Restrictions;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.db.vo.SysMenu;
import com.yf.base.db.vo.SysUsergroup;
import com.yf.base.db.vo.SysUsergroupMenus;
import com.yf.base.service.SysMenuService;
import com.yf.base.service.SysUsergroupService;
import com.yf.data.base.TransactionalCallBack;
import com.yf.service.IService;

public class TreeData extends ActionSupport {

	private SysMenuService sysMenuService;
	private String sugid;//用户组主键
	private SysUsergroupService sysUsergroupService;
	private List<SysMenu>sysMenuList=new ArrayList<SysMenu>();
	private String jsonString;
	public SysMenuService getSystemMenuService() {
		return sysMenuService;
	}

	public void setSystemMenuService(SysMenuService sysMenuService) {
		this.sysMenuService = sysMenuService;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	@Override
	public String execute() throws Exception {
		try {
			 this.sysMenuService.executeTransactional(new TransactionalCallBack() {	
					@Override
					public Object execute(IService service) {
						executeDoing();
						return null;
					}
				});
				//return super.execute();
				return "data";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "data";
	}
	
	public void executeDoing(){
		
		Comparator<SysMenu> comp =  new Comparator<SysMenu>(){
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
			}};
			SysUsergroup sysUsergroup=sysUsergroupService.findById(sugid);		
			List <SysUsergroupMenus> tempList=sysUsergroup.getSysUsergroupMenuss();
		
			if(tempList!=null&&tempList.size()>0){
				for (SysUsergroupMenus temp : tempList) {
					sysMenuList.add(temp.getSysMenu());	
				}	
			}
			List list =  sysMenuService.findByDetachedCriteria(sysMenuService.createCriteria().add(Restrictions.isNull("sysMenu")));
			Collections.sort(list,comp);			
		    jsonString = buildTree(list).toString();
		   // Debug.println("abcd====="+jsonString);
	}
	private JSONArray buildTree(List list){
		JSONArray array = new JSONArray();	
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			SysMenu sysMenu = (SysMenu) iterator.next();
			JSONObject object = new JSONObject();	
			object.put("text", sysMenu.getMenuText());
			object.put("id", sysMenu.getSystemMenuId());
			object.put("checked", sysMenuList.contains(sysMenu));
			if(sysMenu.getSysMenus().size()>0){
				object.put("expanded", true);		
				object.put("children", buildTree(sysMenu.getSysMenus()));//构建机楼子树
			}
			else{
				object.put("expanded", true);
				object.put("leaf", true);
				
			}
			array.add(object);
		
		}

		return array;
	}
	public SysMenuService getSysMenuService() {
		return sysMenuService;
	}

	public void setSysMenuService(SysMenuService sysMenuService) {
		this.sysMenuService = sysMenuService;
	}

	public String getSugid() {
		return sugid;
	}

	public void setSugid(String sugid) {
		this.sugid = sugid;
	}

	public SysUsergroupService getSysUsergroupService() {
		return sysUsergroupService;
	}

	public void setSysUsergroupService(SysUsergroupService sysUsergroupService) {
		this.sysUsergroupService = sysUsergroupService;
	}

	public List<SysMenu> getSysMenuList() {
		return sysMenuList;
	}

	public void setSysMenuList(List<SysMenu> sysMenuList) {
		this.sysMenuList = sysMenuList;
	}


	

}
