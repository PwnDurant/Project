package org.mon.lottery_system.dao.dataobject;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ActivityDO extends BaseDO{

    private String activityName;

    private String description;

    private String status;

}
