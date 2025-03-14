package com.zqq.eyes.common.advice;

import com.zqq.eyes.common.code.ResultCode;
import com.zqq.eyes.common.exception.ApplicationException;
import com.zqq.eyes.common.pojo.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ResponseBody
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public WebResult applicationException(ApplicationException e){
        log.error(e.getMessage());
        if(e.getErrorResult()!=null) return e.getErrorResult();
        if(e.getMessage()==null|| e.getMessage().isEmpty())
            return WebResult.failed(ResultCode.SERVICE_ERROR);
        return WebResult.failed(e.getMessage());

    }

    @ExceptionHandler(Exception.class)
    public WebResult exceptionHandler(Exception e){
        log.error(e.getMessage());
        if(e.getMessage()==null||e.getMessage().equals("")){
            return WebResult.failed(ResultCode.SERVICE_ERROR);
        }
        return WebResult.failed(e.getMessage());

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public WebResult ResourceHandler(NoResourceFoundException e){
        log.error("访问资源不存在,{}",e.getResourcePath());
        return WebResult.failed(ResultCode.NO_RESOURCE);
    }

}
