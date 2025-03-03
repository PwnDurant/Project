package org.mon.lottery_system.dao.mapper;


import org.apache.ibatis.annotations.*;
import org.mon.lottery_system.dao.dataobject.ActivityUserDO;

import java.util.List;

@Mapper
public interface ActivityUserMapper {

    @Insert("<script>" +
            " insert into activity_user (activity_id, user_id, user_name, status)" +
            " values <foreach collection='items' item='item' index='index' separator=','>"+
            " (#{item.activityId},#{item.userId},#{item.userName},#{item.status})"+
            " </foreach>"+
            " </script>")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int batchInsert(@Param("items") List<ActivityUserDO> activityUserDOList);

    @Select("select * from activity_user where activity_id=#{activityId}")
    List<ActivityUserDO> selectByActivityId(Long activityId);


    @Select("<script>" +
            " select * from activity_user" +
            " where activity_id=#{activityId}" +
            " and user_id in" +
            " <foreach collection='userIds' item='userId' open='(' separator=',' close=')'>" +
            " #{userId}"+
            " </foreach>"+
            " </script>")
    List<ActivityUserDO> bathSelectByActivityAUIDs(Long activityId,@Param("userIds") List<Long> userIds);


    @Update("<script>" +
            " update activity_user set status=#{status}" +
            " where activity_id=#{activityId}" +
            " and user_id in" +
            " <foreach collection='userIds' item='userId' open='(' separator=',' close=')'>" +
            " #{userId}"+
            " </foreach>"+
            " </script>")
    void batchUpdateStatus(Long activityId, List<Long> userIds, String status);
}
