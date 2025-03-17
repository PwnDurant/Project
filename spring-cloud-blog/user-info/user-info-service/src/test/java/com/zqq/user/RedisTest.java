package com.zqq.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void test(){
        redisTemplate.opsForValue().set("hello","spring");
        System.out.println(redisTemplate.opsForValue().get("hello"));

    }


}
