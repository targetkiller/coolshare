package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;


@Entity
@Table(name="collection")
public class Collection extends GenericModel{
	@Id
	@Column(name="id",nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long id;
	
	@Column(name="user_id",nullable=false)
	public long userId;
	
	@Column(name="doc_id",nullable=false)
	public long docId;
	
	@Column(name="time",nullable=false)
	public long time;
	
	public static Collection getCollectionByUserIdAndDocId(long userId,long docId){
		return Collection.find("user_id = ? and doc_id = ?", userId,docId).first();
	}

}
