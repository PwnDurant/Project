package org.mon.gobang.model;


import lombok.Data;

@Data
public class User {

    private int userId;

    private String username;

    private String password;

    private int score;

    private int totalCount;

    private int winCount;

}
