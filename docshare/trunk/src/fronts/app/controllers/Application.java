package controllers;

import play.*;
import play.db.jpa.Model;
import play.mvc.*;

import java.text.SimpleDateFormat;
import java.util.*;

import org.scauhci.enumvalue.DocumentType;
import org.scauhci.util.FileUtil;

import models.*;
import models.Collection;

public class Application extends Controller {

	public static void Home() {
		render();
	}

	public static void LookForRandom() {
		List<Document> documentList=Document.find("status = ? order by id desc",DocumentType.common.value).fetch(40);
		for(Document doc:documentList){
			doc.iconLocation=FileUtil.getFileTypeIconURL(doc.sufix);
		    doc.initTagList();
			User user=User.findById(doc.userId);
			if(user!=null){
				doc.authorName=user.name;
			}
		}
		render(documentList);
	}

	public static void HotCollection() {
		List<Document> documentList = Document.getHotCollections();
		for(Document doc:documentList){
			doc.initTagList();
		}
		renderArgs.put("documentList", documentList);
		render();
	}

	public static void HotDownload() {
		List<Document> documentList = Document.find(
				"status = ? order by downloadNum",DocumentType.common.value).fetch(40);
		for (Document doc : documentList) {
			doc.iconLocation=FileUtil.getFileTypeIconURL(doc.sufix);
			doc.initTagList();
			User u = User.findById(doc.userId);
			if (u != null) {
				doc.authorName = u.name;
			}
		}
		render(documentList);
	}

	public static void SearchResult(String tagName,String keyWord,int page) {
		int defaultLength = 40;
		int pageCount = 1;
		List<Document> docs = null;

		if(page == 0)
			page = 1;
		
		if(keyWord == null && tagName ==null)
			LookForRandom();
		
		if(tagName == null){
			long count = Document.count("title like ? or summary like ?",
					"%" + keyWord + "%","%" + keyWord + "%");
			pageCount = (int)((count % defaultLength == 0) ? count / defaultLength :  count / defaultLength + 1);
			docs = Document.find("title like ? or summary like ?",
					"%" + keyWord + "%","%" + keyWord + "%").fetch((int)page,defaultLength);
		}else{
			Tag tag = Tag.find("byName",tagName).first();
			List<Tag_rel_doc> trds = Tag_rel_doc.find("tagId = ?", tag.id).fetch();
			docs = new LinkedList<Document>();
			for(Tag_rel_doc trd : trds){
				Document doc = Document.findById(trd.docId);
				docs.add(doc);
			}
		}

		for(Document doc : docs){
        	doc.initTagList();
        	doc.initAuthorName();
        }

		if(tagName != null){
			pageCount = 
			docs.size() % defaultLength == 0 ? docs.size()/defaultLength : docs.size() / defaultLength + 1;
		}
		int pageStart = (page - 5) > 0 ? page - 5 : 1;
		int pageEnd = (page + 5) < pageCount ? page + 5 : pageCount;
		List<Document> documentList = docs;
		String tag = tagName,key = keyWord;
		List<Integer> pageList = new ArrayList();
		for(int i = pageStart;i<=pageEnd;i++){
			pageList.add(i);
		}
		render(documentList,tag,key,pageCount,page,pageList);
    }
	public static void aboutus(){
		render();
	}
	
}