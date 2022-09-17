package com.epidemicmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.epidemicmanagement.config.Result;
import com.epidemicmanagement.dao.TaskDao;
import com.epidemicmanagement.entity.Task;
import com.epidemicmanagement.entity.Volunteer;
import com.epidemicmanagement.service.TaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author XUE
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final String[] columns = {"id", "name", "publisher", "publisher_id", "participant_cnt", "status", "`desc`", "start_time", "end_time", "insert_time"};

    @Autowired
    private TaskDao taskDao;

    @Override
    public PageInfo<Task> selectAllTask(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Task> wrapper = new QueryWrapper<>();
        wrapper.select(columns).orderByDesc("insert_time");
        List<Task> tasks = taskDao.selectList(wrapper);
        tasks.forEach(new Consumer<Task>() {
            @Override
            public void accept(Task task) {
                long curTime = System.currentTimeMillis();
                Long endTime = task.getEndTime();
                if (curTime > endTime) {
                    task.setStatus("2");
                    taskDao.updateById(task);
                }
            }
        });
        return new PageInfo<>(tasks);
    }

    @Override
    public PageInfo<Task> searchTask(Integer pageNum, Integer pageSize, String status, String content) {
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Task> wrapper = new QueryWrapper<>();
        if (!status.isEmpty()) {
            wrapper.select(columns).eq("status", status).like("name", content);
        } else {
            wrapper.select(columns).like("name", content);
        }
        List<Task> tasks = taskDao.selectList(wrapper);
        return new PageInfo<>(tasks);
    }

    @Override
    public Task searchById(Integer id) {
        return taskDao.selectById(id);
    }

    @Override
    public boolean insertTask(Task task) {
        // 管理员发布的任务无需审核
        if (task.getPublisherId() == 1) {
            task.setStatus("1");
        }
        task.setInsertTime(System.currentTimeMillis());
        int item = taskDao.insert(task);
        return item > 0;
    }

    @Override
    public boolean joinTask(Integer taskId, Integer volId) {
        int item = taskDao.insertTaskVolRel(taskId, volId);
        Task task = taskDao.selectById(taskId);
        task.setParticipantCnt(task.getParticipantCnt() + 1);
        int item2 = taskDao.updateById(task);
        return item > 0 && item2 > 0;
    }

    @Override
    public Task getParticipantList(Integer id) {
        return taskDao.getParticipantList(id);
    }

    @Override
    public boolean agreeTask(Integer id) {
        Task task = taskDao.selectById(id);
        task.setStatus("1");
        int item = taskDao.updateById(task);
        return item > 0;
    }

    @Override
    public boolean accomplishTask(Integer id) {
        Task task = taskDao.selectById(id);
        task.setStatus("2");
        int item = taskDao.updateById(task);
        return item > 0;
    }

    @Override
    public List<Integer> getTaskChartData() {
        QueryWrapper<Task> wrapper = new QueryWrapper<>();
        wrapper.select("id", "status");
        List<Task> tasks = taskDao.selectList(wrapper);
        int i = 0, j = 0, x = 0, y = 0; // 【审核中；进行中；已完成；取消】
        for (Task t : tasks) {
            if (t.getStatus().equals("0")) {
                i++;
            } else if (t.getStatus().equals("1")) {
                j++;
            } else if (t.getStatus().equals("2")) {
                x++;
            } else if (t.getStatus().equals("3")) {
                y++;
            }
        }
        List<Integer> res = new ArrayList<>(4);
        res.add(i);
        res.add(j);
        res.add(x);
        res.add(y);
        return res;
    }

    @Override
    public Integer getSize() {
        QueryWrapper<Task> wrapper = new QueryWrapper<>();
        wrapper.select("id");
        return taskDao.selectCount(wrapper);
    }


}
