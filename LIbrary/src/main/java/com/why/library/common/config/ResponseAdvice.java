package com.why.library.common.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.why.library.dao.Result;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice {

//   用于JSON和Java对象之间的转换
    @Autowired
    private ObjectMapper objectMapper;


//    true表示支持所有返回类型和转换器
//    returnType:表示SpringMVC处理的方法返回值类型
//    converterType:表示处理返回值的转换器类型
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @SneakyThrows //自动处理受检异常，不需要手动try catch or throws
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if(body instanceof Result<?>) return body;
        if(body instanceof String) return objectMapper.writeValueAsString(Result.success(body));
        return Result.success(body);
    }
}
