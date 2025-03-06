package org.mon.gobang.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.mon.gobang.game.MatchRequest;
import org.mon.gobang.game.MatchResponse;
import org.mon.gobang.game.Matcher;
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

    @Autowired
    private Matcher matcher;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        玩家上线，加入到onlineUserManager中
//        1,获取到当前用户的身份信息（谁在游戏大厅中建立连接）
//        这个可以把http中的Attribute中拿过来
//        这里的user有可能为空，直接通过/game_hall.html

        try{
            User user=(User)session.getAttributes().get("user");

//            先判断当前用户是否是在线状态，如果是，就不应该进行后续操作
            WebSocketSession webSocketSession=onlineUserManager.getFromGameHall(user.getUserId());
            if(webSocketSession!=null){
//                当前用户已经登入
//                告诉客户端不能重复登入
                MatchResponse response=new MatchResponse();
                response.setOk(false);
                response.setReason("禁止多开！");
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
                session.close();
                return ;
            }

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

//        实现处理开始匹配请求，和停止匹配请求
        User user=(User) session.getAttributes().get("user");
//        获取到客户端给服务器发送数据
        String payload= message.getPayload();
//        当前是一个json，要转换为java对象
        MatchRequest matchRequest=objectMapper.readValue(payload, MatchRequest.class);
        MatchResponse matchResponse=new MatchResponse();
        if(matchRequest.getMessage().equals("startMatch")){
//            进入匹配队列
//            TODO
            matcher.add(user);
//            把玩家信息放入匹配队列之后，就返回一个响应给客户端
            matchResponse.setOk(true);
            matchResponse.setMessage("startMatch");
        } else if (matchRequest.getMessage().equals("stopMatch")) {
//            退出匹配队列
//            TODO
            matcher.remove(user);
//            把当前用户从队列移除
            matchResponse.setOk(true);
            matchResponse.setMessage("stopMatch");
        }else{
//            非法情况
            matchResponse.setOk(false);
            matchResponse.setReason("非法匹配请求！");
        }
//        服务器在处理匹配请求的时候，应该要返回一个请求
        String jsonString=objectMapper.writeValueAsString(matchResponse);
        session.sendMessage(new TextMessage(jsonString));

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        try{
            //        玩家下线，从onlineUserManager删除
            User user=(User)session.getAttributes().get("user");
            WebSocketSession webSocketSession=onlineUserManager.getFromGameHall(user.getUserId());
            if(webSocketSession==session){
                onlineUserManager.exitGameHall(user.getUserId());
            }
            matcher.remove(user);
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
            WebSocketSession webSocketSession=onlineUserManager.getFromGameHall(user.getUserId());
            if(webSocketSession==session){
                onlineUserManager.exitGameHall(user.getUserId());
            }
            matcher.remove(user);
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
