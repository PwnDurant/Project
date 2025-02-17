package org.mon.library_management_system.model;


import lombok.Data;
import org.mon.library_management_system.constant.Constants;

@Data
public class Result<T> {
    private int code ; //200:成功 -1:用户未登入 -2:程序出错     业务状态码，非http状态码
    private String errMsg;
    private T data;

    public static <T> Result<T> unlogin(){
        Result result=new Result();
        result.setCode(Constants.UNLOGIN_CODE);
        result.setErrMsg("用户未登入");
        return result;
    }

    public static <T> Result<T> fail(T data){
        Result result=new Result();
        result.setCode(Constants.FAIL_CODE);
        result.setErrMsg("程序发生错误");
        return result;
    }

    public static <T> Result<T> fail(){
        Result result=new Result();
        result.setCode(Constants.FAIL_CODE);
        result.setErrMsg("程序发生错误");
        return result;
    }

    public static <T> Result<T> fail(String errMsg){
        Result result=new Result();
        result.setCode(Constants.FAIL_CODE);
        result.setErrMsg(errMsg);
        return result;
    }

    public static <T> Result<T> fail(String errMsg,int code){
        Result result=new Result();
        result.setCode(Constants.FAIL_CODE);
        result.setErrMsg(errMsg);
        return result;
    }

    public static <T> Result<T> success(T data){
        Result result=new Result();
        result.setCode(Constants.SUCCESS_CODE);
        result.setErrMsg("");
        result.setData(data);
        return result;
    }

}
