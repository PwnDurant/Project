package org.mon.lottery_system.service.activitystatus.Impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.mon.lottery_system.common.errorcode.ServiceErrorCodeConstants;
import org.mon.lottery_system.common.exception.ServiceException;
import org.mon.lottery_system.service.ActivityService;
import org.mon.lottery_system.service.activitystatus.ActivityStatusManager;
import org.mon.lottery_system.service.activitystatus.operator.AbstractActivityOperator;
import org.mon.lottery_system.service.dto.ConvertActivityStatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Slf4j
@Component
public class ActivityStatusManagerImpl implements ActivityStatusManager {

    @Autowired
    private final Map<String, AbstractActivityOperator> operatorMap =new HashMap<>();

    @Autowired
    private ActivityService activityService;

    @Override
    @Transactional(rollbackFor = Exception.class)
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
        update=processConvertStatus(activityStatusDTO,currMap,2) || update;

//        更新缓存
        if(update) {
            activityService.cacheActivity(activityStatusDTO.getActivityId());
        }


    }

    @Override
    public void rollbackHandlerEvent(ConvertActivityStatusDTO activityStatusDTO) {

//        operatorMap：活动，奖品，人员

//        活动是否需要回滚，绝对需要：奖品都恢复为INIT状态了，所以活动下的奖品绝对没有抽完

        for(AbstractActivityOperator operator: operatorMap.values()){
            operator.convert(activityStatusDTO);
        }

//        缓存更新
        activityService.cacheActivity(activityStatusDTO.getActivityId());
    }

    private Boolean processConvertStatus(ConvertActivityStatusDTO activityStatusDTO,
                                         Map<String, AbstractActivityOperator> currMap,
                                         int sequence) {
        Boolean update=false;
//        扭转状态
        Iterator<Map.Entry<String, AbstractActivityOperator>> iterator=currMap.entrySet().iterator();
        while(iterator.hasNext()){
            AbstractActivityOperator operator = iterator.next().getValue();
//        Operator是否需要转换
            if(operator.sequence()!=sequence || !operator.needConvert(activityStatusDTO)){
                continue;
            }
//            需要转换：转换
            if(!operator.convert(activityStatusDTO)){
                log.error("iterator：{},转换失败",operator.getClass().getName());
                throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_STATUS_CONVERT_ERROR);
            }
//        在Map中删除当前遍历到的Operator
            iterator.remove();
            update=true;
        }

//        返回
        return update;

    }
}
