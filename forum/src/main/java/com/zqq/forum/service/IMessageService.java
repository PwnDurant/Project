package com.zqq.forum.service;

import com.zqq.forum.model.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IMessageService {

    /**
     * 发送站内信
     * @param message
     */
    void create (Message message);

    /**
     * 根据用户Id查询信息数量
     * @param receiveUserId
     * @return
     */
    Integer selectCountById(Long receiveUserId);


    /**
     * 根据接收者用户Id查询所有站内信
     * @param receiveUserId
     * @return
     */
    List<Message> selectByReceiveId (Long receiveUserId);

    /**
     * 更新指定站内信的状态
     * @param id
     * @param state
     */
    void updateStateById(Long id,Byte state);


    /**
     * 根据Id查询信息
     * @param id
     * @return
     */
    Message selectById(Long id);


    /**
     * 回复站内信
     * @param repliedId 要回复的站内信id
     * @param message 要回复的对象
     */
    @Transactional
    void reply(Long repliedId,Message message);
}
