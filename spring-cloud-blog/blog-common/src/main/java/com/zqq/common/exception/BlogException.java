package com.zqq.common.exception;

import lombok.Data;

@Data
public class BlogException extends RuntimeException{
    private Integer code;
    private String message;

    public BlogException(String message) {
        this.message = message;
    }

    public BlogException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
