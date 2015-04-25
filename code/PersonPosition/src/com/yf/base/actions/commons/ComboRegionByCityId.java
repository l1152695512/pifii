package com.yf.base.actions.commons;


import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.yf.base.db.vo.YwRegion;
import com.yf.base.service.YwRegionService;
import com.yf.data.base.TransactionalCallBack;
import com.yf.ext.base.BaseAction;
import com.yf.service.IService;
import com.yf.util.Debug;

public class ComboRegionByCityId extends BaseAction  {
	
	private YwRegionService ywRegionService;
	private String cityId;//城市编号
	@Override
	public String execute() throws Exception {
		try{
			
			TransactionalCallBack callBack=new TransactionalCallBack(){
				@Override
				public Object execute(IService arg0) {
					// TODO Auto-generated method stub
					executeDoing();
					return null;
				}
				
			};
			this.ywRegionService.executeTransactional(callBack);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "comboData";
	}
	public void executeDoing(){
	   
	    Debug.println("cityId==111=="+this.cityId);
	    JSONArray  arry=new JSONArray();
		JSONObject o=null;
	    if(!StringUtils.isBlank(this.cityId)){
	    	int index=this.cityId.indexOf(",");
	    	if(index>=0)this.cityId=this.cityId.substring(index+1, this.cityId.length()).trim();//奇怪，编辑选择是会把id1,id2,其中id1是上一次的，暂具如是处理
		    YwRegion ywregion=this.ywRegionService.findById(this.cityId);
		    if(ywregion!=null){
			    List<YwRegion>temp=ywregion.getYwRegions();
				
			    for (YwRegion ywRegion2 : temp) {
					o=new JSONObject();
		    		o.put("name" ,ywRegion2.getName());
		    		o.put("rid", ywRegion2.getRid());
		    		arry.add(o);
				}
		    }
	
	    }

		this.jsonString = arry.toString();
	}

	public YwRegionService getYwRegionService() {
		return ywRegionService;
	}
	public void setYwRegionService(YwRegionService ywRegionService) {
		this.ywRegionService = ywRegionService;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	

	

}
