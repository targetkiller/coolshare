package controllers;

import java.io.File;

import models.Document;
import mqs.DocParserMQ;
import play.libs.Files;
import play.mvc.Controller;

public class Demos extends Controller {

	public static void go() {
		Document doc = new Document();
		doc.id = 1;
		doc.name = "文档1";
		doc.cateId = 1;
		doc.downloadNum = 0;
		doc.title = "大国崛起";
		doc.summary = "hello";
		DocParserMQ.publishDocument(doc);
		renderText("ok");
	}
	
	public static void goUpload() {
		render("Demos/upload.html");
	}
	
	public static void upload(File file, String xxx) {
		checkAuthenticity();
		if(validation.hasErrors()) {
			System.out.println("error!");
		}
		System.out.println(xxx);
		//File tarFile = new File("C:\\Users\\Public\\Desktop\\" + file.getName());
		//Files.copy(file, tarFile);
		goUpload();
	}
	
	public static void saveDoc() {
		Document doc = new Document();
		doc.name = "文档1";
		doc.cateId = 1;
		doc.downloadNum = 0;
		doc.title = "大国崛起";
		doc.summary = "hello";
		doc.location = "sfsaf";
		doc.save();
		renderText("ok");
	}
}
