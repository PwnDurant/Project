package org.mon.lottery_system.dao.mapper;


import jakarta.validation.constraints.NotBlank;
import org.apache.ibatis.annotations.*;
import org.mon.lottery_system.dao.dataobject.Encrypt;
import org.mon.lottery_system.dao.dataobject.UserDO;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 查询邮箱绑定的人数
     * @param email
     * @return
     */
    @Select("select count(*) from user where email=#{email}")
    int countByMail(@Param("email") String email);


    @Select("select count(*) from user where phone_number=#{phoneNumber}")
    int countByPhone(@NotBlank(message = "电话不能为空") Encrypt phoneNumber);


    @Insert("insert into user (user_name,email,phone_number,password,identity)"+
    " values (#{userName},#{email},#{phoneNumber},#{password},#{identity})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id") //将自动生成的主键Id，设置为对象里面的id，做一个匹配
    void insert(UserDO userDO);


    @Select("select * from user where email=#{email}")
    UserDO selectByMail(@NotBlank(message = "手机或者邮箱不能为空！") @Param("email") String email);


    @Select("select * from user where phone_number=#{phoneNumber}")
    UserDO selectByPhone(@Param("phoneNumber") Encrypt phoneNumber);


    @Select("<script>"+
            " select * from user"+
            " <if test=\"identity!=null\">"+
            " where identity=#{identity}"+
            " </if>"+
            " order by id desc"+
            " </script>")
    List<UserDO> selectByIdentityUserLIst(@Param("identity") String identity);

//    1 2 3 -> 1 2

    @Select("<script>" +
            " select id from user" +
            " where id in  " +
            "<foreach item='item' collection='items' open='(' separator=',' close=')'>"+
            " #{item}"+
            " </foreach>"+
            " </script>")
    List<Long> selectExistByIds(@Param("items") List<Long> ids);

    @Select("select * from user where identity='NORMAL' order by id desc ")
    List<UserDO> selectNormalByIdentityUserLIst();
}

