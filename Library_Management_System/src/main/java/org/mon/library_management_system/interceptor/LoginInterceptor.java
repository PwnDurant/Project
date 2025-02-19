package org.mon.library_management_system.interceptor;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.mon.library_management_system.constant.Constants;
import org.mon.library_management_system.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

//    用于JSON和Java对象的转换
    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        true --放行，false --拦截
        log.info("登入拦截......");

//        这里的false和true是有说法的
//        true:获取当前会话，如果没有的话就会创建一个新的会话session
//        false:获取当前会话，如果没有会话的话就返回null
        HttpSession session=request.getSession(false);
        if(session==null||session.getAttribute(Constants.SESSION_USER_KEY)==null||
        !StringUtils.hasLength((String)session.getAttribute(Constants.SESSION_USER_KEY))){
//            用户未登入
            log.error("用户未登入，进行拦截");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            Result result=Result.unlogin();

//            将JSON字节流写入HTTP响应体
            response.getOutputStream().write(objectMapper.writeValueAsString(result).getBytes());
            response.setContentType("application/json;charset=utf-8");
//            重定向
//            response.sendRedirect("login.html");
            return false;
        }
        log.info("用户校验通过..");
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
//    }
}
