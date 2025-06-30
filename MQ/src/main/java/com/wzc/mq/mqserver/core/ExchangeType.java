package com.wzc.mq.mqserver.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExchangeType {

    DIRECT(0),
    FANOUT(1),
    TOPIC(2);

    private final int type;

}
