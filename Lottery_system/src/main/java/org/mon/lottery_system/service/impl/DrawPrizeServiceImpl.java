package org.mon.lottery_system.service.impl;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.mon.lottery_system.common.errorcode.ServiceErrorCodeConstants;
import org.mon.lottery_system.common.exception.ServiceException;
import org.mon.lottery_system.common.utils.JacksonUtil;
import org.mon.lottery_system.common.utils.RedisUtil;
import org.mon.lottery_system.controller.param.DrawPrizeParam;
import org.mon.lottery_system.controller.param.ShowWinningRecordsParam;
import org.mon.lottery_system.dao.dataobject.*;
import org.mon.lottery_system.dao.mapper.*;
import org.mon.lottery_system.service.DrawPrizeService;
import org.mon.lottery_system.service.dto.WinningRecordDTO;
import org.mon.lottery_system.service.enums.ActivityPrizeTiersEnum;
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
    public Boolean checkDrawPrizeParam(DrawPrizeParam param) {

        ActivityDO activityDO=activityMapper.selectById(param.getActivityId());
//        奖品是否存在可以从activity_prize中查,原因是保存activity的时候做了本地事务，保证了一致性
        ActivityPrizeDO activityPrizeDO=activityPrizeMapper.selectByAPId(param.getActivityId(),param.getPrizeId());

//        活动是否有效和是否存在
        if(null==activityDO || null==activityPrizeDO) {
//            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_OR_PRIZE_IS_EMPTY);
            log.info("校验抽奖请求失败！because:{}",ServiceErrorCodeConstants.ACTIVITY_OR_PRIZE_IS_EMPTY.getMessage());
            return false;
        }

        if(activityDO.getStatus().equalsIgnoreCase(ActivityStatusEnum.COMPLETED.name())){
//            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_COMPLETED);
            log.info("校验抽奖请求失败！because:{}",ServiceErrorCodeConstants.ACTIVITY_COMPLETED.getMessage());
            return false;
        }

//        判断奖品是否有效
        if(activityPrizeDO.getStatus().equalsIgnoreCase(ActivityStatusEnum.COMPLETED.name())){
//            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_PRIZE_COMPLETED);
            log.info("校验抽奖请求失败！because:{}",ServiceErrorCodeConstants.ACTIVITY_PRIZE_COMPLETED.getMessage());
            return false;
        }

//        中奖者人数是否和设置奖品数量一致 3 2
        if(activityPrizeDO.getPrizeAmount()!=param.getWinnerList().size()){
            throw new ServiceException(ServiceErrorCodeConstants.WINNER_PRIZE_AMOUNT_ERROR);
        }
        return true;
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
            winningRecordDO.setWinningTime(paramD.getWinningTime());
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

    @Override
    public void deleteRecords(Long activityId, Long prizeId) {
        if(null==activityId){
            log.warn("要删除中奖记录相关的活动Id为空");
            return ;
        }
//        删除数据表
        winningRecordMapper.deleteRecords(activityId,prizeId);
        if(null!=prizeId){
            deleteWinningRecords(activityId+"_"+prizeId);
        }
//        删除缓存,无论是否传递了prizeId，都需要删除活动纬度的中奖记录的缓存
//        如果传递了prizeId，证明奖品未抽奖
        deleteWinningRecords(String.valueOf(activityId));
    }

    /**
     * 获取中奖记录
     * @param param
     * @return
     */
    @Override
    public List<WinningRecordDTO> getRecords(ShowWinningRecordsParam param) {
//        先查询redis:奖品维度和活动纬度
        String key=null==param.getPrizeId()?String.valueOf(param.getActivityId()):param.getActivityId()+"_"+ param.getPrizeId();

        List<WinningRecordDO> winningRecords = getWinningRecords(key);

        if(!CollectionUtils.isEmpty(winningRecords)){
            return convertToWinningRecordDTO(winningRecords);
        }

//        如果redis不存在，查库
        List<WinningRecordDO> winningRecordDOList=winningRecordMapper.selectByActivityIdOrPrizeId(param.getActivityId(),param.getPrizeId());

//        存放记录到redis中
        if(CollectionUtils.isEmpty(winningRecordDOList)) {
            log.info("查询中奖记录为空！param:{}",param);
            return Arrays.asList();
        }
        System.out.println("从数据库中查到的记录为:"+winningRecordDOList);
        cacheWinningRecords(key,winningRecordDOList,WINNING_RECORDS_TIMEOUT);
        return convertToWinningRecordDTO(winningRecordDOList);
    }

    private List<WinningRecordDTO> convertToWinningRecordDTO(List<WinningRecordDO> winningRecords) {
        if(CollectionUtils.isEmpty(winningRecords)) return Arrays.asList();

        return winningRecords.stream()
                .map(winningRecordDO -> {
                    WinningRecordDTO winningRecordDTO=new WinningRecordDTO();
                    winningRecordDTO.setWinnerId(winningRecordDO.getWinnerId());
                    winningRecordDTO.setWinnerName(winningRecordDO.getWinnerName());
                    winningRecordDTO.setPrizeName(winningRecordDO.getPrizeName());
                    winningRecordDTO.setPrizeTier(ActivityPrizeTiersEnum.forName(winningRecordDO.getPrizeTier()));
                    winningRecordDTO.setWinningTime(winningRecordDO.getWinningTime());
                    return winningRecordDTO;
                }).toList();
    }

    private void deleteWinningRecords(String key) {
        try{
            if(redisUtil.hasKey(WINNING_RECORDS_PREFIX+key)){
                redisUtil.del(WINNING_RECORDS_PREFIX+key);
            }
        }catch (Exception e){
            log.error("删除中奖记录缓存异常，key:{}",key);
        }
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
            System.out.println("要缓存的内容为:"+winningRecordDOList);
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
