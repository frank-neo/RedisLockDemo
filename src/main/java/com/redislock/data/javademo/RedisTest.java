package com.redislock.data.javademo;

import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 * 我们用这个测试类来测试下Jedis中多参数set方法的使用
 * 加锁后拿返回值的操作
 * @by FrankLee
 * 2018.7.27

 * 然后，我们用这个测试类来测试下Jedis中多参数eval()方法的使用
 * 解锁后拿返回值的操作
 * @by FrankLee
 * 2018.7.27
 */
public class RedisTest {

    //先定义常量
    //是否成功上锁？
    private static final String LOCK_SUCCESS = "OK";
    //ET IF NOT EXIST，即当key不存在时，我们进行set操作；若key已经存在，则不做任何操作；
    private static final String SET_IF_NOT_EXIST = "NX";
    //这个参数是PX，意思是我们要给这个key加一个过期的设置，具体时间由第五个参数决定。只是一个格式。
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    //解锁成功状态码
    private static final Long RELEASE_SUCCESS = 1L;

    public static void main(String[] args){

        Jedis jedis = new Jedis("localhost");

        System.out.println("Redis连接状态： "+jedis.ping());

        String lockKey = "testRdeiskey";//模拟锁值
        String requestId = "112233445566uiop";//模拟客户端传过来的值


        //开始使用set(String，String，String，String，Long)
        //解释下各个参数的作用：set(String【键值key】，String【缓存的值value，我们这是区分和记忆不同客户端的值】，String【以什么方式设置key？我们这里NX代表setnx】，String【成功后返回值】，Long【key的生效时间】)
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, 15000L);


        System.out.println("Redis的set结果："+result);

        //因为setnx，设置生效时间，都在一个set里面操作这就满足了原子性【要么全部成功要么全部失败】
        /**运行结果：
         * 第一次运行：
         * Redis连接状态： PONG
         * Redis的set结果：OK
         * ---------------------------
         * 5秒内第二次运行：
         * Redis连接状态： PONG
         * Redis的set结果：null
         * 【因为key还未失效，所以SET_IF_NOT_EXIST失败，返回null】
         * ---------------------------
         * 5秒后第二次运行：
         * Redis连接状态： PONG
         * Redis的set结果：OK
         * 【此时key已经失效，所以SET_IF_NOT_EXIST成功设置到key的值】
         */

        //开始解锁(设置此线程休眠时间，来控制什么时候去执行解锁，【失效前or失效后】)
        try {
            Thread.currentThread().sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object resultRelease = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        System.out.println(resultRelease);

        /**执行结果：
         * //设置1S后解锁，此时锁还没有失效，我们可以成功解锁
         * Redis连接状态： PONG
         * Redis的set结果：OK
         * 1
         * //设置6S后解锁，此时锁已经失效，我们解锁操作失败
         * Redis连接状态： PONG
         * Redis的set结果：OK
         * 0
         */

        /*
        关于解锁：那个lua脚本（也就是script），我要简单讲解下：
        其中redis.call（）是具体执行脚本的方法，并在执行结束之后返回值。【上面脚本的逻辑是get到锁我们就del，并且返回1；get不到锁我们就直接返回0】
        KEYS[1]是eval（Collections第一个值），也就是lockKey = "testRdeiskey",ARGV[1]同理。
        最后，最关键的一点：eval命令是原子性的，也就是说，lua脚本里面的命令要么全部成功，要么全部失败。
        */

    }

}
