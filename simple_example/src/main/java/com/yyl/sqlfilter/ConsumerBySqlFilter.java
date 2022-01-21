package com.yyl.sqlfilter;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;

import java.util.List;

/**
 * Consumer， 使用SQL92语法对消息的Tag进行过滤
 *  默认broker在被消费消息时不支持使用sql模式过滤消息<br>
 *  broker配置文件添加`enablePropertyFilter=true`。<br>
 *  每次过滤都去执行SQL表达式会影响效率，SQL92的表达式上下文为消息的属性。<br>
 * @author yyl
 * 2022/1/21 15:58
 */
public class ConsumerBySqlFilter {

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("unique_consumer_group_name");
        consumer.setNamesrvAddr("www.youngeryang.top:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("TopicTest", MessageSelector.bySql("num between 0 and 3"));
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            System.out.println("您有新的消息，请注意查收");
            msgs.forEach(System.out::println);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
    }
}
