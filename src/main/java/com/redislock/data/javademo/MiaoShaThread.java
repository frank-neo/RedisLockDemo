package com.redislock.data.javademo;

import redis.clients.jedis.Jedis;

import java.util.UUID;

public class MiaoShaThread extends Thread{

    Jedis jedis = new Jedis("localhost",6379);
    String key = "MiaoSHA";
    String requestId = UUID.randomUUID().toString();
    Long expireTime = 5000L;

    //秒杀逻辑
    @Override
    public void run(){
        //if (RedisLockTool.tryGetDistributedLock(jedis,key,requestId,expireTime)){
        if (RedisLockTool.tryGetDistributedLock(jedis,key,requestId,expireTime)){

            if (KuCun.kucun-1>=0){
                KuCun.kucun--;
                System.out.println("当前线程为："+Thread.currentThread().getName()+";抢到货了！！！");
                //及时的关闭连接很重要，这样我redis.maxTotal=40设置为40，50个人来抢资源也是勉强足够的【设置为1的时候不抱错，但是有点问题】
                jedis.close();
            }else{
                System.out.println("当前线程为："+Thread.currentThread().getName()+";没库存了~");
                jedis.close();
            }

            RedisLockTool.releaseDistributedLock(jedis,key,requestId);

        }else{

            System.out.println("当前线程为："+Thread.currentThread().getName()+";手速不够被别人抢了。。。");
            jedis.close();
        }
    }
}
