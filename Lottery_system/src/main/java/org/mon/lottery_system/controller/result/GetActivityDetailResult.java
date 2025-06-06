package org.mon.lottery_system.controller.result;

import lombok.Data;


import org.mon.lottery_system.service.enums.ActivityPrizeTiersEnum;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class GetActivityDetailResult implements Serializable {

    //    活动信息
    private Long activityId;

    //    活动名称
    private String activityName;

    //    活动描述
    private String description;

    //    活动是否有效
    private Boolean valid;



//    奖品信息：列表

    private List<Prize> prizes;

//    奖品信息：列表

    private List<User> users;


    @Data
    public static class Prize{

        private Long prizeId;

        private String name;

        private String imageUrl;

        private BigDecimal price;

        private String description;


        /**
         * 奖品等级
         * @see ActivityPrizeTiersEnum#getMessage()
         */
        private String prizeTierName;

        private Long prizeAmount;

        /**
         * 奖品是否有效
         */
        private Boolean valid;

    }

    @Data
    public static class User{

        private Long userId;

        private String userName;

        private Boolean valid;

    }


}
