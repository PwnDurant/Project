package org.mon.gobang.game;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;

@Component
public class OnlineUserManager {
//   这个哈希表就用来表示当前用户在游戏大厅中的在线状态
    private HashMap<Integer, WebSocketSession> gameHall=new HashMap<>();

    public void enterGameHall(int userId,WebSocketSession webSocketSession) {
        gameHall.put(userId, webSocketSession);
    }

    public void exitGameHall(int userId){
        gameHall.remove(userId);
    }

    public WebSocketSession getFromGameHall(int userId){
        return gameHall.get(userId);
    }

}
