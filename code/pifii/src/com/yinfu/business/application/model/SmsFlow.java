package com.yinfu.business.application.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.shop.model.Shop;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DateUtil;

@TableBind(tableName="bp_sms_flow")
public class SmsFlow extends Model<SmsFlow>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final SmsFlow dao = new SmsFlow();
	
	/**
	 * 保存盒子所发送的短信验证码信息
	 * @param deviceId
	 * @param phoneNumber
	 * @return
	 */
	public boolean saveSmsFlowInfo(int deviceId,String phoneNumber){
		SmsFlow sms = new SmsFlow();
		sms.set("device_id", deviceId);
		sms.set("phone_number", phoneNumber);
		sms.set("send_time", new Date());
		return sms.save();
	}
	
	/**
	 * 获取短信统计信息图表的xml
	 * @param startDate
	 * @param enddate
	 * @return
	 */
	public String getSmsStatisInfoXml(String startDate,String endDate,String monthnums){
		String dataxml ="";
		try {
			//默认为6个月数据，如果开始时间和结束时间都不为空，则统计设置时间区间内的数据。
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			if ((endDate!=null && !endDate.equals(""))&&(startDate!=null && !startDate.equals(""))) {
				endDate = sdf.format(sdf.parse(endDate));
				startDate = sdf.format(sdf.parse(startDate));
			}else{
				endDate = sdf.format(new Date());
				startDate =  DateUtil.getBeforOrAfterNMonth(endDate, -6);
			}
			List<Shop> shops = Shop.dao.list();
			Shop shop = shops.size()!=0?shops.get(0):new Shop();
			StringBuffer sql = new StringBuffer();
			sql.append("select count(f.id) zs,DATE_FORMAT(f.send_time,'%Y-%m') sendtime from bp_shop s");
			sql.append(" left join bp_device d on s.id=d.shop_id");
			sql.append(" left join bp_sms_flow f on d.id=f.device_id");
			sql.append(" where 1=1");
			sql.append(" and s.id="+shop.getId());
			sql.append(" and DATE_FORMAT(f.send_time,'%Y-%m')>='" +startDate +"'");
			sql.append(" and DATE_FORMAT(f.send_time,'%Y-%m')<='" +endDate +"'");
			sql.append(" group by sendTime");
			List<Record> zslist = Db.find(sql.toString());
			List<String> smslist = getSmsSendTimeDime(startDate,endDate,monthnums);
			dataxml=buildColumnXml(startDate,endDate,smslist,zslist,shop);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataxml;
	}
	
	
	/**
	 * 组装column统计图形xml
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public String buildColumnXml(String startDate,String endDate,List<String> smslist,
			List<Record> zsList,Shop shop){
		StringBuffer xmlData = new StringBuffer();
		String caption = startDate + "至" + endDate + "短信统计表";
		xmlData.append("<chart caption='" + caption + "' xaxisname='日期' yaxisname='短信数' theme='fint'");
		xmlData.append(" baseFont ='微软雅黑'  baseFontSize='13' numberprefix='' rotatevalues='1' ");
		xmlData.append(" placevaluesinside='1' legendshadow='0' legendborderalpha='0' legendbgcolor='FFFFFF' showborder='0'>");
		xmlData.append("<categories>");
		for (int i = 0; i < smslist.size(); i++) {
			xmlData.append("<category label='" + smslist.get(i)+ "' />");
		}
		xmlData.append(" </categories>");
		int zs =0;
		xmlData.append(" <dataset seriesname='"+shop.getName()+"' showvalues='1'>");
		boolean bool=true;
		for(int i=0;i<smslist.size();i++){
			bool=true;
			for (int j = 0; j < zsList.size(); j++) {
				Record zsObj = zsList.get(j);
				if(smslist.get(i).equals(zsObj.get("sendtime"))){
					zs=Integer.parseInt(zsObj.get("zs").toString());
					xmlData.append("<set value='" + zs + "' />");
					bool=false;
				}
                if(bool && j==zsList.size()-1){
                	xmlData.append("<set value='0' />");
                }
			}			
		}
		xmlData.append(" </dataset> ");
		xmlData.append(" </chart>");
		return xmlData.toString();
	}
	
	/**
	 * 得到时间维度
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException 
	 */
	public List<String> getSmsSendTimeDime(String startDate,String endDate,String monthnums) throws ParseException{
		int month = 0;
		int count = -3;
		if(monthnums!=null && !monthnums.equals("")){
			month=Integer.parseInt(monthnums);
			switch (month) {
			case 0:
				count=0;
				break;
			case 1:
				count=-1;
				break;
			case 2:
				count=-2;
				break;
			case 3:
				count=-3;
				break;
			}
		}
		List<String> rellist =new ArrayList<String>();
	
		for(int i=count;i<0;i++){
			rellist.add(DateUtil.getBeforOrAfterNMonth(endDate, i));
		}
		rellist.add(endDate);
		return rellist;
	}

}
