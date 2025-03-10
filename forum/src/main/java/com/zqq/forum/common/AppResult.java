package com.zqq.forum.common;

public class AppResult<T> {


//    状态码
    private int code;
//    描述信息
    private String message;
//    返回类型
    private T data;

    public AppResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public AppResult(int code, String message) {
        this(code,message,null);
    }

    /**
     * 对外提供的构造方法
     * @return
     */

    /**
     * 成功
     * @return
     */
    public static AppResult success(){
        return new AppResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    public static AppResult success(String message){
        return new AppResult(ResultCode.SUCCESS.getCode(), message);
    }

    public static <T> AppResult<T> success(T data){
        return new AppResult<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(),data);
    }

    public static <T> AppResult<T> success(String message,T data){
        return new AppResult<>(ResultCode.SUCCESS.getCode(), message,data);
    }

    /**
     * 失败
     */
    public static AppResult failed(){
        return new AppResult(ResultCode.FAILED.getCode(),ResultCode.FAILED.getMessage());
    }

    public static AppResult failed(String message){
        return new AppResult(ResultCode.FAILED.getCode(),message);
    }

    public static AppResult failed(ResultCode resultCode){
        return new AppResult(resultCode.getCode(), resultCode.getMessage());
    }

}
