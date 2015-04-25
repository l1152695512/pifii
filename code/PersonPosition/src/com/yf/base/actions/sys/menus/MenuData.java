package com.yf.base.actions.sys.menus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.auth.SystemUser;
import com.yf.base.db.vo.SysActionItem;
import com.yf.base.db.vo.SysMenu;
import com.yf.base.db.vo.SysUser;
import com.yf.base.db.vo.SysUsergroup;
import com.yf.base.db.vo.SysUsergroupAction;
import com.yf.base.db.vo.SysUsergroupMenus;
import com.yf.base.db.vo.SysUsergroupUser;
import com.yf.base.service.SysActionItemService;
import com.yf.base.service.SysMenuService;
import com.yf.base.service.SysUserService;
import com.yf.constants.SystemConstants;
import com.yf.data.base.TransactionalCallBack;
import com.yf.service.IService;


public class MenuData extends ActionSupport implements ServletRequestAware {

	private SysMenuService sysMenuService;
	private SysUserService sysUserService;
	private SysActionItemService sysActionItemService;
	private String jsonString;
	
	public String getJsonString() {
		return jsonString;
	}
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
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
		}};

	@Override
	public String execute() throws Exception {
	     try{
	    	
			 this.sysMenuService.executeTransactional(new TransactionalCallBack() {

					@Override
					public Object execute(IService service) {
						try {
							executeDoing();
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						return null;
					}
				});
				return super.execute();
	    	 
	     }catch(Exception e){
	    	 e.printStackTrace();
	     }
	     return super.execute();

	}
	private void executeDoing(){

		SystemUser sysuser = (SystemUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String uid = sysuser.getUserId();
		SysUser user = sysUserService.findById(uid);
		List authMenus = null;
		if(!"admin".equals(sysuser.getUsername())){
			authMenus=getMenusList(user);
			getPowerList(user);
		}else{
			authMenus = sysMenuService.getAll();
		}
		
		Map powerActionMap=new HashMap();

		JSONArray array = new JSONArray();
		List <SysMenu>list =  sysMenuService.findByDetachedCriteria(sysMenuService.createCriteria().add(Restrictions.isNull("sysMenu")));
		processTreeNode(array,list,authMenus);
		jsonString = array.toString();
		
	}

	private List getMenusList(SysUser user){
		List<SysUsergroupUser>sysUsergroupUserList=user.getSysUsergroupUsers();
		SysUsergroup  sysUsergroup;//用户组
		Map powerActionMap=new HashMap();
		List<SysMenu> list=new ArrayList();
		for (SysUsergroupUser sysUsergroupUser : sysUsergroupUserList) {
			sysUsergroup=sysUsergroupUser.getSysUsergroup();
			 List <SysUsergroupMenus> sysUsergroupMenusList=sysUsergroup.getSysUsergroupMenuss();//用户组菜单
		    for (SysUsergroupMenus sysUsergroupMenus : sysUsergroupMenusList) {
		    	list.add(sysUsergroupMenus.getSysMenu());
			}	
		}
		return list;
	}
	
	private void getPowerList(SysUser user) {
		List<SysUsergroupUser> sysUsergroupUserList = user.getSysUsergroupUsers();
		List<SysUsergroupAction> sysUsergroupActionList;
		List<SysActionItem> sysActionItemList;
		SysUsergroup sysUsergroup;// 用户组
		Map powerActionMap = new HashMap();
		Map AllpowerActionMap = new HashMap();
		for (SysUsergroupUser sysUsergroupUser : sysUsergroupUserList) {
			sysUsergroup = sysUsergroupUser.getSysUsergroup();
			sysUsergroupActionList = sysUsergroup.getSysUsergroupActions();
			for (SysUsergroupAction sysUsergroupAction : sysUsergroupActionList) {
				sysActionItemList = sysUsergroupAction.getSysAction().getSysActionItems();
				for (SysActionItem sysActionItem : sysActionItemList) {
					 if(StringUtils.isNotEmpty(sysActionItem.getPath())){
						 powerActionMap.put(sysActionItem.getPath().trim(), "1");		
					 }
				}
			}
		}
		List<SysActionItem> actionItemList = sysActionItemService.getAll();
		for (SysActionItem sysActionItem : actionItemList) {
			 if(StringUtils.isNotEmpty(sysActionItem.getPath())){
				 AllpowerActionMap.put(sysActionItem.getPath().trim(), "0");		
			 }
		}
		ActionContext.getContext().getSession().put(SystemConstants.SESSION_POWER_MAP,powerActionMap);
		ActionContext.getContext().getSession().put(SystemConstants.SESSION_ALL_POWER_MAP,AllpowerActionMap);
		
	}
	
	
//	private List getMenusListByUser(SysUser user){
//		List<SysUsergroupUser>sysUsergroupUserList=user.getSysUsergroupUsers();
//		List<SysUsergroupAction> sysUsergroupActionList;
//		List <SysActionItem>sysActionItemList;
//		SysUsergroup  sysUsergroup;//用户组
//		Map powerActionMap=new HashMap();
//		for (SysUsergroupUser sysUsergroupUser : sysUsergroupUserList) {
//			sysUsergroup=sysUsergroupUser.getSysUsergroup();
//			sysUsergroupActionList=sysUsergroup.getSysUsergroupActions();
//		    for (SysUsergroupAction sysUsergroupAction : sysUsergroupActionList) {
//		    	sysActionItemList=sysUsergroupAction.getSysAction().getSysActionItems();
//		    	for (SysActionItem sysActionItem : sysActionItemList) {
//		    		if(sb.indexOf("start"+sysActionItem.getPath()+"end")<0)
//		    			sb.append("start"+sysActionItem.getPath()+"end");
//		    		    //把该权限路径保存在会话中,以便在拦截器对该action路径进行处理
//		    		powerActionMap.put(sysActionItem.getPath().trim(), "1");
//				}
//			}	
//		}
//		this.request.getSession().setAttribute(SystemConstants.SESSION_POWER_MAP, powerActionMap);
//		List <SysMenu>sysMenuList=this.sysMenuService.getAll();
//		List<SysMenu> list=new ArrayList();
//		for (SysMenu sysMenu : sysMenuList) {
//			if(sysMenu.getActionPath()==null||sysMenu.getActionPath().trim().equals("")){		
//				//具有该菜单的子权限
//				if(isHasChilrd(sysMenu)){
//					list.add(sysMenu);	
//				}		
//			}else if(sb.indexOf("start"+sysMenu.getActionPath()+"end")>=0){
//					list.add(sysMenu);			
//			}
//		}
//
//		return list;
//	}
	//是否存在子菜单
//	private boolean isHasChilrd(SysMenu parentSysMenu){
//		boolean checkBoolean=false;
//		List <SysMenu>list=parentSysMenu.getSysMenus();
//		if(list==null||list.size()==0)return false;
//		for (SysMenu sysMenu : list) {
//			String actionPath=sysMenu.getActionPath();
//			if(actionPath==null||actionPath.equals("")){
//				checkBoolean=isHasChilrd(sysMenu);
//			}
//			else{
//				if(sb.indexOf("start"+sysMenu.getActionPath()+"end")>=0)
//					checkBoolean=true;
//			}
//			if(checkBoolean)return true;
//		}
//		return false;
//		
//	}
	private void processTreeNode(JSONArray array , List menus ,List authMenus){
		
		Collections.sort(menus,comp);
		for (Iterator iterator = menus.iterator(); iterator.hasNext();) {
			SysMenu bean = (SysMenu) iterator.next();
			if(bean.getEnabled()!=null && !bean.getEnabled() && authMenus.contains(bean)){
				JSONObject json = new JSONObject();
				String extId = bean.getExtId();
				if(StringUtils.isBlank(extId)){
					json.put("id", "menu"+bean.getSystemMenuId());
				}else{
					json.put("id", extId);
				}
				json.put("text", StringUtils.isBlank(bean.getMenuText())?"(未设置)":bean.getMenuText());
				if(bean.getExpanded())json.put("expanded", true);
				
				if(bean.getIsGrey() == null || !bean.getIsGrey()) json.put("disabled", false);

				if(bean.getIsGrey() != null && bean.getIsGrey()) json.put("disabled", true);
				
				
//				if(bean.getLeaf() && bean.getSysMenus().size() == 0)
//				{
//					json.put("leaf", true);
//				}
//				else
//				{
//					json.put("leaf", false);
//				}
				json.put("leaf", bean.getLeaf());
				
				if(StringUtils.isNotBlank(bean.getActionPath()))json.put("action", bean.getActionPath());
				if(StringUtils.isNotBlank(bean.getIconClass()))json.put("iconCls", bean.getIconClass());
				if(bean.getSysMenus().size()>0){
					JSONArray children = new JSONArray();
					processTreeNode(children ,bean.getSysMenus(),authMenus);
					json.put("children", children);
				}
				
				array.add(json);
			}
		}
	}

	public SysMenuService getSysMenuService(){
		return sysMenuService;
	}

	public void setSysMenuService(SysMenuService sysMenuService) {
		this.sysMenuService = sysMenuService;
	}

	public SysUserService getSysUserService() {
		return sysUserService;
	}

	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

	public SysActionItemService getSysActionItemService() {
		return sysActionItemService;
	}
	public void setSysActionItemService(SysActionItemService sysActionItemService) {
		this.sysActionItemService = sysActionItemService;
	}
	public Comparator<SysMenu> getComp() {
		return comp;
	}

	public void setComp(Comparator<SysMenu> comp) {
		this.comp = comp;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		
	}




}
