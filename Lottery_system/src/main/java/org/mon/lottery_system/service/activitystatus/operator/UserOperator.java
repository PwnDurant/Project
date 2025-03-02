package org.mon.lottery_system.service.activitystatus.operator;


import org.mon.lottery_system.service.dto.ConvertActivityStatusDTO;
import org.springframework.stereotype.Component;

@Component
public class UserOperator extends AbstractActivityOperator{


    @Override
    public Integer sequence() {
        return 1;
    }

    @Override
    public Boolean needConvert(ConvertActivityStatusDTO convertActivityStatusDTO) {
//        判断当前人员状态是否不是COMPLETED
        return null;
    }
}
