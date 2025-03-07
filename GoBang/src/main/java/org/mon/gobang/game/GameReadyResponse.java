package org.mon.gobang.game;

import lombok.Data;

@Data
public class GameReadyResponse {
    private String message;
    private Boolean ok;
    private String reason;
    private String roomId;
    private int thisUserId;
    private int thatUserId;
    private int whiteUser;
}
