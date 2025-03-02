package org.mon.lottery_system.dao.mapper;


import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.*;
import org.mon.lottery_system.dao.dataobject.ActivityPrizeDO;

import java.util.List;

@Mapper
public interface ActivityPrizeMapper {


    @Insert("<script>" +
            " insert into activity_prize (activity_id, prize_id, prize_amount, prize_tiers,status)" +
            " values <foreach collection='items' item='item' index='index' separator=','>"+
            " (#{item.activityId},#{item.prizeId},#{item.prizeAmount},#{item.prizeTiers},#{item.status})"+
            " </foreach>"+
            " </script>")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int batchInsert(@Param("items") List<ActivityPrizeDO> activityPrizeDOList);

    @Select("select * from activity_prize where activity_id=#{activityId}")
    List<ActivityPrizeDO> selectByActivityId(@Param("activityId") Long activityId);


    @Select("select * from activity_prize where activity_id=#{activityId} and prize_id=#{prizeId}")
    ActivityPrizeDO selectByAPId(@NotNull(message = "活动共Id不能为空") @Param("activityId") Long activityId, @NotNull(message = "奖品Id不能为空") @Param("prizeId") Long prizeId);
}
