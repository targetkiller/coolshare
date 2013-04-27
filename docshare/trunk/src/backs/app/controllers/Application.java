package controllers;

import play.*;
import play.data.validation.Required;
import play.mvc.*;

import java.util.*;

import org.scauhci.enumvalue.DefauleValue;
import org.scauhci.enumvalue.TagType;

import models.*;
import models.Collection;

@With(UserSecure.class)
public class Application extends Controller {

	public static void index() {
		render();
	}

	public static void ajaxAddCate(String name) {
		flash.keep();
		int status = 0;
		Map map = new HashMap();
		Cate cate = Cate.find("user_id = ? AND name = ?",
				UserSecure.getUserId(), name).first();
		if (name == null || name == "" || cate != null) {
			status = DefauleValue.failStatus;
		} else {
			Cate c = new Cate();
			c.name = name;
			c.userId = UserSecure.getUserId();
			c.save();
			status = DefauleValue.successStatus;
			map.put("id", c.id);
		}
		map.put("status", status);
		renderJSON(map);
	}

	public static void tag() {
		List<Tag> myTagList = Tag.getTagListByUserId(UserSecure.getUserId());
		List<Tag> tagList = Tag.findTagByStatus(TagType.systemTag);
		render(myTagList, tagList);
	}

	public static void addTag(long[] tag) {
		long haveTagNum = Tag.getCountTagByUserId(UserSecure.getUserId());
		if (haveTagNum < 5) {
			long canAddNum = 5 - haveTagNum;
			if (tag != null) {
				for (int i = 0, j = 0; j < canAddNum && i < tag.length; i++, j++) {
					Tag_rel_user tr = Tag_rel_user
							.getTag_rel_userBytagId(tag[i]);
					if (tr == null) {
						Tag_rel_user tru = new Tag_rel_user();
						tru.tagId = tag[i];
						tru.userId = UserSecure.getUserId();
						tru.save();
					} else {
						j--;
					}
				}
			}
		}
		tag();
	}
	public static void deleteTag(long tagId){
		Tag_rel_user tru=Tag_rel_user.find("userId = ? AND tagId = ?", UserSecure.getUserId(),tagId).first();
		if(tru!=null){
			Tag tag=Tag.findById(tagId);
			tru.delete();
		}
		tag();
	}
	public static void addUserTag(@Required()String tagName){
		if(validation.hasErrors()||Tag_rel_user.getCountUserTag(UserSecure.getUserId())>=5){
			tag();
		}
		List<Tag> tmpTagList= Tag.find("select t from Tag t,Tag_rel_user tru where t.name = ? AND t.id = tru.tagId AND tru.userId = ?",tagName,UserSecure.getUserId()).fetch();
		if(tmpTagList!=null&&tmpTagList.size()>0){
			tag();
		}
		Tag tag=Tag.find("name = ?",tagName).first();
		Tag_rel_user tru=new Tag_rel_user();
		if(tag==null){
			tag=new Tag();
			tag.name=tagName;
			tag.status=TagType.userTag;
			tag.save();
		}
		tru.tagId=tag.id;
		tru.userId=UserSecure.getUserId();
		tru.save();
		tag();
	}
}