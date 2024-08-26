package com.mashibing.common.util;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/16 13:58
 */

import com.mashibing.common.enums.CMPP2ResultEnums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/16 13:58
 */

public class CMPP2ResultUtil {
    private static Map<Integer,String> operators = new HashMap<>();
    static {
        CMPP2ResultEnums[] cmpp2ResultEnums = CMPP2ResultEnums.values();
        for(CMPP2ResultEnums cmpp2ResultEnum :cmpp2ResultEnums){
            operators.put(cmpp2ResultEnum.getResult(),cmpp2ResultEnum.getMsg());
        }
    }
    //通过result get msg
    public static  String getResultMessage(Integer result){
        return operators.get(result);
    }
}
