package com.mashibing.monitor.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;
import java.util.Set;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/8/8 18:52
 */
@FeignClient(value = "beacon-cache")
public interface CacheClient {

    @PostMapping(value = "/cache/keys/{pattern}")
    Set<String> keys(@PathVariable String pattern);
    @GetMapping(value = "/cache/hgetall/{key}")
    Map hGetAll(@PathVariable(value="key") String key);
}
