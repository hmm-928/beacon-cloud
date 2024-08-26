package com.mashibing.push.mq;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/29 21:30
 */


import com.mashibing.common.constant.RabbitMQConstants;
import com.mashibing.common.model.StandardReport;
import com.mashibing.common.util.JsonUtil;
import com.mashibing.push.config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/29 21:30
 */


@Component
@Slf4j
public class PushReportListener {

    // 重试的时间间隔。
    private int[] delayTime = {0,15000,30000,60000,300000};

    private final String SUCCESS = "SUCCESS";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 监控策略模块推送过来的消息（暂时是策略）
     * @param report
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = RabbitMQConstants.SMS_PUSH_REPORT)
    public void consume(StandardReport report, Channel channel,Message message) throws IOException {
        //1、获取客户的回调地址
        String callbackUrl = report.getCallbackUrl();
        if(StringUtils.isEmpty(callbackUrl)){
            log.info("【推送模块-推送状态报告】 客户方没有设置回调的地址信息！callbackUrl = {} ",callbackUrl);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }

        //2、发送状态报告
        boolean flag = pushReport(report);

        //3、如果发送失败，重试
        isResend(report, flag);

        //4、直接手动ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }


    /**
     * 监听延迟交换机路由过来的消息
     * @param report
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = RabbitMQConfig.DELAYED_QUEUE)
    public void delayedConsume(StandardReport report, Channel channel,Message message) throws IOException {
        // 1、发送状态报告
        boolean flag = pushReport(report);

        // 2、判断状态报告发送情况
        isResend(report, flag);

        // 手动ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }


    /**
     * 发送请求，给callbackUrl
     * @param report
     * @return
     */
    private boolean pushReport(StandardReport report) {
        // 声明返回结果，你默认为false
        boolean flag = false;

        //1、声明发送的参数
        String body = JsonUtil.obj2JSON(report);

        //2、声明RestTemplate的模板代码
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        try {
            log.info("【推送模块-推送状态报告】 第{}次推送状态报告开始！report = {}",report.getResendCount() + 1,report);
            String result = restTemplate.postForObject("http://" + report.getCallbackUrl(), new HttpEntity<>(body, httpHeaders), String.class);
            flag = SUCCESS.equals(result);
        } catch (RestClientException e) {
        }
        //3、得到响应后，确认是否为SUCCESS
        return flag;
    }
    /**
     * 判断状态报告是否推送成功，失败的话需要发送重试消息
     * @param report
     * @param flag
     */
    private void isResend(StandardReport report, boolean flag) {
        if(!flag){
            log.info("【推送模块-推送状态报告】 第{}次推送状态报告失败！report = {}",report.getResendCount() + 1,report);
            report.setResendCount(report.getResendCount() + 1);
            rabbitTemplate.convertAndSend(RabbitMQConfig.DELAYED_EXCHANGE, "", report, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {

                    // 设置延迟时间
                    message.getMessageProperties().setDelay(delayTime[report.getResendCount()]);
                    return message;
                }
            });
        }else{
            log.info("【推送模块-推送状态报告】 第一次推送状态报告成功！report = {}",report);
        }
    }


}