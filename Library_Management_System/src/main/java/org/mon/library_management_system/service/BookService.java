package org.mon.library_management_system.service;


import org.mon.library_management_system.enums.BookStatusEnum;
import org.mon.library_management_system.mapper.BookMapper;
import org.mon.library_management_system.model.BookInfo;
import org.mon.library_management_system.model.PageRequest;
import org.mon.library_management_system.model.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookMapper bookMapper;

    public Integer insertBook(BookInfo bookInfo) {
        return bookMapper.insertBook(bookInfo);
    }


    public PageResponse<BookInfo> getListByPage(PageRequest pageRequest) {
//        1,总记录数
        Integer count=bookMapper.count();
//        2,当前页的记录
        List<BookInfo> bookInfos=bookMapper.getBookByPage(pageRequest);
//        给每一本书设置相应的状态码
        for(BookInfo bookInfo:bookInfos){
            bookInfo.setStatusCN(BookStatusEnum.getStatusByCode(bookInfo.getStatus()).getDesc());
        }
//        返回响应对象
        return new PageResponse<BookInfo>(count,bookInfos,pageRequest);
    }
}
