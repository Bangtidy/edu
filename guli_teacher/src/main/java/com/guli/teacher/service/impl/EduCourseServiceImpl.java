package com.guli.teacher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.EduCourseDescription;
import com.guli.teacher.entity.query.CourseQuery;
import com.guli.teacher.entity.vo.CoursePublishVo;
import com.guli.teacher.entity.vo.CourseVo;
import com.guli.teacher.mapper.EduCourseMapper;
import com.guli.teacher.service.EduCourseDescriptionService;
import com.guli.teacher.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Wrapper;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author bangtidy
 * @since 2021-03-07
 */
@Service
@Transactional//加事务
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

    @Override
    public CourseVo getCourseVoById(String id) {
        //创建一个VO对象
        CourseVo vo = new CourseVo();
        //根据课程ID获取课程对象 EduCourse
        EduCourse eduCourse = baseMapper.selectById(id);
        //把课程加到vo对象中
        if(eduCourse == null){
            return vo;
        }
        vo.setEduCourse(eduCourse);
        //根据课程ID获取课程描述
        EduCourseDescription eduCourseDescription = eduCourseDescriptionService.getById(eduCourse.getId());
        //把描述加到Vo对象中
        if(eduCourseDescription == null){
            return vo;
        }
        vo.setCourseDescription(eduCourseDescription);
        return vo;
    }

    @Override
    public Boolean updateVo(CourseVo vo) {
        //1、先判断ID是否存在，如果不存在直接返回false;
        if(StringUtils.isEmpty(vo.getEduCourse().getId())){
            return false;
        }
        //2、修改course
        int i = baseMapper.updateById(vo.getEduCourse());
        if(i <= 0){
            return false;
        }
        //3、修改courseDesc
        vo.getCourseDescription().setId(vo.getEduCourse().getId());
        boolean flag = eduCourseDescriptionService.updateById(vo.getCourseDescription());
        return flag;
    }

    @Override
    public void getPageList(Page<EduCourse> objectPage, CourseQuery courseQuery) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        if(courseQuery == null){
            baseMapper.selectPage(objectPage,wrapper);
        }else{
            String subjectId = courseQuery.getSubjectId();
            String subjectParentId = courseQuery.getSubjectParentId();
            String teacherId = courseQuery.getTeacherId();
            String title = courseQuery.getTitle();

            if(!StringUtils.isEmpty(subjectId)){
                wrapper.eq("subject_id",subjectId);
            }
            if(!StringUtils.isEmpty(subjectParentId)){
                wrapper.eq("subject_parent_id",subjectParentId);
            }
            if(!StringUtils.isEmpty(teacherId)){
                wrapper.eq("teacher_id",teacherId);
            }
            if(!StringUtils.isEmpty(title)){
                wrapper.like("title",title);
            }

            baseMapper.selectPage(objectPage,wrapper);
        }
    }

    @Override
    public Boolean deleteById(String id) {

        //TODO 删除课程相关的小节

        //TODO 删除课程相关的章节

        //删除描述
        boolean b = eduCourseDescriptionService.removeById(id);
        if(!b){
            return false;
        }
        //删除基本信息
        int i = baseMapper.deleteById(id);
        return i==1;
    }

    @Override
    public CoursePublishVo getCoursePublishVoById(String id) {
        CoursePublishVo vo = baseMapper.getCoursePublishVoById(id);
        return vo;
    }

    @Override
    public Boolean updateStatusById(String id) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");
        int i = baseMapper.updateById(eduCourse);
        return i==1;
    }

    @Override
    public Map<String, Object> getMapById(String id) {
        Map<String,Object> map = baseMapper.getMapById(id);
        return map;
    }
}
