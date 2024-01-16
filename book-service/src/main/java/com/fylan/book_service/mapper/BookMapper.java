package com.fylan.book_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fylan.book_service.domain.po.BookPo;
import com.fylan.book_service.domain.vo.BookVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BookMapper extends BaseMapper<BookPo> {
}
