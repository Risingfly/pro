package com.genge.demo.util;


import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

public class JedisAdapter {

    public static void print(int index,Object obj){
        System.out.println(String.format("%d,%s",index,obj.toString()));
    }

    public static void main(String[] args) {

        Jedis jedis = new Jedis("192.168.10.136",6379);
//      删除数据
        jedis.flushAll();

//      一般字符
        jedis.set("根哥","牛逼");
        jedis.rename("根哥","genge");
        print(1,jedis.get("genge"));
        jedis.setex("genge",10,"NB");
        jedis.set("pv","100");
        jedis.incr("pv");
        print(2,jedis.get("pv"));
        jedis.incrBy("pv",50);
        print(3,jedis.get("pv"));

//      列表操作
        String listName = "list";
        for (int i = 0; i < 10; i++) {
            jedis.lpush(listName,String.valueOf(i));
        }
        print(4,jedis.lrange(listName,0,11));
        print(5,jedis.lpop(listName));
        print(6,jedis.llen(listName));
        print(7,jedis.lindex(listName,3));
        print(8,jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER,"4","xx"));
        print(9,jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE,"4","xx"));
        print(10,jedis.lrange(listName,0,12));

//      哈希
        String userKey = "userxx";
        jedis.hset(userKey,"name","genge");
        jedis.hset(userKey,"age","100");
        jedis.hset(userKey,"phone","6666666666");

        print(12,jedis.hget(userKey,"name"));
        print(12,jedis.hget(userKey,"phone"));
        print(12,jedis.hgetAll(userKey));
        jedis.hdel(userKey,"phone");
        print(13,jedis.hgetAll(userKey));
        print(14,jedis.hkeys(userKey));
        print(15,jedis.hvals(userKey));
        print(16,jedis.hexists(userKey,"email"));
        print(16,jedis.hexists(userKey,"age"));
        jedis.hsetnx(userKey,"school","southeast");
        jedis.hsetnx(userKey,"name","dzt");
        print(17,jedis.hgetAll(userKey));

//      set集合
        String likeKeys1 = "newsLike1";
        String likeKeys2 = "newsLike2";
        for (int i = 0; i < 10; i++) {
            jedis.sadd(likeKeys1,String.valueOf(i));
            jedis.sadd(likeKeys2,String.valueOf(i * 2));
        }
        print(18,jedis.smembers(likeKeys1));
        print(18,jedis.smembers(likeKeys2));
        print(19,jedis.sinter(likeKeys1,likeKeys2));
        print(20,jedis.sunion(likeKeys1,likeKeys2));
        print(21,jedis.sdiff(likeKeys1,likeKeys2));
        print(21,jedis.sdiff(likeKeys2,likeKeys1));
        jedis.srem(likeKeys1,"5");
        print(22,jedis.sismember(likeKeys1,"5"));
        print(23,jedis.scard(likeKeys1));
        jedis.smove(likeKeys2,likeKeys1,"18");
        print(25,jedis.smembers(likeKeys1));
        print(25,jedis.smembers(likeKeys2));

//      zset
        String rankKey = "rankKey";
        jedis.zadd(rankKey,18,"jim");
        jedis.zadd(rankKey,16,"Jame");
        jedis.zadd(rankKey,23,"Bob");
        jedis.zadd(rankKey,180,"Mary");
        jedis.zadd(rankKey,1000,"Gen");
        print(30,jedis.zcard(rankKey));
        print(30,jedis.zcount(rankKey,18,40));
        print(30,jedis.zscore(rankKey,"Gen"));
        jedis.zincrby(rankKey,222,"Gen");
        jedis.zincrby(rankKey,222,"Gens");
        print(43,jedis.zscore(rankKey,"Gen"));
        print(43,jedis.zcount(rankKey,0,10000));
        print(22,jedis.zrevrange(rankKey,1,100));

        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey,"0","10000")) {
            print(45,tuple.getElement()+":"+String.valueOf(tuple.getScore()));
        }

        print(45,jedis.zrank(rankKey,"Gen"));
        print(45,jedis.zrevrank(rankKey,"Gen"));

        JedisPool pool = new JedisPool("192.168.10.136",6379);
        for (int i = 0; i < 199; i++) {
            Jedis j = pool.getResource();
            System.out.println("Pool="+j);
        }
    }
}
