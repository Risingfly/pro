package com.genge.focus.async.handler;

import com.genge.focus.async.EventHandler;
import com.genge.focus.async.EventModel;
import com.genge.focus.async.EventType;
import com.genge.focus.model.HostHolder;
import com.genge.focus.model.Message;
import com.genge.focus.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
@Component
public class LoginExceptionHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    HostHolder hostHolder;

    @Override
    public void doHandler(EventModel model) {
//    判断是否有异常登陆
        System.out.println(Thread.currentThread().getName());
        System.out.println("兄弟们异常登陆啦啦啦啦啦啦啦啦啦啦");
        Message message = new Message();
        message.setToId(model.getActorId());
        message.setContent("你上次的登陆异常");
        message.setFromId(3);
        message.setCreateDate(new Date());
        message.setConversationId("异常登陆");
        message.setHasRead(0);
        messageService.addMessage(message);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
