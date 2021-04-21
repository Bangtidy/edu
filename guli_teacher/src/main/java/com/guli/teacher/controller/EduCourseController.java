package com.guli.teacher.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.result.Result;
import com.guli.teacher.entity.EduCourse;
import com.guli.teacher.entity.query.CourseQuery;
import com.guli.teacher.entity.vo.CoursePublishVo;
import com.guli.teacher.entity.vo.CourseVo;
import com.guli.teacher.service.EduCourseDescriptionService;
import com.guli.teacher.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author bangtidy
 * @since 2021-03-07
 */
@RestController
@RequestMapping("/course")
@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    /*
    * 保存基本信息
    * */
    @PostMapping("saveVo")
    public Result save(@RequestBody CourseVo vo){
        String courseId = courseService.saveVo(vo);
        return Result.ok().data("id",courseId);
    }

    /*
    * 根据课程ID获取课程基本信息和描述
    * */
    @GetMapping("{id}")
    public Result getCourseVoById(@PathVariable String id){
        CourseVo vo = courseService.getCourseVoById(id);
        return Result.ok().data("courseInfo",vo);
    }

    /*
    * 修改课程基本信息
    * */
    @PostMapping("updateVo")
    public Result updateVo(@RequestBody CourseVo vo){
        Boolean flag = courseService.updateVo(vo);
        if(flag){
            return Result.ok();
        }else{
            return Result.error();
        }
    }

    /**
     * 根据搜索条件分页查询
     */
    @PostMapping("{page}/{limit}")
    public Result getPageList(@PathVariable Integer page,
                              @PathVariable Integer limit,
                              @RequestBody CourseQuery courseQuery){
        Page<EduCourse> objectPage = new Page<>(page, limit);
        courseService.getPageList(objectPage,courseQuery);
        return Result.ok()
                .data("rows",objectPage.getRecords())
                .data("total",objectPage.getTotal());
    }

    /**
     * 根据课程ID删除课程信息
     */
    @DeleteMapping("{id}")
    public Result deleteById(@PathVariable String id){
        Boolean flag = courseService.deleteById(id);
        if(flag){
            return Result.ok();
        }else {
            return Result.error();
        }
    }

    /**
     * 根据课程ID查询发布课程的详情
     */
    @GetMapping("vo/{id}")
    public Result getCoursePublishVoById(@PathVariable String id){
        //1、用Vo封装
        //CoursePublishVo vo = courseService.getCoursePublishVoById(id);
        //2、用Map封装
        Map<String,Object> map = courseService.getMapById(id);
        return Result.ok().data(map);
    }

    /**
     * 根据课程ID修改发布状态
     */
    @GetMapping("updateStatusById/{id}")
    public Result updateStatusById(@PathVariable String id){
        Boolean flag = courseService.updateStatusById(id);
        if(flag){
            return Result.ok();
        }else{
            return  Result.error();
        }
    }
}

