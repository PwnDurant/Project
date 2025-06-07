package com.zqq.blog_improve.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 判断 redis 中传入的 key 是否存在
     * @param key 传入的 key
     * @return 返回是否存在
     */
    public boolean hasKey( final String key){
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
    public boolean expire(final String key,final Long timeout,final TimeUnit timeUnit){
        return redisTemplate.expire(key,timeout,timeUnit);
    }

    /**
     * 获取指定 key 的剩余时间
     * @param key 指定的 key
     * @param timeUnit 返回时间的单位
     * @return 根据单位返回的时间大小
     */
    public Long getExpire(final String key,final TimeUnit timeUnit){
        return redisTemplate.getExpire(key,timeUnit);
    }

    /**
     * 删除指定 key
     * @param key 指定 key
     * @return 是否删除成功
     */
    public boolean deleteKey(final String key){
        return redisTemplate.delete(key);
    }

    /**
     * 缓存指定的键值对
     * @param key 需要缓存的 key
     * @param data 需要缓存的数据
     * @param <T> 代表可以缓存的数据是任何类型
     */
    public <T> void setCacheObject(final String key,final T data){
        redisTemplate.opsForValue().set(key,data);
    }

}
