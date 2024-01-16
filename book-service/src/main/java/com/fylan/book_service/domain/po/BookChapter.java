package com.fylan.book_service.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_book_chapter")
public class BookChapter {

    @TableId("serial")
    private int serial;
    @TableField("book_id")
    private String bookId;
    @TableField("chapter_name")
    private String chapterName;
    @TableField("chapter_number")
    private String chapterNumber;
    @TableField("chapter_content")
    private String chapterContent;


}
