package org.mon.lottery_system.common.errorcode;


/**
 * 全局错误码
 */
public interface GlobalErrorCodeConstants {

    ErrorCode SUCCESS=new ErrorCode(200,"成功！");

    ErrorCode INTERNAL_SERVER_ERROR=new ErrorCode(500,"系统异常！");

    ErrorCode UNKNOWN=new ErrorCode(999,"未知错误！");

}
