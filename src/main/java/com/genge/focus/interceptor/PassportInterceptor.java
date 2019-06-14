package com.genge.focus.interceptor;

import com.genge.focus.dao.LoginTicketDAO;
import com.genge.focus.dao.UserDAO;
import com.genge.focus.model.HostHolder;
import com.genge.focus.model.LoginTicket;
import com.genge.focus.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 此处不能加入@Component,
 * 不能真正注入bean，需要到拦截器注册中心进行注入
 * @author Genge
 */
public class PassportInterceptor implements HandlerInterceptor {
   @Autowired
   private LoginTicketDAO loginTicketDAO;

   @Autowired
   private UserDAO userDAO;

   @Autowired
   private HostHolder hostHolder;

    /**
     *拦截请求到达之前
     * true继续请求，false拒绝请求
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("客户端请求Request到达之前，我开始工作了，我是---preHandle");
        String ticket = null;
//        浏览器cookie中查找是否存在对应的cookie
        if (request.getCookies() != null){
            System.out.println("preHandler:浏览器中存在cookie");
            for (Cookie cookie :
                 request.getCookies()) {
//                万恶的tickets是,是ticket==BUG警示
                if ("ticket".equals(cookie.getName())){
                    ticket = cookie.getValue();
                    System.out.println("preHandler:ticket="+ticket);
                    break;
                }
            }
        }
//        找到ticket后继续判断其存在时间是否过期，或者状态不等于0，也表示该次登陆作废
        if (ticket != null){
            System.out.println("preHandler:验证cookie对应的ticket是否有效");
            System.out.println("loginTicket ticket="+ticket);
            System.out.println("loginTicketDAO="+loginTicketDAO);
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0){
                return false;
            }
            System.out.println("preHandler:ticket有效，根据ticket查询用户user，并记录到hostHolder（ThreadLocal）中" +
                    "供后续的操作使用user");
            User user = userDAO.selectById(loginTicket.getUserId());
            System.out.println("拦截器工作了");
            System.out.println("用户名="+user.getName());
            hostHolder.setUser(user);
        }

        return true;
    }

    /**
     * request被响应之后，可以添加模型数据，自动传给前端
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        if (modelAndView != null && hostHolder.getUser() != null){
//            modelAndView.addObject("user",hostHolder.getUser());
//        }
    }

    /**
     * 视图渲染之前以及request全部结束之后
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        hostHolder.clear();
    }
}
