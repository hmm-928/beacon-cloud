package com.mashibing.monitor.task;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/8/8 21:41
 */

import com.mashibing.monitor.client.CacheClient;
import com.mashibing.monitor.util.MailUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * 监听客户余额
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/8/8 21:41
 */
@Component
@Slf4j
public class MonitorClientBalanceTask {
    //客户余额小于500直接发送
    private final long balanceLimit = 500000;
    private final String EMAIL = "extend1";
    private final String BALANCE = "balance";
    //
    private final String CLIENT_BALANCE_PATTERN="client_balance:*";
    private String text="客户您好，您的余额剩余%s元,请您及时补充余额。";

    @Autowired
    private CacheClient cacheClient;

    @Autowired
    private MailUtil mailUtil;
    @XxlJob("MonitorClientBalanceTask")
    public void monitor(){
        //1.查询客户余额信息
        Set<String> keys = cacheClient.keys(CLIENT_BALANCE_PATTERN);
        //2判断余额
        for(String key:keys){
            Map map = cacheClient.hGetAll(key);
            Long balance = Long.parseLong(map.get(BALANCE)+"");
            String email = (String) map.get(EMAIL);
            if(balance< balanceLimit){
                log.info("余额不足");
                //发送邮件
                mailUtil.sendEmail(email,"【短信平台】提醒您余额不足。",String.format(text,balance/1000));
            }
        }
    }

}
