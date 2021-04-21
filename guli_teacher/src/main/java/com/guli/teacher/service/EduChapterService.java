package com.guli.teacher.service;

import com.guli.teacher.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.teacher.entity.vo.OneChapter;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author bangtidy
 * @since 2021-03-07
 */
public interface EduChapterService extends IService<EduChapter> {
    /**
     * 根据课程ID查询章节和小节的列表
     * @param id
     * @return
     */
    List<OneChapter> getChapterAndVideoById(String id);

    /**
     * 保存章节
     * 判断保存的章节名称是否存在
     * @param eduChapter
     * @return
     */
    boolean saveChapter(EduChapter eduChapter);

    /**
     * 修改章节
     * 修改时判断名字是否存在
     * @return
     */
    boolean updateChapterById(EduChapter eduChapter);

    /**
     * 根据章节Id删除章节信息
     * @param id
     * @return
     */
    Boolean removeChapterById(String id);
}
