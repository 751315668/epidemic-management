package com.epidemicmanagement.controller;

import com.epidemicmanagement.config.Result;
import com.epidemicmanagement.dao.TaskDao;
import com.epidemicmanagement.entity.Task;
import com.epidemicmanagement.entity.Volunteer;
import com.epidemicmanagement.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author XUE
 */
@Controller
@RequestMapping(value = "/api")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @ResponseBody
    @GetMapping(value = "/getTaskList")
    public Result getTaskList(@RequestParam("pageNum") Integer pageNum,
                              @RequestParam("pageSize") Integer pageSize) {
        PageInfo<Task> taskPageInfo = taskService.selectAllTask(pageNum, pageSize);
        return Result.success(200, "查询成功").setAttribute("info", taskPageInfo);
    }

    @ResponseBody
    @GetMapping(value = "/searchTask")
    public Result searchTask(@RequestParam("pageNum") Integer pageNum,
                             @RequestParam("pageSize") Integer pageSize,
                             @RequestParam("status") String status,
                             @RequestParam("input") String content) {
        PageInfo<Task> taskPageInfo = taskService.searchTask(pageNum, pageSize, status, content);
        return Result.success(200, "查询成功").setAttribute("info", taskPageInfo);
    }

    @ResponseBody
    @GetMapping(value = "/searchById")
    public Result searchById(@RequestParam Integer id) {
        Task task = taskService.searchById(id);
        if (task != null) {
            return Result.success(200, "查询成功").setAttribute("task", task);
        } else {
            return Result.fail(500, "查询失败");
        }
    }

    @ResponseBody
    @PostMapping(value = "/publishTask")
    public Result publishTask(@RequestBody Task task) {
        boolean flag = taskService.insertTask(task);
        if (flag) {
            return Result.success(200, "插入成功");
        } else {
            return Result.fail(500, "插入失败");
        }
    }

    @ResponseBody
    @PostMapping(value = "/joinTask")
    public Result joinTask(@RequestBody Map<String, Object> map) {
        Integer taskId = (Integer) map.get("taskId");
        Integer volId = (Integer) map.get("volId");
        boolean flag = taskService.joinTask(taskId, volId);
        if (flag) {
            return Result.success(200, "插入成功");
        } else {
            return Result.fail(500, "插入失败");
        }
    }

    @ResponseBody
    @PostMapping(value = "/agreeTask")
    public Result agreeTask(@RequestBody Map<String, Object> map) {
        Integer taskId = (Integer) map.get("taskId");
        boolean flag = taskService.agreeTask(taskId);
        if (flag) {
            return Result.success(200, "审核成功");
        } else {
            return Result.fail(500, "审核失败");
        }
    }

    @ResponseBody
    @PostMapping(value = "/accomplishTask")
    public Result accomplishTask(@RequestBody Map<String, Object> map) {
        Integer taskId = (Integer) map.get("taskId");
        boolean flag = taskService.accomplishTask(taskId);
        if (flag) {
            return Result.success(200, "操作成功");
        } else {
            return Result.fail(500, "操作失败");
        }
    }

    @ResponseBody
    @GetMapping(value = "/getParticipant")
    public Result getParticipant(@RequestParam Integer id) {
        Task task = taskService.getParticipantList(id);
        if (task != null) {
            return Result.success(200, "查询成功").setAttribute("volList", task.getVolunteerList());
        } else {
            return Result.fail(200, "没有参与的志愿者");
        }
    }

}
