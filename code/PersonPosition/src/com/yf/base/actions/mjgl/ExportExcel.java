package com.yf.base.actions.mjgl;


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

import com.opensymphony.xwork2.ActionContext;
import com.yf.base.actions.datastatistics.CommonSearch;
import com.yf.ext.base.BaseAction;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

@SuppressWarnings("deprecation")
public class ExportExcel extends BaseAction {
	private static final long serialVersionUID = 1L;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);

	private String communityId;
	private String communityName;
	private String cardId;
	private String name;
	private HSSFWorkbook workbook;
	private HSSFSheet sheet;
	private short currentRowIndex = 0;
//	private int columnsNum = 0;
	private String[] headers = new String[]{"卡编号","手机号码","小区名称","RFID卡名称","上传时间差"};
	private String sheetName;
	
	@Override
	public String execute() throws Exception {
		sheetName = "RFID卡状态查看.xls";
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
	
	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private List<?> getExcelData(){
		List<?> dataList = new ArrayList<Object>();
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT prl.id,prl.phone,prl.card_id cardID,com.name comname,c.name,TIMEDIFF(NOW(),prl.upload_time) uploadtime");
			sql.append(" from bp_phone_rfid_location_tbl prl");
			sql.append(" JOIN bp_card_tbl c on (prl.card_id=c.card_mark)");
			sql.append(" JOIN bp_community_tbl com on (c.communityId=com.id)");
			sql.append(" JOIN bp_person_tbl p on (p.communityId=com.id)");
			sql.append(" LEFT JOIN sys_dictionary_tbl d on (d.DIC_ID=p.dictionaryId)");
			sql.append(" WHERE 1=1");		
			if(!StringUtils.isBlank(cardId)){
				sql.append(" and prl.card_id LIKE '"+DBHelper.wrapFuzzyQuery(cardId)+"'");
			}
			if(!StringUtils.isBlank(name)){
				sql.append(" and c.name LIKE '"+DBHelper.wrapFuzzyQuery(name)+"'");
			}
			if(!StringUtils.isBlank(communityId)){
				sql.append(" and c.communityId = '"+communityId+"'");
			}
			sql.append(" GROUP BY prl.card_id");
			sql.append(" ORDER BY prl.upload_time");
			dataList = dbhelper.getMapListBySql(sql.toString());
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
		if(StringUtils.isNotBlank(communityName)){
			sb.append("小区名称:"+communityName+";");
		}
		if(StringUtils.isNotBlank(cardId)){
			sb.append("卡编号:"+cardId+";");
		}
		if(StringUtils.isNotBlank(name)){
			sb.append("卡名称:"+name+";");
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
		
		Iterator<Map<String,String>> ite = (Iterator<Map<String, String>>) data.iterator();
		while(ite.hasNext()){
			Map<String,String> rowData = ite.next();
			HSSFRow row = sheet.createRow(currentRowIndex);
			HSSFCellStyle style = null;
			
			String process = "";
			String isDone = rowData.get("is_done");
			String positionDate = rowData.get("position_date");
			if(StringUtils.isNotBlank(positionDate)){
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
			}
			setCell(row,style,rowData.get("date"),0);
			setCell(row,style,rowData.get("person_name"),1);
			setCell(row,style,rowData.get("community_name"),2);
			setCell(row,style,rowData.get("person_type"),3);
			setCell(row,style,rowData.get("route_name"),4);
			setCell(row,style,rowData.get("route_point_name"),5);
			setCell(row,style,rowData.get("phone"),6);
			setCell(row,style,rowData.get("start_time"),7);
			setCell(row,style,rowData.get("end_time"),8);
			setCell(row,style,process,9);
			
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

	private void setCell(HSSFRow row,HSSFCellStyle style,String value,int index){
		HSSFCell cell = row.createCell(index);
		cell.setCellValue(value);
		cell.setCellStyle(style);
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
	
}
