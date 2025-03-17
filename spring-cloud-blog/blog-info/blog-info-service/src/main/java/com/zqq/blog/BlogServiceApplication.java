package com.zqq.blog;

import com.zqq.common.utils.Redis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BlogServiceApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(BlogServiceApplication.class, args);
		Redis testRedis = (Redis)run.getBean("testRedis");
		System.out.println(testRedis);
	}

}
