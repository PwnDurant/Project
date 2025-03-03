package org.mon.lottery_system.service.impl;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.mon.lottery_system.common.errorcode.ServiceErrorCodeConstants;
import org.mon.lottery_system.common.exception.ServiceException;
import org.mon.lottery_system.common.utils.JacksonUtil;
import org.mon.lottery_system.common.utils.RedisUtil;
import org.mon.lottery_system.controller.param.DrawPrizeParam;
import org.mon.lottery_system.dao.dataobject.*;
import org.mon.lottery_system.dao.mapper.*;
import org.mon.lottery_system.service.DrawPrizeService;
import org.mon.lottery_system.service.enums.ActivityStatusEnum;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mon.lottery_system.common.config.DirectRabbitConfig.EXCHANGE_NAME;
import static org.mon.lottery_system.common.config.DirectRabbitConfig.ROUTING;

@Slf4j
@Service
public class DrawPrizeServiceImpl implements DrawPrizeService {

    private final Long WINNING_RECORDS_TIMEOUT=60*60*24*2L;
    private final String WINNING_RECORDS_PREFIX="WINNING_RECORDS_";

//    行为对象
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PrizeMapper prizeMapper;
    @Autowired
    private ActivityUserMapper activityUserMapper;
    @Autowired
    private WinningRecordMapper winningRecordMapper;
    @Autowired
    private RedisUtil redisUtil;


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

    @Override
    public List<WinningRecordDO> saveWinnerRecords(DrawPrizeParam paramD) {
//        查询信息:活动，奖品，人员，活动关联奖品表

        ActivityDO activityDO=activityMapper.selectById(paramD.getActivityId());
        List<UserDO> userDOList=userMapper.batchSelectByIds(paramD.getWinnerList().stream().map(DrawPrizeParam.Winner::getUserId).toList());
        PrizeDO prizeDO=prizeMapper.selectById(paramD.getPrizeId());
        ActivityPrizeDO activityPrizeDO=activityPrizeMapper.selectByAPId(paramD.getActivityId(), paramD.getPrizeId());

//        构造记录

        List<WinningRecordDO> winningRecordDOList=userDOList.stream().map(userDO -> {
            WinningRecordDO winningRecordDO=new WinningRecordDO();
            winningRecordDO.setActivityId(activityDO.getId());
            winningRecordDO.setActivityName(activityDO.getActivityName());
            winningRecordDO.setPrizeId(prizeDO.getId());
            winningRecordDO.setPrizeName(prizeDO.getName());
            winningRecordDO.setPrizeTier(activityPrizeDO.getPrizeTiers());
            winningRecordDO.setWinnerEmail(userDO.getEmail());
            winningRecordDO.setWinnerId(userDO.getId());
            winningRecordDO.setWinnerName(userDO.getUserName());
            winningRecordDO.setWinnerPhoneNumber(userDO.getPhoneNumber());
            winningRecordDO.setWinnerTime(paramD.getWinningTime());
            return winningRecordDO;
        }).toList();
        winningRecordMapper.batchInsert(winningRecordDOList);

//        缓存记录
//        缓存奖品纬度的中奖记录(WinningRecord_activityId_prizeId,winnerRecordDOList（奖品纬度的中奖名单）)
        cacheWinningRecords(paramD.getActivityId()+"_"+paramD.getPrizeId(),winningRecordDOList,WINNING_RECORDS_TIMEOUT);

//        缓存活动纬度的中奖记录(WinningRecord_activityId,winningRecordDOList（活动纬度的中奖名单）)
//        当活动已完成的时候，再去存放活动纬度的中奖记录
        if(activityDO.getStatus().equalsIgnoreCase(ActivityStatusEnum.COMPLETED.name())){

//            查询活动纬度的全量中奖记录
            List<WinningRecordDO> allList=winningRecordMapper.selectByActivityId(paramD.getActivityId());

            cacheWinningRecords(String.valueOf(paramD.getActivityId()),allList,WINNING_RECORDS_TIMEOUT);

        }
        return winningRecordDOList;
    }

    /**
     * 缓存中奖记录
     * @param key
     * @param winningRecordDOList
     * @param winningRecordsTimeout
     */
    private void cacheWinningRecords(String key, List<WinningRecordDO> winningRecordDOList, Long winningRecordsTimeout) {
        try{
            if(!StringUtils.hasText(key)|| CollectionUtils.isEmpty(winningRecordDOList)){
                log.warn("要缓存的内容为空:key={},value={}",WINNING_RECORDS_PREFIX+key,JacksonUtil.writeValueAsString(winningRecordDOList));
                return ;
            }
            redisUtil.set(WINNING_RECORDS_PREFIX+key,JacksonUtil.writeValueAsString(winningRecordDOList),winningRecordsTimeout);
        }catch (Exception e){
            log.error("缓存中奖记录异常:key={},value={}",WINNING_RECORDS_PREFIX+key,JacksonUtil.writeValueAsString(winningRecordDOList));
        }
    }


    /**
     * 从缓存中获取中奖记录
     * @param key
     * @return
     */
    private List<WinningRecordDO> getWinningRecords(String key){
        try{

            if(StringUtils.hasText(key)) return Arrays.asList();

            String result=redisUtil.get(WINNING_RECORDS_PREFIX+key);

            if(!StringUtils.hasText(result)) return Arrays.asList();

            return JacksonUtil.readListValue(result,WinningRecordDO.class);

        }catch (Exception e){
            log.error("从缓存中查询奖记录异常");
            return Arrays.asList();
        }
    }
}
