package org.mon.gobang.game;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.mon.gobang.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

//这个类表示匹配器，通过这个类负责完成整个匹配功能
@Slf4j
@Component
public class Matcher {
//    创建三分匹配队列
    private Queue<User> normalQueue=new LinkedList<>();
    private Queue<User> highQueue=new LinkedList<>();
    private Queue<User> veryHighQueue=new LinkedList<>();

    @Autowired
    private OnlineUserManager onlineUserManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoomManager roomManager;

//    操作匹配队列
    public void add(User user){
        if(user.getScore()<2000){
            synchronized (normalQueue){
                normalQueue.offer(user);
                normalQueue.notify();
            }
            log.info("把玩家:{},加入到normalQueue中",user.getUsername());
        }else if(user.getScore()<3000){
            synchronized (highQueue){
                highQueue.offer(user);
                highQueue.notify();
            }
            log.info("把玩家:{},加入到highQueue中",user.getUsername());
        }else{
            synchronized (veryHighQueue){
                veryHighQueue.offer(user);
                veryHighQueue.notify();
            }
            log.info("把玩家:{},加入到veryHighQueue中",user.getUsername());
        }
    }

//    停止匹配
    public void remove(User user){
        if(user.getScore()<2000){
            synchronized (normalQueue){
                normalQueue.remove(user);
            }
            log.info("把玩家:{}移除队列:normalQueue",user.getUsername());
        }else if(user.getScore()<3000){
            synchronized (highQueue) {
                highQueue.remove(user);
            }
            log.info("把玩家:{}移除队列:highQueue",user.getUsername());
        }else{
            synchronized (veryHighQueue){
                veryHighQueue.remove(user);
            }
            log.info("把玩家:{}移除队列:veryHighQueue",user.getUsername());
        }
    }

    public Matcher(){
//        创建三个线程，分别针对这三个匹配队列进行操作
        Thread t1=new Thread(){
            @Override
            public void run() {
                while(true){
                    handlerMatch(normalQueue);
                }
            }
        };
        t1.start();
        Thread t2=new Thread(){
            @Override
            public void run() {
                while(true){
                    handlerMatch(highQueue);
                }
            }
        };
        t2.start();
        Thread t3=new Thread(){
            @Override
            public void run() {
                while(true){
                    handlerMatch(veryHighQueue);
                }
            }
        };
        t3.start();
    }

    private void handlerMatch(Queue<User> matchQueue) {
        synchronized (matchQueue){
            try{
                //        先判断
                while(matchQueue.size()<2){
                    matchQueue.wait();
                }
                System.out.println(matchQueue.size());
                User player1=matchQueue.poll();
                User player2=matchQueue.poll();
                System.out.println("匹配出两个玩家:"+player1.getUsername()+","+player2.getUsername());

//        获取到玩家的websocket，为了告诉玩家排到了
                WebSocketSession session1 = onlineUserManager.getFromGameHall(player1.getUserId());
                WebSocketSession session2 = onlineUserManager.getFromGameHall(player2.getUserId());

                if(session1==null){
//            理论上是在线，但是最好还是再判断一下
//            如果玩家1不完了，再把玩家二重新加回到匹配队中
                    matchQueue.offer(player2);
                    return ;
                }
                if(session2==null){
                    matchQueue.offer(player1);
                    return ;
                }

//        理论上也不会存在
//        如果玩家下线，就会移除匹配队列
                if(session1==session2){
                    matchQueue.offer(player2);
                    return ;
                }


//        把这两个玩家，放入到一个游戏房间中
//        TODO 游戏房间
                Room room=new Room();
                roomManager.add(room, player1.getUserId() ,player2.getUserId());

//        给玩家反馈信息：匹配到对手了
//        通过websocket返回一个message为matchSuccess这样的响应
                MatchResponse response1=new MatchResponse();
                response1.setOk(true);
                response1.setMessage("matchSuccess");
                session1.sendMessage(new TextMessage(objectMapper.writeValueAsString(response1)));

                MatchResponse response2=new MatchResponse();
                response2.setOk(true);
                response2.setMessage("matchSuccess");
                session2.sendMessage(new TextMessage(objectMapper.writeValueAsString(response2)));

            }catch (IOException e){
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
