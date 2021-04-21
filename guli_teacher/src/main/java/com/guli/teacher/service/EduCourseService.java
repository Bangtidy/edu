package com.guli.teacher.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.teacher.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.teacher.entity.query.CourseQuery;
import com.guli.teacher.entity.vo.CoursePublishVo;
import com.guli.teacher.entity.vo.CourseVo;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author bangtidy
 * @since 2021-03-07
 */
public interface EduCourseService extends IService<EduCourse> {

    /*
     * 保存课程基本信息
     * */
    String saveVo(CourseVo vo);

    /*
    * 根据课程ID查询基本信息和描述
    * */
    CourseVo getCourseVoById(String id);

    /**
     * 修改课程基本信息
     * @param vo
     * @return
     */
    Boolean updateVo(CourseVo vo);

    /**
     * 根据搜索条件分页查询
     * @param objectPage
     * @param courseQuery
     */
    void getPageList(Page<EduCourse> objectPage, CourseQuery courseQuery);

    /**
     * 根据课程ID删除课程信息
     * @param id
     * @return
     */
    Boolean deleteById(String id);

    /**
     * 根据课程ID查询课程发布详情信息
     * @param id
     * @return
     */
    CoursePublishVo getCoursePublishVoById(String id);

    /**
     * 根据课程ID修改课程发布状态
     * @param id
     * @return
     */
    Boolean updateStatusById(String id);

    /**
     * 根据课程Id获取课程Map
     * @param id
     * @return
     */
    Map<String, Object> getMapById(String id);
}
