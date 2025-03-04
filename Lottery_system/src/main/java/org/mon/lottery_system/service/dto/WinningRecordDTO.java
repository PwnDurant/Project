package org.mon.lottery_system.service.dto;


import lombok.Data;
import org.mon.lottery_system.service.enums.ActivityPrizeTiersEnum;

import java.io.Serializable;
import java.util.Date;

@Data
public class WinningRecordDTO implements Serializable {

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
    private ActivityPrizeTiersEnum prizeTier;

    /**
     * 获奖时间
     */
    private Date winningTime;
}
