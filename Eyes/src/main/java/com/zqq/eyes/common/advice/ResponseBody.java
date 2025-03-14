package com.zqq.eyes.common.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zqq.eyes.common.pojo.WebResult;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/**
 * 统一格式返回（AOP）
 */
@ControllerAdvice
public class ResponseBody implements ResponseBodyAdvice {

    @Autowired
    private ObjectMapper objectMapper;

    public ResponseBody(){
        super();
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        if(body instanceof WebResult<?>) return body;

        if(body instanceof String) return objectMapper.writeValueAsString(WebResult.success(body));

        return WebResult.success(body);

    }
}
