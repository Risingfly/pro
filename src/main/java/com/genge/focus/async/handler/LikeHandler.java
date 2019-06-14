package com.genge.focus.async.handler;

import com.genge.focus.async.EventHandler;
import com.genge.focus.async.EventModel;
import com.genge.focus.async.EventType;
import com.genge.focus.model.Message;
import com.genge.focus.model.User;
import com.genge.focus.service.MessageService;
import com.genge.focus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
@Component
public class LikeHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandler(EventModel model) {
        System.out.println(Thread.currentThread().getName());
        Message message = new Message();
        message.setFromId(3);
        message.setToId(model.getActorId());
        User user = userService.getUser(model.getActorId());
        message.setContent("用户"+user.getName()+"赞了你的资讯，http://127.0.0.1:8080/news"
        +model.getEntityId());
        message.setCreateDate(new Date());
        message.setConversationId("点赞通知大兄弟！有人喜欢你^^");
        messageService.addMessage(message);
        System.out.println("Liked");
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
