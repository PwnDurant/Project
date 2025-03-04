package org.mon.lottery_system.service.activitystatus;

import org.mon.lottery_system.service.dto.ConvertActivityStatusDTO;

public interface ActivityStatusManager {


    /**
     * 处理活动相关状态转换
     * @param activityStatusDTO
     */
    void handlerEvent(ConvertActivityStatusDTO activityStatusDTO);


    /**
     * 回滚活动相关状态转换
     * @param activityStatusDTO
     */
    void rollbackHandlerEvent(ConvertActivityStatusDTO activityStatusDTO);
}
