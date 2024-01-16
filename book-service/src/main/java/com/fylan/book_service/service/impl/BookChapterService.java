package com.fylan.book_service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fylan.book_service.domain.po.BookChapter;
import com.fylan.book_service.domain.vo.BookChapterVo;
import com.fylan.book_service.mapper.BookChapterMapper;
import com.fylan.book_service.service.IBookChapterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookChapterService extends ServiceImpl<BookChapterMapper, BookChapter> implements IBookChapterService {

    @Override
    public List<BookChapterVo> getBookChapter(String bookId) {
        List<BookChapter> bookChapter = getBaseMapper().getBookChapter(bookId);
        return BeanUtil.copyToList(bookChapter, BookChapterVo.class);
    }

}
