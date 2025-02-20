package org.mon.blog_system.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ResultStatusEnums {
//    200表示成功 -1失败
    SUCCESS(200),
    FAIL(-1);

    @Getter
    int code;
}
