package org.mon.lottery_system.controller.param;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class DrawPrizeParam implements Serializable {

//    活动Id
    @NotNull(message = "活动共Id不能为空")
    private Long activityId;
//    奖品Id
    @NotNull(message = "奖品Id不能为空")
    private Long prizeId;
//    奖品等级
//    @NotBlank(message = "奖品等级l不能为空")
//    private String prizeTiers;
//    中奖时间
    @NotNull(message = "中奖时间不能为空")
    private Date winningTime;
//    中奖人员列表
    @NotEmpty(message = "活动人员列表不能为空")
    @Valid
    private List<Winner> winnerList;


    @Data
    public static class Winner{
//        人员id
        @NotNull(message = "人员Id不能为空")
        private Long userId;
//        人员姓名
        @NotBlank(message = "中奖者姓名不能为空")
        private String userName;
    }
}
