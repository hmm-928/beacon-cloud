package com.mashibing.strategy.util;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/27 15:41
 */

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/27 15:41
 */

/**
 * 欠费工具类
 */
public class ClientBalanceUtil {
    /**
     * 如果后期要给客户指定欠费的额度等级，再重写方法
     * @param clientId
     * @return
     */
    public static Long getClientAmountLimit(Long clientId){
        //负10元
        return -10000L;

    }
}
