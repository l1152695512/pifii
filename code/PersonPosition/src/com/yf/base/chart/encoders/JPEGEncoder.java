package com.yf.base.chart.encoders;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

public class JPEGEncoder implements Encoder {
	public void encode(BufferedImage bufferedImage, OutputStream outputStream,
			float quality) throws Throwable {
		ImageOutputStream ios = null;
		try {
			Iterator writers = ImageIO.getImageWritersByFormatName("JPEG");
			ImageWriter writer = (ImageWriter) writers.next();

			JPEGImageWriteParam params = new JPEGImageWriteParam(null);
			params.setCompressionMode(2);
			params.setCompressionQuality(quality);
			params.setProgressiveMode(0);
			params.setDestinationType(new ImageTypeSpecifier(ColorModel
					.getRGBdefault(), ColorModel.getRGBdefault()
					.createCompatibleSampleModel(16, 16)));

			IIOImage image = new IIOImage(bufferedImage, null, null);

			ios = ImageIO.createImageOutputStream(outputStream);
			writer.setOutput(ios);
			writer.write(null, image, params);
			ios.close();
		} catch (IllegalArgumentException e) {
			if (ios != null)
				ios.close();

			throw new Throwable();
		} catch (IOException e) {
			if (ios != null)
				ios.close();

			throw new Throwable();
		}
	}

	public void encode(BufferedImage bufferedImage, OutputStream outputStream)
			throws Throwable {
		encode(bufferedImage, outputStream, 1.0F);
	}

	public void encode(BufferedImage bufferedImage, OutputStream outputStream,
			float quality, String format) throws Throwable {
		encode(bufferedImage, outputStream, quality);
	}

	public void encode(BufferedImage bufferedImage,
			FileImageOutputStream fileImageOutputStream, float quality)
			throws Throwable {
		Iterator writers = ImageIO.getImageWritersByFormatName("JPEG");
		ImageWriter writer = (ImageWriter) writers.next();

		JPEGImageWriteParam params = new JPEGImageWriteParam(null);
		params.setCompressionMode(2);
		params.setCompressionQuality(quality);
		params.setProgressiveMode(0);
		params.setDestinationType(new ImageTypeSpecifier(ColorModel
				.getRGBdefault(), ColorModel.getRGBdefault()
				.createCompatibleSampleModel(16, 16)));

		IIOImage image = new IIOImage(bufferedImage, null, null);

		writer.setOutput(fileImageOutputStream);
		writer.write(null, image, params);
		fileImageOutputStream.close();
	}

	public void encode(BufferedImage bufferedImage,
			FileImageOutputStream fileImageOutputStream) throws Throwable {
		encode(bufferedImage, fileImageOutputStream, 1.0F);
	}

	public void encode(BufferedImage bufferedImage,
			FileImageOutputStream fileImageOutputStream, float quality,
			String format) throws Throwable {
		encode(bufferedImage, fileImageOutputStream, quality);
	}
}
