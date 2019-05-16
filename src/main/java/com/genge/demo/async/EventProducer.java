package com.genge.demo.async;

import com.alibaba.fastjson.JSONObject;
import com.genge.demo.util.JedisAdapter;
import com.genge.demo.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 将事件发送出去
 * 将事件序列化后加入到redis事件队列中
 * @author Genge
 */
@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel model){
        try {
            String json = JSONObject.toJSONString(model);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key,json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
