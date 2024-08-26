package com.mashibing.common.util;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/16 13:58
 */

import com.mashibing.common.enums.CMPP2DeliverEnums;
import com.mashibing.common.model.StandardReport;
import com.mashibing.common.model.StandardReport;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于cmpp的状态回调时，获取核心信息的方式
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/16 13:58
 */

public class CMPPDeliverMapUtil {
    private static ConcurrentHashMap<String, StandardReport> map = new ConcurrentHashMap<>();

    public static void put(String sequence, StandardReport standardReport) {
        map.put(sequence, standardReport);

    }

    public static StandardReport get(String sequence) {
        return map.get(sequence);
    }

    public static StandardReport remove(String sequence) {
        return map.remove(sequence);
    }
}

