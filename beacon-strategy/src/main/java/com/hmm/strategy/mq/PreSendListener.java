package com.mashibing.strategy.mq;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/12 19:40
 */

import com.hmm.common.constant.RabbitMQConstants;
import com.hmm.common.model.StandardSubmit;
import com.mashibing.strategy.filter.StrategyFilterContext;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author hemengmeng
 * @version 1.0 监听接口模块推送的消息
 * Create by 2024/7/12 19:40
 */

@Component
@Slf4j
public class PreSendListener {


    /**
     * 整个策略模块的校验
     */
    @Autowired
    private StrategyFilterContext filterContext;

    @RabbitListener(queues = RabbitMQConstants.SMS_PRE_SEND)
    public void listen(StandardSubmit submit, Message message, Channel channel) throws IOException {
        log.info("【策略模块-接收消息】 接收到接口模块发送的消息 submit = {}",submit);
        // 处理业务…………
        try {
            filterContext.strategy(submit);
            log.info("【策略模块-消费完毕】手动ack");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("【策略模块-消费失败】凉凉~~~");
        }
    }
}