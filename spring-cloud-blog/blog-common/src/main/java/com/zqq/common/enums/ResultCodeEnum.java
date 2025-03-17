package com.zqq.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ResultCodeEnum {
    SUCCESS(200),
    FAIL(-1);
    @Getter
    int code;

}
