package com.yf.base.actions.setting.region;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.yf.base.db.vo.YwRegion;
import com.yf.base.service.YwRegionService;
import com.yf.data.base.TransactionalCallBack;
import com.yf.ext.base.BaseAction;
import com.yf.service.IService;

public class SaveForm extends BaseAction {

	private YwRegionService ywRegionService;
	private String pid; 
	private String rid;
	private String name;
	private String code;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String execute() throws Exception {
		try {
			this.ywRegionService
					.executeTransactional(new TransactionalCallBack() {
						@Override
						public Object execute(IService arg0) {
							executeDoing();
							return null;
						}
					});

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "failure";
		}
		return "success";
	}
	public void executeDoing(){
		try{
			if(StringUtils.isBlank(this.getRid())){
				YwRegion  ywRegion=new YwRegion();	
				BeanUtils.copyProperties(ywRegion, this);
				if(StringUtils.isNotBlank(this.pid)){
					YwRegion  parent=this.ywRegionService.findById(pid);
					ywRegion.setYwRegion(parent);
				}
				this.ywRegionService.add(ywRegion);
			}
			else{
				YwRegion  ywRegion=this.ywRegionService.findById(this.getRid());
				BeanUtils.copyProperties(ywRegion, this);
	
				this.ywRegionService.update(ywRegion);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public YwRegionService getYwRegionService() {
		return ywRegionService;
	}
	public void setYwRegionService(YwRegionService ywRegionService) {
		this.ywRegionService = ywRegionService;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	
	
}
