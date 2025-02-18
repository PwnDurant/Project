package org.mon.library_management_system.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.mon.library_management_system.model.BookInfo;
import org.mon.library_management_system.model.PageRequest;
import org.mon.library_management_system.model.PageResponse;

import java.util.List;

@Mapper
public interface BookMapper {

    @Insert("insert into book_info (book_name, author, count, price, publish,status) "+
    "values (#{bookName},#{author},#{count},#{price},#{publish},#{status})")
    Integer insertBook(BookInfo bookInfo);

    @Select("select count(*) from book_info where status != 0;")
    Integer count();

    @Select("select * from book_info where status!=0 order by id limit #{offset},#{pageSize}")
    List<BookInfo> getBookByPage(PageRequest pageRequest);

    @Select("select * from book_info where status!=0 and id=#{bookId}")
    BookInfo queryBookById(Integer bookId);

    Integer updateBook(BookInfo bookInfo);

    void batchDelete(List<Integer> ids);


}
