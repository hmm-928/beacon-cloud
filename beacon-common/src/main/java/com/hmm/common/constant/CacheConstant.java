package com.mashibing.common.constant;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/11 20:37
 */

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/11 20:37
 */
//缓存模块前缀
public interface CacheConstant {
    /**
     * 客户信息
     */
    String CLIENT_BUSINESS="client_business:";
    /**
     * 客户签名
     */
    String CLIENT_SIGN="client_sign:";
    /**
     * 客户签名模板
     */
    String CLIENT_TEMPLATE="client_template:";
    /**
     * 客户余额
     */
    String CLIENT_BALANCE="client_balance:";
    /**
     * 号段补全
     */
    String PHASE="phase:";
    /**
     *敏感词
     */
    String DIRTY_WORD="dirty_word:";
    /**
     *间隔符
     */
    String SEPARATE=":";
    /**
     *黑名单
     */
    String BLACK="black:";
    /**
     *携号转网
     */
    String TRANSFER="transfer:";
    /**
     *一分钟限流
     */
    String LIMIT_MINUTES ="limit_minutes:";
    /**
     * 一小时限流
     */
    String  LIMIT_HOURS="limit_hours:";
    /**
     * 路由策略
     */
    String CLIENT_CHANNEL="client_channel:";
    /**
     * 通道信息权重
     */
    String CHANNEL="channel:";
}
