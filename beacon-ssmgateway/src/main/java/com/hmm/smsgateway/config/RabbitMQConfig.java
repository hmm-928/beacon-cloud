package com.hmm.smsgateway.config;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/12 18:30
 */

import com.hmm.common.constant.RabbitMQConstants;
import org.springframework.amqp.core.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hemengmeng
 * @version 1.0构建队列&交换机信息
 * Create by 2024/7/12 18:30
 */

@Configuration
public class RabbitMQConfig {
    private final int TTL = 10000;
    private final String FANOUT_ROUTING_KEY = "";

    /**
     * 声明死信队列 需要准备普通交换机
     * @return
     */
    @Bean
    public Exchange normalExchange(){
        return ExchangeBuilder.fanoutExchange(RabbitMQConstants.SMS_GATEWAY_NORMAL_EXCHANGE).build();
    }

    /**
     *
     * @return
     */
    @Bean
    public Queue normalQueue(){
        Queue queue =QueueBuilder.durable(RabbitMQConstants.SMS_GATEWAY_NORMAL_QUEUE)
                .withArgument("x-message-ttl",TTL)
                .withArgument("x-dead-letter-exchange",RabbitMQConstants.SMS_GATEWAY_DEAD_EXCHANGE )
                .withArgument("x-dead-letter-routing-key",FANOUT_ROUTING_KEY)
                .build();
        return queue;
    }
    @Bean
    public Binding normalBinding( Exchange normalExchange,Queue normalQueue){
        return BindingBuilder.bind(normalQueue).to(normalExchange).with("").noargs();
    }
    //
    @Bean
    public Exchange deadExchange(){
        return ExchangeBuilder.fanoutExchange(RabbitMQConstants.SMS_GATEWAY_DEAD_EXCHANGE).build();
    }
    @Bean
    public Queue deadQueue(){
        return QueueBuilder.durable(RabbitMQConstants.SMS_GATEWAY_DEAD_QUEUE).build();
    }
    @Bean
    public Binding deadBinding( Exchange deadExchange,Queue deadQueue){
        return BindingBuilder.bind(deadQueue).to(deadExchange).with("").noargs();
    }
}