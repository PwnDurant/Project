package org.mon.gobang.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.mon.gobang.game.MatchResponse;
import org.mon.gobang.game.OnlineUserManager;
import org.mon.gobang.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * 通过这个类来处理匹配功能中的websocket请求
 */
@Slf4j
@Component
public class MatchAPI extends TextWebSocketHandler {

    private ObjectMapper objectMapper=new ObjectMapper();

    @Autowired
    private OnlineUserManager onlineUserManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        玩家上线，加入到onlineUserManager中
//        1,获取到当前用户的身份信息（谁在游戏大厅中建立连接）
//        这个可以把http中的Attribute中拿过来
//        这里的user有可能为空，直接通过/game_hall.html

        try{
            User user=(User)session.getAttributes().get("user");
//        把玩家设置为在线状态
            onlineUserManager.enterGameHall(user.getUserId(),session);
            log.info("玩家:{},进入游戏大厅",user.getUsername());
        }catch (NullPointerException e){
//            表示未登入
            e.printStackTrace();
            MatchResponse matchResponse=new MatchResponse();
            matchResponse.setOk(false);
            matchResponse.setReason("您尚未登入，不能进行后续匹配！");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(matchResponse)));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        try{
            //        玩家下线，从onlineUserManager删除
            User user=(User)session.getAttributes().get("user");
            onlineUserManager.exitGameHall(user.getUserId());
        }catch (NullPointerException e){
            //            表示未登入
            e.printStackTrace();
            MatchResponse matchResponse=new MatchResponse();
            matchResponse.setOk(false);
            matchResponse.setReason("您尚未登入，不能进行后续匹配！");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(matchResponse)));
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        try{
            //        玩家下线，从onlineUserManager删除
            User user=(User)session.getAttributes().get("user");
            onlineUserManager.exitGameHall(user.getUserId());
        }catch (NullPointerException e){

            //            表示未登入
            e.printStackTrace();
            MatchResponse matchResponse=new MatchResponse();
            matchResponse.setOk(false);
            matchResponse.setReason("您尚未登入，不能进行后续匹配！");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(matchResponse)));
        }
    }
}
