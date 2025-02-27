package org.mon.lottery_system.service.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityPrizeTiersEnum {

    FIRST_PRIZE(1,"一等奖"),
    SECOND_PRIZE(2,"二等奖"),
    THIRD_PRIZE(3,"三等奖");

    private final Integer code;

    private final String message;


    /**
     * 根据name去获取对应的枚举值
     */
    public static ActivityPrizeTiersEnum forName(String name){
        for(ActivityPrizeTiersEnum activityPrizeStatusEnum:ActivityPrizeTiersEnum.values()){
            if(activityPrizeStatusEnum.name().equalsIgnoreCase(name)){
                return activityPrizeStatusEnum;
            }
        }
        return null;
    }


}
