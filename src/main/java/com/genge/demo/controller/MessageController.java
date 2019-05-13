package com.genge.demo.controller;

import com.genge.demo.model.Message;
import com.genge.demo.model.User;
import com.genge.demo.model.ViewObject;
import com.genge.demo.service.MessageService;
import com.genge.demo.service.UserService;
import com.genge.demo.util.TouTiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    /**
     * 发表消息
     * @param fromId
     * @param toId
     * @param content
     * @return
     */
    @RequestMapping(path = "/msg/addMessage",method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(@RequestParam("fromId")int fromId,
                             @RequestParam("toId")int toId,
                             @RequestParam("content") String content){

        try{
            Message msg = new Message();
            msg.setContent(content);
            msg.setFromId(fromId);
            msg.setToId(toId);
            msg.setCreateDate(new Date());
            msg.setConversationId(fromId < toId ? String.format("%d_%d",fromId,toId) : String.format("%d_%d",toId,fromId));
            messageService.addMesssage(msg);
            return TouTiaoUtil.getJSONString(0,"成功");
        }catch (Exception e){
            LOGGER.error("添加消息失败",e.getMessage());
            return TouTiaoUtil.getJSONString(1,"插入失败");
        }
    }

    /**
     * 显示一个用户和另一个用户之间的所有消息
     * 通过conversationId进行查找
     * @param model
     * @param conversationId
     * @return
     */
    @RequestMapping(path = "/msg/detail")
    public String conversationDetail(Model model, @Param("conversationId")String conversationId){
        try {
            List<Message> conversationList = messageService.getConversationDetail(conversationId,0,1);
            List<ViewObject> messages = new ArrayList<>();
            for (Message msg: conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("message",msg);
                User user = userService.getUser(msg.getFromId());
                if (user == null){
                    continue;
                }
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userId",user.getId());
                messages.add(vo);
            }
            model.addAttribute("messages",messages);
        }catch (Exception e){
            LOGGER.error("获取详情页失败"+e.getMessage());
        }
        return "letterDetail";
    }
}
