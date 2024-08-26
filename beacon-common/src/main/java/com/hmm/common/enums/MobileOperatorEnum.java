package com.mashibing.common.enums;

import lombok.Getter;

/**
 * 电话运营商枚举类
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/16 13:53
 */
@Getter
public enum MobileOperatorEnum {
    CHINA_MOBILE(1,"移动"),
    CHINA_UNICOM(2,"联通"),
    CHINA_TELECOM(3,"电信"),
    UNKNOWN(0,"未知");

    private Integer operatorId;
    private String operatorName;

    MobileOperatorEnum(Integer operatorId, String operatorName) {
        this.operatorId = operatorId;
        this.operatorName = operatorName;
    }
}
