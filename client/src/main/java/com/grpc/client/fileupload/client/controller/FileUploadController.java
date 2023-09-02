package com.grpc.client.fileupload.client.controller;


import com.grpc.client.fileupload.client.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author A.Sarang Kumar Tak
 * @youtubechannelname Dev Problems
 */
@Slf4j
@RestController
public class FileUploadController {

    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile multipartFile)  {
        return this.fileUploadService.uploadFile(multipartFile);
    }

}
