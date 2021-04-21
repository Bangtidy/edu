package com.guli.teacher.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.result.ResultCode;
import com.guli.teacher.entity.EduTeacher;
import com.guli.teacher.entity.query.TeacherQuery;
import com.guli.teacher.exception.EduException;
import com.guli.teacher.service.EduTeacherService;
import com.guli.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author bangtidy
 * @since 2021-03-02
 */
@RestController
@RequestMapping("/teacher")
@Api(description = "讲师管理")
@CrossOrigin
public class EduTeacherController {

    @Autowired
    private EduTeacherService teacherService;

    @ApiOperation(value = "所有讲师列表")
    @GetMapping("list")
    public Result list() {
        //int i =1/0;//ArithmeticException
        try{
            List<EduTeacher> list = teacherService.list(null);
            return Result.ok().data("items", list);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error();
        }

    }

    @ApiOperation(value = "讲师删除")
    @DeleteMapping("{id}")
    //占位符：
    // 1、如果占位符中的参数名和形参名一致的话，那么@PathVariable可以省略；
    //2、如果配置了Swagger，并在形参前加了其他注解，那么@ParhVariable必须加；

    public Result deleteTeacherById(
            @ApiParam(name = "id", value = "讲师id", required = true)
            @PathVariable(value = "id") String id) {
        try {
            teacherService.removeById(id);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            ;
            return Result.error();
        }
    }

    @ApiOperation(value = "讲师分页列表")
    @GetMapping("/{page}/{limit}")
    public Result selectTeacherByPage(
            @ApiParam(name = "page", value = "当前页", required = true)
            @PathVariable(value = "page") Integer page,

            @ApiParam(name = "limit", value = "每页显示记录数", required = true)
            @PathVariable(value = "limit") Integer limit) {
        try {
            Page<EduTeacher> teacherPage = new Page<>(page, limit);

            teacherService.page(teacherPage, null);
            List<EduTeacher> teacherList = teacherPage.getRecords();
            long total = teacherPage.getTotal();

            return Result.ok().data("total", total).data("rows", teacherList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @ApiOperation(value = "讲师分页条件列表")
    @PostMapping("/{page}/{limit}")
    public Result selectTeacherByPageAndWrapper(
            @ApiParam(name = "page", value = "当前页", required = true)
            @PathVariable(value = "page") Integer page,

            @ApiParam(name = "limit", value = "每页显示记录数", required = true)
            @PathVariable(value = "limit") Integer limit,
            @RequestBody TeacherQuery query) {
        try {
            Page<EduTeacher> teacherPage = new Page<>(page, limit);

            teacherService.pageQuery(teacherPage, query);
            List<EduTeacher> teacherList = teacherPage.getRecords();
            long total = teacherPage.getTotal();

            return Result.ok().data("total", total).data("rows", teacherList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @ApiOperation(value = "添加教师")
    @PostMapping("save")
    public Result saveTeacher(@RequestBody EduTeacher teacher){
       try{
           teacherService.save(teacher);
           return Result.ok();
       }catch (Exception e){
           e.printStackTrace();
           return Result.error();
       }

    }

    @ApiOperation(value = "根据id查询")
    @GetMapping("{id}")
    public Result getTeacherById(
            @ApiParam(name = "id",value = "讲师id",required = true)
            @PathVariable String id){
        EduTeacher teacher = teacherService.getById(id);
        if(teacher == null){
            throw new EduException(ResultCode.EDU_ID_ERROR,"没有此讲师信息");
        }
        try{
            return Result.ok().data("teacher",teacher);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error();
        }

    }

    @ApiOperation(value = "修改讲师信息")
    @PutMapping("update")
    public Result updateTeacher(@RequestBody EduTeacher teacher){
        try{
            teacherService.updateById(teacher);
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return Result.error();
        }
    }
}

