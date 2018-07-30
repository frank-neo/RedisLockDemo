package com.redislock.data.springdemo;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class SpringRedisTest {

    static ClassPathXmlApplicationContext appCtx = new ClassPathXmlApplicationContext("classpath*:applicationContext-redis.xml");
    static JedisPool jedisPool = (JedisPool)appCtx.getBean("jedisPool",JedisPool.class);


    public static void main(String[] args) {

        for (int i = 0; i <50 ; i++) {
            Jedis jedis = jedisPool.getResource();
            MiaoShaThread miaoShaThread = new MiaoShaThread(jedis);
            miaoShaThread.setName("线程"+i);
            miaoShaThread.start();
        }

    }

}
