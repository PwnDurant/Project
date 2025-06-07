package com.zqq.blog_improve.common.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Map;

/**
 * JWT 令牌工具包
 */
@Service
@Slf4j
public class JwtUtil {
    /**
     * BASE64 密钥
     * SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
     * String encode = Encoders.BASE64.encode(secretKey.getEncoded());
     */
    private static final String secret="rARtjWX851sjqOeZ+bGq04yisJdBNAJEjG4x1XQcXMc=";

    private static final Key key= Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

    /**
     * 根据需要放入的载荷，生成对应的 token
     * @param payload 需要放入 token 的数据
     * @return 返回生成的 token
     */
    public static String createJWT(Map<String,Object> payload){
        return Jwts.builder()
                .setClaims(payload)   // 设置载荷
                .signWith(key)        // 设置签名密钥和加密算法（可分下来设置）
                .compact();
    }


}
