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
    private int chapterNumber;
    @TableField("chapter_content")
    private String chapterContent = "";


    public int getSerial() {
        return serial;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getChapterContent() {
        return chapterContent;
    }

    public void setChapterContent(String chapterContent) {
        this.chapterContent = chapterContent;
    }

    @Override
    public String toString() {
        return "BookChapter{" +
                "serial=" + serial +
                ", bookId='" + bookId + '\'' +
                ", chapterName='" + chapterName + '\'' +
                ", chapterNumber='" + chapterNumber + '\'' +
                ", chapterContent='" + chapterContent + '\'' +
                '}';
    }
}
