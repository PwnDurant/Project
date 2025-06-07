package com.zqq.blog_improve.common.utils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class TokenService {

    @Resource(name = "jwtUtil")
    private JwtUtil jwtUtil;
    @Resource(name = "redisService")
    private RedisService redisService;

    /**
     * 根据传入的载荷信息生成 token
     * 并且放入缓存中设定过期时间
     * @param map 要放入 jwt 中的载荷数据
     * @return 返回 token 并把 jwt 放入缓存中 并设置过期时间
     * 规定 redis 格式: key:value ==> loginToken：随机数  ：jwtToken
     */
    public String createToken(Map<String,Object> map){
        String jwt = jwtUtil.createJWT(map);
//        redisService.setCacheObject();
        return null;
    }

}
