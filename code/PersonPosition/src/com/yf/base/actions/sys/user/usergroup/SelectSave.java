package com.yf.base.actions.sys.user.usergroup;

import com.yf.base.db.vo.SysUser;
import com.yf.base.db.vo.SysUsergroup;
import com.yf.base.db.vo.SysUsergroupUser;
import com.yf.base.service.SysUserService;
import com.yf.base.service.SysUsergroupService;
import com.yf.base.service.SysUsergroupUserService;
import com.yf.ext.base.BaseAction;

public class SelectSave extends  BaseAction {
	
	private SysUsergroupUserService sysUsergroupUserService;
	private SysUsergroupService sysUsergroupService;
	private SysUserService sysUserService;
	private String userId;//用户组主键
	private String []ids;//主键
	@Override
	public String execute() throws Exception {
		try{
			SysUser  sysUser=this.sysUserService.findById(userId);
			SysUsergroupUser sysUsergroupUser=null;
			SysUsergroup sysUsergroup=null;
			for(int i=0;i<ids.length;i++){
				sysUsergroup=this.sysUsergroupService.findById(ids[i]);
				if(sysUser!=null){
					sysUsergroupUser=new SysUsergroupUser ();
					sysUsergroupUser.setSysUser(sysUser);
					sysUsergroupUser.setSysUsergroup(sysUsergroup);
					this.sysUsergroupUserService.add(sysUsergroupUser);
				}	
			}
		}catch(Exception e){
			e.printStackTrace();
			this.msg="保存失败,请稍后再试!";
			return "failure";
		}
		
		return super.execute();
	}
	public SysUsergroupUserService getSysUsergroupUserService() {
		return sysUsergroupUserService;
	}
	public void setSysUsergroupUserService(
			SysUsergroupUserService sysUsergroupUserService) {
		this.sysUsergroupUserService = sysUsergroupUserService;
	}
	public SysUsergroupService getSysUsergroupService() {
		return sysUsergroupService;
	}
	public void setSysUsergroupService(SysUsergroupService sysUsergroupService) {
		this.sysUsergroupService = sysUsergroupService;
	}
	public SysUserService getSysUserService() {
		return sysUserService;
	}
	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}


	

}
