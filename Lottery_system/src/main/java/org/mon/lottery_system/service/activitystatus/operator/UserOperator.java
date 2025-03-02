package org.mon.lottery_system.service.activitystatus.operator;


import org.mon.lottery_system.dao.dataobject.ActivityUserDO;
import org.mon.lottery_system.dao.mapper.ActivityUserMapper;
import org.mon.lottery_system.service.dto.ConvertActivityStatusDTO;
import org.mon.lottery_system.service.enums.ActivityUserStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Component
public class UserOperator extends AbstractActivityOperator{

    @Autowired
    private ActivityUserMapper activityUserMapper;

    @Override
    public Integer sequence() {
        return 1;
    }

    @Override
    public Boolean needConvert(ConvertActivityStatusDTO convertActivityStatusDTO) {
//        判断当前人员状态是否不是COMPLETED
        Long activityId= convertActivityStatusDTO.getActivityId();
        List<Long> userIds=convertActivityStatusDTO.getUserIds();
        ActivityUserStatusEnum targetUserStatus =convertActivityStatusDTO.getTargetUserStatus();
        if(null==activityId|| CollectionUtils.isEmpty(userIds) || targetUserStatus==null) return false;

        List<ActivityUserDO> activityUserDOList = activityUserMapper.bathSelectByActivityAUIDs(activityId, userIds);
        if(CollectionUtils.isEmpty(activityUserDOList)) return false;
        for(ActivityUserDO auDO: activityUserDOList){
            if(auDO.getStatus().equalsIgnoreCase(targetUserStatus.name())){
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean convert(ConvertActivityStatusDTO convertActivityStatusDTO) {
        Long activityId= convertActivityStatusDTO.getActivityId();
        List<Long> userIds=convertActivityStatusDTO.getUserIds();
        ActivityUserStatusEnum targetUserStatus =convertActivityStatusDTO.getTargetUserStatus();
        try{
            activityUserMapper.batchUpdateStatus(activityId,userIds,targetUserStatus.name());
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
