package com.mashibing.strategy.filter.impl;

import com.hmm.common.constant.CacheConstant;
import com.hmm.common.constant.SmsConstant;
import com.hmm.common.enums.ExceptionEnums;
import com.hmm.common.exception.StrategyException;
import com.hmm.common.model.StandardSubmit;
import com.mashibing.strategy.client.BeaconCacheClient;
import com.mashibing.strategy.filter.StrategyFilter;
import com.mashibing.strategy.util.ErrorSendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 1小时发送3条的限流规则
 * @author hmm
 * @description
 */
@Service("limitOneHour")
@Slf4j
public class LimitOneHourStrategyFilter implements StrategyFilter {


    private final String UTC = "+8";

    private final long ONE_HOUR = 60 * 1000 * 60 - 1;

    private final int RETRY_COUNT = 2;

    private final int LIMIT_HOUR = 3;

    @Autowired
    private BeaconCacheClient cacheClient;

    @Autowired
    private ErrorSendMsgUtil sendMsgUtil;


    @Override
    public void strategy(StandardSubmit submit) {
        // 判断短信类型不是验证码类的，直接结束方法
        if(submit.getState() != SmsConstant.CODE_TYPE){
            return;
        }
        //1、基于submit获取短信的发送时间
        LocalDateTime sendTime = submit.getSendTime();
        //2、基于LocalDateTime获取到时间的毫秒值
        long sendTimeMilli = sendTime.toInstant(ZoneOffset.of(UTC)).toEpochMilli();
        submit.setOneHourLimitMilli(sendTimeMilli);
        //3、基于submit获取客户标识以及手机号信息
        Long clientId = submit.getClientId();
        String mobile = submit.getMobile();

        //4、优先将当前短信发送信息插入到Redis的ZSet结构中 zadd
        String key = CacheConstant.LIMIT_HOURS + clientId + CacheConstant.SEPARATE + mobile;

        //5、如果插入失败，需要重新的将毫秒值做改变，尝试重新插入
        int retry = 0;
        while(!cacheClient.zadd(key, submit.getOneHourLimitMilli(), submit.getOneHourLimitMilli())){
            // 发送失败,尝试重试
            if(retry == RETRY_COUNT) break;
            retry++;
            // 插入失败，是因为存储的member不允许重复，既然重复了，将时间向后移动，移动到当前系统时间
            submit.setOneHourLimitMilli(System.currentTimeMillis());
        }
        // 如果retry为2，代表已经重试了2次，但是依然没有成功
        if(retry == RETRY_COUNT){
            log.info("【策略模块-一小时限流策略】  插入失败！ 满足一小时限流规则，无法发送！");
            submit.setErrorMsg(ExceptionEnums.ONE_HOUR_LIMIT + ",mobile = " + mobile);
            sendMsgUtil.sendWriteLog(submit);
            sendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnums.ONE_HOUR_LIMIT);
        }
        // 没有重试2次，3次之内，将数据正常的插入了。基于zrangebyscore做范围查询
        long start = submit.getOneHourLimitMilli() - ONE_HOUR;
        int count = cacheClient.zRangeByScoreCount(key, Double.parseDouble(start + ""), Double.parseDouble(submit.getOneHourLimitMilli() + ""));

        if(count > LIMIT_HOUR){
            log.info("【策略模块-一小时限流策略】  插入失败！ 满足一小时限流规则，无法发送！");
            cacheClient.zRemove(key,submit.getOneHourLimitMilli() + "");
            submit.setErrorMsg(ExceptionEnums.ONE_HOUR_LIMIT + ",mobile = " + mobile);
            sendMsgUtil.sendWriteLog(submit);
            sendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnums.ONE_HOUR_LIMIT);
        }

        log.info("【策略模块-一小时限流策略】  一小时限流规则通过，可以发送！");

    }
}