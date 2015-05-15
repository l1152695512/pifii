
package com.yinfu.business.device.controller;

import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.DbExt;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.device.model.Device;
import com.yinfu.business.util.Utils;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.PropertyUtils;
import com.yinfu.jbase.util.remote.RouterHelper;
import com.yinfu.routersyn.task.SynAllTask;
import com.yinfu.routersyn.util.SynUtils;

@ControllerBind(controllerKey = "/business/device", viewPath = "/")
public class DeviceController extends Controller<Device> {
	private static List<String> limitFile = new ArrayList<String>();
	static{
		String limitFileStr = PropertyUtils.getProperty("route.file.list.limit", "/USB4(Pa1),/attach,/lost+found,/.thumb,/Data");
		String[] filesArray = limitFileStr.split(",");
		for(int i=0;i<filesArray.length;i++){
			limitFile.add(filesArray[i]);
		}
	}
	
	//@formatter:off 
	/**(non-Javadoc)
	 * Title: index
	 * Description: 进入设备首页
	 * Created On: 2014年8月8日 上午11:04:29
	 * @author JiaYongChao
	 * <p>
	 * @see com.yinfu.jbase.jfinal.ext.Controller#index()
	 */
	//@formatter:on
	public void toIndex(){
		render("page/business/device.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: getDeviceInfo
	 * Description:获得设备信息
	 * Created On: 2014年7月23日 上午9:56:06
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void getDeviceInfo() {
		String shopId = getPara("shopId");// 获得商铺Id
		renderJson(Device.dao.getDeviceInfo(shopId));
	}
	
	public void getDeviceSelect() {
		renderJson(Device.dao.find("select id,name from bp_device where shop_id=? and type=1 order by report_date desc ", new Object[]{getPara("shopId")}));
	}
	
	//@formatter:off 
	/**(non-Javadoc)
	 * Title: add
	 * Description: 增加设备
	 * Created On: 2014年7月23日 上午10:13:02
	 * @author JiaYongChao
	 * <p>
	 * @see com.yinfu.jbase.jfinal.ext.Controller#add()
	 */
	//@formatter:on
	public void add() {
		renderJsonResult(getModel().save());
	}
	
	//@formatter:off 
	/**(non-Javadoc)
	 * Title: edit
	 * Description: 修改设备信息
	 * Created On: 2014年7月23日 上午10:13:22
	 * @author JiaYongChao
	 * <p>
	 * @see com.yinfu.jbase.jfinal.ext.Controller#edit()
	 */
	//@formatter:on
	public void edit() {
		Device device = getModel();
		if (device.getId() != null) {
			
		}
	}
	public void modifyMarker(){
		Record rec = Db.findFirst("select marker from bp_device where router_sn=? ", new Object[]{getPara("sn")});
		if(null != rec){
			setAttr("marker", rec.get("marker"));
		}
		setAttr("sn", getPara("sn"));
		render("page/business/device/modifyMarker.jsp");
	}
	public void saveMarker(){
		Db.update("update bp_device set marker=? where router_sn=? ", new Object[]{getPara("marker"),getPara("sn")});
		renderJsonResult(true);
	}
	public void modifyName(){
		String namePrefix = PropertyUtils.getProperty("route.name.prefix", "");
		String nameSuffix = PropertyUtils.getProperty("route.name.suffix", "");
		Record shopType = Db.findFirst("select s.is_trde_cust,ifnull(d.name,'') name from bp_shop s join bp_device d on (s.id=d.shop_id and d.router_sn=?)", new Object[]{getPara("sn")});
		if(null != shopType.get("is_trde_cust")
				&& "0".equals(shopType.get("is_trde_cust").toString())){
			setAttr("name_prefix", namePrefix);
			setAttr("name_suffix", nameSuffix);
			String name = shopType.get("name");
			if(name.startsWith(namePrefix)){
				name = name.substring(namePrefix.length());
			}
			if(name.endsWith(nameSuffix)){
				name = name.substring(0,name.length()-nameSuffix.length());
			}
			setAttr("name", name);
		}else{
			setAttr("name_prefix", "");
			setAttr("name_suffix", "");
			setAttr("name", shopType.get("name"));
		}
		setAttr("sn", getPara("sn"));
		render("page/business/device/modifyName.jsp");
	}
	private String getRouterName(String name){
		Record shopType = Db.findFirst("select s.is_trde_cust,ifnull(d.name,'') name from bp_shop s join bp_device d on (s.id=d.shop_id and d.router_sn=?)", new Object[]{getPara("sn")});
		if(null != shopType.get("is_trde_cust")
				&& "0".equals(shopType.get("is_trde_cust").toString())){
			String namePrefix = PropertyUtils.getProperty("route.name.prefix", "");
			String nameSuffix = PropertyUtils.getProperty("route.name.suffix", "");
			name = namePrefix+name+nameSuffix;
		}
		return name;
	}
	public void saveName(){
		//调用接口更改名称
		Record rec = Utils.getAccount(getPara("sn"));
		String name = getRouterName(getPara("name"));
		if(null != rec && !name.equals(rec.get("name"))){
			try{
				String token = RouterHelper.routerToken(rec.getStr("remote_account"), rec.getStr("remote_pass"));
				String loginResult = RouterHelper.sys_hostname_set(name,token);
				JSONObject obj = JSONObject.parseObject(loginResult);
				if(null != obj.get("status") && 
						("0".equals(obj.get("status").toString()) || 
								"1".equals(obj.get("status").toString()))){
					Db.update("update bp_device set name=? where router_sn=? ", new Object[]{name,getPara("sn")});
					renderJsonResult(true);
				}else{
					renderJson("msg", "由于网络问题，暂时无法设置名称，稍后请重试！");
				}
			}catch(Exception e){
				renderJson("msg", "暂时无法连接盒子，稍后请重试！");
			}
		}else{
			renderJsonResult(true);
		}
	}
	
	public void restart(){
		Record rec = Utils.getAccount(getPara("sn"));
		String loginResult = RouterHelper.reboot(RouterHelper.routerToken(rec.getStr("remote_account"), rec.getStr("remote_pass")));
		JSONObject obj = JSONObject.parseObject(loginResult);
		if(null != obj.get("status") && 
				("0".equals(obj.get("status").toString()) || 
				"1".equals(obj.get("status").toString()))){
			int interval = 0-PropertyUtils.getPropertyToInt("route.uploadInterval", 600)-1;//路由上报数据的时间间隔,要考虑网络延迟
			Db.update("update bp_device set report_date = date_add(now(), interval ? second) where router_sn=? ", 
					new Object[]{interval,getPara("sn")});
			renderJsonResult(true);
		}else{
			renderJson("msg", "由于网络问题，暂时无法重启，稍后请重试！");
		}
	}
	
	public void listFilePage(){
		setAttr("sn", getPara("sn"));
		render("page/business/device/fileList.jsp");
	}
	
	public void listFile(){
		if(canAccess(getPara("path"))){
			Record rec = Utils.getAccount(getPara("sn"));
			try{
				String result = RouterHelper.fileList(getPara("path"),RouterHelper.routerToken(rec.getStr("remote_account"), rec.getStr("remote_pass")));
				System.err.println("result="+result);
				JSONObject obj = JSONObject.parseObject(result);
				if(null != obj && null!=obj.getJSONArray("contents")){
					List<Record> returnFiles = new ArrayList<Record>();
					JSONArray files = obj.getJSONArray("contents");
					Iterator<Object> ite = files.iterator();
					while(ite.hasNext()){
						Record thisFile = new Record();
						JSONObject file = JSONObject.parseObject(ite.next().toString());
						String thisPath = file.get("path").toString();
						if(canAccess(thisPath)){
							returnFiles.add(thisFile.set("path", thisPath).set("name", thisPath.substring(thisPath.lastIndexOf("/")+1))
									.set("modified", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(file.getLong("modified_lts")*1000))));
							if(file.getBooleanValue("is_dir")){
								thisFile.set("icon", "images/business/device/folder.png");
								thisFile.set("size", "");
							}else{
								thisFile.set("size", file.get("size"));
								thisFile.set("icon", "images/business/device/file.png");
							}
						}
					}
					renderJson(returnFiles);
				}else if(null != obj && null!=obj.get("gateway")){
					renderJson("msg", "请求超时，盒子可能没有连接网络！");
				}else{
					renderJson("msg", "目录不存在！");
				}
			}catch(NullPointerException e){
				renderJson("msg", "暂时无法访问盒子，稍后请重试！");
			}catch(Exception e){
				renderJson("msg", "目录不存在！");
			}
		}else{
			renderError(401);
		}
	}
	private boolean canAccess(String folder){
		if(null == folder){
			return false;
		}
		for(int i=0;i<limitFile.size();i++){
			if(folder.startsWith(limitFile.get(i))){
				return false;
			}
		}
		return true;
	}
	
	//@formatter:off 
	/**(non-Javadoc)
	 * Title: delete
	 * Description: 删除设备信息
	 * Created On: 2014年7月23日 上午10:13:39
	 * @author JiaYongChao
	 * <p>
	 * @see com.yinfu.jbase.jfinal.ext.Controller#delete()
	 */
	//@formatter:on
	public void delete() {
		
	}
	/**
	 * 按步骤创建路由主页的发布时使用，仅用于展示路由
	 */
	public void getShopDevice() {
		StringBuffer sql = new StringBuffer();
		sql.append("select d.name device_name,s.name shop_name ");
		sql.append("from bp_device d join bp_shop s on (s.id=? and d.shop_id = s.id) ");
		sql.append("order by d.create_date ");
		List<Record> apps = Db.find(sql.toString(), new Object[] { getPara("shopId") });
		renderJson(apps);
	}
	/**
	 * 用于菜单中点击设备时显示当前店铺的所有设备的信息及状态
	 */
	public void getShopDeviceWithStatus(){
		int interval = PropertyUtils.getPropertyToInt("route.uploadInterval", 600);//路由上报数据的时间间隔,要考虑网络延迟
		StringBuffer sql = new StringBuffer();
		sql.append("select d.router_sn,s.name shop_name,d.name,IF(date_add(d.report_date, interval "+interval+" second) > now(),d.online_num,-1) online_num,d.online_type,ifnull(d.marker,'') marker ");
		sql.append("from bp_device d join bp_shop s on (d.shop_id = s.id) ");
		sql.append("where d.shop_id = ? order by IF(date_add(d.report_date, interval "+interval+" second) > now(),1,0) desc,d.name ");
		List<Record> apps = Db.find(sql.toString(), new Object[]{getPara("shopId")});
		renderJson(apps);
	}
	
	public void getOnlinePerson() throws ParseException{
		SimpleDateFormat timeFm = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateTimeFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		int interval = PropertyUtils.getPropertyToInt("route.uploadInterval", 600);//路由上报数据的时间间隔,要考虑网络延迟
		StringBuffer sql = new StringBuffer();
//		sql.append("select host,mac,ip,substring(timediff(max(create_date), min(create_date)),1,8) time_length from (");
//		sql.append("select * from bp_report where sn=? and date(create_date)=date(now()) and date_add(create_date, interval ? second) > now() order by create_date desc");
//		sql.append(") a group by mac order by create_date desc ");
		sql.append("select distinct r1.host,r1.mac,r1.ip,date_format(r1.create_date,'%Y-%m-%d %H:%i:%s') create_date ");
		sql.append("from bp_report r1 join bp_report r2 on (r2.sn=? and r1.sn=r2.sn and date_add(r2.create_date, interval ? second) > now()) ");
		sql.append("where date(r1.create_date)=date(now()) and r1.mac=r2.mac ");
		sql.append("order by r1.mac,r1.create_date desc ");
		
		List<Record> persons = new ArrayList<Record>();
		List<Record> personsData = Db.find(sql.toString(), new Object[]{getPara("routersn"),interval});
		String currentMac = "";
		String previousDate = "";
		boolean isContinue = false;
		Iterator<Record> ite = personsData.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			String thisMac = rowData.getStr("mac");
			String thisDate = rowData.getStr("create_date");
			if(!currentMac.equals(thisMac)){
				isContinue = false;
			}else if(isContinue){
				continue;
			}
			boolean timeDifference = "".equals(previousDate)?true:(dateTimeFm.parse(previousDate).getTime()-dateTimeFm.parse(thisDate).getTime())>interval*1000?true:false;
			if(!currentMac.equals(thisMac) || timeDifference){
				if(persons.size() > 0){
					Record lastData = persons.get(persons.size() -1);
					if(lastData.get("time_length") == null){
//						System.err.println("开始时间："+previousDate+"，结束时间："+lastData.getStr("create_date"));
						lastData.set("time_length",timeFm.format(new Date(dateFm.parse(dateFm.format(new Date())).getTime()+(dateTimeFm.parse(lastData.getStr("create_date")).getTime()-dateTimeFm.parse(previousDate).getTime()))));
					}
				}
				if(!currentMac.equals(thisMac)){
					currentMac = thisMac;
					persons.add(rowData);
				}else if(timeDifference){//当天前一次的连接wifii信息
					isContinue = true;
				}
			}
			previousDate = thisDate;
			if(!ite.hasNext() && !isContinue){
				if(persons.size() > 0){
					Record lastData = persons.get(persons.size() -1);
					if(lastData.get("time_length") == null){
						lastData.set("time_length", timeFm.format(new Date(dateFm.parse(dateFm.format(new Date())).getTime()+(dateTimeFm.parse(lastData.getStr("create_date")).getTime()-dateTimeFm.parse(previousDate).getTime()))));
//						System.err.println("开始时间："+thisDate+"，结束时间："+lastData.getStr("create_date"));
					}
				}
			}
		}
		Object[] returnSortPerson = persons.toArray();
		Arrays.sort(returnSortPerson,new OnlineTimeComparator());
		renderJson(returnSortPerson);
	}
	
	public void reloadDataFile(){
		final Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
			boolean success = false;
			List<Record> devices = new ArrayList<Record>();
			devices.add(new Record().set("router_sn", getPara("sn")));
			List<String> sqls = new ArrayList<String>();
			Record taskInfo = new Record().set("task_desc", "重置Portal页面文件");
			Map<String,List<File>> res = SynAllTask.synRes(getPara("shopId"), sqls, taskInfo,null,devices);
			if(null != res){
				SynUtils.putAllFiles(deleteRes, res);
				if(sqls.size()>0){
					DbExt.batch(sqls);
				}
				success = true;
			}
			return success;
		}});
		if(isSuccess){
			SynUtils.deleteRes(deleteRes.get("success"));
		}else{
			SynUtils.deleteRes(deleteRes.get("fail"));
		}
		renderJsonResult(isSuccess);
	}
}
class OnlineTimeComparator implements Comparator<Object>{
	SimpleDateFormat timeFm = new SimpleDateFormat("HH:mm:ss");
    @Override
    public int compare(Object o1, Object o2) {
        try {
			if(timeFm.parse(((Record)o1).getStr("time_length")).after(timeFm.parse(((Record)o2).getStr("time_length")))){
				return -1;
			}else{
				return 1;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return 1;
    }
    
}
