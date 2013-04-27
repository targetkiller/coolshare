package controllers;

import java.util.List;

import models.User;
import models.Tag;
import models.UserInfo;
import play.mvc.Controller;

public class UserZone extends Controller {
	
	public static void Userinfo(long userId) {
		List<Tag> tagList=Tag.getTagListByUserId(userId);
		User user = User.findById(userId);
		UserInfo userInfo = null;
		if (user != null) {
			userInfo = UserInfo.find("userId=?", userId).first();
		}
		renderArgs.put("tagList",tagList);
		renderArgs.put("user", user);
		renderArgs.put("userInfo", userInfo);
		render();
	}
}
