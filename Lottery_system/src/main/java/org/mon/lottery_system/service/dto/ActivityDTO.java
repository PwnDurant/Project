package org.mon.lottery_system.service.dto;


import lombok.Data;
import org.mon.lottery_system.dao.dataobject.BaseDO;
import org.mon.lottery_system.service.enums.ActivityStatusEnum;

import java.io.Serializable;

@Data
public class ActivityDTO implements Serializable {

    //        活动Id
    private Long activityId;

    //        活动名称
    private String activityName;

    //        活动描述
    private String description;

    //        活动状态
    private ActivityStatusEnum status;

    /**
     * 活动是否有效
     * @return
     */
    public Boolean valid(){
        return status.equals(ActivityStatusEnum.RUNNING);
    }


}
