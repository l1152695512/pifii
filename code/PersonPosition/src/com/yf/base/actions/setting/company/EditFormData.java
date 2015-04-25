package com.yf.base.actions.setting.company;

import net.sf.json.JSONObject;

import com.yf.base.db.vo.YwCompany;
import com.yf.base.service.YwCompanyService;
import com.yf.constants.SystemConstants;
import com.yf.data.base.TransactionalCallBack;
import com.yf.ext.base.BaseAction;
import com.yf.service.IService;
import com.yf.util.JsonUtils;

public class EditFormData extends BaseAction {

	private YwCompanyService ywCompanyService;
	private String cid;
	
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
			e.printStackTrace();

		}
		return "formData";

	}

	public void executeDoing() {
		YwCompany ywCompany = this.ywCompanyService.findById(this.cid);
		if (ywCompany == null) {
			this.msg = "该公司不存在!";
			return ;
		}
		JSONObject record = JsonUtils.getJsonHelper().setDateFormat(
				SystemConstants.STANDARD_DATE_FORMAT)
				.excludeForeignObject(YwCompany.class)
				.toJSONObject(ywCompany);

		record.put("rid", ywCompany.getYwRegion()==null?"":ywCompany.getYwRegion().getRid());


		jsonString = record.toString();

	}

	public YwCompanyService getYwCompanyService() {
		return ywCompanyService;
	}

	public void setYwCompanyService(YwCompanyService ywCompanyService) {
		this.ywCompanyService = ywCompanyService;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

}
