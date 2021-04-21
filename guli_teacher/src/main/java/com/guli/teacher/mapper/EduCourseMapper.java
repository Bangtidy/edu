package com.guli.teacher.mapper;

import com.guli.teacher.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guli.teacher.entity.vo.CoursePublishVo;

import java.util.Map;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author bangtidy
 * @since 2021-03-07
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    CoursePublishVo getCoursePublishVoById(String id);

    Map<String, Object> getMapById(String id);
}
