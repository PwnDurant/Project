package org.mon.lottery_system.controller;


import lombok.extern.slf4j.Slf4j;
import org.mon.lottery_system.common.pojo.CommonResult;
import org.mon.lottery_system.controller.param.DrawPrizeParam;
import org.mon.lottery_system.service.DrawPrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DrawPrizeController {

    @Autowired
    private DrawPrizeService drawPrizeService;

    @RequestMapping("/draw-prize")
    public CommonResult<Boolean> drawPrize(@RequestBody @Validated DrawPrizeParam drawPrizeParam){

        log.info("drawPrize drawPrizeParam:{}",drawPrizeParam);

//        service层方法
        drawPrizeService.drawPrize(drawPrizeParam);
        return CommonResult.success(true);
    }


}
