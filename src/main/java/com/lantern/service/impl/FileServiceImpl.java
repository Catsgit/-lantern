package com.lantern.service.impl;

import com.google.common.collect.Lists;
import com.lantern.service.IFileService;
import com.lantern.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by cat on 17-5-30.
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();   //文件名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);   //扩展名 去除'.'
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName; //将文件名修改 避免重复
        logger.info("开始上传文件, 上传文件的文件名:{}, 上传的路径是:{}, 新文件名:{}", fileName, path, uploadFileName);

        File fileDir = new File(path);
        if(!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);

        try {
            file.transferTo(targetFile);
            //将targetFile上传的ftp
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //上传完后之后,删除upload下面的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常", e);
            return null;
        }
        return targetFile.getName();

    }
}
