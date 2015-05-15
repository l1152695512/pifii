package com.yinfu.business.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;

public class PageUtil {
	 /**
	  * 同步数据
	  * @param shopId
	  * @param type（index_app：应用；index_adv：广告；index_shop:商店；index_temp：模板；index_page：发布;video:视频；book:阅读；apk:应用；
	  * 		preferential:最新优惠；tide：潮机推荐；flowpack：包子铺；introduce:商铺介绍）
	  * @param keyId
	  * @param operateType（1：增加；2：修改；3：删除；4：发布）
	  * @return
	  */
	public static boolean changPageLog(String shopId,String type,String keyId,String operateType){
//		if(isPagePublished(shopId)){
			List<String> devices = getDevice(shopId);
			final Object[][] params = new Object[devices.size()][5];
			for(int i=0;i<devices.size();i++){
				params[i] = new Object[]{devices.get(i),operateType,"0",type,keyId};
			}
			return Db.tx(new IAtom(){public boolean run() throws SQLException {
				int[] changeRows = Db.batch("insert into bp_page_operate(router_sn,operate_type,status,operate_date,type,key_id) values(?,?,?,now(),?,?)", params, params.length);
				for(int i=0;i<changeRows.length;i++){
					if(changeRows[i] < 1){
						return false;
					}
				}
				return true;
			}});
//		}else{
//			return true;
//		}
	}
	private static boolean isPagePublished(Object shopId){
		Record record = Db.findFirst("select is_publish from bp_shop_page where shop_id=? ", new Object[]{shopId});
		try{
			if("1".equals(record.getStr("is_publish"))){
				return true;
			}
		}catch(Exception e){
		}
		return false;
	}
	private static List<String> getDevice(Object shopId){
		List<String> devices = new ArrayList<String>();
		List<Record> Records = Db.find("select router_sn from bp_device where shop_id=? ", new Object[]{shopId});
		Iterator<Record> ite = Records.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			devices.add(rec.getStr("router_sn"));
		}
		return devices;
	}
	
	public static String getShopId(Object pageId){
		Record rec = Db.findFirst("select shop_id from bp_shop_page where id=? ", new Object[]{pageId});
		if(null != rec){
			return rec.getInt("shop_id")+"";
		}else{
			return "";
		}
	}
	public static Object getPageIdByShopId(Object shopId){
		Record rec = Db.findFirst("select id from bp_shop_page where shop_id=? ", new Object[]{shopId});
		if(null != rec){
			return rec.get("id");
		}else{
			return "";
		}
	}
	public static Object getTemplateId(Object shopId){
		Record rec = Db.findFirst("select template_id from bp_shop_page where shop_id=? ", new Object[]{shopId});
		if(null != rec){
			return rec.get("template_id");
		}else{
			return "";
		}
	}
	public static Object getTemplateIdByPageId(Object pageId){
		Record rec = Db.findFirst("select template_id from bp_shop_page where id=? ", new Object[]{pageId});
		if(null != rec){
			return rec.get("template_id");
		}else{
			return "";
		}
	}
}
