package org.mon.lottery_system.service;


import org.mon.lottery_system.controller.param.CreateActivityParam;
import org.mon.lottery_system.controller.param.PageParam;
import org.mon.lottery_system.service.dto.ActivityDTO;
import org.mon.lottery_system.service.dto.ActivityDetailDTO;
import org.mon.lottery_system.service.dto.CreateActivityDTO;
import org.mon.lottery_system.service.dto.PageListDTO;

public interface ActivityService {
//    dto是service层对外输出的，do是mapper层对外输出的

    /**
     * 创建活动
     * @param param
     * @return
     */
    CreateActivityDTO createActivity(CreateActivityParam param);


    /**
     * 翻页查询活动摘要列表
     * @param param
     * @return
     */
    PageListDTO<ActivityDTO> findActivityList(PageParam param);


    /**
     * 获取活动详细属性
     * @param activityId
     * @return
     */
    ActivityDetailDTO getActivityDetail(Long activityId);
}
