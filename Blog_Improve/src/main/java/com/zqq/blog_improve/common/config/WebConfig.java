package com.zqq.blog_improve.common.config;

import com.zqq.blog_improve.common.interceptor.LoginInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注册拦截器
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource(name = "loginInterceptor")
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/user/**","/blog/**")
                .excludePathPatterns("/user/login","/user/register");
    }
}
