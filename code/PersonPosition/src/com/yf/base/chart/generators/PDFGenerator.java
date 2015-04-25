package com.yf.base.chart.generators;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.zip.Deflater;

import com.yf.base.chart.beans.ChartMetadata;
import com.yf.base.chart.beans.ExportBean;

public class PDFGenerator {
	private Logger logger = Logger.getLogger(PDFGenerator.class.getName());
	int pageIndex = 0;
	ArrayList<ExportBean> pagesData = new ArrayList();

	public PDFGenerator(String data, ChartMetadata metadata) {
		setBitmapData(data, metadata);
	}

	public void setBitmapData(String imageData_FCFormat, ChartMetadata metadata) {
		ExportBean exportBean = new ExportBean();
		exportBean.setStream(imageData_FCFormat);
		exportBean.setMetadata(metadata);

		this.pagesData.add(this.pageIndex, exportBean);
		this.pageIndex += 1;
	}

	private byte[] addImageToPDF(int id, boolean isCompressed) {
		byte[] imagePDFBytes = null;
		ByteArrayOutputStream imagePDFBAOS = new ByteArrayOutputStream();
		try {
			int imgObjNo = 6 + id * 3;

			byte[] bitmapImage = getBitmapData24(id);

			byte[] imgBinary = (isCompressed) ? compress(bitmapImage)
					: bitmapImage;

			int length = imgBinary.length;
			ChartMetadata metadata = (this.pagesData.get(id))
					.getMetadata();

			String imgObj = imgObjNo
					+ " 0 obj\n<<\n/Subtype /Image /ColorSpace /DeviceRGB /BitsPerComponent 8 /HDPI 72 /VDPI 72 "
					+ ((isCompressed) ? "/Filter /FlateDecode " : "")
					+ "/Width " + metadata.getWidth() + " /Height "
					+ metadata.getHeight() + " /Length " + length
					+ " >>\nstream\n";

			String imgObj2 = "endstream\nendobj\n";

			imagePDFBAOS.write(imgObj.getBytes());
			imagePDFBAOS.write(imgBinary);
			imagePDFBAOS.write(imgObj2.getBytes());
			imagePDFBytes = imagePDFBAOS.toByteArray();
			imagePDFBAOS.close();
		} catch (IOException e) {
			this.logger.severe("Exception while parsing image data for PDF: "
					+ e.toString());
		}

		return imagePDFBytes;
	}

	private byte[] getBitmapData24(int id) {
		byte[] imageData = null;
		ByteArrayOutputStream imageData24OS = new ByteArrayOutputStream();

		StringTokenizer rows = new StringTokenizer((this.pagesData
				.get(id)).getStream(), ";");

		this.logger.info("Parsing image data");
		StringTokenizer pixels = null;
		String[] pixelData = null;
		String color = null;
		int repeat = 0;

		String bgColor = (this.pagesData.get(id)).getMetadata()
				.getBgColor();
		if ((bgColor == null) || (bgColor.equals(""))) {
			(this.pagesData.get(id)).getMetadata().setBgColor(
					"FFFFFF");
			bgColor = "FFFFFF";
		}

		while (rows.hasMoreElements()) {
			pixels = new StringTokenizer((String) rows.nextElement(), ",");
			while (pixels.hasMoreElements()) {
				pixelData = ((String) pixels.nextElement()).split("_");
				color = pixelData[0];
				repeat = Integer.parseInt(pixelData[1]);

				if ((color == null) || (color.equals("")))
					color = bgColor;

				if (color.length() < 6) {
					color = "000000".substring(0, 6 - color.length()) + color;
				}

				byte[] rgbBytes = hexToBytes(color);
				byte[] repeatedBytes = repeatBytes(rgbBytes, repeat);
				try {
					imageData24OS.write(repeatedBytes);
					imageData24OS.flush();
				} catch (IOException e) {
					this.logger
							.severe("Exception while writing image data for PDF: "
									+ e.toString());
				}
			}

		}

		imageData = imageData24OS.toByteArray();
		try {
			imageData24OS.close();
		} catch (IOException e) {
			this.logger.severe("Exception while closing stream for PDF: "
					+ e.toString());
		}
		this.logger.info("Image data parsed successfully");
		return imageData;
	}

	public byte[] getPDFObjects(boolean isCompressed) {
		this.logger.info("Creating PDF specific objects.");

		ByteArrayOutputStream PDFBytesOS = new ByteArrayOutputStream();
		byte[] pdfBytes = null;

		String strTmpObj = "";

		ArrayList xRefList = new ArrayList();

		xRefList.add(0, "xref\n0 ");
		xRefList.add(1, "0000000000 65535 f \n");

		strTmpObj = "%PDF-1.3\n%{FC}\n";
		try {
			PDFBytesOS.write(strTmpObj.getBytes());

			strTmpObj = "1 0 obj<<\n/Author (FusionCharts)\n/Title (FusionCharts)\n/Creator (FusionCharts)\n>>\nendobj\n";
			xRefList.add(calculateXPos(String.valueOf(PDFBytesOS.size())));

			PDFBytesOS.write(strTmpObj.getBytes());

			strTmpObj = "2 0 obj\n<< /Type /Catalog /Pages 3 0 R >>\nendobj\n";
			xRefList.add(calculateXPos(String.valueOf(PDFBytesOS.size())));

			PDFBytesOS.write(strTmpObj.getBytes());

			strTmpObj = "3 0 obj\n<<  /Type /Pages /Kids [";
			for (int i = 0; i < this.pageIndex; ++i)
				strTmpObj = strTmpObj + ((i + 1) * 3 + 1) + " 0 R\n";

			strTmpObj = strTmpObj + "] /Count " + this.pageIndex
					+ " >>\nendobj\n";

			xRefList.add(calculateXPos(String.valueOf(PDFBytesOS.size())));

			PDFBytesOS.write(strTmpObj.getBytes());

			ChartMetadata metadata = null;

			this.logger.info("Gathering data for  each page");
			for (int itr = 0; itr < this.pageIndex; ++itr) {
				metadata = (this.pagesData.get(itr)).getMetadata();
				int iWidth = metadata.getWidth();
				int iHeight = metadata.getHeight();

				strTmpObj = ((itr + 2) * 3 - 2)
						+ " 0 obj\n<<\n/Type /Page /Parent 3 0 R \n/MediaBox [ 0 0 "
						+ iWidth + " " + iHeight
						+ " ]\n/Resources <<\n/ProcSet [ /PDF ]\n/XObject <</R"
						+ (itr + 1) + " " + ((itr + 2) * 3)
						+ " 0 R>>\n>>\n/Contents [ " + ((itr + 2) * 3 - 1)
						+ " 0 R ]\n>>\nendobj\n";
				xRefList.add(calculateXPos(String.valueOf(PDFBytesOS.size())));
				PDFBytesOS.write(strTmpObj.getBytes());

				xRefList.add(calculateXPos(String.valueOf(PDFBytesOS.size())));
				PDFBytesOS.write(getXObjResource(itr).getBytes());

				byte[] imgPDFBytes = addImageToPDF(itr, isCompressed);

				xRefList.add(calculateXPos(String.valueOf(PDFBytesOS.size())));
				PDFBytesOS.write(imgPDFBytes);
			}

			String xRef0 = ((String) xRefList.get(0)) + (xRefList.size() - 1)
					+ "\n";
			xRefList.set(0, xRef0);

			String trailer = getTrailer(PDFBytesOS.size(), xRefList.size() - 1);

			PDFBytesOS.write(arrayToString(xRefList, "").getBytes());

			PDFBytesOS.write(trailer.getBytes());

			String EOF = "%%EOF\n";

			PDFBytesOS.write(EOF.getBytes());
			pdfBytes = PDFBytesOS.toByteArray();
			this.logger.info("PDF data created successfully");
		} catch (IOException e) {
			this.logger.severe("Exception while writing PDF data: "
					+ e.toString());
		}
		return pdfBytes;
	}

	private String arrayToString(ArrayList<String> a, String separator) {
		StringBuffer result = new StringBuffer();
		if (a.size() > 0) {
			result.append(a.get(0));
			for (int i = 1; i < a.size(); ++i) {
				result.append(separator);
				result.append(a.get(i));
			}
		}
		return result.toString();
	}

	private String getXObjResource(int itr) {
		ChartMetadata metadata = (this.pagesData.get(itr))
				.getMetadata();
		return ((itr + 2) * 3 - 1)
				+ " 0 obj\n<< /Length "
				+ (24 + new StringBuilder().append(metadata.getWidth()).append(
						metadata.getHeight()).toString().length())
				+ " >>\nstream\nq\n" + metadata.getWidth() + " 0 0 "
				+ metadata.getHeight() + " 0 0 cm\n/R" + (itr + 1)
				+ " Do\nQ\nendstream\nendobj\n";
	}

	private String calculateXPos(String posn) {
		String paddedStr = "0000000000".substring(0, 10 - posn.length()) + posn;
		return paddedStr + " 00000 n \n";
	}

	private String getTrailer(int xrefpos, int numxref) {
		return "trailer\n<<\n/Size " + numxref
				+ "\n/Root 2 0 R\n/Info 1 0 R\n>>\nstartxref\n" + xrefpos
				+ "\n";
	}

	private byte[] compress(byte[] data) {
		this.logger.info("Compressing the image data");
		byte[] compressedData = null;

		Deflater compressor = new Deflater();
		int count;
		compressor.setLevel(9);

		compressor.setInput(data);
		compressor.finish();

		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);

		byte[] buf = new byte[1024];
		while (!(compressor.finished())) {
			count = compressor.deflate(buf);
			bos.write(buf, 0, count);
		}
		try {
			bos.close();
		} catch (IOException ex) {
		}
		compressedData = bos.toByteArray();
		this.logger.info("Image data compressed");
		return compressedData;
	}

	private byte[] hexToBytes(String hex) {
		return hexToBytes(hex.toCharArray());
	}

	private byte[] hexToBytes(char[] hex) {
		int length = 3;
		byte[] raw = new byte[length];
		for (int i = 0; i < length; ++i) {
			int high = Character.digit(hex[(i * 2)], 16);
			int low = Character.digit(hex[(i * 2 + 1)], 16);
			int value = high << 4 | low;

			raw[i] = (byte) (((value & 0xFF) <= 127) ? value : value - 256);
		}
		return raw;
	}

	private byte[] repeatBytes(byte[] bytes, int repeat) {
		byte[] repeatedBytes = new byte[bytes.length * repeat];
		int counter = 0;
		for (int i = 0; i < repeat; ++i)
			for (int j = 0; j < bytes.length; ++j)
				repeatedBytes[(counter++)] = bytes[j];

		return repeatedBytes;
	}
}