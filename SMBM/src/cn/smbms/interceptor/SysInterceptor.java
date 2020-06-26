package cn.smbms.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.smbms.pojo.User;
import cn.smbms.tools.ConstUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SysInterceptor extends HandlerInterceptorAdapter {
	private Logger logger = Logger.getLogger(SysInterceptor.class);
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
		logger.debug("SysInterceptor preHandle ==========================");
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute(ConstUtils.USER_SESSION);
		if(null == user){
			response.sendRedirect(request.getContextPath()+"/401.jsp");
			return false;
		}
		return true;
	}
}
