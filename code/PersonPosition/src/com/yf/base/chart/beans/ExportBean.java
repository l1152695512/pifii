package com.yf.base.chart.beans;

import java.util.HashMap;
import java.util.Iterator;

public class ExportBean {
	private ChartMetadata metadata;
	private String stream;
	private HashMap<String, Object> exportParameters = null;

	public ExportBean() {
		this.exportParameters = new HashMap<String, Object>();

		this.exportParameters.put("exportfilename", "FusionCharts");
		this.exportParameters.put("exportaction", "download");
		this.exportParameters.put("exportargetwindow", "_self");
		this.exportParameters.put("exportformat", "PDF");
	}

	public ChartMetadata getMetadata() {
		return this.metadata;
	}

	public void setMetadata(ChartMetadata metadata) {
		this.metadata = metadata;
	}

	public String getStream() {
		return this.stream;
	}

	public void setStream(String stream) {
		this.stream = stream;
	}

	public HashMap<String, Object> getExportParameters() {
		return new HashMap<String, Object>(this.exportParameters);
	}

	public Object getExportParameterValue(String key) {
		return this.exportParameters.get(key);
	}

	public void setExportParameters(HashMap<String, Object> exportParameters) {
		this.exportParameters = exportParameters;
	}

	public void addExportParameter(String parameterName, Object value) {
		this.exportParameters.put(parameterName.toLowerCase(), value);
	}

	public void addExportParametersFromMap(
			HashMap<String, String> moreParameters) {
		this.exportParameters.putAll(moreParameters);
	}

	public String getParametersAndMetadataAsQueryString() {
		String queryParams = "";
		queryParams = queryParams + "?width=" + this.metadata.getWidth();
		queryParams = queryParams + "&height=" + this.metadata.getHeight();
		queryParams = queryParams + "&bgcolor=" + this.metadata.getBgColor();

		Iterator<String> iter = this.exportParameters.keySet().iterator();

		while (iter.hasNext()) {
			String key = iter.next();
			String value = (String) this.exportParameters.get(key);
			queryParams = queryParams + "&" + key + "=" + value;
		}

		return queryParams;
	}

	public String getMetadataAsQueryString(String filePath, boolean isError,
			boolean isHTML) {
		String queryParams = "";
		if (isError) {
			queryParams = queryParams + ((isHTML) ? "<BR>" : "&") + "width=0";
			queryParams = queryParams + ((isHTML) ? "<BR>" : "&") + "height=0";
		} else {
			queryParams = queryParams + ((isHTML) ? "<BR>" : "&") + "width="
					+ this.metadata.getWidth();
			queryParams = queryParams + ((isHTML) ? "<BR>" : "&") + "height="
					+ this.metadata.getHeight();
		}

		queryParams = queryParams + ((isHTML) ? "<BR>" : "&") + "DOMId="
				+ this.metadata.getDOMId();
		if (filePath != null) {
			queryParams = queryParams + ((isHTML) ? "<BR>" : "&") + "fileName="
					+ filePath;
		}

		return queryParams;
	}
}
