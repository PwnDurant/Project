package com.zqq.cookieshop.common.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zqq.cookieshop.common.constants.Constants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisService {

    @Resource(name = "redisTemplateWithJsonRedisSerializer")
    private RedisTemplate redisTemplate;

    /**
     * 判断 redis 中传入的 key 是否存在
     * @param key 传入的 key
     * @return 返回是否存在
     */
    public boolean Key(final String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置 key 的过期时间
     * 参数加上 final 是为了防止在 方法内部对数值进行修改
     * @param key 需要设置时间的 key
     * @param timeout 过期时间大小
     * @param timeUnit 时间单位
     * @return 是否设置成功
     */
    public boolean expire(String key,final long timeout,final TimeUnit timeUnit){
        return redisTemplate.expire(key,timeout,timeUnit);
    }

    /**
     * 获取指定 key 的剩余时间
     * @param key 指定的 key
     * @param timeUnit 返回时间的单位
     * @return 根据单位返回的时间大小
     */
    public Long getExpire(String key,final TimeUnit timeUnit){
        return redisTemplate.getExpire(key,timeUnit);
    }

    /**
     * 删除指定 key
     * @param key 指定 key
     * @return 是否删除成功
     */
    public boolean deleteKey(String key){
        return redisTemplate.delete(key);
    }

    /**
     * 缓存指定的键值对
     * @param key 需要缓存的 key
     * @param data 需要缓存的数据
     * @param <T> 代表可以缓存的数据是任何类型
     */
    public <T> void setCacheObject(String key,final T data,final long timeout,final TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key,data,timeout,timeUnit);
    }
    public <T> void setCacheObject(String key,final T data){
        redisTemplate.opsForValue().set(key,data);
    }

    /**
     * 获得缓存的基本对象
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key, Class<T> clazz) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        T t = operation.get(key);
        if (t instanceof String) {
            return t;
        }
        return JSON.parseObject(String.valueOf(t), clazz);
    }
    public <T> T getCacheObject(final String key, TypeReference<T> reference) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        T t = operation.get(key);
        if (t instanceof String) {
            return t;
        }
        return JSON.parseObject(String.valueOf(t), reference);
    }

    /**
     * 操作 list 数据结构
     */
    public <T> Long leftPush(String key,T data){
        return redisTemplate.opsForList().leftPush(key,data);
    }


    public <T> T getCacheList(String key, com.fasterxml.jackson.core.type.TypeReference<T> typeReference) throws JsonProcessingException {
        Long size = redisTemplate.opsForList().size(key);
        List range = redisTemplate.opsForList().range(key, 0, size);
        return JsonUtils.fromJson(range,typeReference);
    }


    /**
     * 批量插入缓存
     */
    public <T> Long leftPushAll(Collection<T> collection) {
        return redisTemplate.opsForList().leftPushAll(Constants.BLOG_LIST,collection);
    }
}
