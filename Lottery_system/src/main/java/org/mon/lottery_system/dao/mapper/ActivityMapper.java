package org.mon.lottery_system.dao.mapper;


import org.apache.ibatis.annotations.*;
import org.mon.lottery_system.dao.dataobject.ActivityDO;
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
}
