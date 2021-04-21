package com.guli.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /*
    * 上传图片，返回图片地址
    * */
    String upload(MultipartFile file);

}
