package com.fylan.book_service.controller;

import com.fylan.book_service.service.IUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(tags = "上传接口")
@RequestMapping("/file")
@RestController
@RequiredArgsConstructor
public class UploadController {

    private final IUploadService uploadService;

    @ApiOperation("上传图书")
    @PostMapping("/upload")
    public void upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {

        uploadService.upload(multipartFile);
    }
}
