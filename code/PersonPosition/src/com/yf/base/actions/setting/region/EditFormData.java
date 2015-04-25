package com.yf.base.actions.setting.region;

import net.sf.json.JSONObject;

import com.yf.base.db.vo.YwRegion;
import com.yf.base.service.YwRegionService;
import com.yf.constants.SystemConstants;
import com.yf.data.base.TransactionalCallBack;
import com.yf.ext.base.BaseAction;
import com.yf.service.IService;
import com.yf.util.JsonUtils;

public class EditFormData extends BaseAction {

	
	private YwRegionService ywRegionService;
	private String rid;
	
	@Override
	public String execute() throws Exception {
		try {
			this.ywRegionService.executeTransactional(new TransactionalCallBack() {
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
		YwRegion ywRegion = this.ywRegionService.findById(this.rid);
		if (ywRegion == null) {
			this.msg = "该区载不存在!";
			return ;
		}
		JSONObject record = JsonUtils.getJsonHelper().setDateFormat(
				SystemConstants.STANDARD_DATE_FORMAT)
				.excludeForeignObject(YwRegion.class)
				.toJSONObject(ywRegion);
		
		record.put("pname", ywRegion.getYwRegion()==null?"无父区域":ywRegion.getYwRegion().getName());
	

		jsonString = record.toString();

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

	

}
