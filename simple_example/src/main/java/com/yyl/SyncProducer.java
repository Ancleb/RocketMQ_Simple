package com.yyl;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;

/**
 * producer端同步发送消息
 */
public class SyncProducer {
    public static void main( String[] args ) throws MQClientException {
        // 实例化生产者
        DefaultMQProducer defaultProducer = new DefaultMQProducer("unique_producer_group_name");
        // 设置nameServer地址，默认获取环境变量
        defaultProducer.setNamesrvAddr("www.youngeryang.top:9876");
        // 启动producer
        defaultProducer.start();



    }
}
