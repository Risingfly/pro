package com.genge.demo;

import com.genge.demo.dao.*;
import com.genge.demo.model.*;
import com.genge.demo.util.JedisAdapter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
	@Autowired
	UserDAO userDAO;
	@Autowired
	NewsDAO newsDAO;
	@Autowired
	LoginTicketDAO loginTicketDAO;

	@Autowired
	CommentDAO commentDAO;

	@Autowired
	MessageDAO messageDAO;

	@Autowired
	JedisAdapter jedisAdapter;
	@Test
	public void testObject(){
		User user = new User();
		user.setHeadUrl("http://image.genge.nb.com");
		user.setName("user1");
		user.setPassword("pwd");
		user.setSalt("salt");
		jedisAdapter.setObject("user1xx",user);
		User user1 = jedisAdapter.getObject("user1xx",User.class);
		System.out.println("相等="+(user == user1));
		System.out.println(user1);
	}
	@Test
	public void contextLoads1(){
		for (int i = 0; i < 3; i++) {
			Message msg = new Message();
			msg.setContent("66"+i);
			msg.setFromId(i+1);
			msg.setToId(i + 2);
			msg.setCreateDate(new Date());
			msg.setConversationId("begin"+i);
			messageDAO.addMessage(msg);
		}
	}

	@Test
	public void contextLoads(){
		for (int i = 0; i < 3; i++) {
			Comment comment = new Comment();
			comment.setUserId(i + 1);
			comment.setEntityId(++i);
			comment.setEntityType(EntityType.ENTITY_NEWS);
			comment.setStatus(0);
			comment.setCreatedDate(new Date());
			comment.setContent("comment"+String.valueOf(i));
			commentDAO.addComment(comment);
		}
		Assert.assertNotNull(commentDAO.selectByEntity(1,EntityType.ENTITY_NEWS).get(0));
	}
//	@Test
//	public void contextLoads1(){
//		LoginTicket ticket = new LoginTicket(0,1,new Date(),0,"ticket-2");
//
//		loginTicketDAO.addTicket(ticket);
//		loginTicketDAO.updateStatus("ticket-1",1);
//		loginTicketDAO.selectByTicket("ticket-1");
//	}
//	@Test
//	public void contextLoads() {
//		Random random = new Random();
////		userDAO.deleteById("");
//		for (int i = 23; i < 28; i++) {
//			User user = new User();
//			user.setId(2*i);
//			user.setHeadUrl(String.format("http://www.genge.nb.com/head/%dt.png",random.nextInt(1000)));
//			user.setName(String.format("USER_%d",4 * i));
//			user.setPassword("");
//			user.setSalt("");
//			userDAO.addUser(user);
//
//			News news = new News();
//			news.setCommentCount(i);
//			Date date = new Date();
//			date.setTime(date.getTime()+1000*3600*5*i);
//			news.setCreatedDate(date);
//			news.setImage(String.format("http://www.genge.nb.com/head/%dm.png",random.nextInt(1000)));
//			news.setLikeCount(i + 1);
//			news.setUserId(i + 1);
//			news.setTitle(String.format("TITLE{%d}",i));
//			news.setLink(String.format("http://www.genge.nb.com/%d.html",i));
//
//			newsDAO.addNews(news);
//		}
////		Assert.assertEquals("newpassword",userDAO.selectById(1).getPassword());
//
//	}

}
