package com.zqq.forum.interceptor;


import com.zqq.forum.common.AppConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登入拦截器
 */
@Configuration
public class LoginInterceptor implements HandlerInterceptor {

    @Value("${zqq-forum.login.url}")
    private String defaultUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session= request.getSession(false);

        if(session!=null&&session.getAttribute(AppConfig.USER_SESSION)!=null){
            return true;
        }

        if(!defaultUrl.startsWith("/")){
            defaultUrl="/"+defaultUrl;
        }
        response.sendRedirect(defaultUrl);
        return false;
    }
}
