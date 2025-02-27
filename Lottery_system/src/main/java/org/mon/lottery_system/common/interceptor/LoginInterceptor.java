package org.mon.lottery_system.common.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.mon.lottery_system.common.utils.JWTUtil;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * 登入拦截器
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {


    /**
     * 预处理，业务请求之前调用
     * @param httpServletRequest
     * @param response
     * @param object
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object object){
//        获取请求头
        String token = request.getHeader("user_token");
        log.info("获取token:{}",token);
        log.info("获取路径:{}",request.getRequestURL());
//        令牌解析
        Claims claims = JWTUtil.parseJWT(token);
        if(null==claims){
            log.error("解析令牌失败");
            response.setStatus(401);
            return false;
        }
        log.info("解析成功");
        return true;

    }
}
