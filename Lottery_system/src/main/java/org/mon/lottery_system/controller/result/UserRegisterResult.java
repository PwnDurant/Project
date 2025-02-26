package org.mon.lottery_system.controller.result;


import lombok.Data;

import java.io.Serializable;

/**
 * 需要在http中进行解析，所以需要对这个对象进行序列化
 */
@Data
public class UserRegisterResult implements Serializable {

    private Long userId;


}
