package org.mon.library_management_system.model;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BookInfo {
    private Integer id;
    private String bookName;
    private String author;
    private Integer count;
    private BigDecimal price;
    private String publish;
    private Integer status;
    private String statusCN;
    private Date createTime;
    private Date updateTime;
}
