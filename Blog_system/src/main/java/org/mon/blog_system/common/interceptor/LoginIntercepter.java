package org.mon.blog_system.common.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.mon.blog_system.common.utils.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;


@Slf4j
@Component
public class LoginIntercepter implements HandlerInterceptor {

//    在目标方法执行前进行校验
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        校验token是否正确
//        获取token
        String token = request.getHeader("user_header_token");
        log.info("获取token:{}",token);
//        校验token的合法性
        if(!StringUtils.hasLength(token)){
            response.setStatus(401);
            return false;
        }
//        token存在的话进行解析，异常为null
        Claims claims = JwtUtil.parseToken(token);
//        token不合法
        if(claims==null){
            response.setStatus(401);
            return false;
        }

//        也可以再做的细一点 ，比如校验id和name是否匹配，是否存在
//        claims.get("name")

        return true;

    }
}
