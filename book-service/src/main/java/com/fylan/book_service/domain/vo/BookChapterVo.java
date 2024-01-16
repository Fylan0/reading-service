package com.fylan.book_service.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "图书章节实体")
public class BookChapterVo {

    @ApiModelProperty("图书ID")
    private String bookId;
    @ApiModelProperty("章节名称")
    private String chapterName;
    @ApiModelProperty("第几章")
    private String chapterNumber;
    @ApiModelProperty("章节内容")
    private String chapterContent;

}
