package com.zqq.eyes.common.exception;

import com.zqq.eyes.common.Constants.ResultCode;
import lombok.Getter;

/**
 * 自定义异常
 */
@Getter
public class ServiceException extends RuntimeException  {

    private final ResultCode resultCode;

    public ServiceException(ResultCode resultCode){
        this.resultCode=resultCode;
    }

}
