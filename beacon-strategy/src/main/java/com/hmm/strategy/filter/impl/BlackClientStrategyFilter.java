package com.mashibing.strategy.filter.impl;

import com.hmm.common.constant.CacheConstant;
import com.hmm.common.enums.ExceptionEnums;
import com.hmm.common.exception.StrategyException;
import com.hmm.common.model.StandardSubmit;
import com.mashibing.strategy.client.BeaconCacheClient;
import com.mashibing.strategy.filter.StrategyFilter;
import com.mashibing.strategy.util.ErrorSendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "blackClient")
@Slf4j
public class BlackClientStrategyFilter implements StrategyFilter {

    @Autowired
    private ErrorSendMsgUtil sendMsgUtil;

    @Autowired
    private BeaconCacheClient cacheClient;

    // 黑名单的默认value
    private final String TRUE = "1";

    @Override
    public void strategy(StandardSubmit submit) {
        log.info("【策略模块-客户级别黑名单校验】   校验ing…………");
        //1、获取发送短信的手机号,以及客户的ID
        String mobile = submit.getMobile();
        Long clientId = submit.getClientId();

        //2、调用Redis查询
        String value = cacheClient.getString(CacheConstant.BLACK + clientId + CacheConstant.SEPARATE + mobile);

        //3、如果查询的结果为"1"，代表是黑名单
        if(TRUE.equals(value)){
            log.info("【策略模块-客户级别黑名单校验】   当前发送的手机号是客户黑名单！ mobile = {}",mobile);
            submit.setErrorMsg(ExceptionEnums.BLACK_CLIENT + ",mobile = " + mobile);
            sendMsgUtil.sendWriteLog(submit);
            sendMsgUtil.sendPushReport(submit);
            throw new StrategyException(ExceptionEnums.BLACK_CLIENT);
        }
        //4、不是1，正常结束
        log.info("【策略模块-客户级别黑名单校验】   当前手机号不是客户黑名单！ ");
    }
}