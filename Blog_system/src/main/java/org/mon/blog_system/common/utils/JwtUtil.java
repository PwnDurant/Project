package org.mon.blog_system.common.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class JwtUtil {
    private static final String secret="rARtjWX851sjqOeZ+bGq04yisJdBNAJEjG4x1XQcXMc=";
    private static final Key key= Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

    //    生成token

//    设置24小时过期
    private static final long expiration=24*60*60*1000;
    public static String genToken(Map<String,Object> map){
//        签名信息 要大于256位

        return Jwts.builder()
                .setClaims(map)
                .signWith(key)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .compact();
    }

    //    校验token
    public static Claims parseToken(String token){

        if(!StringUtils.hasLength(token)) return null;

//        Jwt的解析器
        JwtParser build = Jwts.parserBuilder().setSigningKey(key).build();
        Claims body=null;
        try{
             body = build.parseClaimsJws(token).getBody();
             return body;
        }catch (ExpiredJwtException e){
            log.error("token过期，token:{}",token);
        }catch (SignatureException e){
            log.error("token签名错误,token:{}",token);
        }catch (Exception e){
            log.error("token解析失败,token:{}",token);
        }
        return null;
    }
}
