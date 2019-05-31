package com.xuecheng.filesystem.controller;

import com.xuecheng.api.file.FileSystemControllerApi;
import com.xuecheng.filesystem.service.FileSystemService;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author lpt
 * @Date 2019/4/8 14:51
 * @Version 1.0
 */
@RequestMapping("/filesystem")
@RestController
public class FileSystemController implements FileSystemControllerApi {
    @Autowired
    FileSystemService fileSystemService;

    @Override
    @PostMapping("/upload")
    public UploadFileResult upload(MultipartFile multipartFile, String filetag, String businesskey, String metadata) {
        return fileSystemService.upload(multipartFile, filetag, businesskey, metadata);
    }

}