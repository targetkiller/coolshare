package org.scauhci.doc;

import java.io.File;

import play.Play;

public class DocUtils {
	
	public static boolean isWindow() {
		String os = System.getProperty("os.name").toLowerCase();
		return os.contains("window");
	}
	
	/**
	 * 获取文件的路径
	 */
	public static String getPath(String location, String sufix) {
		return Play.configuration.getProperty("docshare.attachments.path") + 
				File.separator + location + File.separator + location + sufix;
	}
	
	/**
	 * 获取生成swf的目录
	 * @return
	 */
	public static String getSwfDir(String location) {
		return Play.configuration.getProperty("docshare.attachments.path") + 
				File.separator + location + File.separator + location + "_preview.swf";
	}

}
