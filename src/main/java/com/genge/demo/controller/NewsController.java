package com.genge.demo.controller;

import com.genge.demo.model.*;
import com.genge.demo.service.CommentService;
import com.genge.demo.service.NewsService;
import com.genge.demo.service.UserService;
import com.genge.demo.util.TouTiaoUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    NewsService newsService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;
    /**
     * 评论详情首页
     * @param newsId
     * @param model
     * @return
     */
    @RequestMapping(path = "/news/{newsId}",method = RequestMethod.GET)
    public String newsDetail(@PathVariable("newsId") int newsId, Model model){
        News news = newsService.getById(newsId);
        if (news != null){
            List<Comment> comments = commentService.getCommentByEntity(news.getId(), EntityType.ENTITY_NEWS);
            List<ViewObject> commentVos = new ArrayList<>();
            for (Comment comment : comments) {
                ViewObject vo = new ViewObject();
                vo.set("comment",comment);
                vo.set("user",userService.getUser(comment.getUserId()));
                commentVos.add((vo));
            }
            model.addAttribute("comments",commentVos);
        }
        return "detail";
    }

    /**
     *
     * 用户添加评论
     * @param newsId
     * @param content
     * @return
     */
    @RequestMapping(path = {"/addComment/"},method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId")int newsId,
                             @RequestParam("content") String content){

        try {
//          敏感词过滤（待优化）
            System.out.println("代码进来了");
            System.out.println("hostHolder.getUser()="+hostHolder.getUser());

            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);
//          更新news里的评论数量
            int count = commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            System.out.println("count="+count);
            System.out.println("comment.getEntityId()"+comment.getEntityId());
            newsService.updateCommentCount(count,comment.getEntityId());
            System.out.println("请求插入新闻成功！");
//          怎么异步化
        }catch (Exception e){
            LOGGER.error("添加评论失败"+e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }
    /**
     * 上传图片
     * @param file
     * @return
     */
    @RequestMapping(path = {"/uploadImage/"},method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file")MultipartFile file){
        try {
            String fileUrl = newsService.saveImage(file);
            if (fileUrl == null){
                return TouTiaoUtil.getJSONString(1,"上传图片失败");
            }
            return TouTiaoUtil.getJSONString(0,fileUrl);
        }catch (Exception e){
            LOGGER.error("上传图片失败"+e.getMessage());
            return TouTiaoUtil.getJSONString(1,"上传失败");
        }

    }

    /**
     * 用户下载图片
     * @param imageName
     * @param response
     */
    @RequestMapping(value = "/image",method = RequestMethod.GET)
    @ResponseBody
    public void getImage(@RequestParam("name")String imageName,
                         HttpServletResponse response){
        try {
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new File(TouTiaoUtil.IMAGE_DIR + imageName)),
                    response.getOutputStream());
        }catch (Exception e){
            LOGGER.error("读取图片错误"+e.getMessage());
        }
    }

    /**
     * 用户发布消息
     * @param image
     * @param title
     * @param link
     * @return
     */
    @RequestMapping(path = {"/addNews/"},method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image")String image,
                          @RequestParam("title")String title,
                          @RequestParam("link")String link){
        try {
            News news = new News();
            if (hostHolder.getUser() != null){
                news.setUserId(hostHolder.getUser().getId());
            }else {
//                匿名
                news.setUserId(3);
            }
            news.setImage(image);
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setLink(link);
            newsService.addNews(news);
            return TouTiaoUtil.getJSONString(0,"成功");
        }catch (Exception e){
            LOGGER.error("用户咨询上传失败"+e.getMessage());
            return TouTiaoUtil.getJSONString(1,"发布失败");
        }
    }
}
