package org.mon.lottery_system.service.impl;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.mon.lottery_system.common.errorcode.ServiceErrorCodeConstants;
import org.mon.lottery_system.common.exception.ServiceException;
import org.mon.lottery_system.common.utils.JacksonUtil;
import org.mon.lottery_system.controller.param.DrawPrizeParam;
import org.mon.lottery_system.dao.dataobject.ActivityDO;
import org.mon.lottery_system.dao.dataobject.ActivityPrizeDO;
import org.mon.lottery_system.dao.mapper.ActivityMapper;
import org.mon.lottery_system.dao.mapper.ActivityPrizeMapper;
import org.mon.lottery_system.service.DrawPrizeService;
import org.mon.lottery_system.service.enums.ActivityStatusEnum;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.mon.lottery_system.common.config.DirectRabbitConfig.EXCHANGE_NAME;
import static org.mon.lottery_system.common.config.DirectRabbitConfig.ROUTING;

@Slf4j
@Service
public class DrawPrizeServiceImpl implements DrawPrizeService {

//    行为对象
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;


    @Override
    public void drawPrize(DrawPrizeParam drawPrizeParam) {

        Map<String,String> map=new HashMap<>();
        map.put("messageId",String.valueOf(UUID.randomUUID()));
        map.put("messageDate", JacksonUtil.writeValueAsString(drawPrizeParam));
//        发消息:交换机 ，绑定的Key,消息体
        rabbitTemplate.convertAndSend(EXCHANGE_NAME,ROUTING,map);
        log.info("mq消息成功：map={}",JacksonUtil.writeValueAsString(drawPrizeParam));

    }

    @Override
    public void checkDrawPrizeParam(DrawPrizeParam param) {

        ActivityDO activityDO=activityMapper.selectById(param.getActivityId());
//        奖品是否存在可以从activity_prize中查,原因是保存activity的时候做了本地事务，保证了一致性
        ActivityPrizeDO activityPrizeDO=activityPrizeMapper.selectByAPId(param.getActivityId(),param.getPrizeId());

//        活动是否有效和是否存在
        if(null==activityDO || null==activityPrizeDO) {
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_OR_PRIZE_IS_EMPTY);
        }

        if(activityDO.getStatus().equalsIgnoreCase(ActivityStatusEnum.COMPLETED.name())){
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_COMPLETED);
        }

//        判断奖品是否有效
        if(activityPrizeDO.getStatus().equalsIgnoreCase(ActivityStatusEnum.COMPLETED.name())){
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_PRIZE_COMPLETED);
        }

//        中奖者人数是否和设置奖品数量一致 3 2
        if(activityPrizeDO.getPrizeAmount()!=param.getWinnerList().size()){
            throw new ServiceException(ServiceErrorCodeConstants.WINNER_PRIZE_AMOUNT_ERROR);
        }

    }
}
