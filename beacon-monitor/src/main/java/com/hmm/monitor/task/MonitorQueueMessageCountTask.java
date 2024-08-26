package com.mashibing.monitor.task;

import com.mashibing.common.constant.RabbitMQConstants;
import com.mashibing.monitor.client.CacheClient;

import com.mashibing.monitor.util.MailUtil;
import com.rabbitmq.client.Channel;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

/**
 * 监控队列中的消息个数，如果队列消息超过10000条，直接发送短信，通知。
 * @author hmm
 * @version V1.0.0
 */
@Component
@Slf4j
public class MonitorQueueMessageCountTask {

    String text = "<h1>您的队列消息队列堆积了，队名为%s，消息个数为%s</h1>";

    // 队列消息限制
    private final long MESSAGE_COUNT_LIMIT = 10000;

    // 查询队列名称的固定pattern
    private final String QUEUE_PATTERN = "channel:*";

    // 得到需要截取channelID的索引地址
    private final int CHANNEL_ID_INDEX = QUEUE_PATTERN.indexOf("*");

    // 注入RabbitMQ的ConnectionFactory
    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private CacheClient cacheClient;

    @Autowired
    private MailUtil mailUtil;

    @XxlJob("monitorQueueMessageCountTask")
    public void monitor() throws MessagingException {
        //1、拿到所有的队列名称
        Set<String> keys = cacheClient.keys(QUEUE_PATTERN);



        //2、需要基于channel去操作
        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(false);
        listenQueueAndSendEmail(channel, RabbitMQConstants.SMS_PRE_SEND);
        for (String key : keys) {
            // 封装队列名称
            String queueName = RabbitMQConstants.SMS_GATEWAY + key.substring(CHANNEL_ID_INDEX);
            listenQueueAndSendEmail(channel, queueName);
        }




    }

    private void listenQueueAndSendEmail(Channel channel, String queueName) throws MessagingException {
        // 队列不存在，直接构建，如果已经存在，直接忽略
        try {
            channel.queueDeclare(queueName,true,false,false,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //3、拿到对应队列的消息，确认消息数量，超过限制，及时通知

        long count = 0;
        try {
            count = channel.messageCount(queueName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(count > MESSAGE_COUNT_LIMIT){
            //4、通知的方式就是发送短信。
            mailUtil.sendEmail(queueName + "队列消息堆积",String.format(text,queueName,count));
        }
    }


}