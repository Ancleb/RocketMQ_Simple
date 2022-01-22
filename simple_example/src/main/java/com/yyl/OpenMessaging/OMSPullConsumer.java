package com.yyl.OpenMessaging;

import io.openmessaging.Message;
import io.openmessaging.MessagingAccessPoint;
import io.openmessaging.OMS;
import io.openmessaging.OMSBuiltinKeys;
import io.openmessaging.consumer.PullConsumer;
import io.openmessaging.producer.Producer;
import io.openmessaging.producer.SendResult;

/**
 * 用OMS PullConsumer 来从指定的队列中拉取消息
 * @author yyl
 * 2022/1/22 11:51
 */
public class OMSPullConsumer {
    public static void main(String[] args) {
        final MessagingAccessPoint messagingAccessPoint = OMS.getMessagingAccessPoint("oms:rocketmq://www.youngeryang.top:9876/default:default");
        messagingAccessPoint.startup();
        final PullConsumer consumer = messagingAccessPoint.createPullConsumer(
                OMS.newKeyValue().put(OMSBuiltinKeys.CONSUMER_ID, "OMS_CONSUMER"));
        messagingAccessPoint.startup();
        System.out.printf("MessagingAccessPoint startup OK%n");
        final String queueName = "OMS_HELLO_TOPIC";

        consumer.attachQueue(queueName);
        consumer.startup();
        System.out.printf("Consumer startup OK%n");
        // 运行直到发现一个消息被发送了
        boolean stop = false;
        while (!stop) {
            Message message = consumer.receive();
            if (message != null) {
                String msgId = message.sysHeaders().getString(Message.BuiltinKeys.MESSAGE_ID);
                System.out.printf("Received one message: %s%n", msgId);
                consumer.ack(msgId);
                stop = true;
            } else {
                System.out.printf("Return without any message%n");
            }
        }
        consumer.shutdown();
        messagingAccessPoint.shutdown();
    }
}
