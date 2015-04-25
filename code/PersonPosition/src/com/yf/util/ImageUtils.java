package com.yf.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImageUtils {

	public static void reduceImgWidth(String imgsrc, int targetLength) {
		reduceImg(imgsrc, targetLength,true);
	}

	
	public static void reduceImgHeight(String imgsrc, int targetLength) {
		reduceImg(imgsrc, targetLength,false);
	}



	public static void reduceImg(String imgsrc, int targetLength,
			boolean isWidth) {
		try {
			File srcfile = new File(imgsrc);
			if (!srcfile.exists()) {
				return;
			}
			Image src = ImageIO.read(srcfile);

			// 原始图像高和宽
			int width = src.getWidth(null);
			int height = src.getHeight(null);

			int widthdist = 0;
			int heightdist = 0;

			// 确定图像的缩放后的高和宽
			if (isWidth) {
				if (targetLength >= width)
					return;
				double scale = targetLength * 1.0 / width;
				widthdist = targetLength;
				heightdist = (int) (height * scale);
			} else {
				if (targetLength >= height)
					return;
				double scale = targetLength * 1.0 / height;
				widthdist = (int) (width * scale);
				heightdist = targetLength;
			}

			BufferedImage tag = new BufferedImage((int) widthdist,
					(int) heightdist, BufferedImage.TYPE_INT_RGB);

			tag.getGraphics().drawImage(
					src.getScaledInstance(widthdist, heightdist,
							Image.SCALE_SMOOTH), 0, 0, null);
			String formatName = getFormatName(srcfile);// 此句必须在new
														// FileOutputStream之前，因为是替换原图，FileOutputStream对象未关闭之前，ImageInputStream无法获得文件格式。
			// Debug.println(formatName);
			FileOutputStream out = new FileOutputStream(srcfile);
			ImageIO.write(tag, formatName, out);
			out.flush();
			out.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private static String getFormatName(File o) {
		try {
			// Create an image input stream on the image
			ImageInputStream iis = ImageIO.createImageInputStream(o);

			// Find all image readers that recognize the image format
			Iterator iter = ImageIO.getImageReaders(iis);
			if (!iter.hasNext()) {
				// No readers found
				return null;
			}

			// Use the first reader
			ImageReader reader = (ImageReader) iter.next();

			// Close stream
			iis.close();

			// Return the format name
			return reader.getFormatName();
		} catch (IOException e) {
		}
		// The image could not be read
		return null;
	}

	public static void main(String[] args) {
		reduceImg("d:/test.jpg", 600, true);
	}

}
