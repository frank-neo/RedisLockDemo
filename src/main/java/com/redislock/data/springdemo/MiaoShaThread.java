package com.redislock.data.springdemo;

import com.redislock.data.javademo.KuCun;
import redis.clients.jedis.Jedis;

import java.util.UUID;

public class MiaoShaThread extends Thread{

    String key = "MiaoSHA";
    String requestId = UUID.randomUUID().toString();
    Long expireTime = 5000L;

    Jedis jedis ;

    public MiaoShaThread(Jedis jedis){
        this.jedis = jedis;
    }

    //秒杀逻辑
    @Override
    public void run(){
        if (RedisLockTool.tryGetDistributedLock(jedis,key,requestId,expireTime)){

            if (KuCun.kucun-1>=0){
                KuCun.kucun--;
                System.out.println("当前线程为："+Thread.currentThread().getName()+";抢到货了！！！"+"；当前库存为："+KuCun.kucun);
                jedis.close();
            }else{
                System.out.println("当前线程为："+Thread.currentThread().getName()+";没库存了~"+"；当前库存为："+KuCun.kucun);
                jedis.close();
            }

            RedisLockTool.releaseDistributedLock(jedis,key,requestId);

        }else{

            System.out.println("当前线程为："+Thread.currentThread().getName()+";手速不够被别人抢了。。。");

        }
    }
}
