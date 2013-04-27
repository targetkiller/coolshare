package controllers;
import models.Tag;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.scauhci.enumvalue.DefauleValue;

import play.Play;
import play.mvc.Controller;

import models.Collection;
import models.Document;
import models.Score;
import models.User;
import models.UserInfo;

public class Preview extends Controller {
	public static void Preview(long docId) {
		Document document=Document.findById(docId);
		List<Tag> tagList=Tag.getTagListByDocId(docId);
		if(document==null){
			render(tagList);
		}
		User user = User.findById(document.userId);
		UserInfo userInfo = null;
		if (user != null) {
			userInfo = UserInfo.find("user_id", document.userId).first();
		}
		int collectNum = Collection.find("doc_id", document.id).fetch().size();
		Collection collection = Collection.getCollectionByUserIdAndDocId(
				document.userId, document.id);
		if (collection != null) {
			flash.put("isCollected", true);
		} 
		int avgScore = Score.getAvgScore(docId);
		if(UserSecure.isConnected())
		{
			Score s = Score.find("docId = ? and userId = ?", docId,Long.parseLong(session.get("userId"))).first();
			boolean isScored=false;
			if(s != null){
				isScored=true;
			}
			render(tagList,collectNum, user, userInfo,document,avgScore,isScored);
		}
		
		render(tagList,collectNum, user, userInfo,document,avgScore,false);
	}

	public static void collect(long docId) {
		Map map=new HashMap();
		int status=0;
		String message=null;
		Document document=Document.findById(docId);
		if(document==null){
			status=DefauleValue.documentNotExist;
			message="该文档已经不存在，收藏失败!";
			map.put("status",status);
			map.put("message",message);
			renderJSON(map);
		}
		Collection col = Collection.getCollectionByUserIdAndDocId(UserSecure.getUserId(),
				document.id);
		if (col != null) {
			flash.put("isCollected", true);
			message = "你已收藏过该文档!";
			status = DefauleValue.failStatus;
		} else {
			Collection collection = new Collection();
			collection.docId = document.id;
			collection.userId = UserSecure.getUserId();
			collection.time = System.currentTimeMillis();
			collection.save();
			status = DefauleValue.successStatus;
			message = "收藏成功!";
		}
		map.put("status",status);
		map.put("message", message);
		renderJSON(map);
	}
	
	public static void scoreDoc(long docId,int score){
		final int SC = 200;
		if(session.get("userId") == null){
			HashMap map = new HashMap();
			map.put("status", 403);
			map.put("url", "/UserSecure/signin");
			renderJSON(map);
		}
		Score s = Score.find("docId = ? and userId = ?", docId,Long.parseLong(session.get("userId"))).first();
		if(s == null){
			s = new Score();
			s.docId = docId;
			s.userId = Long.parseLong(session.get("userId"));
			s.score = score;
			s.save();
		}
		else{ 
			s.score = score;
			s.save();
		}
		Map map = new HashMap();
		map.put("status", SC);
		map.put("score", Score.getAvgScore(docId));
		map.put("isScored", true);
		renderJSON(map);
	}
	public static void scoreAgain(long docId){
		Score score = Score.find("docId = ? and userId = ?", docId,UserSecure.getUserId()).first();
		int status=0;
		Map map = new HashMap();
		if(score!=null){
			Score.delete("docId = ? and userId = ?", docId,UserSecure.getUserId());
			Preview(docId);
			status = 200;
			map.put("status", status);
			map.put("score", Score.getAvgScore(docId));
			map.put("isScored", false);
		}
		status = 400;
		map.put("status", status);
		map.put("score", Score.getAvgScore(docId));
		map.put("isScored", true);
		renderJSON(map);
	}
}
