package com.jjzhong.mall.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public interface UploadService {
    String uploadImageReturnUrl(MultipartFile file, String context) throws IOException;
    URI getURI(String context);
    File uploadFile(MultipartFile file, String context);
}
