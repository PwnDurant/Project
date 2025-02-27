package org.mon.lottery_system.service.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityUserStatusEnum {
    INIT(1,"初始化"),
    COMPLETED(2,"已被抽取");

    private final Integer code;

    private final String message;


    /**
     * 根据name去获取对应的枚举值
     */
    public static ActivityUserStatusEnum forName(String name){
        for(ActivityUserStatusEnum activityUserStatusEnum:ActivityUserStatusEnum.values()){
            if(activityUserStatusEnum.name().equalsIgnoreCase(name)){
                return activityUserStatusEnum;
            }
        }
        return null;
    }
}
