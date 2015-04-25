package com.yf.base.actions.sys.usergroup;

import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.yf.base.db.vo.SysUsergroup;
import com.yf.base.service.SysUsergroupService;
import com.yf.ext.base.BaseAction;

public class SaveForm extends  BaseAction {
	
	private SysUsergroupService sysUsergroupService;
	
	private String sugid;
	private String name;
	private String remards;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
			SysUsergroup sysUsergroup ;
			if(!StringUtils.isBlank(this.sugid)){
				sysUsergroup = this.sysUsergroupService.findById(sugid);
				if(sysUsergroup == null){
					this.msg="保存失败,ID为" + sugid + "用户组不存在";
					return "failure";
				}
				this.msg="成功更新用户组信息!";
				BeanUtils.copyProperties(sysUsergroup, this);
				this.sysUsergroupService.update(sysUsergroup);
			}else {
			
				if(this.sysUsergroupService.findByProperty("name", name).size() != 0){
					this.msg="保存失败,该用户组已存在!";
					return "failure";
				}
				sysUsergroup=new SysUsergroup();
				BeanUtils.copyProperties(sysUsergroup, this);
				sysUsergroup.setCreateTime(new Date());
				this.sysUsergroupService.add(sysUsergroup);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return super.execute();
	}
	public SysUsergroupService getSysUsergroupService() {
		return sysUsergroupService;
	}
	public void setSysUsergroupService(SysUsergroupService sysUsergroupService) {
		this.sysUsergroupService = sysUsergroupService;
	}
	public String getSugid() {
		return sugid;
	}
	public void setSugid(String sugid) {
		this.sugid = sugid;
	}


}
