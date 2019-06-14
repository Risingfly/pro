package com.genge.focus.service;

import com.genge.focus.dao.MessageDAO;
import com.genge.focus.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageDAO messageDAO;

    public int getMessage(Message message){
        return messageDAO.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId,int offset,int limit){
        return messageDAO.getConversationDetail(conversationId,offset,limit);
    }

    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageDAO.getConversationList(userId,offset,limit);
    }

    public int getConversationUnReadCount(int userId,String conversationId){
        return messageDAO.getConversationUnReadCount(userId,conversationId);
    }
    public int addMessage(Message message) {
        return messageDAO.addMessage(message);
    }
}
