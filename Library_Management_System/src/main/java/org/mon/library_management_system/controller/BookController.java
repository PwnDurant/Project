package org.mon.library_management_system.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.mon.library_management_system.model.BookInfo;
import org.mon.library_management_system.model.PageRequest;
import org.mon.library_management_system.model.PageResponse;
import org.mon.library_management_system.model.Result;
import org.mon.library_management_system.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;


//   添加图书信息
    @RequestMapping(value = "/addBook",produces = "application/json")
    public String addBook(BookInfo bookInfo){
        log.info("添加图书：bookInfo:{}",bookInfo);
//        参数校验
        if(!StringUtils.hasLength(bookInfo.getBookName())
        ||!StringUtils.hasLength(bookInfo.getAuthor())
        ||bookInfo.getCount()==null
        ||bookInfo.getPrice()==null
        ||!StringUtils.hasLength(bookInfo.getPublish())){
            return "参数不合法，请重新校验";
        }
        try{
//            插入数据
            Integer result=bookService.insertBook(bookInfo);
            if(result==1){
                return "";
            }
        }catch (Exception e){
            log.error("数据插入发生异常，e：",e);
        }
        return "数据插入失败，请联系管理员";
    }

//    查询图书信息（翻页使用）
    @RequestMapping("/getListByPage")
    public Result<PageResponse<BookInfo>> getListByPage(PageRequest pageRequest, HttpServletRequest request){
        log.info("获取图书列表，pageRequest：{}",pageRequest);
//        参数校验省略

//        实例化一个响应对象
        PageResponse<BookInfo> bookInfoPageResponse=new PageResponse<>();
        try{
//            尝试通过在请求中获取偏移量从而返回对应的书本信息
            bookInfoPageResponse=bookService.getListByPage(pageRequest);
        }catch (Exception e){
            log.error("获取图书列表失败");
            return Result.fail();
        }
        return Result.success(bookInfoPageResponse);
    }

}
