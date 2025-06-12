package com.zqq.cookieshop.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@AutoConfigureBefore(RedisAutoConfiguration.class) //解决冲突
//@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

//    @Autowired
//    private RedisProperties redisProperties;
//
//    public void test(){
//        redisProperties.getPort();
//    }

    @Bean(name = "redisTemplateWithJsonRedisSerializer")
    public RedisTemplate<String,Object> redisTemplateWithJsonRedisSerializer(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        设置 key 和 hashKey 存储的时候用字符串来序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
//        设置 value 用 自定义序列化器序列化 序列化(实现了 RedisSerializer 接口)
        JsonRedisSerializer<Object> jsonRedisSerializer = new JsonRedisSerializer<>(Object.class);
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);
        redisTemplate.afterPropertiesSet();  //触发 RedisTemplate 的初始化逻辑，完成各个序列化器、连接工厂等组件的配置
        return redisTemplate;
    }

    public RedisTemplate<String,Object> redisTemplateWithJackson2JsonRedisSerializer(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        设置 key 和 hashKey 存储的时候用字符串来序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
//        设置 value 用 JSON 序列化（Jackson）
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }



}
