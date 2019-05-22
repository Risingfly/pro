package com.genge.demo.controller;

import com.genge.demo.aspect.LogAspect;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@Controller
public class IndexController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);


    @RequestMapping("/setting")
    @ResponseBody
    public String setting(){
        return "setting : OK";
    }
    @RequestMapping(path = {"/genge","jb/{groupId}/{userId}/{type}"})
    @ResponseBody
    public String genge(@PathVariable("groupId") String Id,
                        @PathVariable("userId") int userId,
                        @RequestParam(value = "type",defaultValue = "999")String x){
        System.out.println("groupId="+Id+ "/// "+"userId" + userId+
        "x="+x);

        return String.format("{%s},{%d},{%s}",Id,userId,x);
    }
    @RequestMapping("/nb")
    public String nb(){
        return "red";
    }
    @RequestMapping("test")
    public String test(Model model){
//        com.genge.demo.User user = new com.genge.demo.User(110,"genge","6666");
//        model.addAttribute("user",user);
        return "test";
    }
    @RequestMapping("/request")
    @ResponseBody
    public String request(HttpServletRequest request, HttpServletResponse response,
                          HttpSession session){
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headerName = request.getHeaderNames();
        while (headerName.hasMoreElements()){
            String name = headerName.nextElement();
            sb.append(name+":"+request.getHeader(name) + "<br>");
        }
        for (Cookie c :
                request.getCookies()) {
            sb.append("Cookie:");
            sb.append(c.getName());
            sb.append(":");
            sb.append(c.getValue());
            sb.append("<br>");
        }
        sb.append("getMethod:"+request.getMethod()+"<br>");
        sb.append("getPathInfo:"+request.getPathInfo()+"<br>");
        sb.append("getQueryString:"+request.getQueryString()+"<br>");
        sb.append("getRequestURI:"+request.getRequestURI()+"<br>");
        return sb.toString();
    }
    @RequestMapping("/response")
    @ResponseBody
    public String response(@CookieValue(value = "six",defaultValue = "6") String six,
                           @RequestParam(value = "key",defaultValue = "key") String key,
                           @RequestParam(value = "value",defaultValue = "value") String value,
                           HttpServletResponse response){
        response.addCookie(new Cookie(key,value));
        response.addHeader(key,value);
        return "Genge NB From Cookie:"+six;
    }
    @RequestMapping("/redirect/{code}")
    public RedirectView redirect(@PathVariable("code") int code,
                           HttpSession session){
        RedirectView rev = new RedirectView("http://localhost:8080/test",true);
        if (code == 301){
            rev.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
        }
        rev.setContextRelative(true);
        rev.setHosts("/test");
        if (code == 302){
        }
        return rev;
    }

    @RequestMapping("/session/{val}")
    public String sessionInfo(@PathVariable("val")int code,HttpSession session){
        session.setAttribute("msg","Jump from redirect");
        return "redirect:/";
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value = "key",required = false)String key){
         if ("admin".equals(key)){
             return "hello admin";
         }
         throw new RuntimeException("password is Error!");
    }

    @ExceptionHandler
    @ResponseBody
    public String error(Exception e){
        return "error:"+e.getMessage();
    }

    @RequestMapping("/gtvg/subscribe/email=根哥")
    @ResponseBody
    public String test(@PathVariable("email")String email){
        System.out.println("email执行了");
        return "欢迎您！";
    }
}
