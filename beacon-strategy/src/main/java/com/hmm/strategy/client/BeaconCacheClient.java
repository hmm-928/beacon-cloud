package com.mashibing.strategy.client;


import org.springframework.web.bind.annotation.*;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.Map;
import java.util.Set;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/14 22:34
 */
@FeignClient(value = "beacon-cache")
public interface BeaconCacheClient {
    @PostMapping(value = "/cache/sinterstr/{key}/{sinterKey}")
    Set<Object> sinterStr(@PathVariable(value="key") String key, @PathVariable String sinterKey,@RequestBody String... value);
    @GetMapping("/cache/hget/{key}/{field}")
    String hget(@PathVariable(value = "key")String key, @PathVariable(value = "field")String field);
    @GetMapping(value = "/cache/get/{key}")
    String get(@PathVariable(value="key") String key);
    @GetMapping(value = "/cache/smember/{key}")
    Set<String> smember(@PathVariable(value="key") String key);
    @GetMapping(value = "/cache/smember/{key}")
    Set<Map> smemberMap(@PathVariable(value="key") String key);
    @GetMapping("/cache/hget/{key}/{field}")
    Integer hgetInteger(@PathVariable(value = "key")String key, @PathVariable(value = "field")String field);

    @GetMapping(value = "/cache/get/{key}")
    String getString(@PathVariable(value="key") String key);
    @PostMapping(value = "/cache/zadd/{key}/{score}/{member}")
    Boolean zadd(@PathVariable(value = "key")String key,
                        @PathVariable(value = "score")Long score,
                        @PathVariable(value = "member")Object member);

    @GetMapping(value = "/cache/zrangebyscorecount/{key}/{start}/{end}")
    int zRangeByScoreCount(@PathVariable(value = "key") String key,
                                  @PathVariable(value = "start") Double start,
                                  @PathVariable(value = "end") Double end) ;
    @DeleteMapping(value = "/cache/zremove/{key}/{member}")
    void zRemove(@PathVariable(value="key") String key,@PathVariable(value="member") String member);
    @PostMapping(value = "/cache/hincrby/{key}/{field}/{delta}")
    Long hIncrBy(@PathVariable(value = "key") String key,
                        @PathVariable(value = "field") String field,
                        @PathVariable(value = "delta") Long delta);
    @GetMapping(value = "/cache/hgetall/{key}")
     Map hGetAll(@PathVariable(value="key") String key);

}
