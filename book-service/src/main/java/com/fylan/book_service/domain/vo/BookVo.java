package com.fylan.book_service.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Value Object（值对象）
 * vo: 接口返回值
 */
@Data
@ApiModel(description = "图书实体")
public class BookVo {

    @ApiModelProperty("图书ID")
    private String bookId;
    @ApiModelProperty("图书名称")
    private String bookName;
    @ApiModelProperty("图书内容")
    private String bookContent;
    @ApiModelProperty("图书作者")
    private String bookAuthor;
    @ApiModelProperty("图书简介")
    private String bookShortIntro;
    @ApiModelProperty("图书封面")
    private String bookCover;

}
