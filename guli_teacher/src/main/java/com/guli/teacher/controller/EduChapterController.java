package com.guli.teacher.controller;


import com.guli.common.result.Result;
import com.guli.teacher.entity.EduChapter;
import com.guli.teacher.entity.vo.OneChapter;
import com.guli.teacher.service.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author bangtidy
 * @since 2021-03-07
 */
@RestController
@RequestMapping("/edu-chapter")
@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService eduChapterService;

    /**
     * 根据课程的ID获取章节和小节列表
     * Tree
     * 1、创建一个对象，作为章节VO,里面包括二级集合（小节）
     * 2、创建二级的Vo（video)
     * 3、根据课程ID查询章节的列表，遍历这个列表，根据每一个章节的ID查询二级列表（Video集合）
     */
    @GetMapping("{courseId}")
    public Result getChapterAmdVideoById(@PathVariable String courseId){
        List<OneChapter> list = eduChapterService.getChapterAndVideoById(courseId);
        return Result.ok().data("list",list);
    }

    /**
     * 添加章节
     * @param eduChapter
     * @return
     */
    @PostMapping("save")
    public Result save(@RequestBody EduChapter eduChapter){
       try {
           boolean save = eduChapterService.saveChapter(eduChapter);
           return Result.ok();
       }catch (Exception e){
           e.printStackTrace();
           return Result.error().message(e.getMessage());
       }
    }

    /**
     * 获取章节
     * @param id
     * @return
     */
    @GetMapping("get/{id}")
    public Result getChapterById(@PathVariable String id){
        EduChapter chapter = eduChapterService.getById(id);
        return Result.ok().data("chapter",chapter);
    }

    /**
     * 修改章节
     * @param eduChapter
     * @return
     */
    @PutMapping("update")
    public Result updateById(@RequestBody EduChapter eduChapter){
        try {
            boolean b = eduChapterService.updateChapterById(eduChapter);
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return Result.error().message(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public Result deleteById(@PathVariable String id){
        Boolean flag = eduChapterService.removeChapterById(id);
        if(flag){
            return Result.ok();
        }else{
            return Result.error();
        }
    }
}

