package com.epidemicmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.epidemicmanagement.config.Result;
import com.epidemicmanagement.dao.TaskDao;
import com.epidemicmanagement.dao.UserDao;
import com.epidemicmanagement.dao.VolunteerDao;
import com.epidemicmanagement.entity.News;
import com.epidemicmanagement.entity.Task;
import com.epidemicmanagement.entity.User;
import com.epidemicmanagement.entity.Volunteer;
import com.epidemicmanagement.service.VolunteerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.swing.plaf.ViewportUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XUE
 */
@Service
@Transactional
public class VolunteerServiceImpl implements VolunteerService {

    private final String[] columns = {"id", "`name`", "age", "sex", "phone", "politics_status", "vaccine_status", "detailed_address", "`desc`", "img_url", "photo_url", "insert_time", "is_enable", "is_deny", "user_id"};

    @Autowired
    private VolunteerDao volunteerDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TaskDao taskDao;

    @Override
    public PageInfo<Volunteer> getVolunteerList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Volunteer> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("is_enable", 1);
        queryWrapper.select(columns).orderByAsc("insert_time");
        List<Volunteer> volunteerList = volunteerDao.selectList(queryWrapper);
        return new PageInfo<>(volunteerList);
    }

    @Override
    public PageInfo<Volunteer> searchVolunteerList(Integer pageNum, Integer pageSize, String param) {
        QueryWrapper<Volunteer> wrapper = new QueryWrapper<>();
        wrapper.select(columns).like(StringUtils.hasText(param),"name", param)
                .in("is_enable", 1)
                .orderByAsc("insert_time");
        List<Volunteer> volunteers = volunteerDao.selectList(wrapper);
        return new PageInfo<>(volunteers);
    }

    @Override
    public PageInfo<Volunteer> getApplyList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Volunteer> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("is_enable", 0).in("is_deny", 0);
        queryWrapper.select(columns).orderByAsc("insert_time");
        List<Volunteer> volunteerList = volunteerDao.selectList(queryWrapper);
        return new PageInfo<>(volunteerList);
    }

    @Override
    public boolean insertVolunteer(Volunteer volunteer) {
        volunteer.setInsertTime(System.currentTimeMillis());
        volunteer.setIsEnable(0);
        volunteer.setIsDeny(0);
        int item = volunteerDao.insert(volunteer);
        return item > 0;
    }

    @Override
    public boolean acceptApply(Integer id) {
        Volunteer volunteer = volunteerDao.selectById(id);
        volunteer.setIsDeny(0);
        volunteer.setIsEnable(1);
        Integer userId = volunteer.getUserId();
        User user = userDao.selectById(userId);
        user.setIsEnable(1);
        userDao.updateById(user);
        int item = volunteerDao.updateById(volunteer);
        return item > 0;
    }

    @Override
    public boolean denyApply(Integer id) {
        Volunteer volunteer = volunteerDao.selectById(id);
        volunteer.setIsDeny(1);
        volunteer.setIsEnable(0);
        // 拒绝时删除对应的登录账户
        Integer userId = volunteer.getUserId();
        int item2 = userDao.deleteById(userId);
        int item = volunteerDao.updateById(volunteer);
        return item > 0 && item2 > 0;
    }

    @Override
    public boolean updateVolunteer(Volunteer volunteer) {
        int item = volunteerDao.updateById(volunteer);
        return item > 0;
    }

    @Override
    public Volunteer findByUserId(Integer id) {
        QueryWrapper<Volunteer> wrapper = new QueryWrapper<>();
        wrapper.select(columns).eq("user_id", id);
        return volunteerDao.selectOne(wrapper);
    }

    @Override
    public Volunteer findById(Integer id) {
        QueryWrapper<Volunteer> wrapper = new QueryWrapper<>();
        wrapper.select(columns).eq("id", id);
        return volunteerDao.selectOne(wrapper);
    }

    // 移除志愿者
    @Override
    public boolean removeVolunteer(Integer id) {
        Volunteer volunteer = volunteerDao.selectById(id);
        Integer taskId = volunteerDao.getTaskIdByVolId(id);
        int item = 0;
        if (taskId != null) { // 有参与任务时，同时移除相关信息
            Task task = taskDao.selectById(taskId);
            task.setParticipantCnt(task.getParticipantCnt() - 1); // 将其参与的任务的参加人数-1
            taskDao.updateById(task);
            item = volunteerDao.deleteTaskInfo(id); // 删除志愿者任务关系表中的记录
        }
        Integer userId = volunteer.getUserId();
        userDao.deleteById(userId); // 删除登录用户
        int item2 = volunteerDao.deleteById(id); // 删除志愿者
        return item > 0 || item2 > 0;
    }

    @Override
    public List<Integer> getVolChartData() {
        QueryWrapper<Volunteer> wrapper = new QueryWrapper<>();
        wrapper.select("id", "sex");
        List<Volunteer> volunteers = volunteerDao.selectList(wrapper);
        int male = 0, female = 0;
        for (Volunteer v : volunteers) {
            if (v.getSex().equals("男")) {
                male++;
            } else female++;
        }
        List<Integer> res = new ArrayList<>(2);
        res.add(male);
        res.add(female);
        return res;
    }

    @Override
    public Integer getSize() {
        QueryWrapper<Volunteer> wrapper = new QueryWrapper<>();
        wrapper.select("id").eq("is_enable", 1);
        return volunteerDao.selectCount(wrapper);
    }
}
