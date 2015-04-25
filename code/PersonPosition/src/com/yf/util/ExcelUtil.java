package com.yf.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFSimpleShape;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.struts2.ServletActionContext;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import jxl.write.WritableImage;
import jxl.write.WritableSheet;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;

public class ExcelUtil {

	private static final Logger log = Logger.getLogger(ExcelUtil.class);

	/**
	 * 功能：将HSSFWorkbook写入Excel文件
	 * 
	 * @param wb
	 *            HSSFWorkbook
	 * @param absPath
	 *            写入文件的相对路径
	 * @param wbName
	 *            文件名
	 */
	public static void writeWorkbook(HSSFWorkbook wb, String fileName) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName);
			wb.write(fos);
		} catch (java.io.FileNotFoundException e) {
			log.error(new StringBuffer("[").append(e.getMessage()).append("]")
					.append(e.getCause()));
		} catch (java.io.IOException e) {
			log.error(new StringBuffer("[").append(e.getMessage()).append("]")
					.append(e.getCause()));
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				log.error(new StringBuffer("[").append(e.getMessage()).append(
						"]").append(e.getCause()));
			}
		}
	}

	/**
	 * 功能：将HSSFWorkbook写入Excel文件
	 * 
	 * @param wb
	 *            HSSFWorkbook
	 * @param absPath
	 *            写入文件的相对路径
	 * @param wbName
	 *            文件名
	 * @throws IOException
	 */
	public static void writeWorkbookRequest(HSSFWorkbook wb, String fileName)
			throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		OutputStream out = response.getOutputStream();
		try {
			response.setContentType("application/octet-stream");

			response.setHeader("Content-Disposition", "attachment;filename="
					+ new String(fileName.getBytes(), "ISO8859-1"));
			wb.write(out);
			out.flush();
			response.flushBuffer();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

		}
	}

	/**
	 * 功能：创建HSSFSheet工作簿
	 * 
	 * @param wb
	 *            HSSFWorkbook
	 * @param sheetName
	 *            String
	 * @return HSSFSheet
	 */
	public static HSSFSheet createSheet(HSSFWorkbook wb, String sheetName) {
		HSSFSheet sheet = wb.createSheet(sheetName);
		sheet.setDefaultColumnWidth(12);
		sheet.setGridsPrinted(false);
		sheet.setDisplayGridlines(false);
		return sheet;
	}

	/**
	 * 功能：创建HSSFRow
	 * 
	 * @param sheet
	 *            HSSFSheet
	 * @param rowNum
	 *            int
	 * @param height
	 *            int
	 * @return HSSFRow
	 */
	public static HSSFRow createRow(HSSFSheet sheet, int rowNum, int height) {
		HSSFRow row = sheet.createRow(rowNum);
		row.setHeight((short) height);
		return row;
	}

	/**
	 * 功能：创建CellStyle样式
	 * 
	 * @param wb
	 *            HSSFWorkbook
	 * @param backgroundColor
	 *            背景色
	 * @param foregroundColor
	 *            前置色
	 * @param font
	 *            字体
	 * @return CellStyle
	 */
	public static CellStyle createCellStyle(HSSFWorkbook wb,
			short backgroundColor, short foregroundColor, short halign,
			Font font) {
		CellStyle cs = wb.createCellStyle();
		cs.setAlignment(halign);
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cs.setFillBackgroundColor(backgroundColor);
		cs.setFillForegroundColor(foregroundColor);
		cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cs.setFont(font);
		return cs;
	}

	/**
	 * 功能：创建带边框的CellStyle样式
	 * 
	 * @param wb
	 *            HSSFWorkbook
	 * @param backgroundColor
	 *            背景色
	 * @param foregroundColor
	 *            前置色
	 * @param font
	 *            字体
	 * @return CellStyle
	 */
	public static CellStyle createBorderCellStyle(HSSFWorkbook wb,
			short backgroundColor, short foregroundColor, short halign,
			Font font) {
		CellStyle cs = wb.createCellStyle();
		cs.setAlignment(halign);
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cs.setFillBackgroundColor(backgroundColor);
		cs.setFillForegroundColor(foregroundColor);
		cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cs.setFont(font);
		cs.setBorderLeft(CellStyle.BORDER_THIN);
		cs.setBorderRight(CellStyle.BORDER_THIN);
		cs.setBorderTop(CellStyle.BORDER_THIN);
		cs.setBorderBottom(CellStyle.BORDER_THIN);
		cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
		cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
		return cs;
	}

	/**
	 * 功能：创建CELL
	 * 
	 * @param row
	 *            HSSFRow
	 * @param cellNum
	 *            int
	 * @param style
	 *            HSSFStyle
	 * @return HSSFCell
	 */
	public static HSSFCell createCell(HSSFRow row, int cellNum, CellStyle style) {
		HSSFCell cell = row.createCell(cellNum);
		cell.setCellStyle(style);
		return cell;
	}

	/**
	 * 功能：合并单元格
	 * 
	 * @param sheet
	 *            HSSFSheet
	 * @param firstRow
	 *            int
	 * @param lastRow
	 *            int
	 * @param firstColumn
	 *            int
	 * @param lastColumn
	 *            int
	 * @return int 合并区域号码
	 */
	public static int mergeCell(HSSFSheet sheet, int firstRow, int lastRow,
			int firstColumn, int lastColumn) {
		return sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow,
				firstColumn, lastColumn));
	}

	/**
	 * 功能：合并单元格
	 * 
	 * @param sheet
	 *            HSSFSheet
	 * @param firstRow
	 *            int
	 * @param lastRow
	 *            int
	 * @param firstColumn
	 *            int
	 * @param lastColumn
	 *            int
	 * @return cellReangeAddress
	 */
	public static CellRangeAddress mergeCellRegion(HSSFSheet sheet,
			int firstRow, int lastRow, int firstColumn, int lastColumn) {
		return new CellRangeAddress(firstRow, lastRow, firstColumn, lastColumn);
	}

	/**
	 * 功能：创建字体
	 * 
	 * @param wb
	 *            HSSFWorkbook
	 * @param boldweight
	 *            short
	 * @param color
	 *            short
	 * @return Font
	 */
	public static Font createFont(HSSFWorkbook wb, short boldweight,
			short color, short size) {
		Font font = wb.createFont();
		font.setBoldweight(boldweight);
		font.setColor(color);
		font.setFontHeightInPoints(size);
		return font;
	}

	/**
	 * 设置合并单元格的边框样式
	 * 
	 * @param sheet
	 *            HSSFSheet
	 * @param ca
	 *            CellRangAddress
	 * @param style
	 *            CellStyle
	 */
	public static void setRegionStyle(HSSFSheet sheet, CellRangeAddress ca,
			CellStyle style) {
		for (int i = ca.getFirstRow(); i <= ca.getLastRow(); i++) {
			HSSFRow row = HSSFCellUtil.getRow(i, sheet);
			for (int j = ca.getFirstColumn(); j <= ca.getLastColumn(); j++) {
				HSSFCell cell = HSSFCellUtil.getCell(row, j);
				cell.setCellStyle(style);
			}
		}
	}

	/**
	 * 导入图片，保持图片尺寸
	 * 
	 * @param double
	 *            图片左上角相对excel位置（列位置）
	 * @param double
	 *            图片左上角相对excel位置（行位置）
	 * @param File
	 *            图片文件
	 * @param WritableSheet
	 *            操作sheet
	 */
	public  static void addImage(int col, int column, int row, String fileName,
			String imgURL, HSSFWorkbook wb) throws Exception {
        
		// 单元格为标,以左上为起点,想右移,范围0-1023 dx1 must be between 0 and 1023
		int x1 = 0;
		// 单元格为标,以左上为起点,想下移,范围0-1023 dy1 must be between 0 and 255
		int y1 = 0;
		// 单元格为标,以右上为起点,想左移,范围0-1023 dx1 must be between 0 and 1023
		int x2 = 1023;
		// 单元格为标,以右下为起点,想上移,范围0-1023 dy1 must be between 0 and 255
		int y2 = 255;

		// 先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		BufferedImage bufferImg = ImageIO.read(new File(imgURL));// 读取图片
		ImageIO.write(bufferImg, "PNG", byteArrayOut);// 写入图片
		HSSFSheet sheet = wb.getSheetAt(col);
		CellRangeAddress region=ExcelUtil.mergeCellRegion(sheet, row, row, 0, column);//先跨行合并行
		sheet.addMergedRegion(region);//设置
		Font font=ExcelUtil.createFont(wb, (short)1, HSSFColor.LIGHT_YELLOW.index, (short)1);
		CellStyle cellStyle3=ExcelUtil.createBorderCellStyle(wb, HSSFColor.LIGHT_CORNFLOWER_BLUE.index, HSSFColor.LIME.index,HSSFCellStyle.ALIGN_CENTER, font);
		ExcelUtil.setRegionStyle(sheet, region, cellStyle3);//样式
        HSSFRow rows = sheet.createRow(row);
        rows.setHeightInPoints(260); 
		sheet.setColumnWidth(9, (short) (35.7 *160)); 
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		HSSFClientAnchor anchor = new HSSFClientAnchor(x1,y1,x2,y2,(short)(column-8),row,(short)(column-1),row);
	      anchor.setAnchorType(3);  
        patriarch.createPicture(anchor, wb.addPicture(byteArrayOut
				.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));
       
		writeWorkbookRequest(wb,fileName);
	}
	
	
	
	public static void addImageToExcel(int sheetIndex,int row,String imgPath,String filename, HSSFWorkbook wb) throws Exception
	{
		// 单元格为标,以左上为起点,想右移,范围0-1023 dx1 must be between 0 and 1023
		int x1 = 0;
		// 单元格为标,以左上为起点,想下移,范围0-1023 dy1 must be between 0 and 255
		int y1 = 0;
		// 单元格为标,以右上为起点,想左移,范围0-1023 dx1 must be between 0 and 1023
		int x2 = 1023;
		// 单元格为标,以右下为起点,想上移,范围0-1023 dy1 must be between 0 and 255
		int y2 = 255;
		
		// 先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		BufferedImage bufferImg = ImageIO.read(new File(imgPath));// 读取图片
		ImageIO.write(bufferImg, "JPG", byteArrayOut);// 写入图片
		HSSFSheet sheet = wb.getSheetAt(sheetIndex);
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		
		//开始画图
		HSSFClientAnchor anchor = new HSSFClientAnchor(x1, y1, x2, y2,(short) 0, row, (short) 10, row + 23);
		patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
		
		writeWorkbookRequest(wb,filename);
	}
  	
	

}
