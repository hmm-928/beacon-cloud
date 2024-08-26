package com.mashibing.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.Set;

/**
 * @author zjw
 * @description
 */
@FeignClient(value = "beacon-cache")
public interface BeaconCacheClient {

    @GetMapping("/cache/hgetall/{key}")
    Map hGetAll(@PathVariable(value = "key")String key);

    @GetMapping("/cache/hget/{key}/{field}")
    Object hget(@PathVariable(value = "key")String key,@PathVariable(value = "field")String field);

    @GetMapping("/cache/hget/{key}/{field}")
    String hgetString(@PathVariable(value = "key")String key,@PathVariable(value = "field")String field);

    @GetMapping("/cache/smember/{key}")
    Set<Map> smember(@PathVariable(value = "key")String key);

}
