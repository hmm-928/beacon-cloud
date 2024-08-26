package com.mashibing.common.enums;

import lombok.Getter;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/8/7 19:12
 */
@Getter
public enum CMPP2ResultEnums {
    OK(0,"正确"),
    MESSAGE_BUILD_ERROR(1,"消息结构错"),
    COMMAND_WORD_ERROR(2,"命令字错"),
    MESSAGE_SEQUENCE_ERROR(3,"消息序号重复"),
    MESSAGE_LENGTH_ERROR(4,"消息长度错"),
    INCORRECT_TARIFF_ERROR(5,"资费代码错"),
    EXCEEDING_MAXIMUM_MESSAGE_LENGTH(6,"超过最大信息长"),
    BUSINESS_CODE_ERROR(7,"业务代码错"),
    FLOW_CONTROL_ERROR(8,"流量控制错"),
    UNKNOWN(9,"其他错误")
    ;
    private Integer result;
    private String msg ;

    CMPP2ResultEnums(Integer result, String msg) {
        this.result = result;
        this.msg = msg;
    }
}
