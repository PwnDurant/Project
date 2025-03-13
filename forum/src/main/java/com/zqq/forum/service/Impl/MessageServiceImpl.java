package com.zqq.forum.service.Impl;

import com.zqq.forum.common.AppResult;
import com.zqq.forum.common.ResultCode;
import com.zqq.forum.dao.MessageMapper;
import com.zqq.forum.exception.ApplicationException;
import com.zqq.forum.model.Message;
import com.zqq.forum.model.User;
import com.zqq.forum.service.IMessageService;
import com.zqq.forum.service.IUserService;
import com.zqq.forum.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class MessageServiceImpl implements IMessageService {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private IUserService userService;

    @Override
    public void create(Message message) {
        if(message==null||message.getPostUserId()==null||message.getReceiveUserId()==null
        || StringUtil.isEmpty(message.getContent())){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        User user = userService.selectById(message.getReceiveUserId());
        if(user==null||user.getDeleteState()==1){
            log.warn(ResultCode.FAILED_USER_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }

        message.setState((byte)0);
        message.setDeleteState((byte)0);
        message.setCreateTime(new Date());
        message.setUpdateTime(new Date());

        int row = messageMapper.insertSelective(message);
        if(row!=1){
            log.warn(ResultCode.ERROR_SERVICES.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }
    }

    @Override
    public Integer selectCountById(Long receiveUserId) {

        if(receiveUserId==null||receiveUserId<=0) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        Integer row = messageMapper.selectCountById(receiveUserId);
        if(row==null){
            log.warn(ResultCode.ERROR_SERVICES.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }

        return row;

    }

    @Override
    public List<Message> selectByReceiveId(Long receiveUserId) {

        if(receiveUserId==null||receiveUserId<=0) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        return  messageMapper.selectByReceiveId(receiveUserId);
    }

    @Override
    public void updateStateById(Long id, Byte state) {

        if(id==null||id<=0||state<0||state>2) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        Message updateMessage=new Message();
        updateMessage.setId(id);
        updateMessage.setState(state);
        updateMessage.setUpdateTime(new Date());

        int row = messageMapper.updateByPrimaryKeySelective(updateMessage);
        if(row!=1){
            log.warn(ResultCode.ERROR_SERVICES.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }

    }

    @Override
    public Message selectById(Long id) {

        if(id==null||id<=0) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        return messageMapper.selectByPrimaryKey(id);

    }

    @Override
    public void reply(Long repliedId, Message message) {

        if(repliedId==null||repliedId<=0) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        Message existsMessage = messageMapper.selectByPrimaryKey(repliedId);
        if(existsMessage==null||existsMessage.getDeleteState()==1){
            log.warn(ResultCode.FAILED_MESSAGE_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_MESSAGE_NOT_EXISTS));
        }

        updateStateById(repliedId,(byte)2);

        create(message);

    }

}

