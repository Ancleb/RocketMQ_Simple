package com.yyl.OpenMessaging;

import io.openmessaging.*;
import io.openmessaging.producer.Producer;
import io.openmessaging.producer.SendResult;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

/**
 * OpenMessaging旨在建立消息和流处理规范，以为金融、电子商务、物联网和大数据领域提供通用框架及工业级指导方案。在分布式异构环境中，设计原则是面向云、简单、灵活和独立于语言。符合这些规范将帮助企业方便的开发跨平台和操作系统的异构消息传递应用程序。提供了openmessaging-api 0.3.0-alpha的部分实现，下面的示例演示如何基于OpenMessaging访问RocketMQ。
 *
 * @author yyl
 * 2022/1/22 11:40
 */
public class OMSProducer {

    public static void main(String[] args) {
        final MessagingAccessPoint messagingAccessPoint =
                OMS.getMessagingAccessPoint("oms:rocketmq://www.youngeryang.top:9876/default:default");
        final Producer producer = messagingAccessPoint.createProducer();
        messagingAccessPoint.startup();
        System.out.printf("MessagingAccessPoint 启动 OK%n");
        producer.startup();
        System.out.printf("Producer 启动 OK%n");
        // producer发送消息
        {
            Message message = producer.createBytesMessage("OMS_HELLO_TOPIC", "OMS_HELLO_BODY".getBytes(StandardCharsets.UTF_8));
            SendResult sendResult = producer.send(message);
            //final Void aVoid = result.get(3000L);
            System.out.printf("发送异步消息 OK, msgId: %s%n", sendResult.messageId());
        }
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        {
            final Future<SendResult> result = producer.sendAsync(producer.createBytesMessage("OMS_HELLO_TOPIC", "OMS_HELLO_BODY".getBytes(Charset.forName("UTF-8"))));
            result.addListener(new FutureListener<SendResult>() {
                @Override
                public void operationComplete(Future<SendResult> future) {
                    if (future.getThrowable() != null) {
                        System.out.printf("发送异步消息失败, error: %s%n", future.getThrowable().getMessage());
                    } else {
                        System.out.printf("发送异步消息成功, msgId: %s%n", future.get().messageId());
                    }
                    countDownLatch.countDown();
                }
            });
        }
        // 发送单向消息
        {
            producer.sendOneway(producer.createBytesMessage("OMS_HELLO_TOPIC", "OMS_HELLO_BODY".getBytes(StandardCharsets.UTF_8)));
            System.out.printf("Send oneway message OK%n");
        }
        try {
            countDownLatch.await();
            Thread.sleep(500); // 等一些时间来发送消息
        } catch (InterruptedException ignore) {
        }
        producer.shutdown();
    }
}
