package com.yf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 功能：将任意格式的视频转化为flv格式，有利于在线视频播放 前置条件：e:\\ff下有
 * ffmpeg.exe、ffplay.exep、threadGC2.dll、mencoder.exe、drv43260.dll ps: ffmpeg
 * 能解析的格式：asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等 mencoder 解析剩下的格式：wmv9，rm，rmvb
 * author：Administrator time：2010.12.9
 */
public class VideoUtil {
	public static void main(String[] args) {
		VideoUtil.convert("C:\\OCXRecordFiles\\2013-07-17\\2.mp4",
				"C:\\OCXRecordFiles\\2013-07-17\\2.flv",
				"C:\\OCXRecordFiles\\2013-07-17\\2.jpg");
//		List<String> inputFiles = new java.util.ArrayList<String>();
//		inputFiles.add("C:\\OCXRecordFiles\\2013-07-17\\1.flv");
//		inputFiles.add("C:\\OCXRecordFiles\\2013-07-17\\2.flv");
//		VideoUtil.videoMerge(inputFiles,"C:\\OCXRecordFiles\\2013-07-17\\12.flv");
	}

	/**
	 * 功能函数
	 * 
	 * @param inputFile
	 *            待处理视频，需带路径
	 * @param outputFile
	 *            处理后视频，需带路径
	 * @return
	 */
	public static boolean convert(String inputFile, String outputFile,
			String newimg) {
		if (!checkfile(inputFile)) {
			System.out.println(inputFile + " is not file");
			return false;
		}
		if (process(inputFile, outputFile, newimg)) {
			System.out.println("ok");
			return true;
		}
		return false;
	}

	/**
	 * 检查文件是否存在
	 * 
	 * @param path
	 * @return
	 */
	private static boolean checkfile(String path) {
		File file = new File(path);
		if (!file.isFile()) {
			return false;
		}
		return true;
	}

	/**
	 * 转换过程 ：先检查文件类型，在决定调用 processFlv还是processAVI
	 * 
	 * @param inputFile
	 * @param outputFile
	 * @return
	 */
	private static boolean process(String inputFile, String outputFile,
			String newimg) {
		int type = checkContentType(inputFile);
		boolean status = false;
		if (type == 0) {
			String inputType = inputFile.substring(
					inputFile.lastIndexOf(".") + 1, inputFile.length())
					.toLowerCase();
			if (inputType.equals("flv")) {
				processImg(inputFile, newimg);
				System.out.println("图片生成完成！！");
			} else {
				status = processFLV(inputFile, outputFile, newimg);// 直接将文件转为flv文件
				processImg(inputFile, newimg);
				System.out.println("直接转型后图片生成完成！");
			}
		} else if (type == 1) {
			String avifilepath = processAVI(type, inputFile);
			if (avifilepath == null) {
				return false;// avi文件没有得到
			}
			status = processFLV(avifilepath, outputFile, newimg);// 将avi转为flv
			processImg(inputFile, newimg);
		}
		System.out.println("================status:" + status);
		return status;
	}

	/**
	 * 检查视频类型
	 * 
	 * @param inputFile
	 * @return ffmpeg 能解析返回0，不能解析返回1
	 */
	private static int checkContentType(String inputFile) {
		String type = inputFile.substring(inputFile.lastIndexOf(".") + 1,
				inputFile.length()).toLowerCase();
		// ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
		if (type.equals("avi")) {
			return 0;
		} else if (type.equals("mpg")) {
			return 0;
		} else if (type.equals("wmv")) {
			return 0;
		} else if (type.equals("3gp")) {
			return 0;
		} else if (type.equals("mov")) {
			return 0;
		} else if (type.equals("mp4")) {
			return 0;
		} else if (type.equals("asf")) {
			return 0;
		} else if (type.equals("asx")) {
			return 0;
		} else if (type.equals("flv")) {
			return 0;
		}
		// 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
		// 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
		else if (type.equals("wmv9")) {
			return 1;
		} else if (type.equals("rm")) {
			return 1;
		} else if (type.equals("rmvb")) {
			return 1;
		}
		return 9;
	}

	/**
	 * ffmepg: 能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
	 * 
	 * @param inputFile
	 * @param outputFile
	 * @return
	 */
	private static boolean processFLV(String inputFile, String outputFile,
			String newimg) {
		if (!checkfile(inputFile)) {
			System.out.println(inputFile + " is not file");
			return false;
		}

		List<String> commend = new java.util.ArrayList<String>();
		// 低精度
		commend.add("e:\\ff\\ffmpeg");
		commend.add("-i");
		commend.add(inputFile);
		commend.add("-ab");
		commend.add("128");
		commend.add("-acodec");
		commend.add("libmp3lame");
		commend.add("-ac");
		commend.add("1");
		commend.add("-ar");
		commend.add("22050");
		commend.add("-r");
		commend.add("29.97");
		// 高精度 将-b 512 修改成-qscale 6
		commend.add("-qscale");
		commend.add("6");
//		commend.add("-b");
//		commend.add("512");
		commend.add("-y");
		commend.add(outputFile);
		StringBuffer test = new StringBuffer();
		for (int i = 0; i < commend.size(); i++)
			test.append(commend.get(i) + " ");
		System.out.println(test);
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			builder.start();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Mencoder: 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
	 * 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
	 * 
	 * @param type
	 * @param inputFile
	 * @return
	 */
	private static String processAVI(int type, String inputFile) {
		String name = inputFile.substring(0, inputFile.lastIndexOf("."))
				+ ".avi";
		File file = new File(name);
		if (file.exists())
			file.delete();
		List<String> commend = new java.util.ArrayList<String>();
		commend.add("e:\\ff\\mencoder");
		commend.add(inputFile);
		commend.add("-oac");
		commend.add("mp3lame");
		commend.add("-lameopts");
		commend.add("preset=64");
		commend.add("-ovc");
		commend.add("xvid");
		commend.add("-xvidencopts");
		commend.add("bitrate=1000");
		commend.add("-of");
		commend.add("avi");
		commend.add("-o");
		commend.add(name);

		StringBuffer test = new StringBuffer();
		for (int i = 0; i < commend.size(); i++) {
			test.append(commend.get(i) + " ");
		}
		System.out.println(test);
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			Process p = builder.start();
			/**
			 * 清空Mencoder进程 的输出流和错误流 因为有些本机平台仅针对标准输入和输出流提供有限的缓冲区大小，
			 * 如果读写子进程的输出流或输入流迅速出现失败，则可能导致子进程阻塞，甚至产生死锁。
			 */
			final InputStream is1 = p.getInputStream();
			final InputStream is2 = p.getErrorStream();
			new Thread() {
				public void run() {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(is1));
					try {
						String lineB = null;
						while ((lineB = br.readLine()) != null) {
							if (lineB != null) {
								System.out.println(lineB);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
			new Thread() {
				public void run() {
					BufferedReader br2 = new BufferedReader(
							new InputStreamReader(is2));
					try {
						String lineC = null;
						while ((lineC = br2.readLine()) != null) {
							if (lineC != null)
								System.out.println(lineC);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();

			// 等Mencoder进程转换结束，再调用ffmepg进程
			p.waitFor();
			System.out.println("who cares");
			return name;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

	/**
	 * 截图
	 * 
	 * @param inputFile
	 * @param newimg
	 */
	public static void processImg(String inputFile, String newimg) {
		List<String> commend = new java.util.ArrayList<String>();
		commend.add("e:\\ff\\ffmpeg");
		commend.add("-i");
		commend.add(inputFile);
		commend.add("-y");
		commend.add("-f");
		commend.add("image2");
		commend.add("-ss");
		commend.add("10");
		commend.add("-t");
		commend.add("0.001");
		commend.add("-s");
		commend.add("100x100");
		commend.add(newimg);

		StringBuffer test = new StringBuffer();
		for (int i = 0; i < commend.size(); i++) {
			test.append(commend.get(i) + " ");
		}
		System.out.println("图片生成命令：" + test.toString());
		ProcessBuilder builder = new ProcessBuilder();
		builder.command(commend);
		try {
			builder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 视频合并
	 * @param inputFiles
	 * @param outputFile
	 * @return
	 */
	private static boolean videoMerge(List<String> inputFiles, String outputFile) {
		try {
			for(int i = 0;i<inputFiles.size();i++){
				if (!checkfile(inputFiles.get(i))) {
					System.out.println(inputFiles.get(i) + " is not file");
					return false;
				}
			}
			
			List<String> commend = new java.util.ArrayList<String>();
			commend.add("e:\\ff\\mencoder");
			for(int j = 0;j<inputFiles.size();j++){
				commend.add(inputFiles.get(j));
			}
			commend.add("-oac");
			commend.add("pcm");
			commend.add("-ovc");
			commend.add("copy");
			commend.add("-o");
			commend.add(outputFile);

			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			builder.start();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}	
}