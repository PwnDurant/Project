package com.zqq.eyes.common.code;


/**
 * 定义错误码
 */

public enum ResultCode {

    SUCCESS         (0,"操作成功！"),
    FAILED          (300,"操作失败"),
    SERVICE_ERROR   (500,"服务器内部异常"),
    UNKNOWN         (999,"未知错误"),
    NO_RESOURCE     (600,"访问资源不存在");

    /**
     * 错误码
     */
    private int code;

    /**
     * 错误描述
     */
    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "code = "+code+", message = "+message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


}
