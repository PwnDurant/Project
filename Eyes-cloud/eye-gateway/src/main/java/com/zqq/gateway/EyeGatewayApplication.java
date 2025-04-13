package com.zqq.gateway;

import com.zqq.common.core.enums.ResultCode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class EyeGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(EyeGatewayApplication.class,args);
//        System.out.println(ResultCode.class.getName()+"-------------------------------------");
    }
}
