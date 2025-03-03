package org.mon.lottery_system;


import org.junit.jupiter.api.Test;
import org.mon.lottery_system.controller.param.DrawPrizeParam;
import org.mon.lottery_system.service.DrawPrizeService;
import org.mon.lottery_system.service.activitystatus.ActivityStatusManager;
import org.mon.lottery_system.service.dto.ConvertActivityStatusDTO;
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
        param.setActivityId(1L);
        param.setPrizeId(1L);
        param.setPrizeTiers("FIRST_PRIZE");
        param.setWinningTime(new Date());
        List<DrawPrizeParam.Winner> winnerList=new ArrayList<>();
        DrawPrizeParam.Winner winner=new DrawPrizeParam.Winner();
        winner.setUserId(1L);
        winner.setUserName("xxx");
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

}
