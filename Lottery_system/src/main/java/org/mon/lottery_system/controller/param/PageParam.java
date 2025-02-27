package org.mon.lottery_system.controller.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageParam implements Serializable {
//    当前页
    private Integer currentPage=1;
//    当前页数量
    private Integer pageSize=10;

    public Integer offset(){
        return (currentPage-1)*pageSize;
    }

}
