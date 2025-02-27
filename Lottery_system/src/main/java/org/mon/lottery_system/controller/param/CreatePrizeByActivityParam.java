package org.mon.lottery_system.controller.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;


@Data
public class CreatePrizeByActivityParam implements Serializable {
    /**
     * 活动关联的奖品Id
     */

    @NotNull(message = "活动关联的奖品Id不能为空")
    private Long prizeId;

    /**
     * 奖品数量
     */
    @NotNull(message = "活动关联的奖品数量不能为空")
    private Long prizeAmount;

    /**
     * 奖品等级
     */
    @NotBlank(message = "奖品等级不能为空！")
    private String prizeTires;
}
