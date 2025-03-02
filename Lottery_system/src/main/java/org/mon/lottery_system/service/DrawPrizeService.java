package org.mon.lottery_system.service;

import org.mon.lottery_system.controller.param.DrawPrizeParam;

public interface DrawPrizeService {


    /**
     * 异步抽奖接口
     * @param drawPrizeParam
     */
    void drawPrize(DrawPrizeParam drawPrizeParam);

    /**
     * 校验抽奖请求是否有效
     */
    void checkDrawPrizeParam(DrawPrizeParam param);


}
