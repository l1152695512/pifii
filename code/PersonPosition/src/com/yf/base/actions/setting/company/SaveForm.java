package com.yf.base.actions.setting.company;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.yf.base.db.vo.YwCompany;
import com.yf.base.db.vo.YwRegion;
import com.yf.base.service.YwCompanyService;
import com.yf.base.service.YwRegionService;
import com.yf.data.base.TransactionalCallBack;
import com.yf.ext.base.BaseAction;
import com.yf.service.IService;

public class SaveForm extends BaseAction {

	private YwCompanyService ywCompanyService;
	private YwRegionService ywRegionService;
	private String rid; 
	private String cid;
	private String cname;
	private String category;
	private String deptNo;
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	@Override
	public String execute() throws Exception {
		try {
			this.ywCompanyService
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

			if(StringUtils.isBlank(this.getCid())){
				YwCompany  ywCompany=new YwCompany();	
				BeanUtils.copyProperties(ywCompany, this);
				if(StringUtils.isNotBlank(this.rid)){
					YwRegion  ywRegion=this.ywRegionService.findById(rid);
					ywCompany.setYwRegion(ywRegion);
				}
				ywCompany.setDeptNo(getGrenrateDeptNo());
				this.ywCompanyService.add(ywCompany);
			}
			else{
				YwCompany  ywCompany=this.ywCompanyService.findById(this.getCid());
				BeanUtils.copyProperties(ywCompany, this);
				if(StringUtils.isNotBlank(this.rid)){
					YwRegion  ywRegion=this.ywRegionService.findById(rid);
					ywCompany.setYwRegion(ywRegion);
				}
				
				this.ywCompanyService.update(ywCompany);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String getGrenrateDeptNo()
	{
		DetachedCriteria dc = this.ywCompanyService.createCriteria();
		dc.add(Restrictions.like("deptNo", this.deptNo+"__"));
		
		String dno = "";
		long maxno = 0l;
		
		List list = this.ywCompanyService.findByDetachedCriteria(dc);
		if(!list.isEmpty())
		{
			maxno = Long.parseLong(((YwCompany)list.get(0)).getDeptNo());
			dno = ((YwCompany)list.get(0)).getDeptNo();
			
			for(int i = 0; i < list.size(); i++)
			{
				YwCompany yc = (YwCompany)list.get(i);
				long tempNo = Long.parseLong(yc.getDeptNo());
				String tempStr = yc.getDeptNo();
				if(tempNo>maxno)
				{
					dno = tempStr;
				}
			}
			
			String realNo = dno.substring(this.deptNo.length());
			if(Integer.parseInt(realNo) < 9)
			{
				dno = this.deptNo+"0" + (Integer.parseInt(realNo) + 1);
			}
			else
			{
				dno = this.deptNo+ (Integer.parseInt(realNo) + 1);
			}
			
		}
		else
		{
			dno = this.deptNo+"01";
		}
		
		return dno;
	}
	
	
	public YwCompanyService getYwCompanyService() {
		return ywCompanyService;
	}
	public void setYwCompanyService(YwCompanyService ywCompanyService) {
		this.ywCompanyService = ywCompanyService;
	}
	public YwRegionService getYwRegionService() {
		return ywRegionService;
	}
	public void setYwRegionService(YwRegionService ywRegionService) {
		this.ywRegionService = ywRegionService;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	
}
