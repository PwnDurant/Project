package org.mon.lottery_system.service.dto;


import lombok.Data;
import org.mon.lottery_system.service.enums.ActivityPrizeStatusEnum;
import org.mon.lottery_system.service.enums.ActivityStatusEnum;
import org.mon.lottery_system.service.enums.ActivityUserStatusEnum;

import java.util.List;

@Data
public class ConvertActivityStatusDTO {

    /**
     * 活动Id
     */
    private Long activityId;


    /**
     * 活动目标状态
     */
    private ActivityStatusEnum targetActivityStatus;


    /**
     * 奖品Id
     */
    private Long prizeId;

    /**
     * 奖品目标状态
     */
    private ActivityPrizeStatusEnum targetPrizeStatus;

    /**
     * 人员列表Id
     */
    private List<Long> userIds;

    /**
     * 人员目标状态
     */
    private ActivityUserStatusEnum targetUserStatus;

}
