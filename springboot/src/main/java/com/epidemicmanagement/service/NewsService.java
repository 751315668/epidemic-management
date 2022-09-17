package com.epidemicmanagement.service;

import com.epidemicmanagement.entity.News;
import com.github.pagehelper.PageInfo;

/**
 * @author XUE
 */
public interface NewsService {

    PageInfo<News> selectAllNews(Integer pageNum, Integer pageSize);

    boolean publishNews(News news);

}
