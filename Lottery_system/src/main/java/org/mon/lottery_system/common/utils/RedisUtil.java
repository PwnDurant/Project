package org.mon.lottery_system.common.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class RedisUtil {

    /**
     * StringRedisTemplate: 直接存放的就是String ，方便读取
     * RedisTemplate: 先将被存储的数据转换为字节数组（不方便读取），再存储到Redis中，读取按照字节数组读取
     * 项目背景：存放的都是String类型，读取也是String
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 设置值
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key,String value){
        try{
            stringRedisTemplate.opsForValue().set(key,value);
            return true;
        }catch (Exception e){
            log.error("RedisUtil error,set({},{})",key,value,e);
            return false;
        }
    }

    /**
     * 设置过期时间
     * @param key
     * @param value
     * @param time  单位为s
     * @return
     */
    public boolean set(String key,String value,Long time){
        try{
            stringRedisTemplate.opsForValue().set(key,value,time, TimeUnit.SECONDS);
            return true;
        }catch (Exception e){
            log.error("RedisUtil error,set({},{},{})",key,value,time,e);
            return false;
        }
    }

    /**
     * 获取值
     * @param key
     * @return
     */
    public String get(String key){
        try{
            return StringUtils.hasText(key)?
                    stringRedisTemplate.opsForValue().get(key)
                    :null;
        }catch (Exception e){
            log.error("RedisUtil error,get({})",key,e);
            return null;
        }
    }

    /**
     * 删除值
     * @param key
     * @return
     */
    public Boolean del(String... key){
        try{
            if(null!=key&&key.length>0) {
                if(key.length==1) stringRedisTemplate.delete(key[0]);
                else {
                    stringRedisTemplate.delete(
                            (Collection<String>)CollectionUtils.arrayToList(key)
                    );
                }
            }
            return true;
        }catch (Exception e){
            log.error("RedisUtil error,del({})",key,e);
            return false;
        }
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public Boolean hasKey(String key){
        try{
            return StringUtils.hasText(key)
                    ?stringRedisTemplate.hasKey(key)
                    :false;
        }catch (Exception e){
            log.error("RedisUtil error,hasKey({})",key,e);
            return false;
        }
    }

}
