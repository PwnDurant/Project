package org.mon.blog_system.JwtUtils;


import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final String secret="rARtjWX851sjqOeZ+bGq04yisJdBNAJEjG4x1XQcXMc=";
    private static final Key key= Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

//    生成token
    @Test
    public void genToken(){
//        签名信息 要大于256位



        Map<String,Object> map=new HashMap<>();
        map.put("id",1);
        map.put("name","zhangsan");


        String compact = Jwts.builder()
                .setClaims(map)
                .signWith(key)
                .compact();
        System.out.println(compact);
    }
//    校验token
    @Test
    public void parseToken(){
        String token="eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiemhhbmdzYW4iLCJpZCI6MX0.ZXZvc3uH8Sb78U-T2fRXs4BLxUudwlveBQKGdwSHKpw";
//        Jwt的解析器
        JwtParser build = Jwts.parserBuilder().setSigningKey(key).build();
        try{
            Object body = build.parse(token).getBody();
            System.out.println(body);
        }catch (Exception e){
            System.out.println("token非法");
        }

    }

//    生成key
    @Test
    public void getKey(){
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String encode = Encoders.BASE64.encode(secretKey.getEncoded());
        System.out.println(secretKey.toString());
        System.out.println(encode);

    }
}
