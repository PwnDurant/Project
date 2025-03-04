package org.mon.lottery_system.controller.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class ShowWinningRecordsParam implements Serializable {

    /**
     * 活动共Id
     */
    @NotNull(message = "活动Id不能为空")
    private Long activityId;

    /**
     * 奖品Id
     */
    private Long prizeId;


}
