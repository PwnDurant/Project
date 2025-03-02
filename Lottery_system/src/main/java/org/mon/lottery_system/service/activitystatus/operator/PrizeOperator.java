package org.mon.lottery_system.service.activitystatus.operator;


import org.mon.lottery_system.dao.dataobject.ActivityPrizeDO;
import org.mon.lottery_system.dao.mapper.ActivityPrizeMapper;
import org.mon.lottery_system.service.dto.ConvertActivityStatusDTO;
import org.mon.lottery_system.service.enums.ActivityPrizeStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrizeOperator extends AbstractActivityOperator{

    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;


    @Override
    public Integer sequence() {
        return 1;
    }

    @Override
    public Boolean needConvert(ConvertActivityStatusDTO convertActivityStatusDTO) {
//        判断当前奖品状态是否不是COMPLETED
        Long activityId=convertActivityStatusDTO.getActivityId();
        Long prizeId=convertActivityStatusDTO.getPrizeId();
        ActivityPrizeStatusEnum targetPrizeStatus=convertActivityStatusDTO.getTargetPrizeStatus();
        if(null==prizeId||null==targetPrizeStatus||null==activityId) return false;

        ActivityPrizeDO activityPrizeDO = activityPrizeMapper.selectByAPId(activityId, prizeId);

        if(null==activityPrizeDO) return false;

        if(targetPrizeStatus.name().equalsIgnoreCase(activityPrizeDO.getStatus())){
            return false;
        }

        return true;
    }

    @Override
    public Boolean convert(ConvertActivityStatusDTO convertActivityStatusDTO) {
        Long activityId=convertActivityStatusDTO.getActivityId();
        Long prizeId=convertActivityStatusDTO.getPrizeId();
        ActivityPrizeStatusEnum targetPrizeStatus=convertActivityStatusDTO.getTargetPrizeStatus();

        try{
            activityPrizeMapper.updateStatus(activityId,prizeId,targetPrizeStatus.name());
            return true;
        }catch (Exception e){
            return false;
        }

    }
}
