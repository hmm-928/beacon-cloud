package com.mashibing.strategy.config;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/12 18:30
 */

import com.hmm.common.constant.RabbitMQConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hemengmeng
 * @version 1.0构建队列&交换机信息
 * Create by 2024/7/12 18:30
 */

@Configuration
public class RabbitMQConfig {

    /**
     * 接口模块发送消息到策略模块的队列
     * @return
     */
    @Bean
    public Queue preSendQueue(){
        return QueueBuilder.durable(RabbitMQConstants.MOBILE_AREA_OPERATOR).build();
    }
    /**
     * 写日志的队列
     * @return
     */
    @Bean
    public Queue writeLogQueue(){
        return QueueBuilder.durable(RabbitMQConstants.SMS_WRITE_LOG).build();
    }
   //状态报告推送队列
    @Bean
    public Queue pushReportQueue(){
        return QueueBuilder.durable(RabbitMQConstants.SMS_PUSH_REPORT).build();
    }

}