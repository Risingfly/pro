package com.genge.demo.service;

import com.genge.demo.controller.LoginController;
import com.genge.demo.dao.LoginTicketDAO;
import com.genge.demo.dao.UserDAO;
import com.genge.demo.model.HostHolder;
import com.genge.demo.model.LoginTicket;
import com.genge.demo.model.User;
import com.genge.demo.util.TouTiaoUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class UserService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;


    @Autowired
    private HostHolder hostHolder;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    /***
     *
     * 用户注册
     * @param username
     * @param password
     * @return
     */
    public Map<String,Object> register(String username, String password){
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isEmpty(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(password)){
            map.put("msgp","用户密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if (user != null){
            map.put("msgname","用户名已被注册");
            return map;
        }
//      密码强度可扩展
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://www.genge.nb.com/head/%dt.png",new Random().nextInt(1000)));
        user.setPassword(TouTiaoUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

//        注册完默认直接登陆
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }


    /**
     *
     * 用户登陆
     * @param username
     * @param password
     * @return
     */
    public Map<String,Object> login(String username, String password){
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isEmpty(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(password)){
            map.put("msgp","用户密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);

        if (user == null){
            map.put("msgname","用户名不存在");
            return map;
        }

        if (!TouTiaoUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msgname","密码不正确");
            return map;
        }
//        测试，记住user
        hostHolder.setUser(user);
        return map;
    }

    /**
     * 登出
     * @param ticket
     */
    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }

    /**
     *
     * 添加ticket作为登陆认证并与用户id绑定
     * @param userId
     * @return
     */
    private String addLoginTicket(int userId){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000 * 3600 * 24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }
}
