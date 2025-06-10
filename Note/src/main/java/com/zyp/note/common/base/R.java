package com.zyp.note.common.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class R<T> {

    private Integer code;

    private String msg;

    private T data;

    public static R<Boolean> ok(){
        R<Boolean> r = new R<>();
        r.setCode(ResultCode.SUCCESS.getCode());
        r.setMsg(ResultCode.SUCCESS.getMsg());
        r.setData(true);
        return r;
    }

    public static <T> R<T> ok(T data){
        return new R<T>(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMsg(),data);
    }

    public static  R<Boolean> fail(){
        return new R<Boolean>(ResultCode.FAILED.getCode(),ResultCode.FAILED.getMsg(), false);
    }

    public static <T> R<T> fail(ResultCode resultCode){
        return new R<T>(ResultCode.FAILED.getCode(),ResultCode.FAILED.getMsg(),null);
    }

    public static <T> R<T> fail(int code,String msg){
        return new R<T>(code,msg,null);
    }

}
