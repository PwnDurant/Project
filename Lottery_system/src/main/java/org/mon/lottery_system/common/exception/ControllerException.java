package org.mon.lottery_system.common.exception;


import lombok.Data;
import org.mon.lottery_system.common.errorcode.ErrorCode;

import java.rmi.ConnectIOException;

/**
 * 自定义异常
 */

@Data
public class ControllerException extends RuntimeException {
  /**
   * 异常码
   * @see org.mon.lottery_system.common.errorcode.ControllerErrorCodeConstants
   */
    private Integer code;
  /**
   * 异常消息
   */
  private String message;

  public ControllerException(Integer code,String message){
    this.code=code;
    this.message=message;
  }

//  为了序列化创建无参构造函数
  public ControllerException(){

  }

  public ControllerException(ErrorCode errorCode){
    this.code= errorCode.getCode();
    this.message= errorCode.getMessage();
  }





}
