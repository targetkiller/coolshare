package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.swing.JOptionPane;

import org.scauhci.enumvalue.DefauleValue;
import org.scauhci.enumvalue.DocumentType;
import org.scauhci.util.FileUtil;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;


@Entity
@Table(name="document")
public class Document extends GenericModel{
	@Id
	@Column(name="id",nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long id;
    /**
     * 文档名字
     */
	@Column(name="name",nullable=false,length=100)
	public String name;
	
	/**
	 * 文档存放位置，采用UUID方式标记这个文件,文件名采用(location.sufix),如xxxaaa.pdf
	 */
	@Column(name="location",nullable=false,length=255)
	public String location;
	
	/**
	 * 文档类型
	 */
	@Column(name="status",nullable=false)
	public int status;
	
	/**
	 * 文档标题
	 */
	@Column(name="title",nullable=false,length=100)
	public String title;
	
	/**
	 * 文档摘要
	 */
	@Column(name="summary",nullable=false)
	public String summary;
	
	/**
	 * 文档下载次数
	 */
	@Column(name="downloadNum",nullable=false)
	public int downloadNum;
	
	@Column(name="cate_id",nullable=false)
	public long cateId;
	
	@Column(name="user_id",nullable=false)
	public long userId;
	
	@Column(name="time",nullable=false)
	public long time;
	
	@Column(name = "sufix")
	public String sufix;
	
	@Transient
	public String authorName;
	@Transient
	public String averageScore;
	@Transient
	public int collectionNum;
	@Transient
	public String cateName;
	@Transient
	public String iconLocation;
	@Transient
	public List tagList;

	@Transient
	public List<String> tagNames;

	public long getScoreTimes(){
		return Score.count("docId = ?",id);
	}
	public static long getUserId(long docId){
		Document doc = Document.findById(docId);
		return doc.userId;
	}
	public long getCollectionNum(){
		long collectNum = Collection.count("doc_id",id);
		return collectNum;
	}

	public List<String> initTagNames(){
		List<Tag_rel_doc> trds = Tag_rel_doc.find("byDoc_id", this.id).fetch();
		List<String> list = new ArrayList();
		for(Tag_rel_doc trd : trds){
			Tag tag = Tag.find("id", trd.tagId).first();
			list.add(tag.name);
		}
		return list;
	}
	public void initAuthorName(){
		User user = User.findById(this.userId);
		this.authorName = user.name;
	}
	public long getCateId(){
		Cate cate = Cate.findById(this.cateId);
		return cate==null?DefauleValue.cateDefauleId:cate.id;
	}
	public String getAuthorName(){
		User user = User.findById(this.userId);
		return user.name;
	}
	public static List<Document> getHotDownload(){
		List<Document> documentList = Document.find(
				"status = ? order by downloadNum",DocumentType.common.value).fetch(20);
		for (Document doc : documentList) {
			String sufix=FileUtil.getFileExtension(doc.name);
			doc.sufix=sufix;
			doc.iconLocation=FileUtil.getFileTypeIconURL(sufix);
			User u = User.findById(doc.userId);
			if (u != null) {
				doc.authorName = u.name;
			}
		}
		return documentList;
	}
	public static List<Document> getHotCollectionDocs() {
//		return Document.find("select d, count(c.id) num from Document d, Collection c " +
//				"where d.id = c.docId group by d.id order by num").fetch(20);
		String sql = "select doc_id, count(id) as num from collection group by doc_id order by num desc";
		List<Object[]> list = Model.em().createNativeQuery(sql).getResultList();
		List<Document> documentList = new ArrayList<Document>();
		for (int i = 0; i < 20 && i < list.size(); i++) {
			Document doc = Document.findById(Long.parseLong(list.get(i)[0].toString()));
			if(doc==null){
				i--;
				continue;
			}
			doc.collectionNum = Integer.parseInt(list.get(i)[1].toString());
			String sufix=FileUtil.getFileExtension(doc.name);
			doc.sufix=sufix;
			doc.iconLocation=FileUtil.getFileTypeIconURL(sufix);
			User u = User.findById(doc.userId);
			if (u != null) {
				doc.authorName = u.name;
			}
			documentList.add(doc);
		}
		return documentList;
	}
	
	public static List<Document> getHotCollections() {
		return Document.find("select d from Document d, Collection c " +
							 "where d.id = c.docId group by d.id order by count(c.id) desc").fetch(20);
	}
	
	public String getSufix() {
		return sufix = FileUtil.getFileExtension(name);
	}
	
	public String getIconLocation() {
		String sufix = getSufix();
		return FileUtil.getFileTypeIconURL(sufix);
	}
	
	public void  initTagList(){
		this.tagList=Tag.getTagListByDocId(this.id);
	}
}













