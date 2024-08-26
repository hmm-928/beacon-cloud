package com.mashibing.controller;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/10 16:53
 */

import com.msb.framework.redis.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/10 16:53
 */
@RestController
@Slf4j
public class CacheController {
    @Autowired
    private RedisClient redisClient;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @PostMapping(value = "/cache/hmset/{key}")
    public void hmSet(@PathVariable(value="key") String key, @RequestBody Map<String,Object> map){
        log.info("[缓存模块]hmset方法存储key={},存储value={}",key,map);
        redisClient.hSet(key,map);
    }
    @PostMapping(value = "/cache/set/{key}")
    public void set(@PathVariable(value="key") String key, @RequestParam(value = "value") Object value){
        log.info("[缓存模块] set方法存储key={},存储value={}",key,value);
        redisClient.set(key,value);
    }
    @PostMapping(value = "/cache/sadd/{key}")
    public void sadd(@PathVariable(value="key") String key, @RequestBody Map<String,Object>... value){
        log.info("[缓存模块] sadd方法存储key={},存储value={}",key,value);
        redisClient.sAdd(key,value);
    }
    @PostMapping(value = "/cache/saddstr/{key}")
    public void saddStr(@PathVariable(value="key") String key, @RequestBody String... value){
        log.info("[缓存模块] saddStr方法存储key={},存储value={}",key,value);
        redisClient.sAdd(key,value);
    }
    @PostMapping(value = "/cache/sinterstr/{key}/{sinterKey}")
    public Set<Object> sinterStr(@PathVariable(value="key") String key, @PathVariable String sinterKey,@RequestBody String... value){
        log.info("[缓存模块] sinterStr 的交集方法 key={},sinterKey={},存储value={}",key,sinterKey,value);
        //存储数据到set集合
        redisClient.sAdd(key,value);
        //2.需要将key和sinterKey做交集,并拿回返回的set
        Set<Object> result = redisTemplate.opsForSet().intersect(key,sinterKey);
        //3.将key删除
        redisClient.delete(key);
       //4.返回交集结果
        return result;
    }


    @GetMapping(value = "/cache/hgetall/{key}")
    public Map hGetAll(@PathVariable(value="key") String key){
        Map<String,Object> value= redisClient.hGetAll(key);
        log.info("[缓存模块]hGetAll方法获取key={},存储value={}",key,value);
        return value;
    }
    @GetMapping(value = "/cache/hget/{key}/{filed}")
    public Object hGet(@PathVariable(value="key") String key,@PathVariable(value="filed") String filed){
        Object value= redisClient.hGet(key,filed);
        log.info("[缓存模块]hGet方法获取key={},filed={},存储value={}",key,filed,value);
        return value;
    }

    @GetMapping(value = "/cache/smember/{key}")
    public Set smember(@PathVariable(value="key") String key){
        Set<Object> values= redisClient.sMembers(key);
        log.info("[缓存模块]smember方法获取key={}, 存储value={}",key, values);
        return values;
    }

    @PostMapping(value = "/cache/pipeline/string")
    public void pipelineString(@RequestBody Map<String,String> map){
        log.info("[缓存模块]pipelineString,获取到存储的数据，map的长度={}的数据",map.size());
        redisClient.pipelined(operations->{
            for(Map.Entry<String,String> entry :map.entrySet()){
                operations.opsForValue().set(entry.getKey(),entry.getValue());
            }
        });
    }
    @GetMapping(value = "/cache/get/{key}")
    public Object get(@PathVariable(value="key") String key){
        Object value= redisClient.get(key);
        log.info("[缓存模块]get方法获取key={}, 存储value={}",key, value);
        return value;
    }
    @PostMapping(value = "/cache/zadd/{key}/{score}/{member}")
    public Boolean zadd(@PathVariable(value = "key")String key,
                        @PathVariable(value = "score")Long score,
                        @PathVariable(value = "member")Object member){
        log.info("【缓存模块】 zaddLong方法，存储key = {}，存储score = {}，存储value = {}", key,score, member);
        Boolean result = redisClient.zAdd(key, member, score);
        return result;
    }

    @GetMapping(value = "/cache/zrangebyscorecount/{key}/{start}/{end}")
    public int zRangeByScoreCount(@PathVariable(value = "key") String key,
                                  @PathVariable(value = "start") Double start,
                                  @PathVariable(value = "end") Double end) {
        log.info("【缓存模块】 zRangeByScoreCount方法，查询key = {},start = {},end = {}", key,start,end);
        Set<ZSetOperations.TypedTuple<Object>> values = redisTemplate.opsForZSet().rangeByScoreWithScores(key, start, end);
        if(values != null){
            return values.size();
        }
        return 0;
    }

    @DeleteMapping(value = "/cache/zremove/{key}/{member}")
    public void zRemove(@PathVariable(value="key") String key,@PathVariable(value="member") String member){
        log.info("[缓存模块]zRemove方法删除key={},member={}",key,  member);
        redisClient.zRemove(key,member);
    }

    @PostMapping(value = "/cache/hincrby/{key}/{field}/{delta}")
    public Long hIncyBy(@PathVariable(value="key") String key,
                        @PathVariable(value = "field")String field,
                        @PathVariable(value="delta")Long delta){
        Long result = redisClient.hIncrementBy(key,field,delta);
        log.info("[缓存模块]hIncyBy方法自增key={},field={}, number={},剩余数值为={}",key, field,delta,result);

        return result;
    }
    @PostMapping(value = "/cache/keys/{pattern}")
    public Set<String>  keys(@PathVariable String pattern){
        log.info("[缓存模块]keys方法查询所有keys信息pattern{}",pattern);
        Set<String> keys =  redisTemplate.keys(pattern);
        log.info("[缓存模块]keys方法查询所有keys信息pattern{} keys信息{}",pattern,keys);
        return  keys;

    }
 /*   @PostMapping(value = "/cache/hincrby/{key}/{field}/{delta}")
    public Long hIncrBy(@PathVariable(value = "key") String key,
                        @PathVariable(value = "field") String field,
                        @PathVariable(value = "delta") Long delta){
        log.info("【缓存模块】 hIncrBy方法，自增   key = {},field = {}，number = {}", key,field,delta);
        Long result = redisClient.incrementMap(key, field, delta);
        log.info("【缓存模块】 hIncrBy方法，自增   key = {},field = {}，number = {},剩余数值为 = {}", key,field,delta,result);
        return result;
    }*/

}
