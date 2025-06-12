package com.zqq.cookieshop.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * 自定义序列化器
 * @param <T>
 */
public class JsonRedisSerializer<T> implements RedisSerializer<T> {

    private final Type type;

    public JsonRedisSerializer(Type type) {
        this.type = type;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        try {
            if (t == null) {
                return new byte[0];
            }
            return JSON.toJSONString(t).getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new SerializationException("Redis JSON serialization error", e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        try {
            if (bytes == null || bytes.length == 0) {
                return null;
            }
            String str = new String(bytes, StandardCharsets.UTF_8);
            return JSON.parseObject(str, type);
        } catch (Exception e) {
            throw new SerializationException("Redis JSON deserialization error", e);
        }
    }

    public static <T> JsonRedisSerializer<T> of(Class<T> clazz) {
        return new JsonRedisSerializer<>(clazz);
    }

    public static <T> JsonRedisSerializer<T> of(TypeReference<T> typeReference) {
        return new JsonRedisSerializer<>(typeReference.getType());
    }
}
