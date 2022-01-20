package com.yyl;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 计数器测试
 *
 * @author yyl
 * 2022/1/20 11:48
 */
public class ParallelLimit {
    static int threadCount = 5;
    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        CountDownLatch cdl = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            CountRunnable runnable = new CountRunnable(cdl);
            pool.execute(runnable);
        }
        System.out.println("线程创建完毕");
    }
}

class CountRunnable implements Runnable {
    private final CountDownLatch countDownLatch;
    public CountRunnable(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }
    @Override
    public void run() {
        try {
            /* 每次减少一个容量，内部使用了aqs的volatile和CAS保证了线程安全问题 */

                countDownLatch.countDown();
                System.out.println("thread counts = " + (countDownLatch.getCount()));

            countDownLatch.await(5000, TimeUnit.SECONDS);
            System.out.println("concurrency counts = " + (ParallelLimit.threadCount - countDownLatch.getCount()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}