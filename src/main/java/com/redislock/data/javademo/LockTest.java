package com.redislock.data.javademo;

public class LockTest {

    public static void main(String[] args) {

        for (int i = 0; i <50 ; i++) {

            MiaoShaThread miaoShaThread = new MiaoShaThread();
            miaoShaThread.setName("线程"+i);
            miaoShaThread.start();
        }

    }

}


