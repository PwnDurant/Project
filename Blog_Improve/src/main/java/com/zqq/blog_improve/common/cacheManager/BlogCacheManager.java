package com.zqq.blog_improve.common.cacheManager;

import com.zqq.blog_improve.common.pojo.domain.BlogInfo;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class BlogCacheManager {

    @Resource(name = "redisTemplateWithJsonRedisSerializer")
    private RedisTemplate redisTemplate;

    /**
     * 向 redis 中增加缓存
     * @param key key
     * @param blogInfo 博客信息
     */
    public void addCache(String key,BlogInfo blogInfo){
        redisTemplate.opsForList().leftPush(key,blogInfo);
    }


    /**
     * 向 redis 中杀出缓存
     * @param key key
     */
    public <T> void deleteCache(String key,T value){
        redisTemplate.opsForList().remove(key,1L,value);
    }

}
