package org.mon.lottery_system.controller;


import lombok.extern.slf4j.Slf4j;
import org.mon.lottery_system.common.pojo.CommonResult;
import org.mon.lottery_system.controller.param.DrawPrizeParam;
import org.mon.lottery_system.controller.param.ShowWinningRecordsParam;
import org.mon.lottery_system.controller.result.WinningRecordsResult;
import org.mon.lottery_system.service.DrawPrizeService;
import org.mon.lottery_system.service.dto.WinningRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

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

    @RequestMapping("/winning-records/show")
    public CommonResult<List<WinningRecordsResult>> showWinningRecords(@Validated @RequestBody ShowWinningRecordsParam param){
        log.info("showWinningRecords param:{}",param);
        List<WinningRecordDTO> winningRecordDTOList=drawPrizeService.getRecords(param);
        return CommonResult.success(converToWinningRecordDTOList(winningRecordDTOList));
    }

    private List<WinningRecordsResult> converToWinningRecordDTOList(List<WinningRecordDTO> winningRecordDTOList) {

        if(CollectionUtils.isEmpty(winningRecordDTOList)) {
            return Arrays.asList();
        }

        return winningRecordDTOList.stream()
                .map(winningRecordDTO -> {
                    WinningRecordsResult result=new WinningRecordsResult();
                    result.setWinnerId(winningRecordDTO.getWinnerId());
                    result.setWinnerName(winningRecordDTO.getWinnerName());
                    result.setPrizeName(winningRecordDTO.getPrizeName());
                    result.setPrizeTier(winningRecordDTO.getPrizeTier().getMessage());
                    result.setWinningTime(winningRecordDTO.getWinningTime());
                    return result;
                }).toList();
    }


}
