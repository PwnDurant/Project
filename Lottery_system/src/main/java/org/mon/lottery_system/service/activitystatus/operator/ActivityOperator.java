package org.mon.lottery_system.service.activitystatus.operator;


import org.mon.lottery_system.service.dto.ConvertActivityStatusDTO;
import org.springframework.stereotype.Component;

@Component
public class ActivityOperator extends AbstractActivityOperator{
    @Override
    public Integer sequence() {
        return 2;
    }

    @Override
    public Boolean needConvert(ConvertActivityStatusDTO convertActivityStatusDTO) {
//        需要判断奖品是否全部抽完
        return null;
    }
}
