package org.mon.lottery_system.service;

import org.mon.lottery_system.controller.param.DrawPrizeParam;
import org.mon.lottery_system.controller.param.ShowWinningRecordsParam;
import org.mon.lottery_system.dao.dataobject.WinningRecordDO;
import org.mon.lottery_system.service.dto.WinningRecordDTO;

import java.util.List;

public interface DrawPrizeService {


    /**
     * 异步抽奖接口
     * @param drawPrizeParam
     */
    void drawPrize(DrawPrizeParam drawPrizeParam);

    /**
     * 校验抽奖请求是否有效
     */
    Boolean checkDrawPrizeParam(DrawPrizeParam param);


    /**
     * 保存中奖者名单
     * @param paramD
     */
    List<WinningRecordDO> saveWinnerRecords(DrawPrizeParam paramD);

    /**
     * 删除活动奖品下的中奖记录
     * @param activityId
     * @param prizeId
     */
    void deleteRecords(Long activityId,Long prizeId);



    List<WinningRecordDTO> getRecords(ShowWinningRecordsParam param);
}
