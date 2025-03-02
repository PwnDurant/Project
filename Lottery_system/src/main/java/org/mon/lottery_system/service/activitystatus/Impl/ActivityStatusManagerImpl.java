package org.mon.lottery_system.service.activitystatus.Impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.mon.lottery_system.service.activitystatus.ActivityStatusManager;
import org.mon.lottery_system.service.activitystatus.operator.AbstractActivityOperator;
import org.mon.lottery_system.service.dto.ConvertActivityStatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class ActivityStatusManagerImpl implements ActivityStatusManager {

    @Autowired
    private final Map<String, AbstractActivityOperator> operatorMap =new HashMap<>();

    @Override
    public void handlerEvent(ConvertActivityStatusDTO activityStatusDTO) {

//        1，活动状态扭转有依赖性，导致代码维护性差
//        2，状态扭转条件可能会扩展，当前写法扩展性和维护性都很差

        if(CollectionUtils.isEmpty(operatorMap)){
            log.warn("当前operator为空");
            return ;
        }
//        map<String,AbstractActivityOperator> ，遍历Map
        Map<String,AbstractActivityOperator> currMap=new HashMap<>(operatorMap);

        Boolean update=false;
//        先处理：人员，奖品
        update=processConvertStatus(activityStatusDTO,currMap,1);

//        后处理：活动
        if (update) {
            processConvertStatus(activityStatusDTO,currMap,2);
        }else{
            update=processConvertStatus(activityStatusDTO,currMap,2);
        }

//        更新缓存
        if(update) {

        }


    }

    private Boolean processConvertStatus(ConvertActivityStatusDTO activityStatusDTO,
                                         Map<String, AbstractActivityOperator> currMap,
                                         int sequence) {
//        扭转状态

//        遍历Map，

//        Operator是否需要转换

//        需要转换就进行转换

//        在Map中删除当前遍历到的Operator

//        返回
    }
}
