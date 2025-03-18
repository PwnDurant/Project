package com.zqq.common.utils;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class JsonUtil<T> {

    /**
     * 字符串转对象
     */
    public static <T> T parseJson(String json, Class<T> clazz){
        if (!StringUtils.hasLength(json) || clazz==null){
            return null;
        }
        try {
            return JSON.parseObject(json, clazz);
        }catch (Exception e){
            log.error("JsonUtil parseJson error, e:", e);
            //TODO
            //异常时处理逻辑, 看需求决定是否抛出异常
            return null;
        }

    }

    /**
     * 对象转字符串
     */
    public static String toJson(Object o){
        try{
            return o==null ? null: JSON.toJSONString(o);
        }catch (Exception e){
            log.error("JsonUtil toJson error, e:", e);
            //TODO
            //异常时处理逻辑, 看需求决定是否抛出异常
            return null;
        }
    }

}
