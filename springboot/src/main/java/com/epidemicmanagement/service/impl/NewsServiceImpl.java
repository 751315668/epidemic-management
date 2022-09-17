package com.epidemicmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.epidemicmanagement.config.Result;
import com.epidemicmanagement.dao.NewsDao;
import com.epidemicmanagement.entity.News;
import com.epidemicmanagement.service.NewsService;
import com.epidemicmanagement.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.w3c.dom.ls.LSException;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author XUE
 */
@Service
@Transactional
public class NewsServiceImpl implements NewsService {

    private final String[] columns = {"id", "title", "`desc`", "content", "img_url", "create_time"};

    @Autowired
    private RedisTemplate<String, Object> template;

//    @Autowired
//    private ObjectMapper mapper;

    @Autowired
    private NewsDao newsDao;

    @Override
    public PageInfo<News> selectAllNews(Integer pageNum, Integer pageSize) {
        ValueOperations<String, Object> valOps = template.opsForValue();
        Integer pag = (Integer) valOps.get("pageNum");
        Integer lim = (Integer) valOps.get("pageSize");
        PageInfo<News> newsListPageInfo = (PageInfo<News>) valOps.get("newsListPageInfo");
        boolean flag = false; // 记录分页信息是否改变
        if (pag == null || lim == null) { // 首次查询
            // 存储分页信息
            pag = 1;
            lim = 10;
            valOps.set("pageNum", pag, 30, TimeUnit.MINUTES);
            valOps.set("pageSize", lim, 30, TimeUnit.MINUTES);
        } else if (!pag.equals(pageNum) || !lim.equals(pageSize)) { // 后续查询新的分页数据时
            valOps.set("pageNum", pageNum, 30, TimeUnit.MINUTES);
            valOps.set("pageSize", pageSize, 30, TimeUnit.MINUTES);
            pag = pageNum;
            lim = pageSize;
            flag = true;
        }
        // 缓存数据不存在或改变分页数据时，查询数据库并更新缓存
        if (newsListPageInfo == null || flag) {
            PageHelper.startPage(pag, lim);
            QueryWrapper<News> queryWrapper = new QueryWrapper<>();
            queryWrapper.select(columns).orderByDesc("create_time");
            List<News> newsList = newsDao.selectList(queryWrapper);
            PageInfo<News> pageInfo = new PageInfo<>(newsList);
            valOps.set("newsListPageInfo", pageInfo, 30, TimeUnit.MINUTES); // 缓存集合分页数据
            return pageInfo;
        }
        return newsListPageInfo;
    }

    // 新闻发布
    @Override
    public boolean publishNews(News news) {
        news.setCreateTime(System.currentTimeMillis());
        String content = news.getContent();
        String desc = getDesc(content);
        news.setDesc(desc);
        int item = newsDao.insert(news);
        template.delete("newsListPageInfo"); // 删除缓存
        return item > 0;
    }

    // 截取新闻前15字符作为描述信息
    private String getDesc(String content) {
        Document doc = Jsoup.parse(content);
        String text = doc.text();
        if (text.length() > 30) {
            String str = text.substring(0, 30);
            return str + "...";
        } else return text + "...";
    }
}
