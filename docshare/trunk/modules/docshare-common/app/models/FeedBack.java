package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;


@Entity
@Table(name="feedback")
public class FeedBack extends GenericModel{
	@Id
	@Column(name="id",nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long id;
	
	@Column(name="user_id",nullable=false)
	public long userId;
	
	@Column(name="title",nullable=false,length=100)
	public String title;
	
	@Column(name="content",nullable=false,length=1024)
	public String content;
	
	@Column(name="name",nullable=false,length=32)
	public String name;
	
	@Column(name="serious",nullable=false,length=8)
	public int serious;
	
	@Column(name="email",nullable=false,length=32)
	public String email;
	
	public String getSeriousName(){
		String s="";
		switch(serious){
			case 1:s="不清楚";break;
			case 2:s="有问题,但不影响我使用";break;
			case 3:s="严重影响我使用!";break;
			case 4:s="我受不了了!!";break;
			default:break;
		}
		return s;
	}	
}
