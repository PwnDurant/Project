package org.mon.lottery_system.service.Mq;


import lombok.extern.slf4j.Slf4j;
import org.mon.lottery_system.common.exception.ServiceException;
import org.mon.lottery_system.common.utils.JacksonUtil;
import org.mon.lottery_system.controller.param.DrawPrizeParam;
import org.mon.lottery_system.service.DrawPrizeService;
import org.mon.lottery_system.service.activitystatus.ActivityStatusManager;
import org.mon.lottery_system.service.dto.ConvertActivityStatusDTO;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
//        校验抽奖请求是否有效
            drawPrizeService.checkDrawPrizeParam(paramD);
//        活动，奖品，人员状态处理（状态扭转处理）(重要！！！）
            statusConvert(paramD);

//        保存中奖者名单

//        通知中奖者（短信，邮箱）

        }catch (ServiceException e){
            log.error("处理Mq消息异常{}:{}",e.getCode(),e.getMessage(),e);
//        如果发送异常，需要保证事务的一致性，需要进行回滚，并且再次抛出异常，提醒Mq，进行重发，放到死性队列中

        }catch (Exception e){
            log.error("处理Mq消息异常！",e);
//        如果发送异常，需要保证事务的一致性，需要进行回滚，并且再次抛出异常，提醒Mq，进行重发，放到死性队列中

        }
    }

    /**
     * 状态扭转
     * @param param
     */
    private void statusConvert(DrawPrizeParam param){
        ConvertActivityStatusDTO convertActivityStatusDTO=new ConvertActivityStatusDTO();
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
