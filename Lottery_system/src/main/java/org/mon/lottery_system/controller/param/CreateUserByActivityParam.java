package org.mon.lottery_system.controller.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreateUserByActivityParam implements Serializable {

    @NotNull(message = "活动人员不能为空！")
    private Long userId;


    @NotBlank(message = "姓名不能为空！")
    private String userName;

}
