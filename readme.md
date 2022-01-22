# 踩坑
## 安装启动

### broker配置文件
官网默认的启动命令`nohup sh mqbroker -n localhost:9876 &` <br>
localhost:9876  ：连接namesrv的地址 <br>
指定配置文件： `nohup sh mqbroker -n localhost:9876 -c ../conf/broker.conf &`<br>
配置文件中的重用配置项：
- brokerClusterName = broker所属集群名称
- brokerName = broker的名称
- brokerId = 相同brokerName中0：master，!0 = slave
- flushDiskType = 同步刷盘或者异步刷盘。 异步刷盘：消息到达broker的PageCache后即可返回投递ack，broker后台异步的将消息持久化到磁盘。<br> 注意：client的发送异步消息只是等待broker返回ack的线程是异步的。flushDiskType是broker决定什么时候返回ack标识
- brokerIP1 = broker的IP地址。



### connection fail
broker启动后，会使用这个IP把broker注册到namesrv中的路由信息表表中。<br>
默认情况下，namesrv将自动取broker发送心跳包（broker信息）时request的来源ip。<br>
但是broker和namesrv在同一台机器上时，这个request的来源IP将变成内网IP，导致外网的Client获取到的路由表保存的是一个内网IP。最终connect failed<br>

在config/broker.conf文件中添加`brokerIP1 = broker的ip地址`（client端将使用这个ip进行连接broker）。


### 开放端口
- nameSrv:9876
- 非vip通道端口:10911
- vip通道端口:10909
10909是VIP通道对应的端口，在JAVA中的消费者对象或者是生产者对象中关闭VIP通道即可无需开放10909端口


### doesn't support filter by SQL92
默认broker在被消费消息时不支持使用sql模式过滤消息<br>
broker配置文件添加`enablePropertyFilter=true`。<br>
每次过滤都去执行SQL表达式会影响效率，SQL92的表达式上下文为消息的属性。<br>


# 一次性发布大小限制
默认情况下每次最大只能发布4M，这一点可以在源码中查看到。
`org.apache.rocketmq.client.Validators.checkMessage()`

# 延迟队列
使用`msg.setDelayTimeLevel()`设置消息的延迟等级即可。
设置延时等级对应的延迟时间。 [1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h\]。 
Broker内部每一个等级都对应一个队列（某一个队列中只存储相同延迟等级的消息），这些队列属于SCHEDULE_TOPIC_XXXX的TOPIC。延迟时间到达后broker才将消息投递到真实的topic中。


# 消费起点
使用`consumer.setConsumeFromWhere()`方法可以设置consumer从queue的哪个位置上开始消费。<br>
- CONSUME_FROM_FIRST_OFFSET：consumer从最早可用的消息开始消费。
- CONSUME_FROM_LAST_OFFSET：从上次停止的位置开始消费。如果是新启动的consumer，则看consumerGroup最早的消息还没有过期的话，就从头开始消费，如果最早的消息已经过期则从最新消息开始消费，之前的消息全部丢弃。 （消息是经过topic和tag确定后的消息）
- CONSUME_FROM_TIMESTAMP：从指定时间戳开始消费。


# 批量消息
适用于一批小消息，这些小消息应该具有一些共同的特征：
1. 相同的topic
2. 相同的waitStoreOK

因为默认一次投递的消息大小不能超过4M,所以在不确定一批消息的大小时，需要对这一批消息进行分割。<br>
虽然发布消息的时候是一次性发布，但是consumer在消费消息时还是会调起多个线程对不同消息进行消费。而不是一个线程一次性批量接收。



# 顺序消息
RocketMQ的Features中将顺序消息分为`全局顺序`和`分区顺序`。<br>

顺序消费的原理：
    默认情况下，消息发送时会使用轮序的方式把消息发送到不同的queue（分区队列）；而消费消息时从多个queue中拉取消息，这种情况发送和消费时不能保证顺序的。
    控制发送的顺序消息依次只发送到同一个queue中，消费的时候只从这个queue上依次拉取，就保证了消费顺序。
    当发送和消费参与的queue只有一个时，则是全局有序；如果多个queue参与，则为分区有序。

用订单进行分区有序的示例。一个订单的顺序流程是：创建、付款、推送、完成。订单号相同的消息会被先后发送到同一个队列中，消费时，同一个OrderId获取到的肯定是同一个队列。


# 事务消息的使用原则
1. 事务消息不支持延时消息和批量消息。
2. 为了避免单个消息被检查太多次而导致半队列消息累积，我们默认将单个消息的检查次数限制为 15 次，但是用户可以通过 Broker 配置文件的 transactionCheckMax参数来修改此限制。如果已经检查某条消息超过 N 次的话（ N = transactionCheckMax ） 则 Broker 将丢弃此消息，并在默认情况下同时打印错误日志。用户可以通过重写 AbstractTransactionalMessageCheckListener 类来修改这个行为。
3. 事务消息将在 Broker 配置文件中的参数 transactionTimeout 这样的特定时间长度之后被检查。当发送事务消息时，用户还可以通过设置用户属性 CHECK_IMMUNITY_TIME_IN_SECONDS 来改变这个限制，该参数优先于 transactionTimeout 参数。
4. 事务性消息可能不止一次被检查或消费。
5. 提交给用户的目标主题消息可能会失败，目前这依日志的记录而定。它的高可用性通过 RocketMQ 本身的高可用性机制来保证，如果希望确保事务消息不丢失、并且事务完整性得到保证，建议使用同步的双重写入机制。
6. 事务消息的生产者 ID 不能与其他类型消息的生产者 ID 共享。与其他类型的消息不同，事务消息允许反向查询、MQ服务器能通过它们的生产者 ID 查询到消费者。



# OpenMessaging
OpenMessaging旨在建立消息和流处理规范，以为金融、电子商务、物联网和大数据领域提供通用框架及工业级指导方案。在分布式异构环境中，设计原则是面向云、简单、灵活和独立于语言。符合这些规范将帮助企业方便的开发跨平台和操作系统的异构消息传递应用程序。提供了openmessaging-api 0.3.0-alpha的部分实现，下面的示例演示如何基于OpenMessaging访问RocketMQ。