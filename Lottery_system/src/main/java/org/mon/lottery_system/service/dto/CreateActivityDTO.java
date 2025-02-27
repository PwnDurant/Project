package org.mon.lottery_system.service.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class CreateActivityDTO implements Serializable {

    /**
     * 创建的活动Id
     */
    private Long activityId;

}
