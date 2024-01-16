package com.fylan.book_service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fylan.book_service.domain.po.BookPo;
import com.fylan.book_service.mapper.BookMapper;
import com.fylan.book_service.service.IBookService;
import org.springframework.stereotype.Service;

@Service
public class BookService extends ServiceImpl<BookMapper, BookPo> implements IBookService {


}
