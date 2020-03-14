# Seckill-system
*clone项目后，修改/server/src/main/resources/application.properties中的数据源配置和发送邮件配置，将项目部署在Tomcat上即可，默认首页为http://localhost:8080/  
已将建库文件db_second_kill.sql上传  
需要同时打开Zookeeper和Redis*
+ 基于SpringBoot的秒杀系统，前端利用JSP页面做简单展示，使用maven管理依赖，整个系统部署在外部Tomcat服务器上，即本机，没有使用内嵌Tomcat
+ 用户登录使用Shiro来控制
+ 分别利用redis、redisson和zookeeper来控制并发情况下产生线程安全问题，利用Jmeter做了压力测试
+ 利用雪花算法来产生分布式唯一ID，避免订单号重复
+ 生成订单号之后通过RabbitMQ异步发送邮件通知用户
+ 利用死信队列失效超时未支付订单

*TODO:* 未支付订单对商品数量进行回滚

&emsp;&emsp;&ensp;&ensp;编写支付宝、微信支付功能
