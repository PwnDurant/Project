package org.mon.lottery_system.dao.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.mon.lottery_system.dao.dataobject.ActivityDO;

@Mapper
public interface ActivityMapper {

    @Insert("insert into activity (activity_name, description, status) VALUES (#{activityName},#{description},#{status})")
    @Options(useGeneratedKeys = true ,keyProperty = "id",keyColumn = "id")
    int insert(ActivityDO activityDO);

}
