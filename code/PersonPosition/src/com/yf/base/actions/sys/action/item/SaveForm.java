package com.yf.base.actions.sys.action.item;

import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.yf.base.db.vo.SysAction;
import com.yf.base.db.vo.SysActionItem;
import com.yf.base.service.SysActionItemService;
import com.yf.base.service.SysActionService;
import com.yf.ext.base.BaseAction;

public class SaveForm extends  BaseAction {
	
	private SysActionService sysActionService;
	private SysActionItemService sysActionItemService;
	private String said;
	private String saiid;
	private String path;
	private String name;

	public SysActionItemService getSysActionItemService() {
		return sysActionItemService;
	}
	public void setSysActionItemService(SysActionItemService sysActionItemService) {
		this.sysActionItemService = sysActionItemService;
	}

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SysActionService getSysActionService() {
		return sysActionService;
	}
	public void setSysActionService(SysActionService sysActionService) {
		this.sysActionService = sysActionService;
	}
	
	@Override
	public String execute() throws Exception {
		try{
			SysActionItem  sysActionItem;
			if(!StringUtils.isBlank(saiid)){
				sysActionItem = this.sysActionItemService.findById(saiid);
				if(sysActionItem == null){
					this.msg="保存失败,ID为" + saiid + "Action不存在";
					return "failure";
				}
				this.msg="成功更新权限Action!";
				BeanUtils.copyProperties(sysActionItem, this);
				this.sysActionItemService.update(sysActionItem);
			}else {
				
				SysAction sysAction=this.sysActionService.findById(said);
				sysActionItem=new SysActionItem();
				BeanUtils.copyProperties(sysActionItem, this);
				sysActionItem.setSysAction(sysAction);
				sysActionItem.setCreateTime(new Date());
				this.sysActionItemService.add(sysActionItem);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return super.execute();
	}
	public String getSaid() {
		return said;
	}
	public void setSaid(String said) {
		this.said = said;
	}
	public String getSaiid() {
		return saiid;
	}
	public void setSaiid(String saiid) {
		this.saiid = saiid;
	}


}
