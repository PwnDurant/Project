package com.zqq.forum.exception;


import com.zqq.forum.common.AppResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ApplicationException.class)
    public AppResult applicationException(ApplicationException e){
//        打印异常信息
        log.error(e.getMessage());
        if(e.getErrorResult()!=null){
            return e.getErrorResult();
        }
    }
}
