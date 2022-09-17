package com.epidemicmanagement.service;

import com.epidemicmanagement.config.Result;
import com.epidemicmanagement.entity.News;
import com.epidemicmanagement.entity.Task;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author XUE
 */
public interface TaskService {

    PageInfo<Task> selectAllTask(Integer pageNum, Integer pageSize);

    PageInfo<Task> searchTask(Integer pageNum, Integer pageSize, String status, String content);

    Task searchById(Integer id);

    boolean insertTask(Task task);

    boolean joinTask(Integer taskId, Integer volId);

    Task getParticipantList(Integer id);

    boolean agreeTask(Integer id);

    boolean accomplishTask(Integer id);

    List<Integer> getTaskChartData();

    Integer getSize();

}
