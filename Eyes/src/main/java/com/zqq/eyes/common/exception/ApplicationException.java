package com.zqq.eyes.common.exception;

import com.zqq.eyes.common.pojo.WebResult;
import lombok.Data;

/**
 * 自定义异常
 */
@Data
public class ApplicationException extends RuntimeException  {

    /**
     * 在自定义异常中有一个统一返回的错误信息
     */
    private WebResult errorResult;

    /**
     * 构造方法
     */
    public ApplicationException (WebResult webResult){
        super(webResult.getMessage());
        this.errorResult=webResult;
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

}
