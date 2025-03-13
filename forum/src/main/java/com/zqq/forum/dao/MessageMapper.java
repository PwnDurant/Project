package com.zqq.forum.dao;

import com.zqq.forum.model.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    int insert(Message row);

    int insertSelective(Message row);

    Message selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Message row);

    int updateByPrimaryKey(Message row);

    /**
     * 根据用户Id查询信息数量
     * @param receiveUserId
     * @return
     */
    Integer selectCountById(@Param("receiveUserId") Long receiveUserId);


    /**
     * 根据接收者用户Id查询所有站内信
     * @param receiveUserId
     * @return
     */
    List<Message> selectByReceiveId (@Param("receiveUserId") Long receiveUserId);


}