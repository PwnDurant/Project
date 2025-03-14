package com.zqq.eyes.common.pojo;


import com.zqq.eyes.common.code.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 设置统一数据返回的格式
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebResult<T> implements Serializable {

    private Integer code;

    private String message;

    private T data;

    public WebResult(int code,String message){
        this(code,message,null);
    }


    /**
     * 这是对外提供的构造方法
     * @return
     */

    /**
     * 成功
     * @return
     */
    public static WebResult success(){
        return new WebResult(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMessage());
    }

    public static WebResult success(String message){
        return new WebResult(ResultCode.SUCCESS.getCode(), message);
    }

    public static <T> WebResult<T> success(T data){
        return new WebResult<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(),data);
    }

    public static <T> WebResult<T> success(String message,T data){
        return new WebResult<>(ResultCode.SUCCESS.getCode(), message,data);
    }

    /**
     * 失败
     */
    public static WebResult failed(){
        return new WebResult(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage());
    }

    public static WebResult failed(String message){
        return new WebResult(ResultCode.FAILED.getCode(), message);
    }

    public static WebResult failed(ResultCode resultCode){
        return new WebResult(resultCode.getCode(), resultCode.getMessage());
    }


}
