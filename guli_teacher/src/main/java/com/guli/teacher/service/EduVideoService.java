package com.guli.teacher.service;

import com.guli.teacher.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author bangtidy
 * @since 2021-04-20
 */
public interface EduVideoService extends IService<EduVideo> {

    /**
     * 根据ID删除Video
     * @param id
     * @return
     */
    Boolean removeEduVideoById(String id);

}
