package com.redislock.data.redisdemo;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

public class MsTest {

    static ClassPathXmlApplicationContext appCtx = new ClassPathXmlApplicationContext("applicationContext-redis.xml");


    public static void main(String[] args) {
        RedisTemplate<String, Object> redisTemplate = (RedisTemplate)appCtx.getBean("redisTemplate",RedisTemplate.class);


        //RedisUtil redisUtil=(RedisUtil) appCtx.getBean("redisUtil");
        System.out.println("开始");

        MsService service = new MsService();

        for (int i = 0; i < 100; i++) {
            ThreadB threadA = new ThreadB(service, redisTemplate, "MSKEY");
            threadA.start();

        }

    }
}
