package com.zqq.blog_improve;

import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zqq.blog_improve.common.constant.Constants;
import com.zqq.blog_improve.common.pojo.domain.BaseEntity;
import com.zqq.blog_improve.common.pojo.domain.BlogInfo;
import com.zqq.blog_improve.common.pojo.domain.UserInfo;
import com.zqq.blog_improve.common.utils.RedisService;
import com.zqq.blog_improve.mapper.BlogMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

@SpringBootTest
public class testRedis {

    @Autowired
    private BlogMapper blogMapper;

    @Resource(name = "redisService")
    private RedisService redisService;

    @Resource(name = "redisTemplateWithJsonRedisSerializer")
    private RedisTemplate redisTemplate;

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

    @Test
    public void testW(){
        BlogInfo blogInfo = new BlogInfo();
        blogInfo.setId(1L);
        blogInfo.setTitle("test1");
        blogInfo.setContent("test");
        redisService.setCacheObject(Constants.BLOG_LIST+blogInfo.getId(),blogInfo);
        blogInfo.setId(2L);
        blogInfo.setTitle("test2");
        blogInfo.setContent("test");
        redisService.setCacheObject(Constants.BLOG_LIST+blogInfo.getId(),blogInfo);
    }

    @Test
    public void testR(){
        BlogInfo cacheObject = redisService.getCacheObject(Constants.BLOG_LIST+1, BlogInfo.class);
        System.out.println(cacheObject);
    }

    @Test
    public void testW1(){
        BlogInfo blogInfo = new BlogInfo();
        blogInfo.setId(1L);
        blogInfo.setTitle("test1");
        blogInfo.setContent("test");
        redisTemplate.opsForList().leftPush("b:l:",blogInfo);
        blogInfo.setId(2L);
        blogInfo.setTitle("test2");
        blogInfo.setContent("test");
        redisTemplate.opsForList().leftPush("b:l:",blogInfo);
    }

    @Test
    public void testR1(){
        Long size = redisTemplate.opsForList().size("b:l");
        List<BlogInfo> o = (List<BlogInfo>)redisTemplate.opsForList().range("b:l",0,size);
        System.out.println(o);
    }

    @Test
    public void testW2(){
        System.out.println(redisTemplate.opsForList().leftPush("b:l", "test1"));
    }

    @Test
    public void testR3() throws JsonProcessingException {
        List<BlogInfo> cacheList = redisService.getCacheList("b:l:", new com.fasterxml.jackson.core.type.TypeReference<List<BlogInfo>>() {
        });
    }

    @Test
    public void testW3(){
        List<BlogInfo> blogInfoList = blogMapper.selectList(new LambdaQueryWrapper<BlogInfo>()
                .eq(BaseEntity::getDeleteFlag, 0)
                .orderByDesc(BlogInfo::getUpdateTime));
        redisService.leftPushAll(blogInfoList);
    }

    @Test
    public void testR4() throws JsonProcessingException {
        List<BlogInfo> cacheList = redisService.getCacheList(Constants.BLOG_LIST, new com.fasterxml.jackson.core.type.TypeReference<List<BlogInfo>>() {
        });
        System.out.println(cacheList);
    }


}
