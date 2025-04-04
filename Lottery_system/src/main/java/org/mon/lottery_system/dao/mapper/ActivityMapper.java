package org.mon.lottery_system.dao.mapper;


import org.apache.ibatis.annotations.*;
import org.mon.lottery_system.dao.dataobject.ActivityDO;
import org.mon.lottery_system.dao.dataobject.ActivityPrizeDO;
import org.mon.lottery_system.dao.dataobject.ActivityUserDO;
import org.mon.lottery_system.dao.dataobject.PrizeDO;

import java.util.List;

@Mapper
public interface ActivityMapper {

    @Insert("insert into activity (activity_name, description, status) VALUES (#{activityName},#{description},#{status})")
    @Options(useGeneratedKeys = true ,keyProperty = "id",keyColumn = "id")
    int insert(ActivityDO activityDO);

    @Select("select count(*) from activity")
    int count();

    @Select("select * from activity order by id desc limit #{offset},#{pageSize}")
    List<ActivityDO> selectActivityList(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    @Select("select * from activity where id=#{id};")
    ActivityDO selectById(@Param("id") Long activityId);

    @Update("update activity set status=#{status} where id=#{id}")
    void updateStatus(Long id, String status);
}
