package com.mashibing.common.util;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/27 20:41
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/27 20:41
 */

public class JsonUtil {

    private static ObjectMapper objectMapper= new ObjectMapper()  ;
    public static String obj2JSON(Object obj){
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("转换json失败");
        }

    }
}
