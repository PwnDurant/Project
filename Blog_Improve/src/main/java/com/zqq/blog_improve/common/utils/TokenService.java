package com.zqq.blog_improve.common.utils;

import cn.hutool.core.lang.UUID;
import com.zqq.blog_improve.common.constant.Constants;
import com.zqq.blog_improve.common.pojo.domain.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TokenService {

    @Resource(name = "redisService")
    private RedisService redisService;

    /**
     * 根据传入的载荷信息生成 token
     * 并且放入缓存中设定过期时间
     * @param userInfo   要放入 jwt 中的载荷数据
     * @return 返回 token 并把 jwt 放入缓存中 并设置过期时间
     * 规定 redis 格式: key:value ==> loginToken：随机数  ：jwtToken
     */
    public String createToken(UserInfo userInfo){
//        生成 jwt
        Claims claims = Jwts.claims();
        String userKey = UUID.fastUUID().toString();
        claims.put(Constants.LOGIN_USER_KEY, userKey);
        claims.put(Constants.LOGIN_USER_ID, userInfo.getId());
//        开始设置 redis 缓存中存放的数据
        UserInfo userInfoRedis = new UserInfo();
        userInfoRedis.setUserName(userInfo.getUserName());
        userInfoRedis.setGithubUrl(userInfo.getGithubUrl());
        String tokenKey=Constants.LOGIN_TOKEN_KEY+userKey;
        redisService.setCacheObject(tokenKey,userInfoRedis,Constants.EXPIRATION_MIN, TimeUnit.MINUTES);
//        返回 token 令牌
        return JwtUtil.createJWT(claims);
    }

    /**
     * 延长 token 的有效时间
     * @param token 需要被延长的 token
     */
    public void extendToken(String token){
//        获取 token 中用户唯一标识 userKey
        String userKey = JwtUtil.getUserKey(JwtUtil.parseToken(token));
        if(userKey==null) return ;
        String tokenKey=Constants.LOGIN_TOKEN_KEY+userKey;
//        当过期时间还剩下 180 分钟的时候延长
        Long expire = redisService.getExpire(tokenKey, TimeUnit.MINUTES);
        if(expire!=null&&expire<Constants.REFRESH_TIME){
            redisService.expire(tokenKey,Constants.EXPIRATION_MIN,TimeUnit.MINUTES);
        }
    }

}
