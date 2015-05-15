package com.yinfu.business.application.model;

import java.util.List;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DbUtil;

/**
 * @author JiaYongChao
 *最新优惠
 */
@TableBind(tableName = "bp_funny" )
public class Funny extends Model<Funny> {

	private static final long serialVersionUID = 1L;
	
	public static Funny dao = new Funny();

	//@formatter:off 
	/**
	 * Title: FunnyList
	 * Description:最新优惠列表
	 * Created On: 2014年8月21日 下午8:14:11
	 * @author JiaYongChao
	 * <p>
	 * @param shopId
	 * @param name
	 * @return 
	 */
	//@formatter:on
	public List<Record> funnyList(String shopId, String name) {
		String sql ="select id,shop_id,title,img,ifnull(txt,'') txt,create_date from bp_funny where shop_id="+shopId+" and delete_date is null ";
		if(name!=null && !name.equals("")){
//			sql+=" and title like '%"+name+"%'";
			sql+="and LOCATE('"+DbUtil.replaceSqlStr(name)+"',CONCAT(title,txt)) > 0 ";
		}
		List<Record> list = Db.find(sql);
		return list;
	}
	
}
