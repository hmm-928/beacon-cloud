package com.mashibing.strategy.filter.impl;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/16 9:27
 */

import com.hmm.common.constant.CacheConstant;
import com.hmm.common.constant.RabbitMQConstants;
import com.hmm.common.model.StandardSubmit;
import com.hmm.common.util.OperatorUtil;
import com.mashibing.strategy.client.BeaconCacheClient;
import com.mashibing.strategy.filter.StrategyFilter;
import com.mashibing.strategy.util.MobileOperatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 号段补全  手机号运营商及归属地
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/16 9:27
 * 首先需要基于submit中存储的手机号，去缓存模块查询对应的区域以及运营商信息
 *
 * 如果查询不到，需要通过发送请求的方式，请求 `https://cx.shouji.360.cn/phonearea.php?number=1388888`
 * 地址查询手机号对应的区域以及运营商信息，同时还需要发送消息到 短信管理平台模块 同步信息到MySQL，同时还需要同步到Redis中。
 *
 * 如果查询到，正常封装到StandardSubmit对象中
 */

@Service(value = "phase")
@Slf4j
public class PhaseStrategyFilter implements StrategyFilter {

    /**
     * 切分手机号前7位
     */
    private final int MOBILE_START = 0;
    private final int MOBILE_END = 7;
    /**
     * 校验的长度
     */
    private final int LENGTH = 2;
    /**
     * 分割区域和运营商的标识
     */
    private final String SEPARATE = ",";
    /**
     * 未知的情况
     */
    private final String UNKNOWN = "未知 未知,未知";

    @Autowired
    private BeaconCacheClient cacheClient;

    @Autowired
    private MobileOperatorUtil mobileOperatorUtil;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public void strategy(StandardSubmit submit) {
        log.info("【策略模块-号段补齐】   补全ing…………");
        //1、根据手机号前7位，查询手机号信息
        String mobile = submit.getMobile().substring(MOBILE_START, MOBILE_END);
        String mobileInfo = cacheClient.get(CacheConstant.PHASE + mobile);

        getMobileInfo: if (StringUtils.isEmpty(mobileInfo))  {
            //2、查询不到，需要调用三方接口，查询手机号对应信息
            mobileInfo = mobileOperatorUtil.getMobileInfoBy360(mobile);
            if(!StringUtils.isEmpty(mobileInfo)){
                //3、调用三方查到信息后，发送消息到MQ，并且同步到MySQL和Redis
                rabbitTemplate.convertAndSend(RabbitMQConstants.MOBILE_AREA_OPERATOR,submit.getMobile());
                break getMobileInfo;
            }
            mobileInfo = UNKNOWN;
            //TODO 通知手机号未知
        }

        //4、无论是Redis还是三方接口查询到之后，封装到StandardSubmit对象中
        String[] areaAndOperator = mobileInfo.split(SEPARATE);
        if (areaAndOperator.length == LENGTH) {
            submit.setArea(areaAndOperator[0]);
            submit.setOperatorId(OperatorUtil.getOperatorIdByOperatorName(areaAndOperator[1]));
        }

    }
}
