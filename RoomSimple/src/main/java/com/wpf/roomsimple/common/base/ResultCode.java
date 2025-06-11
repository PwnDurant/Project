package com.wpf.roomsimple.common.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS                     (200,"操作成功！"),
    FAILED                      (1000,"服务器繁忙，请稍后重试！"),
    FAILED_PARAMS_VALIDATE      (10001,"参数校验失败" );

    private final int code;

    private final String msg;

}
