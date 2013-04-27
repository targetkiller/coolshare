package controllers;

import java.io.File;
import java.util.ArrayList;

import org.scauhci.doc.DocUtils;

import models.Document;
import play.Play;
import play.mvc.Controller;
import play.mvc.With;
import play.mvc.Http.Header;

@With(UserSecure.class)
public class DownLoads extends Controller {

	public static void downloadDoc(long docId, String location, String sufix) {
		String path = getPath(location, sufix);
		Document doc = Document.findById(docId);
		if(null != doc) {
			doc.downloadNum += 1;
			doc.save();
			if(DocUtils.isWindow()) {
				File df = new File(path);
				if(df.exists()) {
					renderBinary(df, doc.name);
				}
			}else {
				nginx(path);
			}
		}
	}
	
	public static void nginx(String path) {
		Header h = new Header();
        h.name = "X-Accel-Redirect";
        h.values = new ArrayList<String>(1);
        h.values.add("/docshare/files/" + path);
        response.headers.put("X-Accel-Redirect", h);
	}
	
	private static String getPath(String location, String sufix) {
		return Play.configuration.getProperty("attachments.path") + location + File.separator + location + "." + sufix;
	}
}
