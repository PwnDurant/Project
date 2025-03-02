package org.mon.lottery_system;


import org.junit.jupiter.api.Test;
import org.mon.lottery_system.controller.param.DrawPrizeParam;
import org.mon.lottery_system.service.DrawPrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class RabbitMqTest {

    @Autowired
    private DrawPrizeService drawPrizeService;

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

}
