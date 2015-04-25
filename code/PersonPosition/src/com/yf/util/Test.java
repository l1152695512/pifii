package com.yf.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import com.fusioncharts.exporter.FusionChartsExportHelper;
import com.fusioncharts.exporter.beans.ExportConfiguration;

public class Test {

	/**
	 * @param args
	 */
	/* 23 */   private static Logger logger = null;
	/*    */ 
	/* 27 */   public static String EXPORTHANDLER = "FCExporter_";
	/*    */ 
	/* 36 */   public static String RESOURCEPACKAGE = "com.fusioncharts.exporter.resources";
	/*    */ 
	/* 44 */   public static String SAVEPATH = "./";
	/*    */ 
	/* 49 */   public static String SAVEABSOLUTEPATH = "./";
	/*    */ 
	/* 56 */   public static String HTTP_URI = "http://yourdomain.com/";
	/*    */ 
	/* 61 */   public static String TMPSAVEPATH = "";
	/*    */ 
	/* 72 */   public static boolean OVERWRITEFILE = false;
	/*    */ 
	/* 74 */   public static boolean INTELLIGENTFILENAMING = true;
	/*    */ 
	/* 76 */   public static String FILESUFFIXFORMAT = "TIMESTAMP";
	public static void main(String[] args) {
	     Properties props = new Properties();
	     /*    */     try
	     /*    */     {
	     /* 91 */       props.load(
	     /* 92 */         FusionChartsExportHelper.class.getResourceAsStream("/fusioncharts_export.properties"));
	     /*    */ 
	     /* 94 */       EXPORTHANDLER = props.getProperty("EXPORTHANDLER", EXPORTHANDLER);
	     /* 95 */       RESOURCEPACKAGE = props.getProperty("RESOURCEPACKAGE", 
	     /* 96 */         RESOURCEPACKAGE);
	     /* 97 */       SAVEPATH = props.getProperty("SAVEPATH", SAVEPATH);
	                     System.out.println(SAVEPATH);

	     File f = new File(SAVEPATH);
	       boolean savePathAbsolute = f.isAbsolute();
	       System.out.println(savePathAbsolute);
	       logger.info("Is SAVEPATH on server absolute?" + savePathAbsolute);
	     
	     /*    */ 
	     /* 99 */       HTTP_URI = props.getProperty("HTTP_URI", HTTP_URI);
	     /* 100 */       TMPSAVEPATH = props.getProperty("TMPSAVEPATH", TMPSAVEPATH);
	     /*    */ 
	     /* 113 */       String OVERWRITEFILESTR = props.getProperty("OVERWRITEFILE", 
	     /* 114 */         "false");
	     /* 115 */       OVERWRITEFILE = new Boolean(OVERWRITEFILESTR).booleanValue();
	     /*    */ 
	     /* 117 */       String INTELLIGENTFILENAMINGSTR = props.getProperty(
	     /* 118 */         "INTELLIGENTFILENAMING", "true");
	     /* 119 */       INTELLIGENTFILENAMING = new Boolean(INTELLIGENTFILENAMINGSTR)
	     /* 120 */         .booleanValue();
	     /*    */ 
	     /* 122 */       FILESUFFIXFORMAT = props.getProperty("FILESUFFIXFORMAT", 
	     /* 123 */         FILESUFFIXFORMAT);
	     /*    */     }
	     /*    */     catch (NullPointerException e) {
	     /* 126 */       logger.info("NullPointer: Properties file not FOUND");
	     /*    */     }
	     /*    */     catch (FileNotFoundException e) {
	     /* 129 */       logger.info("Properties file not FOUND");
	     /*    */     }
	     /*    */     catch (IOException e)
	     /*    */     {
	     /* 134 */       logger.info("IOException: Properties file not FOUND");
	     /*    */     }
	     /*    */   }
}
