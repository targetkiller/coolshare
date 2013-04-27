package controllers;

import play.*;
import play.db.jpa.Model;
import play.mvc.*;

import java.io.File;
import java.util.*;

import org.scauhci.enumvalue.DefauleValue;
import org.scauhci.enumvalue.DocumentType;
import org.scauhci.enumvalue.TagType;

import models.*;
import models.Collection;

public class ManagerControll extends Controller {
	public static void managerframe(){
		render();
	}
	public static void manageraddtag(){
		List<Tag> tagList=Tag.findTagByStatus(TagType.systemTag);
		renderArgs.put("tagList", tagList);
		render();
	}
	public static void managercheckdocs(){
		List<Document> documentList = Document.findAll();
		render(documentList);
	}
	public static void secureError(){
		flash.error("你没有管理员权限");
		long userId = UserSecure.getUserId();
		render(userId);
	}

	public static void deleteDoc(long docId) {
		int status = 0;
		Map map = new HashMap();
		Document document = Document.find("user_id = ? AND id = ?",
				Document.getUserId(docId), docId).first();
		if (document != null) {
			document.delete();
			Score.delete("doc_id",docId);
			Collection.delete("doc_id", docId);
			Tag_rel_doc.delete("doc_id", docId);
			status = DefauleValue.successStatus;
			map.put("status", status);
			renderJSON(map);
		} else {
			status = DefauleValue.failStatus;
			map.put("status", status);
			renderJSON(map);
		}

	}

	public static void checkfeedback(){
		List<FeedBack> feedbackList = new ArrayList<FeedBack>();
		feedbackList = FeedBack.findAll();
		renderArgs.put("feedbackList",feedbackList);
		render();
	}
	public static void checkdetailedfeedback(long Id){
		FeedBack feedback = FeedBack.findById(Id);
		render(feedback);
	}
	public static void deleteFeedback(long Id){
		int status=0;
		Map map = new HashMap();
		FeedBack feedback = FeedBack.findById(Id);
		if(feedback!=null){
			FeedBack.delete("Id=?", Id);
			status=200;
			map.put("status", status);
			renderJSON(map);
		}
		else{
			status=400;
			map.put("status", status);
			renderJSON(map);
		}
	}
	public static void addTag(String tagName){
		Map map=new HashMap();
		if(tagName==null){
			map.put("status", 400);
			map.put("message", "标签名不能为空");
			renderJSON(map);
		}
		Tag tag=Tag.findByTagName(tagName);
		if(tag!=null){
			map.put("status", 400);
			map.put("message", "该标签已存在");
			renderJSON(map);
		}
		tag=new Tag();
		tag.name=tagName;
		tag.status=TagType.systemTag;
		tag.save();
        map.put("status", 200);
        map.put("message", "添加成功!");
        renderJSON(map);
	}
	
	@Before(unless = {"secureError"})
	static void check(){
		boolean isAdmin = UserSecure.checkIsAdmin();
		if(!isAdmin){
			secureError();
		}
	}
	
}