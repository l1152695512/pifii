package com.yf.base.actions.sys.action;

import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.yf.base.db.vo.SysAction;
import com.yf.base.service.SysActionService;
import com.yf.ext.base.BaseAction;

public class SaveForm extends  BaseAction {
	
	public String getSaid() {
		return said;
	}
	public void setSaid(String said) {
		this.said = said;
	}
	private SysActionService sysActionService;
	private String said;

	private String remards;
	private String name;
	
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


	public String getRemards() {
		return remards;
	}
	public void setRemards(String remards) {
		this.remards = remards;
	}
	@Override
	public String execute() throws Exception {
		try{
			SysAction sysAction ;
			if(!StringUtils.isBlank(this.said)){
				sysAction = this.sysActionService.findById(said);
				if(sysAction == null){
					this.msg="保存失败,ID为" + said + "权限不存在";
					return "failure";
				}
				this.msg="成功更新权限!";
				BeanUtils.copyProperties(sysAction, this);
				this.sysActionService.update(sysAction);
			}else {
				if(this.sysActionService.findByProperty("name", name).size() != 0){
					this.msg="保存失败,该权限已存在!";
					return "failure";
				}
				sysAction=new SysAction();
				BeanUtils.copyProperties(sysAction, this);
				sysAction.setCreateTime(new Date());
				this.sysActionService.add(sysAction);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return super.execute();
	}

}
