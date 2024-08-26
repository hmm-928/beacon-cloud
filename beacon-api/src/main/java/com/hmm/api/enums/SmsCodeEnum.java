package com.mashibing.api.enums;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/9 18:27
 */

import lombok.Getter;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/9 18:27
 * 响应信息  code  message对应
 */
@Getter
public enum SmsCodeEnum {
    PARAMETER_ERROR(-10,"参数不合法");
    private Integer code;
    private String  msg;

     SmsCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
