package com.yyl.sync;

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
        // 实例化生产者。 producerGroup在生产者组中必须是唯一的，对于非事务性消息，每个进程中唯一即可。
        DefaultMQProducer defaultProducer = new DefaultMQProducer("unique_producer_group_name");
        // 设置nameServer地址，默认获取环境变量
        defaultProducer.setNamesrvAddr("www.youngeryang.top:9876");
        // 启动producer
        defaultProducer.start();

        for (int i = 0; i < 10; i++) {
            // 创建消息，并指定Topic，Tag和消息体。  keys：默认为空字符串，flag：是否单向RPC，waitStoreMsgOK：等待消息存储成功
            Message message = new Message("TopicTest", "TAG_A", "", 0, ("Hello RocketMQ" + (i + 1)).getBytes(StandardCharsets.UTF_8), true);
            // 设置延时等级。 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h。 Broker内部每一个等级都有一个队列，这些队列属于SCHEDULE_TOPIC_XXXX的TOPIC中。延迟时间到达后broker才将消息投递到真实的topic中。
            message.setDelayTimeLevel(3);
            // 发送消息到一个Broker
            SendResult sendResult = defaultProducer.send(message, 50000);
            // 查看返回结果
            System.out.println(sendResult);
        }
        defaultProducer.shutdown();
    }
}
