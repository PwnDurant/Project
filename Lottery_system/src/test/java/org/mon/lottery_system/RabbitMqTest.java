package org.mon.lottery_system;


import org.junit.jupiter.api.Test;
import org.mon.lottery_system.controller.param.DrawPrizeParam;
import org.mon.lottery_system.controller.param.ShowWinningRecordsParam;
import org.mon.lottery_system.service.DrawPrizeService;
import org.mon.lottery_system.service.activitystatus.ActivityStatusManager;
import org.mon.lottery_system.service.dto.ConvertActivityStatusDTO;
import org.mon.lottery_system.service.dto.WinningRecordDTO;
import org.mon.lottery_system.service.enums.ActivityPrizeStatusEnum;
import org.mon.lottery_system.service.enums.ActivityStatusEnum;
import org.mon.lottery_system.service.enums.ActivityUserStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class RabbitMqTest {

    @Autowired
    private DrawPrizeService drawPrizeService;

    @Autowired
    private ActivityStatusManager activityStatusManager;

    @Test
    void setDrawPrizeService(){

        DrawPrizeParam param=new DrawPrizeParam();
        param.setActivityId(25L);
        param.setPrizeId(19L);
        param.setPrizeTiers("FIRST_PRIZE");
        param.setWinningTime(new Date());
        List<DrawPrizeParam.Winner> winnerList=new ArrayList<>();
        DrawPrizeParam.Winner winner=new DrawPrizeParam.Winner();
        winner.setUserId(44L);
        winner.setUserName("zhaoqianqian");
        winnerList.add(winner);
        param.setWinnerList(winnerList);
        drawPrizeService.drawPrize(param);
    }


    @Test
    void statusConvert(){
        ConvertActivityStatusDTO convertActivityStatusDTO=new ConvertActivityStatusDTO();
        convertActivityStatusDTO.setActivityId(24L);
        convertActivityStatusDTO.setTargetActivityStatus(ActivityStatusEnum.COMPLETED);
        convertActivityStatusDTO.setPrizeId(19L);
        convertActivityStatusDTO.setTargetPrizeStatus(ActivityPrizeStatusEnum.COMPLETED);
        List<Long> userIds= Arrays.asList(45L);
        convertActivityStatusDTO.setUserIds(userIds);
        convertActivityStatusDTO.setTargetUserStatus(ActivityUserStatusEnum.COMPLETED);
        activityStatusManager.handlerEvent(convertActivityStatusDTO);
    }

    @Test
    void saveWinnerRecords(){
        DrawPrizeParam param=new DrawPrizeParam();
        param.setActivityId(24L);
        param.setPrizeId(19L);
        param.setPrizeTiers("FIRST_PRIZE");
        param.setWinningTime(new Date());
        List<DrawPrizeParam.Winner> winnerList=new ArrayList<>();
        DrawPrizeParam.Winner winner=new DrawPrizeParam.Winner();
        winner.setUserId(45L);
        winner.setUserName("xxx");
        winnerList.add(winner);
        param.setWinnerList(winnerList);
        drawPrizeService.saveWinnerRecords(param);
    }


//    正向流程
//    处理异常
//    消息堆积（重发），存放死性队列中


    @Test
    void showWinningRecords(){
        ShowWinningRecordsParam param=new ShowWinningRecordsParam();
        param.setActivityId(24L);
        List<WinningRecordDTO> list=drawPrizeService.getRecords(param);
        for (WinningRecordDTO winningRecordDTO:list){
            System.out.println(winningRecordDTO.getWinnerName()+"_"+
                    winningRecordDTO.getPrizeName()+"_"+
                    winningRecordDTO.getPrizeTier().getMessage());
        }
    }



}
