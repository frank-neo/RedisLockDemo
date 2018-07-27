package com.redislock.data.redisdemo;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolConfiger {

    public static JedisPoolConfig jedisPoolConfig;
    public static JedisConnectionFactory jedisConnectionFactory;
    public static RedisTemplate redisTemplate;

    public JedisPoolConfiger(){

        JedisPoolConfig jpc = new JedisPoolConfig();
        jpc.setMaxIdle(300);
        jpc.setMaxTotal(1000);
        jpc.setMaxWaitMillis(1000);
        jpc.setMinEvictableIdleTimeMillis(300000);
        jpc.setNumTestsPerEvictionRun(1024);
        jpc.setTimeBetweenEvictionRunsMillis(30000);
        jpc.setTestOnBorrow(true);
        jpc.setTestWhileIdle(true);
        this.jedisPoolConfig = jpc;

        JedisConnectionFactory jcf = new JedisConnectionFactory();
        jcf.setPoolConfig(jedisPoolConfig);
        jcf.setHostName("127.0.0.1");
        jcf.setPort(6379);
        //本地redis没有设置密码所以不设置
        //jedisConnectionFactory.setPassword();
        jcf.setTimeout(10000);
        this.jedisConnectionFactory = jcf;


        RedisTemplate rt = new RedisTemplate();
        rt.setConnectionFactory(jedisConnectionFactory);
        rt.setKeySerializer(new StringRedisSerializer());
        rt.setValueSerializer(new JdkSerializationRedisSerializer());
        rt.setHashKeySerializer(new StringRedisSerializer());
        rt.setHashValueSerializer(new JdkSerializationRedisSerializer());
        rt.setEnableTransactionSupport(true);
        this.redisTemplate = rt;

    }

}
