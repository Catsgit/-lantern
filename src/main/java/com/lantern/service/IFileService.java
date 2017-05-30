package com.lantern.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by cat on 17-5-30.
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}
