package org.mon.blog_system.common.pojo;


import lombok.Data;
import org.mon.blog_system.common.enums.ResultStatusEnums;


//设置统一数据返回的格式
@Data
public class Result<T> {
    private int code;
    private String errMsg;
    private T data;

//    这里加T是为了声明这是一个泛型方法，编译器会自动根据调用它的类型推导出返回类型是什么
    public static <T> Result<T> ok(T data){
        Result result=new Result();
        result.setCode(ResultStatusEnums.SUCCESS.getCode());
        result.setData(data);
        result.setErrMsg("");
        return result;
    }

    public static <T> Result<T> fail(String errMsg){
        Result result=new Result();
        result.setCode(ResultStatusEnums.FAIL.getCode());
        result.setData(null);
        result.setErrMsg(errMsg);
        return result;
    }

    public static <T> Result<T> fail(String errMsg,T data){
        Result result=new Result();
        result.setCode(ResultStatusEnums.FAIL.getCode());
        result.setData(data);
        result.setErrMsg(errMsg);
        return result;
    }
}
