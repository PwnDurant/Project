package com.zqq.forum.exception;


import com.zqq.forum.common.AppResult;

/**
 * 自定义异常
 */
public class ApplicationException extends RuntimeException{

//    在异常中持有一个错误信息
    protected AppResult errorResult;


    /**
     * 构造方法
     * @param appResult
     */
    public ApplicationException (AppResult appResult){
        super(appResult.getMessage());
        this.errorResult=appResult;
    }

    public AppResult getErrorResult() {
        return errorResult;
    }

    public void setErrorResult(AppResult errorResult) {
        this.errorResult = errorResult;
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
