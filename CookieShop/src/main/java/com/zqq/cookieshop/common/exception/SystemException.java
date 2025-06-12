package com.zqq.cookieshop.common.exception;

import com.zqq.cookieshop.common.base.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SystemException extends RuntimeException {
    public SystemException(String message) {
        super(message);
    }

    private ResultCode resultCode;
}
