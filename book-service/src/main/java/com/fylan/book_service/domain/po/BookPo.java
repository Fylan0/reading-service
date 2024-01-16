package com.fylan.book_service.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Persistent Object（持久化对象）
 * PO:对应数据库表字段
 */
@Data
@TableName("tb_book")
public class BookPo {

    @TableId("book_id")
    private String bookId;
    @TableField("book_name")
    private String bookName;
    @TableField("book_content")
    private String bookContent;
    @TableField("book_author")
    private String bookAuthor;
    @TableField("book_shortIntro")
    private String bookShortIntro;
    @TableField("book_cover")
    private String bookCover;

}
