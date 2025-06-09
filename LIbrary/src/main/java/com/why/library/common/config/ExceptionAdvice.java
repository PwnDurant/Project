package com.why.library.common.config;


import com.why.library.dao.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;


//统一异常处理
@Slf4j
@ControllerAdvice
@ResponseBody
public class ExceptionAdvice {


    @ExceptionHandler
    public Object handler(Exception e){
        log.error("发生异常,e:{}",e);
        return Result.fail();
    }

    @ExceptionHandler
    public Object handler (ArrayIndexOutOfBoundsException e){
        log.error("发生异常，e:{}",e.getMessage());
        return Result.fail("发生数组越界");
    }

    @ExceptionHandler
    public Object handler (NullPointerException e){
        log.error("发生异常，e:{}",e);
        return Result.fail("发生空指针异常");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object handler(NoResourceFoundException e){
        log.error("发生异常,e:{},path:{}",e.getDetailMessageCode(),e.getResourcePath());
        return Result.fail("发生没有找到资源异常");
    }
}
