package com.mashibing.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 因为使用框架 这些不需要了
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/10 11:28
 * 因为默认使用data-redis提供的RedisTemplate对象时，
 * 针对key和value的序列化方式都是byte[]，这种方式在图形化界面上的查看不太优化，
 * 将key做String的序列化，将Value做JSON的序列化。
 *
 * 同时也是为了让RedisTemplate支持JDK8的日期格式
 */
@Configuration
public class RedisConfig {
   /* @Bean
    public <T> RedisTemplate<String,T>
    redisTemplate(RedisConnectionFactory foctory, RedisSerializer<Object> redisSerializer){
        //1.构建redistemplate对象
        RedisTemplate<String,T> redisTemplate = new RedisTemplate<>();
        //2.设置RedisConnectionFactory连接到redistemplate对象
        redisTemplate.setConnectionFactory(foctory);
        //3.设置redis的key序列化方式
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        //4.设置value序列化方式  Date
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);

        //5.保证生效
        redisTemplate.afterPropertiesSet();
        //6.返回
        return redisTemplate;
    }
    //基于value序列化方式
    @Bean
    public RedisSerializer<Object> redisSerializer(){
        //构建Jackson的objectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        //JDK8日期支持
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Module timeModule = new JavaTimeModule()
                .addDeserializer(LocalDate.class,new LocalDateDeserializer(dateFormatter))
                .addDeserializer(LocalDateTime.class,new LocalDateTimeDeserializer(dateTimeFormatter))
                .addSerializer(LocalDate.class,new LocalDateSerializer(dateFormatter))
                .addSerializer(LocalDateTime.class,new LocalDateTimeSerializer(dateTimeFormatter));

        objectMapper.registerModule(timeModule);
        //3. 构建Jackson2JsonRedisSerializer
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        //返回设置好的
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        //5. 返回设置好的Jackson2JsonRedisSerializer
        return jackson2JsonRedisSerializer;
    }*/
}
