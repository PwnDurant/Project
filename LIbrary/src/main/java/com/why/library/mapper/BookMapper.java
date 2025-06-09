package com.why.library.mapper;



import com.why.library.dao.BookInfo;
import com.why.library.dao.PageRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
