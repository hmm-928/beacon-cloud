package com.mashibing.common.enums;

import lombok.Getter;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/11 20:51
 */
@Getter
public enum ExceptionEnums {

    ERROR_APIKEY(-1,"非法的apikey"),
    IP_NOT_WHITE(-2,"请求的ip不在白名单内"),
    ERROR_SIGN(-3,"无可用签名"),
    ERROR_TEMPLATE(-4,"无可用模板"),
    ERROR_MOBILE(-5,"手机号格式不正确"),
    BALANCE_NOT_ENOUGH(-6,"手客户余额不足"),
    PARAMETER_ERROR(-10,"参数不合法！"),
    SNOWFLAKE_OUT_OF_RANGE(-11,"雪花算法机器id/服务id超出最大范围"),
    SNOWFLAKE_TIME_BACK(-12,"当前服务雪花算法出现时间回拨！！！"),
    HAVE_DIRTY_WORD(-13,"短信内容包含敏感词信息"),
    BLACK_GLOBAL(-14,"当前发送的手机号是平台黑名单"),
    BLACK_CLIENT(-15,"当前发送的手机号是客户黑名单"),
    ONE_MINUTE_LIMIT(-16,"一分钟限流生效，无法发送短信！"),
    ONE_HOUR_LIMIT(-17,"一小时限流生效，无法发送短信！"),
    NO_CHANNEL(-18,"没有选择到可用的通道！"),
    UNKNOWN_ERROR(-19,"声明通道对应队列以及发送消息时出现了问题！"),
    SEARCH_INDEX_ERROR(-20,"es添加文档信息失败"),
    SEARCH_UPDATE_ERROR(-21,"es修改日志失败"),
    NOT_LOGIN(-22,"没有登录"),
    SMS_NO_AUTHOR(-23,"用户权限不足"),
    KAPACHA_ERROR(-24,"验证码不正确"),
    AUTHEN_ERROR(-25,"用户名或密码错误"),
    USER_MENU_ERROR(-26,"查询用户菜单失败")

    ;
    private Integer code;

    private String msg;

    ExceptionEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
