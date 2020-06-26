package cn.smbms.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import cn.smbms.pojo.User;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.ConstUtils;

@Controller
public class LoginController {
private Logger logger = Logger.getLogger(LoginController.class);
	
	@Resource
	private UserService userService;
	
	@RequestMapping(value="/login.html")
	public String login(){
		logger.debug("LoginController welcome SMBMS==================");
		return "login";
	}
	
	@RequestMapping(value="/dologin.html",method=RequestMethod.POST)
	public String doLogin(@RequestParam String userCode,@RequestParam String userPassword,HttpServletRequest request,HttpSession session)
			throws Exception{
		logger.debug("doLogin====================================");
		//调用service方法，进行用户匹配
		User user = userService.login(userCode,userPassword);
		//登录成功
		if(null != user){
			//放入session
			session.setAttribute(ConstUtils.USER_SESSION, user);
			//页面跳转（adminframe.jsp）
			if (user.getUserRole() == 1) {
				return "redirect:/sys/adminmain.html";
			} else if (user.getUserRole() == 2) {
				return "redirect:/sys/managermain.html";
			} else if(user.getUserRole() == 3) {
				return "redirect:/sys/usermain.html";
			}
		}else{
			//页面跳转（login.jsp）带出提示信息--转发
			request.setAttribute("error", "用户名或密码不正确");
			return "login";
		}
		request.setAttribute("error","无角色匹配");
		return "login";
	}
	
	@RequestMapping(value="/user/logout.html")
	public String logout(HttpSession session){
		//清除session
		session.removeAttribute(ConstUtils.USER_SESSION);
		return "login";
	}

	/**
	 * 管理员页面跳转
	 * @return
	 */
	@RequestMapping(value="/sys/adminmain.html")
	public String adminmain(){
		return "adminframe";
	}

	/**
	 * 管理员页面跳转
	 * @return
	 */
	@RequestMapping(value="/sys/managermain.html")
	public String managermain(){
		return "managerframe";
	}

	/**
	 * 管理员页面跳转
	 * @return
	 */
	@RequestMapping(value="/sys/usermain.html")
	public String usermain(){
		return "userframe";
	}
}
