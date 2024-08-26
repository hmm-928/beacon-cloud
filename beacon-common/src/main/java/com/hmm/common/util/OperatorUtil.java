package com.mashibing.common.util;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/16 13:58
 */

import com.mashibing.common.enums.MobileOperatorEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/16 13:58
 */

public class OperatorUtil {
    private static Map<String,Integer> operators = new HashMap<>();
    static {
        MobileOperatorEnum[] operatorEnums = MobileOperatorEnum.values();
        for(MobileOperatorEnum operatorEnum :operatorEnums){
            operators.put(operatorEnum.getOperatorName(),operatorEnum.getOperatorId());
        }
    }
    //通过运营商名称 获取运营商id
    public static  Integer getOperatorIdByOperatorName(String operatorName){
        return operators.get(operatorName);
    }
}
