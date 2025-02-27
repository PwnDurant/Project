package org.mon.lottery_system.common.errorcode;


/**
 * controller层的错误码
 */
public interface ControllerErrorCodeConstants {
    //    ------ 人员模块错误码 ------
        ErrorCode REGISTER_ERROR=new ErrorCode(100,"注册失败");
        ErrorCode LOGIN_ERROR=new ErrorCode(101,"登入失败");


    //    ------ 活动模块错误码 ------
        ErrorCode CREATE_ACTIVITY_ERROR=new ErrorCode(300,"创建活动失败");


    //    ------ 奖品模块错误码 ------
        ErrorCode FIND_PRIZE_LIST_ERROR=new ErrorCode(200,"查询奖品列表失败");



    //    ------ 抽奖错误码 ------



}
