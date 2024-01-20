package com.fylan.book_service.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.fylan.book_service.domain.po.BookChapter;
import com.fylan.book_service.domain.po.BookPo;
import com.fylan.book_service.service.IUploadService;
import com.fylan.book_service.utils.LocalPageLoaderUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class UploadService extends BookService implements IUploadService {
    @Override
    public void upload(MultipartFile multipartFile) throws IOException {

        // 表单项的名字
        System.out.println("form 定义的name值 = " + multipartFile.getName());
        // 文件名
        System.out.println("原始文件名 = " + multipartFile.getOriginalFilename());

        String originalFilename = multipartFile.getOriginalFilename();

        BookPo bookVo = new BookPo();
        bookVo.setBookId(UUID.randomUUID().toString());
        bookVo.setBookContent("");
        bookVo.setBookName(originalFilename);
        bookVo.setBookAuthor("author");
        bookVo.setBookShortIntro("intro");
        bookVo.setBookCover(null);

        Db.save(bookVo);

        File file = convertMultipartFileToFile(multipartFile);
        multipartFile.transferTo(file);
        List<BookChapter> bookChapters = LocalPageLoaderUtil.loadChapters(file, bookVo.getBookId());

        Db.saveBatch(bookChapters);

    }

    public static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        // 获取项目根目录下的 "uploads" 文件夹路径
        String uploadsDir = "uploads";
        String realPath = System.getProperty("user.dir") + "/" + uploadsDir;

        // 确保文件夹存在，如果不存在则创建
        File uploadsDirFile = new File(realPath);
        if (!uploadsDirFile.exists()) {
            uploadsDirFile.mkdirs();
        }

        // 构建文件路径
        String fileName = multipartFile.getOriginalFilename();
        String filePath = realPath + "/" + fileName;

        // 创建 Path 对象
        Path path = Path.of(filePath);

        // 将 MultipartFile 写入 Path
        Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // 创建 File 对象
        File file = new File(filePath);

        return file;
    }
}
