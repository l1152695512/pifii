package com.yf.base.chart;

import java.io.File;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class ErrorHandler {
	private static Logger logger = null;
	static HashMap<String, String> errorMessages;

	static {
		logger = Logger.getLogger(ErrorHandler.class.getName());

		errorMessages = new HashMap();

		errorMessages.put("E100", " Insufficient data.");
		errorMessages.put("E101", " Width/height not provided.");
		errorMessages.put("E102", " Insufficient export parameters.");
		errorMessages.put("E400", " Bad request.");
		errorMessages.put("E401", " Unauthorized access.");
		errorMessages.put("E403", " Directory write access forbidden.");
		errorMessages.put("E404", " Export Resource not found.");
		errorMessages.put("E507", " Insufficient Storage.");
		errorMessages.put("E508", " Server Directory does not exist.");
		errorMessages.put("W509", " File already exists.");
		errorMessages
				.put("W510",
						" Export handler's Overwrite setting is on. Trying to overwrite.");
		errorMessages.put("E511",
				" Overwrite forbidden. File cannot be overwritten");

		errorMessages.put("E512", "Intelligent File Naming is Turned off.");
		errorMessages
				.put(
						"W513",
						"Background Color not specified. Taking White (FFFFFF) as default background color.");

		errorMessages
				.put("W514",
						"Using intelligent naming of file by adding unique suffix to the exising name.");
		errorMessages.put("W515", "The filename has changed - ");

		errorMessages.put("E516", " Unable to encode buffered image.");
		errorMessages.put("E600", "Internal Server Error");
	}

	public static String getErrorForCode(String code) {
		if (errorMessages.containsKey(code)) {
			return (errorMessages.get(code));
		}

		return " Error/Warning : No Specific Message.";
	}

	public static String buildResponse(String eCodes, boolean isHTML) {
		StringTokenizer tokenizer = new StringTokenizer(eCodes, ",");

		StringBuffer err_buf = new StringBuffer();
		StringBuffer warn_buf = new StringBuffer();
		String errors = "";
		String notices = "";
		String errCode = null;

		while (tokenizer.hasMoreTokens()) {
			errCode = tokenizer.nextToken();
			if (errCode.length() > 0)
				if (errCode.indexOf("E") != -1) {
					err_buf.append(getErrorForCode(errCode));
				} else {
					warn_buf.append(getErrorForCode(errCode));
				}

		}

		if (err_buf.length() > 0)
			errors = ((isHTML) ? "<BR>" : "&") + "statusMessage="
					+ err_buf.substring(0) + ((isHTML) ? "<BR>" : "&")
					+ "statusCode=0";
		else
			errors = "statusMessage=successful&statusCode=1";
		if (warn_buf.length() > 0)
			notices = ((isHTML) ? "<BR>" : "&") + "notice="
					+ warn_buf.substring(0);
		logger.info("Errors=" + errors);
		logger.info("Notices=" + notices);
		return errors + notices;
	}

	public static String checkServerSaveStatus(String basePath, String fileName) {
		StringBuffer retCodes = new StringBuffer();

		String pathToSaveFolder = basePath + File.separator
				+ FusionChartsExportHelper.SAVEPATH;

		File saveFolder = new File(pathToSaveFolder);
		if (!(saveFolder.exists())) {
			retCodes.append("E508,");
			return retCodes.toString();
		}

		if (!(saveFolder.canWrite())) {
			retCodes.append("E403,");
			return retCodes.toString();
		}

		String completeFilePath = pathToSaveFolder + File.separator + fileName;
		File saveFile = new File(completeFilePath);

		if (saveFile.exists()) {
			retCodes.append("W509,");

			if (FusionChartsExportHelper.OVERWRITEFILE) {
				retCodes.append("W510,");

				if (!(saveFile.canWrite())) {
					retCodes.append("W511,");
				}

			} else if (!(FusionChartsExportHelper.INTELLIGENTFILENAMING)) {
				retCodes.append("E512,");
			}

		}

		return retCodes.toString();
	}
}