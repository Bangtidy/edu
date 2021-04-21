package com.guli.teacher.controller;


import com.guli.common.result.Result;
import com.guli.teacher.entity.EduChapter;
import com.guli.teacher.entity.EduVideo;
import com.guli.teacher.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author bangtidy
 * @since 2021-04-20
 */
@RestController
@RequestMapping("/video")
@CrossOrigin
public class  EduVideoController {

    @Autowired
    private EduVideoService eduVideoService;

    /**
     * 保存
     */
    @PostMapping("save")
    public Result save(@RequestBody EduVideo eduVideo){
        boolean save = eduVideoService.save(eduVideo);
        if(save){
            return Result.ok();
        }else {
            return Result.error();
        }
    }

    /**
     * 根据ID查询Video对象 回显
     */
    @GetMapping("{id}")
    public Result getVideoById(@PathVariable String id){
        EduVideo video = eduVideoService.getById(id);
        return Result.ok().data("video",video);
    }


    /**
     * 修改
     */
    @PutMapping("update")
    public Result update(@RequestBody EduVideo video){
        boolean update = eduVideoService.updateById(video);
        if(update){
            return Result.ok();
        }else{
            return Result.error();
        }
    }


    /**
     * 删除
     */
    @DeleteMapping("{id}")
    public Result deleteById(@PathVariable String id){
        Boolean flag = eduVideoService.removeEduVideoById(id);
        if(flag){
            return Result.ok();
        }else{
            return Result.error();
        }
    }
}

