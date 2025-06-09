package com.zqq.blog_improve.common.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    public static <T> T fromJson(List s, com.fasterxml.jackson.core.type.TypeReference<T> typeReference ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

//        自定义 Java 8 时间模块
        JavaTimeModule javaTimeModule = new JavaTimeModule();
//        自定义反序列化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));

        objectMapper.registerModule(javaTimeModule);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String s1 = objectMapper.writeValueAsString(s);
        return objectMapper.readValue(s1,typeReference);
    }
}
