package com.wpf.roomsimple.common.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.wpf.roomsimple.common.base.R;
import com.wpf.roomsimple.common.base.ResultCode;
import com.wpf.roomsimple.common.exception.SystemException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(SystemException.class)
    public R<?> handlerException(SystemException e, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        ResultCode resultCode = e.getResultCode();
        log.error("[用户]请求地址:{},发生用户业务异常:{}",requestURI,resultCode.getMsg());
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

    @ExceptionHandler(Exception.class)
    public R<?> handleException(Exception e,HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.error("[系统]请求地址:{},发生异常:",requestURI,e);
        return R.fail(ResultCode.FAILED);
    }

}
