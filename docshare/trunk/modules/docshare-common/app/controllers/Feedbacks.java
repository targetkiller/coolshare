package controllers;

import models.FeedBack;
import models.User;
import play.Play;
import play.data.validation.Email;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.i18n.Messages;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
@With(UserSecure.class)
public class Feedbacks extends Controller {
		public static void feedback(){
			flash.remove("success");
			flash.remove("error");
			render();
		}
		public static void addFeedback(@Required()@MaxSize(value=100)String title,@Required()@MaxSize(value=32)String username,@Required()String content,@Required()@Email()@MaxSize(value=32)String email,int serious){

			flash.put("title", title);
			flash.put("username", username);
			flash.put("content", content);
			flash.put("email", email);
			if(validation.hasErrors()){
				flash.remove("success");
				flash.put("error", "数据填写有误，提交失败！");
				render("Feedbacks/feedback.html");
			}
			FeedBack feedBack=new FeedBack();
			feedBack.content=content;
			feedBack.email=email;
			feedBack.name=username;
			feedBack.serious=serious;
			feedBack.userId=UserSecure.getUserId();
			feedBack.title=title;
			feedBack.save();
			flash.remove("error");
		    flash.put("success", "保存成功，我们将尽快回复！");
		    render("Feedbacks/feedback.html");
		}
}
