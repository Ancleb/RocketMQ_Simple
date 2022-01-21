package com.yyl.sqlfilter;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;

/**
 * 简单的同步发送消息
 * @author yyl
 * 2022/1/21 15:50
 */
public class Producer {

    static String[] tags = {"TagA", "TagB", "TagC"};

    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("unique_producer_group_name");
        producer.setNamesrvAddr("www.youngeryang.top:9876");
        producer.start();

        // 发送10条消息，不同的的topic，不同的key，不同的properties
        for (int i = 0; i < 10; i++) {
            Message message = new Message("TopicTest", tags[i % tags.length], "Key" + i, ("Hello RocketMQ " + (i + 1)).getBytes(StandardCharsets.UTF_8));
            // 设置一些属性
            message.putUserProperty("num", String.valueOf(i));
            SendResult sendResult = producer.send(message);
            System.out.println(sendResult);
        }
        producer.shutdown();
    }
}
