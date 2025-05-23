package com.zqq.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GatewayServiceApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(GatewayServiceApplication.class, args);
    }
}
