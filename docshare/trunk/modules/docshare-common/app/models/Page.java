package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

/**
 * 文档的每页内容，第0页为文档简介,可不填
 * @author shunai
 *
 */
@Entity
@Table(name="page")
public class Page extends GenericModel{
	@Id
	@Column(name="id",nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long id;
	
	@Column(name="doc_id",nullable=false)
	public long docId;
	
	@Column(name="content")
	public String content;
	
	@Column(name="pageNum",nullable=false)
	public int pageNum;
	

}
