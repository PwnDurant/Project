package org.mon.gobang.game;

import lombok.Data;

@Data
public class MatchResponse {

    private Boolean ok;

    private String reason;

    private String message;
}
