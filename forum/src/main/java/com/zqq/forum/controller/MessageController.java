package com.zqq.forum.controller;


import com.zqq.forum.common.AppConfig;
import com.zqq.forum.common.AppResult;
import com.zqq.forum.common.ResultCode;
import com.zqq.forum.exception.ApplicationException;
import com.zqq.forum.model.Message;
import com.zqq.forum.model.User;
import com.zqq.forum.service.IMessageService;
import com.zqq.forum.service.IUserService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private IMessageService messageService;
    @Autowired
    private IUserService userService;

    @PostMapping("/send")
    public AppResult send(HttpServletRequest request,
                          @RequestParam("receiveUserId")@Nonnull Long receiveUserId,
                          @RequestParam("content")@Nonnull String content){

        HttpSession session= request.getSession(false);
        User user=(User)session.getAttribute(AppConfig.USER_SESSION);

        if(user.getState()==1){
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }

        if(Objects.equals(user.getId(), receiveUserId)){
            return AppResult.failed("不能给自己发送站内信");
        }

        User receiveUser = userService.selectById(receiveUserId);
        if(receiveUser==null||receiveUser.getDeleteState()==1){
            return AppResult.failed("接收者状态异常");
        }

        Message message=new Message();
        message.setPostUserId(user.getId());
        message.setReceiveUserId(receiveUserId);
        message.setContent(content);

        messageService.create(message);

        return AppResult.success("发送成功");
    }

    @GetMapping("/getUnreadCount")
    public AppResult<Integer> getUnreadCount(HttpServletRequest request){

        HttpSession session= request.getSession(false);
        User user=(User)session.getAttribute(AppConfig.USER_SESSION);

        Integer count = messageService.selectCountById(user.getId());

        return AppResult.success(count);
    }

    @GetMapping("/getAll")
    public AppResult<List<Message>> getAll(HttpServletRequest request){

        HttpSession session= request.getSession(false);
        User user=(User)session.getAttribute(AppConfig.USER_SESSION);

        List<Message> messages = messageService.selectByReceiveId(user.getId());
        return  AppResult.success(messages);

    }

    @PostMapping("/markRead")
    public AppResult markRead(HttpServletRequest request,
                              @RequestParam("id") @Nonnull Long id){

        Message message=messageService.selectById(id);
        if(message==null||message.getDeleteState()==1){
            log.warn(ResultCode.FAILED_MESSAGE_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_MESSAGE_NOT_EXISTS));
        }

        HttpSession session= request.getSession(false);
        User user=(User)session.getAttribute(AppConfig.USER_SESSION);

        if(!Objects.equals(message.getReceiveUserId(), user.getId())){
            return AppResult.failed(ResultCode.FAILED_FORBIDDEN);
        }

        messageService.updateStateById(id,(byte)1);

        return AppResult.success();
    }


    /**
     * 回复站内信
     * @param request
     * @param repliedId
     * @param content
     * @return
     */
    @PostMapping("/reply")
    public AppResult reply(HttpServletRequest request,
                           @RequestParam("repliedId")@Nonnull Long repliedId,
                           @RequestParam("content")@Nonnull String content){

        Message existsMessage = messageService.selectById(repliedId);
        if(existsMessage==null||existsMessage.getDeleteState()==1){
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_MESSAGE_NOT_EXISTS));
        }

        HttpSession session= request.getSession(false);
        User user=(User)session.getAttribute(AppConfig.USER_SESSION);

        if(user.getState()==1){
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_BANNED));
        }

        if(Objects.equals(user.getId(), existsMessage.getPostUserId())){
            return AppResult.failed("不能自己回复自己");
        }

        Message message=new Message();
        message.setPostUserId(user.getId());
        message.setReceiveUserId(existsMessage.getPostUserId());
        message.setContent(content);

        messageService.reply(repliedId,message);

        return AppResult.success();
    }




}
