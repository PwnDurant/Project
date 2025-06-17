package com.hxm.hxm.common.exception;

import com.hxm.hxm.common.base.ResultCode;
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
