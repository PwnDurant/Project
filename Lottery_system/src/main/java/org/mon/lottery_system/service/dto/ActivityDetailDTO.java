package org.mon.lottery_system.service.dto;


import lombok.Data;
import org.mon.lottery_system.service.enums.ActivityPrizeStatusEnum;
import org.mon.lottery_system.service.enums.ActivityPrizeTiersEnum;
import org.mon.lottery_system.service.enums.ActivityStatusEnum;
import org.mon.lottery_system.service.enums.ActivityUserStatusEnum;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ActivityDetailDTO {

//    活动信息
    private Long activityId;

//    活动名称
    private String activityName;

//    活动描述
    private String desc;

//     活动状态
    private ActivityStatusEnum status;


    public Boolean valid(){
        return status.equals(ActivityStatusEnum.RUNNING);
    }


//    奖品信息：列表

    private List<PrizeDTO> prizeDTOList;

//    奖品信息：列表

    private List<UserDTO> userDTOList;


    @Data
    public static class PrizeDTO{

        private Long prizeId;

        private String name;

        private String imageUrl;

        private BigDecimal price;

        private String description;

        private ActivityPrizeTiersEnum Tier;

        private Long prizeAmount;

        private ActivityPrizeStatusEnum status;

        public Boolean valid(){
            return status.equals(ActivityPrizeStatusEnum.INIT);
        }
    }

    @Data
    public static class UserDTO{

        private Long userId;

        private String userName;

        private ActivityUserStatusEnum status;

        public Boolean valid(){
            return status.equals(ActivityUserStatusEnum.INIT);
        }
    }



}











