package org.mon.lottery_system.service;


import org.mon.lottery_system.controller.param.CreateActivityParam;
import org.mon.lottery_system.service.dto.CreateActivityDTO;

public interface ActivityService {
//    dto是service层对外输出的，do是mapper层对外输出的

    /**
     * 创建活动
     * @param param
     * @return
     */
    CreateActivityDTO createActivity(CreateActivityParam param);
}
