package org.scauhci.util;

public class FileUtil {
	/**
	 * 获取文件后缀名,包括点
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileExtension(String fileName) {
		String sufix = null;
		if (fileName != null) {
			String temp = fileName.trim();
			int dot = temp.lastIndexOf(".");
			if (dot >= 0||dot>temp.length()) {
				sufix = temp.substring(dot+1, temp.length());
			}
		}
		return sufix;
	}

	/**
	 * 获取文件名，出去后缀名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileNameWithoutExtension(String fileName) {
		String name = null;
		if (fileName != null) {
			String temp = fileName.trim();
			int dot = temp.lastIndexOf(".");
			if (dot >= 0) {
				name = temp.substring(0, dot);
			}
		}
		return name;
	}
	
	
	public static String getFileTypeIconURL(String sufix){
		String url="";
		if("doc".equalsIgnoreCase(sufix)||"docx".equalsIgnoreCase(sufix)){
			url=FileTypeIconURL.docURL;
		}else if("pdf".equalsIgnoreCase(sufix)){
			url=FileTypeIconURL.pdfURL;
		}else if("jpg".equalsIgnoreCase(sufix)||"jpeg".equalsIgnoreCase(sufix)){
			url=FileTypeIconURL.jpegURL;
		}else if("gif".equalsIgnoreCase(sufix)){
			url=FileTypeIconURL.gifURL;
		}else if("png".equalsIgnoreCase(sufix)){
			url=FileTypeIconURL.pngURL;
		}else if("ppt".equalsIgnoreCase(sufix)||"pptx".equalsIgnoreCase(sufix)){
			url=FileTypeIconURL.pptURL;
		}else if("xls".equalsIgnoreCase(sufix)||"xlsx".equalsIgnoreCase(sufix)){
			url=FileTypeIconURL.xlsURL;
		}else if("txt".equalsIgnoreCase(sufix)){
			url=FileTypeIconURL.textURL;
		}
		return url;
	}
}
