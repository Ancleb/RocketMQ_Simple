package com.yyl;

import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 消息消费者
 *
 * @author yyl
 * 2022/1/20 16:31
 */
public class Consumer {

    public static void main(String[] args) throws MQClientException {
        // 同一个consumerGroupName中订阅相同TOPIC的Consumer可以组成一个消费组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("unique_consumer_group_name");
        // 设置nameSrv地址。  client需要从nameSrv中获取publishTableInfo（存了已注册的broker信息，由topic和address构成路由表）
        consumer.setNamesrvAddr("www.youngeryang.top:9876");

        // 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
        // consumer.subscribe("TopicTest", "TAG_A");
        consumer.subscribe("TopicTest", "*");

        // 注册回调。 consumer从broker中拉取消息后，启动一个线程去处理消息后，再次去broker拉取消息。
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            // System.out.println(JSONObject.toJSONString(context));
            // System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
            // System.out.println();
            System.out.println("您有新的消息，请注意查收");
            msgs.forEach(msg -> {
                System.out.printf("%s\tReceive New Messages: %s\t%s\t%s\t%s\t%s\n", Thread.currentThread().getName(), new String(msg.getBody()), msg.getTopic(), msg.getQueueId(), msg.getMsgId(), msg.getProperties());
            });
            // 标记该消息已经被成功消费
            return ((int)(Math.random() * 10 + 1)) % 2 == 0 ? ConsumeConcurrentlyStatus.CONSUME_SUCCESS : ConsumeConcurrentlyStatus.RECONSUME_LATER;
        });

        consumer.start();

        Set<MessageQueue> topicTest = consumer.fetchSubscribeMessageQueues("TopicTest");
        System.out.println(topicTest);
        System.out.println("Consumer Started");
    }
}
