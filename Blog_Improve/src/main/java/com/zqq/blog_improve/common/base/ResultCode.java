package com.zqq.blog_improve.common.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态码
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS                     (1000,"操作成功！"),
    FAILED                      (2000,"服务器繁忙，请稍后重试！"),
    USER_NOT_EXISTS             (20001,"当前用户不存在,请注册！"),
    FAILED_PARAMS_VALIDATE      (20002,"输入参数校验异常！"),
    REQUEST_NOT_SUPPORT         (20003,"请求方式不支持！"),
    NAME_OR_PASSWORD_ERROR      (20004,"账号或密码错误！"),
    PASSWORD_IS_EMPTY           (20005,"密码不能为空！"),
    SQL_PASSWORD_IS_ERROR       (20006,"当前用户数据库中密码错误！" );

    private final int code;

    private final String msg;

}
