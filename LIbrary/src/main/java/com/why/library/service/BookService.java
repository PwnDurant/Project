package com.why.library.service;


import com.why.library.common.enums.BookStatusEnum;
import com.why.library.dao.BookInfo;
import com.why.library.dao.PageRequest;
import com.why.library.dao.PageResponse;
import com.why.library.mapper.BookMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BookService {

    @Autowired
    private BookMapper bookMapper;

    public Integer insertBook(BookInfo bookInfo) {
        return bookMapper.insertBook(bookInfo);
    }


    public PageResponse<BookInfo> getListByPage(PageRequest pageRequest) {
        System.out.println("service:"+pageRequest);
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

    public BookInfo queryBookById(Integer bookId) {
        log.info("接收到参数service:{}",bookId);
        return bookMapper.queryBookById(bookId);
    }

    public Integer updateBook(BookInfo bookInfo) {
        System.out.println("传入参数"+bookInfo.toString());
        return bookMapper.updateBook(bookInfo);
    }


    public void batchDelete(List<Integer> ids) {
         bookMapper.batchDelete(ids);
    }
}
