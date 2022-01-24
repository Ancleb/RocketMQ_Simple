package com.yyl;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.remoting.protocol.RocketMQSerializable;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQListenerContainer;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.apache.rocketmq.spring.support.RocketMQUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.core.MessagePostProcessor;

@SpringBootTest
@Slf4j
class BootRocketmqProducerApplicationTests {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    void contextLoads() {
        rocketMQTemplate.convertAndSend("BOOT_PRODUCER_TOPIC", "Hello Spring RocketMQ");
        log.info("Producer：发送消息完成");
    }

}
