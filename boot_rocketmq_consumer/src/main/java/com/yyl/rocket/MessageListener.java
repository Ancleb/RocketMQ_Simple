package com.yyl.rocket;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;


/**
 * @author yyl
 * 2022/1/24 16:08
 */
@RocketMQMessageListener(consumerGroup = "${rocketmq.consumer.group}", topic = "BOOT_PRODUCER_TOPIC")
@Slf4j
@Component
public class MessageListener implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        log.info("您有新的消息，请注意查收：" + message);
    }
}
