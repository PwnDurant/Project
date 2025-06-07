package com.zqq.blog_improve.common.handler;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.zqq.blog_improve.common.base.R;
import com.zqq.blog_improve.common.base.ResultCode;
import com.zqq.blog_improve.common.exception.BlogException;
import com.zqq.blog_improve.common.exception.UserException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理用户业务异常
     * @param e 异常状态码
     * @param request 请求地址
     * @return 返回 R 格式地址
     */
    @ExceptionHandler(UserException.class)
    public R<?> handleUserException(UserException e, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        ResultCode resultCode = e.getResultCode();
        log.error("[用户]请求地址:{},发生用户业务异常:{}",requestURI,resultCode.getMsg());
        return R.fail(resultCode);
    }

    /**
     * 处理博客业务异常
     * @param e 异常状态码
     * @param request 请求地址
     * @return 返回 R 格式地址
     */
    @ExceptionHandler(BlogException.class)
    public R<?> handleBlogException(BlogException e, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        ResultCode resultCode = e.getResultCode();
        log.error("[博客]请求地址:{},发生用博客务异常:{}",requestURI,resultCode.getMsg());
        return R.fail(resultCode);
    }

    @ExceptionHandler(BindException.class)
    public R<Void> handleBindException(BindException e){
        log.error("参数校验异常:{}",e.getMessage());
        String message = join(e.getAllErrors(), DefaultMessageSourceResolvable::getDefaultMessage);
        return R.fail(ResultCode.FAILED_PARAMS_VALIDATE.getCode(),message);
    }

    private <E> String join(Collection<E> collection, Function<E,String> function){
        if(CollUtil.isEmpty(collection)){
            return StrUtil.EMPTY;
        }
        return collection.stream().map(function).filter(Objects::nonNull).collect(Collectors.joining(","));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<?> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.error("[系统]请求地址'{}',不支持'{}'请求",requestURI,e.getMethod());
        return R.fail(ResultCode.REQUEST_NOT_SUPPORT);
    }

    @ExceptionHandler(Exception.class)
    public R<?> handleException(Exception e,HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.error("[系统]请求地址:{},发生异常:",requestURI,e);
        return R.fail(ResultCode.FAILED);
    }

}
