package com.fylan.book_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fylan.book_service.domain.po.BookChapter;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BookChapterMapper extends BaseMapper<BookChapter> {

    @Select("select * from tb_book_chapter where book_id = #{bookId}")
    public List<BookChapter> getBookChapter(String bookId);

}
