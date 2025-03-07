package org.mon.gobang.game;


import lombok.Data;

@Data
public class GameResponse {
    private String message;
    private int userId;
    private int row;
    private int col;
    private int winner;

}
