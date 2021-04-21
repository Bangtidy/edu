package com.guli.oss.service;

import com.aliyun.oss.OSSClient;
import com.guli.oss.util.ConstantPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{

    private static String TYPESTR[] ={".png",".jpg",".bmp",".gif","jpeg"};

    @Override
    public String upload(MultipartFile file) {
        OSSClient ossClient = null;
        String url = null;
        Boolean flag = false;
        try{
            // 创建OSSClient实例。
            ossClient = new OSSClient(
                    ConstantPropertiesUtil.END_POINT,
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            //文件上传是否安全问题：
            //1、判断文件格式
            for (String tpye : TYPESTR){
               if(StringUtils.endsWithIgnoreCase(file.getOriginalFilename(),tpye)){
                   flag = true;
                   break;
                }
            }
            if(!flag){
                return "图片格式不正确";
            }

            //2、判断文件内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if(image != null){
                System.err.println(String.valueOf(image.getHeight()));
                System.err.println(String.valueOf(image.getWidth()));
            }else {
                return "文件内容不正确";
            }

            //获取文件名称
            String filename = file.getOriginalFilename();
            //文件名字后缀
            String ext = filename.substring(filename.lastIndexOf("."));
            String newName = UUID.randomUUID().toString()+ext;
            String dataPath = new DateTime().toString("yyyy/MM/dd");
            String urlPath = ConstantPropertiesUtil.FILE_HOST+"/"+dataPath+"/"+newName;
            // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
            InputStream inputStream = file.getInputStream();
            // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
            ossClient.putObject(ConstantPropertiesUtil.BUCKET_NAME, urlPath, inputStream);
            url ="https://"+ConstantPropertiesUtil.BUCKET_NAME+"."+ConstantPropertiesUtil.END_POINT+"/"+urlPath;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
        return url;
    }
}
