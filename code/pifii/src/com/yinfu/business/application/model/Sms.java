package com.yinfu.business.application.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Page;
import com.yinfu.jbase.jfinal.ext.Model;

@TableBind(tableName = "bp_sms")
public class Sms extends Model<Sms>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5177957883050303276L;
	public static Sms dao = new Sms();
	

	/**
	 *  根据shopId获取所有菜单
	 */
	public Page<Sms> getListByShopId(int pageNum, int pageSize, String shopId)
	{
		String sql = "select id,shop_id,send_type,title,content,send_num,send_date,status,create_date ";
		String sqlExceptSelect = " from bp_sms  where shop_id= "+shopId;
		
		Page<Sms> page = dao.paginate(pageNum, pageSize, sql, sqlExceptSelect.toString());
		return page;
	}
	
}
