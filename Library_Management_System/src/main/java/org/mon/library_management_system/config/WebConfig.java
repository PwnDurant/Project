package org.mon.library_management_system.config;

import org.mon.library_management_system.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        如果用new LoginInterceptor的话那么里面的@Autowired就不会生效，因为这里创建的是普通对象，Spring无法对其进行注入
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/book/**") //对哪些路径生效
                .excludePathPatterns("/user/login"); //排除哪些路径


    }
}
