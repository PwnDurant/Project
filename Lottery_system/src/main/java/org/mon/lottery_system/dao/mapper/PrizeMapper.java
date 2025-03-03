package org.mon.lottery_system.dao.mapper;

import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.*;
import org.mon.lottery_system.dao.dataobject.PrizeDO;

import java.util.List;


@Mapper
public interface PrizeMapper {


    @Insert("insert into prize (name,image_url, price,description ) VALUES "+
            "(#{name},#{imageUrl},#{price},#{description})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id") //拿到数据库中的Id，拿到之后再塞回传入的对象的Id字段
    int insert(PrizeDO prizeDO);

    @Select("select count(*) from prize")
    int count();

    @Select("select * from prize order by id desc limit #{offset},#{pageSize}")
    List<PrizeDO> selectPrizeList(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);


    @Select("<script>" +
            " select id from prize" +
            " where id in  " +
            "<foreach item='item' collection='items' open='(' separator=',' close=')'>"+
            " #{item}"+
            " </foreach>"+
            " </script>")
    List<Long> selectExistByIds(@Param("items") List<Long> ids);


    @Select("<script>" +
            " select * from prize" +
            " where id in  " +
            "<foreach item='item' collection='items' open='(' separator=',' close=')'>"+
            " #{item}"+
            " </foreach>"+
            " </script>")
    List<PrizeDO> batchSelectByIds(@Param("items") List<Long> ids);


    @Select("select * from prize where id=#{prizeId}")
    PrizeDO selectById(@NotNull(message = "奖品Id不能为空")@Param("prizeId") Long prizeId);
}
