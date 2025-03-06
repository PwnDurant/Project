package org.mon.gobang.game;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.mon.gobang.model.User;

import java.util.UUID;

@Getter
@Setter
public class Room {

    private String roomId;


    private User user1;
    private User user2;

    public Room(){
        this.roomId= UUID.randomUUID().toString();
    }


    public static void main(String[] args) {
        Room room=new Room();
        System.out.println(room.roomId);
    }
}
