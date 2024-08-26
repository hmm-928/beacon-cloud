package com.mashibing;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/12 19:27
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/12 19:27
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class StrategyStarterApp {

    public static void main(String[] args) {
        SpringApplication.run(StrategyStarterApp.class,args);
    }
    @Bean
    public RestTemplate restTemplate(){
        return  new RestTemplate();
    }
}