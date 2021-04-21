package com.guli.teacher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.teacher.entity.EduChapter;
import com.guli.teacher.entity.EduVideo;
import com.guli.teacher.entity.vo.OneChapter;
import com.guli.teacher.entity.vo.TwoVideo;
import com.guli.teacher.exception.EduException;
import com.guli.teacher.mapper.EduChapterMapper;
import com.guli.teacher.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.teacher.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author bangtidy
 * @since 2021-03-07
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;

    @Override
    public List<OneChapter> getChapterAndVideoById(String id) {

        List<OneChapter> list = new ArrayList<>();
        //判断ID

        //1、根据ID查询章节列表
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",id);
        wrapper.orderByAsc("sort");
        List<EduChapter> chapterList = baseMapper.selectList(wrapper);
        //2、遍历章节列表
        for(EduChapter chapter:chapterList){
            //3、把每一个章节对象复制到OneChapter
            OneChapter oneChapter = new OneChapter();
            BeanUtils.copyProperties(chapter,oneChapter);
            //4、根据每一个章节ID查询小节列表
            QueryWrapper<EduVideo> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("chapter_id",oneChapter.getId());
            wrapper1.orderByAsc("sort");
            List<EduVideo> videoList = eduVideoService.list(wrapper1);
            //5、遍历每一个小节
            for(EduVideo eduVideo:videoList){
                //6、把每一个小节对象复制到TwoVideo
                TwoVideo twoVideo = new TwoVideo();
                BeanUtils.copyProperties(eduVideo, twoVideo);
                //7、把每一个TwoVideo加到章节children
                oneChapter.getChildren().add(twoVideo);
            }
            list.add(oneChapter);
            //8、把每一个章节加到集合中
        }
        return list;
    }

    /**
     *   保存章节
     *   判断保存的章节名称是否存在
     * @param eduChapter
     * @return
     */
    @Override
    public boolean saveChapter(EduChapter eduChapter) {
        if(eduChapter == null){
            return false;
        }
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("title",eduChapter.getTitle());
        wrapper.eq("course_id",eduChapter.getCourseId());
        Integer count = baseMapper.selectCount(wrapper);
        if(count > 0){
            throw new EduException(20001,"章节名称已存在");
        }
        int insert =baseMapper.insert(eduChapter);
        return insert==1;
    }

    /**
     * 修改章节
     * 修改时判断名字是否存在
     * @param eduChapter
     * @return
     */
    @Override
    public boolean updateChapterById(EduChapter eduChapter) {
        if(eduChapter == null){
            return false;
        }
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("title",eduChapter.getTitle());
        Integer count = baseMapper.selectCount(wrapper);
        if(count > 0){
            throw new EduException(20001,"章节名称已存在");
        }
        int i = baseMapper.updateById(eduChapter);
        return i==1;
    }

    @Override
    public Boolean removeChapterById(String id) {
        //1、判断章节Id下面是否存小节
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",id);
        List<EduVideo> list = eduVideoService.list(wrapper);
        //2、如果有不能删除直接false
        if(list.size() != 0){
            throw new EduException(20001,"此章节下有小节，不能删除");
        }
        //3、删除章节
        int i = baseMapper.deleteById(id);
        return i == 1;
    }
}
