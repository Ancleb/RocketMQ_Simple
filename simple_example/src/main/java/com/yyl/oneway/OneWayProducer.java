package com.yyl.oneway;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;

/**
 * producer单向发送消息
 *
 * @author yyl
 * 2022/1/20 16:02
 */
public class OneWayProducer {


    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException {
        // 实例化生产者，producerGroupName在生产者中必须是唯一的。 对于在非事务性消息，每个仅保证每个进程中是唯一的即可。
        DefaultMQProducer producer = new DefaultMQProducer("unique_producer_group_name");
        // 设置nameSrv地址。  client需要从nameSrv中获取publishTableInfo（存了已注册的broker信息，由topic和address构成路由表）
        producer.setNamesrvAddr("www.youngeryang.top:9876");
        // 启动producer
        producer.start();
        // 发送消息
        // 创建消息，并指定Topic，Tag和消息体。  keys：默认为空字符串，flag：是否单向RPC，waitStoreMsgOK：等待消息存储成功
        Message message = new Message("TopicTest", "TAG_A", null);
        for (int i = 0; i < 10; i++) {
            message.setBody(("Hello RocketMQ " + (i + 1)).getBytes(StandardCharsets.UTF_8));

            // 发送单向消息，没有任何返回结果
            producer.sendOneway(message);
        }
        producer.shutdown();
    }
}
