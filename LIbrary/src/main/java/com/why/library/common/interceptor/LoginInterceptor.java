package com.why.library.common.interceptor;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.why.library.common.constant.Constants;
import com.why.library.dao.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("登入拦截......");
        HttpSession session=request.getSession(false);
        if(session==null||session.getAttribute(Constants.SESSION_USER_KEY)==null||
        !StringUtils.hasLength((String)session.getAttribute(Constants.SESSION_USER_KEY))){
            log.error("用户未登入，进行拦截");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            Result result=Result.unlogin();

            response.getOutputStream().write(objectMapper.writeValueAsString(result).getBytes());
            response.setContentType("application/json;charset=utf-8");
            return false;
        }
        log.info("用户校验通过..");
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
