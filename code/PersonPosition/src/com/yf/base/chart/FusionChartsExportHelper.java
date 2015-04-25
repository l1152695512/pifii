package com.yf.base.chart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

import com.yf.base.chart.beans.ChartMetadata;
import com.yf.base.chart.beans.ExportBean;

public class FusionChartsExportHelper {
	private static HashMap<String, String> mimeTypes = new HashMap();
	private static HashMap<String, String> extensions = new HashMap();
	private static HashMap<String, String> handlerAssociationsMap = new HashMap();
	private static Logger logger = null;
	public static String EXPORTHANDLER;
	public static String RESOURCEPATH;
	public static String SAVEPATH;
	public static String HTTP_URI;
	public static String TMPSAVEPATH;
	public static boolean OVERWRITEFILE;
	public static boolean INTELLIGENTFILENAMING;
	public static String FILESUFFIXFORMAT;

	static {
		handlerAssociationsMap.put("PDF", "PDF");
		handlerAssociationsMap.put("JPEG", "IMG");
		handlerAssociationsMap.put("JPG", "IMG");
		handlerAssociationsMap.put("PNG", "IMG");
		handlerAssociationsMap.put("GIF", "IMG");

		mimeTypes.put("jpg", "image/jpeg");
		mimeTypes.put("jpeg", "image/jpeg");
		mimeTypes.put("png", "image/png");
		mimeTypes.put("gif", "image/gif");
		mimeTypes.put("pdf", "application/pdf");

		extensions.put("jpeg", "jpg");
		extensions.put("jpg", "jpg");
		extensions.put("png", "png");
		extensions.put("gif", "gif");
		extensions.put("pdf", "pdf");

		logger = Logger.getLogger(FusionChartsExportHelper.class.getName());

		EXPORTHANDLER = "FCExporter_";

		RESOURCEPATH = "Resources/";

		SAVEPATH = "./";

		HTTP_URI = "http://yourdomain.com/";

		TMPSAVEPATH = "";

		OVERWRITEFILE = false;

		INTELLIGENTFILENAMING = true;
		FILESUFFIXFORMAT = "TIMESTAMP";

		Properties props = new Properties();
		try {
			props.load(FusionChartsExportHelper.class
					.getResourceAsStream("/fusioncharts_export.properties"));

			EXPORTHANDLER = props.getProperty("EXPORTHANDLER", "FCExporter_");

			RESOURCEPATH = props.getProperty("RESOURCEPATH", "Resources"
					+ File.separator);
			SAVEPATH = props.getProperty("SAVEPATH", "./");
			HTTP_URI = props.getProperty("HTTP_URI", "http://yourdomain.com/");
			TMPSAVEPATH = props.getProperty("TMPSAVEPATH", "");

			String OVERWRITEFILESTR = props.getProperty("OVERWRITEFILE",
					"false");
			OVERWRITEFILE = new Boolean(OVERWRITEFILESTR).booleanValue();

			String INTELLIGENTFILENAMINGSTR = props.getProperty(
					"INTELLIGENTFILENAMING", "true");
			INTELLIGENTFILENAMING = new Boolean(INTELLIGENTFILENAMINGSTR)
					.booleanValue();

			FILESUFFIXFORMAT = props.getProperty("FILESUFFIXFORMAT",
					"TIMESTAMP");
		} catch (NullPointerException e) {
			logger.info("NullPointer: Properties file not FOUND");
		} catch (FileNotFoundException e) {
			logger.info("Properties file not FOUND");
		} catch (IOException e) {
			logger.info("IOException: Properties file not FOUND");
		}
	}

	public static ExportBean parseExportRequestStream(
			HttpServletRequest exportRequestStream) {
		ExportBean exportBean = new ExportBean();

		String stream = exportRequestStream.getParameter("stream");

		String parameters = exportRequestStream.getParameter("parameters");

		ChartMetadata metadata = new ChartMetadata();

		String strWidth = exportRequestStream.getParameter("meta_width");
		metadata.setWidth(Integer.parseInt(strWidth));
		String strHeight = exportRequestStream.getParameter("meta_height");
		metadata.setHeight(Integer.parseInt(strHeight));

		String bgColor = exportRequestStream.getParameter("meta_bgColor");

		String DOMId = exportRequestStream.getParameter("meta_DOMId");

		metadata.setDOMId(DOMId);

		metadata.setBgColor(bgColor);

		exportBean.setMetadata(metadata);
		exportBean.setStream(stream);

		HashMap exportParamsFromRequest = bang(parameters);

		exportBean.addExportParametersFromMap(exportParamsFromRequest);

		return exportBean;
	}

	public static String getExporterFilePath(String strFormat) {
		String exporterSuffix = (handlerAssociationsMap.get(strFormat) != null) ? (String) handlerAssociationsMap
				.get(strFormat)
				: strFormat;
		String path = RESOURCEPATH + EXPORTHANDLER
				+ exporterSuffix.toUpperCase() + ".jsp";
		return path;
	}

	public static HashMap<String, String> bang(String strParams) {
		HashMap params = new HashMap();
		StringTokenizer stPipe = new StringTokenizer(strParams, "|");

		while (stPipe.hasMoreTokens()) {
			String keyValue = stPipe.nextToken();
			String[] keyValueArr = keyValue.split("=");
			if (keyValueArr.length > 1)
				params.put(keyValueArr[0].toLowerCase(), keyValueArr[1]);
		}

		return params;
	}

	public static HashMap<String, String> getMimeTypes() {
		return mimeTypes;
	}

	public static String getMimeTypeFor(String format) {
		return (mimeTypes.get(format));
	}

	public static String getExtensionFor(String format) {
		return (extensions.get(format));
	}

	public static String getUniqueFileName(String filePath, String extension) {
		UUID uuid = UUID.randomUUID();
		String uid = uuid.toString();
		String uniqueFileName = filePath + "." + extension;
		do {
			uniqueFileName = filePath;
			if (!(FILESUFFIXFORMAT.equalsIgnoreCase("TIMESTAMP"))) {
				uniqueFileName = uniqueFileName + uid + "_" + Math.random();
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("dMyHms");
				String date = sdf.format(Calendar.getInstance().getTime());
				uniqueFileName = uniqueFileName + uid + "_" + date + "_"
						+ Calendar.getInstance().getTimeInMillis();
			}
			uniqueFileName = uniqueFileName + "." + extension;
		} while (new File(uniqueFileName).exists());
		return uniqueFileName;
	}
}
