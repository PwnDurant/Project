package com.zqq.common.core.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 定义错误码
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS                             (0,"操作成功！"),
    ERROR                               (1,"服务器繁忙，请稍后重试"),
    FAILED                              (2,"操作失败"),
    FAILED_PARAMS_VALIDATE              (3,"参数校验失败"),
    FAILED_UNAUTHORIZED                 (4,"未授权"),



    USER_ERROR                          (100,"用户错误"),
    FAILED_USER_PHONE                   (101,"您输入的手机号码有误"),
    FAILED_FREQUENT                     (102,"操作频繁，请稍后重试"),
    FAILED_TIME_LIMIT                   (103,"当天请求次数已经达到上限"),
    FAILED_SEND_CODE                    (104,"发送验证码失败"),
    FAILED_INVALID_CODE                 (105,"验证码无效"),
    FAILED_ERROR_CODE                   (106,"验证码错误"),
    FAILED_USER_NOT_EXISTS              (107,"用户不存在"),



    FAILED_FILE_UPLOAD                  (200,"图片上传失败"),
    FAILED_FILE_UPLOAD_TIME_LIMIT       (201,"图片上传超过次数");


    private final int code; //错误码

    private final String msg; //错误信息


}
