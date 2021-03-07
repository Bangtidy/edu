package com.guli.teacher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.teacher.entity.EduSubject;
import com.guli.teacher.entity.vo.OneSubject;
import com.guli.teacher.entity.vo.TwoSubject;
import com.guli.teacher.mapper.EduSubjectMapper;
import com.guli.teacher.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author bangtidy
 * @since 2021-03-06
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public List<String> importEXCEL(MultipartFile file) {

        //存储错误信息集合
        List<String> meg =new ArrayList<>();

        try{
            //1、获取文件流
            InputStream inputStream = file.getInputStream();
            //2、根据流创建一个workBook
            Workbook workbook = new XSSFWorkbook(inputStream);
            //3、获取sheet，getSheetAt(0)
            Sheet sheet = workbook.getSheetAt(0);
            //4、根据这sheet获取行数
            int lastRowNum = sheet.getLastRowNum();
            if(lastRowNum <= 1){
                meg.add("请填写数据");
                return meg;
            }
            //5、遍历行
            for(int rowNum=1;rowNum < lastRowNum; rowNum++){
                Row row = sheet.getRow(rowNum);
                //6、获取每一行 中第一列：一级分列
                Cell cell = row.getCell(0);
                if(cell == null){
                    meg.add("第"+rowNum+"行第一列为空");
                    continue;
                }
                String cellValue = cell.getStringCellValue();
                if(StringUtils.isEmpty(cellValue)){
                    meg.add("第"+rowNum+"行第一列数据为空");
                    continue;
                }
                //7、判断列是否存在，存在获取列的数据
                EduSubject subject = this.selectSubjectByName(cellValue);
                String pid = null;
                //8、把这个第一列中的数据（一级分类）保存到数据库中
                if(subject == null){
                    //9、在保存之前判断此一级分类是否存在，如果存在，不再添加，如果不存在再保存
                    EduSubject new_subject = new EduSubject();
                    new_subject.setTitle(cellValue);
                    new_subject.setParentId("0");
                    new_subject.setSort(0);
                    baseMapper.insert(new_subject);
                    pid = new_subject.getId();
                }else {
                    pid = subject.getId();
                }

                //10、再获取每一行的第二列
                Cell cell1 = row.getCell(1);
                //11、获取第二列中的数据（二级分类）
                if(cell1 == null){
                    meg.add("第"+rowNum+"行第二列为空");
                    continue;
                }
                String stringCellValue = cell1.getStringCellValue();
                if(StringUtils.isEmpty(stringCellValue)){
                    meg.add("第"+rowNum+"行第二列数据为空");
                    continue;
                }
                //12、判断此一级分类中是否存在此二级分类
                EduSubject subject2 = this.selectSubjectByNameAndParentId(stringCellValue,pid);
                //13、如果此一级分类中有此二级分类：不保存
                if(subject2 == null){ //14、如果没有，保存二级分类
                    EduSubject su = new EduSubject();
                    su.setTitle(stringCellValue);
                    su.setParentId(pid);
                    su.setSort(0);
                    baseMapper.insert(su);
                }
            }
        }catch (Exception e){

        }
        return meg;
    }


    /*
    * 根据课程一级分类的名字查询是否存在
    * */

    private EduSubject selectSubjectByName(String cellValue){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",cellValue);
        wrapper.eq("parent_id",0);
        EduSubject subject = baseMapper.selectOne(wrapper);
        return subject;
    }

    /*
    * 根据二级分类名称和parentID查询是否存在subject
    * */

    private EduSubject selectSubjectByNameAndParentId(String stringCellValue,String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",stringCellValue);
        wrapper.eq("parent_id",pid);
        EduSubject eduSubject = baseMapper.selectOne(wrapper);
        return eduSubject;
    }

    @Override
    public List<OneSubject> getTree() {

        //创建一个集合来存放OneSubject
        List<OneSubject> oneSubjectList = new ArrayList<>();
        //获得一级分类列表
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",0);
        List<EduSubject> eduSubjectList = baseMapper.selectList(wrapper);
        //遍历一级分类的列表
        for(EduSubject subject :eduSubjectList){
            //把一级分类的数据复制到OneSubject
            OneSubject oneSubject = new OneSubject();
            BeanUtils.copyProperties(subject,oneSubject);
            //根据每一个一级分类获得二级分类的列表
            QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id",oneSubject.getId());
            List<EduSubject> eduSubjects = baseMapper.selectList(queryWrapper);
            //遍历二级分类列表
            for(EduSubject su : eduSubjects){
                //把二级分类对象复制到TwoSubject
                TwoSubject twoSubject = new TwoSubject();
                BeanUtils.copyProperties(su,twoSubject);
                //把TwoSubjcet添加到OneSubject的children集合中
                oneSubject.getChildren().add(twoSubject);
            }
            oneSubjectList.add(oneSubject);
        }
        //把OneSubject添加到集合中
        return oneSubjectList;
    }

    @Override
    public boolean deleteById(String id) {
        //根据查询数据库中是否存在此ID为ParentID(二级分类)
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<EduSubject> eduSubjectList = baseMapper.selectList(wrapper);
        if(eduSubjectList.size()!=0){
            //如果有，返回false
            return false;
        }
        int i =baseMapper.deleteById(id);
        //如果没有直接删除并返回true
        return i==1;
    }

    @Override
    public Boolean saveLevelOne(EduSubject subject) {

        //1、根据这个title判断一级分类是否存在
        EduSubject eduSubject = this.selectSubjectByName(subject.getTitle());
        //2、存在直接返回false
        System.out.println("=================");
        if(eduSubject != null){
            return false;
        }
        //3、不存在保存到数据库并返回true
        subject.setSort(0);
        int insert = baseMapper.insert(subject);
        return insert == 1;

    }

    @Override
    public Boolean savaLevelTwo(EduSubject subject) {

        //判断此一级分类是否存在此二级分类的title
        EduSubject sub = this.selectSubjectByNameAndParentId(subject.getTitle(), subject.getParentId());
        if(sub != null){
            //已经存在
           return false;
        }
        int insert = baseMapper.insert(subject);
        return insert == 1;
    }

}
