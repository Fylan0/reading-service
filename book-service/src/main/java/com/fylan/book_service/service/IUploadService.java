package com.fylan.book_service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IUploadService {
    void upload(MultipartFile multipartFile) throws IOException;
}
