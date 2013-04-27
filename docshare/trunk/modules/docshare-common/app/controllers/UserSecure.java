package controllers;

import java.io.File;
import models.User;
import models.UserInfo;
import play.Play;
import play.data.validation.MaxSize;
import play.data.validation.Min;
import play.data.validation.Required;
import play.i18n.Messages;
import play.mvc.Before;
import play.mvc.Controller;

/**
 * 登录验证 
 * @author shunai
 */
public class UserSecure extends Controller {

	static final String SESSION_KEY = "userId";
	static final String NAME_SESSION_KEY = "userName";

	@Before(unless = { "signin", "authenticate", "signout","register","checkRegisterInfo" })
	static void checkAccess() {
		// Authent
		if (!session.contains(SESSION_KEY)) {
			String fromUrl = "GET".equals(request.method) ? request.url : Play.configuration.getProperty("http.path", "/");
			flash.put("url", fromUrl);
			signin();
		}
	}

	public static void signin() {
		render();
	}
	public static void checkRegisterInfo( String account,String name,String password,String repeatpassword){
		if(account==null||account.length()<5){
			flash.remove("success");
			flash.put("error","账号不能少于5位！");
			params.flash();
			render("UserSecure/register.html");
		}
		if(name==null){
			flash.remove("success");flash.remove("success");
			flash.put("error","昵称不能为空！");
			params.flash();
			render("UserSecure/register.html");
		}
		if(password==null||password.length()<6){
			flash.remove("success");
			flash.put("error","密码不能少于6位！");
			params.flash();
			render("UserSecure/register.html");
		}
		if(User.getByAccount(account)!=null){
			flash.remove("success");
			flash.put("error","账号已经存在！");
			params.flash();
			render("UserSecure/register.html");
		}
		if(!password.equals(repeatpassword)){
			flash.remove("success");
			flash.put("error", "两次密码输入不一样！");
			params.flash();
			render("UserSecure/register.html");
		}
		User user=new User();
		user.account=account;
		user.name=name;
		user.password=password;
		user.save();
		UserInfo userInfo=new UserInfo();
		userInfo.userId=user.id;
		userInfo.save();
		flash.remove("error");
		flash.put("success", "注册成功");
		session.put(SESSION_KEY, user.id);
		session.put(NAME_SESSION_KEY, user.name);
		// Redirect to the original URL (or WebRoot)
		try {
			redirectToOriginalURL();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void register(){
		flash.remove("success");
		flash.remove("error");
		render();
	}

	public static void signout() {
		session.clear();
		flash.success("您已经退出系统.");
		//String fromUrl = Play.configuration.getProperty("http.path", "/UserSecure/signin");
		String toUrl="http://local.scauhci.org:9403";
		redirect(toUrl);
	}

	public static void authenticate(@Required String account, @Required String password) throws Throwable {
		checkAuthenticity();
		// Check tokens
		Boolean allowed = false;
		User user = User.getByAccountAndPassword(account, password);
		if(null != user) {
			allowed = true;
		}
		if (validation.hasErrors() || !allowed) {
			flash.keep("url");
			flash.error("账号不存在或者密码不正确.");
			params.flash();
			signin();
		}
		session.put(SESSION_KEY, user.id);
		session.put(NAME_SESSION_KEY, user.name);
		// Redirect to the original URL (or WebRoot)
		redirectToOriginalURL();
	}

	static void redirectToOriginalURL() throws Throwable {
		String url = flash.get("url");
		if (url == null) {
			url = "GET".equals(request.method) ? request.url : Play.configuration.getProperty("http.path", "/");
		}
		redirect(url);
	}

	static boolean isConnected() {
		return session.contains(SESSION_KEY);
	}

	static String connected() {
		return session.get(SESSION_KEY);
	}
	
	static String getUserName() {
		return session.get(NAME_SESSION_KEY);
	}
	
	static long getUserId() {
		return Long.valueOf(session.get(SESSION_KEY));
	}
	static void setUserName(String userName){
		session.put(NAME_SESSION_KEY, userName);
	}
	
	static boolean checkIsAdmin(){
		boolean isAdmin = false;
		UserSecure.checkAccess();
		User user = User.findById(getUserId());
		String admin = Play.configuration.getProperty("admin","admin");
		String[] adminAccounts = admin.split(File.pathSeparator);
		for(String s : adminAccounts){
			if(s.equals(user.account))
				isAdmin = true;
		}
		return isAdmin;
	}
}