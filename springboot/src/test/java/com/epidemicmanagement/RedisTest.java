package com.epidemicmanagement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.epidemicmanagement.dao.NewsDao;
import com.epidemicmanagement.entity.News;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author XUE
 */
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ObjectMapper mapper;

    /*************************/
    @Autowired
    private NewsDao newsDao;

    @Test
    public void redisTest() {
        redisTemplate.opsForValue().set("name", "xue");
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println(name);
    }

    @Test
    public void listTest() {
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        List<Object> list = listOps.range("newsList", 0, -1);
//        List<News> newList = mapper.convertValue(list, new TypeReference<List<News>>() {});
        List<News> newList = (List<News>)(Object) list;
        if (newList == null || newList.size() == 0) {
            PageHelper.startPage(1, 10);
            QueryWrapper<News> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("create_time");
            newList = newsDao.selectList(queryWrapper);
            listOps.leftPush("newsList", newList);
        }
        System.out.println(newList);
    }

}
