package com.yyl.OpenMessaging;

import io.openmessaging.Message;
import io.openmessaging.MessagingAccessPoint;
import io.openmessaging.OMS;
import io.openmessaging.OMSBuiltinKeys;
import io.openmessaging.consumer.MessageListener;
import io.openmessaging.consumer.PushConsumer;

/**
 * @author yyl
 * 2022/1/22 11:53
 */
public class OMSPushConsumer {
    public static void main(String[] args) {
        final MessagingAccessPoint messagingAccessPoint = OMS.getMessagingAccessPoint("oms:rocketmq://www.youngeryang.top:9876/default:default");
        // 创建consumer
        PushConsumer consumer = messagingAccessPoint.createPushConsumer(OMS.newKeyValue().put(OMSBuiltinKeys.CONSUMER_ID, "OMS_CONSUMER"));
        // 启动accessPoint
        messagingAccessPoint.startup();

        System.out.printf("MessagingAccessPoint startup OK%n");

        // 注册主线程停止钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            consumer.shutdown();
            messagingAccessPoint.shutdown();
        }));

        // 消费者注册队列监听器
        consumer.attachQueue("OMS_HELLO_TOPIC", new MessageListener() {
            @Override
            public void onReceived(Message message, Context context) {
                System.out.printf("Received one message: %s,  %s%n", message.sysHeaders().getString(Message.BuiltinKeys.MESSAGE_ID), context.attributes());
                context.ack();
            }
        });
        // 消费者启动
        consumer.startup();
        System.out.printf("Consumer startup OK%n");
    }
}
