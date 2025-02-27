package org.mon.lottery_system.controller;


import lombok.extern.slf4j.Slf4j;
import org.mon.lottery_system.common.errorcode.ControllerErrorCodeConstants;
import org.mon.lottery_system.common.exception.ControllerException;
import org.mon.lottery_system.common.pojo.CommonResult;
import org.mon.lottery_system.controller.param.CreateActivityParam;
import org.mon.lottery_system.controller.result.CreateActivityResult;
import org.mon.lottery_system.service.ActivityService;
import org.mon.lottery_system.service.dto.CreateActivityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

    private CreateActivityResult converToCreateActivityResult(CreateActivityDTO createActivityDTO) {
        if(null==createActivityDTO) throw new ControllerException(ControllerErrorCodeConstants.CREATE_ACTIVITY_ERROR);

        CreateActivityResult result=new CreateActivityResult();

        result.setActivityId(createActivityDTO.getActivityId());

        return result;

    }


}
