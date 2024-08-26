package com.mashibing.strategy.util;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/16 14:06
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 获取手机号归属地和运营商的工具
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/16 14:06
 */
@Component
public class MobileOperatorUtil {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestTemplate restTemplate;
    private final String url1="https://cx.shouji.360.cn/phonearea.php?number=";
    private final String   CODE="code";
    private final String  DATA="data";
    private final String  PROVINCE="province";
    private final String  CITY="city";
    private final String  SP="sp";
    private final String  SPACE=" ";
    private final String  SEPARATE=",";
    public String getMobileInfoBy360(String mobile){
        String url = url1;
        //发送请求获取信息
        String mobileInfoJson = restTemplate.getForObject(url+mobile,String.class);
        //解析json
        Map map = null;
        try {
            map = objectMapper.readValue(mobileInfoJson, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Integer code =(Integer) map.get(CODE);
        if(code!=0){
            return null;
        }
        Map<String ,String> areaAndOperator =(Map<String,String>) map.get(DATA);
        String province = areaAndOperator.get(PROVINCE);
        String city = areaAndOperator.get(CITY);
        String sp =areaAndOperator.get(SP);
        //封装为省市 运营商 格式返回
        return province+SPACE+city+SEPARATE+sp;


    }
}
