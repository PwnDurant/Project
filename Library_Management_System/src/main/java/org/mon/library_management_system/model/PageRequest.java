package org.mon.library_management_system.model;


import lombok.Data;

@Data
public class PageRequest {
//    关于偏移量的一些信息
    private Integer currentPage=1;
    private Integer pageSize=10;

    private Integer offset;

    public Integer getOffset(){
        return (currentPage-1)*pageSize;
    }
}
