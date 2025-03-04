package org.mon.lottery_system.controller.result;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WinningRecordsResult implements Serializable {

    /**
     * 中奖者Id
     */
    private Long winnerId;

    /**
     * 中奖者姓名
     */
    private String winnerName;

    /**
     * 奖品姓名
     */
    private String prizeName;

    /**
     * 奖品等级
     */
    private String prizeTier;

    /**
     * 获奖时间
     */
    private Date winningTime;

}
