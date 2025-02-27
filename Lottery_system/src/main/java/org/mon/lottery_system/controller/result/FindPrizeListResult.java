package org.mon.lottery_system.controller.result;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class FindPrizeListResult implements Serializable {

    /**
     * 总量
     */
    private Integer total;

    /**
     * 当前列表
     */
    private List<PrizeInfo> records;

    @Data
    public static class PrizeInfo implements Serializable{
        /**
         * 奖品Id
         */
        private Long prizeId;
        /**
         * 奖品名称
         */
        private String prizeName;
        /**
         * 描述
         */
        private String description;
        /**
         * 价值
         */
        private BigDecimal price;
        /**
         * 奖品图
         */
        private String imageUrl;
    }

}
