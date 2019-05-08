package com.genge.demo.service;

import com.genge.demo.dao.NewsDAO;
import com.genge.demo.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatestNews(int uerId,int offset,int limit){
        return newsDAO.selectByUserIdAndOffset(uerId,offset,limit);
    }
}
