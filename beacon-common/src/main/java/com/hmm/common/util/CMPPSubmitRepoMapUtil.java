package com.mashibing.common.util;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/8/7 14:00
 */

import com.mashibing.common.model.StandardSubmit;

import java.util.concurrent.ConcurrentHashMap;

/**
 * CMPP发送短信 临时存储的位置
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/8/7 14:00
 */

public class CMPPSubmitRepoMapUtil {
    private static ConcurrentHashMap<String, StandardSubmit>  map = new ConcurrentHashMap<>();
    public static void put(int sequence,StandardSubmit standardSubmit){
        map.put(sequence + "",standardSubmit);

    }
    public  static StandardSubmit get(int sequence){
        return map.get(sequence+"");
    }
    public static StandardSubmit remove(int sequence){
       return  map.remove(sequence+"");
    }
}
