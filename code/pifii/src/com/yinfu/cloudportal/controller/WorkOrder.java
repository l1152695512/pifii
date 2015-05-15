package com.yinfu.cloudportal.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.jfinal.aop.Before;
import com.jfinal.ext.DbExt;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.cloudportal.RouterInterceptor;
import com.yinfu.cloudportal.utils.Comms;
import com.yinfu.cloudportal.utils.CommsPhoneCode;
import com.yinfu.cloudportal.utils.SessionParams;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.Sec;
import com.yinfu.jbase.util.Send;
import com.yinfu.routersyn.task.SynAllTask;
import com.yinfu.routersyn.util.SynUtils;

/**
 * 这里要注意图片资源路径，认证平台上是没有图片资源的，需要加载商户平台上的资源
 * @author l
 *
 */
@Before(RouterInterceptor. class)
@ControllerBind(controllerKey = "/portal/mb/workorder", viewPath = "/portal/mb/workOrder")
public class WorkOrder extends Controller<Record>{
	private static Logger logger = Logger.getLogger(WorkOrder.class);
	
	public void index(){
		Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
		StringBuffer sql = new StringBuffer();
		sql.append("select wo.wo_id,so.name org_name,ifnull(bd.value,'') trde,ifnull(s.sn,'') shop_sn,ifnull(u.phone,'') tel,ifnull(s.addr,'') addr,s.name shop_name,");
		sql.append("wo.wo_state,if(wo.wo_state=1,'派单中','完成') state,if(s.is_trde_cust=1,'集团客户','行业客户') trde_type,d.location device_location ");
		sql.append("from bp_work_order wo join bp_shop s on (wo.shop_id=s.id) ");
		sql.append("join sys_org so on (s.org_id=so.id) ");
		sql.append("join bp_device d on (d.router_sn=? and s.id=d.shop_id) ");
		sql.append("join system_user u on (s.owner=u.id) ");
		sql.append("join bp_dictionary bd on (s.trde=bd.id) ");
		Record workOrderInfo = Db.findFirst(sql.toString(), new Object[]{device.get("router_sn")});
		if(null == workOrderInfo){
			workOrderInfo = new Record();
		}
		workOrderInfo.set("router_sn", device.getStr("router_sn"));
		setAttr("workOrderInfo", workOrderInfo);
//		setAttr("deviceInfo", getShopDevice(device.get("shop_id")));
		render("workOrder.jsp");
	}
	
	private Record getWorkOrderInfo(Object woId){
		StringBuffer sql = new StringBuffer();
		sql.append("select wo.wo_id,so.name org_name,ifnull(bd.value,'') trde,ifnull(s.sn,'') shop_sn,ifnull(u.phone,'') tel,ifnull(s.addr,'') addr,s.name shop_name,");
		sql.append("wo.wo_state,if(wo.wo_state=1,'派单中','完成') state,if(s.is_trde_cust=1,'集团客户','行业客户') trde_type ");
		sql.append("from bp_work_order wo join bp_shop s on (wo.shop_id=s.id) ");
		sql.append("join sys_org so on (s.org_id=so.id) join system_user u on (s.owner=u.id) ");
		sql.append("join bp_dictionary bd on (s.trde=bd.id) ");
		sql.append("where wo.wo_id=? ");
		return Db.findFirst(sql.toString(), new Object[]{woId});
	}
	
	
	public void doneWorkOrder(){
		final Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		final Map<String,Object> returnValue = new HashMap<String,Object>();
		boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
			try{
				Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
				if(StringUtils.isBlank(device.get("router_sn").toString()) && StringUtils.isNotBlank(getPara("sn"))){//防止出现session超时
					device.set("router_sn", getPara("sn"));
				}
				String workOrderId = getPara("workOrderId");
				Record deviceShop = Db.findFirst("select shop_id from bp_device where router_sn=?", new Object[]{device.get("router_sn")});
				if(null != deviceShop){
					if(null == deviceShop.get("shop_id") || StringUtils.isBlank(deviceShop.get("shop_id").toString())){
						StringBuffer sql = new StringBuffer();
						sql.append("select wo.wo_state,wo.shop_id,ifnull(s.lng,'') lng,ifnull(s.lat,'') lat,ifnull(s.location,'') location,u.name ");
						sql.append("from bp_work_order wo join bp_shop s on (wo.shop_id=s.id) ");
						sql.append("join system_user u on (s.`owner`=u.id) ");
						sql.append("where wo.wo_id=? ");
						Record myWorkOrder = Db.findFirst(sql.toString(),new Object[]{workOrderId});
						if(null != myWorkOrder){
							if("2".equals(myWorkOrder.get("wo_state").toString())){//工单已完成
								returnValue.put("msg", "该工单已完成，不能进行此操作！");
							}else{
								Object lng = "";
								Object lat = "";
								Object location = "";
								if(null == getPara("lng") || StringUtils.isBlank(getPara("lng"))){
									lng = myWorkOrder.get("lng");
									lat = myWorkOrder.get("lat");
									location = myWorkOrder.get("location");
								}else{
									lng = getPara("lng");
									lat = getPara("lat");
									location = getPara("location");
								}
								if(StringUtils.isBlank(lng.toString())){//如果即没有定位到盒子地址又没有设置商铺地址则提示无法绑定盒子
									returnValue.put("msg", "无法初始化盒子地址（请检查工单中设置的商铺地址是否正确）！");
									return false;
								}
								Object shopId = myWorkOrder.get("shop_id");
								Db.update("update bp_device d join bp_work_order wo on (d.router_sn=? and wo.wo_id=?) set d.shop_id=wo.shop_id,d.lng=?,d.lat=?,d.location=? ", 
										new Object[]{device.getStr("router_sn"),workOrderId,lng,lat,location});
								if("1".equals(getPara("useMe"))){
									Db.update("update bp_shop set lng=?,lat=?,location=? where id=? ", 
											new Object[]{getPara("lng"),getPara("lat"),getPara("location"),shopId});
								}
								Record thisDevice = Db.findFirst("select type from bp_device where router_sn=?", new Object[]{device.getStr("router_sn")});
								if(null != thisDevice){
									String fieldName = "";
									if(null!= thisDevice.get("type") && "1".equals(thisDevice.get("type").toString())){
										fieldName = "router_active_num";
									}else if(null!= thisDevice.get("type") && "2".equals(thisDevice.get("type").toString())){
										fieldName = "ap_active_num";
									}
									if(StringUtils.isNotBlank(fieldName)){
										Db.update("update bp_work_order set "+fieldName+"="+fieldName+"+1 where wo_id=? ", new Object[]{workOrderId});
									}
								}
								List<String> sqls = deleteDeviceSynTask(device.getStr("router_sn"));
								if(sqls.size() > 0){
									DbExt.batch(sqls);
								}
								//如果商铺的portal页面发布了则执行下发任务
								List<Record> synList = new ArrayList<Record>();
								synList.add(new Record().set("router_sn", device.getStr("router_sn")));
								sqls.clear();
								Record taskInfo = new Record().set("task_desc", "发布Portal页面");
								Map<String,List<File>> res = SynAllTask.synRes(shopId, sqls, taskInfo, null, synList);
								if(null != res){
									SynUtils.putAllFiles(deleteRes, res);
									if(sqls.size() > 0){
										DbExt.batch(sqls);
									}
									setSessionAttr(SessionParams.ACCESS_DEVICE, Comms.getDeviceInfo(device.getStr("router_sn")));
									//查找用户是否已发送了帐号及密码信息，如果已发送，就不需要再次发送（每次发送会重置用户的密码）
									Record account = Db.findFirst("select send_account,ifnull(phone,'') phone from system_user where name=?", new Object[]{myWorkOrder.get("name")});
									if(null != account && (null == account.get("send_account") || "0".equals(account.get("send_account").toString()))
											&& account.getStr("phone").length() >6){
										String thisPhone = account.get("phone").toString();
										String password = thisPhone.substring(thisPhone.length()-6);
										Db.update("update system_user set send_account=1,pwd=? where name=? ", new Object[]{Sec.md5(password),myWorkOrder.get("name")});
										sendAccountMessage(thisPhone,password);//发送帐号信息
									}
									returnValue.put("success", "true");
								}else{
									returnValue.put("msg", "初始化盒子内容失败，稍后请重试！");
									return false;
								}
							}
						}else{
							returnValue.put("msg", "该工单不存在，不能进行此操作！");
						}
					}else{
						returnValue.put("msg", "该设备已激活，不能重复操作！");
						returnValue.put("cmd", "refresh");
					}
				}else{
					returnValue.put("msg", "该盒子未入库，请联系平台入库！");
				}
				return true;
			}catch(Exception e){
				e.printStackTrace();
				returnValue.put("msg", "操作失败稍后请重试！");
				return false;
			}
		}});
		if(isSuccess){
			SynUtils.deleteRes(deleteRes.get("success"));
		}else{
			SynUtils.deleteRes(deleteRes.get("fail"));
		}
		renderJson(returnValue);
	}
	
	private boolean sendAccountMessage(String phone,String password){
		Pattern p = Pattern.compile(CommsPhoneCode.PATTERN);  
		Matcher m = p.matcher(phone); 
		if(m.matches()){
			try {
				return Send.SMS(phone,phone,password);
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("帐号密码发送异常---->【phone="+phone+",msg="+e.getMessage()+"】",e);
			} 
		}else{
			logger.warn("帐号密码发送失败---->【手机号码有误,phone="+phone+"】");
		}
		return false;
	}
	private String generAccountPassword(){
		String[] chars = new String[]{"0","1","2","3","4","5","6","7","8","9",
				"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q",
				"r","s","t","u","v","w","x","y","z"};//生成密码的字符
		StringBuffer passwordBuffer = new StringBuffer();
		Random rand =new Random();
		for(int i=0;i<4;i++){
			passwordBuffer.append(chars[rand.nextInt(chars.length)]);
		}
		return passwordBuffer.toString();
	}
	
	public void getWorkOrderInfo(){
		Record rec = new Record();
		if(null!=getPara("workOrderId")){
			Record workOrderInfo = getWorkOrderInfo(getPara("workOrderId"));
			if(null != workOrderInfo){
				rec.set("workOrderInfo", workOrderInfo);
			}else{
				rec.set("workOrderInfo", "");
			}
		}else{
			rec.set("workOrderInfo", "");
		}
		renderJson(rec);
	}
	
	private List<String> deleteDeviceSynTask(String routerSn){
		List<String> sqls = new ArrayList<String>();
		List<Record> listCmd = Db.find("select uid from bp_task where router_sn = ?",new Object[]{routerSn});
		sqls.add("delete from bp_cmd where uid in ("+DataOrgUtil.recordListToSqlIn(listCmd, "uid")+") ");
		sqls.add("delete from bp_task where router_sn = '"+routerSn+"' ");
		sqls.add("delete from bp_res_task where (left(task_type,4) != 'lua_' && left(task_type,5) != 'data_') && router_sn = '"+routerSn+"' ");//删除同步任务展示
		sqls.add("delete from bp_page_operate where router_sn = '"+routerSn+"' ");//删除同步任务展示
		return sqls;
	}
}
