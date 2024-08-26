package com.mashibing.strategy.util;

import com.hmm.common.constant.CacheConstant;
import com.hmm.common.constant.RabbitMQConstants;
import com.hmm.common.constant.SmsConstant;
import com.hmm.common.model.StandardReport;
import com.hmm.common.model.StandardSubmit;
import com.mashibing.strategy.client.BeaconCacheClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ErrorSendMsgUtil {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BeaconCacheClient cacheClient;

    /**
     * 策略模块校验未通过，发送写日志操作
     * @param submit
     * @param dirtyWords
     */
    public void sendWriteLog(StandardSubmit submit) {
        submit.setReportState(SmsConstant.REPORT_FAIL);
        // 发送消息到写日志队列
        rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_WRITE_LOG,submit);
    }

    /**
     * 策略模块校验未通过，发送状态报告操作
     */
    public void sendPushReport(StandardSubmit submit) {
        // 查询当前客户的isCallback
        Integer isCallback = cacheClient.hgetInteger(CacheConstant.CLIENT_BUSINESS + submit.getApikey(), "isCallback");
        // 查看是否需要给客户一个回调
        if(isCallback == 1){
            // 如果需要回调，再查询客户的回调地址
            String callbackUrl = cacheClient.hget(CacheConstant.CLIENT_BUSINESS + submit.getApikey(), "callbackUrl");
            // 如果回调地址不为空。
            if(!StringUtils.isEmpty(callbackUrl)){
                // 封装客户的报告推送的信息，开始封装StandardReport
                StandardReport report = new StandardReport();
                BeanUtils.copyProperties(submit,report);
                report.setIsCallback(isCallback);
                report.setCallbackUrl(callbackUrl);
                // 发送消息到RabbitMQ
                rabbitTemplate.convertAndSend(RabbitMQConstants.SMS_PUSH_REPORT,report);
            }

        }
    }
}