package com.zqq.blog_improve;

import cn.hutool.core.lang.UUID;
import com.zqq.blog_improve.common.utils.SecurityUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;


@SpringBootTest
class BlogImproveApplicationTests {

    @Test
    void contextLoads() {

        System.out.println(UUID.randomUUID().toString());

    }

    @Test
    void testPassword(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //$2a$10$smtJ9MZVlJXksYc2KiGMIuuvHetl7XRKkZTRATn3ZeCekZDv3tgP.
        //$2a$10$zfueD09WdE2JIoHr8EM2k.RKoWPxjuHfS749VnEzvvEV2Gj9GiR1y
        //$2a$10$jG8MOVHQVJALQkeGrNDWN.8yni8QJAitMcMQDQXVo8tXPTgpdd1nK
        System.out.println(encoder.encode("123456"));
    }

    @Test
    void testMatch(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.matches("123456", "$2a$10$smtJ9MZVlJXksYc2KiGMIuuvHetl7XRKkZTRATn3ZeCekZDv3tgP."));
        System.out.println(encoder.matches("123456", "$2a$10$zfueD09WdE2JIoHr8EM2k.RKoWPxjuHfS749VnEzvvEV2Gj9GiR1y"));
        System.out.println(encoder.matches("123456", "$2a$10$jG8MOVHQVJALQkeGrNDWN.8yni8QJAitMcMQDQXVo8tXPTgpdd1nK"));
    }

    @Test
    void testFast(){
        System.out.println(UUID.fastUUID().toString());
    }

    @Test
    void createSecret(){
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String encode = Encoders.BASE64.encode(secretKey.getEncoded());
        System.out.println(encode);
    }

    @Test
    void testJwt(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("userKey","test");
        map.put("userId","test2");
        String secret="rARtjWX851sjqOeZ+bGq04yisJdBNAJEjG4x1XQcXMc=";
        Key key= Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        long expiration=10000;
        String compact = Jwts.builder()
                .setClaims(map)   // 设置载荷
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
        System.out.println(compact);
        System.out.println(Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(compact).getBody());
    }

    @Test
    public void testExp(){
        String secret="rARtjWX851sjqOeZ+bGq04yisJdBNAJEjG4x1XQcXMc=";
        Key key= Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        String compact="eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJ0ZXN0MiIsInVzZXJLZXkiOiJ0ZXN0IiwiaWF0IjoxNzQ5MzYxNzgyLCJleHAiOjE3NDkzNjE3OTJ9.CcIKgzIlwPvx43tDDr1vt3-QkeaK2KTspb7wmCUy1qQ";
//        过期就解析不了了
        System.out.println(Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(compact).getBody());
    }

    @Test
    public void testTraditional(){
        System.out.println(SecurityUtil.encrypt_traditional("123456"));
    }



}
