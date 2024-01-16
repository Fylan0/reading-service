package com.fylan.book_service;

import com.fylan.book_service.controller.BookChapterController;
import com.fylan.book_service.controller.BookController;
import com.fylan.book_service.domain.vo.BookChapterVo;
import com.fylan.book_service.domain.vo.BookVo;
import com.fylan.book_service.service.IBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ServiceTest {

    @Autowired
    private IBookService bookService;

    @Autowired
    private BookController controller;


    @Autowired
    private BookChapterController chapterController;

    @Test
    public void test1() {
//        List<BookPo> list = bookService.list();
//        List<BookVo> bookVos = BeanUtil.copyToList(list, BookVo.class);
//        System.out.println("bookVos = " + bookVos);

        List<BookVo> bookList = controller.getBookList();
        System.out.println("bookVos = " + bookList);
    }


    @Test
    public void test2() {
//        List<BookPo> list = bookService.list();
//        List<BookVo> bookVos = BeanUtil.copyToList(list, BookVo.class);
//        System.out.println("bookVos = " + bookVos);

        List<BookChapterVo> bookList = chapterController.getBookChapter("123456");
        System.out.println("BookChapterVo = " + bookList);
    }
}
