package org.mon.blog_system.common.exception;


import lombok.Data;

@Data
public class BlogException extends RuntimeException {


    private int code;
    private String message;

    public BlogException (int code){
        this.code=code;
    }

    public BlogException(){}

    public BlogException(int code ,String message){
        this.code=code;
        this.message=message;
    }

    public BlogException (String message){
        this.message=message;
    }
}
