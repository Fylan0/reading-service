package com.fylan.book_service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fylan.book_service.domain.po.BookChapter;
import com.fylan.book_service.domain.vo.BookChapterVo;

import java.util.List;

public interface IBookChapterService extends IService<BookChapter> {
    List<BookChapterVo> getBookChapter(String bookId);
}
