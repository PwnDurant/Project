package com.zqq.common.security.service;

import cn.hutool.core.lang.UUID;
import com.zqq.common.core.constants.CacheConstants;
import com.zqq.common.core.constants.JwtConstants;
import com.zqq.common.core.domain.LoginUser;
import com.zqq.common.core.utils.JwtUtils;
import com.zqq.common.redis.service.RedisService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 操作用户登入token的方法
 */
@Slf4j
@Service
public class TokenService {

    @Autowired
    private RedisService redisService;


    /**
     * 根据用户信息创建token
     * @param userId 用户id
     * @param secret 密钥（签名verify)
     * @param nickName 用户昵称
     * @return 返回生成的token
     */
    public String createToken(Long userId,String secret,String nickName){
//        token里包含header，payload，verify,其中payload就是主要信息
//        这里payload中放 用户id（用户后续通过token获取用户对应相关信息） 和 用户key（用户唯一标识）
        Map<String,Object> claims=new HashMap<>();
//        生成用户唯一标识并放入到载荷中，之所以放userId和userKey是为了防止同一个用户同时登入不同设备
//        这样如果只用userId的话有唯一性，会覆盖之前的登入状态
        String userKey = UUID.fastUUID().toString();
        claims.put(JwtConstants.LOGIN_USER_ID,userId);
        claims.put(JwtConstants.LOGIN_USER_KEY,userKey);
//        根据载荷和密钥生成token
        String token = JwtUtils.createToken(claims, secret);
//         生成好的token要放入到redis中，方便对过期时间进行控制
        String key=getTokenKsy(userKey);
        LoginUser loginUser=new LoginUser();
        loginUser.setName(nickName);
//        在缓存里设置720分钟的过期时间
        redisService.setCacheObject(key,loginUser,CacheConstants.EXP, TimeUnit.MINUTES);
        return token;
    }

    /**
     * 延长token
     * 在身份认证通过并且在请求到达controller之前进行判别延长，延长token的有效时间就是延长redis中的信息        需要操作redis   token-> 唯一标识
     * @param token token
     */
    public void extendToken(String token,String secret){
//        先拿到对应的用户key，再进行判断
        String userKey = getUserKey(token, secret);
        if(userKey==null){
            return ;
        }
        String tokenKey=getTokenKsy(userKey);
//        剩余180min分钟延长
        Long expire = redisService.getExpire(tokenKey, TimeUnit.MINUTES);
        if(expire!=null&&expire<CacheConstants.REFRESH_TIME){
            redisService.expire(tokenKey,CacheConstants.EXP,TimeUnit.MINUTES);
        }
    }

    public String getTokenKsy(String userKey){
        return CacheConstants.LOGIN_TOKEN_KEY+userKey;
    }

    public LoginUser getLoginUser(String token,String secret) {
//        先解析token，从token中获取唯一标识
        String userKey = getUserKey(token, secret);
        if(userKey==null) return null;
        return  redisService.getCacheObject(getTokenKsy(userKey), LoginUser.class);

    }

    /**
     * 获取token令牌中的用户Id
     * @param claims 直接把token中的载荷解析出来传给方法，直接获取userId
     * @return
     */
    public Long getUserId(Claims claims) {
        if(claims==null) return null;
        return Long.valueOf(JwtUtils.getUserId(claims));  //获取jwt中的key
    }


    /**
     * 获取token中用户的唯一标识
     * @param claims 再提供一种直接通过传入载荷获取userKey的方法
     * @return
     */
    public String getUserKey(Claims claims){
        if(claims==null) return null;
        return JwtUtils.getUserKey(claims);  //获取jwt中的key
    }

    private String getUserKey(String token, String secret) {
        Claims claims = getClaims(token, secret);
        if (claims == null) return null;
        return JwtUtils.getUserKey(claims);  //获取jwt中的key
    }

    /**
     * 从token中获取载荷
     * @param token 传入的token信息
     * @param secret 签名
     * @return 返回claims载荷
     */
    public Claims getClaims(String token, String secret) {
        Claims claims;
        try {
            claims = JwtUtils.parseToken(token, secret); //获取令牌中信息  解析payload中信息  存储着用户唯一标识信息
            if (claims == null) {
                log.error("解析token：{}, 出现异常", token);
                return null;
            }
        } catch (Exception e) {
            log.error("解析token：{}, 出现异常", token, e);
            return null;
        }
        return claims;
    }

    public boolean deleteLoginUser(String token, String secret) {
        String userKey = getUserKey(token, secret);
        if(userKey==null) return false;
        return redisService.deleteObject(getTokenKsy(userKey));
    }


//    刷新用户信息缓存
    public void refreshLoginUser(String nickName, String userKey) {
//        根据用户Key拿到TokenKey
        String tokenKey=getTokenKey(userKey);
        LoginUser loginUser = redisService.getCacheObject(tokenKey, LoginUser.class);
        loginUser.setName(nickName);
        redisService.setCacheObject(tokenKey,loginUser);
    }

    public String getTokenKey(String userKey){
        return CacheConstants.LOGIN_TOKEN_KEY+userKey;
    }
}
