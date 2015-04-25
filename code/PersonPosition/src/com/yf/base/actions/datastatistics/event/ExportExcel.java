package com.yf.base.actions.datastatistics.event;


import java.io.OutputStream;
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

import com.yf.ext.base.BaseAction;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

@SuppressWarnings("deprecation")
public class ExportExcel extends BaseAction {
	private static final long serialVersionUID = 1L;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	//显示所有的记录，包含字段，姓名、小区、路线、日期、完成度、详情（可查看完成的线路点）
	private String eventResult;
	private String eventResultName;
	private String eventType;
	private String eventTypeName;
	private String personName;
	private String startDate;
	private String endDate;
	
	private HSSFWorkbook workbook;
	private HSSFSheet sheet;
	private short currentRowIndex = 0;
//	private int columnsNum = 0;
	private String[] headers = new String[]{"位置","事件发起人","处理人","事件类型","处理结果","事件描述","时间"};
	private String sheetName;
	
	@Override
	public String execute() throws Exception {
		sheetName = "事件数据("+startDate.replaceAll(":", ".")+" - "+endDate.replaceAll(":", ".")+").xls";
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
		StringBuffer dataSql = new StringBuffer();
		dataSql.append("select c.name community_name,p.name person_name,u.user_name,d.key_value,DATE_FORMAT(we.event_time,'%Y-%m-%d %H:%i:%s') event_time,we.is_deal,we.description ");
		StringBuffer commonSql = new StringBuffer();
		commonSql.append("from bp_warn_event_tbl we ");
		commonSql.append("join bp_person_tbl p on (");
		if(StringUtils.isNotBlank(personName)){
			commonSql.append("p.name LIKE '"+DBHelper.wrapFuzzyQuery(personName)+"' and ");
		}
		commonSql.append("p.id = we.warn_person_id) ");
		commonSql.append("join bp_community_tbl c on (we.community_id=c.id) ");
		commonSql.append("left join sys_user_tbl u on (u.user_id = we.deal_user_id) ");
		commonSql.append("join sys_dictionary_tbl d on (");
		if(StringUtils.isNotBlank(eventType)){
			commonSql.append("d.dic_id = '"+eventType+"' and ");
		}
		commonSql.append("d.DIC_ID = we.event_type) ");
		commonSql.append("where 1=1 ");
		if(StringUtils.isNotBlank(eventResult)){
			if("1".equals(eventResult)){
				commonSql.append(" and !we.is_deal ");
			}else if("2".equals(eventResult)){
				commonSql.append(" and we.is_deal ");
			}else{
				commonSql.append(" and we.is_deal is null ");
			}
		}
		if(StringUtils.isNotBlank(startDate)){
			commonSql.append(" and we.event_time >= '"+startDate+"' ");
		}
		if(StringUtils.isNotBlank(endDate)){
			commonSql.append(" and we.event_time <= '"+endDate+"' ");
		}
		dataSql.append(commonSql.toString());
		dataSql.append(" ORDER BY we.event_time DESC ");
		List<?> dataList = dbhelper.getMapListBySql(dataSql.toString());
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
		if(StringUtils.isNotBlank(eventTypeName)){
			sb.append("事件类型:"+eventTypeName+";");
		}
		if(StringUtils.isNotBlank(eventResultName)){
			sb.append("处理结果:"+eventResultName+";");
		}
		if(StringUtils.isNotBlank(personName)){
			sb.append("事件发起人（相似匹配）:"+personName+";");
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
		HSSFCellStyle whiteStyle = getWhiteStyle();
		HSSFCellStyle warnStyle = getWarnStyle();
		
		Iterator<Map<String,Object>> ite = (Iterator<Map<String, Object>>) data.iterator();
		while(ite.hasNext()){
			Map<String,Object> rowData = ite.next();
			HSSFRow row = sheet.createRow(currentRowIndex);
			HSSFCellStyle style = whiteStyle;
			if(null == rowData.get("is_deal")){
				style = warnStyle;
			}
			String result = "";
			if(null != rowData.get("is_deal")){
				if("1".equals(rowData.get("is_deal").toString())){
					result = "已解决";
					style = whiteStyle;
				}else{
					result = "未解决";
					style = whiteStyle;
				}
			}else{
				result = "未处理";
				style = warnStyle;
			}
			setCell(row,style,rowData.get("community_name").toString(),0);
			setCell(row,style,rowData.get("person_name").toString(),1);
			setCell(row,style,(null == rowData.get("user_name"))?"":rowData.get("user_name").toString(),2);
			setCell(row,style,rowData.get("key_value").toString(),3);
			setCell(row,style,result,4);
			setCell(row,style,(null == rowData.get("description"))?"":rowData.get("description").toString(),5);
			setCell(row,style,rowData.get("event_time").toString(),6);
			currentRowIndex++;
		}
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

	public String getEventResult() {
		return eventResult;
	}

	public void setEventResult(String eventResult) {
		this.eventResult = eventResult;
	}

	public String getEventResultName() {
		return eventResultName;
	}

	public void setEventResultName(String eventResultName) {
		this.eventResultName = eventResultName;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventTypeName() {
		return eventTypeName;
	}

	public void setEventTypeName(String eventTypeName) {
		this.eventTypeName = eventTypeName;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
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

	
}
