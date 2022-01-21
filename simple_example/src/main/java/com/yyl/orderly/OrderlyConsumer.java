package com.yyl.orderly;

import lombok.SneakyThrows;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Consumer 顺序消费消息，带事务方式（client可以控制offset什么时候提交）
 * @author yyl
 * 2022/1/21 11:36
 */
public class OrderlyConsumer {

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("unique_consumer_group_name");
        // 设置NameSrv
        consumer.setNamesrvAddr("www.youngeryang.top:9876");
        // 订阅主题
        consumer.subscribe("TopicTest", "TagA || TagB || TagC");
        /*
            设置消费者第一次启动后从队列的头部开始消费还是从尾部还是根据时间戳
            如果消费者不是第一次启动，那么将会自动继续上一次消费的位置开始消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        consumer.registerMessageListener(new MessageListenerOrderly() {
            @SneakyThrows
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                context.setAutoCommit(true); // 默认
                for (MessageExt msg : msgs) {
                    // 可以看到每个queue有唯一的consume线程来消费, 订单对每个queue(分区)有序
                    System.out.println("consumeThread=" + Thread.currentThread().getName() + "queueId=" + msg.getQueueId() + ", content:" + new String(msg.getBody()));
                }
                // 模拟处理业务中......
                TimeUnit.SECONDS.sleep(new Random(10).nextInt());
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        consumer.start();

        System.out.println("Consumer Started");


    }
}
