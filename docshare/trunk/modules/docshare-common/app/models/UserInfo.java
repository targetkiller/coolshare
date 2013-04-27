package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
@Entity
@Table(name="user_info")
public class UserInfo extends GenericModel{
	@Id
	@Column(name="id",nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long id;
	
	@Column(name="user_id",nullable=false)
	public long userId;
	
	@Column(name="sexual",length=8)
	public int sexual;
	
	@Column(name="long_tel",length=32)
	public String longTel;
	
	@Column(name="short_tel",length=32)
	public String shortTel;
	
	@Column(name="email",length=64)
	public String email;
	
	@Column(name="birthday",length=32)
	public String birthday;
	
	@Column(name="qq",length=32)
	public String qq;
	
	@Column(name="msn",length=32)
	public String msn;
	
	@Column(name="address",length=100)
	public String address;
	
	@Column(name="weibo",length=100)
	public String weibo;
}

