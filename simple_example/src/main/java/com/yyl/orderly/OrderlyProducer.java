package com.yyl.orderly;

import lombok.Data;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * producer 顺序发送消息
 *
 * @author yyl
 * 2022/1/21 10:08
 */
public class OrderlyProducer {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static String[] tags = {"TagA", "TagB", "TagC"};

    public static void main(String[] args) throws MQClientException, InterruptedException, MQBrokerException, RemotingException {
        DefaultMQProducer producer = new DefaultMQProducer("unique_producer_group_name");
        producer.setNamesrvAddr("www.youngeryang.top:9876");
        producer.start();

        // 发送10条消息
        List<Order> orders = buildOrders();
        for (int i = 0; i < orders.size(); i++) {
            String body = sdf.format(new Date()) + " Hello RocketMQ " + orders.get(i);
            Message message = new Message("TopicTest", tags[i % tags.length], "Key" + i, body.getBytes(StandardCharsets.UTF_8));
            SendResult sendResult = producer.send(message, new MessageQueueSelector() {
                /**
                 * 自定义一个算法， 选择消息要发送到哪个队列中。 默认是队列轮询。
                 *
                 * @param mqs client会每30s通过心跳从nameSrv取出broker及队列信息。
                 * @param arg 业务参数，send方法的第三个参数会被传入这个select方法
                 */
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    // 根据订单ID，选择一个队列。则这个订单ID相关的所有操作都会进入同一个队列中，这样consumer在拉取消息时就会使有序的。
                    // 虽然不同的订单Id可能会进入同一个队列。但是我们只要保证同一个订单的所有操作是相对有序的，至于在创建和付款之间是否会有其他的订单操作不影响最终的有序性。
                    Long orderId = (Long) arg;
                    long index = orderId % mqs.size();
                    return mqs.get((int) index);
                }
            }, orders.get(i).orderId);
            System.out.printf("SendResult status:%s, queueId:%d, body:%s%n", sendResult.getSendStatus(), sendResult.getMessageQueue().getQueueId(), body);
        }


        producer.shutdown();


    }


    private static List<Order> buildOrders(){
        List<Order> orderList = new ArrayList<>();
        Order orderDemo = new Order();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("创建");
        orderList.add(orderDemo);

        orderDemo = new Order();
        orderDemo.setOrderId(15103111065L);
        orderDemo.setDesc("创建");
        orderList.add(orderDemo);

        orderDemo = new Order();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("付款");
        orderList.add(orderDemo);

        orderDemo = new Order();
        orderDemo.setOrderId(15103117235L);
        orderDemo.setDesc("创建");
        orderList.add(orderDemo);

        orderDemo = new Order();
        orderDemo.setOrderId(15103111065L);
        orderDemo.setDesc("付款");
        orderList.add(orderDemo);

        orderDemo = new Order();
        orderDemo.setOrderId(15103117235L);
        orderDemo.setDesc("付款");
        orderList.add(orderDemo);

        orderDemo = new Order();
        orderDemo.setOrderId(15103111065L);
        orderDemo.setDesc("完成");
        orderList.add(orderDemo);

        orderDemo = new Order();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("推送");
        orderList.add(orderDemo);

        orderDemo = new Order();
        orderDemo.setOrderId(15103117235L);
        orderDemo.setDesc("完成");
        orderList.add(orderDemo);

        orderDemo = new Order();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("完成");
        orderList.add(orderDemo);


        return orderList;
    }



    /**
     * 订单
     */
    @Data
    private static class Order {
        private long orderId;
        private String desc;
    }
}
