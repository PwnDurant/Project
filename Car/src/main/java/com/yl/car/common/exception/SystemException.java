package com.wpf.roomsimple.common.exception;

import com.wpf.roomsimple.common.base.ResultCode;
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
