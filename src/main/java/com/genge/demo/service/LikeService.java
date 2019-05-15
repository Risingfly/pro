package com.genge.demo.service;

import com.genge.demo.util.JedisAdapter;
import com.genge.demo.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Redis实现点赞踩赞功能
 * 思路：对喜欢和不喜欢都维持一个对应的set集合，点赞将该用户加入到喜欢集合中，并从
 * 不喜欢集合中删除；踩赞反之亦然。
 * 集合的key是根据like/dislike再加上对应的实体和id实现（因为有多个地方可能出现点赞功能）
 * @author Genge
 */
@Service
public class LikeService {

    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 判断（当前）用户的喜欢状态，喜欢返回1，不喜欢返回-1，其他返回0
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public int getLikeStatus(int userId,int entityType,int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        if (jedisAdapter.sismember(likeKey,String.valueOf(userId))){
            return 1;
        }
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId,entityType);
        return jedisAdapter.sismember(disLikeKey,String.valueOf(userId)) ? -1 : 0;
    }

    /**
     * 当前用户点赞喜欢
     * 先生成喜欢的key,在将其与当前用户绑定并插入到喜欢集合中，同时将其从不喜欢中删除
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public long like(int userId,int entityType,int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));

        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId,entityType);
        jedisAdapter.srem(disLikeKey,String.valueOf(userId));
//      查询有多少喜欢的（因为有多个线程在操作）
        return jedisAdapter.scard(likeKey);
    }

    /**
     * 当前用户点赞不喜欢
     * 生成不喜欢key，再将其与当前用户id绑定并插入不喜欢集合，同时删除喜欢中该用户，
     * 最后显示当前有多少喜欢
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public long disLike(int userId,int entityType,int entityId){
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId,entityType);
        jedisAdapter.sadd(disLikeKey,String.valueOf(userId));

        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        jedisAdapter.srem(likeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }
}
