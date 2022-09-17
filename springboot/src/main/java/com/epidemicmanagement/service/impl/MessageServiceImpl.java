package com.epidemicmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.epidemicmanagement.dao.MessageDao;
import com.epidemicmanagement.entity.Message;
import com.epidemicmanagement.entity.News;
import com.epidemicmanagement.service.MessageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author XUE
 */
@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    private final String[] columns = {"id", "parent", "user_id", "name", "img_url", "content", "insert_time"};

    @Autowired
    private MessageDao messageDao;

    @Override
    public PageInfo<Message> selectAllMsg(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(columns).isNull("parent").orderByDesc("insert_time");
        List<Message> parentList = messageDao.selectList(queryWrapper); // 父级留言
        if (parentList.size() < 1) {
            return new PageInfo<>(parentList);
        }
        Integer[] parentIdArray = new Integer[parentList.size()];
        for (int i = 0; i < parentIdArray.length; i++) {
            parentIdArray[i] = parentList.get(i).getId();
        }
        List<Message> childrenList = messageDao.selectList(new QueryWrapper<Message>() // 子级留言
                .in("parent", Arrays.asList(parentIdArray))
                .orderByAsc("insert_time"));
        parentList.forEach(parent -> {
            childrenList.forEach(child -> {
                List<Message> children = parent.getChildrenList();
                if (parent.getId().equals(child.getParent())) {
                    children.add(child);
                }
            });
        });
        return new PageInfo<>(parentList);
    }

    @Override
    public boolean insertMsg(Message message) {
        message.setInsertTime(System.currentTimeMillis());
        int item = messageDao.insert(message);
        return item > 0;
    }
}

