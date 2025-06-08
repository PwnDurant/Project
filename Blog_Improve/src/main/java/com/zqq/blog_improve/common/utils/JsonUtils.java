package com.zqq.blog_improve.common.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;

/**
 * 自定义序列化工具
 */
public class JsonUtils {

    public static <T> T fromJson(String json,Class<T> clazz){
        return JSON.parseObject(json,clazz);
    }

    public static <T> T fromJson(String json, TypeReference<T> reference){
        return JSON.parseObject(json,reference);
    }

    public static String toString(Object obj){
        return JSON.toJSONString(obj);
    }

}
