package com.yyl.transaction;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.ThreadFactoryImpl;
import org.apache.rocketmq.common.constant.LoggerName;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.logging.inner.Level;
import org.apache.rocketmq.logging.inner.Logger;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * producer  事务消息发布者
 * 事务的三种状态：提交状态，回滚装填，中间状态:
 * COMMIT_MESSAGE：提交事务。消费者可以开始消费事务中发送的消息了。
 * RollbackTransaction：回滚事务。代表该消息将被删除。消费者不能消费
 * Unknown：中间状态，代表需要检查消息队列来确定状态。
 *
 * @author yyl
 * 2022/1/21 17:05
 */
public class TransactionProducer {
    public static void main(String[] args) throws MQClientException, InterruptedException {
        // 实例化事务生产者
        TransactionMQProducer producer = new TransactionMQProducer("unique_producer_group_name");
        // 设置NameSrv地址，用于从nameSrv中获取所有已注册的topic和broker信息
        producer.setNamesrvAddr("www.youngeryang.top:9876");
        // 设置事务监听器
        producer.setTransactionListener(new TransactionListenerImpl());
        // 设置执行线程池
        producer.setExecutorService(new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(200), new ThreadFactoryImpl("client-transaction-msg-check-thread")));
        // 启动producer
        producer.start();

        String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < 10; i++) {
            Message message = new Message("TopicTest", tags[i % tags.length], "Key" + i, ("Hello RocketMQ In Transaction" + i).getBytes(StandardCharsets.UTF_8));
            TransactionSendResult sendResult = producer.sendMessageInTransaction(message, null);
            System.out.printf("%s%n", sendResult);
            Thread.sleep(10);
        }
        for (int i = 0; i < 100000; i++) {
            Thread.sleep(1000);
        }
        producer.shutdown();
    }

    /**
     * 当发送半消息成功时，我们使用 executeLocalTransaction 方法来执行本地事务。它返回前一节中提到的三个事务状态之一。
     * checkLocalTransaction 方法用于检查本地事务状态，并回应消息队列的检查请求。它也是返回前一节中提到的三个事务状态之一。
     */
    private static class TransactionListenerImpl implements TransactionListener {
        private final AtomicInteger transactionIndex = new AtomicInteger(0);
        private final ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

        /**
         * 执行本地事务，返回一个事务状态。
         * 返回UNKNOW：告诉队列去执行checkLocalTransaction()方法区检查本地的事务状态。
         */
        @Override
        public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            System.out.println("executeLocalTransaction:" + new String(msg.getBody()));
            int value = transactionIndex.getAndIncrement();
            int status = value % 3;
            localTrans.put(msg.getTransactionId(), status);
            return LocalTransactionState.UNKNOW;
        }

        /**
         * 用于检查本地的事务状态。回应消息队列该消息的事务状态。
         */
        @Override
        public LocalTransactionState checkLocalTransaction(MessageExt msg) {
            Integer status = localTrans.get(msg.getTransactionId());
            String sd = "checkLocalTransaction:" + new String(msg.getBody()) + ", status:" + status + ",";
            switch (status){
                case 0:
                    System.out.print(sd + "准备再次执行检查\n");
                    return LocalTransactionState.UNKNOW;
                case 1:
                    System.out.print(sd + "消息发送成功\n");
                    return LocalTransactionState.COMMIT_MESSAGE;
                case 2:
                    System.out.print(sd + "该消息将被回滚丢弃\n");
                    return LocalTransactionState.ROLLBACK_MESSAGE;
            }
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
    }
}
