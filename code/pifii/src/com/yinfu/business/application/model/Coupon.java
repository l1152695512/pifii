package com.yinfu.business.application.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Page;
import com.yinfu.business.device.model.Device;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.model.dataTables.DataTablesModel;
import com.yinfu.model.jqgrid.JqGridModel;

/**
 * @author JiaYongChao
 *	优惠券实体类
 */
@TableBind(tableName = "bp_coupon" )
public class Coupon extends Model<Coupon> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static Coupon dao = new Coupon();


	//@formatter:off 
	/**
	 * Title: findByShopId
	 * Description:通过商铺ID获得优惠券列表
	 * Created On: 2014年7月30日 下午12:28:43
	 * @author JiaYongChao
	 * <p>
	 * @param pageNum
	 * @param pageSize
	 * @param shopId
	 * @return 
	 */
	//@formatter:on
	public Page<Coupon> findByShopId(int pageNum, int pageSize, String shopId) {
		String sql ="select t.id,t.companyname,t.shopname ,CASE t.type when 0 THEN '火锅' when 2 then '水煮' when 3 then '烹饪' end as type,t.description,t.icon,t.storename,t.validity   ";
		String sqlExceptSelect = " from bp_coupon t  where 1=1 and t.delete_date is null ";
		if(shopId!=null && StringUtils.isNotEmpty(shopId)){
			sqlExceptSelect+=" and t.shopId="+shopId;
		}
		Page<Coupon> page = dao.paginate(pageNum, pageSize, sql, sqlExceptSelect);
		return page;
	}


	//@formatter:off 
	/**
	 * Title: couponList
	 * Description:通过商铺ID获得优惠券列表
	 * Created On: 2014年8月9日 下午12:18:38
	 * @author JiaYongChao
	 * <p>
	 * @param name 优惠券名称
	 * @param shopId商铺ID
	 * @return 
	 */
	//@formatter:on
	public List<Coupon> couponList(String shopId, String name) {
		String sql ="select t.id,t.companyname,t.shopname,t.type,t.description,t.icon,t.storename,t.validity from bp_coupon t  where 1=1 and t.delete_date is null  and shopId ="+shopId;
		if(name!=null && !name.equals("")){
			sql+=" and t.description like '%"+name+"%'";
		}
		List<Coupon> list = dao.find(sql);
		return list;
	}
	
}
