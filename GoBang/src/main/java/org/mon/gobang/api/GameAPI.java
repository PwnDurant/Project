package org.mon.gobang.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mon.gobang.game.*;
import org.mon.gobang.model.User;
import org.mon.gobang.model.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;


@Component
public class GameAPI extends TextWebSocketHandler {

    private ObjectMapper objectMapper=new ObjectMapper();

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OnlineUserManager onlineUserManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        GameReadyResponse response= new GameReadyResponse();
//        1,先获取用户的身份信息（从Httpsession拿到当前用户对象）
        User user = (User)session.getAttributes().get("user");
        if(user==null) {
            response.setOk(false);
            response.setReason("用户尚未登入");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
            return;
        }

//        2,判断当前用户是否已经进入房间（拿着房间管理器进行查询）
        Room room = roomManager.getRoomByUserId(user.getUserId());
        if(room==null){
//            改玩家还没有匹配到
            response.setOk(false);
            response.setReason("用户尚为匹配到！");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsBytes(response)));
        }

//        判断当前是否是多开的，该用户是不是已经在其它地方进行游戏了
        if(onlineUserManager.getFromGameHall(user.getUserId())!=null
                ||onlineUserManager.getFromGameRoom(user.getUserId())!=null){
//            如果一个账号一边在游戏大厅，一边在游戏房间，也是多开
            response.setOk(true);
            response.setReason("禁止多开");
            response.setMessage("repeat");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
            return ;
        }
//        4,设置玩家上线
        onlineUserManager.enterGameRoom(user.getUserId(),session);

//        5,把两个玩家加入到游戏房间中
//        当前这个逻辑是在game_room.html页面加载的时候进行
        synchronized (room){
            if(room.getUser1()==null){
//            第一个玩家还尚未进入房间
//            就把当前连上websocket的玩家作为user1，加入到房间中
                room.setUser1(user);
                room.setWhiteUser(user.getUserId());
                System.out.println("玩家"+user.getUsername()+"已经准备就绪，作为1");
                return;
            }
            if(room.getUser2()==null){
                room.setUser2(user);
                System.out.println("玩家"+user.getUsername()+"已经准备就绪，作为2");
//            当两个玩家都加入成功之后，就让服务器都返回websocket的

                noticeGameReady(room,room.getUser1(),room.getUser2());

                noticeGameReady(room,room.getUser2(),room.getUser1());
                return ;
            }
        }
//        如果又有一个玩家连接
        response.setOk(false);
        response.setReason("当前房间满了");
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
    }

    private void noticeGameReady(Room room, User thisUser, User thatUser) throws IOException {
        GameReadyResponse gameReadyResponse=new GameReadyResponse();
        gameReadyResponse.setOk(true);
        gameReadyResponse.setReason("");
        gameReadyResponse.setMessage("gameReady");
        gameReadyResponse.setRoomId(room.getRoomId());
        gameReadyResponse.setThisUserId(thisUser.getUserId());
        gameReadyResponse.setThatUserId(thatUser.getUserId());
        gameReadyResponse.setWhiteUser(room.getWhiteUser());
        // 把当前的响应数据传回给玩家.
        WebSocketSession webSocketSession = onlineUserManager.getFromGameRoom(thisUser.getUserId());
        webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(gameReadyResponse)));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        User user=(User) session.getAttributes().get("user");
        if(user==null){
//            此处就简单处理，在断开连接的时候就不给客户端返回响应了
            return ;
        }
        WebSocketSession exitSession = onlineUserManager.getFromGameRoom(user.getUserId());
        if(exitSession==session){
            onlineUserManager.exitGameRoom(user.getUserId());
        }
        System.out.println("当前用户"+user.getUserId()+"离开房间");
        //        通知对手获胜
        noticeThatUserWin(user);

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        User user=(User) session.getAttributes().get("user");
        if(user==null){
//            此处就简单处理，在断开连接的时候就不给客户端返回响应了
            return ;
        }
        WebSocketSession exitSession = onlineUserManager.getFromGameRoom(user.getUserId());
        if(exitSession==session){
            onlineUserManager.exitGameRoom(user.getUserId());
        }
        System.out.println("当前用户"+user.getUserId()+"房间连接异常");
//        通知对手获胜
        noticeThatUserWin(user);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//先从session中拿到当前的用户对象
        User user=(User) session.getAttributes().get("user");
        if(user==null){
            System.out.println("当前玩家尚未登入");
            return ;
        }
//        2,根据玩家Id获得房间对象
        Room room=roomManager.getRoomByUserId(user.getUserId());
//        3,通过room对象来处理这次具体的请求
        room.putChess(message.getPayload());

    }

    private void noticeThatUserWin(User user) throws IOException {
        // 1. 根据当前玩家, 找到玩家所在的房间
        Room room = roomManager.getRoomByUserId(user.getUserId());
        if (room == null) {
            // 这个情况意味着房间已经被释放了, 也就没有 "对手" 了
            System.out.println("当前房间已经释放, 无需通知对手!");
            return;
        }

        // 2. 根据房间找到对手
        User thatUser = (user == room.getUser1()) ? room.getUser2() : room.getUser1();
        // 3. 找到对手的在线状态
        WebSocketSession webSocketSession = onlineUserManager.getFromGameRoom(thatUser.getUserId());
        if (webSocketSession == null) {
            // 这就意味着对手也掉线了!
            System.out.println("对手也已经掉线了, 无需通知!");
            return;
        }
        // 4. 构造一个响应, 来通知对手, 你是获胜方
        GameResponse resp = new GameResponse();
        resp.setMessage("putChess");
        resp.setUserId(thatUser.getUserId());
        resp.setWinner(thatUser.getUserId());
        webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(resp)));

//        更新信息
        userMapper.userWin(thatUser.getUserId());
        userMapper.userLose(user.getUserId());

//        释放房间对象
        roomManager.del(room.getRoomId(),room.getUser1().getUserId(),room.getUser2().getUserId());

    }
}
