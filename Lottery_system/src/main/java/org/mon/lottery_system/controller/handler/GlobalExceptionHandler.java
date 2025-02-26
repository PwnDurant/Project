package org.mon.lottery_system.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.mon.lottery_system.common.errorcode.GlobalErrorCodeConstants;
import org.mon.lottery_system.common.exception.ControllerException;
import org.mon.lottery_system.common.exception.ServiceException;
import org.mon.lottery_system.common.pojo.CommonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice  //可以捕获全局抛出的异常
public class GlobalExceptionHandler {


    @ExceptionHandler(value = ServiceException.class)
    public CommonResult<?> serviceException(ServiceException e){
//        打错误日志(不需要用占位符去替代)
        log.error("serviceException:",e);
//        构造错误返回/结果
        return CommonResult.error(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(),e.getMessage());
    }


    @ExceptionHandler(value = ControllerException.class)
    public CommonResult<?> controllerException(ControllerException e){
//        打错误日志(不需要用占位符去替代)
        log.error("controllerException:",e);
//        构造错误返回/结果
        return CommonResult.error(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(),e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public CommonResult<?> Exception(Exception e){
//        打错误日志(不需要用占位符去替代)
        log.error("Exception:",e);
//        构造错误返回/结果
        return CommonResult.error(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(),e.getMessage());
    }


}
