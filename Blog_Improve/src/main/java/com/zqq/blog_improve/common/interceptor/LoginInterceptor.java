package com.zqq.blog_improve.common.interceptor;


import com.zqq.blog_improve.common.constant.Constants;
import com.zqq.blog_improve.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 自定义登入拦截器
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 在目标方法执行前进行校验
     * @param request 请求
     * @param response 响应
     * @param handler 处理
     * @return 结果
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        先拿到 token 并检验 token 是否正确
        String token = request.getHeader(Constants.USER_TOKEN);
        log.info("获取到 token：{}",token);
//        开始解析 token 并校验 token 的合法性 -- 有什么错误会在方法里面报出
        JwtUtil.parseToken(token);
        return true;
    }
}
