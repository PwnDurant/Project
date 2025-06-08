package com.zqq.blog_improve;

import com.alibaba.fastjson2.TypeReference;
import com.zqq.blog_improve.common.pojo.domain.UserInfo;
import com.zqq.blog_improve.common.utils.RedisService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
public class testRedis {

    @Resource(name = "redisService")
    private RedisService redisService;

    @Test
    public void testWrite(){

        Map<String, List<UserInfo>> userMap = new HashMap<>();
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setUserName("test1");
        UserInfo userInfo2 = new UserInfo();
        userInfo2.setUserName("test2");
        userMap.put("admin", Arrays.asList(userInfo1));
        userMap.put("guest", Arrays.asList(userInfo2));
        redisService.setCacheObject("test",userMap);

    }

    @Test
    public void testRead(){
        Map<String, List<UserInfo>> object = redisService.getCacheObject("test", new TypeReference<Map<String, List<UserInfo>>>() {});
        System.out.println(object);
        List<UserInfo> admin = object.get("admin");
    }

    @Test
    public void testReadWithKey(){
//        System.out.println(redisService.getCacheObject("test"));
    }


}
