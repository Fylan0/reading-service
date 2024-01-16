package com.fylan.book_service.controller;

import com.fylan.book_service.domain.po.BookChapter;
import com.fylan.book_service.domain.vo.BookChapterVo;
import com.fylan.book_service.service.IBookChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "书籍章节相关接口")
@RequestMapping("/book/chapter")
@RestController
@RequiredArgsConstructor
public class BookChapterController {

    private final IBookChapterService bookChapterService;

    @ApiModelProperty("根据图书ID获取章节列表")
    @GetMapping("{book_id}")
    public List<BookChapterVo> getBookChapter(@ApiParam("图书ID") @PathVariable("book_id") String bookId) {
        return bookChapterService.getBookChapter(bookId);
    }

}
