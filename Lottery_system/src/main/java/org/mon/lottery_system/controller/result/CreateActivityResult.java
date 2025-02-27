package org.mon.lottery_system.controller.result;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreateActivityResult implements Serializable {
    /**
     * 创建的活动Id
     */
    private Long activityId;
}
