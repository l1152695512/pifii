package com.yf.base.actions.datastatistics.warn;


import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.struts2.ServletActionContext;

import com.yf.base.actions.datastatistics.CommonSearch;
import com.yf.ext.base.BaseAction;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

@SuppressWarnings("deprecation")
public class ExportExcel extends BaseAction {
	private static final long serialVersionUID = 1L;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	//显示所有的记录，包含字段，姓名、小区、路线、日期、完成度、详情（可查看完成的线路点）
	private String name;
	private String communityId;
	private String warnAreaId;
	private String startDate;
	private String endDate;
	
	private String communityName;
	private String areaName;
	
	private HSSFWorkbook workbook;
	private HSSFSheet sheet;
	private short currentRowIndex = 0;
//	private int columnsNum = 0;
	private String[] headers = new String[]{"日期","位置","告警区域","区域类型","开始时间","结束时间","人员","人员类型","电话","定位时间","结果"};
	private String sheetName;
	
	@Override
	public String execute() throws Exception {
		sheetName = "区域管理数据("+startDate+" - "+endDate+").xls";
		generExcel(getExcelData());
		try {
			HttpServletResponse response=ServletActionContext.getResponse();
			response.setContentType("application/octet-stream");
		    response.setHeader( "Content-Disposition", "attachment;filename=" + new String(sheetName.getBytes(), "ISO8859-1"));
		    OutputStream out = response.getOutputStream();
		    workbook.write(out);
		    response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private List<?> getExcelData(){
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		if(CommonSearch.initSearchTable(dbhelper, startDate, endDate)){
			StringBuffer dataSql = new StringBuffer();
			dataSql.append("select DATE_FORMAT(sd.date,'%Y-%m-%d') date,c.name community_name,wa.name area_name,d.key_value as area_type_name,p.name person_name,d1.key_value as person_type_name,");
			dataSql.append("p.phone,DATE_FORMAT(prl.upload_time,'%Y-%m-%d %H:%i:%s') position_date,bct.locationX,bct.locationY,");
			dataSql.append("DATE_FORMAT(t.start_time,'%H:%i:%s') start_time,DATE_FORMAT(t.end_time,'%H:%i:%s') end_time,t.area_type,wa.id area_id ");
			dataSql.append("from bp_fine_area_time_assign_tbl ta ");
			dataSql.append("join bp_fine_area_time_tbl t on (");
			if(StringUtils.isNotBlank(warnAreaId)){
				dataSql.append("t.area_id='"+warnAreaId+"' and ");
			}
			dataSql.append("t.id = ta.area_time_id) ");
			dataSql.append("join sys_dictionary_tbl d on (t.area_type = d.dic_id) ");
			dataSql.append("join bp_fine_area_tbl wa on (wa.id = t.area_id) ");
			dataSql.append("join bp_community_tbl c on (");
			if(StringUtils.isNotBlank(communityId)){
				dataSql.append("c.id = '"+communityId+"' and ");
			}
			dataSql.append("c.id = wa.community_id) ");
			dataSql.append("join bp_person_tbl p on (");
			if(StringUtils.isNotBlank(name)){
				dataSql.append("p.name like '"+DBHelper.wrapFuzzyQuery(name)+"' and ");
			}
			dataSql.append("p.id = ta.person_id) ");
			dataSql.append("join sys_dictionary_tbl d1 on (p.dictionaryId = d1.dic_id) ");
			dataSql.append("join bp_search_date_tbl sd ");
			dataSql.append("left join bp_phone_rfid_location_tbl prl on (");
			if(StringUtils.isNotBlank(startDate)){
				dataSql.append("date(prl.upload_time) >= '"+startDate+"' and ");
			}
			if(StringUtils.isNotBlank(endDate)){
				dataSql.append("date(prl.upload_time) < '"+endDate+"' and ");
			}
			dataSql.append("date(prl.upload_time) = sd.date and TIME(prl.upload_time) >= t.start_time and TIME(prl.upload_time) <= t.end_time and prl.phone = p.phone) ");
			dataSql.append("left join bp_card_tbl bct on (bct.card_mark = prl.card_id) ");
			dataSql.append("order by sd.date,c.id,wa.id,p.id,t.id,prl.upload_time ");
			System.err.println(dataSql.toString());
			dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(dataSql.toString());
			Map<String,List<Map<String,Object>>> warnAreas = Commons.getWarnAreas(dbhelper);
			//检查区域是否有违规记录
			if(null != warnAreas){
				Iterator<Map<String,Object>> iteDataList = dataList.iterator();
				while(iteDataList.hasNext()){
					Map<String,Object> rowData = iteDataList.next();
					String areaId = rowData.get("area_id").toString();
					String areaType = rowData.get("area_type").toString();
					boolean isLegal = checkPositionIsLegal(warnAreas,areaId,rowData.get("locationX"),rowData.get("locationY"),areaType);
					rowData.put("is_legal", isLegal?"1":"0");
				}
			}
		}
		return dataList;
	}
	
	private boolean checkPositionIsLegal(Map<String,List<Map<String,Object>>> warnAreas,String areaId,Object locationX,Object locationY,String areaType){
		if(null == locationX || null == locationY){
			return false;
		}
		int pointPosition = Commons.checkPointInWarnArea(warnAreas.get(areaId),(Double)locationX,(Double)locationY);//为0的情况都算是合法的，即在线上
		if(("9".equals(areaType) && 1 == pointPosition) //进入了禁止进入的区域
				|| ("10".equals(areaType) && -1 == pointPosition)){//离开了规定的区域
			return false;
		}
		return true;
	}
	
	private void generExcel(List<?> data){
		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet();
		workbook.setSheetName(0, sheetName);
		setSheetColumnWidth();
		setTitle();
		setHeader();
		setContent(data);
	}
	
	private void setSheetColumnWidth(){
		for(int i=0;i<headers.length;i++){
			sheet.setColumnWidth(i, 5000); // 定义各列的列宽
		}
	}
	
	private void setTitle(){
		HSSFCellStyle style = workbook.createCellStyle();
//		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	//	cellStyleHead.setWrapText(true);	
		HSSFFont fontHead = workbook.createFont();
		fontHead.setFontHeight((short) 250); // 大小		
		fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 粗体
		style.setFont(fontHead);
		
		HSSFRow row = sheet.createRow(currentRowIndex);
		row.setHeight((short) 500);
		StringBuffer sb = new StringBuffer();
		sb.append("数据搜索条件【");
		sb.append("开始时间:"+startDate+";");
		sb.append("结束时间:"+endDate+";");
		if(StringUtils.isNotBlank(communityName)){
			sb.append("位置名称:"+communityName+";");
		}
		if(StringUtils.isNotBlank(areaName)){
			sb.append("告警区域名称:"+areaName+";");
		}
		if(StringUtils.isNotBlank(name)){
			sb.append("人员名称（相似匹配）:"+name+";");
		}
		sb.append("】");
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			if(i==0){
				cell.setCellValue(sb.toString());
				cell.setCellStyle(style);
			}
		}
		sheet.addMergedRegion(new CellRangeAddress(currentRowIndex,currentRowIndex,0,headers.length-1));
		currentRowIndex++;
	}
	
	private void setHeader(){
		HSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	//	cellStyleHead.setWrapText(true);	
		HSSFFont fontHead = workbook.createFont();
		fontHead.setFontHeight((short) 220); // 大小		
		fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 粗体
		style.setFont(fontHead);
		style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);//前景颜色
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充方式，前色填充
		
		HSSFRow row = sheet.createRow(currentRowIndex);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(style);
		}
		currentRowIndex++;
	}
	
	@SuppressWarnings("unchecked")
	private void setContent(List<?> data){
		HSSFCellStyle redStyle = getRedStyle();
		HSSFCellStyle whiteStyle = getWhiteStyle();
		HSSFCellStyle warnStyle = getWarnStyle();
		
		Iterator<Map<String,String>> ite = (Iterator<Map<String, String>>) data.iterator();
		while(ite.hasNext()){
			Map<String,String> rowData = ite.next();
			HSSFRow row = sheet.createRow(currentRowIndex);
			HSSFCellStyle style = null;
			
			String result = "";
			String isLegal = rowData.get("is_legal");
			String positionDate = rowData.get("position_date");
			if(StringUtils.isNotBlank(positionDate)){
				if("1".equals(isLegal)){
					result = "正常";
					style = whiteStyle;
				}else{
					result = "越界";
					style = redStyle;
				}
			}else{
				result = "无定位信息（设备无网络或人员当天不在区域内）";
				style = warnStyle;
			}
			setCell(row,style,rowData.get("date"),0);
			setCell(row,style,rowData.get("community_name"),1);
			setCell(row,style,rowData.get("area_name"),2);
			setCell(row,style,rowData.get("area_type_name"),3);
			setCell(row,style,rowData.get("start_time"),4);
			setCell(row,style,rowData.get("end_time"),5);
			
			setCell(row,style,rowData.get("person_name"),6);
			setCell(row,style,rowData.get("person_type_name"),7);
			setCell(row,style,rowData.get("phone"),8);
			setCell(row,style,rowData.get("position_date"),9);
			setCell(row,style,result,10);
			
			currentRowIndex++;
		}
	}
	
	private HSSFCellStyle getRedStyle(){
		HSSFCellStyle style = workbook.createCellStyle();
//		style.setFillBackgroundColor(HSSFColor.RED.index);
		style.setFillForegroundColor(HSSFColor.RED.index);//前景颜色
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充方式，前色填充
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		HSSFFont fontHead = workbook.createFont();
		fontHead.setFontHeight((short) 200); // 大小		
		style.setFont(fontHead);
		return style;
	}
	
	private HSSFCellStyle getWhiteStyle(){
		HSSFCellStyle style = workbook.createCellStyle();
//		style.setFillBackgroundColor(HSSFColor.RED.index);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());//前景颜色
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充方式，前色填充
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		HSSFFont fontHead = workbook.createFont();
		fontHead.setFontHeight((short) 200); // 大小		
		style.setFont(fontHead);
		return style;
	}
	
	private HSSFCellStyle getWarnStyle(){
		HSSFCellStyle style = workbook.createCellStyle();
//		style.setFillBackgroundColor(HSSFColor.RED.index);
		style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());//前景颜色
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充方式，前色填充
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		HSSFFont fontHead = workbook.createFont();
		fontHead.setFontHeight((short) 200); // 大小		
		style.setFont(fontHead);
		return style;
	}
	
	private void setCell(HSSFRow row,HSSFCellStyle style,String value,int index){
		HSSFCell cell = row.createCell(index);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}

	public String getWarnAreaId() {
		return warnAreaId;
	}
	
	public void setWarnAreaId(String warnAreaId) {
		this.warnAreaId = warnAreaId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public String getAreaName() {
		return areaName;
	}
	
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
}
