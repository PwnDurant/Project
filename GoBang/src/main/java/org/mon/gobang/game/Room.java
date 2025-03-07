package org.mon.gobang.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.mon.gobang.GoBangApplication;
import org.mon.gobang.model.User;
import org.mon.gobang.model.UserMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.io.IOException;
import java.util.UUID;


@Getter
@Setter
public class Room {

    private String roomId;

    private int whiteUser;

    private User user1;
    private User user2;

    private static final int MAX_ROW=15;
    private static final int MAX_COL=15;

    public Room(){
        this.roomId= UUID.randomUUID().toString();
//        通过在入口类的context手动获取
        onlineUserManager= GoBangApplication.context.getBean(OnlineUserManager.class);
        roomManager=GoBangApplication.context.getBean(RoomManager.class);
        userMapper=GoBangApplication.context.getBean(UserMapper.class);
    }


    public static void main(String[] args) {
        Room room=new Room();
        System.out.println(room.roomId);
    }

//    约定使用0表示当前位置未落子，初始化好的二维数组就是全0
//    使用1表示user1落子位置，
//    使用2表示user2落子位置
    private int[][] board=new int[MAX_ROW][MAX_COL];

//通过这个方法处理一次落子操作
    private ObjectMapper objectMapper=new ObjectMapper();

//    引入
//    @Resource(name = "onlineUserManager")
    private OnlineUserManager onlineUserManager;

//    @Resource(name = "roomManager")
    private RoomManager roomManager;

    private UserMapper userMapper;

    public void putChess(String jsonString) throws IOException {
//    记录当前落子的位置
        GameRequest request=objectMapper.readValue(jsonString, GameRequest.class);
        GameResponse response=new GameResponse();
//        当前这个子是玩家1落的还是玩家2落的，根据是玩家1还是玩家2决定往数组中写入
        int chess=request.getUserId()==user1.getUserId()?1:2;
        int row=request.getRow();
        int col= request.getCol();
        if(board[row][col]!=0){
//            但是客户端已经判定过了，为了程序更急稳定，再次判断
            System.out.println("当前位置已经有子了");
        }
        board[row][col]=chess;
//        打印出棋盘信息，方便观察局势
        printBoard();
//    进行胜负判定
        int winner=checkWinner(row,col,chess);
//        给房间里的所有客户端返回数据
        response.setMessage("putChess");
        response.setUserId(request.getUserId());
        response.setRow(row);
        response.setCol(col);
        response.setWinner(winner);

//        要想给用户发送websocket数据，就需要获取到用户的websocketSession

        WebSocketSession session1 = onlineUserManager.getFromGameRoom(user1.getUserId());
        WebSocketSession session2 = onlineUserManager.getFromGameRoom(user2.getUserId());

//        万一下线了
        if(session1==null){
            response.setWinner(user2.getUserId());
            System.out.println("玩家1下线");
        }
        if(session2==null){
            response.setWinner(user1.getUserId());
            System.out.println("玩家2下线");
        }

        String respJson= objectMapper.writeValueAsString(response);
        if(session1!=null){
            session1.sendMessage(new TextMessage(respJson));
        }
        if(session2!=null){
            session2.sendMessage(new TextMessage(respJson));
        }

//        如果胜负已分
        if(response.getWinner()!=0){
            System.out.println("开始销毁房间");

//            更新获胜和失败的信息
            int winUserId= response.getWinner();
            int loseUserId=response.getWinner()==user1.getUserId()?user2.getUserId():user1.getUserId();
            userMapper.userWin(winUserId);
            userMapper.userLose(loseUserId);

            roomManager.del(roomId,user1.getUserId(),user2.getUserId());
        }

    }

    /**
     * 打印棋盘
     */
    private void printBoard() {
        System.out.println("[打印棋盘信息]");
        System.out.println("==============================");
        for(int r=0;r<MAX_ROW;r++){
            for(int c=0;c<MAX_COL;c++){
                System.out.print(board[r][c]+" ");
            }
            System.out.println();
        }
        System.out.println("==============================");
    }

    //    TODO判断当前落子是否分出胜负,如果1获胜，返回1的userId，else返回2的userId，还没有返回0
    private int checkWinner(int row, int col,int chess) {
//        1，检查所有的行
        for(int c=col-4;c<=col;c++){
//            针对其中一种情况，来判定是不是连在一起了
//            不关是五个字得连着，还要是同一个玩家
            try{
                if(board[row][c]==chess
                        &&board[row][c+1]==chess
                        &&board[row][c+2]==chess
                        &&board[row][c+3]==chess
                        &&board[row][c+4]==chess){
//                构成了五子连珠
                    return chess==1?user1.getUserId():user2.getUserId();
                }
            }catch (ArrayIndexOutOfBoundsException e){
                continue;
            }
        }

//        判定所有列
        for(int r=row-4;r<=row;r++){
            try{
                if(board[r][col]==chess
                    &&board[r+1][col]==chess
                    &&board[r+2][col]==chess
                    &&board[r+3][col]==chess
                    &&board[r+4][col]==chess){
//                构成了五子连珠
                    return chess==1?user1.getUserId():user2.getUserId();
                }

            }catch (ArrayIndexOutOfBoundsException e){
                continue;
            }
        }

//        判定对角线情况
        for(int r=row-4,c=col-4;r<=row&&c<=col;r++,c++){
            try{
                if(board[r][c]==chess
                    &&board[r+1][c+1]==chess
                    &&board[r+2][c+2]==chess
                    &&board[r+3][c+3]==chess
                    &&board[r+4][c+4]==chess){
//                构成了五子连珠
                    return chess==1?user1.getUserId():user2.getUserId();}
            }catch (ArrayIndexOutOfBoundsException e){
                continue;
            }
        }

        for(int r=row-4,c=col+4;r<=row&&c>=col;r++,c--){
            try{
                if(board[r][c]==chess
                    &&board[r+1][c-1]==chess
                    &&board[r+2][c-2]==chess
                    &&board[r+3][c-3]==chess
                    &&board[r+4][c-4]==chess){
//                构成了五子连珠
                    return chess==1?user1.getUserId():user2.getUserId();}
            }catch (ArrayIndexOutOfBoundsException e){
                continue;
            }
        }
//        胜负未分，就直接返回0了

        return 0;
    }
}
