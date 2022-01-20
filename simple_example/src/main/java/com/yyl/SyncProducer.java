package com.yyl;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * producer端同步发送消息
 *  使用比较广泛，比如重要的消息通知，短信通知。
 */
public class SyncProducer {
    public static void main( String[] args ) throws MQClientException, UnsupportedEncodingException, MQBrokerException, RemotingException, InterruptedException {
        // 实例化生产者
        DefaultMQProducer defaultProducer = new DefaultMQProducer("unique_producer_group_name");
        // 设置nameServer地址，默认获取环境变量
        defaultProducer.setNamesrvAddr("www.youngeryang.top:9876");
        // 启动producer
        defaultProducer.start();

        for (int i = 0; i < 10; i++) {
            // 创建消息，并指定Topic，Tag和消息体。  keys：默认为空字符串，flag：是否单向RPC，waitStoreMsgOK：等待消息存储成功
            Message message = new Message("TopicTest", "TAG_A", "", 0, "Hello RocketMQ".getBytes(StandardCharsets.UTF_8), true);
            // 发送消息到一个Broker
            SendResult sendResult = defaultProducer.send(message, 50000);
            // 查看返回结果
            System.out.println(sendResult);
        }
        defaultProducer.shutdown();
    }
}
