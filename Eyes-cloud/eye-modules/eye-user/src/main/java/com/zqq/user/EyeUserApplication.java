package com.zqq.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zqq.**.mapper")
public class EyeUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(EyeUserApplication.class,args);
    }
}
