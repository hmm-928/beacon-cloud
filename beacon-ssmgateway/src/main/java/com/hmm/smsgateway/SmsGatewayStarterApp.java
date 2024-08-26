package com.hmm.smsgateway;

import cn.hippo4j.core.enable.EnableDynamicThreadPool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
//hippo4j多线程
@EnableDynamicThreadPool
@EnableFeignClients
public class SmsGatewayStarterApp {

    public static void main(String[] args) {
        SpringApplication.run(SmsGatewayStarterApp.class, args);
    }

}