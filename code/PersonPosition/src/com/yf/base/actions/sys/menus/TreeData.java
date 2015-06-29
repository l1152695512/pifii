package com.yf.base.actions.sys.menus;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.db.vo.SysMenu;
import com.yf.base.service.SysMenuService;
import com.yf.data.base.TransactionalCallBack;
import com.yf.service.IService;

public class TreeData extends ActionSupport {

	private SysMenuService sysMenuService;
	
	private String node;
	private String nodeId;
	private boolean checkbox;
	private boolean checked;
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isCheckbox() {
		return checkbox;
	}

	public void setCheckbox(boolean checkbox) {
		this.checkbox = checkbox;
	}

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
		 this.sysMenuService.executeTransactional(new TransactionalCallBack() {
			
			@Override
			public Object execute(IService service) {
				JSONArray array = new JSONArray();
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
				if(StringUtils.isBlank(node)||node.equals("0")){
					List list =  sysMenuService.findByDetachedCriteria(sysMenuService.createCriteria().add(Restrictions.isNull("sysMenu")));
					Collections.sort(list,comp);
					processTreeNode(array,list);
				}else{
					SysMenu menu = sysMenuService.findById(node);
					List list = menu.getSysMenus();
					Collections.sort(list,comp);
					processTreeNode(array,list);
				}
				jsonString = array.toString();
				return null;
			}
		});
		return super.execute();
	}

	private void processTreeNode(JSONArray array , List depts){
		for (Iterator iterator = depts.iterator(); iterator.hasNext();) {
			SysMenu bean = (SysMenu) iterator.next();
			
			JSONObject json = new JSONObject();
			json.put("nodeType","async" );
			//json.put("checked",false);
			json.put("id", bean.getSystemMenuId());
			if(checkbox)json.put("checked",checked);
			json.put("text", StringUtils.isBlank(bean.getMenuText())?"(未设置)":bean.getMenuText());
			int size = bean.getSysMenus().size();
			
			if(bean.getExpanded() && !bean.getLeaf()){
				json.put("expanded", true);
			}
			if(bean.getLeaf() && size == 0)json.put("leaf", true);
			if(StringUtils.isNotBlank(bean.getActionPath()))json.put("action", bean.getActionPath());
			if(StringUtils.isNotBlank(bean.getIconClass()))json.put("iconCls", bean.getIconClass());
			array.add(json);
		}
	}

	public SysMenuService getSysMenuService() {
		return sysMenuService;
	}

	public void setSysMenuService(SysMenuService sysMenuService) {
		this.sysMenuService = sysMenuService;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	

}
