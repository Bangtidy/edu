package com.guli.teacher.mapper;

import com.guli.teacher.entity.EduSubject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程科目 Mapper 接口
 * </p>
 *
 * @author bangtidy
 * @since 2021-03-06
 */
public interface EduSubjectMapper extends BaseMapper<EduSubject> {

    void selectList();
}
