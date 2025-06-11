package com.zqq.cookieshop.common.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 设置统一返回格式
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class R<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 成功返回
     * @return 代表操作成功
     */
    public static R<Boolean> ok(){
        R<Boolean> r = new R<>();
        r.setCode(ResultCode.SUCCESS.getCode());
        r.setMsg(ResultCode.SUCCESS.getMsg());
        r.setData(true);
        return r;
    }

    /**
     * 成功返回
     * @param data 需要返回的 data 数据
     * @return 代表操作成功并返回前端需要的数据
     * @param <T> 携带 data 数据返回
     */
    public static <T> R<T> ok(T data){
        return assembleResult(data,ResultCode.SUCCESS);
    }

    /**
     * 失败返回
     * @return 代表操作失败
     */
    public static R<Boolean> fail(){
        R<Boolean> r = new R<>();
        r.setCode(ResultCode.FAILED.getCode());
        r.setMsg(ResultCode.FAILED.getMsg());
        r.setData(false);
        return r;
    }

    /**
     * 失败返回
     * @param resultCode 传入设定好的错误状态码
     * @return 返回带有具体规定好的错误信息的 R 对象
     * @param <T> 携带返回的具体 msg
     */
    public static <T> R<T> fail(ResultCode resultCode){
        return assembleResult(null,resultCode);
    }

    public static <T> R<T> fail(int code,String msg){
        return assembleResult(code,msg,null);
    }

    /**
     * 封装返回结果
     * @param data  要返回的数据
     * @param resultCode  返回的状态码
     * @return 返回 R 对象
     * @param <T> 传入的泛型数据
     */
    public static <T> R<T> assembleResult(T data,ResultCode resultCode){
        R<T> r = new R<>();
        r.setCode(resultCode.getCode());
        r.setMsg(resultCode.getMsg());
        r.setData(data);
        return r;
    }

    public static <T> R<T> assembleResult(int code,String msg,T data){
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setMsg(msg);
        return r;
    }

}
