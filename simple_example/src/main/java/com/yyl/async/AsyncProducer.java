package com.yyl.async;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * producer异步发送消息
 *  用于响应时间比较敏感的业务中
 *
 * @author yyl
 * 2022/1/20 11:32
 */
public class AsyncProducer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException {
        // 实例化生产者。 producerGroup在生产者组中必须是唯一的，对于非事务性消息，每个进程中唯一即可。
        DefaultMQProducer producer = new DefaultMQProducer("unique_producer_group_name");
        // 设置broker要连接的nameSrv
        producer.setNamesrvAddr("www.youngeryang.top:9876");
        // 启动producer。内部会建立长连接
        producer.start();

        // 异步发送模式下，消息发送失败时内部重试次数。 由于幂等性原因(broker返回ack时,出现问题导致client接收或处理失败)可能会造成重复发送。
        producer.setRetryTimesWhenSendAsyncFailed(0);

        int messageCount = 10;
        // 根据消息数量实例化减数器
        CountDownLatch countDownLatch = new CountDownLatch(messageCount);

        for (int i = 0; i < messageCount; i++) {
            Message message = new Message("TopicTest", "TagA", "OrderID188", ("Hello Async RocketMQ " + (i + 1)).getBytes(StandardCharsets.UTF_8));
            // 异步等待broker返回ack，长连接监听到broker的响应，启动一个新的线程执行回调函数。
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    countDownLatch.countDown();
                    System.out.println("sendResult = " + sendResult);
                }

                @Override
                public void onException(Throwable e) {
                    countDownLatch.countDown();
                    System.out.println("消息发送失败");
                    e.printStackTrace();
                }
            });
        }
        // broker全部响应完成。 等待5s。不能一直等待，因为有可能broker返回响应时client由于一切意外情况接收失败响应。就会造成线程死锁
        countDownLatch.await(5, TimeUnit.SECONDS);
        // 关闭Producer实例
        producer.shutdown();
    }
}
