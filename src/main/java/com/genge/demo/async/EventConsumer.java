package com.genge.demo.async;

import com.alibaba.fastjson.JSON;
import com.genge.demo.aspect.LogAspect;
import com.genge.demo.util.JedisAdapter;
import com.genge.demo.util.RedisKeyUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 *取出事件队列中的事件，并调用相应的handler进行处理
 * @author Genge
 */
@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware{

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);
    private ApplicationContext applicationContext;
    private Map<EventType,List<EventHandler>> config = new HashMap<>();

    @Autowired
    JedisAdapter jedisAdapter;
    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null){
            for (Map.Entry<String,EventHandler> entry: beans.entrySet()) {
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                for (EventType type: eventTypes) {
                    if (!config.containsKey(type)){
                        config.put(type,new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true){
//                    String key = RedisKeyUtil.getEventQueueKey();
//                    List<String> events = jedisAdapter.brpop(0,key);
//                    for (String message: events) {
//                        if (message.equals(key)){
//                            continue;
//                        }
//                        EventModel eventModel = JSON.parseObject(message,EventModel.class);
//                        if (!config.containsKey(eventModel.getEventType())){
//                            LOGGER.error("不能识别的事件");
//                            continue;
//                        }
//                        for (EventHandler handler : config.get(eventModel.getEventType())) {
//                            handler.doHandler(eventModel);
//                        }
//                    }
//                }
//            }
//        });
        ExecutorService poolExecutor = Executors.newSingleThreadExecutor();
        ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 10, 10, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(10));
        pool.execute(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> events = jedisAdapter.brpop(0,key);
                    for (String message: events) {
                        if (message.equals(key)){
                            continue;
                        }
                        EventModel eventModel = JSON.parseObject(message,EventModel.class);
                        if (!config.containsKey(eventModel.getEventType())){
                            LOGGER.error("不能识别的事件");
                            continue;
                        }
                        for (EventHandler handler : config.get(eventModel.getEventType())) {
                            handler.doHandler(eventModel);
                        }
                    }
                }
            }
        });
//        poolExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                while (true){
//                    String key = RedisKeyUtil.getEventQueueKey();
//                    List<String> events = jedisAdapter.brpop(0,key);
//                    for (String message: events) {
//                        if (message.equals(key)){
//                            continue;
//                        }
//                        EventModel eventModel = JSON.parseObject(message,EventModel.class);
//                        if (!config.containsKey(eventModel.getEventType())){
//                            LOGGER.error("不能识别的事件");
//                            continue;
//                        }
//                        for (EventHandler handler : config.get(eventModel.getEventType())) {
//                            handler.doHandler(eventModel);
//                        }
//                    }
//                }
//            }
//        });
//        thread.setName("我是处理异步的线程^^");
//        thread.start();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
