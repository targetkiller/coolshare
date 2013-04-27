package models;

import javax.persistence.Column;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.scauhci.util.FileUtil;

import controllers.UserSecure;

import play.data.validation.MaxSize;
import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

/**
 * 用户
 * @author shunai
 *
 */
@Entity
@Table(name="user")
public class User extends GenericModel {
	@Id
	@Column(name="id",nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long id;
	
	@Required(message="用户昵称不能为空")
	@MaxSize(value=32,message="用户呢称不能超过32位")
	@Column(name="name",nullable=false,length=32)
	public String name;
	
	@Required(message="账号不能为空")
	@MaxSize(value=32,message="账号不能超过32位")
	@Column(name="account",nullable=false,length=32)
	public String account;
	
	@Required(message="密码不能为空")
	@MaxSize(value=32,message="密码不能超过32位")
	@Column(name="password",nullable=false,length=32)
	public String password;
	
	public static User getByAccountAndPassword(String account, String password) {
		return User.find("account = ? and password = ?", account, password).first();
	}
	public static User getByAccount(String account){
		return User.find("account", account).first();
	}
	public User(){
		
	}
	public User(long id){
		
	}
	public List<Document> getDocList(){
		List<Document> documentList = new ArrayList<Document>();
		documentList = Document.find("userId = ?", id).fetch();	
		for(Document doc:documentList){
			doc.initTagList();
		}
		return documentList;
	}
	public static User getUser(long id){
		User user = new User();
		user = User.findById(id);
		return user;
	}
}
	
