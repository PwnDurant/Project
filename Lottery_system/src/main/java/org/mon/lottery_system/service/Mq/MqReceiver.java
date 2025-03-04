package org.mon.lottery_system.service.Mq;


import cn.hutool.core.date.DateUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.mon.lottery_system.common.config.ExecutorConfig;
import org.mon.lottery_system.common.exception.ServiceException;
import org.mon.lottery_system.common.utils.JacksonUtil;
import org.mon.lottery_system.common.utils.MailUtil;
import org.mon.lottery_system.common.utils.SMSUtil;
import org.mon.lottery_system.controller.param.DrawPrizeParam;
import org.mon.lottery_system.dao.dataobject.ActivityPrizeDO;
import org.mon.lottery_system.dao.dataobject.WinningRecordDO;
import org.mon.lottery_system.dao.mapper.ActivityPrizeMapper;
import org.mon.lottery_system.dao.mapper.WinningRecordMapper;
import org.mon.lottery_system.service.DrawPrizeService;
import org.mon.lottery_system.service.activitystatus.ActivityStatusManager;
import org.mon.lottery_system.service.dto.ConvertActivityStatusDTO;
import org.mon.lottery_system.service.enums.ActivityPrizeStatusEnum;
import org.mon.lottery_system.service.enums.ActivityPrizeTiersEnum;
import org.mon.lottery_system.service.enums.ActivityStatusEnum;
import org.mon.lottery_system.service.enums.ActivityUserStatusEnum;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mon.lottery_system.common.config.DirectRabbitConfig.QUEUE_NAME;



@RabbitListener(queues = QUEUE_NAME)
@Component
@Slf4j
public class MqReceiver {

    @Autowired
    private DrawPrizeService drawPrizeService;

    @Autowired
    private ActivityStatusManager activityStatusManager;

    @Resource(name = "asyncServiceExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private SMSUtil smsUtil;

    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;

    @Autowired
    private WinningRecordMapper winningRecordMapper;


//    处理消息
    @RabbitHandler
    public void process(Map<String,String> map){
//        表示成功接收到队列的消息
        log.info("接收消息:mao={}", JacksonUtil.writeValueAsString(map));
        String param=map.get("messageDate");
//        反序列化
        DrawPrizeParam paramD=JacksonUtil.readValue(param, DrawPrizeParam.class);
//        处理抽奖流程
        try {
//            if(true) throw new ServiceException();

//            if(true){
//                return ;
//            }

            if(!drawPrizeService.checkDrawPrizeParam(paramD)) return ;

//        校验抽奖请求是否有效
//            1，有可能前端发起两个一样的抽奖请求，对于paramD来说，也是两个一样的请求
//            2，paramD：
//            处理paramD1：最后一个奖项-》活动完成，奖品完成
//            处理paramD2：会回滚之前的状态
            drawPrizeService.checkDrawPrizeParam(paramD);
//        活动，奖品，人员状态处理（状态扭转处理）(重要！！！）
            statusConvert(paramD);

//        保存中奖者名单
            List<WinningRecordDO> winningRecordDOList= drawPrizeService.saveWinnerRecords(paramD);

//        通知中奖者（短信，邮箱）根据中奖信息去发送
//            抽奖之后的后续流程，采用异步（并发）的方式处理
            syncExecute(winningRecordDOList);

        }catch (ServiceException e){
            log.error("处理Mq消息异常{}:{}",e.getCode(),e.getMessage(),e);
//        如果发送异常，需要保证事务的一致性，需要进行回滚，并且再次抛出异常，提醒Mq，进行重发，放到死性队列中
            rollback(paramD);
            throw e;

        }catch (Exception e){
            log.error("处理Mq消息异常！",e);
//        如果发送异常，需要保证事务的一致性，需要进行回滚，并且再次抛出异常，提醒Mq，进行重发，放到死性队列中
            rollback(paramD);
            throw e;
        }
    }


    /**
     * 处理抽奖异常的回滚行为,恢复处理请求之前的状态
     * @param paramD
     */
    private void rollback(DrawPrizeParam paramD) {

//        1，回滚状态：活动表，奖品表，人员表
//        判断是否需要回滚
//        不需要回滚：return
//        需要回滚：回滚
        if(!statusNeedRollback(paramD)) return ;
        rollbackStatus(paramD);

//        2，回滚中奖者名单
//        是否需要回滚
//        不需要：return 需要：回滚
        if(!winnerNeedRollback(paramD)) return ;
        rollbackWinner(paramD);

    }


    /**
     * 回滚中奖记录
     * @param paramD
     */
    private void rollbackWinner(DrawPrizeParam paramD) {
        drawPrizeService.deleteRecords(paramD.getActivityId(), paramD.getPrizeId());

    }

    private boolean winnerNeedRollback(DrawPrizeParam paramD) {
//        只需要判断活动中的奖品是否存在中奖者
        int count=winningRecordMapper.countByAPId(paramD.getActivityId(),paramD.getPrizeId());

        return count>0;
    }


    /**
     * 恢复相关状态的方法
     * @param paramD
     */
    private void rollbackStatus(DrawPrizeParam paramD) {
//        涉及状态的恢复工作

        ConvertActivityStatusDTO convertActivityStatusDTO=new ConvertActivityStatusDTO();
        convertActivityStatusDTO.setActivityId(paramD.getActivityId());
        convertActivityStatusDTO.setTargetActivityStatus(ActivityStatusEnum.RUNNING);
        convertActivityStatusDTO.setPrizeId(paramD.getPrizeId());
        convertActivityStatusDTO.setTargetPrizeStatus(ActivityPrizeStatusEnum.INIT);
        convertActivityStatusDTO.setUserIds(paramD.getWinnerList().stream().map(
                DrawPrizeParam.Winner::getUserId).toList()
        );
        convertActivityStatusDTO.setTargetUserStatus(ActivityUserStatusEnum.INIT);


        activityStatusManager.rollbackHandlerEvent(convertActivityStatusDTO);


    }

    private boolean statusNeedRollback(DrawPrizeParam paramD) {
//        判断活动+奖品+人员，判断相关状态是否扭转（正常思路）
//        但是在扭转状态的时候，保证了本地事务的一致性，所以只需要判断一个状态就行了
//        因此只需判断人员/奖品是否扭转过，就可以判断出状态是否全部扭转，但是不可以判断活动是否已经扭转
//        结论：判断奖品状态是否扭转，就可以判断出全部状态是否扭转
        ActivityPrizeDO activityPrizeDO = activityPrizeMapper.selectByAPId(paramD.getActivityId(), paramD.getPrizeId());
        return activityPrizeDO.getStatus().equalsIgnoreCase(ActivityStatusEnum.COMPLETED.name());
    }


    /**
     * 并发处理抽奖后续流程
     * @param winningRecordDOList
     */
    private void syncExecute(List<WinningRecordDO> winningRecordDOList) {
//        通过线程池

//        短信通知
        threadPoolTaskExecutor.execute(()->sendMessage(winningRecordDOList));
//        邮箱通知
        threadPoolTaskExecutor.execute(()->sendMail(winningRecordDOList));
    }

    /**
     * 发送邮件
     * @param winningRecordDOList
     */
    private void sendMail(List<WinningRecordDO> winningRecordDOList) {
        if(CollectionUtils.isEmpty(winningRecordDOList)){
            log.info("发送中奖列表为空，不用发邮件");
            return ;
        }
        for (WinningRecordDO winningRecordDO:winningRecordDOList){
            String context = "Hi，" + winningRecordDO.getWinnerName() + "。恭喜你在"
                    + winningRecordDO.getActivityName() + "活动中获得"
                    + ActivityPrizeTiersEnum.forName(winningRecordDO.getPrizeTier()).getMessage()
                    + "：" + winningRecordDO.getPrizeName() + "。获奖时间为"
                    + DateUtil.formatTime(winningRecordDO.getWinningTime()) + "，请尽快领 取您的奖励！";
            mailUtil.sendSampleMail(winningRecordDO.getWinnerEmail(),
                    "中奖通知", context);
        }

    }


    /**
     * 发送短信
     * @param winningRecordDOList
     */
    private void sendMessage(List<WinningRecordDO> winningRecordDOList) {
        if (CollectionUtils.isEmpty(winningRecordDOList)) {
            log.info("中奖列表为空，不用发短信！");
            return;
        }
        for (WinningRecordDO winningRecordDO : winningRecordDOList) {
            Map<String, String> map = new HashMap<>();
            map.put("name", winningRecordDO.getWinnerName());
            map.put("activityName", winningRecordDO.getActivityName());
            map.put("prizeTiers", ActivityPrizeTiersEnum.forName(winningRecordDO.getPrizeTier()).getMessage());
            map.put("prizeName", winningRecordDO.getPrizeName());
            map.put("winningTime", DateUtil.formatTime(winningRecordDO.getWinningTime()));
            smsUtil.sendMessage("SMS_479100562",
                    winningRecordDO.getWinnerPhoneNumber().getValue(),
                    JacksonUtil.writeValueAsString(map));
        }
    }

    /**
     * 状态扭转
     * @param param
     */


    private void statusConvert(DrawPrizeParam param){
        ConvertActivityStatusDTO convertActivityStatusDTO=new ConvertActivityStatusDTO();
        convertActivityStatusDTO.setActivityId(param.getActivityId());
        convertActivityStatusDTO.setTargetActivityStatus(ActivityStatusEnum.COMPLETED);
        convertActivityStatusDTO.setPrizeId(param.getPrizeId());
        convertActivityStatusDTO.setTargetPrizeStatus(ActivityPrizeStatusEnum.COMPLETED);
        convertActivityStatusDTO.setUserIds(param.getWinnerList().stream().map(DrawPrizeParam.Winner::getUserId).toList());
        convertActivityStatusDTO.setTargetUserStatus(ActivityUserStatusEnum.COMPLETED);
        activityStatusManager.handlerEvent(convertActivityStatusDTO);
    }


//    private void statusConvert(DrawPrizeParam paramD) {
////        问题：
////        1，活动状态扭转有依赖性，导致代码维护性差
////        2，状态扭转条件可能会扩展，当前写法扩展性和维护性都很差
////        3，代码的灵活性和扩展性差
////        解决方案：设计模式（责任链设计模式，策略模式）
//
////        扭转活动状态，奖品状态，人员状态
//
////        活动：RUNNING->COMPLETED ?? 全部奖品抽完之后才会改变状态
////        奖品：INIT->COMPLETED
////        人员：INIT->COMPLETED
//
////        1，扭转奖品或人/状态
////        查询活动关联的奖品信息
////        条件判断是否符合扭转奖品状态，如果不是COMPLETED就扭转
//
//
//
////        2，扭转活动状态（必须在扭转奖品状态之后完成)
////        查询活动状态
////        条件判断是否符合扭转奖品状态，如果不是且全部奖品抽完之后才改变状态
//
//
////        更新活动完整信息的缓存
//
//    }


}
