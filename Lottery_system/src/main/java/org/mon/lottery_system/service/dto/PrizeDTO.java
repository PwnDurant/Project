package org.mon.lottery_system.service.dto;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class PrizeDTO {

    private Long prizeId;

    private String name;

    private String imageUrl;

    private BigDecimal price;

    private String description;
}
