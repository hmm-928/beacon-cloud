package com.hmm.smsgateway.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/14 22:34
 */
@FeignClient(value = "beacon-cache")
public interface BeaconCacheClient {
    @GetMapping("/cache/hget/{key}/{field}")
    String hget(@PathVariable(value = "key")String key, @PathVariable(value = "field")String field);

    @GetMapping("/cache/hget/{key}/{field}")
    Integer hgetInteger(@PathVariable(value = "key")String key, @PathVariable(value = "field")String field);



}
