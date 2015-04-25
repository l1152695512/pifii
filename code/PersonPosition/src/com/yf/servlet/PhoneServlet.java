package com.yf.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

import com.huawei.uniportal.device.smp.util.Base64;
import com.yf.base.actions.mapposition.datastatistics.warn.Commons;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

import freemarker.template.Configuration;

public class PhoneServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(PhoneServlet.class);
	private static int gps2BaiDuTimeOut = 30 * 1000;//半分钟

	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private Configuration configuration;

	@Override
	public void init() {
		configuration = new Configuration();
		configuration.setServletContextForTemplateLoading(getServletContext(),
				"freemarker");
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/*
		 * Map<String,Object> data = new HashMap<String,Object>(); String
		 * message = "欢迎访问"; data.put("message", message); Template t =
		 * configuration.getTemplate("welcome.ftl");
		 * response.setContentType("text/html;charset=" + t.getEncoding());
		 * Writer out = response.getWriter(); try { t.process(data, out); }
		 * catch (TemplateException e) { e.printStackTrace(); }
		 */
//		updateWifiLocation(request.getParameter("wifi"));
		try {
			String requestCmd = request.getParameter("requestCmd");
			if("UploadTruTalkData".equals(requestCmd)){//定时上传位置信息的请求
				insertPhoneLocation(request.getParameter("requestData"));
			}else if("UploadRegInfo".equals(requestCmd)){//开机数据上传，可以回复数据来设置手机的定位上传的参数，可创建表保存每个人员的定位时间
				String sql = "select start_width_system,start_time,end_time,is_basestation,is_read_only,period,sensibility from bp_phone_upload_seeting_tbl limit 1 ";
				List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
				String startWidthSystem = "1";
				String startTime = "08:00";
				String endTime = "18:00";
				String isBasestation = "1";
				String isReadOnly = "0";
				String period = "300";
				String sensibility = "0";
				if(dataList.size()>0){
					Map<String,Object> data = dataList.get(0);
					startWidthSystem = null==data.get("start_width_system")?startWidthSystem:data.get("start_width_system").toString();
					startTime = null==data.get("start_time")?startTime:data.get("start_time").toString().substring(0, 5);
					endTime = null==data.get("end_time")?endTime:data.get("end_time").toString().substring(0, 5);
					isBasestation = null==data.get("is_basestation")?isBasestation:data.get("is_basestation").toString();
					isReadOnly = null==data.get("is_read_only")?isReadOnly:data.get("is_read_only").toString();
					period = null==data.get("period")?period:data.get("period").toString();
					sensibility = null==data.get("sensibility")?sensibility:data.get("sensibility").toString();
				}
				response.getWriter().println("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
						"<TruTalkData>\n" +
							"<RetTruTalkData>\n" +
								"<Result>8888</Result>\n" +
								"<NextMessage>0</NextMessage>\n" +
								"<TimeStamp>1390530336241</TimeStamp>\n" +
							"</RetTruTalkData>\n" +
							"<GPSService>\n" +
								"<BStaRun>"+startWidthSystem+"</BStaRun>\n" +//是否开机启动
								"<EndHour>"+endTime.substring(0, 2)+"</EndHour>\n" +//定位上传截止时间（小时）
								"<EndMinute>"+endTime.substring(3, 5)+"</EndMinute>\n" +//定位上传截止时间（分钟）
								"<ISBasestation>"+isBasestation+"</ISBasestation>\n" +//是否启用基站定位
								"<IsReadOnly>"+isReadOnly+"</IsReadOnly>\n" +//是否只读
								"<Period>"+period+"</Period>\n" +//定位周期（秒）
								"<Sensibility>"+sensibility+"</Sensibility>\n" +//灵敏度
								"<StartHour>"+startTime.substring(0, 2)+"</StartHour>\n" +//定位上传开始时间（小时）
								"<StartMinute>"+startTime.substring(3, 5)+"</StartMinute>\n" +//定位上传开始时间（分钟）
							"</GPSService>\n" +
						"</TruTalkData>\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 信息结构:
			CodeSign=None,
			TruTalkLatlng=Number,YYMMDDHHMMSS,Longitude,Latitude,Speed,Aspect,State,Mileage,QState,Isbasestation,Exp
			名称				说明			值示例			描述
			CodeSign		编码标识		None			详见编码标识列表 
			TruTalkLatlng	信息标识						定位数据标识
			Number			终端标识		18917611537		终端标识即手机号码, 如号码为空则传IMSI.
			YYMMDDHHMMSS	时间标识		122727200000	终端定位时间，格式：年月日时分秒
			Longitude		经度标识		1142730152		单位千万分之一度.  该值=经度*10000000正数代表东经,负数代表西经
			Latitude		纬度标识		311009269		单位千万分之一度.  该值=纬度*10000000正数代表北纬,负数代表南纬
			Speed			速度标识		100				单位:km/h(公里/小时) 整数. 该值=单位*100
			Aspect			方向标识		100				该值=以正北为0度,顺时针偏移的度数/2*100
			State			状态			0				0代表普通.暂时无其它. 该值=状态*100
			Mileage			里程			100				单位米,整数.   该值=里程*100
			QState			QChat状态	400				QChat状态, 详见列表 该值=状态*100
			Isbasestation	定位标识		0				0代表卫星定位，1代表基站辅助定位
			Exp 			扩展标识		0				扩展标识

		数据格式示例：
		　　CodeSign=NONE,TruTalkLatlng=18917611537,121227200000,1142730152,311009269,100,100,0,100,400,0,0
		　　CodeSign=BASE64,VHJ1VGFsa0xhdGxuZz0xODkxNzYxMTUzNywxMjEyMjcyMDAwMDAsMTE0MjczMDE1MiwzMTEwMDkyNjksMTAwLDEwMCwwLDEwMCw0MDAsMCww
	 * @param cmd
	 * @param data
	 * @return
	 * @throws Exception 
	 * @throws Exception
	 */
	
	private boolean insertPhoneLocation(String data) throws Exception{
		System.err.println(data);
		String codeSign = data.substring(data.indexOf("=")+1, data.indexOf(","));
		String truTalkLatlng = "";
		if("BASE64".equals(codeSign)){//需要解码
			String dataTemp = data.substring(data.indexOf(",")+1);
			try {
				String dataTempDecode = new String(new BASE64Decoder().decodeBuffer(dataTemp));
				truTalkLatlng = dataTempDecode.substring(dataTempDecode.indexOf("=")+1);
			} catch (IOException e) {
				throw new Exception("手机上传的数据格式有误1！");
			}
		}else if("NONE".equalsIgnoreCase(codeSign)){
			truTalkLatlng = data.substring(data.lastIndexOf("=")+1);
		}else{
			throw new Exception("手机上传的数据格式有误2！");
		}
		String[] phoneInfo = truTalkLatlng.split(",");
		if(phoneInfo.length != 11){
			throw new Exception("手机上传的数据格式有误3！");
		}
		String sql = "insert into bp_phone_location_tbl values(?,?,now(),?,?,?,?,?,?,?,?,?) ";
		Object[] params = new Object[11];
		params[0] = UUID.randomUUID().toString().replaceAll("-", "");
		params[1] = phoneInfo[0];
		double[] result = new double[2];
		gps2BaiDu(Long.parseLong(phoneInfo[2])/10000000.0,Long.parseLong(phoneInfo[3])/10000000.0,result);
//		result[0] = Long.parseLong(phoneInfo[2])/10000000.0;
//		result[1] = Long.parseLong(phoneInfo[3])/10000000.0;
		checkAreas(phoneInfo[0],result[0],result[1]);
		checkRoutes(phoneInfo[0],result[0],result[1]);
		if(result[0] != 0.0 && result[1] != 0.0){
			params[2] = result[0];
			params[3] = result[1];
			params[4] = phoneInfo[4];
			params[5] = phoneInfo[5];
			params[6] = phoneInfo[6];
			params[7] = phoneInfo[7];
			params[8] = phoneInfo[8];
			params[9] = phoneInfo[9];
			params[10] = phoneInfo[10];
			return dbhelper.insert(sql,params);
		}
		return true;
	}

	private void gps2BaiDu(double locationX,double locationY,double[] result){
		String path = "http://api.map.baidu.com/ag/coord/convert?from=0&to=4&x="+ locationX + "+&y=" + locationY + "&callback=BMap.Convertor.cbk_7594";
		try{
			// 使用http请求获取转换结果
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(gps2BaiDuTimeOut);
			InputStream inStream = conn.getInputStream();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1){
				outStream.write(buffer, 0, len);
			}
			// 得到返回的结果
			String res = outStream.toString();
			System.out.println(res);
			// 处理结果
			if (res.indexOf("(") > 0 && res.indexOf(")") > 0){
				String str = res.substring(res.indexOf("(") + 1, res.indexOf(")"));
				String err = res.substring(res.indexOf("error") + 7, res.indexOf("error") + 8);
				if ("0".equals(err)){
					JSONObject js = JSONObject.fromObject(str);
					// 编码转换
					result[0] = Double.parseDouble(new String(Base64.decode(js.getString("x"))));
					result[1] = Double.parseDouble(new String(Base64.decode(js.getString("y"))));
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			result = new double[2];
		}
	}
	/*检查定位人员是否离开了分配的路线
	 * 
	 * 违规规定（检查是否违规之前先检查路线点是否设置了详细时间段）：假如路线上的每个点都已设置了到达该检查点的有效时间段，
	 * 		当手机上传位置时，判断当前时刻在哪个路线点的时间段之间，
	 * 
	 * 		如果没有找到时间段对应的路线点（因为手机上传位置有时间间隔，可能人已经经过了检查点但在上传位置信息时，人又离开了检查点
	 * 			避免这个问题可通过设置点的详细时间段时将时间段的值大于上传间隔），
	 * 			检查离当前时间最近的点的时间段，如果找到该点，查看该人员在该点的时间段内的历史位置记录，
	 * 			看之前有没有经过该点，如果没有，查看是否记录过日志，如果没有则记录日志（违规）
	 * 		如果找到时间段对应的路线点，则判断当前位置是否在该点的有效范围内，如果不在，查看该人员在该点的时间段内的历史位置记录，
	 * 			看之前有没有经过该点，如果没有则查看提醒日志是否已发送提醒短信，如果没发则发短信提醒（不是违规）并记录日志（提醒）；
	 * 		
	 * 		
	 * 因为粗定位偏差比较大，所以在检查上传的位置点是否在路线点的有效范围内时需要考虑这个偏差
	 */
	private void checkRoutes(String phone,double locationX,double locationY){
		Map<String,List<Map<String,Object>>> assignedRouteTimes = getAssignedRouteTimesInTime(phone);//仅包含已设置了详细时间段的路线
		Iterator<String> ite = assignedRouteTimes.keySet().iterator();
		while(ite.hasNext()){
			String routeTimeId = ite.next();
			List<Map<String,Object>> points = assignedRouteTimes.get(routeTimeId);
			try {
				int pointIdex = getPointIndex(points);
				if(pointIdex == -1){//没找到对应时间段的路线点
					int nearestPointIndex = getNearestPointIndex(points);
					if(-1 != nearestPointIndex){//得到最近一个时间段的点
						Map<String,Object> point = points.get(nearestPointIndex);
						if(getCrossPointNumber(phone,point) == 0){//之前没有经过该点
							if(!existLog(point.get("person_id").toString(),point.get("route_time_id").toString(),point.get("point_id").toString(),"2")){//"2"代表违规
								writeLog(point.get("person_id").toString(),point.get("route_time_id").toString(),point.get("point_id").toString(),"2");
							}
						}
					}
				}else{//找到对应时间段的路线点
					Map<String,Object> point = points.get(pointIdex);
					if(Math.sqrt(Math.pow((locationX-(Double)point.get("location_x")), 2)+Math.pow((locationY-(Double)point.get("location_y")), 2)) 
							> (Double)point.get("effective_range")){//不在有效范围内
						if(getCrossPointNumber(phone,point) == 0){//之前没有经过该点
							if(!existLog(point.get("person_id").toString(),point.get("route_time_id").toString(),point.get("point_id").toString(),"1")){//"1"代表短信提醒路线巡更
								//发送短信提醒
								//..
								writeLog(point.get("person_id").toString(),point.get("route_time_id").toString(),point.get("point_id").toString(),"1");
							}
						}
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private Map<String,List<Map<String,Object>>> getAssignedRouteTimesInTime(String phone){//Map<路线时间id,List<行记录>>
		Map<String,List<Map<String,Object>>> assignedRoutes = new HashMap<String,List<Map<String,Object>>>();
		String sql = "select r.effective_range,rt.id route_time_id,p.id person_id,rp.id point_id,rp.location_x,rp.location_y,rpt.start_time,rpt.end_time " +
				"from bp_coarse_route_time_assign_tbl rta " +
				"join bp_person_tbl p on (p.phone = '"+phone+"' and rta.person_id = p.id) " +
				"join bp_coarse_route_time_tbl rt on (rta.route_time_id = rt.id and rt.start_time < now() and rt.end_time > now()) " +
				"join bp_coarse_route_tbl r on (rt.route_id = r.id) " +
				"join bp_coarse_route_point_tbl rp on (r.id = rp.route_id) " +
				"left join bp_coarse_route_point_time_tbl rpt on (rt.id = rpt.route_time_id and rp.id = rpt.route_point_id) " +
				"order by r.id,rpt.start_time ";
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
		Iterator<Map<String,Object>> ite = dataList.iterator();
		String ignoreRouteTimeId = "";
		while(ite.hasNext()){
			Map<String,Object> rowData = ite.next();
			String routeTimeId = rowData.get("route_time_id").toString();
			if(ignoreRouteTimeId.equals(routeTimeId)){//忽略没有设置详细路线点时间段的路线时间
				continue;
			}
			Object startTime = rowData.get("start_time");
			Object endTime = rowData.get("end_time");
			if(null == startTime || null == endTime){
				ignoreRouteTimeId = routeTimeId;
				assignedRoutes.remove(routeTimeId);//删除未设置路线点时间段的路线
				continue;
			}
			List<Map<String,Object>> points = assignedRoutes.get(routeTimeId);
			if(null == points){
				points = new ArrayList<Map<String,Object>>();
				assignedRoutes.put(routeTimeId, points);
			}
			points.add(rowData);
		}
		return assignedRoutes;
	}
	
	private int getPointIndex(List<Map<String,Object>> points) throws ParseException{//数据正确的情况下只会有一个point符合条件
		SimpleDateFormat formate = new SimpleDateFormat("HH:mm:ss");
		for(int i = 0;i<points.size();i++){
			Map<String,Object> point = points.get(i);
			Date startTime = formate.parse(point.get("start_time").toString());
			Date endTime = formate.parse(point.get("end_time").toString());
			Date now = formate.parse(formate.format(new Date()));
			if(startTime.before(now) && endTime.after(now)){
				return i;
			}
		}
		return -1;
	}
	
	private int getNearestPointIndex(List<Map<String,Object>> points) throws ParseException{
		SimpleDateFormat formate = new SimpleDateFormat("HH:mm:ss");
		for(int i = points.size()-1;i>=0;i--){
			Map<String,Object> point = points.get(i);
			Date endTime = formate.parse(point.get("end_time").toString());
			Date now = formate.parse(formate.format(new Date()));
			if(endTime.before(now)){
				return i;
			}
		}
		return -1;
	}
	
	@SuppressWarnings("unchecked")
	private long getCrossPointNumber(String phone,Map<String,Object> point){
		String pointId = point.get("point_id").toString();
		String startTime = point.get("start_time").toString();
		String endTime = point.get("end_time").toString();
		String sql = "select count(*) count from bp_coarse_route_tbl r " +
				"join bp_coarse_route_point_tbl rp on (rp.id = '"+pointId+"' and r.id = rp.route_id) " +
				"join bp_phone_location_tbl pl on (pl.phone_imsi = '"+phone+"' and pl.upload_date > DATE(now()) " +
				"and TIME(pl.upload_date) > '"+startTime+"' and TIME(pl.upload_date) < '"+endTime+"' and " +
				"sqrt(pow(rp.location_x-pl.location_x,2)+pow(rp.location_y-pl.location_y,2)) < r.effective_range )";
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
		return (Long)dataList.get(0).get("count");
	}
	
	@SuppressWarnings("unchecked")
	private boolean existLog(String personId,String routeTimeId,String pointId,String type){
		String sql = "select count(*) count from bp_coarse_event_tbl " +
				"where person_id = '"+personId+"' and time_id = '"+routeTimeId+"' " +
				"and point_id = '"+pointId+"' and type = '"+type+"' and add_date > DATE(now())";
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
		return (Long)dataList.get(0).get("count") > 0;
	}
	
	private void writeLog(String personId,String routeTimeId,String pointId,String type){
		String id =UUID.randomUUID().toString().replaceAll("-", "");
		String sql = "insert into bp_coarse_event_tbl values('"+id+"','"+personId+"','"+routeTimeId+"','"+pointId+"','"+type+"',now())";
		dbhelper.insert(sql);
	}
	
	
	//检查定位人员是否离开或进入了分配的区域
	@SuppressWarnings("unchecked")
	private void checkAreas(String phone,double locationX,double locationY){
		String sql = "select ca.id area_id,cat.area_type,cat.id area_time_id,p.id person_id " +
				"from bp_coarse_area_time_assign_tbl cata " +
				"join bp_person_tbl p on (p.phone = '"+phone+"' and cata.person_id = p.id) " +
				"join bp_coarse_area_time_tbl cat on (cata.area_time_id = cat.id and cat.start_time < now() and cat.end_time > now()) " +
				"join bp_coarse_area_tbl ca on (cat.area_id = ca.id) ";
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
		Iterator<Map<String,Object>> ite = dataList.iterator();
		while(ite.hasNext()){
			Map<String,Object> rowData = ite.next();
			String areaId = rowData.get("area_id").toString();
			String areaType = rowData.get("area_type").toString();
			int position = Commons.checkPointInWarnArea(dbhelper, areaId, locationX, locationY);
			if("9".equals(areaType) && 1 == position){//进入了禁止进入的区域
				if(!existLog(rowData.get("person_id").toString(),rowData.get("area_time_id").toString(),"","3")){//如果没有发过违规短信
					//发送短信通知区域违规
					//..
					writeLog(rowData.get("person_id").toString(),rowData.get("area_time_id").toString(),"","3");//3为禁止进入
				}
			}
			if("10".equals(areaType) && -1 == position){//离开了指定的区域
				if(!existLog(rowData.get("person_id").toString(),rowData.get("area_time_id").toString(),"","4")){//如果没有发过违规短信
					//发送短信通知区域违规
					//..
					writeLog(rowData.get("person_id").toString(),rowData.get("area_time_id").toString(),"","4");//4为禁止离开
				}
			}
		}
	}
	
	private boolean updateWifiLocation(String wifi){
		String rssi_wireless = ""; // wifi1
		String rssi_online = ""; // wifi2
		int i = wifi.indexOf("04:4f:aa:11:de:28=") + 18; //wifi1
		rssi_wireless = wifi.substring(i, i + 3);
		i = wifi.indexOf("ec:17:2f:ab:61:a6=") + 18;// wifi2
		rssi_online = wifi.substring(i, i + 3);

		// update数据库
		System.err.println("--------------------");
		StringBuffer sql = new StringBuffer();
		sql.append("update bp_person_location_tbl ");
		sql.append("set wifi1 = ?,wifi2 = ? ");
		sql.append("where id = ? ");
		Object[] params = new Object[3];
		params[0] = rssi_wireless;
		params[1] = rssi_online;
		params[2] = "1";
		return dbhelper.update(sql.toString(),params);
	}
	
	public String getJSONBean(HttpServletRequest request) {
		InputStream in = null;
		ByteArrayOutputStream read = null;
		String bkStr = "";
		try {
			in = request.getInputStream();
			read = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				read.write(buffer, 0, len);
			}
			byte[] data = read.toByteArray();
			bkStr = new String(data, "UTF-8").intern();
			return bkStr;
		} catch (Exception e) {
			e.printStackTrace();
			return bkStr;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (read != null) {
					read.close();
				}
			} catch (IOException e) {
			}
		}
	}
}
