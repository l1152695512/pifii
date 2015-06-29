package com.yf.base.actions.mjgl;

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

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.auth.SystemUser;
import com.yf.base.db.vo.SysMenu;
import com.yf.base.db.vo.SysUser;
import com.yf.base.db.vo.SysUsergroup;
import com.yf.base.db.vo.SysUsergroupMenus;
import com.yf.base.db.vo.SysUsergroupUser;
import com.yf.base.service.SysMenuService;
import com.yf.base.service.SysUserService;
import com.yf.data.base.TransactionalCallBack;
import com.yf.service.IService;


public class ButtonView extends ActionSupport implements ServletRequestAware {

	private SysMenuService sysMenuService;
	private SysUserService sysUserService;
	private String jsonString;
	
	private HttpServletRequest request;

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
		List<SysUsergroupUser> sysUsergroupUserList = user.getSysUsergroupUsers();
		SysUsergroup sysUsergroup;//用户组
		Map powerActionMap = new HashMap();
		List<SysMenu> list = new ArrayList();
		for (SysUsergroupUser sysUsergroupUser : sysUsergroupUserList) {
			sysUsergroup = sysUsergroupUser.getSysUsergroup();
			List <SysUsergroupMenus> sysUsergroupMenusList = sysUsergroup.getSysUsergroupMenuss();//用户组菜单
		    for (SysUsergroupMenus sysUsergroupMenus : sysUsergroupMenusList) {
		    	list.add(sysUsergroupMenus.getSysMenu());
			}	
		}
		return list;
	}

	private void processTreeNode(JSONArray array , List menus ,List authMenus){
		
		Collections.sort(menus,comp);
		for (Iterator iterator = menus.iterator(); iterator.hasNext();) {
			SysMenu bean = (SysMenu) iterator.next();
			if(bean.getEnabled()!=null && !bean.getEnabled() && authMenus.contains(bean)){
				JSONObject json = new JSONObject();
				
				if(bean.getSysMenus().size()>0){
					processTreeNode(array ,bean.getSysMenus(),authMenus);
				}else{
					if(bean.getShortcuts()){
						String extId = bean.getExtId();
						if(StringUtils.isBlank(extId)){
							json.put("id", "menu"+bean.getSystemMenuId());
						}else{
							json.put("id", extId);
						}
						json.put("text", StringUtils.isBlank(bean.getMenuText())?"(未设置)":bean.getMenuText());
						json.put("action", StringUtils.isBlank(bean.getActionPath()) ? "" : bean.getActionPath());
						json.put("iconCls", StringUtils.isBlank(bean.getIconClass()) ? "" : bean.getIconClass());
						
						array.add(json);
					}
				}
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
