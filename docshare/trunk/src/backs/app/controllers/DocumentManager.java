package controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Cache;

import org.scauhci.enumvalue.DefauleValue;
import org.scauhci.enumvalue.DocumentType;
import org.scauhci.util.FileUtil;

import com.rabbitmq.tools.json.JSONUtil;
import com.sun.org.apache.xerces.internal.impl.dtd.models.DFAContentModel;

import models.Cate;
import models.Collection;
import models.Document;
import models.Score;
import models.Tag;
import models.Tag_rel_doc;
import models.User;

import play.Play;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.libs.Files;
import play.mvc.Controller;
import play.mvc.Scope;
import play.mvc.With;

@With(UserSecure.class)
public class DocumentManager extends Controller {

	/**
	 * 文档上传
	 * 
	 * @param document
	 */
	public static void upload() {
		List<Tag> tagList=Tag.findAll();
		List<Cate> cateList = Cate.find("user_id", UserSecure.getUserId())
				.fetch();
		if (cateList == null) {
			cateList = new ArrayList<Cate>();
		}
		render("DocumentManager/upload.html",tagList,cateList);
	}

	public static void addDocumentInfo() {
	}

	public static void editdoc(Document doc){
		Document document = Document.findById(doc.id);
		int status = 0;
		List<Cate> cateList = Cate.find("userId=?", UserSecure.getUserId()).fetch();
		if (cateList == null) {
			cateList = new ArrayList<Cate>();
		}
		if(document == null){	
			renderArgs.put("cateList", cateList);
			render();
		}
		else{
		renderArgs.put("cateList", cateList);
		renderArgs.put("document", document);
		render();
		}
	}
	public static void checkFileEdit(long docId,@Required()@MaxSize(value=100)String title,int type, long cateId,@MaxSize(value=255)String summary){
		checkAuthenticity();
		int status=0;
		String message="";
		Document document = Document.findById(docId);
		List<Cate> cateList = Cate.find("userId=?", document.userId).fetch();
		if (cateList == null) {
			cateList = new ArrayList<Cate>();
		}
		if (document == null||validation.hasErrors()) {
			status=400;
			message="修改失败!";
			renderArgs.put("cateList", cateList);
			renderArgs.put("status", status);
			renderArgs.put("message", message);
			render("DocumentManager/editdoc.html");
		} else {
			document.cateId = cateId;// 设置为未分类文档
			document.status = type;
			document.title = title;
			document.summary = summary;
			document.save();
			message="修改成功!";
			status=200;
			renderArgs.put("document", document);
			renderArgs.put("cateList", cateList);
			renderArgs.put("status", status);
			renderArgs.put("message", message);
			render("DocumentManager/editdoc.html");
		}
		
	}
	public static void checkFileInfo(long[] tag,File document,@Required()@MaxSize(value=100)String title,int type, long cateId,@MaxSize(value=255)String summary){
		checkAuthenticity();
		String documentName = null;
		if (document == null || validation.hasErrors()) {
			int status = DefauleValue.failStatus;
			String message = "文档上传失败!";
			List<Cate> cateList = Cate.find("user_id", UserSecure.getUserId())
					.fetch();
			List<Tag> tagList=Tag.findAll();
			if (cateList == null)
				cateList = new ArrayList<Cate>();
			render("DocumentManager/upload.html", cateList, status, message,tagList);

		} else {
			documentName = document.getName().trim();// 获取文件名
			String uuid = UUID.randomUUID() + "";
			Document doc = new Document();
			doc.cateId = cateId;// 设置为未分类文档
			String sufix = FileUtil.getFileExtension(documentName);
			doc.title = title;
			doc.downloadNum = 0;
			doc.name = documentName;
			doc.status = type;
			doc.userId = UserSecure.getUserId();
			String dir = Play.configuration.getProperty("attachments.path") + uuid;// 文件存放的目录
			File saveDir = new File(dir);
			if(!saveDir.exists()) {
				saveDir.mkdirs();
			}
			doc.summary = summary;
			doc.location = uuid;
			doc.sufix = sufix;
			String path = dir + File.separator + doc.location + "." + doc.sufix;
			File saveDoc = new File(path);
			Files.copy(document, saveDoc);
			doc.time = System.currentTimeMillis();
			doc.save();
			if(tag!=null){
				for(int i=0;i<5&&i<tag.length;i++){
					Tag_rel_doc trd=new Tag_rel_doc();
					trd.docId=doc.id;
					trd.tagId=tag[i];
					trd.save();
				}
			}
			document(Long.SIZE, "",null);
		}

	}

	public static void updateDocumentInfo(int status, long cateId, long docId,
			String summary, String title) {
		Document doc = Document.findById(docId);
		if (doc != null) {
			doc.status = status;
			doc.cateId = cateId;
			doc.summary = summary;
			doc.title = title;
			doc.save();
		}

	}

	public static void upload_in() {
		render();
	}

	public static void download() {

	}

	public static void collection(int page, String key) {
		if (page < 1) {// 如果请求页面小于1，则条到第一页
			page = 1;
		}
		List<Document> documentList = new ArrayList<Document>();
		long recordCount = 0;
		int pageLength = 5;// 每页多少条记录
		long pageNum = 0;// 每页pageLength条记录，计算多少页
		List pageList = new ArrayList();// 页脚数字list
		if (key == null || "".equals(key)) {// 搜索条件为空
			recordCount = Collection.count("user_id", UserSecure.getUserId());
			pageNum = recordCount / pageLength;
			if (recordCount % pageLength != 0) {
				pageNum += 1;
			}
			if (page > pageNum) {
				page = (int) pageNum;
			}
			List<Collection> collectionList = Collection.find("user_id",
					UserSecure.getUserId()).fetch(page, pageLength);
			for (Collection c : collectionList) {
				Document doc = Document.findById(c.docId);
				User u = User.findById(doc.userId);
				if (u != null) {
					doc.authorName = u.name;
				} else {
					doc.authorName = "";
				}
				doc.collectionNum = (int) Collection.count("doc_id", doc.id);
				documentList.add(doc);
			}
		} else {
			List<Collection> collectionList = Collection.find("user_id",
					UserSecure.getUserId()).fetch();
			for (Collection c : collectionList) {
				Document doc = Document.findById(c.docId);
				if (doc.name.contains(key) || doc.title.contains(key)) {
					recordCount++;
					User u = User.findById(doc.userId);
					if (u != null) {
						doc.authorName = u.name;
					} else {
						doc.authorName = "";
					}
					documentList.add(doc);
				}
			}
			pageNum = recordCount / pageLength;
			if (recordCount % pageLength != 0) {
				pageNum += 1;
			}
			if (page > pageNum) {
				page = (int) pageNum;
			}
			if (page > 0) {
				if (page * pageLength > recordCount) {
					documentList = documentList.subList(
							(page - 1) * pageLength, (int) recordCount);
				} else {
					documentList = documentList.subList(
							(page - 1) * pageLength, page * pageLength);
				}
			}
		}
		pageList = new ArrayList();
		if (pageNum - page >= 5) {
			for (long i = page; i <= page + 4; i++) {
				pageList.add(i);
			}
		} else {
			for (long i = page - 5 + (pageNum - page + 1); i <= pageNum; i++) {
				if (i > 0) {
					pageList.add(i);
				}

			}
		}
		renderArgs.put("key", key);
		renderArgs.put("recordCount", recordCount);
		renderArgs.put("documentList", documentList);
		renderArgs.put("pageNum", pageNum);
		renderArgs.put("nowPage", page);
		renderArgs.put("pageList", pageList);
		render();
	}

	public static void cancelCollect(long docId) {
		int status = 0;
		Map map = new HashMap();
		Collection collection = Collection.find("user_id = ? AND doc_id = ?",
				UserSecure.getUserId(), docId).first();
		if (collection != null) {
			collection.delete();
			status = 200;
			map.put("status", status);
			renderJSON(map);
		} else {
			status = 400;
			map.put("status", status);
			renderJSON(map);
		}
	}

	public static void deleteDoc(long docId) {
		int status = 0;
		Map map = new HashMap();
		Document document = Document.find("user_id = ? AND id = ?",
				UserSecure.getUserId(), docId).first();
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
	public static void document(int page, String key, String orderBy) {
		if(orderBy == null){
			orderBy = "time desc";
		}
		if (page < 1) {// 如果请求页面小于1，则条到第一页
			page = 1;
		}
		List<Document> documentList;
		long recordCount;
		int pageLength = 5;// 每页多少条记录
		long pageNum = 0;// 每页pageLength条记录，计算多少页
		List pageList = new ArrayList();// 页脚数字list
		if (key == null || "".equals(key)) {// 搜索条件为空
			recordCount = Document.count("user_id", UserSecure.getUserId());
			pageNum = recordCount / pageLength;
			if (recordCount % pageLength != 0) {
				pageNum += 1;
			}
			if (page > pageNum) {
				page = (int) pageNum;
			}
			documentList = Document.find("user_id = ? order by " + orderBy + " time desc",
					UserSecure.getUserId())
					.fetch(page, pageLength);
		} else {// 搜索条件不为空
			recordCount = Document.count(
					"user_id = ? AND ( title like ?  OR name like ? )",
					UserSecure.getUserId(), "%" + key + "%", "%" + key + "%");
			pageNum = recordCount / pageLength;
			if (recordCount % pageLength != 0) {
				pageNum += 1;
			}
			if (page > pageNum) {
				page = (int) pageNum;
			}
			documentList = Document.find(
					"user_id = ? AND ( title like ?  OR name like ?  ) " +
					"order by " + orderBy + " time desc",
					UserSecure.getUserId(), "%" + key + "%", "%" + key + "%")
					.fetch(page, pageLength);
		}
		for (Document d : documentList) {
			d.iconLocation = FileUtil.getFileTypeIconURL(d.sufix);
			Cate c = Cate.findById(d.cateId);
			if (c != null) {
				d.cateName = c.name;
			} else {
				d.cateName = "";
			}
			List<Score> sList = Score.find("doc_id", d.id).fetch();
			if (sList == null || sList.isEmpty()) {
				d.averageScore = "暂无评分";
			} else {
				int tol = 0;
				for (Score s : sList) {
					tol += s.score;
				}
				int temp = (int) (0.5 + (1.0 * tol) / sList.size());
				d.averageScore = "" + temp;
			}
		}
		if (pageNum - page >= 5) {
			for (long i = page; i <= page + 4; i++) {
				pageList.add(i);
			}
		} else {
			for (long i = page - 5 + (pageNum - page + 1); i <= pageNum; i++) {
				if (i > 0) {
					pageList.add(i);
				}
			}
		}
		if(orderBy.equals("time desc")||orderBy.equals("name asc"))
			flash.put("orderByName", "name desc");
		else flash.put("orderByName", "name asc");
		if(orderBy.equals("time desc")||orderBy.equals("cateId asc"))
			flash.put("orderByCateId", "cateId desc");
		else flash.put("orderByCateId", "cateId asc");
		if(orderBy.equals("time desc")||orderBy.equals("downloadNum asc"))
			flash.put("orderByDownloadNum", "downloadNum desc");
		else flash.put("orderByDownloadNum", "downloadNum asc");
		renderArgs.put("orderBy", orderBy);
		renderArgs.put("key", key);
		renderArgs.put("recordCount", recordCount);
		renderArgs.put("documentList", documentList);
		renderArgs.put("pageNum", pageNum);
		renderArgs.put("nowPage", page);
		renderArgs.put("pageList", pageList);
		render();
	}

}
