package com.zqq.cookieshop.common.utils;

import com.zqq.cookieshop.common.base.ResultCode;
import com.zqq.cookieshop.common.constants.Constants;
import com.zqq.cookieshop.common.exception.SystemException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

/**
 * JWT 令牌工具包
 */
@Slf4j
public class JwtUtil {
    /**
     * BASE64 密钥
     * SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
     * String encode = Encoders.BASE64.encode(secretKey.getEncoded());
     * 注意：这里是 base64 编码，但是官网需要 base64Url 解码，所以需要转换为 base64Url，规则：
     * + ==> -
     * / ==> _
     * = ==> 去除
     */
    @Value("${jwt.secret}")
    private static final String secret="rARtjWX851sjqOeZ+bGq04yisJdBNAJEjG4x1XQcXMc=";

    private static final Key key= Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

    /**
     * 根据需要放入的载荷，生成对应的 token
     * @param payload 需要放入 token 的数据
     * @return 返回生成的 token
     */
    public static String createJWT(Claims payload){
        return Jwts.builder()
                .setClaims(payload)   // 设置载荷
                .signWith(key)        // 设置签名密钥和加密算法（可分下来设置）
                .compact();
    }

    /**
     * 根据需要放入的载荷，生成对应的 token
     * @param payload 需要放入 token 的数据
     * @return 返回生成的 token
     */
    public static String createJWTWithExpiration(Claims payload){
        payload.setSubject(Constants.LOGIN_TOKEN_INFO);
        payload.setIssuedAt(new Date());   //在载荷中设置令牌颁发时间
        payload.setExpiration(new Date(System.currentTimeMillis()+Constants.EXPIRATION));  //在载荷中设置截止时间：1天
        return Jwts.builder()
                .setClaims(payload)   // 设置载荷
                .signWith(key)        // 设置签名密钥和加密算法（可分下来设置）
                .compact();
    }


    /**
     * 从令牌中获取数据并验证令牌
     * @param token 令牌
     * @return 数据
     */
    public static Claims parseToken(String token){
//        判断令牌是否为空
        if(!StringUtils.hasLength(token)) {
            log.error("token为空！");
            throw new SystemException(ResultCode.TOKEN_IS_EMPTY);
        }
//        开始解析令牌
        JwtParserBuilder parserBuilder = Jwts.parserBuilder();
        Claims body;
        try{
            body=parserBuilder.setSigningKey(key).build().parseClaimsJws(token).getBody();
            return body;
        }catch (ExpiredJwtException e){
            log.error("token 过期,token:{}",token);
            throw new SystemException(ResultCode.TOKEN_IS_EXPIRED);
        }catch (SignatureException e){
            log.error("token 签名错误,token:{}",token);
            throw new SystemException(ResultCode.TOKEN_SIGNATURE_ERROR);
        }catch (Exception e){
            log.error("token 解析失败,token:{}",token);
            throw new SystemException(ResultCode.TOKEN_PARSE_ERROR);
        }
    }

    /**
     * 获取 jwt 中用户的唯一标识:userKey
     * @param claims jwt 中的 payload 载荷部分
     * @return 返回 userKey
     */
    public static String getUserKey(Claims claims){
        return claims.get(Constants.LOGIN_USER_KEY, String.class);
    }

    /**
     * 获取 jwt 中用户的唯一标识:userId
     * @param claims jwt 中的 payload 载荷部分
     * @return 返回 userId
     */
    public static String getUserId(Claims claims){
        return claims.get(Constants.LOGIN_USER_ID, String.class);
    }

}
