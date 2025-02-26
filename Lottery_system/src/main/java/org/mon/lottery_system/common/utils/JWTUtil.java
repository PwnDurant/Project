package org.mon.lottery_system.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: yibo
 */
public class JWTUtil {
    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);
    /**
     * 密钥：Base64编码的密钥
     */
    private static final String SECRET = "mXsryY84BPeVtSuYkdhdwwAwi2KOOx8lVby0KLJ1Ljg=";
    /**
     * 生成安全密钥：将一个Base64编码的密钥解码并创建一个HMAC SHA密钥。
     */
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            Decoders.BASE64.decode(SECRET));
    /**
     * 过期时间(单位: 毫秒)
     */
    private static final long EXPIRATION = 60*60*1000;

    /**
     * 生成密钥
     *
     * @param claim  {"id": 12, "name":"张山"}
     * @return
     */
    public static String genJwt(Map<String, Object> claim){
        //签名算法
        String jwt = Jwts.builder()
                .setClaims(claim)             // 自定义内容(载荷)
                .setIssuedAt(new Date())      // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // 设置过期时间
                .signWith(SECRET_KEY)         // 签名算法
                .compact();
        return jwt;
    }

    /**
     * 验证密钥
     */
    public static Claims parseJWT(String jwt){
        if (!StringUtils.hasLength(jwt)){
            return null;
        }
        // 创建解析器, 设置签名密钥
        JwtParserBuilder jwtParserBuilder = Jwts.parserBuilder().setSigningKey(SECRET_KEY);
        Claims claims = null;
        try {
            //解析token
            claims = jwtParserBuilder.build().parseClaimsJws(jwt).getBody();
        }catch (Exception e){
            // 签名验证失败
            logger.error("解析令牌错误,jwt:{}", jwt, e);
        }
        return claims;

    }

    /**
     * 从token中获取用户ID
     */
    public static Integer getUserIdFromToken(String jwtToken) {
        Claims claims = JWTUtil.parseJWT(jwtToken);
        if (claims != null) {
            Map<String, Object> userInfo = new HashMap<>(claims);
            return (Integer) userInfo.get("userId");
        }
        return null;
    }
}
