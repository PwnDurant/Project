package org.mon.lottery_system.service.activitystatus.operator;

import org.mon.lottery_system.service.dto.ConvertActivityStatusDTO;

public abstract class AbstractActivityOperator {

    /**
     * 控制处理顺序
     * @return
     */
    public abstract Integer sequence();

    /**
     * 是否需要转换
     * @param convertActivityStatusDTO
     * @return
     */
    public abstract Boolean needConvert(ConvertActivityStatusDTO convertActivityStatusDTO);

}
