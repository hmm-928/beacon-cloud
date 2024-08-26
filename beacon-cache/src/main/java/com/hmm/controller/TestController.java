package com.mashibing.controller;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/10 13:35
 */

import com.msb.framework.redis.RedisClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/10 13:35
 */
@RestController
public class TestController {
    //写测试  hash结构
    @Autowired
    private RedisClient redisClient;
    @PostMapping("/test/set/{key}")
    public String set(@PathVariable String key, @RequestBody Map map){
        redisClient.hSet(key,map);
        return "ok";
    }
    // 读测试
    @GetMapping("/test/get/{key}")
    public Map get(@PathVariable String key){
        Map<Object, Object> result = redisClient.hGet("age",key);
        return result;
    }
    /**
     * #### 3.6.1 问题一：
     *
     * 直接使用RedisTemplate对象，及基于他的API与Redis服务交互时，方法的实用性比较差。
     *
     * 方法名和Redis中的命令差别很大。
     *
     * #### 3.6.2 问题二：
     *
     * 基于Java客户端与Redis交互时，很多使用会采用一个注解，@Cacheable。
     *
     * 如果当前缓存模块需要基于@Cacheable去与Redis交互时，需要再次配置CacheManager，
     * 以及KeyGenerator等配置信息
     *
     * 其次如果还需要使用Redis做分布式锁，一般用分布式锁的客户端大多是采用Redisson，
     * Redisson内部的watchDog以及一些锁重入等操作时，封装的比较完善。
     * 需要再次配置一些基于Redisson的信息。
     */
    //管道测试
    @PostMapping("/test/pipeline")
    public String pipeline(){
        Map<String,Object> maps = new HashMap<>();
        maps.put("1888888","北京，北京,移动 ");
        maps.put("188888d","北京，北京,移动 ");
        redisClient.pipelined(operations -> {
            for(Map.Entry<String,Object> entry :maps.entrySet()){
                operations.opsForValue().set(entry.getKey(),entry.getValue());
            }
        });
                return "ok";
    }
}
