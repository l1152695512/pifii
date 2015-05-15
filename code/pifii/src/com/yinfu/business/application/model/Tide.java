package com.yinfu.business.application.model;

import java.util.List;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DbUtil;
/**
 * @author JiaYongChao
 *潮机推荐
 */
@TableBind(tableName = "bp_tide" )
public class Tide extends Model<Tide> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Tide dao = new Tide();
	//@formatter:off 
	/**
	 * Title: tideList
	 * Description:
	 * Created On: 2014年8月21日 下午9:09:34
	 * @author JiaYongChao
	 * <p>
	 * @param shopId
	 * @param name
	 * @return 
	 */
	//@formatter:on
	public List<Record> tideList(String shopId, String name) {
		String sql ="select id,shop_id,name,img,ifnull(des,'') des,price,ifnull(picdes,'') picdes,preprice,create_date from bp_tide where shop_id="+shopId+" and delete_date is null ";
		if(name!=null && !name.equals("")){
//			sql+=" and name like '%"+name+"%'";
			sql+="and LOCATE('"+DbUtil.replaceSqlStr(name)+"',CONCAT(name,des,picdes)) > 0 ";
		}
		List<Record> list = Db.find(sql);
		return list;
	}
}
