package com.guli.vod.service;

import org.springframework.web.multipart.MultipartFile;

public interface VodService {

    /**
     * 上传文件
     * @param file
     * @return 视频ID
     */
    String upload(MultipartFile file);

    /**
     * 根据视频资源ID删除云端视频
     * @param videoSourceId
     * @return
     */
    Boolean deleteVodById(String videoSourceId);


}
