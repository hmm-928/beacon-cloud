package com.mashibing.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author zjw
 * @description
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {
        "com.mashibing.api",
        "com.mashibing.common"
})
public class ApiStarterApp {

    public static void main(String[] args) {
        SpringApplication.run(ApiStarterApp.class,args);
    }
}
