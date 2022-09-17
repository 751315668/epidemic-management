package com.epidemicmanagement.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.epidemicmanagement.config.Result;
import com.epidemicmanagement.entity.News;
import com.epidemicmanagement.service.NewsService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * @author XUE
 */
@Controller
@RequestMapping("/api")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @ResponseBody
    @GetMapping(value = "/getNewsList")
    public Result getNewsList(@RequestParam("pageNum") Integer pageNum,
                              @RequestParam("pageSize") Integer pageSize) {
        PageInfo<News> newsPageInfo = newsService.selectAllNews(pageNum, pageSize);
        return Result.success(200, "查询成功").setAttribute("info", newsPageInfo);
    }

    @ResponseBody
    @PostMapping(value = "/publishNews")
    public Result publishNews(@RequestBody News news) {
        boolean success = newsService.publishNews(news);
        return success ? Result.success(200, "发布成功") : Result.fail(500, "发布失败");
    }

    @ResponseBody
    @PostMapping(value = "/uploadImg")
    public Result uploadImg(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf('.'));
        fileName = UUID.randomUUID() + suffixName;
        String filePath = "C:\\Users\\hp\\IdeaProjects\\epidemic-management\\src\\main\\resources\\static\\newsimg\\";
        try {
            file.transferTo(new File(filePath + fileName));
            return Result.success(200, "图片上传成功").setAttribute("imgName", fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "上传失败");
        }
    }


}
