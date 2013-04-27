package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
@Entity
@Table(name="tag_rel_user")
public class Tag_rel_user extends GenericModel{
	@Id
	@Column(name="id",nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long id;
	
	@Column(name="user_id")
	public long userId;
	
	@Column(name="tag_id")
	public long tagId;
	
	public static Tag_rel_user getTag_rel_userBytagId(long tagId){
		return Tag_rel_user.find("tagId", tagId).first();
	}
    public static long getCountUserTag(long userId){
    	return Tag_rel_user.count("userId", userId);
    }
}
