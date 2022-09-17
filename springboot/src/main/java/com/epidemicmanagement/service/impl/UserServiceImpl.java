package com.epidemicmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.epidemicmanagement.config.Result;
import com.epidemicmanagement.dao.UserDao;
import com.epidemicmanagement.entity.User;
import com.epidemicmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author XUE
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User login(String account, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", account)
                .eq("password", password)
                .eq("is_enable", 1);
        User user = userDao.selectOne(wrapper);
        if (user != null) {
            return user;
        }
        return null;
    }

    @Override
    public boolean insertUser(User user) {
        int item = userDao.insert(user);
        return item > 0;
    }

    @Override
    public User findUserById(Integer id) {
        User user = userDao.selectById(id);
        return user;
    }

    @Override
    public boolean updateUser(User user) {
        int item = userDao.updateById(user);
        return item > 0;
    }

    @Override
    public boolean checkName(String name) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name);
        User user = userDao.selectOne(wrapper);
        return user == null;
    }
}
