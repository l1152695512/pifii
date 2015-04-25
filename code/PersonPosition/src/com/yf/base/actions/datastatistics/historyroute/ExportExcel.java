package com.yf.base.actions.datastatistics.historyroute;


import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.xwork.StringUtils;
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
	//显示所有的记录，包含字段，姓名、小区、日期、详情（可查看完成的线路点）
	private String name;
	private String communityId;
	//private String routeId;
	private String startDateTime;
	private String endDateTime;
	
	private String communityName;
	private String personId;
	private String personName;
	//private String routeName;
	
	private HSSFWorkbook workbook;
	private HSSFSheet sheet;
	private short currentRowIndex = 0;
//	private int columnsNum = 0;
	private String[] headers = new String[]{"日期","人员","人员类型","小区","位置","电话","开始时间","结束时间"};
	private String sheetName;
	
	@Override
	public String execute() throws Exception {		
		//sheetName = "巡更数据("+startDateTime+" - "+endDateTime+").xls";
		sheetName = "巡更数据("+startDateTime.replaceAll(":", ".")+" - "+endDateTime.replaceAll(":", ".")+").xls";
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
	
	private List<?> getExcelData(){
		List<?> dataList = new ArrayList<Object>();
		if(CommonSearch.searchTable(dbhelper, startDateTime, endDateTime)){
			StringBuffer sql = new StringBuffer();
			sql.append("select DATE_FORMAT(sd.date,'%Y-%m-%d') as date,p.name as person_name,d.key_value as person_type,c.name as community_name,c.map,");
			sql.append("rp.name as route_point_name,p.phone,");
			sql.append("IFNULL(DATE_FORMAT(rpt.start_time,'%H:%i:%s'),DATE_FORMAT(rt.start_time,'%H:%i:%s')) as start_time,");
			sql.append("IFNULL(DATE_FORMAT(rpt.end_time,'%H:%i:%s'),DATE_FORMAT(rt.end_time,'%H:%i:%s')) as end_time ");
			//sql.append("IF(min(sqrt(pow(rp.location_x-bct.locationX,2)+pow(rp.location_y-bct.locationY,2))) < r.effective_range,'1','0') is_done ");
			sql.append("from bp_fine_route_time_assign_tbl rta ");
			sql.append("join bp_fine_route_time_tbl rt on (rt.id=rta.route_time_id) ");
			sql.append("join bp_fine_route_tbl r on (");
			/*if(!StringUtils.isBlank(routeId)){
				sql.append("r.id = '"+routeId+"' and ");
			}*/
			sql.append("r.id = rt.route_id) ");
			sql.append("join bp_community_tbl c on (");
			if(!StringUtils.isBlank(communityId)){
				sql.append("c.id = '"+communityId+"' and ");
			}
			sql.append("c.id = r.community_id) ");
			sql.append("join bp_fine_route_point_tbl rp on (rp.route_id = r.id) ");
			sql.append("left join bp_fine_route_point_time_tbl rpt on (rpt.route_time_id = rt.id and rpt.route_point_id = rp.id) ");
			sql.append("join bp_person_tbl p on (");
			if(!StringUtils.isBlank(personId)){
				sql.append("p.id = '"+personId+"' and ");
			}
			/*if(!StringUtils.isBlank(name)){
				sql.append("p.name LIKE '"+DBHelper.wrapFuzzyQuery(name)+"' and ");
			}*/
			sql.append("p.id = rta.person_id) ");
			sql.append("join sys_dictionary_tbl d on (p.dictionaryId = d.dic_id) ");
			sql.append("join bp_search_date_tbl sd ");
			
			sql.append("left join bp_phone_rfid_location_tbl prl on (");
			if(StringUtils.isNotBlank(startDateTime)){
				sql.append("date(prl.upload_time) >= '"+startDateTime+"' and ");
			}
			if(StringUtils.isNotBlank(endDateTime)){
				sql.append("date(prl.upload_time) < '"+endDateTime+"' and ");
			}
			sql.append("date(prl.upload_time) = sd.date and p.phone = prl.phone and TIME(prl.upload_time)>IFNULL(rpt.start_time,rt.start_time) and TIME(prl.upload_time)<IFNULL(rpt.end_time,rt.end_time)) ");
			sql.append("left join bp_card_tbl bct on (bct.card_mark = prl.card_id) ");
			sql.append("group by sd.date,r.id,p.id,rt.id,rp.id ");
			dataList = dbhelper.getMapListBySql(sql.toString());
			System.out.println(sql.toString());
		}
		return dataList;
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
			sheet.setColumnWidth(i, 4000); // 定义各列的列宽
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
		sb.append("开始时间:"+startDateTime+";");
		sb.append("结束时间:"+endDateTime+";");
		if(StringUtils.isNotBlank(communityName)){
			sb.append("小区名称:"+communityName+";");
		}
		/*if(StringUtils.isNotBlank(routeName)){
			sb.append("路线名称:"+routeName+";");
		}*/
		if(StringUtils.isNotBlank(personName)){
			sb.append("人员名称:"+personName+";");
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
		//HSSFCellStyle redStyle = getRedStyle();
		HSSFCellStyle whiteStyle = getWhiteStyle();
		
		Iterator<Map<String,String>> ite = (Iterator<Map<String, String>>) data.iterator();
		while(ite.hasNext()){
			Map<String,String> rowData = ite.next();
			HSSFRow row = sheet.createRow(currentRowIndex);
			HSSFCellStyle style = whiteStyle;
			
			//String process = "";
			//String isDone = rowData.get("is_done");
			//String positionDate = rowData.get("position_date");
			/*if(StringUtils.isNotBlank(positionDate)){
				if("1".equals(isDone)){
					process = "完成";
					style = whiteStyle;
				}else{
					process = "未完成";
					style = redStyle;
				}
			}else{
				process = "未完成";
				style = redStyle;
			}*/
			setCell(row,style,rowData.get("date"),0);
			setCell(row,style,rowData.get("person_name"),1);
			setCell(row,style,rowData.get("community_name"),2);
			setCell(row,style,rowData.get("person_type"),3);
			//setCell(row,style,rowData.get("route_name"),4);
			setCell(row,style,rowData.get("route_point_name"),4);
			setCell(row,style,rowData.get("phone"),5);
			setCell(row,style,rowData.get("start_time"),6);
			setCell(row,style,rowData.get("end_time"),7);
			//setCell(row,style,process,9);
			
			currentRowIndex++;
		}
	}
	
	/*private HSSFCellStyle getRedStyle(){
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
	}*/
	
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

	private void setCell(HSSFRow row,HSSFCellStyle style,String value,int index){
		HSSFCell cell = row.createCell(index);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
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


	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public String getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}
	
}
