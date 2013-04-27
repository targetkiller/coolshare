package org.scauhci.doc;

import java.io.File;
import java.io.IOException;

import play.Logger;
import play.Play;

/**
 * pdf 文档
 * @author shunai
 *
 */
public class PdfDoc extends SpecialDoc {

	/**
	 * 解析逻辑
	 * @return
	 */
	public PdfDoc parser() {
		File file = getFile();
		if(null != file) {
			Runtime r = Runtime.getRuntime();
			if(DocUtils.isWindow()) {
				String exe = Play.configuration.getProperty("window.swftools.location");
				String srcPath = getPath();
				String targetPath = DocUtils.getSwfDir(location);
				Process p = null;
				try {
					p = r.exec(exe + " " + srcPath + " -o " + targetPath);
				} catch (IOException e) {
					Logger.error("文件转换出错!");
					Logger.error(e.getMessage());
					e.printStackTrace();
				}finally {
					if(null != p) {
						p.destroy();
					}
				}
			}
		}
		return this;
	} 
	
	private String getPath() {
		return DocUtils.getPath(location, ".pdf");
	}
	
	public File getFile() {
		String path = getPath();
		return new File(path);
	}
}
