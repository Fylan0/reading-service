package com.fylan.book_service.controller;

import cn.hutool.core.bean.BeanUtil;
import com.fylan.book_service.domain.po.BookPo;
import com.fylan.book_service.domain.vo.BookVo;
import com.fylan.book_service.service.IBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "书籍管理相关接口")
@RequestMapping("/book")
@RestController
@RequiredArgsConstructor
public class BookController {

    private final IBookService bookService;

    @ApiOperation("获取书籍列表")
    @GetMapping()
    public List<BookVo> getBookList() {
        List<BookPo> list = bookService.list();
        return BeanUtil.copyToList(list, BookVo.class);

    }

}
