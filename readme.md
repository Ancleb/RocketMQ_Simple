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
- flushDiskType = 同步刷盘或者异步刷盘。 异步刷盘：消息到达broker的PageCache后即可返回投递ack，broker后台异步的将消息持久化到磁盘。
- brokerIP1 = broker的IP地址。



### connection fail
broker启动后，会使用这个IP把broker注册到namesrv中的路由信息表表中。<br>
默认情况下，namesrv将自动取broker发送心跳包（broker信息）时request的来源ip。<br>
但是broker和namesrv在同一台机器上时，这个request的来源IP将变成内网IP，导致外网的Client获取到的路由表保存的是一个内网IP。最终connect failed<br>

在config/broker.conf文件中添加`brokerIP1 = broker的ip地址`（client端将使用这个ip进行连接broker）。
