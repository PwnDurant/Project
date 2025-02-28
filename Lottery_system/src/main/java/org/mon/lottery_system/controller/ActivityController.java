package org.mon.lottery_system.controller;


import lombok.extern.slf4j.Slf4j;
import org.mon.lottery_system.common.errorcode.ControllerErrorCodeConstants;
import org.mon.lottery_system.common.exception.ControllerException;
import org.mon.lottery_system.common.pojo.CommonResult;
import org.mon.lottery_system.common.utils.JacksonUtil;
import org.mon.lottery_system.controller.param.CreateActivityParam;
import org.mon.lottery_system.controller.param.PageParam;
import org.mon.lottery_system.controller.result.CreateActivityResult;
import org.mon.lottery_system.controller.result.FindActivityListResult;
import org.mon.lottery_system.service.ActivityService;
import org.mon.lottery_system.service.dto.ActivityDTO;
import org.mon.lottery_system.service.dto.CreateActivityDTO;
import org.mon.lottery_system.service.dto.PageListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @RequestMapping("/activity/create")
    public CommonResult<CreateActivityResult> createActivity(@RequestBody @Validated CreateActivityParam param){
        log.info("createActivity param:{}",param);
        return CommonResult.success(converToCreateActivityResult(activityService.createActivity(param)));
    }

    @RequestMapping("/activity/find-list")
    public CommonResult<FindActivityListResult> findActivityList(PageParam param){

        log.info("findActivityList PageParam:{}", JacksonUtil.writeValueAsString(param));

        return CommonResult.success(
                converToFindActivityListResult(
                        activityService.findActivityList(param)));

    }

    private FindActivityListResult converToFindActivityListResult(PageListDTO<ActivityDTO> activityList) {
        if(null==activityList){
            throw new ControllerException(ControllerErrorCodeConstants.FIND_ACTIVITY_ACTIVITY_ERROR);
        }

        FindActivityListResult result=new FindActivityListResult();
        result.setTotal(activityList.getTotal());
        result.setRecords(activityList.getRecords().stream()
                .map(activityDTO -> {
                    FindActivityListResult.ActivityInfo activityInfo=new FindActivityListResult.ActivityInfo();
                    activityInfo.setActivityId(activityDTO.getActivityId());
                    activityInfo.setActivityName(activityDTO.getActivityName());
                    activityInfo.setDescription(activityInfo.getDescription());
                    activityInfo.setValid(activityDTO.valid());
                    return activityInfo;
                }).toList()
        );
        return result;
    }

    private CreateActivityResult converToCreateActivityResult(CreateActivityDTO createActivityDTO) {
        if(null==createActivityDTO) throw new ControllerException(ControllerErrorCodeConstants.CREATE_ACTIVITY_ERROR);

        CreateActivityResult result=new CreateActivityResult();

        result.setActivityId(createActivityDTO.getActivityId());

        return result;

    }


}
