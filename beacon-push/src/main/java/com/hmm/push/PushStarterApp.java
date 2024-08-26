package com.mashibing.push;/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/29 21:15
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author hemengmeng
 * @version 1.0
 * Create by 2024/7/29 21:15
 */

@SpringBootApplication
@EnableDiscoveryClient
public class PushStarterApp {

    public static void main(String[] args) {
        SpringApplication.run(PushStarterApp.class,args);
    }

}