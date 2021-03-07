package com.guli.teacher.controller;


import com.guli.common.result.Result;
import com.guli.teacher.entity.vo.CourseVo;
import com.guli.teacher.service.EduCourseDescriptionService;
import com.guli.teacher.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}

