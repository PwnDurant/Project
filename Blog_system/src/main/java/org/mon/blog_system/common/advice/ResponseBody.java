package org.mon.blog_system.common.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.mon.blog_system.common.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

//统一返回的注解
@ControllerAdvice
public class ResponseBody implements ResponseBodyAdvice {

    @Autowired
    ObjectMapper objectMapper;

    public ResponseBody() {
        super();
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

//    在整个方法上加上一个try catch
    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
//        String
//        Result
        if(body instanceof Result<?>) return body;

//        对String 类型做特殊处理，通常情况下我们不返回String类型
        if(body instanceof String) return objectMapper.writeValueAsString(Result.ok(body));

        return Result.ok(body);
    }
}
