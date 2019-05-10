package com.genge.demo.controller;

import com.genge.demo.model.HostHolder;
import com.genge.demo.model.News;
import com.genge.demo.model.ViewObject;
import com.genge.demo.service.NewsService;
import com.genge.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private NewsService newsService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(value = {"/user/{userId}"},method = {RequestMethod.GET,RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId,
                            @RequestParam(value = "pop",defaultValue = "0")int pop){
        model.addAttribute("pop",pop);
        return "home";
    }
    @RequestMapping(path = {"/","/index"},method = {RequestMethod.GET,RequestMethod.POST})
    public String index(Model model){
        List<News> newsList = newsService.getLatestNews(24,24,3);
        List<ViewObject> vos = new ArrayList<>();
        for (News news:
             newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news",news);
            vo.set("user",userService.getUser(news.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("vos",vos);
        model.addAttribute("today",new Date());
        return "home";
    }
}
