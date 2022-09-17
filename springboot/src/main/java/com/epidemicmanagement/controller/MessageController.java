package com.epidemicmanagement.controller;

import com.epidemicmanagement.config.Result;
import com.epidemicmanagement.entity.Message;
import com.epidemicmanagement.entity.Task;
import com.epidemicmanagement.service.MessageService;
import com.epidemicmanagement.service.TaskService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author XUE
 */
@Controller
@RequestMapping(value = "/api")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @ResponseBody
    @GetMapping(value = "/getMsgList")
    public Result getMsgList(@RequestParam("pageNum") Integer pageNum,
                              @RequestParam("pageSize") Integer pageSize) {
        PageInfo<Message> messagePageInfo = messageService.selectAllMsg(pageNum, pageSize);
        return Result.success(200, "查询成功").setAttribute("info", messagePageInfo);
    }

    @ResponseBody
    @PostMapping(value = "/publishMsg")
    public Result publishMsg(@RequestBody Message message) {
        boolean flag = messageService.insertMsg(message);
        if (flag) {
            return Result.success(200, "插入成功");
        } else {
            return Result.fail(500, "插入失败");
        }
    }

}
