package models;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import controllers.UserSecure;

import play.db.jpa.GenericModel;

/**
 * 评分
 * @author shunai
 *
 */
@Entity
@Table(name="score")
public class Score extends GenericModel{
	@Id
	@Column(name="id",nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long id;
	
	@Column(name="user_id",nullable=false)
	public long userId;
	
	@Column(name="doc_id",nullable=false)
	public long docId;
	
	@Column(name="score",nullable=false)
	public int score;
	
	public static int getAvgScore(long docId){
		List<Score> allScore = Score.find("docId = ?", docId).fetch();
		int sum = 0;
		int avgScore = 0;
		if(allScore.size()!=0)
		{
		for(Score sc : allScore){
			sum += sc.score;
		}
		avgScore=sum/allScore.size();
		}
		return avgScore;
	}

}
