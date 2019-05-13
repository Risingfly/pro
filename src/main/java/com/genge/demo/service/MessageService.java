package com.genge.demo.service;

import com.genge.demo.dao.MessageDAO;
import com.genge.demo.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageDAO messageDAO;

    public int addMesssage(Message message){
        return messageDAO.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId,int offset,int limit){
        return messageDAO.getConversationDetail(conversationId,offset,limit);
    }
}
