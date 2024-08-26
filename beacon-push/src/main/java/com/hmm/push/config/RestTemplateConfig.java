package com.mashibing.push.config;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/29 23:18
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/29 23:18
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
