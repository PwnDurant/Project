package org.mon.lottery_system.service.activitystatus.operator;


import org.mon.lottery_system.dao.dataobject.ActivityDO;
import org.mon.lottery_system.dao.mapper.ActivityMapper;
import org.mon.lottery_system.dao.mapper.ActivityPrizeMapper;
import org.mon.lottery_system.service.dto.ConvertActivityStatusDTO;
import org.mon.lottery_system.service.enums.ActivityPrizeStatusEnum;
import org.mon.lottery_system.service.enums.ActivityStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivityOperator extends AbstractActivityOperator{


    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;

    @Override
    public Integer sequence() {
        return 2;
    }

    @Override
    public Boolean needConvert(ConvertActivityStatusDTO convertActivityStatusDTO) {

        Long activityId=convertActivityStatusDTO.getActivityId();
        ActivityStatusEnum targetStatus=convertActivityStatusDTO.getTargetActivityStatus();
        if(null==activityId||null==targetStatus){
            return false;
        }
//        根据activityId查询对应的activityDo
        ActivityDO activityDO = activityMapper.selectById(activityId);
        if(null==activityDO) return false;

//        当前活动状态与传入的状态一致，不处理
        if(targetStatus.name().equalsIgnoreCase(activityDO.getStatus())) return false;

//        需要判断奖品是否全部抽完
//        查询INIT状态的奖品个数
        int count=activityPrizeMapper.countPrize(activityId, ActivityPrizeStatusEnum.INIT.name());
        if(count>0){
            return false;
        }

        return true;
    }

    @Override
    public Boolean convert(ConvertActivityStatusDTO convertActivityStatusDTO) {

        try{
            activityMapper.updateStatus(convertActivityStatusDTO.getActivityId(),
                    convertActivityStatusDTO.getTargetPrizeStatus().name());
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
