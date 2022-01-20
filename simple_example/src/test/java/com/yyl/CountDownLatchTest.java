package com.yyl;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 计数器测试
 * @author yyl
 * 2022/1/20 13:40
 */
public class CountDownLatchTest {

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(2);
        System.out.println("主线程开始执行…… ……");
        //第一个子线程执行
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("子线程："+Thread.currentThread().getName()+"执行");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
        }).start();

        //第二个子线程执行
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("子线程："+Thread.currentThread().getName()+"执行");
            latch.countDown();
        }).start();


        System.out.println("等待两个线程执行完毕…… ……");
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("两个子线程都执行完毕，继续执行主线程");
    }
}
