package com.zqq.system;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zqq.**.mapper")
public class EyeSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(EyeSystemApplication.class,args);
    }
}
