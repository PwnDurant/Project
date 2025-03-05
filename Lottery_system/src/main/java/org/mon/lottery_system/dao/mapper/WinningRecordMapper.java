package org.mon.lottery_system.dao.mapper;

import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.*;
import org.mon.lottery_system.dao.dataobject.WinningRecordDO;
import org.mon.lottery_system.service.dto.WinningRecordDTO;

import java.util.List;

@Mapper
public interface WinningRecordMapper {


    @Insert("<script>" +
            " insert into winning_record (activity_id,activity_name,prize_id,prize_name,prize_tier,winner_id,winner_name,winner_email,winner_phone_number,winning_time)" +
            " values <foreach collection='items' item='item' index='index' separator=','>"+
            " (#{item.activityId},#{item.activityName},#{item.prizeId},#{item.prizeName},#{item.prizeTier},#{item.winnerId},#{item.winnerName},#{item.winnerEmail},#{item.winnerPhoneNumber},#{item.winningTime})"+
            " </foreach>"+
            " </script>")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int batchInsert(@Param("items") List<WinningRecordDO> winningRecordDOList);


    @Select("select * from winning_record where activity_id=#{activityId}")
    List<WinningRecordDO> selectByActivityId(@NotNull(message = "活动共Id不能为空")@Param("activityId") Long activityId);


    @Select("select count(1) from winning_record where activity_id=#{activityId} and prize_id=#{prizeId}")
    int countByAPId(@NotNull(message = "活动共Id不能为空")@Param("activityId") Long activityId, @NotNull(message = "奖品Id不能为空")@Param("prizeId") Long prizeId);


    @Delete("<script>" +
            " delete from winning_record" +
            " where activity_id = #{activityId}" +
            " <if test=\"prizeId != null\">" +
            "   and prize_id = #{prizeId}" +
            " </if>" +
            " </script>")
    void deleteRecords(@Param("activityId") Long activityId, @Param("prizeId") Long prizeId);


    @Select("<script>" +
            " select * from winning_record" +
            " where activity_id = #{activityId}" +
            " <if test=\"prizeId != null\">" +
            "   and prize_id = #{prizeId}" +
            " </if>" +
            " </script>")
    List<WinningRecordDO> selectByActivityIdOrPrizeId(@NotNull(message = "活动Id不能为空") @Param("activityId")Long activityId,  @Param("prizeId")Long prizeId);
}
