package com.guli.teacher.service;

import com.guli.teacher.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.teacher.entity.vo.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author bangtidy
 * @since 2021-03-06
 */
public interface EduSubjectService extends IService<EduSubject> {

    /*
    * 根据传递的EXCEL表格模板导入课程分类
    * 返回错误的课程信息
    * */
    List<String> importEXCEL(MultipartFile file);

    /*
    * 获得课程分类树状
    * */
    List<OneSubject> getTree();

    /*
    * 根据Id删除课程分类
    * */
    boolean deleteById(String id);

    /*
    * 添加课程一级分类
    * */
    Boolean saveLevelOne(EduSubject subject);

    /*
    * 添加二级分类
    * */

    Boolean savaLevelTwo(EduSubject subject);
}
