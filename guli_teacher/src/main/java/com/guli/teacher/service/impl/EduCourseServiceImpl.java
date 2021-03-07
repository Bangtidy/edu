package com.guli.teacher.service.impl;

import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.vo.CourseVo;
import com.guli.teacher.mapper.EduCourseMapper;
import com.guli.teacher.service.EduCourseDescriptionService;
import com.guli.teacher.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author bangtidy
 * @since 2021-03-07
 */
@Service
@Transactional
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;

    @Override
    public String saveVo(CourseVo vo) {

        //1、添加课程
        baseMapper.insert(vo.getEduCourse());
        //2、获取课程ID
        String courseId = vo.getEduCourse().getId();
        //3、添加课程描述
        vo.getCourseDescription().setId(courseId);
        eduCourseDescriptionService.save(vo.getCourseDescription());
        return courseId;
    }
}
