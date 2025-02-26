package org.mon.lottery_system;


import org.junit.jupiter.api.Test;

import org.mon.lottery_system.common.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTest {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Test
    void redisTest(){
        stringRedisTemplate.opsForValue().set("key1","value1");
        String s = stringRedisTemplate.opsForValue().get("key1");
        System.out.println(s);
    }

    @Test
    void redisUtil(){
        redisUtil.set("key2","value2");
        redisUtil.set("key3","value2",60L);
        System.out.println("has key2:"+redisUtil.hasKey("key2"));
        System.out.println("has key3:"+redisUtil.hasKey("key3"));
        System.out.println("key2:"+redisUtil.get("key2"));
        System.out.println("key3:"+redisUtil.get("key3"));
        redisUtil.del("key2");
        System.out.println("has key2:"+redisUtil.hasKey("key2"));
        System.out.println("has key3:"+redisUtil.hasKey("key3"));
    }

}
