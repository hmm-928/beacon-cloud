package com.mashibing.common.util;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/16 13:58
 */

import com.mashibing.common.enums.CMPP2DeliverEnums;
import com.mashibing.common.enums.CMPP2ResultEnums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/16 13:58
 */

public class CMPP2DeliverUtil {
    private static Map<String,String> stats = new HashMap<>();
    static {
        CMPP2DeliverEnums[] cmpp2DeliverEnums = CMPP2DeliverEnums.values();
        for(CMPP2DeliverEnums cmpp2DeliverEnum :cmpp2DeliverEnums){
            stats.put(cmpp2DeliverEnum.getStat(),cmpp2DeliverEnum.getDescription());
        }
    }
    //通过result get msg
    public static  String getResultMessage(String stat){
        return stats.get(stat);
    }
}
