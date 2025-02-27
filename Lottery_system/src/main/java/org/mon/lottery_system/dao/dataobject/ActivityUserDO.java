package org.mon.lottery_system.dao.dataobject;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class ActivityUserDO extends BaseDO {

    /**
     * 活动Id
     */
    private Long activityId;
    /**
     * 人员Id
     */
    private Long userId;
    /**
     * 人员姓名
     */
    private String userName;
    /**
     * 人员状态
     */
    private String status;

}
