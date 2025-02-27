package org.mon.lottery_system.dao.dataobject;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class ActivityPrizeDO extends BaseDO {

    /**
     * 关联的活动Id
     */
    private Long activityId;
    /**
     * 关联的奖品Id
     */
    private Long prizeId;
    /**
     * 奖品数量
     */
    private Long prizeAmount;
    /**
     * 奖品等级
     */
    private String prizeTiers;
    /**
     * 奖品状态
     */
    private String status;

}
