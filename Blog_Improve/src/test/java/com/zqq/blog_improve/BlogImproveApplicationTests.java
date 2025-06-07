package com.zqq.blog_improve;

import cn.hutool.core.lang.UUID;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class BlogImproveApplicationTests {

    @Test
    void contextLoads() {

        System.out.println(UUID.randomUUID().toString());

    }

    @Test
    void testPassword(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //$2a$10$smtJ9MZVlJXksYc2KiGMIuuvHetl7XRKkZTRATn3ZeCekZDv3tgP.
        //$2a$10$zfueD09WdE2JIoHr8EM2k.RKoWPxjuHfS749VnEzvvEV2Gj9GiR1y
        //$2a$10$jG8MOVHQVJALQkeGrNDWN.8yni8QJAitMcMQDQXVo8tXPTgpdd1nK
        System.out.println(encoder.encode("123456"));
    }

    @Test
    void testMatch(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.matches("123456", "$2a$10$smtJ9MZVlJXksYc2KiGMIuuvHetl7XRKkZTRATn3ZeCekZDv3tgP."));
        System.out.println(encoder.matches("123456", "$2a$10$zfueD09WdE2JIoHr8EM2k.RKoWPxjuHfS749VnEzvvEV2Gj9GiR1y"));
        System.out.println(encoder.matches("123456", "$2a$10$jG8MOVHQVJALQkeGrNDWN.8yni8QJAitMcMQDQXVo8tXPTgpdd1nK"));
    }

    @Test
    void testFast(){
        System.out.println(UUID.fastUUID().toString());
    }

}
