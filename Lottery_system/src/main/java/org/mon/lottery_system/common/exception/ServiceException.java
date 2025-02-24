package org.mon.lottery_system.common.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mon.lottery_system.common.errorcode.ErrorCode;

/**
 * 自定义异常
 */

@Data
//不写的话，可能会出现问题
@EqualsAndHashCode(callSuper = true)  //使用父类的一些相关属性，可以直接继承到本类中
public class ServiceException extends RuntimeException {
  /**
   * 异常码
   * @see org.mon.lottery_system.common.errorcode.ServiceErrorCodeConstants
   */
    private Integer code;
  /**
   * 异常消息
   */
  private String message;

  public ServiceException(Integer code, String message){
    this.code=code;
    this.message=message;
  }

//  为了序列化创建无参构造函数
  public ServiceException(){

  }

  public ServiceException(ErrorCode errorCode){
    this.code= errorCode.getCode();
    this.message= errorCode.getMessage();
  }





}
