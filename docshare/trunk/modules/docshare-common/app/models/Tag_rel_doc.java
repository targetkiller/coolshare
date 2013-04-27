package models;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(name="tag_rel_doc")
public class Tag_rel_doc extends GenericModel{
	@Id
	@Column(name="id",nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long id;
	
	@Column(name="tag_id",nullable=false)
	public long tagId;
	
	@Column(name="doc_id",nullable=false)
	public long docId;

}
