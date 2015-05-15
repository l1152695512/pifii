
package com.yinfu.business.device.model;

import java.util.ArrayList;
import java.util.List;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.kit.StringKit;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.model.dataTables.DataTablesModel;

/**
 * 设备实体
 * 
 * @author JiaYongChao 2014年7月23日
 */
@TableBind(tableName = "bp_device")
public class Device extends Model<Device> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Device dao = new Device();
	
	//@formatter:off 
	/**
	 * Title: getDeviceInfo
	 * Description:根据商铺ID获得设备信息
	 * Created On: 2014年7月23日 上午9:58:37
	 * @author JiaYongChao
	 * <p>
	 * @param shopId
	 * @return 
	 */
	//@formatter:on
	public List<Device> getDeviceInfo(String shopId) {
		String sql = "select * from bp_device t where t.delete_date is null and 1=1 and shop_id=" + shopId;
		return find(sql);
	}

	
}
