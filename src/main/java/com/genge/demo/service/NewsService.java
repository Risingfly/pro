package com.genge.demo.service;

import com.genge.demo.dao.NewsDAO;
import com.genge.demo.model.News;
import com.genge.demo.util.TouTiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    /**
     * 查找最新资讯
     * @param uerId
     * @param offset
     * @param limit
     * @return
     */
    public List<News> getLatestNews(int uerId,int offset,int limit){
        return newsDAO.selectByUserIdAndOffset(uerId,offset,limit);
    }

    /**
     * 添加资讯
     * @param news
     * @return
     */
    public int addNews(News news){
        return newsDAO.addNews(news);
    }
    /***
     *
     * 处理用户上传的图片
     * @param file
     * @return
     * @throws IOException
     */
    public String saveImage(MultipartFile file)throws IOException{
//      xx.jpg 取点的位置，以后缀来判断是否是文件
        int doPos = file.getOriginalFilename().lastIndexOf(".");
        if (doPos < 0){
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(doPos + 1).toLowerCase();
        if (!TouTiaoUtil.isFiledAllowed(fileExt)){
            return null;
        }
//      上传的文件名重新处理
        String fileName = UUID.randomUUID().toString().replaceAll("-","") + "." + fileExt;
        Files.copy(file.getInputStream(),new File(TouTiaoUtil.IMAGE_DIR + fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        System.out.println("上传路径："+TouTiaoUtil.IMAGE_DIR + fileName);
        return TouTiaoUtil.TOUTIAO_DOMAIN + "image?name=" + fileName;
    }


}
