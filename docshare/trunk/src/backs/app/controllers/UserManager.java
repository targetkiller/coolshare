package controllers;

import org.scauhci.enumvalue.DefauleValue;

import models.User;
import models.UserInfo;
import play.data.validation.Email;
import play.data.validation.Max;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.Scope;
import play.mvc.With;

@With(UserSecure.class)
public class UserManager extends Controller {

	public static void main_common() {
		User user = User.findById(UserSecure.getUserId());
		render(user);
	}

	public static void main_contact() {
		UserInfo userInfo = UserInfo.find("user_id", UserSecure.getUserId())
				.first();
		if (userInfo == null) {
			userInfo = new UserInfo();
			userInfo.userId = UserSecure.getUserId();
			userInfo.save();
		}
		render(userInfo);
	}

	public static void main_password() {
		render();
	}

	/**
	 * 个人信息修改
	 * 
	 * @param id
	 * @param name
	 */
	public static void update(@Required() @MaxSize(value = 32) String name) {
		User user = User.findById(UserSecure.getUserId());
		int status = 0;
		String message = null;
		if (user == null || validation.hasErrors()) {
			message = "修改失败！";
			status = DefauleValue.failStatus;
		} else {
			user.name = name;
			user.save();
			UserSecure.setUserName(user.name);
			status = DefauleValue.successStatus;
			message = "修改成功！";
		}
		render("UserManager/main_common.html", status, message, user);
	}

	/**
	 * 
	 * @param birthday
	 * @param sex
	 * @param email
	 * @param longTel
	 * @param shortTel
	 * @param qq
	 * @param msn
	 * @param address
	 */
	public static void updateContactInfo(@MaxSize(value = 32) String birthday,
			int sex, @MaxSize(value = 64) @Email() String email,
			@MaxSize(value = 32) String longTel,
			@MaxSize(value = 32) String shortTel,
			@MaxSize(value = 32) String qq, @MaxSize(value = 32) String msn,
			@MaxSize(value = 100) String address) {
		UserInfo userInfo = UserInfo.find("user_id", UserSecure.getUserId())
				.first();
		int status = 0;
		String message = null;
		if (userInfo == null || validation.hasErrors()) {
			message = "修改失败！";
			status = DefauleValue.failStatus;
			render("UserManager/main_contact.html", status, message, userInfo);
		} else {
			userInfo.sexual = sex;
			userInfo.email = email;
			userInfo.longTel = longTel;
			userInfo.shortTel = shortTel;
			userInfo.qq = qq;
			userInfo.msn = msn;
			userInfo.address = address;
			userInfo.birthday = birthday;
			userInfo.save();
			message = "修改成功！";
			status = DefauleValue.successStatus;
		}
		render("UserManager/main_contact.html", status, message, userInfo);
	}
	/**
	 * 修改密码
	 * 
	 * @param password
	 */
	public static void updatePassword(@Required() String oldpassword,
			@Required() @MaxSize(value = 32) String newpassword,
			@Required() @MaxSize(value = 32) String confirmpassword) {
		User user = User.findById(UserSecure.getUserId());
		int status = 0;
		String message = null;
		if (user == null || validation.hasErrors()
				|| !newpassword.equals(confirmpassword)
				|| !user.password.equals(oldpassword)) {
			status = DefauleValue.failStatus;
			message = "密码修改失败!";
			if (!user.password.equals(oldpassword)) {
				message = "旧密码输入错误,修改失败!";
			}

		} else {
			user.password = newpassword;
			user.save();
			status = DefauleValue.successStatus;
			message = "密码修改成功!";
		}
		render("Usermanager/main_password.html", status, message);
	}
}