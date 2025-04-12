package com.zqq.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class EyeGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(EyeGatewayApplication.class,args);
    }
}
