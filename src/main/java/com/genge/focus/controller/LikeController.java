package com.genge.focus.controller;

import com.genge.focus.async.EventModel;
import com.genge.focus.async.EventProducer;
import com.genge.focus.async.EventType;
import com.genge.focus.model.EntityType;
import com.genge.focus.model.HostHolder;
import com.genge.focus.service.LikeService;
import com.genge.focus.service.NewsService;
import com.genge.focus.util.JedisAdapter;
import com.genge.focus.util.TouTiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    NewsService newsService;

    @Autowired
    JedisAdapter jedisAdapter;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/like"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String like(@RequestParam("newsId")int newsId){
//        hostHolder.getUser().getId()
        int userId = 65;
        long likeCount = likeService.like(userId, EntityType.ENTITY_NEWS,newsId);
        newsService.updateCommentCount(newsId,(int)likeCount);

        eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(userId).
                setEntityId(newsId).setEntityType(EntityType.ENTITY_NEWS).
                setEntityOwnerId(newsId));
        return TouTiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String dislike(@RequestParam("newsId")int newsId){
        int userId = hostHolder.getUser().getId();
        long likeCount = likeService.disLike(userId, EntityType.ENTITY_NEWS,newsId);
        newsService.updateCommentCount(newsId,(int)likeCount);
        return TouTiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }
}
