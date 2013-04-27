package models;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;

import org.scauhci.enumvalue.TagType;

import play.db.jpa.GenericModel;

/**
 * 标签
 * @author shunai
 *
 */
@Entity
@Table(name="tag")
public class Tag extends GenericModel{
	@Id
	@Column(name="id",nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long id;
	
	@Column(name="name",nullable=false,length=32)
	public String name;
	
	@Column(name="status",length=8)
	public int status;
	
	
	
	
	public static Tag findByTagName(String tagName){
		return Tag.find("name", tagName).first();
	}
	public static List<Tag> findTagByStatus(int tagType){
		return Tag.find("status",tagType).fetch();
	}
	public static List<Tag> getTagListByUserId(long userId){
		List<Tag> tagList=new ArrayList<Tag>();
		 List<Tag_rel_user> rel_userList=Tag_rel_user.find("userId", userId).fetch();
		 for(Tag_rel_user tru:rel_userList){
			 Tag tag=Tag.findById(tru.tagId);
			 if(tag!=null){
				 tagList.add(tag);
			 }
		 }
		 return tagList;
	}
	public static long getCountTagByUserId(long userId){
		 return Tag_rel_user.count("userId", userId);
	}
	
	public static List<Tag> getTagListByDocId(long docId){
		List<Tag> tagList=new ArrayList<Tag>();
		List<Tag_rel_doc> trfList=Tag_rel_doc.find("docId", docId).fetch();
		for(Tag_rel_doc trd:trfList){
			Tag tag=Tag.findById(trd.tagId);
			tagList.add(tag);
		}
		return tagList;
	}

}
